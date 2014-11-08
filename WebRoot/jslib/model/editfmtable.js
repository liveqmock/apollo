/**
 *封面表表单
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
				  fieldLabel:'企业名称',
				  name:'qymc',
				  allowBlank:false,
				  value:'',
				  blankText:'请填写企业名称!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'制表人',
				  name:'zbr',
				  allowBlank:false,
				  value:'',
				  blankText:'请填写制表人名称!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'联系电话(区号)',
				  name:'qh',
				  allowBlank:true,
				  value:'',
				  blankText:'',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'联系电话(固话)',
				  name:'lxdh',
				  allowBlank:false,
				  value:'',
				  blankText:'联系电话不能为空!',
				  anchor:'95%'
				},{
				  xtype:'textfield',
				  fieldLabel:'联系电话(分机)',
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
 *封面表窗体
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
					msg('系统提示','更新信息出现异常!',Ext.MessageBox.ERROR);
				}
	
			} catch (_err) {
				msg('系统提示!', _err.description, Ext.MessageBox.ERROR);
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
	title : '更新数据信息',
	load : function(_id) {
		var url = "/hbsaserver/servlet/model?operation=loadDataByUnitid";
		var _fs = this.form;
		_fs.getForm().load(
			{
				url:url, 
				waitTitle:'系统提示',
				waitMsg:'正在加载',
				method:'get',
				params:{unitid:_id},
				success:function(){
					
				},
				failure:function(){
					msg('系统提示!','加载数据出现异常请联系管理员!', Ext.MessageBox.ERROR);
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
		  	 				msg('系统提示!','编辑封面表信息成功!', Ext.MessageBox.INFO);
		  	 				location.reload();
		  	 			},
		  	 			failure:function(action,form){
		  	 				msg('系统提示!','编辑封面表信息失败!', Ext.MessageBox.INFO);
		  	 			}
		  	 		}
		  	 	);
		  });
	}
	
}