Ext.ns('Yotop');
Yotop.ViewPort = function(){
	var west = new Ext.Panel({
		region:"west",
		id:"west-panel",
		title:"组织单位",
		width:180,
		autoScroll:true,
		layout:"fit",
		items:new Yotop.FmTreePanel()
	});
	var center = new Ext.Panel({
		id:"centerPanel",
		layout:'fit',
		region:"center",
		title:'封面表数据信息',
		items:new Yotop.FmGrid()
	});
	var config = {
		layout:'border',
		renderTo:Ext.getBody(),	
		items:[west,center]
	};
	Yotop.ViewPort.superclass.constructor.call(this,config);
}
Ext.extend(Yotop.ViewPort,Ext.Viewport);

var init = function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../../jslib/ext/resources/images/default/s.gif';
	var view = new Yotop.ViewPort();
}
Ext.onReady(init);