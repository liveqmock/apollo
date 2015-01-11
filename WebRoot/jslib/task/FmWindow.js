Ext.ns('Yotop');
/**
 *������
 **/
Yotop.UnitMetaForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		var cm = new Ext.grid.ColumnModel([
			{header:'���',dataIndex:'id',menuDisabled:true},
			{header:'�Ա�',dataIndex:'name',menuDisabled:true},
			{header:'����',dataIndex:'descn',menuDisabled:true},
			{header:'����',dataIndex:'date',menuDisabled:true}
		]);
		var url = '../../servlet/task?operation=getDictStore';
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
		/*
			var store = new Ext.data.Store({  
	        proxy: new Ext.data.HttpProxy({url:'js/8.jsp'}),  
				data:{
					totalCount:20,
					items:[
						{id:1,name:'AA',descn:'AAA',date:'2014-11-16'},
						{id:2,name:'BB',descn:'AAA',date:'2014-11-17'},
						{id:3,name:'CC',descn:'AAA',date:'2014-11-18'},
						{id:4,name:'DD',descn:'AAA',date:'2014-11-19'}
					]
				},
	        reader: new Ext.data.JsonReader({  
	            totalProperty: 'totalCount',  
	            root: 'items',  
	            id:id
	        }, [  
	          {name: 'id', type: 'int'},  
	          {name: 'name', type: 'string'},  
	          {name: 'descn', type: 'string'},  
	          {name: 'date', type: 'string'}  
	        ]),  
	        baseParams:{  
	         start:0,limit:10  
	        }  
	    });   
	    */
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
		
		
		
		Yotop.UnitMetaForm.superclass.constructor.call(this,{
			labelAlign : 'left',
			frame : true,
			autoScroll:true,
			items:[
			c,
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
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(����)',
				  name:'qh',
				  allowBlank:true,
				  value:'',
				  blankText:'',
				  anchor:'90%'
				},{
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(�̻�)',
				  name:'lxdh',
				  allowBlank:false,
				  value:'',
				  blankText:'��ϵ�绰����Ϊ��!',
				  anchor:'90%'
				},{
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(�ֻ�)',
				  name:'fj',
				  allowBlank:true,
				  value:'',
				  blankText:'',
				  anchor:'90%'
				},
				{xtype:'hidden',name:'unitid'}
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
/**
 *�������
 */
Yotop.UnitMetaWindow = Ext.extend(Ext.Window,
	{
		form : null,
		constructor:function(){
			this.form = new Yotop.UnitMetaForm();
			this.addEvents('submit');
			Yotop.UnitMetaWindow.superclass.constructor.call(this,
				{
					plain : true,
					autoScroll : true,
					width : 500,
					height : 350,
					autoShow:true, 
					modal : true,
//					maximizable:true,
					layout: 'fit',
					items : this.form,
					closeAction : 'hide',
					buttonAlign : 'center',
					listeners:{'hide':function(){this.close();},scope:this},
					buttons : [{
						text : 'ȷ��',
						handler : this.onSubmitClick,
						scope : this
					}, {
						text : 'ȡ��',
						handler : this.onCancelClick,
						scope : this
					}]
				}
			)
		},
		close : function() {
			this.form.reset();
			this.hide();
		},
		onCancelClick : function() {
			this.close();
		},
		onSubmitClick : function() {
			try {
				var valflag = this.form.getForm().isValid();
				if (valflag) {
					this.fireEvent('submit', this, this.form.getValue());
				} else {
					msg('ϵͳ��ʾ','������Ϣ�����쳣!',Ext.MessageBox.ERROR);
				}
	
			} catch (_err) {
				msg('ϵͳ��ʾ!', _err.description, Ext.MessageBox.ERROR);
				return;
			}
		},
		onCanelClick : function() {
			this.close();
		}
		
	}
)
//
/***/
Yotop.UpdateUnitMetaWindow = Ext.extend(Yotop.UnitMetaWindow, {
	title : '���·����������Ϣ',
	load : function(_id) {
		var url = "/hbsaserver/servlet/model?operation=loadDataByUnitid";
		var _fs = this.form;
		_fs.getForm().load(
			{
				url:url, 
				waitTitle:'ϵͳ��ʾ',
				waitMsg:'���ڼ���',
				method:'get',
				params:{unitid:_id},
				success:function(){
					
				},
				failure:function(){
					msg('ϵͳ��ʾ!','�������ݳ����쳣����ϵ����Ա!', Ext.MessageBox.ERROR);
					return;
				}
			}
		);
	}
});

//edit fm  table 
var openFmTable = function(unitid){
	//open and load fmtable 
	if(unitid){
	  var url = "/hbsaserver/servlet/model?operation=saveUnitByUnitid";
	  var updateWin = new UpdateUnitMetaWindow();
		  updateWin.show();
		  updateWin.load(unitid);
		  updateWin.on('submit',function(win,record){
		  	 var _form = win.form.getForm();
		  	 	_form.submit(
		  	 		{
		  	 			method:'get',
		  	 			url:url,
		  	 			success:function(action,form){
		  	 				msg('ϵͳ��ʾ!','�༭�������Ϣ�ɹ�!', Ext.MessageBox.INFO);
		  	 				location.reload();
		  	 			},
		  	 			failure:function(action,form){
		  	 				msg('ϵͳ��ʾ!','�༭�������Ϣʧ��!', Ext.MessageBox.INFO);
		  	 			}
		  	 		}
		  	 	);
		  });
	}
}