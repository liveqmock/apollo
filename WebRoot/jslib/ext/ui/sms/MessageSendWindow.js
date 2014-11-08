/**
 *短信发送窗体
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
				  fieldLabel:'收件人',
				  name:'mrecepersonname',
				  allowBlank:false,
				  value:'无',
				  blankText:'单位名称为必填项',
				  anchor:'95%'
				},
				{
				  xtype:'numberfield',
				  fieldLabel:'手机号码',
				  name:'mtelno',
				  maxLength:11,
				  maxLengthText:'不能多余11个数字',
				  minLength:11,
				  minLengthText:'不能少于11个数字',
				  allowBlank:false,
				  blankText:'手机号码为必填项数字格式',
				  anchor:'95%'
				},
				{
				  xtype:'textarea',
				  fieldLabel:'短信内容',
				  name:'mcontent',
				  allowBlank:false,
				  blankText:'短信内容不能为空!',
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
 *短信发送窗体
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
						text : '确定',
						handler : this.onSubmitClick,
						scope : this
					}, {
						text : '取消',
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
					msg('系统提示', '请填写正确信息！', Ext.MessageBox.ERROR);
				}
	
			} catch (_err) {
				msg('系统提示', _err.description, Ext.MessageBox.ERROR);
				return;
			}
		},
		onCanelClick : function() {
			this.close();
		}
		
	}
)
/*********创建MessageSendWindow结束**************/
/***/
InsertMessageSendWindow = Ext.extend(MessageSendWindow, {
	title : '短信发送'
});
/***/
UpdateMessageSendWindow = Ext.extend(MessageSendWindow, {
	title : '短信发送',
	load : function(_r) {
		this.form.setValue(_r);
	}
});