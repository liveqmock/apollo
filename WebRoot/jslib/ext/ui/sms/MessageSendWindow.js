/**
 *���ŷ��ʹ���
 ***/
MessageSendForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		MessageSendForm.superclass.constructor.call(this,{
			labelAlign : 'left',
			frame : true,
			autoScroll:true,
			items:[
				{
				  xtype:'textfield',
				  fieldLabel:'�ռ���',
				  name:'mrecepersonname',
				  allowBlank:false,
				  value:'��',
				  blankText:'��λ����Ϊ������',
				  anchor:'95%'
				},
				{
				  xtype:'numberfield',
				  fieldLabel:'�ֻ�����',
				  name:'mtelno',
				  maxLength:11,
				  maxLengthText:'���ܶ���11������',
				  minLength:11,
				  minLengthText:'��������11������',
				  allowBlank:false,
				  blankText:'�ֻ�����Ϊ���������ָ�ʽ',
				  anchor:'95%'
				},
				{
				  xtype:'textarea',
				  fieldLabel:'��������',
				  name:'mcontent',
				  allowBlank:false,
				  blankText:'�������ݲ���Ϊ��!',
				  anchor:'95%'
				},
				{xtype:'hidden',name:'mid'}
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
 *���ŷ��ʹ���
 */
MessageSendWindow = Ext.extend(Ext.Window,
	{
		form : null,
		constructor:function(){
			this.form = new MessageSendForm();
			this.addEvents('submit');
			MessageSendWindow.superclass.constructor.call(this,
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
/*********����MessageSendWindow����**************/
/***/
InsertMessageSendWindow = Ext.extend(MessageSendWindow, {
	title : '���ŷ���'
});
/***/
UpdateMessageSendWindow = Ext.extend(MessageSendWindow, {
	title : '���ŷ���',
	load : function(_r) {
		this.form.setValue(_r);
	}
});