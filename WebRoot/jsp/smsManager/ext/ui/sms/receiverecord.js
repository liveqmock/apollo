/**
 * 短信发送历史记录
 */
Ext.onReady(function(){
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../jsp/smsManager/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	/**
	 *重新发送信息 
	 **/
	window.resendmessage = function(value,telno)
	{
		var rec = {};
		for (var i = 0; i < _store.getCount(); i++) {
			var _r = _store.getAt(i);
			if(value==_r.get('id'))
			{
				rec = _r;
				break;
			}
		}
		
		var _winsend = new UpdateMessageSendWindow();
		_winsend.show();
		var _winform = _winsend.form.getForm();
		_winform.findField('mrecepersonname').setValue(rec.get('phonepersonname'));
		_winform.findField('mtelno').setValue(rec.get('phonetel'));
		
//		_winsend.load(rec);
		_winsend.on('submit',function(win,record){
    		var _form = win.form.getForm();
    		var _v = _form.findField('mcontent').getValue();
    		var _telnos = _form.findField('mtelno').getValue();
    		_form.submit({
	            		params:{telnos:_telnos,message:encodeURI(_v)},
	            		waitTitle:'系统提示',
						waitMsg:'信息正在发送，请稍侯...', 
	            		url:'smsoperation?operation=sendmsg',
	            		method:'POST',
	            		success:function(form, action){
	            			_form.findField('mcontent').setValue(_v);
	            			var _url = 'smsoperation?operation=findallreceivemsginfo';
	            			_store.proxy.setUrl(_url,true);
	            			var _param = {start:0,limit:20};
    						_store.baseParams = _param;
    						_store.load({params:_param});
	            			msg('系统提示','发送短信成功',Ext.MessageBox.INFO);
	            		},
	            		failure:function(form, action){
	            			msg('系统提示','发送短信失败,原因网络繁忙!请稍后重试!',Ext.MessageBox.ERROR);
	            		}
	            	});
    	})
	}
	/**
	 *删除发送信息 
	 **/
	window.deleterecord = function(value,tablename)
	{
		var rec = {};
		for (var i = 0; i < _store.getCount(); i++) {
			var _r = _store.getAt(i);
			if(value==_r.get('id'))
			{
				rec = _r;
				break;
			}
		}
		Ext.MessageBox.confirm('系统提示','是否确定删除选中个记录?',
			function(btn){
				if(btn=='yes'){
					Ext.Ajax.request({
						method:'POST',
						params:{id:value,tablename:'msg_inbox'},
						url:'smsoperation?operation=deletereceivemsgbyid',
						success:function(resp,option){
							msg('系统提示','删除数据成功',Ext.MessageBox.INFO);
							_store.remove(rec);
							grid.getView().refresh();
						},
						failure:function(resp,option){
							msg('系统提示','删除数据失败,失败原因网络繁忙!请稍后重试!',Ext.MessageBox.ERROR);
						}
					});
				}
		});
		
	}
	var _data = [
		{pid:'2',mid:'1',message:'催报管理',mdate:'2010-01-01 10:02:12',receiver:'13312345678'}
	];
	var _store = new Ext.data.JsonStore({
//		data:_data,
		totalProperty: 'totalCount', 
		root: 'data',
		url	   : 'smsoperation?operation=findallreceivemsginfo',
//		autoLoad:true,
		fields:['id','phonepersonname','phonecompanyname','phonetel','msgarrivedtime','msgtitle','message']
	});
	_store.load({params:{start:0,limit:20}});
	var _num = new Ext.grid.RowNumberer();
	var grid = new Ext.grid.GridPanel({
		iconCls :'inbox',
        store: _store,
        columns: [
        	_num,
            {header: '发件人', width: 75, sortable: true, dataIndex: 'phonepersonname',renderer:renderHallName},
            {header: '发件人单位', width: 75, sortable: true, dataIndex: 'phonecompanyname',renderer:renderHallName},
            {header: '手机号码', width: 75, sortable: true,  dataIndex: 'phonetel',renderer:renderHallName},
            {header: '信息发送时间', width: 120, sortable: true,  dataIndex: 'msgarrivedtime',renderer:renderHallName},
            {id:'message',header: '信息内容', width: 75, sortable: true,  dataIndex: 'msgtitle',renderer:renderHallName},
          	{header: '回复短信', width: 75, sortable: true,  dataIndex: 'id',renderer:function(value, meta, rec, rowIdx, colIdx, ds){
          			return '<a href="javascript:void(0)" onclick=resendmessage('+value+',\"'+rec.get('phonetel')+'\")>短信回复</a>';
          	}},
          	{header: '删除记录', width: 75, sortable: true,  dataIndex: 'id',renderer:function(value, meta, rec, rowIdx, colIdx, ds){
          			return '<a href="javascript:void(0)" onclick=deleterecord('+value+',\"'+rec.get('phonetel')+'\")>删除记录</a>';
          	}}
          ],
        tbar:[
        	{
        		text:'清空收件箱记录',
        		iconCls:'trash',
        		handler:function(){
        			Ext.MessageBox.confirm('系统提示','是否确定要清空收件箱所有数据,<br/><font color="red">数据清空后将不能恢复</font>?',function(btn){
        				if(btn=='yes')
        				{
        					var _url='smsoperation?operation=deletereceiveallmsg';
			    				Ext.Ajax.request(
			    					{
			    					 params:{name:'msg_inbox'},
			    					 url:_url,
			    					 method:'POST',
			    					 success:function(resp,option){},
			    					 failure:function(resp,option){}
			    					}
			    				);
        				}
        			});
	        	}
        	},'-',
        	
    		new Ext.form.TwinTriggerField({
        		       validationEvent	:false,
					   validateOnBlur	:false,
					   emptyText		:'输入条件进行快速模糊查找!',
					   trigger1Class	:'x-form-clear-trigger',
					   trigger2Class	:'x-form-search-trigger',
					   hideTrigger1		:true,
					   width			:200,
					   hasSearch 		: false,
                       paramName 		: 'query',
                       listeners		:{'specialkey':function(f, e){
                       		if(e.getKey() == e.ENTER){
								this.onTrigger2Click();
							}
					   }},
                       onTrigger1Click 	: function(){
    						if(this.hasSearch){
        						this.el.dom.value = '';
        						this.triggers[0].hide();
        						this.hasSearch = false;
								this.focus();
    						}
						},
						onTrigger2Click : function(){
							/**
							 *按条件进行模糊查询 
							 ***/
    						var v = this.getRawValue();
	    					var _url='smsoperation?operation=findallreceivemsginfobycondtion';
	    					var _n = _store.baseParams.names;
	    					var _param = {}
	    					if(_n==undefined)
	    					{
	    						_param.names = 'yes',
	    						_param.start = 0;
	    						_param.limit = 20;
	    					}else{
	    						_param = _store.baseParams;
	    					}
	    					var _obj = {v:encodeURI(v)};
//    						var _param = _store.baseParams;
    						Ext.apply(_param,_obj);
    						_store.proxy.setUrl(_url,true);
    						_store.baseParams = _param;
    						_store.load({params:_param});
    						this.hasSearch = true;
    						this.triggers[0].show();
							this.focus();
						}			
					})],
        bbar:new Ext.PagingToolbar({
			width:400,
        	store:_store,
        	pageSize:20,
        	displayInfo: true,   
			displayMsg: '第{0} 到 {1} 条数据 共{2}条',   
        	emptyMsg:'没有记录'
    	}),
        stripeRows: true,
        autoExpandColumn: 'message',
        height: 350,
        width: 600,
        title: '系统收件箱',
        stateful: true,
        stateId: 'grid'        
    });
    var _view = new Ext.Viewport({
		renderTo:Ext.getBody(),
		items:[grid],
		layout:'fit'
	}); 
    
});