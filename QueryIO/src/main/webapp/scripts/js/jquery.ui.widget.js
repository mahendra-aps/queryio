(function(c){"function"===typeof define&&define.amd?define(["jquery"],c):c(jQuery)})(function(c,h){if(c.cleanData){var j=c.cleanData;c.cleanData=function(a){for(var b=0,d;null!=(d=a[b]);b++)try{c(d).triggerHandler("remove")}catch(e){}j(a)}}else{var k=c.fn.remove;c.fn.remove=function(a,b){return this.each(function(){b||(!a||c.filter(a,[this]).length)&&c("*",this).add([this]).each(function(){try{c(this).triggerHandler("remove")}catch(a){}});return k.call(c(this),a,b)})}}c.widget=function(a,b,d){var e=
a.split(".")[0],f;a=a.split(".")[1];f=e+"-"+a;d||(d=b,b=c.Widget);c.expr[":"][f]=function(b){return!!c.data(b,a)};c[e]=c[e]||{};c[e][a]=function(a,c){arguments.length&&this._createWidget(a,c)};b=new b;b.options=c.extend(!0,{},b.options);c[e][a].prototype=c.extend(!0,b,{namespace:e,widgetName:a,widgetEventPrefix:c[e][a].prototype.widgetEventPrefix||a,widgetBaseClass:f},d);c.widget.bridge(a,c[e][a])};c.widget.bridge=function(a,b){c.fn[a]=function(d){var e="string"===typeof d,f=Array.prototype.slice.call(arguments,
1),g=this;d=!e&&f.length?c.extend.apply(null,[!0,d].concat(f)):d;if(e&&"_"===d.charAt(0))return g;e?this.each(function(){var b=c.data(this,a),e=b&&c.isFunction(b[d])?b[d].apply(b,f):b;if(e!==b&&e!==h)return g=e,!1}):this.each(function(){var e=c.data(this,a);e?e.option(d||{})._init():c.data(this,a,new b(d,this))});return g}};c.Widget=function(a,b){arguments.length&&this._createWidget(a,b)};c.Widget.prototype={widgetName:"widget",widgetEventPrefix:"",options:{disabled:!1},_createWidget:function(a,b){c.data(b,
this.widgetName,this);this.element=c(b);this.options=c.extend(!0,{},this.options,this._getCreateOptions(),a);var d=this;this.element.bind("remove."+this.widgetName,function(){d.destroy()});this._create();this._trigger("create");this._init()},_getCreateOptions:function(){return c.metadata&&c.metadata.get(this.element[0])[this.widgetName]},_create:function(){},_init:function(){},destroy:function(){this.element.unbind("."+this.widgetName).removeData(this.widgetName);this.widget().unbind("."+this.widgetName).removeAttr("aria-disabled").removeClass(this.widgetBaseClass+
"-disabled ui-state-disabled")},widget:function(){return this.element},option:function(a,b){var d=a;if(0===arguments.length)return c.extend({},this.options);if("string"===typeof a){if(b===h)return this.options[a];d={};d[a]=b}this._setOptions(d);return this},_setOptions:function(a){var b=this;c.each(a,function(a,c){b._setOption(a,c)});return this},_setOption:function(a,b){this.options[a]=b;"disabled"===a&&this.widget()[b?"addClass":"removeClass"](this.widgetBaseClass+"-disabled ui-state-disabled").attr("aria-disabled",
b);return this},enable:function(){return this._setOption("disabled",!1)},disable:function(){return this._setOption("disabled",!0)},_trigger:function(a,b,d){var e,f=this.options[a];d=d||{};b=c.Event(b);b.type=(a===this.widgetEventPrefix?a:this.widgetEventPrefix+a).toLowerCase();b.target=this.element[0];if(a=b.originalEvent)for(e in a)e in b||(b[e]=a[e]);this.element.trigger(b,d);return!(c.isFunction(f)&&!1===f.call(this.element[0],b,d)||b.isDefaultPrevented())}}});