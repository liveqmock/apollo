Ext.ns('Yotop');
/**
 *������
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
				  fieldLabel:'��ҵ����',
				  name:'qymc',
				  allowBlank:false,
				  value:'',
				  blankText:'����д��ҵ����!',
				  anchor:'90%'
				},{
				  xtype:'textfield',
				  fieldLabel:'�Ʊ���',
				  name:'zbr',
				  allowBlank:false,
				  value:'',
				  blankText:'����д�Ʊ�������!',
				  anchor:'90%'
				},{
                        xtype: 'compositefield',
                        fieldLabel: '��ϵ�绰',
                        // anchor    : '-20',
                        // anchor    : null,
                        msgTarget: 'under',
                        items: [
                            {xtype: 'displayfield', value: '����('},
                            {xtype: 'textfield',    name: 'qh', width: 29},
                            {xtype: 'displayfield', value: ')'},
                            {xtype: 'displayfield', value: '�绰'},
                            {xtype: 'textfield',    name: 'lxdh', width: 100, margins: '0 5 0 0',blankText:'��ϵ�绰����Ϊ��!',allowBlank:false},
                            {xtype: 'displayfield', value: '�ֻ�'},
                            {xtype: 'textfield',    name: 'fj', width: 48}
                        ]
                    },{
                    	xtype:'combo',
                        fieldLabel: '���ڵ���',
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
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },
                    {
                    	xtype:'combo',
                        fieldLabel: '������ҵ',
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
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },
                    {
                    	xtype:'combo',
                        fieldLabel: '��Ӫ��ģ',
                        hiddenName:'jygm',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data : [{value:1,text:'����'},{value:2,text:'����'},{value:3,text:'С��'}]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '��֯��ʽ',
                        hiddenName:'zzxs',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
                            data : [
                            	{value:1,text:'���ʹ�˾'},{value:2,text:'�ǹ�˾�ƶ�����ҵ'},{value:3,text:'���йɷ����޹�˾'},
                            	{value:4,text:'�����йɷ����޹�˾'},{value:5,text:'�������ι�˾'},{value:6,text:'�ɷݺ�������ҵ'},
                            	{value:7,text:'���ʻ��Ӫ��ҵ'},{value:8,text:'��ҵ��������ҵ��λ'},{value:9,text:'����'}
                            ]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '�±�����',
                        hiddenName:'xbys',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
							 data : [
								{value:3,text:'����ϲ�'},
								{value:6,text:'�������ı�'},
								{value:8,text:'����'},
								{value:2,text:'�����ƽ�'},
								{value:7,text:'��ת'},
								{value:5,text:'����Ӧ��δ��'},
								{value:1,text:'��Ͷ������'},
								{value:0,text:'�����ϱ�'},
								{value:4,text:'����'}
							]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },{
                    	xtype:'combo',
                        fieldLabel: '��������',
                        hiddenName:'bblx',
                        store: new Ext.data.JsonStore({
                            fields: ['value', 'text'],
							data : [
								{value:'3',text:'���Ⲣ��ҵ��'},
								{value:'7',text:'��ȫ���ܱ�'},
								{value:'4',text:'��ҵ����ҵ��'},
								{value:'0',text:'��������'},
								{value:'1',text:'���Ų���'},
								{value:'H',text:'ѡ����ܱ�'},
								{value:'5',text:'��������ҵ��'},
								{value:'2',text:'���ڲ���ҵ��'},
								{value:'9',text:'���źϲ���'}
							]
                        }),
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'��ѡ��',
                        selectOnFocus:true,
                        width:190
                    },{xtype:'hidden',name:'unitid'}
			],
			 buttons: [
            {
                text   : '����',
                handler: function() {
                	var _form = this.getForm();
                    if (_form.isValid()) {
                        var url = "../servlet/model?operation=saveUnitByUnitid";
				  	 	_form.submit(
				  	 		{
				  	 			method:'get',
				  	 			url:url,
				  	 			success:function(action,form){
//				  	 				msg('ϵͳ��ʾ!','�༭�������Ϣ�ɹ�!', Ext.MessageBox.INFO);
				  	 				
				  	 				Ext.Msg.show(
				  	 					{
				  	 						title:'ϵͳ��ʾ',
				  	 						msg:'�༭�������Ϣ�ɹ�!���[OK]�����¼���ҳ��',
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
				  	 				msg('ϵͳ��ʾ!','�༭�������Ϣʧ��!����ϵ����Ա!', Ext.MessageBox.INFO);
				  	 			}
				  	 		}
				  	 	);
                    }
                },
                scope:this
            },
            
            {
                text   : '����',
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
			waitTitle:'ϵͳ��ʾ',
			waitMsg:'���ڼ���',
			method:'get',
			params:{unitid:id},
			success:function(){
				
			},
			failure:function(){
				msg('ϵͳ��ʾ!','�������ݳ����쳣����ϵ����Ա!', Ext.MessageBox.ERROR);
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
 * ʹ�ü���combox���ز��
 * var cm = new Ext.grid.ColumnModel([
			{header:'���',dataIndex:'id',menuDisabled:true},
			{header:'�Ա�',dataIndex:'name',menuDisabled:true},
			{header:'����',dataIndex:'descn',menuDisabled:true},
			{header:'����',dataIndex:'date',menuDisabled:true}
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
		    fieldLabel : '����',  
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
		    emptyText:'��ѡ��...',  
		    cm:cm,  
		    onSelect:function(record,rowIndex){  
		     c.setValue(record.get("id"));  
		     c.setRawValue(record.get("name"));  
		    },  
		    plugins : [new Ext.plugins.GridCombox()]  
		   });  
 */