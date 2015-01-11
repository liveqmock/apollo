/**
 * ���ŷ�����ʷ��¼
 */
Ext.onReady(function(){
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../jsp/smsManager/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	/**
	 *���·�����Ϣ 
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
	            		waitTitle:'ϵͳ��ʾ',
						waitMsg:'��Ϣ���ڷ��ͣ����Ժ�...', 
	            		url:'smsoperation?operation=sendmsg',
	            		method:'POST',
	            		success:function(form, action){
	            			_form.findField('mcontent').setValue(_v);
	            			var _url = 'smsoperation?operation=findallreceivemsginfo';
	            			_store.proxy.setUrl(_url,true);
	            			var _param = {start:0,limit:20};
    						_store.baseParams = _param;
    						_store.load({params:_param});
	            			msg('ϵͳ��ʾ','���Ͷ��ųɹ�',Ext.MessageBox.INFO);
	            		},
	            		failure:function(form, action){
	            			msg('ϵͳ��ʾ','���Ͷ���ʧ��,ԭ�����緱æ!���Ժ�����!',Ext.MessageBox.ERROR);
	            		}
	            	});
    	})
	}
	/**
	 *ɾ��������Ϣ 
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
		Ext.MessageBox.confirm('ϵͳ��ʾ','�Ƿ�ȷ��ɾ��ѡ�и���¼?',
			function(btn){
				if(btn=='yes'){
					Ext.Ajax.request({
						method:'POST',
						params:{id:value,tablename:'msg_inbox'},
						url:'smsoperation?operation=deletereceivemsgbyid',
						success:function(resp,option){
							msg('ϵͳ��ʾ','ɾ�����ݳɹ�',Ext.MessageBox.INFO);
							_store.remove(rec);
							grid.getView().refresh();
						},
						failure:function(resp,option){
							msg('ϵͳ��ʾ','ɾ������ʧ��,ʧ��ԭ�����緱æ!���Ժ�����!',Ext.MessageBox.ERROR);
						}
					});
				}
		});
		
	}
	var _data = [
		{pid:'2',mid:'1',message:'�߱�����',mdate:'2010-01-01 10:02:12',receiver:'13312345678'}
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
            {header: '������', width: 75, sortable: true, dataIndex: 'phonepersonname',renderer:renderHallName},
            {header: '�����˵�λ', width: 75, sortable: true, dataIndex: 'phonecompanyname',renderer:renderHallName},
            {header: '�ֻ�����', width: 75, sortable: true,  dataIndex: 'phonetel',renderer:renderHallName},
            {header: '��Ϣ����ʱ��', width: 120, sortable: true,  dataIndex: 'msgarrivedtime',renderer:renderHallName},
            {id:'message',header: '��Ϣ����', width: 75, sortable: true,  dataIndex: 'msgtitle',renderer:renderHallName},
          	{header: '�ظ�����', width: 75, sortable: true,  dataIndex: 'id',renderer:function(value, meta, rec, rowIdx, colIdx, ds){
          			return '<a href="javascript:void(0)" onclick=resendmessage('+value+',\"'+rec.get('phonetel')+'\")>���Żظ�</a>';
          	}},
          	{header: 'ɾ����¼', width: 75, sortable: true,  dataIndex: 'id',renderer:function(value, meta, rec, rowIdx, colIdx, ds){
          			return '<a href="javascript:void(0)" onclick=deleterecord('+value+',\"'+rec.get('phonetel')+'\")>ɾ����¼</a>';
          	}}
          ],
        tbar:[
        	{
        		text:'����ռ����¼',
        		iconCls:'trash',
        		handler:function(){
        			Ext.MessageBox.confirm('ϵͳ��ʾ','�Ƿ�ȷ��Ҫ����ռ�����������,<br/><font color="red">������պ󽫲��ָܻ�</font>?',function(btn){
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
					   emptyText		:'�����������п���ģ������!',
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
							 *����������ģ����ѯ 
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
			displayMsg: '��{0} �� {1} ������ ��{2}��',   
        	emptyMsg:'û�м�¼'
    	}),
        stripeRows: true,
        autoExpandColumn: 'message',
        height: 350,
        width: 600,
        title: 'ϵͳ�ռ���',
        stateful: true,
        stateId: 'grid'        
    });
    var _view = new Ext.Viewport({
		renderTo:Ext.getBody(),
		items:[grid],
		layout:'fit'
	}); 
    
});