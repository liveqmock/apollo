Ext.ns('Yotop');
Yotop.FmGrid = function(){
	window.openEditWindow = function(){
		//打开窗体
		try{
			var winUnitMeta = new Yotop.UnitMetaWindow();
			winUnitMeta.show();		
		}catch(e){
			alert(e.description);
		}

	}
	var store = new Ext.data.JsonStore(
		{
			data:[{text:'单位',dwmc:'单位名称',dwdm:'单位代码',actioni:'inbox'}],
			fields:['text','dwmc','dwdm','actioni']
		}
	);
	var column = [];
	/**
	 * 列数据动作
	 */
	var action=new Ext.ux.grid.RowActions({
		header:'更新信息',
		width : 200,
		actions:[
			{
				width:200,
//				text:'查看规则',
				iconCls:'inbox',
				qtip : '查看规则',
				iconIndex:'actioni',
				qtipIndex:'actioni'
			},{},{},{}
		]
	});
	action.on({
		action:function(grid, record, action, row, col) {
			alert("1");
		}
		,beforeaction:function() {
			alert("2");
		}
		,beforegroupaction:function() {
			alert("3");
		}
		,groupaction:function(grid, records, action, groupId) {
			alert("4");
		}
	});
	var config = {
		autoHeight : true,
		height:800,
		width:1000,
		border:false,
		store : store,
		tbar:[
			{
				text:'新增信息',
				iconCls:'inbox'
			}
		],
		plugins:action,
		colModel : new Ext.grid.ColumnModel(
			{
//				defaults : {
//				width : 300,
//				sortable : true
//			},
			columns : [{
				id : 'company',
				header : '企业名称',
				width : 200,
				sortable : true,
				dataIndex : 'text'
			},{
				header : '单位名称',
				width : 200,
				sortable : true,
				dataIndex : 'dwmc'
			},{
				header : '单位代码',
				width : 200,
				sortable : true,
				dataIndex : 'dwdm'
			},{
				header : '编辑',
				width : 200,
				sortable : true,
				dataIndex : 'dwdm',
				renderer:function(){
					return '<a href="#" onclick="openEditWindow()">编辑</a>';
				}
			},action]
			}
		)
	};
	Yotop.FmGrid.superclass.constructor.call(this,config);
}
Ext.extend(Yotop.FmGrid,Ext.grid.GridPanel);
//Ext.extend(Ext.grid.GridPanel,Yotop.FmGrid);