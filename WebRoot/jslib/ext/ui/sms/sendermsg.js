
Ext.onReady(function(){

	
	Ext.form.Field.prototype.msgTarget = 'side';
	Ext.BLANK_IMAGE_URL = '../jsp/smsManager/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	
    var myData = {
		records : [
			
		]
	};


	// Generic fields array to use in both store defs.
	var fields = ['companyname','personname','telno','id'];
	/*
		{name: 'companyname', mapping : 'companyname'},
		{name: 'personname', mapping : 'personname'},
		{name: 'telno', mapping : 'telno'}
	];*/
    // create the data store
    var firstGridStore = new Ext.data.JsonStore({
        fields : fields,
		url	   : 'smsoperation?operation=getphonebook',
//		data   : myData,
		autoLoad:true
    });
	
    var _cm =  new Ext.grid.CheckboxSelectionModel();

   /**
    *�޸Ĵ���
    **/
    window.openupdatewin = function(value){
    	var rec = {};
    	for (var i = 0; i<firstGridStore.getCount(); i++) {
    		var _r = firstGridStore.getAt(i);
    		if(_r.get('id')==value)
    		{
    			rec = _r;
    			break;
    		}
    	}
    	var _updatewin = new UpdatePhoneBookWindow();
    	_updatewin.show();
    	_updatewin.load(rec);
    	
    	_updatewin.on('submit',function(win,record){
    					var _form = win.form.getForm();
    					var _company = _form.findField('companyname').getValue();
    					var _name = _form.findField('personname').getValue();
    					var _tel = _form.findField('telno').getValue();
    					var _id = _form.findField('id').getValue();
    					var _r = new Ext.data.Record(_form.getValues());
    					_form.submit(
    						{
    							params:{id:_id,company:encodeURI(_company),name:encodeURI(_name),tel:encodeURI(_tel)},
    							waitTitle:'ϵͳ��ʾ',
    							waitMsg:'�����ύ�������Ժ�',
    							method:'POST',
    							url:'smsoperation?operation=updatephonebook',
    							success:function(from,action){
    								
    								var data = rec.data;
									for (var i in data) {
										rec.set(i,_r.get(i));
									}
									rec.commit();
    								msg('ϵͳ��ʾ','�������ݳɹ���',Ext.MessageBox.INFO);
    								return;
    							},
    							failure:function(){
    								msg('ϵͳ��ʾ','��������ʧ�ܣ�',Ext.MessageBox.ERROR);
    								return;
    							}
    						}
    					);
    	})
    	
    	
    }
    /**
    *ɾ����¼
    **/
    window.deleterec = function(value)
    {
    	Ext.MessageBox.confirm('ϵͳ��ʾ','�Ƿ�ȷ��ɾ��ѡ���ļ�¼��',
    		function(btn){
    			if(btn=='yes')
    			{
    				Ext.Ajax.request(
    					{
    						params:{id:value},
    						url:'smsoperation?operation=deletephonebook',
    						success:function(resp,option){
    							msg('ϵͳ��ʾ','�ѳɹ�ɾ����¼',Ext.MessageBox.INFO);
    							firstGridStore.reload();
    							return;
    						},
    						failure:function(resp,option){
    							msg('ϵͳ��ʾ','ɾ����¼ʧ��',Ext.MessageBox.INFO);
    							return;
    						}
    					}
    				);
    			}
    		}
    	);
    }
	// Column Model shortcut array
	var cols = [
		_cm,
		{id:'name', header: "��λ", width: 200, sortable: true, dataIndex: 'companyname'},
		{header: "����", width: 80, sortable: true, dataIndex: 'personname'},
		{header: "�ֻ�����", width: 100, sortable: true, dataIndex: 'telno'},
		{header: "�޸�", width: 100, sortable: true, dataIndex: 'id'
			,renderer:function(value, meta, rec, rowIdx, colIdx, ds)
				{
					return '<a href="javascript:void(0)" onclick=openupdatewin('+value+')>�޸�</a>';
				}
		},
		{header: "ɾ��", width: 100, sortable: true, dataIndex: 'id'
			,renderer:function(value, meta, rec, rowIdx, colIdx, ds)
				{
					return '<a href="javascript:void(0)" onclick=deleterec('+value+')>ɾ��</a>';		
				}
		}
		
	];
	//�ڶ���cols
	var secondcols = [
		{id : 'name', header: "��λ", width: 120, sortable: true, dataIndex: 'companyname'},
		{header: "����", width: 100, sortable: true, dataIndex: 'personname'},
		{header: "�ֻ�����", width: 100, sortable: true, dataIndex: 'telno'}
	];
	// declare the source Grid
    var firstGrid = new Ext.grid.GridPanel({
    	tbar			 :[
    		{
    			text:'���ͨѶ¼',
    			iconCls:'vcard_add',
    			handler:function(){
    				var win = new InsertPhoneBookWindow();
    				win.show();
    				win.on('submit',function(win,rec){
    					var _form = win.form.getForm();
    					var _company = _form.findField('companyname').getValue();
    					var _name = _form.findField('personname').getValue();
    					var _tel = _form.findField('telno').getValue();
    					_form.submit(
    						{
    							params:{company:encodeURI(_company),name:encodeURI(_name),tel:encodeURI(_tel)},
    							waitTitle:'ϵͳ��ʾ',
    							waitMsg:'�����ύ�������Ժ�',
    							method:'POST',
    							url:'smsoperation?operation=savephonebook',
    							success:function(from,action){
    								var _r = win.form.getValue();
    								_r.set('id',action.result.msg);
    								firstGridStore.insert(0,_r);
    								msg('ϵͳ��ʾ','�������ݳɹ���',Ext.MessageBox.INFO);
    								return;
    							},
    							failure:function(){
    								msg('ϵͳ��ʾ','��������ʧ�ܣ�',Ext.MessageBox.ERROR);
    								return;
    							}
    						}
    					);
    				})
    			}
    		},'-',
    		{
    			text:'����ɾ��',
    			iconCls:'vcard_delete',
    			handler:function(){
    				var _sel = firstGrid.getSelectionModel().getSelections();
    				var _ids = [];
    				if(_sel.length==0)
    				{
    					msg('ϵͳ��ʾ','������ѡ��һ����¼���в���!',Ext.MessageBox.WARNING);
    					return;
    				}
    				for (var i = 0; i < _sel.length; i++) {
    					_ids.push(_sel[i].get('id'));
    				}
    				Ext.MessageBox.confirm('ϵͳ��ʾ','�Ƿ�ȷ��ɾ��ѡ���ļ�¼��',
			    		function(btn){
			    			if(btn=='yes')
			    			{
			    				Ext.Ajax.request(
			    					{
			    						params:{id:_ids.join(',')},
			    						url:'smsoperation?operation=deletephonebook',
			    						success:function(resp,option){
			    							msg('ϵͳ��ʾ','�ѳɹ�ɾ����¼',Ext.MessageBox.INFO);
			    							firstGridStore.remove(_sel);
			    							return;
			    						},
			    						failure:function(resp,option){
			    							msg('ϵͳ��ʾ','ɾ����¼ʧ��',Ext.MessageBox.ERROR);
			    							return;
			    						}
			    					}
			    				);
			    			}
			    		}
			    	);
    			}
    		},'-',
    		{
    			text:'ˢ���б�',
    			iconCls:'refresh',
    			handler:function(){
    				var _url = 'smsoperation?operation=getphonebook';
    				firstGridStore.proxy.setUrl(_url,true);
    				firstGridStore.reload();
    			}
    		},'-',
    		new Ext.form.TwinTriggerField({
        		       validationEvent	:false,
					   validateOnBlur	:false,
					   emptyText		:'�����������в�ѯ�������ÿո�������п��ٲ�ѯ!',
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
							 *������ѯ��ʱ���һ�β�ѯ������ʾ�������ݽ��
							 *Ϊʲô�ڷ�ҳ��ʱ��������� findallfiletopic.html��ַ��
							 **/
    						var v = this.getRawValue();
	    					var _url='smsoperation?operation=qucikfindpersonname';
    						var _obj = {names:encodeURI(v)};
//    						filestore.proxy.conn.url = _url;
    						firstGridStore.proxy.setUrl(_url,true);
    						firstGridStore.baseParams = _obj;
							firstGridStore.load({params:_obj});
    						this.hasSearch = true;
    						this.triggers[0].show();
							this.focus();
						}			
					})
    	],
    	width			 : 600,
    	iconCls			 : 'vcard',
		ddGroup          : 'secondGridDDGroup',
        store            : firstGridStore,
        columns          : cols,
		enableDragDrop   : true,
        stripeRows       : true,
        autoExpandColumn : 'name',
        sm				 : _cm,
        title            : 'ͨѶ¼'
    });

    var secondGridStore = new Ext.data.JsonStore({
        fields : fields,
		root   : 'records'
    });

    // create the destination Grid
    var secondGrid = new Ext.grid.GridPanel({
    	iconCls			 : 'vcard',
		ddGroup          : 'firstGridDDGroup',
        store            : secondGridStore,
        columns          : secondcols,
		enableDragDrop   : true,
        stripeRows       : true,
        autoExpandColumn : 'name',
        title            : '�ռ����б�'
    });


    
   
    
    
	//Simple 'border layout' panel to house both grids
	var displayPanel = new Ext.Panel({
		width        : 650,
		height       : 400,
		layout       : 'hbox',
		region:'center',		
//		region		 : 'center',
//		renderTo     : 'panel',
//		renderTo:Ext.getBody(),
		defaults     : { flex : 1 }, //auto stretch
		layoutConfig : { align : 'stretch' },
		items        : [
			firstGrid,
			secondGrid
		],
		bbar    : [
			'->', // Fill
			{
				iconCls : 'refresh', 
				text    : '������������',
				handler : function() {
					var _url = 'smsoperation?operation=getphonebook';
    				firstGridStore.proxy.setUrl(_url,true);
    				firstGridStore.reload();

					//purge destination grid
					secondGridStore.removeAll();
				}
			}
		]
	});

	 var simple = new Ext.FormPanel({
	 	iconCls	:'comment_edit',
        labelWidth: 75, // label settings here cascade unless overridden
        labelAlign:'top',
        frame:true,
        title: '��������(������50��)',
        region:'north',
      	height: 200,
        defaults: {width: 230},
        defaultType: 'textfield',
        items: [
        	{
                fieldLabel: '��������',
                blankText:'�������ݲ���Ϊ��',
                enableKeyEvents:true,
                listeners:{
                		keyup:function(src,e){
							var strs = '<font color="red" size=3>*</font>��������(������50��)';
                    		strs += '  �Ѿ�������'+src.getValue().length+'��';
                    		Ext.DomQuery.selectNode('label[for=first]').innerHTML=strs
                    	},
                    	keypress:function(src,e){
							var strs = '<font color="red" size=3>*</font>��������(������50��)';
                    		strs += '  �Ѿ�������'+src.getValue().length+'��';
                    		Ext.DomQuery.selectNode('label[for=first]').innerHTML=strs
                    	}
                 },
                anchor:'95%',
                id:'first',
                name: 'content',
                allowBlank:false
            },
            {
            	fieldLabel:'��������',
            	xtype:'datefield',
            	name:'senddate',
            	format:'Y-m-d H:i:s',
            	allowBlank:false,
            	value:new Date(),
            	invalidText :'���ڱ���Ϊyyyy-mm-dd ��ʽ,��2011��1��1��Ϊ2011-01-01',
            	blankText:'������д��������'
            }
        ],
		buttonAlign:'left',
        buttons: [
	        {
	            text: '����',
	            iconCls:'email_go',
	            handler:function(){
	            	var telnos = [];
	            	for (var i = 0; i < secondGridStore.getCount(); i++) {
	            		telnos.push(secondGridStore.getAt(i).get('telno'));
	            	}
	            	if(telnos.length==0)
	            	{
	            		msg('ϵͳ��ʾ','�ռ����б���Ϊ��,��ѡ���ռ��ˣ�',Ext.MessageBox.ERROR);
	            		return;
	            	}
	            	var _v = simple.getForm().findField('content').getValue();
	            	var _form = simple.getForm().isValid();
	            	if(_form!=true||_v.trim()=='')
	            	{
	            		msg('ϵͳ��ʾ','��������Ϊ��,����д�������ݣ�',Ext.MessageBox.ERROR);
	            		return;	
	            	}
	            	
	            	simple.getForm().submit({
	            		params:{telnos:(telnos.join(',')),message:encodeURI(_v)},
	            		waitTitle:'ϵͳ��ʾ',
						waitMsg:'��Ϣ���ڷ��ͣ����Ժ�...', 
	            		url:'smsoperation?operation=sendmsg',
	            		method:'POST',
	            		success:function(form, action){
	            			simple.getForm().findField('content').setValue(_v);
	            			msg('ϵͳ��ʾ','���Ͷ��ųɹ�',Ext.MessageBox.INFO);
	            		},
	            		failure:function(form, action){
	            			msg('ϵͳ��ʾ','���Ͷ���ʧ��,ԭ�����緱æ!���Ժ�����!');
	            		}
	            	});
	            }
	        },{
	            text: 'ȡ��',
	            iconCls:'cancel',
	            handler:function(){
	            	var _url = 'smsoperation?operation=getphonebook';
    				firstGridStore.proxy.setUrl(_url,true);
    				firstGridStore.reload();
	            	simple.getForm().reset();
	            	secondGridStore.removeAll();
	            }
	        }
        ]
    });

	 var view = new Ext.Viewport({
    	renderTo:Ext.getBody(),
    	layout:'border',
    	items:[simple,displayPanel]
    });
	
	
	// used to add records to the destination stores
	var blankRecord =  Ext.data.Record.create(fields);

        /****
        * Setup Drop Targets
        ***/
        // This will make sure we only drop to the  view scroller element
        var firstGridDropTargetEl =  firstGrid.getView().scroller.dom;
        var firstGridDropTarget = new Ext.dd.DropTarget(firstGridDropTargetEl, {
                ddGroup    : 'firstGridDDGroup',
                notifyDrop : function(ddSource, e, data){
                        var records =  ddSource.dragData.selections;
                        Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
                        firstGrid.store.add(records);
                        firstGrid.store.sort('name', 'ASC');
                        return true
                }
        });


        // This will make sure we only drop to the view scroller element
        var secondGridDropTargetEl = secondGrid.getView().scroller.dom;
        var secondGridDropTarget = new Ext.dd.DropTarget(secondGridDropTargetEl, {
                ddGroup    : 'secondGridDDGroup',
                notifyDrop : function(ddSource, e, data){
                        var records =  ddSource.dragData.selections;
                        Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
                        secondGrid.store.add(records);
                        secondGrid.store.sort('name', 'ASC');
                        return true
                }
        });
});
