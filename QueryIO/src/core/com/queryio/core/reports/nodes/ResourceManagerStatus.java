package com.queryio.core.reports.nodes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.queryio.common.EnvironmentalConstants;
import com.queryio.common.IProductConstants;
import com.queryio.common.charts.independent.AppChart;
import com.queryio.common.charts.independent.AppPieChart;
import com.queryio.common.charts.independent.ChartConstants;
import com.queryio.common.charts.interfaces.UserInterface;
import com.queryio.common.charts.properties.ChartPropertiesManager;
import com.queryio.common.charts.util.UIGraphicsFactory;
import com.queryio.common.exporter.dstruct.AbstractExportableNode;
import com.queryio.common.exporter.dstruct.Chart;
import com.queryio.common.exporter.dstruct.Color;
import com.queryio.common.exporter.dstruct.Group;
import com.queryio.common.exporter.dstruct.IExportableItem;
import com.queryio.common.exporter.dstruct.Label;
import com.queryio.common.exporter.dstruct.Table;
import com.queryio.core.monitor.beans.Parameter;
import com.queryio.core.monitor.beans.SummaryTable;
import com.queryio.core.monitor.managers.SummaryManager;

public class ResourceManagerStatus extends AbstractExportableNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6757527434993431105L;
	private Timestamp currentTimestamp; 
	private Timestamp startTimestamp;
	private Timestamp endTimestamp;
	private static UserInterface userInterface = UIGraphicsFactory.getUserInterface(IProductConstants.USER_INTERFACE_SWING);
	String title;
	public ResourceManagerStatus(String exportedFileName, Timestamp startTimestamp, Timestamp endTimestamp, String title) {
		super("ResourceManager Status Report", 0, "ResourceManager Status Report", exportedFileName, "ResourceManager Status Report");
		this.currentTimestamp = new Timestamp(new Date().getTime());
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.setFilePath(EnvironmentalConstants.getReportsDirectory());
		this.title = title;
	}
	@Override
	public IExportableItem[] getItems() {
		
		SummaryTable st = SummaryManager.getAllResourceManagersSummaryTable(true);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, yyyy-MM-dd hh:mm a");		
		IExportableItem[] items = new IExportableItem[]
			{
				
				new Label(0, 0, 100, 20, " Generated On: "+sdf.format(currentTimestamp.getTime())),
				new Label(0, 0, 100, 20, " Report Filter:"),
				new Label(0, 0, 100, 20, " Start Time: "+ sdf.format(startTimestamp.getTime())+"  End Time: "+ sdf.format(endTimestamp.getTime())),
				new Label(0, 0, 100, 20, " "),
				new Group(0, 0, 100, 50, "", new IExportableItem[]
					{				
						new Chart(0, 0, 49, 30, getPieChart()),
						new Table(50, 0, 49, 30, getStatusSummaryTableModel(), new int[] { 50 }, null)
					}),
				new Table(0, 0, 99, 50, getTableSummaryModel(st), getColumnSize(st), "ResourceManagers Summary")
			};
		return items;
	}

	private int[] getColumnSize(SummaryTable st) {
		int result[] = new int[st.getColNames().size()-1];
		for(int i = 0; i < result.length; i ++){
			result[i] = 100/result.length;
		}
		return result;
	}

	private TableModel getTableSummaryModel(SummaryTable st) {		
		ArrayList colNameList = st.getColNames();
		DefaultTableModel tableModel = new DefaultTableModel();
		for(int i=0;i<colNameList.size()-1;i++){
			tableModel.addColumn((String)colNameList.get(i));
		}
		Object o[];
		for(int i = 0; i < st.getRows().size(); i ++){			
			ArrayList list = (ArrayList) st.getRows().get(i);
			o = new Object[list.size()-1];
			for(int j = 0; j < o.length; j ++){
				o[j] = list.get(j);
			}
			tableModel.addRow(o);
		}				
		return tableModel;		
	}
	private TableModel getStatusSummaryTableModel() {
		ArrayList clusterSumm = SummaryManager.getResourceManagerAppsDetailStatusSummary();

		String[] colNames = {"Status Summary"};
		DefaultTableModel tableModel = new DefaultTableModel(colNames, 0);
		
		Object o[];
		for(int i = 0; i < clusterSumm.size(); i ++){
			Parameter p = (Parameter) clusterSumm.get(i);
			o = new Object[1];
			o[0] = (String) p.getName() + " :  " + p.getValue();
			tableModel.addRow(o);
		}
		return tableModel;
		
	}
	private AppChart getPieChart() {
		ArrayList clusterInfo = SummaryManager.getResourceManagerAppsDetail();

		AppPieChart pieChart = new AppPieChart(userInterface, ChartPropertiesManager.TYPE_RUNTIME_CHARTS, IProductConstants.QUERYIO);
		pieChart.setBackgroundColor(ChartConstants.COLOR_WHITE);
		pieChart.setChartTitle("Application Summary");
		pieChart.showTitle(true);
		pieChart.showLegend(true);
		pieChart.showBorder(true);
		pieChart.setAlternateBackgroundColor(ChartConstants.COLOR_ALTERNATE);
		
		int appsRunning = (Integer) clusterInfo.get(0);
		int appsPending = (Integer) clusterInfo.get(1);
		int appsCompleted = (Integer) clusterInfo.get(2);
		int appsKilled = (Integer) clusterInfo.get(3);
		
		Double appsRunningPer = (Double) (appsRunning * 1.0);
		Double appsPendingPer = (Double) (appsPending * 1.0);
		Double appsCompletedPer = (Double) (appsCompleted * 1.0);
		Double appsKilledPer = (Double) (appsKilled * 1.0);
		
		if(!(appsRunningPer==0 && appsPendingPer==0 && appsCompletedPer==0 && appsKilledPer==0))
		{
			pieChart.initializeSeries(clusterInfo.size());
			while(appsRunningPer > Integer.MAX_VALUE)
			{
				appsRunningPer /= 100;
				appsPendingPer /= 100;
				appsCompletedPer /= 100;
				appsKilledPer /= 100;
			}
			
			pieChart.setSeriesName(0, "Running");
			pieChart.setSeriesValue(0, appsRunningPer.intValue());
			
			pieChart.setSeriesName(1, "Pending");
			pieChart.setSeriesValue(1, appsPendingPer.intValue());
			
			pieChart.setSeriesName(2, "Completed");
			pieChart.setSeriesValue(2, appsCompletedPer.intValue());
			
			pieChart.setSeriesName(3, "Killed");
			pieChart.setSeriesValue(3, appsKilledPer.intValue());
			
			pieChart.setSeriesColour(0, Color.decode("#CC9752"));
			pieChart.setSeriesColour(1, Color.decode("#996699"));
			pieChart.setSeriesColour(2, Color.decode("#FFCC00"));
			pieChart.setSeriesColour(3, Color.decode("#EAA228"));
		}
		else{
			pieChart.initializeSeries(1);
			pieChart.setSeriesName(0, "No Data");
			pieChart.setSeriesValue(0, 100);
			pieChart.setSeriesColour(0, Color.decode("#808080"));
		}
		return pieChart;		
	}

	@Override
	public String getHeaderText() {
		return title;
	}
}
