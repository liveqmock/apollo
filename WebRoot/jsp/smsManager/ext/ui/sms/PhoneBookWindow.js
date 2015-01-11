/**
 *�绰���� 
 ***/
PhoneBookForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		PhoneBookForm.superclass.constructor.call(this,{
			labelAlign : 'left',
			frame : true,
			autoScroll:true,
			items:[
				{
				  xtype:'textfield',
				  fieldLabel:'��λ����',
				  name:'companyname',
				  allowBlank:false,
				  value:'��',
				  blankText:'��λ����Ϊ������',
				  anchor:'95%'
				},
				{
				  xtype:'textfield',
				  fieldLabel:'����',
				  name:'personname',
				  allowBlank:false,
				  blankText:'����Ϊ������',
				  anchor:'95%'
				},
				{
				  xtype:'numberfield',
				  fieldLabel:'�ֻ�����',
				  name:'telno',
				  maxLength:11,
				  maxLengthText:'���ܶ���11������',
				  minLength:11,
				  minLengthText:'��������11������',
				  allowBlank:false,
				  blankText:'�ֻ�����Ϊ���������ָ�ʽ',
				  anchor:'95%'
				},
				{xtype:'hidden',name:'id'}
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
 *�绰���������� 
 */
PhoneBookWindow = Ext.extend(Ext.Window,
	{
		form : null,
		constructor:function(){
			this.form = new PhoneBookForm();
			this.addEvents('submit');
			PhoneBookWindow.superclass.constructor.call(this,
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
					msg('ϵͳ��ʾ', '����д��ȷ��Ϣ��', Ext.MessageBox.ERROR);
				}
	
			} catch (_err) {
				msg('ϵͳ��ʾ', _err.description, Ext.MessageBox.ERROR);
				return;
			}
		},
		onCanelClick : function() {
			this.close();
		}
		
	}
)
/*********����PhoneBookWindow����**************/
/***/
InsertPhoneBookWindow = Ext.extend(PhoneBookWindow, {
	title : '���ͨѶ¼'
});
/***/
UpdatePhoneBookWindow = Ext.extend(PhoneBookWindow, {
	title : '�޸�ͨѶ¼',
	load : function(_r) {
		this.form.setValue(_r);
	}
});