Ext.ns('Yotop');
Yotop.FmGrid = function(){
	window.openEditWindow = function(){
		//�򿪴���
		try{
			var winUnitMeta = new Yotop.UnitMetaWindow();
			winUnitMeta.show();		
		}catch(e){
			alert(e.description);
		}

	}
	var store = new Ext.data.JsonStore(
		{
			data:[{text:'��λ',dwmc:'��λ����',dwdm:'��λ����',actioni:'inbox'}],
			fields:['text','dwmc','dwdm','actioni']
		}
	);
	var column = [];
	/**
	 * �����ݶ���
	 */
	var action=new Ext.ux.grid.RowActions({
		header:'������Ϣ',
		width : 200,
		actions:[
			{
				width:200,
//				text:'�鿴����',
				iconCls:'inbox',
				qtip : '�鿴����',
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
				text:'������Ϣ',
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
				header : '��ҵ����',
				width : 200,
				sortable : true,
				dataIndex : 'text'
			},{
				header : '��λ����',
				width : 200,
				sortable : true,
				dataIndex : 'dwmc'
			},{
				header : '��λ����',
				width : 200,
				sortable : true,
				dataIndex : 'dwdm'
			},{
				header : '�༭',
				width : 200,
				sortable : true,
				dataIndex : 'dwdm',
				renderer:function(){
					return '<a href="#" onclick="openEditWindow()">�༭</a>';
				}
			},action]
			}
		)
	};
	Yotop.FmGrid.superclass.constructor.call(this,config);
}
Ext.extend(Yotop.FmGrid,Ext.grid.GridPanel);
//Ext.extend(Ext.grid.GridPanel,Yotop.FmGrid);