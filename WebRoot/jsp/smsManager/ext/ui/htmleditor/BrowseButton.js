/**
 * 国外网友loeppky所做,用于浏览文件
 */
Ext.namespace('Ext.ux.form');
Ext.ux.form.BrowseButton = Ext.extend(Ext.Button, {
    inputFileName: 'file',
    debug: false,
    FLOAT_EL_WIDTH: 60,
    FLOAT_EL_HEIGHT: 18,
    buttonCt: null,
    clipEl: null,
    floatEl: null,
    inputFileEl: null,
    originalHandler: null,
    originalScope: null,
    initComponent: function(){
        Ext.ux.form.BrowseButton.superclass.initComponent.call(this);
        this.originalHandler = this.handler;
        this.originalScope = this.scope;
        this.handler = null;
        this.scope = null;
    },
    onRender: function(ct, position){
        Ext.ux.form.BrowseButton.superclass.onRender.call(this, ct, position); // render the Ext.Button
        this.buttonCt = this.el.child('.x-btn-center em');
        this.buttonCt.position('relative'); // this is important!
        var styleCfg = {
            position: 'absolute',
            overflow: 'hidden',
            top: '0px', // default
            left: '0px' // default
        };
        if (Ext.isIE) {
            Ext.apply(styleCfg, {
                left: '-3px',
                top: '-3px'
            });
        }
        else 
            if (Ext.isGecko) {
                Ext.apply(styleCfg, {
                    left: '-3px',
                    top: '-3px'
                });
            }
            else 
                if (Ext.isSafari) {
                    Ext.apply(styleCfg, {
                        left: '-4px',
                        top: '-2px'
                    });
                }
        this.clipEl = this.buttonCt.createChild({
            tag: 'div',
            style: styleCfg
        });
        this.setClipSize();
        this.clipEl.on({
            'mousemove': this.onButtonMouseMove,
            'mouseover': this.onButtonMouseMove,
            scope: this
        });
        
        this.floatEl = this.clipEl.createChild({
            tag: 'div',
            style: {
                position: 'absolute',
                width: this.FLOAT_EL_WIDTH + 'px',
                height: this.FLOAT_EL_HEIGHT + 'px',
                overflow: 'hidden'
            }
        });
        
        
        if (this.debug) {
            this.clipEl.applyStyles({
                'background-color': 'green'
            });
            this.floatEl.applyStyles({
                'background-color': 'red'
            });
        }
        else {
            this.floatEl.setOpacity(0.0);
        }
        var buttonEl = this.el.child(this.buttonSelector);
        buttonEl.on('focus', this.onButtonFocus, this);
        if (Ext.isIE) {
            this.el.on('keydown', this.onButtonKeyDown, this);
        }
        
        this.createInputFile();
    },
    setClipSize: function(){
        if (this.clipEl) {
            var width = this.buttonCt.getWidth();
            var height = this.buttonCt.getHeight();
            if (width === 0 || height === 0) {
                this.setClipSize.defer(100, this);
            }
            else {
                if (Ext.isIE) {
                    width = width + 5;
                    height = height + 5;
                }
                else 
                    if (Ext.isGecko) {
                        width = width + 6;
                        height = height + 6;
                    }
                    else 
                        if (Ext.isSafari) {
                            width = width + 6;
                            height = height + 6;
                        }
                this.clipEl.setSize(width, height);
            }
        }
    },
    createInputFile: function(){
        this.floatEl.select('em').each(function(el){
            el.remove();
        });
        this.inputFileEl = this.floatEl.createChild({
            tag: 'input',
            type: 'file',
            size: 1,
            name: this.inputFileName || Ext.id(this.el),
            tabindex: this.tabIndex,
            style: {
                position: 'absolute',
                cursor: 'pointer',
                right: '0px',
                top: '0px'
            }
        });
        this.inputFileEl = this.inputFileEl.child('input') || this.inputFileEl;
        this.inputFileEl.on({
            'click': this.onInputFileClick,
            'change': this.onInputFileChange,
            'focus': this.onInputFileFocus,
            'select': this.onInputFileFocus,
            'blur': this.onInputFileBlur,
            scope: this
        });
        if (this.tooltip) {
            if (typeof this.tooltip == 'object') {
                Ext.QuickTips.register(Ext.apply({
                    target: this.inputFileEl
                }, this.tooltip));
            }
            else {
                this.inputFileEl.dom[this.tooltipType] = this.tooltip;
            }
        }
    },
    onButtonFocus: function(e){
        if (this.inputFileEl) {
            this.inputFileEl.focus();
            e.stopEvent();
        }
    },
    onButtonKeyDown: function(e){
        if (this.inputFileEl && e.getKey() == Ext.EventObject.SPACE) {
            this.inputFileEl.dom.click();
            e.stopEvent();
        }
    },
    onButtonMouseMove: function(e){
        var xy = e.getXY();
        xy[0] -= this.FLOAT_EL_WIDTH / 2;
        xy[1] -= this.FLOAT_EL_HEIGHT / 2;
        this.floatEl.setXY(xy);
    },
    onInputFileFocus: function(e){
        if (!this.isDisabled) {
            this.el.addClass("x-btn-over");
        }
    },
    onInputFileBlur: function(e){
        this.el.removeClass("x-btn-over");
    },
    onInputFileClick: function(e){
        e.stopPropagation();
    },
    onInputFileChange: function(){
        if (this.originalHandler) {
            this.originalHandler.call(this.originalScope, this);
        }
    },
    detachInputFile: function(noCreate){
        var result = this.inputFileEl;
        
        if (typeof this.tooltip == 'object') {
            Ext.QuickTips.unregister(this.inputFileEl);
        }
        else {
            this.inputFileEl.dom[this.tooltipType] = null;
        }
        this.inputFileEl.removeAllListeners();
        this.inputFileEl = null;
        
        if (!noCreate) {
            this.createInputFile();
        }
        return result;
    },
    getInputFile: function(){
        return this.inputFileEl;
    },
    disable: function(){
        Ext.ux.form.BrowseButton.superclass.disable.call(this);
        this.inputFileEl.dom.disabled = true;
    },
    enable: function(){
        Ext.ux.form.BrowseButton.superclass.enable.call(this);
        this.inputFileEl.dom.disabled = false;
    }
});

Ext.reg('browsebutton', Ext.ux.form.BrowseButton);
