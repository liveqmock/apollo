Ext.ns('Yotop');
/**
 *封面表表单
 **/
Yotop.UnitMetaForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		Yotop.UnitMetaForm.superclass.constructor.call(this,{
			renderTo:Ext.getBody(),	
			labelAlign : 'left',
			frame : true,
			height:300,
			autoScroll:true,
			items:[
				{
				  xtype:'textfield',
				  fieldLabel:'企业名称',
				  name:'qymc',
				  allowBlank:false,
				  value:'',
				  blankText:'请填写企业名称!',
				  anchor:'90%'
				},{
				  xtype:'textfield',
				  fieldLabel:'制表人',
				  name:'zbr',
				  allowBlank:false,
				  value:'',
				  blankText:'请填写制表人名称!',
				  anchor:'90%'
				},{
                        xtype: 'compositefield',
                        fieldLabel: '联系电话',
                        // anchor    : '-20',
                        // anchor    : null,
                        msgTarget: 'under',
                        items: [
                            {xtype: 'displayfield', value: '区号('},
                            {xtype: 'textfield',    name: 'qh', width: 29},
                            {xtype: 'displayfield', value: ')'},
                            {xtype: 'displayfield', value: '电话'},
                            {xtype: 'textfield',    name: 'lxdh', width: 100, margins: '0 5 0 0',blankText:'联系电话不能为空!',allowBlank:false},
                            {xtype: 'displayfield', value: '分机'},
                            {xtype: 'textfield',    name: 'fj', width: 48}
                        ]
                    },{
                    	xtype:'combo',
                        fieldLabel: '所在地区',
                        hiddenName:'szdq',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data :dict_area
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },
                    {
                    	xtype:'combo',
                        fieldLabel: '所属行业',
                        hiddenName:'sshy',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data :hydict
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },
                    {
                    	xtype:'combo',
                        fieldLabel: '经营规模',
                        hiddenName:'jygm',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data : [{value:1,text:'大型'},{value:2,text:'中型'},{value:3,text:'小型'}]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '组织形式',
                        hiddenName:'zzxs',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data : [
                            	{value:1,text:'独资公司'},{value:2,text:'非公司制独资企业'},{value:3,text:'上市股份有限公司'},
                            	{value:4,text:'非上市股份有限公司'},{value:5,text:'有限责任公司'},{value:6,text:'股份合作制企业'},
                            	{value:7,text:'合资或合营企业'},{value:8,text:'企业化管理事业单位'},{value:9,text:'其他'}
                            ]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '新报因素',
                        hiddenName:'xbys',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
							 data : [
								{value:3,text:'新设合并'},
								{value:6,text:'报表类别改变'},
								{value:8,text:'其他'},
								{value:2,text:'竣工移交'},
								{value:7,text:'划转'},
								{value:5,text:'上月应报未报'},
								{value:1,text:'新投资设立'},
								{value:0,text:'连续上报'},
								{value:4,text:'分立'}
							]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '报表类型',
                        hiddenName:'bblx',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
							data : [
								{value:'3',text:'境外并企业表'},
								{value:'7',text:'完全汇总表'},
								{value:'4',text:'事业并企业表'},
								{value:'0',text:'单户报表'},
								{value:'1',text:'集团差额表'},
								{value:'H',text:'选择汇总表'},
								{value:'5',text:'基建并企业表'},
								{value:'2',text:'金融并企业表'},
								{value:'9',text:'集团合并表'}
							]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择',
                        selectOnFocus:true,
                        width:190
                    },{xtype:'hidden',name:'unitid'}
			],
			 buttons: [
            {
                text   : '保存',
                handler: function() {
                	var _form = this.getForm();
                    if (_form.isValid()) {
                        var url = "../servlet/model?operation=saveUnitByUnitid";
				  	 	_form.submit(
				  	 		{
				  	 			method:'get',
				  	 			url:url,
				  	 			success:function(action,form){
//				  	 				msg('系统提示!','编辑封面表信息成功!', Ext.MessageBox.INFO);
				  	 				
				  	 				Ext.Msg.show(
				  	 					{
				  	 						title:'系统提示',
				  	 						msg:'编辑封面表信息成功!点击[OK]后将重新加载页面',
				  	 						icon:Ext.Msg.INFO,
				  	 						width:200,
				  	 						fn:function(){
				  	 							location.reload();
				  	 						},
				  	 						buttons:Ext.Msg.OK
				  	 					}
				  	 				);
				  	 			},
				  	 			failure:function(action,form){
				  	 				msg('系统提示!','编辑封面表信息失败!请联系管理员!', Ext.MessageBox.INFO);
				  	 			}
				  	 		}
				  	 	);
                    }
                },
                scope:this
            },
            
            {
                text   : '重置',
                handler: function() {
                    this.reset();
                },
                scope:this
            }
        ]
		})
	},
	getValue:function(){
		return new Ext.data.Record(this.getForm().getValues());
	},
	setValue:function(_r){
		this.getForm().loadRecord(_r);
	},
	reset:function(){
		this.getForm().reset();
	}
});

var loadForm = function(id,formpanel) {
	var url = "/hbsaserver/servlet/model?operation=loadDataByUnitid";
	formpanel.getForm().load(
		{
			url:url, 
			waitTitle:'系统提示',
			waitMsg:'正在加载',
			method:'get',
			params:{unitid:id},
			success:function(){
				
			},
			failure:function(){
				msg('系统提示!','加载数据出现异常请联系管理员!', Ext.MessageBox.ERROR);
				return;
			}
		}
	);
}
var init = function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../jslib/ext/resources/images/default/s.gif';
	var center = new Yotop.UnitMetaForm();
	loadForm(unitID,center);
}
Ext.onReady(init);
/***
 * 使用加载combox加载插件
 * var cm = new Ext.grid.ColumnModel([
			{header:'编号',dataIndex:'id',menuDisabled:true},
			{header:'性别',dataIndex:'name',menuDisabled:true},
			{header:'名称',dataIndex:'descn',menuDisabled:true},
			{header:'描述',dataIndex:'date',menuDisabled:true}
		]);
		var url = '../servlet/task?operation=getDictStore';
		var store = new Ext.data.JsonStore(
			{
				url:url,
				totalProperty:'totalCount',
				baseParams:{start:0,limit:20},
				root:'data',
				autoLoad:true,
				fields:["id","name","descn","date"]
			}
		);
      	var c = new Ext.form.ComboBox({  
		    typeAhead : true,  
		    fieldLabel : '名称',  
		    hiddenName : 'id',  
		    triggerAction : 'all',  
		    lazyRender : true,  
		    width:300,  
		    displayField:'name',  
		    valueField:'id',  
		    store:store,  
		    mode : 'local',  
		    listClass : 'x-combo-list-small',  
		    selectedClass:'',   
		    allowBlank : false,  
		    emptyText:'请选择...',  
		    cm:cm,  
		    onSelect:function(record,rowIndex){  
		     c.setValue(record.get("id"));  
		     c.setRawValue(record.get("name"));  
		    },  
		    plugins : [new Ext.plugins.GridCombox()]  
		   });  
 */