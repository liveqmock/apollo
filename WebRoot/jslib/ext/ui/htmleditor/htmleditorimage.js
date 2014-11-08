/**
 * @author songyinghao
 * @version 1.0
 * @class Ext.ux.HTMLEditorImage
 * @return
 * @classDescription HTMLEditor的一个图片插件,
 * 编辑器HTMLEditor(htmleditor.js),图片浏览按钮BrowseButton.js
 * 均为国外网友制作,在此向他们的勤奋表示敬意
 */
Ext.ux.HTMLEditorImage = function(){

    // 插件主体HTMLEditor
    var editor;
    // 插入图片的Window
    var win;
    //  Ext.FormPanel
    var tabs;
    //一个form,用来提交表单
    var tempForm;
    //图片浏览按钮的宽度
    var browseButtonWidth = 156;
    //图像的URL
    var imageURL = null;
    //文件输入对象
    var inputFileEl = null;
    
    //得到图片浏览按钮的宽度
    var getBrowseButtonWidth = function(){
        if (Ext.isIE) {
            browseButtonWidth = 153;
        }
        else {
            browseButtonWidth = 157;
        }
    }
    // 设置图片的详细信息
    var setImageDetails = function(data){
        tabs.form.findField('src').setValue(data.url);
        tabs.form.findField('alt').setValue(data.name);
        tabs.form.findField('width').setValue(data.width);
        tabs.form.findField('height').setValue(data.height);
        sourceChanged();
    };
    
    // 创建一个图片
    var createImage = function(){
        var imageWidth = tabs.form.findField('width').getValue();
        var imageHeight = tabs.form.findField('height').getValue();
        var element = document.createElement("img");
        element.src = '../../../'+imageURL;
        element.alt = tabs.form.findField('alt').getValue();
        if (imageWidth == null || imageWidth == '') {
        }
        else {
            element.style.width = imageWidth + "px"
        }
        if (imageHeight == null || imageHeight == '') {
        }
        else {
            element.style.height = imageHeight + "px"
        }
        //element.src = tabs.form.findField('src').getValue();
        //element.style.width = tabs.form.findField('width').getValue() + "px";
        //element.style.height = tabs.form.findField('height').getValue() + "px";
        return element;
    }
    //创建一个文件输入对象
    var createInputFileEl = function(browseButton){
        if (!tempForm) {
            var tempFormEl = win.body.createChild({
                tag: 'form',
                style: 'display:none'
            });
            tempForm = new Ext.form.BasicForm(tempFormEl, {
                method: 'POST',
                url: '../../../common/uploadimg.action',
                scope: this,
//                enctype:'multipart/form-data',
                fileUpload: true
            })
        } 
        inputFileEl = browseButton.detachInputFile();
        inputFileEl.appendTo(tempForm.getEl());
        var data = {
            "name": null,
            "height": null,
            "width": null,
            "size": null,
            "url": inputFileEl.dom.value
        }
        setImageDetails(data);
    }
    // 把图片插入editor中
    var insertImageByBrowser = function(){
        if (Ext.isIE) {
            // IE浏览器
            return function(){
                var selection = editor.doc.selection;
                var range = selection.createRange();
                range.pasteHTML(createImage().outerHTML);
            };
            
        }
        else {
            //火狐浏览器
            return function(){
                var selection = editor.win.getSelection();
                if (!selection.isCollapsed) {
                    selection.deleteFromDocument();
                }
                selection.getRangeAt(0).insertNode(createImage());
            };
        }
    }();
    
    var insertImage = function(){
        editor.win.focus();
        insertImageByBrowser();
        editor.updateToolbar();
        editor.deferFocus();
    };
    
    // 调整插入按钮的状态
    var sourceChanged = function(){
        var disabled = (tabs.form.findField('src').getValue() == "");
        Ext.getCmp('insert-btn').setDisabled(disabled);
    };
    
    // 打开ImageWindow
    var openImageWindow = function(){
        getBrowseButtonWidth();
        if (!win) {
            tabs = new Ext.FormPanel({
                labelWidth: 70,
                items: {
                    xtype: 'tabpanel',
                    border: false,
                    activeTab: 0,
                    bodyStyle: 'padding:5px',
                    defaults: {
                        autoHeight: true
                    },
                    items: [{
                        xtype: 'fieldset',
                        border: true,
                        title: '图片信息',
                        autoHeight: true,
                        defaults: {
                            width: 230
                        },
                        items: [{
                            layout: 'column',
                            autoWidth: true,
                            border: false,
                            defaults: {
                                layout: 'form',
                                border: false
                            },
                            hideLabel: true,
                            items: [{
                                items: [{
                                    xtype: 'textfield',
                                    fieldLabel: '图片来源',
                                    name: 'src',
                                    allowBlank: false,
                                    width: browseButtonWidth,
                                    listeners: {
                                        'change': {
                                            fn: sourceChanged,
                                            scope: this
                                        }
                                    }
                                }]
                            }, {
                                items: [{
                                    xtype: "statictextfield",
                                    submitValue: false,
                                    hideLabel: true,
                                    value: ' '
                                }]
                            }, {
                                items: [{
                                    xtype: 'browsebutton',
                                    text: '浏览图片',
                                    scope: this,
                                    inputFileName: 'image',
                                    handler: createInputFileEl
                                }]
                            }]
                        
                        }, {
                            xtype: 'textfield',
                            fieldLabel: '图片描述',
                            name: 'alt'
                        }, {
                            layout: "column",
                            autoWidth: true,
                            border: false,
                            defaults: {
                                layout: 'form',
                                border: false
                            },
                            hideLabel: true,
                            items: [{
                                items: [{
                                    xtype: "numberfield",
                                    fieldLabel: '图片尺寸',
                                    name: 'width',
                                    width: 98,
                                    allowDecimals: false,
                                    allowNegative: false
                                }]
                            }, {
                                items: [{
                                    xtype: "statictextfield",
                                    submitValue: false,
                                    hideLabel: true,
                                    value: '宽x高'
                                }]
                            }, {
                                items: [{
                                    xtype: "numberfield",
                                    hideLabel: true,
                                    name: 'height',
                                    width: 97,
                                    allowDecimals: false,
                                    allowNegative: false
                                }]
                            }]
                        }]
                    }]
                }
            });
            
            win = new Ext.Window({
            	style:'z-index:2;',
                title: '插入图片',
                closable: true,
                modal: true,
                closeAction: 'hide',
                width: 352,
                height: 230,
                plain: true,
                layout: 'fit',
                border: false,
                items: tabs,
                buttons: [{
                    text: '插入',
                    id: 'insert-btn',
                    disabled: true,
                    handler: function(){
                        tempForm.submit({
                            form: tempForm,
                            waitTitle: '上传信息',
                            waitMsg: '图片上传中...',
                            success: function(form, action){
                            	
                            	
                            	
                            	
                                imageURL = action.result.fileURL;
//                                alert(''+imageURL);
//                                location.href = imageURL;
                                
                                insertImage();
                                inputFileEl.remove();
                                tabs.getForm().reset();
                            },
                            failure: function(form, action){
                                alert("failure");
                                inputFileEl.remove();
                                tabs.getForm().reset();
                            }
                        });
                        win.hide();
                    }
                }, {
                    text: '关闭',
                    handler: function(){
                        win.hide();
                        
                    }
                }],
                listeners: {
                    'show': function(){
                        tabs.form.reset();
                        sourceChanged();
                    }
                }
            });
        }
//        win.show(this);
         win.show(Ext.getBody(),function(){/*this.setActive(true);*/},this);
    }
     
    return {
        init: function(htmlEditor){
            editor = htmlEditor;
            
            // 把插件的图标加到editor的工具栏上
            editor.tb.insertToolsAfter('createlink', {
                itemId: 'image',
                cls: 'x-btn-icon x-edit-image',
                handler: openImageWindow,
                scope: this,
                clickEvent: 'mousedown',
                tooltip: {
                    title: '图片',
                    text: '插入图片.',
                    cls: 'x-html-editor-tip'
                }
            });
        }
    }
}

