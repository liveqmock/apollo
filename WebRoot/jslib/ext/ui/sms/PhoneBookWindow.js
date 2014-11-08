/**
 *电话簿表单 
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
				  fieldLabel:'单位名称',
				  name:'companyname',
				  allowBlank:false,
				  value:'无',
				  blankText:'单位名称为必填项',
				  anchor:'95%'
				},
				{
				  xtype:'textfield',
				  fieldLabel:'姓名',
				  name:'personname',
				  allowBlank:false,
				  blankText:'姓名为必填项',
				  anchor:'95%'
				},
				{
				  xtype:'numberfield',
				  fieldLabel:'手机号码',
				  name:'telno',
				  maxLength:11,
				  maxLengthText:'不能多余11个数字',
				  minLength:11,
				  minLengthText:'不能少于11个数字',
				  allowBlank:false,
				  blankText:'手机号码为必填项数字格式',
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
 *电话簿基本窗体 
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
/*********创建PhoneBookWindow结束**************/
/***/
InsertPhoneBookWindow = Ext.extend(PhoneBookWindow, {
	title : '添加通讯录'
});
/***/
UpdatePhoneBookWindow = Ext.extend(PhoneBookWindow, {
	title : '修改通讯录',
	load : function(_r) {
		this.form.setValue(_r);
	}
});