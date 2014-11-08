/**
 *������
 **/
UnitMetaForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		UnitMetaForm.superclass.constructor.call(this,{
			labelAlign : 'left',
			frame : true,
			autoScroll:true,
			items:[
				{
				  xtype:'textfield',
				  fieldLabel:'��ҵ����',
				  name:'qymc',
				  allowBlank:false,
				  value:'',
				  blankText:'����д��ҵ����!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'�Ʊ���',
				  name:'zbr',
				  allowBlank:false,
				  value:'',
				  blankText:'����д�Ʊ�������!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(����)',
				  name:'qh',
				  allowBlank:true,
				  value:'',
				  blankText:'',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(�̻�)',
				  name:'lxdh',
				  allowBlank:false,
				  value:'',
				  blankText:'��ϵ�绰����Ϊ��!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'��ϵ�绰(�ֻ�)',
				  name:'fj',
				  allowBlank:true,
				  value:'',
				  blankText:'',
				  anchor:'95%'
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
UnitMetaWindow = Ext.extend(Ext.Window,
	{
		form : null,
		constructor:function(){
			this.form = new UnitMetaForm();
			this.addEvents('submit');
			UnitMetaWindow.superclass.constructor.call(this,
				{
					plain : true,
					autoScroll : true,
					width : 400,
					height : 250,
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
UpdateUnitMetaWindow = Ext.extend(UnitMetaWindow, {
	title : '����������Ϣ',
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