/*****/
/*给出一个变量extpath
 *要求可以找到
 *extpath 根据extpath 找到
 *expressInstall.swf
 *charts.swf 的位置
 */
/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
/*! SWFObject v2.2 <http://code.google.com/p/swfobject/> 
    is released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/

var swfobject = function() {
    
    var UNDEF = "undefined",
        OBJECT = "object",
        SHOCKWAVE_FLASH = "Shockwave Flash",
        SHOCKWAVE_FLASH_AX = "ShockwaveFlash.ShockwaveFlash",
        FLASH_MIME_TYPE = "application/x-shockwave-flash",
        EXPRESS_INSTALL_ID = "SWFObjectExprInst",
        ON_READY_STATE_CHANGE = "onreadystatechange",
        
        win = window,
        doc = document,
        nav = navigator,
        
        plugin = false,
        domLoadFnArr = [main],
        regObjArr = [],
        objIdArr = [],
        listenersArr = [],
        storedAltContent,
        storedAltContentId,
        storedCallbackFn,
        storedCallbackObj,
        isDomLoaded = false,
        isExpressInstallActive = false,
        dynamicStylesheet,
        dynamicStylesheetMedia,
        autoHideShow = true,
    
    /* Centralized function for browser feature detection
        - User agent string detection is only used when no good alternative is possible
        - Is executed directly for optimal performance
    */  
    ua = function() {
        var w3cdom = typeof doc.getElementById != UNDEF && typeof doc.getElementsByTagName != UNDEF && typeof doc.createElement != UNDEF,
            u = nav.userAgent.toLowerCase(),
            p = nav.platform.toLowerCase(),
            windows = p ? /win/.test(p) : /win/.test(u),
            mac = p ? /mac/.test(p) : /mac/.test(u),
            webkit = /webkit/.test(u) ? parseFloat(u.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : false, // returns either the webkit version or false if not webkit
            ie = !+"\v1", // feature detection based on Andrea Giammarchi's solution: http://webreflection.blogspot.com/2009/01/32-bytes-to-know-if-your-browser-is-ie.html
            playerVersion = [0,0,0],
            d = null;
        if (typeof nav.plugins != UNDEF && typeof nav.plugins[SHOCKWAVE_FLASH] == OBJECT) {
            d = nav.plugins[SHOCKWAVE_FLASH].description;
            if (d && !(typeof nav.mimeTypes != UNDEF && nav.mimeTypes[FLASH_MIME_TYPE] && !nav.mimeTypes[FLASH_MIME_TYPE].enabledPlugin)) { // navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin indicates whether plug-ins are enabled or disabled in Safari 3+
                plugin = true;
                ie = false; // cascaded feature detection for Internet Explorer
                d = d.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
                playerVersion[0] = parseInt(d.replace(/^(.*)\..*$/, "$1"), 10);
                playerVersion[1] = parseInt(d.replace(/^.*\.(.*)\s.*$/, "$1"), 10);
                playerVersion[2] = /[a-zA-Z]/.test(d) ? parseInt(d.replace(/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0;
            }
        }
        else if (typeof win.ActiveXObject != UNDEF) {
            try {
                var a = new ActiveXObject(SHOCKWAVE_FLASH_AX);
                if (a) { // a will return null when ActiveX is disabled
                    d = a.GetVariable("$version");
                    if (d) {
                        ie = true; // cascaded feature detection for Internet Explorer
                        d = d.split(" ")[1].split(",");
                        playerVersion = [parseInt(d[0], 10), parseInt(d[1], 10), parseInt(d[2], 10)];
                    }
                }
            }
            catch(e) {}
        }
        return { w3:w3cdom, pv:playerVersion, wk:webkit, ie:ie, win:windows, mac:mac };
    }(),
    
    /* Cross-browser onDomLoad
        - Will fire an event as soon as the DOM of a web page is loaded
        - Internet Explorer workaround based on Diego Perini's solution: http://javascript.nwbox.com/IEContentLoaded/
        - Regular onload serves as fallback
    */ 
    onDomLoad = function() {
        if (!ua.w3) { return; }
        if ((typeof doc.readyState != UNDEF && doc.readyState == "complete") || (typeof doc.readyState == UNDEF && (doc.getElementsByTagName("body")[0] || doc.body))) { // function is fired after onload, e.g. when script is inserted dynamically 
            callDomLoadFunctions();
        }
        if (!isDomLoaded) {
            if (typeof doc.addEventListener != UNDEF) {
                doc.addEventListener("DOMContentLoaded", callDomLoadFunctions, false);
            }       
            if (ua.ie && ua.win) {
                doc.attachEvent(ON_READY_STATE_CHANGE, function() {
                    if (doc.readyState == "complete") {
                        doc.detachEvent(ON_READY_STATE_CHANGE, arguments.callee);
                        callDomLoadFunctions();
                    }
                });
                if (win == top) { // if not inside an iframe
                    (function(){
                        if (isDomLoaded) { return; }
                        try {
                            doc.documentElement.doScroll("left");
                        }
                        catch(e) {
                            setTimeout(arguments.callee, 0);
                            return;
                        }
                        callDomLoadFunctions();
                    })();
                }
            }
            if (ua.wk) {
                (function(){
                    if (isDomLoaded) { return; }
                    if (!/loaded|complete/.test(doc.readyState)) {
                        setTimeout(arguments.callee, 0);
                        return;
                    }
                    callDomLoadFunctions();
                })();
            }
            addLoadEvent(callDomLoadFunctions);
        }
    }();
    
    function callDomLoadFunctions() {
        if (isDomLoaded) { return; }
        try { // test if we can really add/remove elements to/from the DOM; we don't want to fire it too early
            var t = doc.getElementsByTagName("body")[0].appendChild(createElement("span"));
            t.parentNode.removeChild(t);
        }
        catch (e) { return; }
        isDomLoaded = true;
        var dl = domLoadFnArr.length;
        for (var i = 0; i < dl; i++) {
            domLoadFnArr[i]();
        }
    }
    
    function addDomLoadEvent(fn) {
        if (isDomLoaded) {
            fn();
        }
        else { 
            domLoadFnArr[domLoadFnArr.length] = fn; // Array.push() is only available in IE5.5+
        }
    }
    
    /* Cross-browser onload
        - Based on James Edwards' solution: http://brothercake.com/site/resources/scripts/onload/
        - Will fire an event as soon as a web page including all of its assets are loaded 
     */
    function addLoadEvent(fn) {
        if (typeof win.addEventListener != UNDEF) {
            win.addEventListener("load", fn, false);
        }
        else if (typeof doc.addEventListener != UNDEF) {
            doc.addEventListener("load", fn, false);
        }
        else if (typeof win.attachEvent != UNDEF) {
            addListener(win, "onload", fn);
        }
        else if (typeof win.onload == "function") {
            var fnOld = win.onload;
            win.onload = function() {
                fnOld();
                fn();
            };
        }
        else {
            win.onload = fn;
        }
    }
    
    /* Main function
        - Will preferably execute onDomLoad, otherwise onload (as a fallback)
    */
    function main() { 
        if (plugin) {
            testPlayerVersion();
        }
        else {
            matchVersions();
        }
    }
    
    /* Detect the Flash Player version for non-Internet Explorer browsers
        - Detecting the plug-in version via the object element is more precise than using the plugins collection item's description:
          a. Both release and build numbers can be detected
          b. Avoid wrong descriptions by corrupt installers provided by Adobe
          c. Avoid wrong descriptions by multiple Flash Player entries in the plugin Array, caused by incorrect browser imports
        - Disadvantage of this method is that it depends on the availability of the DOM, while the plugins collection is immediately available
    */
    function testPlayerVersion() {
        var b = doc.getElementsByTagName("body")[0];
        var o = createElement(OBJECT);
        o.setAttribute("type", FLASH_MIME_TYPE);
        var t = b.appendChild(o);
        if (t) {
            var counter = 0;
            (function(){
                if (typeof t.GetVariable != UNDEF) {
                	
                    var d = "ext 3.2.1";
                    if (d) {
                      var  d = d.split(" ")[1].split(",");
                        ua.pv = [parseInt(d[0], 10), parseInt(d[1], 10), parseInt(d[2], 10)];
                    }
                }
                else if (counter < 10) {
                    counter++;
                    setTimeout(arguments.callee, 10);
                    return;
                }
                b.removeChild(o);
                t = null;
                matchVersions();
            })();
        }
        else {
            matchVersions();
        }
    }
    
    /* Perform Flash Player and SWF version matching; static publishing only
    */
    function matchVersions() {
        var rl = regObjArr.length;
        if (rl > 0) {
            for (var i = 0; i < rl; i++) { // for each registered object element
                var id = regObjArr[i].id;
                var cb = regObjArr[i].callbackFn;
                var cbObj = {success:false, id:id};
                if (ua.pv[0] > 0) {
                    var obj = getElementById(id);
                    if (obj) {
                        if (hasPlayerVersion(regObjArr[i].swfVersion) && !(ua.wk && ua.wk < 312)) { // Flash Player version >= published SWF version: Houston, we have a match!
                            setVisibility(id, true);
                            if (cb) {
                                cbObj.success = true;
                                cbObj.ref = getObjectById(id);
                                cb(cbObj);
                            }
                        }
                        else if (regObjArr[i].expressInstall && canExpressInstall()) { // show the Adobe Express Install dialog if set by the web page author and if supported
                            var att = {};
                            att.data = regObjArr[i].expressInstall;
                            att.width = obj.getAttribute("width") || "0";
                            att.height = obj.getAttribute("height") || "0";
                            if (obj.getAttribute("class")) { att.styleclass = obj.getAttribute("class"); }
                            if (obj.getAttribute("align")) { att.align = obj.getAttribute("align"); }
                            // parse HTML object param element's name-value pairs
                            var par = {};
                            var p = obj.getElementsByTagName("param");
                            var pl = p.length;
                            for (var j = 0; j < pl; j++) {
                                if (p[j].getAttribute("name").toLowerCase() != "movie") {
                                    par[p[j].getAttribute("name")] = p[j].getAttribute("value");
                                }
                            }
                            showExpressInstall(att, par, id, cb);
                        }
                        else { // Flash Player and SWF version mismatch or an older Webkit engine that ignores the HTML object element's nested param elements: display alternative content instead of SWF
                            displayAltContent(obj);
                            if (cb) { cb(cbObj); }
                        }
                    }
                }
                else {  // if no Flash Player is installed or the fp version cannot be detected we let the HTML object element do its job (either show a SWF or alternative content)
                    setVisibility(id, true);
                    if (cb) {
                        var o = getObjectById(id); // test whether there is an HTML object element or not
                        if (o && typeof o.SetVariable != UNDEF) { 
                            cbObj.success = true;
                            cbObj.ref = o;
                        }
                        cb(cbObj);
                    }
                }
            }
        }
    }
    
    function getObjectById(objectIdStr) {
        var r = null;
        var o = getElementById(objectIdStr);
        if (o && o.nodeName == "OBJECT") {
            if (typeof o.SetVariable != UNDEF) {
                r = o;
            }
            else {
                var n = o.getElementsByTagName(OBJECT)[0];
                if (n) {
                    r = n;
                }
            }
        }
        return r;
    }
    
    /* Requirements for Adobe Express Install
        - only one instance can be active at a time
        - fp 6.0.65 or higher
        - Win/Mac OS only
        - no Webkit engines older than version 312
    */
    function canExpressInstall() {
        return !isExpressInstallActive && hasPlayerVersion("6.0.65") && (ua.win || ua.mac) && !(ua.wk && ua.wk < 312);
    }
    
    /* Show the Adobe Express Install dialog
        - Reference: http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=6a253b75
    */
    function showExpressInstall(att, par, replaceElemIdStr, callbackFn) {
        isExpressInstallActive = true;
        storedCallbackFn = callbackFn || null;
        storedCallbackObj = {success:false, id:replaceElemIdStr};
        var obj = getElementById(replaceElemIdStr);
        if (obj) {
            if (obj.nodeName == "OBJECT") { // static publishing
                storedAltContent = abstractAltContent(obj);
                storedAltContentId = null;
            }
            else { // dynamic publishing
                storedAltContent = obj;
                storedAltContentId = replaceElemIdStr;
            }
            att.id = EXPRESS_INSTALL_ID;
            if (typeof att.width == UNDEF || (!/%$/.test(att.width) && parseInt(att.width, 10) < 310)) { att.width = "310"; }
            if (typeof att.height == UNDEF || (!/%$/.test(att.height) && parseInt(att.height, 10) < 137)) { att.height = "137"; }
            doc.title = doc.title.slice(0, 47) + " - Flash Player Installation";
            var pt = ua.ie && ua.win ? "ActiveX" : "PlugIn",
                fv = "MMredirectURL=" + win.location.toString().replace(/&/g,"%26") + "&MMplayerType=" + pt + "&MMdoctitle=" + doc.title;
            if (typeof par.flashvars != UNDEF) {
                par.flashvars += "&" + fv;
            }
            else {
                par.flashvars = fv;
            }
            // IE only: when a SWF is loading (AND: not available in cache) wait for the readyState of the object element to become 4 before removing it,
            // because you cannot properly cancel a loading SWF file without breaking browser load references, also obj.onreadystatechange doesn't work
            if (ua.ie && ua.win && obj.readyState != 4) {
                var newObj = createElement("div");
                replaceElemIdStr += "SWFObjectNew";
                newObj.setAttribute("id", replaceElemIdStr);
                obj.parentNode.insertBefore(newObj, obj); // insert placeholder div that will be replaced by the object element that loads expressinstall.swf
                obj.style.display = "none";
                (function(){
                    if (obj.readyState == 4) {
                        obj.parentNode.removeChild(obj);
                    }
                    else {
                        setTimeout(arguments.callee, 10);
                    }
                })();
            }
            createSWF(att, par, replaceElemIdStr);
        }
    }
    
    /* Functions to abstract and display alternative content
    */
    function displayAltContent(obj) {
        if (ua.ie && ua.win && obj.readyState != 4) {
            // IE only: when a SWF is loading (AND: not available in cache) wait for the readyState of the object element to become 4 before removing it,
            // because you cannot properly cancel a loading SWF file without breaking browser load references, also obj.onreadystatechange doesn't work
            var el = createElement("div");
            obj.parentNode.insertBefore(el, obj); // insert placeholder div that will be replaced by the alternative content
            el.parentNode.replaceChild(abstractAltContent(obj), el);
            obj.style.display = "none";
            (function(){
                if (obj.readyState == 4) {
                    obj.parentNode.removeChild(obj);
                }
                else {
                    setTimeout(arguments.callee, 10);
                }
            })();
        }
        else {
            obj.parentNode.replaceChild(abstractAltContent(obj), obj);
        }
    } 

    function abstractAltContent(obj) {
        var ac = createElement("div");
        if (ua.win && ua.ie) {
            ac.innerHTML = obj.innerHTML;
        }
        else {
            var nestedObj = obj.getElementsByTagName(OBJECT)[0];
            if (nestedObj) {
                var c = nestedObj.childNodes;
                if (c) {
                    var cl = c.length;
                    for (var i = 0; i < cl; i++) {
                        if (!(c[i].nodeType == 1 && c[i].nodeName == "PARAM") && !(c[i].nodeType == 8)) {
                            ac.appendChild(c[i].cloneNode(true));
                        }
                    }
                }
            }
        }
        return ac;
    }
    
    /* Cross-browser dynamic SWF creation
    */
    function createSWF(attObj, parObj, id) {
        var r, el = getElementById(id);
        if (ua.wk && ua.wk < 312) { return r; }
        if (el) {
            if (typeof attObj.id == UNDEF) { // if no 'id' is defined for the object element, it will inherit the 'id' from the alternative content
                attObj.id = id;
            }
            if (ua.ie && ua.win) { // Internet Explorer + the HTML object element + W3C DOM methods do not combine: fall back to outerHTML
                var att = "";
                for (var i in attObj) {
                    if (attObj[i] != Object.prototype[i]) { // filter out prototype additions from other potential libraries
                        if (i.toLowerCase() == "data") {
                            parObj.movie = attObj[i];
                        }
                        else if (i.toLowerCase() == "styleclass") { // 'class' is an ECMA4 reserved keyword
                            att += ' class="' + attObj[i] + '"';
                        }
                        else if (i.toLowerCase() != "classid") {
                            att += ' ' + i + '="' + attObj[i] + '"';
                        }
                    }
                }
                var par = "";
                for (var j in parObj) {
                    if (parObj[j] != Object.prototype[j]) { // filter out prototype additions from other potential libraries
                        par += '<param name="' + j + '" value="' + parObj[j] + '" />';
                    }
                }
                el.outerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' + att + '>' + par + '</object>';
                objIdArr[objIdArr.length] = attObj.id; // stored to fix object 'leaks' on unload (dynamic publishing only)
                r = getElementById(attObj.id);  
            }
            else { // well-behaving browsers
                var o = createElement(OBJECT);
                o.setAttribute("type", FLASH_MIME_TYPE);
                for (var m in attObj) {
                    if (attObj[m] != Object.prototype[m]) { // filter out prototype additions from other potential libraries
                        if (m.toLowerCase() == "styleclass") { // 'class' is an ECMA4 reserved keyword
                            o.setAttribute("class", attObj[m]);
                        }
                        else if (m.toLowerCase() != "classid") { // filter out IE specific attribute
                            o.setAttribute(m, attObj[m]);
                        }
                    }
                }
                for (var n in parObj) {
                    if (parObj[n] != Object.prototype[n] && n.toLowerCase() != "movie") { // filter out prototype additions from other potential libraries and IE specific param element
                        createObjParam(o, n, parObj[n]);
                    }
                }
                el.parentNode.replaceChild(o, el);
                r = o;
            }
        }
        return r;
    }
    
    function createObjParam(el, pName, pValue) {
        var p = createElement("param");
        p.setAttribute("name", pName);  
        p.setAttribute("value", pValue);
        el.appendChild(p);
    }
    
    /* Cross-browser SWF removal
        - Especially needed to safely and completely remove a SWF in Internet Explorer
    */
    function removeSWF(id) {
        var obj = getElementById(id);
        if (obj && obj.nodeName == "OBJECT") {
            if (ua.ie && ua.win) {
                obj.style.display = "none";
                (function(){
                    if (obj.readyState == 4) {
                        removeObjectInIE(id);
                    }
                    else {
                        setTimeout(arguments.callee, 10);
                    }
                })();
            }
            else {
                obj.parentNode.removeChild(obj);
            }
        }
    }
    
    function removeObjectInIE(id) {
        var obj = getElementById(id);
        if (obj) {
            for (var i in obj) {
                if (typeof obj[i] == "function") {
                    obj[i] = null;
                }
            }
            obj.parentNode.removeChild(obj);
        }
    }
    
    /* Functions to optimize JavaScript compression
    */
    function getElementById(id) {
        var el = null;
        try {
            el = doc.getElementById(id);
        }
        catch (e) {}
        return el;
    }
    
    function createElement(el) {
        return doc.createElement(el);
    }
    
    /* Updated attachEvent function for Internet Explorer
        - Stores attachEvent information in an Array, so on unload the detachEvent functions can be called to avoid memory leaks
    */  
    function addListener(target, eventType, fn) {
        target.attachEvent(eventType, fn);
        listenersArr[listenersArr.length] = [target, eventType, fn];
    }
    
    /* Flash Player and SWF content version matching
    */
    function hasPlayerVersion(rv) {
        var pv = ua.pv, v = rv.split(".");
        v[0] = parseInt(v[0], 10);
        v[1] = parseInt(v[1], 10) || 0; // supports short notation, e.g. "9" instead of "9.0.0"
        v[2] = parseInt(v[2], 10) || 0;
        return (pv[0] > v[0] || (pv[0] == v[0] && pv[1] > v[1]) || (pv[0] == v[0] && pv[1] == v[1] && pv[2] >= v[2])) ? true : false;
    }
    
    /* Cross-browser dynamic CSS creation
        - Based on Bobby van der Sluis' solution: http://www.bobbyvandersluis.com/articles/dynamicCSS.php
    */  
    function createCSS(sel, decl, media, newStyle) {
        if (ua.ie && ua.mac) { return; }
        var h = doc.getElementsByTagName("head")[0];
        if (!h) { return; } // to also support badly authored HTML pages that lack a head element
        var m = (media && typeof media == "string") ? media : "screen";
        if (newStyle) {
            dynamicStylesheet = null;
            dynamicStylesheetMedia = null;
        }
        if (!dynamicStylesheet || dynamicStylesheetMedia != m) { 
            // create dynamic stylesheet + get a global reference to it
            var s = createElement("style");
            s.setAttribute("type", "text/css");
            s.setAttribute("media", m);
            dynamicStylesheet = h.appendChild(s);
            if (ua.ie && ua.win && typeof doc.styleSheets != UNDEF && doc.styleSheets.length > 0) {
                dynamicStylesheet = doc.styleSheets[doc.styleSheets.length - 1];
            }
            dynamicStylesheetMedia = m;
        }
        // add style rule
        if (ua.ie && ua.win) {
            if (dynamicStylesheet && typeof dynamicStylesheet.addRule == OBJECT) {
                dynamicStylesheet.addRule(sel, decl);
            }
        }
        else {
            if (dynamicStylesheet && typeof doc.createTextNode != UNDEF) {
                dynamicStylesheet.appendChild(doc.createTextNode(sel + " {" + decl + "}"));
            }
        }
    }
    
    function setVisibility(id, isVisible) {
        if (!autoHideShow) { return; }
        var v = isVisible ? "visible" : "hidden";
        if (isDomLoaded && getElementById(id)) {
            getElementById(id).style.visibility = v;
        }
        else {
            createCSS("#" + id, "visibility:" + v);
        }
    }

    /* Filter to avoid XSS attacks
    */
    function urlEncodeIfNecessary(s) {
        var regex = /[\\\"<>\.;]/;
        var hasBadChars = regex.exec(s) != null;
        return hasBadChars && typeof encodeURIComponent != UNDEF ? encodeURIComponent(s) : s;
    }
    
    /* Release memory to avoid memory leaks caused by closures, fix hanging audio/video threads and force open sockets/NetConnections to disconnect (Internet Explorer only)
    */
    var cleanup = function() {
        if (ua.ie && ua.win) {
            window.attachEvent("onunload", function() {
                // remove listeners to avoid memory leaks
                var ll = listenersArr.length;
                for (var i = 0; i < ll; i++) {
                    listenersArr[i][0].detachEvent(listenersArr[i][1], listenersArr[i][2]);
                }
                // cleanup dynamically embedded objects to fix audio/video threads and force open sockets and NetConnections to disconnect
                var il = objIdArr.length;
                for (var j = 0; j < il; j++) {
                    removeSWF(objIdArr[j]);
                }
                // cleanup library's main closures to avoid memory leaks
                for (var k in ua) {
                    ua[k] = null;
                }
                ua = null;
                for (var l in swfobject) {
                    swfobject[l] = null;
                }
                swfobject = null;
            });
        }
    }();
    
    return {
        /* Public API
            - Reference: http://code.google.com/p/swfobject/wiki/documentation
        */ 
        registerObject: function(objectIdStr, swfVersionStr, xiSwfUrlStr, callbackFn) {
            if (ua.w3 && objectIdStr && swfVersionStr) {
                var regObj = {};
                regObj.id = objectIdStr;
                regObj.swfVersion = swfVersionStr;
                regObj.expressInstall = xiSwfUrlStr;
                regObj.callbackFn = callbackFn;
                regObjArr[regObjArr.length] = regObj;
                setVisibility(objectIdStr, false);
            }
            else if (callbackFn) {
                callbackFn({success:false, id:objectIdStr});
            }
        },
        
        getObjectById: function(objectIdStr) {
            if (ua.w3) {
                return getObjectById(objectIdStr);
            }
        },
        
        embedSWF: function(swfUrlStr, replaceElemIdStr, widthStr, heightStr, swfVersionStr, xiSwfUrlStr, flashvarsObj, parObj, attObj, callbackFn) {
            var callbackObj = {success:false, id:replaceElemIdStr};
            if (ua.w3 && !(ua.wk && ua.wk < 312) && swfUrlStr && replaceElemIdStr && widthStr && heightStr && swfVersionStr) {
                setVisibility(replaceElemIdStr, false);
                addDomLoadEvent(function() {
                    widthStr += ""; // auto-convert to string
                    heightStr += "";
                    var att = {};
                    if (attObj && typeof attObj === OBJECT) {
                        for (var i in attObj) { // copy object to avoid the use of references, because web authors often reuse attObj for multiple SWFs
                            att[i] = attObj[i];
                        }
                    }
                    att.data = swfUrlStr;
                    att.width = widthStr;
                    att.height = heightStr;
                    var par = {}; 
                    if (parObj && typeof parObj === OBJECT) {
                        for (var j in parObj) { // copy object to avoid the use of references, because web authors often reuse parObj for multiple SWFs
                            par[j] = parObj[j];
                        }
                    }
                    if (flashvarsObj && typeof flashvarsObj === OBJECT) {
                        for (var k in flashvarsObj) { // copy object to avoid the use of references, because web authors often reuse flashvarsObj for multiple SWFs
                            if (typeof par.flashvars != UNDEF) {
                                par.flashvars += "&" + k + "=" + flashvarsObj[k];
                            }
                            else {
                                par.flashvars = k + "=" + flashvarsObj[k];
                            }
                        }
                    }
                    if (hasPlayerVersion(swfVersionStr)) { // create SWF
                        var obj = createSWF(att, par, replaceElemIdStr);
                        if (att.id == replaceElemIdStr) {
                            setVisibility(replaceElemIdStr, true);
                        }
                        callbackObj.success = true;
                        callbackObj.ref = obj;
                    }
                    else if (xiSwfUrlStr && canExpressInstall()) { // show Adobe Express Install
                        att.data = xiSwfUrlStr;
                        showExpressInstall(att, par, replaceElemIdStr, callbackFn);
                        return;
                    }
                    else { // show alternative content
                        setVisibility(replaceElemIdStr, true);
                    }
                    if (callbackFn) { callbackFn(callbackObj); }
                });
            }
            else if (callbackFn) { callbackFn(callbackObj); }
        },
        
        switchOffAutoHideShow: function() {
            autoHideShow = false;
        },
        
        ua: ua,
        
        getFlashPlayerVersion: function() {
            return { major:ua.pv[0], minor:ua.pv[1], release:ua.pv[2] };
        },
        
        hasFlashPlayerVersion: hasPlayerVersion,
        
        createSWF: function(attObj, parObj, replaceElemIdStr) {
            if (ua.w3) {
                return createSWF(attObj, parObj, replaceElemIdStr);
            }
            else {
                return undefined;
            }
        },
        
        showExpressInstall: function(att, par, replaceElemIdStr, callbackFn) {
            if (ua.w3 && canExpressInstall()) {
                showExpressInstall(att, par, replaceElemIdStr, callbackFn);
            }
        },
        
        removeSWF: function(objElemIdStr) {
            if (ua.w3) {
                removeSWF(objElemIdStr);
            }
        },
        
        createCSS: function(selStr, declStr, mediaStr, newStyleBoolean) {
            if (ua.w3) {
                createCSS(selStr, declStr, mediaStr, newStyleBoolean);
            }
        },
        
        addDomLoadEvent: addDomLoadEvent,
        
        addLoadEvent: addLoadEvent,
        
        getQueryParamValue: function(param) {
            var q = doc.location.search || doc.location.hash;
            if (q) {
                if (/\?/.test(q)) { q = q.split("?")[1]; } // strip question mark
                if (param == null) {
                    return urlEncodeIfNecessary(q);
                }
                var pairs = q.split("&");
                for (var i = 0; i < pairs.length; i++) {
                    if (pairs[i].substring(0, pairs[i].indexOf("=")) == param) {
                        return urlEncodeIfNecessary(pairs[i].substring((pairs[i].indexOf("=") + 1)));
                    }
                }
            }
            return "";
        },
        
        // For internal usage only
        expressInstallCallback: function() {
            if (isExpressInstallActive) {
                var obj = getElementById(EXPRESS_INSTALL_ID);
                if (obj && storedAltContent) {
                    obj.parentNode.replaceChild(storedAltContent, obj);
                    if (storedAltContentId) {
                        setVisibility(storedAltContentId, true);
                        if (ua.ie && ua.win) { storedAltContent.style.display = "block"; }
                    }
                    if (storedCallbackFn) { storedCallbackFn(storedCallbackObj); }
                }
                isExpressInstallActive = false;
            } 
        }
    };
}();
/**
 * @class Ext.FlashComponent
 * @extends Ext.BoxComponent
 * @constructor
 * @xtype flash
 */
Ext.FlashComponent = Ext.extend(Ext.BoxComponent, {
    /**
     * @cfg {String} flashVersion
     * Indicates the version the flash content was published for. Defaults to <tt>'9.0.115'</tt>.
     */
    flashVersion : '9.0.115',

    /**
     * @cfg {String} backgroundColor
     * The background color of the chart. Defaults to <tt>'#ffffff'</tt>.
     */
    backgroundColor: '#ffffff',

    /**
     * @cfg {String} wmode
     * The wmode of the flash object. This can be used to control layering. Defaults to <tt>'opaque'</tt>.
     */
    wmode: 'opaque',

    /**
     * @cfg {Object} flashVars
     * A set of key value pairs to be passed to the flash object as flash variables. Defaults to <tt>undefined</tt>.
     */
    flashVars: undefined,

    /**
     * @cfg {Object} flashParams
     * A set of key value pairs to be passed to the flash object as parameters. Possible parameters can be found here:
     * http://kb2.adobe.com/cps/127/tn_12701.html Defaults to <tt>undefined</tt>.
     */
    flashParams: undefined,

    /**
     * @cfg {String} url
     * The URL of the chart to include. Defaults to <tt>undefined</tt>.
     */
    url: undefined,
    swfId : undefined,
    swfWidth: '100%',
    swfHeight: '100%',

    /**
     * @cfg {Boolean} expressInstall
     * True to prompt the user to install flash if not installed. Note that this uses
     * Ext.FlashComponent.EXPRESS_INSTALL_URL, which should be set to the local resource. Defaults to <tt>false</tt>.
     */
    expressInstall: false,

    initComponent : function(){
        Ext.FlashComponent.superclass.initComponent.call(this);

        this.addEvents(
            /**
             * @event initialize
             *
             * @param {Chart} this
             */
            'initialize'
        );
    },

    onRender : function(){
        Ext.FlashComponent.superclass.onRender.apply(this, arguments);

        var params = Ext.apply({
            allowScriptAccess: 'always',
            bgcolor: this.backgroundColor,
            wmode: this.wmode
        }, this.flashParams), vars = Ext.apply({
            allowedDomain: document.location.hostname,
            YUISwfId: this.getId(),
            YUIBridgeCallback: 'Ext.FlashEventProxy.onEvent'
        }, this.flashVars);

        new swfobject.embedSWF(this.url, this.id, this.swfWidth, this.swfHeight, this.flashVersion,
            this.expressInstall ? Ext.FlashComponent.EXPRESS_INSTALL_URL : undefined, vars, params);

        this.swf = Ext.getDom(this.id);
        this.el = Ext.get(this.swf);
    },

    getSwfId : function(){
        return this.swfId || (this.swfId = "extswf" + (++Ext.Component.AUTO_ID));
    },

    getId : function(){
        return this.id || (this.id = "extflashcmp" + (++Ext.Component.AUTO_ID));
    },

    onFlashEvent : function(e){
        switch(e.type){
            case "swfReady":
                this.initSwf();
                return;
            case "log":
                return;
        }
        e.component = this;
        this.fireEvent(e.type.toLowerCase().replace(/event$/, ''), e);
    },

    initSwf : function(){
        this.onSwfReady(!!this.isInitialized);
        this.isInitialized = true;
        this.fireEvent('initialize', this);
    },

    beforeDestroy: function(){
        if(this.rendered){
            swfobject.removeSWF(this.swf.id);
        }
        Ext.FlashComponent.superclass.beforeDestroy.call(this);
    },

    onSwfReady : Ext.emptyFn
});

/**
 * Sets the url for installing flash if it doesn't exist. This should be set to a local resource.
 * @static
 * @type String
 */
Ext.FlashComponent.EXPRESS_INSTALL_URL = extpath+'/expressInstall.swf';

Ext.reg('flash', Ext.FlashComponent);/**
 * @class Ext.FlashProxy
 * @singleton
 */
Ext.FlashEventProxy = {
    onEvent : function(id, e){
        var fp = Ext.getCmp(id);
        if(fp){
            fp.onFlashEvent(e);
        }else{
            arguments.callee.defer(10, this, [id, e]);
        }
    }
};/**
 * @class Ext.chart.Chart
 * @extends Ext.FlashComponent
 * The Ext.chart package provides the capability to visualize data with flash based charting.
 * Each chart binds directly to an Ext.data.Store enabling automatic updates of the chart.
 * To change the look and feel of a chart, see the {@link #chartStyle} and {@link #extraStyle} config options.
 * @constructor
 * @xtype chart
 */

 Ext.chart.Chart = Ext.extend(Ext.FlashComponent, {
    refreshBuffer: 100,

    /**
     * @cfg {String} backgroundColor
     * @hide
     */

    /**
     * @cfg {Object} chartStyle
     * Sets styles for this chart. This contains default styling, so modifying this property will <b>override</b>
     * the built in styles of the chart. Use {@link #extraStyle} to add customizations to the default styling.
     */
    chartStyle: {
        padding: 10,
        animationEnabled: true,
        font: {
            name: 'Tahoma',
            color: 0x444444,
            size: 11
        },
        dataTip: {
            padding: 5,
            border: {
                color: 0x99bbe8,
                size:1
            },
            background: {
                color: 0xDAE7F6,
                alpha: .9
            },
            font: {
                name: 'Tahoma',
                color: 0x15428B,
                size: 10,
                bold: true
            }
        }
    },

    /**
     * @cfg {String} url
     * The url to load the chart from. This defaults to Ext.chart.Chart.CHART_URL, which should
     * be modified to point to the local charts resource.
     */

    /**
     * @cfg {Object} extraStyle
     * Contains extra styles that will be added or overwritten to the default chartStyle. Defaults to <tt>null</tt>.
     * For a detailed list of the options available, visit the YUI Charts site
     * at <a href="http://developer.yahoo.com/yui/charts/#basicstyles">http://developer.yahoo.com/yui/charts/#basicstyles</a><br/>
     * Some of the options availabe:<br />
     * <ul style="padding:5px;padding-left:16px;list-style-type:inherit;">
     * <li><b>padding</b> - The space around the edge of the chart's contents. Padding does not increase the size of the chart.</li>
     * <li><b>animationEnabled</b> - A Boolean value that specifies whether marker animations are enabled or not. Enabled by default.</li>
     * <li><b>font</b> - An Object defining the font style to be used in the chart. Defaults to <tt>{ name: 'Tahoma', color: 0x444444, size: 11 }</tt><br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>name</b> - font name</li>
     *      <li><b>color</b> - font color (hex code, ie: "#ff0000", "ff0000" or 0xff0000)</li>
     *      <li><b>size</b> - font size in points (numeric portion only, ie: 11)</li>
     *      <li><b>bold</b> - boolean</li>
     *      <li><b>italic</b> - boolean</li>
     *      <li><b>underline</b> - boolean</li>
     *  </ul>
     * </li>
     * <li><b>border</b> - An object defining the border style around the chart. The chart itself will decrease in dimensions to accomodate the border.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color</b> - border color (hex code, ie: "#ff0000", "ff0000" or 0xff0000)</li>
     *      <li><b>size</b> - border size in pixels (numeric portion only, ie: 1)</li>
     *  </ul>
     * </li>
     * <li><b>background</b> - An object defining the background style of the chart.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color</b> - border color (hex code, ie: "#ff0000", "ff0000" or 0xff0000)</li>
     *      <li><b>image</b> - an image URL. May be relative to the current document or absolute.</li>
     *  </ul>
     * </li>
     * <li><b>legend</b> - An object defining the legend style<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>display</b> - location of the legend. Possible values are "none", "left", "right", "top", and "bottom".</li>
     *      <li><b>spacing</b> - an image URL. May be relative to the current document or absolute.</li>
     *      <li><b>padding, border, background, font</b> - same options as described above.</li>
     *  </ul></li>
     * <li><b>dataTip</b> - An object defining the style of the data tip (tooltip).<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>padding, border, background, font</b> - same options as described above.</li>
     *  </ul></li>
     * <li><b>xAxis and yAxis</b> - An object defining the style of the style of either axis.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color</b> - same option as described above.</li>
     *      <li><b>size</b> - same option as described above.</li>
     *      <li><b>showLabels</b> - boolean</li>
     *      <li><b>labelRotation</b> - a value in degrees from -90 through 90. Default is zero.</li>
     *  </ul></li>
     * <li><b>majorGridLines and minorGridLines</b> - An object defining the style of the style of the grid lines.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color, size</b> - same options as described above.</li>
     *  </ul></li></li>
     * <li><b>zeroGridLine</b> - An object defining the style of the style of the zero grid line.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color, size</b> - same options as described above.</li>
     *  </ul></li></li>
     * <li><b>majorTicks and minorTicks</b> - An object defining the style of the style of ticks in the chart.<br/>
     *  <ul style="padding:5px;padding-left:26px;list-style-type:circle;">
     *      <li><b>color, size</b> - same options as described above.</li>
     *      <li><b>length</b> - the length of each tick in pixels extending from the axis.</li>
     *      <li><b>display</b> - how the ticks are drawn. Possible values are "none", "inside", "outside", and "cross".</li>
     *  </ul></li></li>
     * </ul>
     */
    extraStyle: null,

    /**
     * @cfg {Object} seriesStyles
     * Contains styles to apply to the series after a refresh. Defaults to <tt>null</tt>.
     */
    seriesStyles: null,

    /**
     * @cfg {Boolean} disableCaching
     * True to add a "cache buster" to the end of the chart url. Defaults to true for Opera and IE.
     */
    disableCaching: Ext.isIE || Ext.isOpera,
    disableCacheParam: '_dc',

    initComponent : function(){
        Ext.chart.Chart.superclass.initComponent.call(this);
        if(!this.url){
            this.url = Ext.chart.Chart.CHART_URL;
        }
        if(this.disableCaching){
            this.url = Ext.urlAppend(this.url, String.format('{0}={1}', this.disableCacheParam, new Date().getTime()));
        }
        this.addEvents(
            'itemmouseover',
            'itemmouseout',
            'itemclick',
            'itemdoubleclick',
            'itemdragstart',
            'itemdrag',
            'itemdragend',
            /**
             * @event beforerefresh
             * Fires before a refresh to the chart data is called.  If the beforerefresh handler returns
             * <tt>false</tt> the {@link #refresh} action will be cancelled.
             * @param {Chart} this
             */
            'beforerefresh',
            /**
             * @event refresh
             * Fires after the chart data has been refreshed.
             * @param {Chart} this
             */
            'refresh'
        );
        this.store = Ext.StoreMgr.lookup(this.store);
    },

    /**
     * Sets a single style value on the Chart instance.
     *
     * @param name {String} Name of the Chart style value to change.
     * @param value {Object} New value to pass to the Chart style.
     */
     setStyle: function(name, value){
         this.swf.setStyle(name, Ext.encode(value));
     },

    /**
     * Resets all styles on the Chart instance.
     *
     * @param styles {Object} Initializer for all Chart styles.
     */
    setStyles: function(styles){
        this.swf.setStyles(Ext.encode(styles));
    },

    /**
     * Sets the styles on all series in the Chart.
     *
     * @param styles {Array} Initializer for all Chart series styles.
     */
    setSeriesStyles: function(styles){
        this.seriesStyles = styles;
        var s = [];
        Ext.each(styles, function(style){
            s.push(Ext.encode(style));
        });
        this.swf.setSeriesStyles(s);
    },

    setCategoryNames : function(names){
        this.swf.setCategoryNames(names);
    },

    setLegendRenderer : function(fn, scope){
        var chart = this;
        scope = scope || chart;
        chart.removeFnProxy(chart.legendFnName);
        chart.legendFnName = chart.createFnProxy(function(name){
            return fn.call(scope, name);
        });
        chart.swf.setLegendLabelFunction(chart.legendFnName);
    },

    setTipRenderer : function(fn, scope){
        var chart = this;
        scope = scope || chart;
        chart.removeFnProxy(chart.tipFnName);
        chart.tipFnName = chart.createFnProxy(function(item, index, series){
            var record = chart.store.getAt(index);
            return fn.call(scope, chart, record, index, series);
        });
        chart.swf.setDataTipFunction(chart.tipFnName);
    },

    setSeries : function(series){
        this.series = series;
        this.refresh();
    },

    /**
     * Changes the data store bound to this chart and refreshes it.
     * @param {Store} store The store to bind to this chart
     */
    bindStore : function(store, initial){
        if(!initial && this.store){
            if(store !== this.store && this.store.autoDestroy){
                this.store.destroy();
            }else{
                this.store.un("datachanged", this.refresh, this);
                this.store.un("add", this.delayRefresh, this);
                this.store.un("remove", this.delayRefresh, this);
                this.store.un("update", this.delayRefresh, this);
                this.store.un("clear", this.refresh, this);
            }
        }
        if(store){
            store = Ext.StoreMgr.lookup(store);
            store.on({
                scope: this,
                datachanged: this.refresh,
                add: this.delayRefresh,
                remove: this.delayRefresh,
                update: this.delayRefresh,
                clear: this.refresh
            });
        }
        this.store = store;
        if(store && !initial){
            this.refresh();
        }
    },

    onSwfReady : function(isReset){
        Ext.chart.Chart.superclass.onSwfReady.call(this, isReset);
        var ref;
        this.swf.setType(this.type);

        if(this.chartStyle){
            this.setStyles(Ext.apply({}, this.extraStyle, this.chartStyle));
        }

        if(this.categoryNames){
            this.setCategoryNames(this.categoryNames);
        }

        if(this.tipRenderer){
            ref = this.getFunctionRef(this.tipRenderer);
            this.setTipRenderer(ref.fn, ref.scope);
        }
        if(this.legendRenderer){
            ref = this.getFunctionRef(this.legendRenderer);
            this.setLegendRenderer(ref.fn, ref.scope);
        }
        if(!isReset){
            this.bindStore(this.store, true);
        }
        this.refresh.defer(10, this);
    },

    delayRefresh : function(){
        if(!this.refreshTask){
            this.refreshTask = new Ext.util.DelayedTask(this.refresh, this);
        }
        this.refreshTask.delay(this.refreshBuffer);
    },

    refresh : function(){
        if(this.fireEvent('beforerefresh', this) !== false){
            var styleChanged = false;
            // convert the store data into something YUI charts can understand
            var data = [], rs = this.store.data.items;
            for(var j = 0, len = rs.length; j < len; j++){
                data[j] = rs[j].data;
            }
            //make a copy of the series definitions so that we aren't
            //editing them directly.
            var dataProvider = [];
            var seriesCount = 0;
            var currentSeries = null;
            var i = 0;
            if(this.series){
                seriesCount = this.series.length;
                for(i = 0; i < seriesCount; i++){
                    currentSeries = this.series[i];
                    var clonedSeries = {};
                    for(var prop in currentSeries){
                        if(prop == "style" && currentSeries.style !== null){
                            clonedSeries.style = Ext.encode(currentSeries.style);
                            styleChanged = true;
                            //we don't want to modify the styles again next time
                            //so null out the style property.
                            // this causes issues
                            // currentSeries.style = null;
                        } else{
                            clonedSeries[prop] = currentSeries[prop];
                        }
                    }
                    dataProvider.push(clonedSeries);
                }
            }

            if(seriesCount > 0){
                for(i = 0; i < seriesCount; i++){
                    currentSeries = dataProvider[i];
                    if(!currentSeries.type){
                        currentSeries.type = this.type;
                    }
                    currentSeries.dataProvider = data;
                }
            } else{
                dataProvider.push({type: this.type, dataProvider: data});
            }
            this.swf.setDataProvider(dataProvider);
            if(this.seriesStyles){
                this.setSeriesStyles(this.seriesStyles);
            }
            this.fireEvent('refresh', this);
        }
    },

    // private
    createFnProxy : function(fn){
        var fnName = 'extFnProxy' + (++Ext.chart.Chart.PROXY_FN_ID);
        Ext.chart.Chart.proxyFunction[fnName] = fn;
        return 'Ext.chart.Chart.proxyFunction.' + fnName;
    },

    // private
    removeFnProxy : function(fn){
        if(!Ext.isEmpty(fn)){
            fn = fn.replace('Ext.chart.Chart.proxyFunction.', '');
            delete Ext.chart.Chart.proxyFunction[fn];
        }
    },

    // private
    getFunctionRef : function(val){
        if(Ext.isFunction(val)){
            return {
                fn: val,
                scope: this
            };
        }else{
            return {
                fn: val.fn,
                scope: val.scope || this
            }
        }
    },

    // private
    onDestroy: function(){
        if (this.refreshTask && this.refreshTask.cancel){
            this.refreshTask.cancel();
        }
        Ext.chart.Chart.superclass.onDestroy.call(this);
        this.bindStore(null);
        this.removeFnProxy(this.tipFnName);
        this.removeFnProxy(this.legendFnName);
    }
});
Ext.reg('chart', Ext.chart.Chart);
Ext.chart.Chart.PROXY_FN_ID = 0;
Ext.chart.Chart.proxyFunction = {};

/**
 * Sets the url to load the chart from. This should be set to a local resource.
 * @static
 * @type String
 */
Ext.chart.Chart.CHART_URL = extpath+'/charts.swf';

/**
 * @class Ext.chart.PieChart
 * @extends Ext.chart.Chart
 * @constructor
 * @xtype piechart
 */
Ext.chart.PieChart = Ext.extend(Ext.chart.Chart, {
    type: 'pie',

    onSwfReady : function(isReset){
        Ext.chart.PieChart.superclass.onSwfReady.call(this, isReset);

        this.setDataField(this.dataField);
        this.setCategoryField(this.categoryField);
    },

    setDataField : function(field){
        this.dataField = field;
        this.swf.setDataField(field);
    },

    setCategoryField : function(field){
        this.categoryField = field;
        this.swf.setCategoryField(field);
    }
});
Ext.reg('piechart', Ext.chart.PieChart);

/**
 * @class Ext.chart.CartesianChart
 * @extends Ext.chart.Chart
 * @constructor
 * @xtype cartesianchart
 */
Ext.chart.CartesianChart = Ext.extend(Ext.chart.Chart, {
    onSwfReady : function(isReset){
        Ext.chart.CartesianChart.superclass.onSwfReady.call(this, isReset);
        this.labelFn = [];
        if(this.xField){
            this.setXField(this.xField);
        }
        if(this.yField){
            this.setYField(this.yField);
        }
        if(this.xAxis){
            this.setXAxis(this.xAxis);
        }
        if(this.xAxes){
            this.setXAxes(this.xAxes);
        }
        if(this.yAxis){
            this.setYAxis(this.yAxis);
        }
        if(this.yAxes){
            this.setYAxes(this.yAxes);
        }
        if(Ext.isDefined(this.constrainViewport)){
            this.swf.setConstrainViewport(this.constrainViewport);
        }
    },

    setXField : function(value){
        this.xField = value;
        this.swf.setHorizontalField(value);
    },

    setYField : function(value){
        this.yField = value;
        this.swf.setVerticalField(value);
    },

    setXAxis : function(value){
        this.xAxis = this.createAxis('xAxis', value);
        this.swf.setHorizontalAxis(this.xAxis);
    },

    setXAxes : function(value){
        var axis;
        for(var i = 0; i < value.length; i++) {
            axis = this.createAxis('xAxis' + i, value[i]);
            this.swf.setHorizontalAxis(axis);
        }
    },

    setYAxis : function(value){
        this.yAxis = this.createAxis('yAxis', value);
        this.swf.setVerticalAxis(this.yAxis);
    },

    setYAxes : function(value){
        var axis;
        for(var i = 0; i < value.length; i++) {
            axis = this.createAxis('yAxis' + i, value[i]);
            this.swf.setVerticalAxis(axis);
        }
    },

    createAxis : function(axis, value){
        var o = Ext.apply({}, value),
            ref,
            old;

        if(this[axis]){
            old = this[axis].labelFunction;
            this.removeFnProxy(old);
            this.labelFn.remove(old);
        }
        if(o.labelRenderer){
            ref = this.getFunctionRef(o.labelRenderer);
            o.labelFunction = this.createFnProxy(function(v){
                return ref.fn.call(ref.scope, v);
            });
            delete o.labelRenderer;
            this.labelFn.push(o.labelFunction);
        }
        if(axis.indexOf('xAxis') > -1 && o.position == 'left'){
            o.position = 'bottom';
        }
        return o;
    },

    onDestroy : function(){
        Ext.chart.CartesianChart.superclass.onDestroy.call(this);
        Ext.each(this.labelFn, function(fn){
            this.removeFnProxy(fn);
        }, this);
    }
});
Ext.reg('cartesianchart', Ext.chart.CartesianChart);

/**
 * @class Ext.chart.LineChart
 * @extends Ext.chart.CartesianChart
 * @constructor
 * @xtype linechart
 */
Ext.chart.LineChart = Ext.extend(Ext.chart.CartesianChart, {
    type: 'line'
});
Ext.reg('linechart', Ext.chart.LineChart);

/**
 * @class Ext.chart.ColumnChart
 * @extends Ext.chart.CartesianChart
 * @constructor
 * @xtype columnchart
 */
Ext.chart.ColumnChart = Ext.extend(Ext.chart.CartesianChart, {
    type: 'column'
});
Ext.reg('columnchart', Ext.chart.ColumnChart);

/**
 * @class Ext.chart.StackedColumnChart
 * @extends Ext.chart.CartesianChart
 * @constructor
 * @xtype stackedcolumnchart
 */
Ext.chart.StackedColumnChart = Ext.extend(Ext.chart.CartesianChart, {
    type: 'stackcolumn'
});
Ext.reg('stackedcolumnchart', Ext.chart.StackedColumnChart);

/**
 * @class Ext.chart.BarChart
 * @extends Ext.chart.CartesianChart
 * @constructor
 * @xtype barchart
 */
Ext.chart.BarChart = Ext.extend(Ext.chart.CartesianChart, {
    type: 'bar'
});
Ext.reg('barchart', Ext.chart.BarChart);

/**
 * @class Ext.chart.StackedBarChart
 * @extends Ext.chart.CartesianChart
 * @constructor
 * @xtype stackedbarchart
 */
Ext.chart.StackedBarChart = Ext.extend(Ext.chart.CartesianChart, {
    type: 'stackbar'
});
Ext.reg('stackedbarchart', Ext.chart.StackedBarChart);



/**
 * @class Ext.chart.Axis
 * Defines a CartesianChart's vertical or horizontal axis.
 * @constructor
 */
Ext.chart.Axis = function(config){
    Ext.apply(this, config);
};

Ext.chart.Axis.prototype =
{
    /**
     * The type of axis.
     *
     * @property type
     * @type String
     */
    type: null,

    /**
     * The direction in which the axis is drawn. May be "horizontal" or "vertical".
     *
     * @property orientation
     * @type String
     */
    orientation: "horizontal",

    /**
     * If true, the items on the axis will be drawn in opposite direction.
     *
     * @property reverse
     * @type Boolean
     */
    reverse: false,

    /**
     * A string reference to the globally-accessible function that may be called to
     * determine each of the label values for this axis.
     *
     * @property labelFunction
     * @type String
     */
    labelFunction: null,

    /**
     * If true, labels that overlap previously drawn labels on the axis will be hidden.
     *
     * @property hideOverlappingLabels
     * @type Boolean
     */
    hideOverlappingLabels: true,

    /**
     * The space, in pixels, between labels on an axis.
     *
     * @property labelSpacing
     * @type Number
     */
    labelSpacing: 2
};

/**
 * @class Ext.chart.NumericAxis
 * @extends Ext.chart.Axis
 * A type of axis whose units are measured in numeric values.
 * @constructor
 */
Ext.chart.NumericAxis = Ext.extend(Ext.chart.Axis, {
    type: "numeric",

    /**
     * The minimum value drawn by the axis. If not set explicitly, the axis
     * minimum will be calculated automatically.
     *
     * @property minimum
     * @type Number
     */
    minimum: NaN,

    /**
     * The maximum value drawn by the axis. If not set explicitly, the axis
     * maximum will be calculated automatically.
     *
     * @property maximum
     * @type Number
     */
    maximum: NaN,

    /**
     * The spacing between major intervals on this axis.
     *
     * @property majorUnit
     * @type Number
     */
    majorUnit: NaN,

    /**
     * The spacing between minor intervals on this axis.
     *
     * @property minorUnit
     * @type Number
     */
    minorUnit: NaN,

    /**
     * If true, the labels, ticks, gridlines, and other objects will snap to the
     * nearest major or minor unit. If false, their position will be based on
     * the minimum value.
     *
     * @property snapToUnits
     * @type Boolean
     */
    snapToUnits: true,

    /**
     * If true, and the bounds are calculated automatically, either the minimum
     * or maximum will be set to zero.
     *
     * @property alwaysShowZero
     * @type Boolean
     */
    alwaysShowZero: true,

    /**
     * The scaling algorithm to use on this axis. May be "linear" or
     * "logarithmic".
     *
     * @property scale
     * @type String
     */
    scale: "linear",

    /**
     * Indicates whether to round the major unit.
     *
     * @property roundMajorUnit
     * @type Boolean
     */
    roundMajorUnit: true,

    /**
     * Indicates whether to factor in the size of the labels when calculating a
     * major unit.
     *
     * @property calculateByLabelSize
     * @type Boolean
     */
    calculateByLabelSize: true,

    /**
     * Indicates the position of the axis relative to the chart
     *
     * @property position
     * @type String
     */
    position: 'left',

    /**
     * Indicates whether to extend maximum beyond data's maximum to the nearest
     * majorUnit.
     *
     * @property adjustMaximumByMajorUnit
     * @type Boolean
     */
    adjustMaximumByMajorUnit: true,

    /**
     * Indicates whether to extend the minimum beyond data's minimum to the
     * nearest majorUnit.
     *
     * @property adjustMinimumByMajorUnit
     * @type Boolean
     */
    adjustMinimumByMajorUnit: true

});

/**
 * @class Ext.chart.TimeAxis
 * @extends Ext.chart.Axis
 * A type of axis whose units are measured in time-based values.
 * @constructor
 */
Ext.chart.TimeAxis = Ext.extend(Ext.chart.Axis, {
    type: "time",

    /**
     * The minimum value drawn by the axis. If not set explicitly, the axis
     * minimum will be calculated automatically.
     *
     * @property minimum
     * @type Date
     */
    minimum: null,

    /**
     * The maximum value drawn by the axis. If not set explicitly, the axis
     * maximum will be calculated automatically.
     *
     * @property maximum
     * @type Number
     */
    maximum: null,

    /**
     * The spacing between major intervals on this axis.
     *
     * @property majorUnit
     * @type Number
     */
    majorUnit: NaN,

    /**
     * The time unit used by the majorUnit.
     *
     * @property majorTimeUnit
     * @type String
     */
    majorTimeUnit: null,

    /**
     * The spacing between minor intervals on this axis.
     *
     * @property majorUnit
     * @type Number
     */
    minorUnit: NaN,

    /**
     * The time unit used by the minorUnit.
     *
     * @property majorTimeUnit
     * @type String
     */
    minorTimeUnit: null,

    /**
     * If true, the labels, ticks, gridlines, and other objects will snap to the
     * nearest major or minor unit. If false, their position will be based on
     * the minimum value.
     *
     * @property snapToUnits
     * @type Boolean
     */
    snapToUnits: true,

    /**
     * Series that are stackable will only stack when this value is set to true.
     *
     * @property stackingEnabled
     * @type Boolean
     */
    stackingEnabled: false,

    /**
     * Indicates whether to factor in the size of the labels when calculating a
     * major unit.
     *
     * @property calculateByLabelSize
     * @type Boolean
     */
    calculateByLabelSize: true

});

/**
 * @class Ext.chart.CategoryAxis
 * @extends Ext.chart.Axis
 * A type of axis that displays items in categories.
 * @constructor
 */
Ext.chart.CategoryAxis = Ext.extend(Ext.chart.Axis, {
    type: "category",

    /**
     * A list of category names to display along this axis.
     *
     * @property categoryNames
     * @type Array
     */
    categoryNames: null,

    /**
     * Indicates whether or not to calculate the number of categories (ticks and
     * labels) when there is not enough room to display all labels on the axis.
     * If set to true, the axis will determine the number of categories to plot.
     * If not, all categories will be plotted.
     *
     * @property calculateCategoryCount
     * @type Boolean
     */
    calculateCategoryCount: false

});

/**
 * @class Ext.chart.Series
 * Series class for the charts widget.
 * @constructor
 */
Ext.chart.Series = function(config) { Ext.apply(this, config); };

Ext.chart.Series.prototype =
{
    /**
     * The type of series.
     *
     * @property type
     * @type String
     */
    type: null,

    /**
     * The human-readable name of the series.
     *
     * @property displayName
     * @type String
     */
    displayName: null
};

/**
 * @class Ext.chart.CartesianSeries
 * @extends Ext.chart.Series
 * CartesianSeries class for the charts widget.
 * @constructor
 */
Ext.chart.CartesianSeries = Ext.extend(Ext.chart.Series, {
    /**
     * The field used to access the x-axis value from the items from the data
     * source.
     *
     * @property xField
     * @type String
     */
    xField: null,

    /**
     * The field used to access the y-axis value from the items from the data
     * source.
     *
     * @property yField
     * @type String
     */
    yField: null,

    /**
     * False to not show this series in the legend. Defaults to <tt>true</tt>.
     *
     * @property showInLegend
     * @type Boolean
     */
    showInLegend: true,

    /**
     * Indicates which axis the series will bind to
     *
     * @property axis
     * @type String
     */
    axis: 'primary'
});

/**
 * @class Ext.chart.ColumnSeries
 * @extends Ext.chart.CartesianSeries
 * ColumnSeries class for the charts widget.
 * @constructor
 */
Ext.chart.ColumnSeries = Ext.extend(Ext.chart.CartesianSeries, {
    type: "column"
});

/**
 * @class Ext.chart.LineSeries
 * @extends Ext.chart.CartesianSeries
 * LineSeries class for the charts widget.
 * @constructor
 */
Ext.chart.LineSeries = Ext.extend(Ext.chart.CartesianSeries, {
    type: "line"
});

/**
 * @class Ext.chart.BarSeries
 * @extends Ext.chart.CartesianSeries
 * BarSeries class for the charts widget.
 * @constructor
 */
Ext.chart.BarSeries = Ext.extend(Ext.chart.CartesianSeries, {
    type: "bar"
});


/**
 * @class Ext.chart.PieSeries
 * @extends Ext.chart.Series
 * PieSeries class for the charts widget.
 * @constructor
 */
Ext.chart.PieSeries = Ext.extend(Ext.chart.Series, {
    type: "pie",
    dataField: null,
    categoryField: null
});
Ext.menu.Menu = Ext.extend(Ext.Container, {
    
    
    
    minWidth : 120,
    
    shadow : 'sides',
    
    subMenuAlign : 'tl-tr?',
    
    defaultAlign : 'tl-bl?',
    
    allowOtherMenus : false,
    
    ignoreParentClicks : false,
    
    enableScrolling : true,
    
    maxHeight : null,
    
    scrollIncrement : 24,
    
    showSeparator : true,
    
    defaultOffsets : [0, 0],

    
    plain : false,

    
    floating : true,


    
    zIndex: 15000,

    
    hidden : true,

    
    layout : 'menu',
    hideMode : 'offsets',    
    scrollerHeight : 8,
    autoLayout : true,       
    defaultType : 'menuitem',
    bufferResize : false,

    initComponent : function(){
        if(Ext.isArray(this.initialConfig)){
            Ext.apply(this, {items:this.initialConfig});
        }
        this.addEvents(
            
            'click',
            
            'mouseover',
            
            'mouseout',
            
            'itemclick'
        );
        Ext.menu.MenuMgr.register(this);
        if(this.floating){
            Ext.EventManager.onWindowResize(this.hide, this);
        }else{
            if(this.initialConfig.hidden !== false){
                this.hidden = false;
            }
            this.internalDefaults = {hideOnClick: false};
        }
        Ext.menu.Menu.superclass.initComponent.call(this);
        if(this.autoLayout){
            var fn = this.doLayout.createDelegate(this, []);
            this.on({
                add: fn,
                remove: fn
            });
        }
    },

    
    getLayoutTarget : function() {
        return this.ul;
    },

    
    onRender : function(ct, position){
        if(!ct){
            ct = Ext.getBody();
        }

        var dh = {
            id: this.getId(),
            cls: 'x-menu ' + ((this.floating) ? 'x-menu-floating x-layer ' : '') + (this.cls || '') + (this.plain ? ' x-menu-plain' : '') + (this.showSeparator ? '' : ' x-menu-nosep'),
            style: this.style,
            cn: [
                {tag: 'a', cls: 'x-menu-focus', href: '#', onclick: 'return false;', tabIndex: '-1'},
                {tag: 'ul', cls: 'x-menu-list'}
            ]
        };
        if(this.floating){
            this.el = new Ext.Layer({
                shadow: this.shadow,
                dh: dh,
                constrain: false,
                parentEl: ct,
                zindex: this.zIndex
            });
        }else{
            this.el = ct.createChild(dh);
        }
        Ext.menu.Menu.superclass.onRender.call(this, ct, position);

        if(!this.keyNav){
            this.keyNav = new Ext.menu.MenuNav(this);
        }
        
        this.focusEl = this.el.child('a.x-menu-focus');
        this.ul = this.el.child('ul.x-menu-list');
        this.mon(this.ul, {
            scope: this,
            click: this.onClick,
            mouseover: this.onMouseOver,
            mouseout: this.onMouseOut
        });
        if(this.enableScrolling){
            this.mon(this.el, {
                scope: this,
                delegate: '.x-menu-scroller',
                click: this.onScroll,
                mouseover: this.deactivateActive
            });
        }
    },

    
    findTargetItem : function(e){
        var t = e.getTarget('.x-menu-list-item', this.ul, true);
        if(t && t.menuItemId){
            return this.items.get(t.menuItemId);
        }
    },

    
    onClick : function(e){
        var t = this.findTargetItem(e);
        if(t){
            if(t.isFormField){
                this.setActiveItem(t);
            }else if(t instanceof Ext.menu.BaseItem){
                if(t.menu && this.ignoreParentClicks){
                    t.expandMenu();
                    e.preventDefault();
                }else if(t.onClick){
                    t.onClick(e);
                    this.fireEvent('click', this, t, e);
                }
            }
        }
    },

    
    setActiveItem : function(item, autoExpand){
        if(item != this.activeItem){
            this.deactivateActive();
            if((this.activeItem = item).isFormField){
                item.focus();
            }else{
                item.activate(autoExpand);
            }
        }else if(autoExpand){
            item.expandMenu();
        }
    },

    deactivateActive : function(){
        var a = this.activeItem;
        if(a){
            if(a.isFormField){
                
                if(a.collapse){
                    a.collapse();
                }
            }else{
                a.deactivate();
            }
            delete this.activeItem;
        }
    },

    
    tryActivate : function(start, step){
        var items = this.items;
        for(var i = start, len = items.length; i >= 0 && i < len; i+= step){
            var item = items.get(i);
            if(!item.disabled && (item.canActivate || item.isFormField)){
                this.setActiveItem(item, false);
                return item;
            }
        }
        return false;
    },

    
    onMouseOver : function(e){
        var t = this.findTargetItem(e);
        if(t){
            if(t.canActivate && !t.disabled){
                this.setActiveItem(t, true);
            }
        }
        this.over = true;
        this.fireEvent('mouseover', this, e, t);
    },

    
    onMouseOut : function(e){
        var t = this.findTargetItem(e);
        if(t){
            if(t == this.activeItem && t.shouldDeactivate && t.shouldDeactivate(e)){
                this.activeItem.deactivate();
                delete this.activeItem;
            }
        }
        this.over = false;
        this.fireEvent('mouseout', this, e, t);
    },

    
    onScroll : function(e, t){
        if(e){
            e.stopEvent();
        }
        var ul = this.ul.dom, top = Ext.fly(t).is('.x-menu-scroller-top');
        ul.scrollTop += this.scrollIncrement * (top ? -1 : 1);
        if(top ? ul.scrollTop <= 0 : ul.scrollTop + this.activeMax >= ul.scrollHeight){
           this.onScrollerOut(null, t);
        }
    },

    
    onScrollerIn : function(e, t){
        var ul = this.ul.dom, top = Ext.fly(t).is('.x-menu-scroller-top');
        if(top ? ul.scrollTop > 0 : ul.scrollTop + this.activeMax < ul.scrollHeight){
            Ext.fly(t).addClass(['x-menu-item-active', 'x-menu-scroller-active']);
        }
    },

    
    onScrollerOut : function(e, t){
        Ext.fly(t).removeClass(['x-menu-item-active', 'x-menu-scroller-active']);
    },

    
    show : function(el, pos, parentMenu){
        if(this.floating){
            this.parentMenu = parentMenu;
            if(!this.el){
                this.render();
                this.doLayout(false, true);
            }
            this.showAt(this.el.getAlignToXY(el, pos || this.defaultAlign, this.defaultOffsets), parentMenu);
        }else{
            Ext.menu.Menu.superclass.show.call(this);
        }
    },

    
    showAt : function(xy, parentMenu){
        if(this.fireEvent('beforeshow', this) !== false){
            this.parentMenu = parentMenu;
            if(!this.el){
                this.render();
            }
            if(this.enableScrolling){
                
                this.el.setXY(xy);
                
                xy[1] = this.constrainScroll(xy[1]);
                xy = [this.el.adjustForConstraints(xy)[0], xy[1]];
            }else{
                
                xy = this.el.adjustForConstraints(xy);
            }
            this.el.setXY(xy);
            this.el.show();
            Ext.menu.Menu.superclass.onShow.call(this);
            if(Ext.isIE){
                
                this.fireEvent('autosize', this);
                if(!Ext.isIE8){
                    this.el.repaint();
                }
            }
            this.hidden = false;
            this.focus();
            this.fireEvent('show', this);
        }
    },

    constrainScroll : function(y){
        var max, full = this.ul.setHeight('auto').getHeight(),
            returnY = y, normalY, parentEl, scrollTop, viewHeight;
        if(this.floating){
            parentEl = Ext.fly(this.el.dom.parentNode);
            scrollTop = parentEl.getScroll().top;
            viewHeight = parentEl.getViewSize().height;
            
            
            normalY = y - scrollTop;
            max = this.maxHeight ? this.maxHeight : viewHeight - normalY;
            if(full > viewHeight) {
                max = viewHeight;
                
                returnY = y - normalY;
            } else if(max < full) {
                returnY = y - (full - max);
                max = full;
            }
        }else{
            max = this.getHeight();
        }
        
        if (this.maxHeight){
            max = Math.min(this.maxHeight, max);
        }
        if(full > max && max > 0){
            this.activeMax = max - this.scrollerHeight * 2 - this.el.getFrameWidth('tb') - Ext.num(this.el.shadowOffset, 0);
            this.ul.setHeight(this.activeMax);
            this.createScrollers();
            this.el.select('.x-menu-scroller').setDisplayed('');
        }else{
            this.ul.setHeight(full);
            this.el.select('.x-menu-scroller').setDisplayed('none');
        }
        this.ul.dom.scrollTop = 0;
        return returnY;
    },

    createScrollers : function(){
        if(!this.scroller){
            this.scroller = {
                pos: 0,
                top: this.el.insertFirst({
                    tag: 'div',
                    cls: 'x-menu-scroller x-menu-scroller-top',
                    html: '&#160;'
                }),
                bottom: this.el.createChild({
                    tag: 'div',
                    cls: 'x-menu-scroller x-menu-scroller-bottom',
                    html: '&#160;'
                })
            };
            this.scroller.top.hover(this.onScrollerIn, this.onScrollerOut, this);
            this.scroller.topRepeater = new Ext.util.ClickRepeater(this.scroller.top, {
                listeners: {
                    click: this.onScroll.createDelegate(this, [null, this.scroller.top], false)
                }
            });
            this.scroller.bottom.hover(this.onScrollerIn, this.onScrollerOut, this);
            this.scroller.bottomRepeater = new Ext.util.ClickRepeater(this.scroller.bottom, {
                listeners: {
                    click: this.onScroll.createDelegate(this, [null, this.scroller.bottom], false)
                }
            });
        }
    },

    onLayout : function(){
        if(this.isVisible()){
            if(this.enableScrolling){
                this.constrainScroll(this.el.getTop());
            }
            if(this.floating){
                this.el.sync();
            }
        }
    },

    focus : function(){
        if(!this.hidden){
            this.doFocus.defer(50, this);
        }
    },

    doFocus : function(){
        if(!this.hidden){
            this.focusEl.focus();
        }
    },

    
    hide : function(deep){
        if (!this.isDestroyed) {
            this.deepHide = deep;
            Ext.menu.Menu.superclass.hide.call(this);
            delete this.deepHide;
        }
    },

    
    onHide : function(){
        Ext.menu.Menu.superclass.onHide.call(this);
        this.deactivateActive();
        if(this.el && this.floating){
            this.el.hide();
        }
        var pm = this.parentMenu;
        if(this.deepHide === true && pm){
            if(pm.floating){
                pm.hide(true);
            }else{
                pm.deactivateActive();
            }
        }
    },

    
    lookupComponent : function(c){
         if(Ext.isString(c)){
            c = (c == 'separator' || c == '-') ? new Ext.menu.Separator() : new Ext.menu.TextItem(c);
             this.applyDefaults(c);
         }else{
            if(Ext.isObject(c)){
                c = this.getMenuItem(c);
            }else if(c.tagName || c.el){ 
                c = new Ext.BoxComponent({
                    el: c
                });
            }
         }
         return c;
    },

    applyDefaults : function(c){
        if(!Ext.isString(c)){
            c = Ext.menu.Menu.superclass.applyDefaults.call(this, c);
            var d = this.internalDefaults;
            if(d){
                if(c.events){
                    Ext.applyIf(c.initialConfig, d);
                    Ext.apply(c, d);
                }else{
                    Ext.applyIf(c, d);
                }
            }
        }
        return c;
    },

    
    getMenuItem : function(config){
       if(!config.isXType){
            if(!config.xtype && Ext.isBoolean(config.checked)){
                return new Ext.menu.CheckItem(config)
            }
            return Ext.create(config, this.defaultType);
        }
        return config;
    },

    
    addSeparator : function(){
        return this.add(new Ext.menu.Separator());
    },

    
    addElement : function(el){
        return this.add(new Ext.menu.BaseItem({
            el: el
        }));
    },

    
    addItem : function(item){
        return this.add(item);
    },

    
    addMenuItem : function(config){
        return this.add(this.getMenuItem(config));
    },

    
    addText : function(text){
        return this.add(new Ext.menu.TextItem(text));
    },

    
    onDestroy : function(){
        Ext.EventManager.removeResizeListener(this.hide, this);
        var pm = this.parentMenu;
        if(pm && pm.activeChild == this){
            delete pm.activeChild;
        }
        delete this.parentMenu;
        Ext.menu.Menu.superclass.onDestroy.call(this);
        Ext.menu.MenuMgr.unregister(this);
        if(this.keyNav) {
            this.keyNav.disable();
        }
        var s = this.scroller;
        if(s){
            Ext.destroy(s.topRepeater, s.bottomRepeater, s.top, s.bottom);
        }
        Ext.destroy(
            this.el,
            this.focusEl,
            this.ul
        );
    }
});

Ext.reg('menu', Ext.menu.Menu);


Ext.menu.MenuNav = Ext.extend(Ext.KeyNav, function(){
    function up(e, m){
        if(!m.tryActivate(m.items.indexOf(m.activeItem)-1, -1)){
            m.tryActivate(m.items.length-1, -1);
        }
    }
    function down(e, m){
        if(!m.tryActivate(m.items.indexOf(m.activeItem)+1, 1)){
            m.tryActivate(0, 1);
        }
    }
    return {
        constructor : function(menu){
            Ext.menu.MenuNav.superclass.constructor.call(this, menu.el);
            this.scope = this.menu = menu;
        },

        doRelay : function(e, h){
            var k = e.getKey();

            if (this.menu.activeItem && this.menu.activeItem.isFormField && k != e.TAB) {
                return false;
            }
            if(!this.menu.activeItem && e.isNavKeyPress() && k != e.SPACE && k != e.RETURN){
                this.menu.tryActivate(0, 1);
                return false;
            }
            return h.call(this.scope || this, e, this.menu);
        },

        tab: function(e, m) {
            e.stopEvent();
            if (e.shiftKey) {
                up(e, m);
            } else {
                down(e, m);
            }
        },

        up : up,

        down : down,

        right : function(e, m){
            if(m.activeItem){
                m.activeItem.expandMenu(true);
            }
        },

        left : function(e, m){
            m.hide();
            if(m.parentMenu && m.parentMenu.activeItem){
                m.parentMenu.activeItem.activate();
            }
        },

        enter : function(e, m){
            if(m.activeItem){
                e.stopPropagation();
                m.activeItem.onClick(e);
                m.fireEvent('click', this, m.activeItem);
                return true;
            }
        }
    };
}());

Ext.menu.MenuMgr = function(){
   var menus, active, groups = {}, attached = false, lastShow = new Date();

   
   function init(){
       menus = {};
       active = new Ext.util.MixedCollection();
       Ext.getDoc().addKeyListener(27, function(){
           if(active.length > 0){
               hideAll();
           }
       });
   }

   
   function hideAll(){
       if(active && active.length > 0){
           var c = active.clone();
           c.each(function(m){
               m.hide();
           });
           return true;
       }
       return false;
   }

   
   function onHide(m){
       active.remove(m);
       if(active.length < 1){
           Ext.getDoc().un("mousedown", onMouseDown);
           attached = false;
       }
   }

   
   function onShow(m){
       var last = active.last();
       lastShow = new Date();
       active.add(m);
       if(!attached){
           Ext.getDoc().on("mousedown", onMouseDown);
           attached = true;
       }
       if(m.parentMenu){
          m.getEl().setZIndex(parseInt(m.parentMenu.getEl().getStyle("z-index"), 10) + 3);
          m.parentMenu.activeChild = m;
       }else if(last && !last.isDestroyed && last.isVisible()){
          m.getEl().setZIndex(parseInt(last.getEl().getStyle("z-index"), 10) + 3);
       }
   }

   
   function onBeforeHide(m){
       if(m.activeChild){
           m.activeChild.hide();
       }
       if(m.autoHideTimer){
           clearTimeout(m.autoHideTimer);
           delete m.autoHideTimer;
       }
   }

   
   function onBeforeShow(m){
       var pm = m.parentMenu;
       if(!pm && !m.allowOtherMenus){
           hideAll();
       }else if(pm && pm.activeChild){
           pm.activeChild.hide();
       }
   }

   
   function onMouseDown(e){
       if(lastShow.getElapsed() > 50 && active.length > 0 && !e.getTarget(".x-menu")){
           hideAll();
       }
   }

   
   function onBeforeCheck(mi, state){
       if(state){
           var g = groups[mi.group];
           for(var i = 0, l = g.length; i < l; i++){
               if(g[i] != mi){
                   g[i].setChecked(false);
               }
           }
       }
   }

   return {

       
       hideAll : function(){
            return hideAll();
       },

       
       register : function(menu){
           if(!menus){
               init();
           }
           menus[menu.id] = menu;
           menu.on({
               beforehide: onBeforeHide,
               hide: onHide,
               beforeshow: onBeforeShow,
               show: onShow
           });
       },

        
       get : function(menu){
           if(typeof menu == "string"){ 
               if(!menus){  
                   return null;
               }
               return menus[menu];
           }else if(menu.events){  
               return menu;
           }else if(typeof menu.length == 'number'){ 
               return new Ext.menu.Menu({items:menu});
           }else{ 
               return Ext.create(menu, 'menu');
           }
       },

       
       unregister : function(menu){
           delete menus[menu.id];
           menu.un("beforehide", onBeforeHide);
           menu.un("hide", onHide);
           menu.un("beforeshow", onBeforeShow);
           menu.un("show", onShow);
       },

       
       registerCheckable : function(menuItem){
           var g = menuItem.group;
           if(g){
               if(!groups[g]){
                   groups[g] = [];
               }
               groups[g].push(menuItem);
               menuItem.on("beforecheckchange", onBeforeCheck);
           }
       },

       
       unregisterCheckable : function(menuItem){
           var g = menuItem.group;
           if(g){
               groups[g].remove(menuItem);
               menuItem.un("beforecheckchange", onBeforeCheck);
           }
       },

       getCheckedItem : function(groupId){
           var g = groups[groupId];
           if(g){
               for(var i = 0, l = g.length; i < l; i++){
                   if(g[i].checked){
                       return g[i];
                   }
               }
           }
           return null;
       },

       setCheckedItem : function(groupId, itemId){
           var g = groups[groupId];
           if(g){
               for(var i = 0, l = g.length; i < l; i++){
                   if(g[i].id == itemId){
                       g[i].setChecked(true);
                   }
               }
           }
           return null;
       }
   };
}();

Ext.menu.BaseItem = Ext.extend(Ext.Component, {
    
    
    
    
    canActivate : false,
    
    activeClass : "x-menu-item-active",
    
    hideOnClick : true,
    
    clickHideDelay : 1,

    
    ctype : "Ext.menu.BaseItem",

    
    actionMode : "container",

    initComponent : function(){
        Ext.menu.BaseItem.superclass.initComponent.call(this);
        this.addEvents(
            
            'click',
            
            'activate',
            
            'deactivate'
        );
        if(this.handler){
            this.on("click", this.handler, this.scope);
        }
    },

    
    onRender : function(container, position){
        Ext.menu.BaseItem.superclass.onRender.apply(this, arguments);
        if(this.ownerCt && this.ownerCt instanceof Ext.menu.Menu){
            this.parentMenu = this.ownerCt;
        }else{
            this.container.addClass('x-menu-list-item');
            this.mon(this.el, {
                scope: this,
                click: this.onClick,
                mouseenter: this.activate,
                mouseleave: this.deactivate
            });
        }
    },

    
    setHandler : function(handler, scope){
        if(this.handler){
            this.un("click", this.handler, this.scope);
        }
        this.on("click", this.handler = handler, this.scope = scope);
    },

    
    onClick : function(e){
        if(!this.disabled && this.fireEvent("click", this, e) !== false
                && (this.parentMenu && this.parentMenu.fireEvent("itemclick", this, e) !== false)){
            this.handleClick(e);
        }else{
            e.stopEvent();
        }
    },

    
    activate : function(){
        if(this.disabled){
            return false;
        }
        var li = this.container;
        li.addClass(this.activeClass);
        this.region = li.getRegion().adjust(2, 2, -2, -2);
        this.fireEvent("activate", this);
        return true;
    },

    
    deactivate : function(){
        this.container.removeClass(this.activeClass);
        this.fireEvent("deactivate", this);
    },

    
    shouldDeactivate : function(e){
        return !this.region || !this.region.contains(e.getPoint());
    },

    
    handleClick : function(e){
        var pm = this.parentMenu;
        if(this.hideOnClick){
            if(pm.floating){
                pm.hide.defer(this.clickHideDelay, pm, [true]);
            }else{
                pm.deactivateActive();
            }
        }
    },

    
    expandMenu : Ext.emptyFn,

    
    hideMenu : Ext.emptyFn
});
Ext.reg('menubaseitem', Ext.menu.BaseItem);
Ext.menu.TextItem = Ext.extend(Ext.menu.BaseItem, {
    
    
    hideOnClick : false,
    
    itemCls : "x-menu-text",
    
    constructor : function(config){
        if(typeof config == 'string'){
            config = {text: config}
        }
        Ext.menu.TextItem.superclass.constructor.call(this, config);
    },

    
    onRender : function(){
        var s = document.createElement("span");
        s.className = this.itemCls;
        s.innerHTML = this.text;
        this.el = s;
        Ext.menu.TextItem.superclass.onRender.apply(this, arguments);
    }
});
Ext.reg('menutextitem', Ext.menu.TextItem);
Ext.menu.Separator = Ext.extend(Ext.menu.BaseItem, {
    
    itemCls : "x-menu-sep",
    
    hideOnClick : false,
    
    
    activeClass: '',

    
    onRender : function(li){
        var s = document.createElement("span");
        s.className = this.itemCls;
        s.innerHTML = "&#160;";
        this.el = s;
        li.addClass("x-menu-sep-li");
        Ext.menu.Separator.superclass.onRender.apply(this, arguments);
    }
});
Ext.reg('menuseparator', Ext.menu.Separator);
Ext.menu.Item = Ext.extend(Ext.menu.BaseItem, {
    
    
    
    
    
    
    
    
    itemCls : 'x-menu-item',
    
    canActivate : true,
    
    showDelay: 200,
    
    hideDelay: 200,

    
    ctype: 'Ext.menu.Item',

    initComponent : function(){
        Ext.menu.Item.superclass.initComponent.call(this);
        if(this.menu){
            this.menu = Ext.menu.MenuMgr.get(this.menu);
            this.menu.ownerCt = this;
        }
    },

    
    onRender : function(container, position){
        if (!this.itemTpl) {
            this.itemTpl = Ext.menu.Item.prototype.itemTpl = new Ext.XTemplate(
                '<a id="{id}" class="{cls}" hidefocus="true" unselectable="on" href="{href}"',
                    '<tpl if="hrefTarget">',
                        ' target="{hrefTarget}"',
                    '</tpl>',
                 '>',
                     '<img src="{icon}" class="x-menu-item-icon {iconCls}"/>',
                     '<span class="x-menu-item-text">{text}</span>',
                 '</a>'
             );
        }
        var a = this.getTemplateArgs();
        this.el = position ? this.itemTpl.insertBefore(position, a, true) : this.itemTpl.append(container, a, true);
        this.iconEl = this.el.child('img.x-menu-item-icon');
        this.textEl = this.el.child('.x-menu-item-text');
        if(!this.href) { 
            this.mon(this.el, 'click', Ext.emptyFn, null, { preventDefault: true });
        }
        Ext.menu.Item.superclass.onRender.call(this, container, position);
    },

    getTemplateArgs: function() {
        return {
            id: this.id,
            cls: this.itemCls + (this.menu ?  ' x-menu-item-arrow' : '') + (this.cls ?  ' ' + this.cls : ''),
            href: this.href || '#',
            hrefTarget: this.hrefTarget,
            icon: this.icon || Ext.BLANK_IMAGE_URL,
            iconCls: this.iconCls || '',
            text: this.itemText||this.text||'&#160;'
        };
    },

    
    setText : function(text){
        this.text = text||'&#160;';
        if(this.rendered){
            this.textEl.update(this.text);
            this.parentMenu.layout.doAutoSize();
        }
    },

    
    setIconClass : function(cls){
        var oldCls = this.iconCls;
        this.iconCls = cls;
        if(this.rendered){
            this.iconEl.replaceClass(oldCls, this.iconCls);
        }
    },

    
    beforeDestroy: function(){
        if (this.menu){
            delete this.menu.ownerCt;
            this.menu.destroy();
        }
        Ext.menu.Item.superclass.beforeDestroy.call(this);
    },

    
    handleClick : function(e){
        if(!this.href){ 
            e.stopEvent();
        }
        Ext.menu.Item.superclass.handleClick.apply(this, arguments);
    },

    
    activate : function(autoExpand){
        if(Ext.menu.Item.superclass.activate.apply(this, arguments)){
            this.focus();
            if(autoExpand){
                this.expandMenu();
            }
        }
        return true;
    },

    
    shouldDeactivate : function(e){
        if(Ext.menu.Item.superclass.shouldDeactivate.call(this, e)){
            if(this.menu && this.menu.isVisible()){
                return !this.menu.getEl().getRegion().contains(e.getPoint());
            }
            return true;
        }
        return false;
    },

    
    deactivate : function(){
        Ext.menu.Item.superclass.deactivate.apply(this, arguments);
        this.hideMenu();
    },

    
    expandMenu : function(autoActivate){
        if(!this.disabled && this.menu){
            clearTimeout(this.hideTimer);
            delete this.hideTimer;
            if(!this.menu.isVisible() && !this.showTimer){
                this.showTimer = this.deferExpand.defer(this.showDelay, this, [autoActivate]);
            }else if (this.menu.isVisible() && autoActivate){
                this.menu.tryActivate(0, 1);
            }
        }
    },

    
    deferExpand : function(autoActivate){
        delete this.showTimer;
        this.menu.show(this.container, this.parentMenu.subMenuAlign || 'tl-tr?', this.parentMenu);
        if(autoActivate){
            this.menu.tryActivate(0, 1);
        }
    },

    
    hideMenu : function(){
        clearTimeout(this.showTimer);
        delete this.showTimer;
        if(!this.hideTimer && this.menu && this.menu.isVisible()){
            this.hideTimer = this.deferHide.defer(this.hideDelay, this);
        }
    },

    
    deferHide : function(){
        delete this.hideTimer;
        if(this.menu.over){
            this.parentMenu.setActiveItem(this, false);
        }else{
            this.menu.hide();
        }
    }
});
Ext.reg('menuitem', Ext.menu.Item);
Ext.menu.CheckItem = Ext.extend(Ext.menu.Item, {
    
    
    itemCls : "x-menu-item x-menu-check-item",
    
    groupClass : "x-menu-group-item",

    
    checked: false,

    
    ctype: "Ext.menu.CheckItem",
    
    initComponent : function(){
        Ext.menu.CheckItem.superclass.initComponent.call(this);
	    this.addEvents(
	        
	        "beforecheckchange" ,
	        
	        "checkchange"
	    );
	    
	    if(this.checkHandler){
	        this.on('checkchange', this.checkHandler, this.scope);
	    }
	    Ext.menu.MenuMgr.registerCheckable(this);
    },

    
    onRender : function(c){
        Ext.menu.CheckItem.superclass.onRender.apply(this, arguments);
        if(this.group){
            this.el.addClass(this.groupClass);
        }
        if(this.checked){
            this.checked = false;
            this.setChecked(true, true);
        }
    },

    
    destroy : function(){
        Ext.menu.MenuMgr.unregisterCheckable(this);
        Ext.menu.CheckItem.superclass.destroy.apply(this, arguments);
    },

    
    setChecked : function(state, suppressEvent){
        var suppress = suppressEvent === true;
        if(this.checked != state && (suppress || this.fireEvent("beforecheckchange", this, state) !== false)){
            if(this.container){
                this.container[state ? "addClass" : "removeClass"]("x-menu-item-checked");
            }
            this.checked = state;
            if(!suppress){
                this.fireEvent("checkchange", this, state);
            }
        }
    },

    
    handleClick : function(e){
       if(!this.disabled && !(this.checked && this.group)){
           this.setChecked(!this.checked);
       }
       Ext.menu.CheckItem.superclass.handleClick.apply(this, arguments);
    }
});
Ext.reg('menucheckitem', Ext.menu.CheckItem);
 Ext.menu.DateMenu = Ext.extend(Ext.menu.Menu, {
    
    enableScrolling : false,
    
        
    
    hideOnClick : true,
    
    
    pickerId : null,
    
    
    
    
    cls : 'x-date-menu',
    
    
    
    

    initComponent : function(){
        this.on('beforeshow', this.onBeforeShow, this);
        if(this.strict = (Ext.isIE7 && Ext.isStrict)){
            this.on('show', this.onShow, this, {single: true, delay: 20});
        }
        Ext.apply(this, {
            plain: true,
            showSeparator: false,
            items: this.picker = new Ext.DatePicker(Ext.applyIf({
                internalRender: this.strict || !Ext.isIE,
                ctCls: 'x-menu-date-item',
                id: this.pickerId
            }, this.initialConfig))
        });
        this.picker.purgeListeners();
        Ext.menu.DateMenu.superclass.initComponent.call(this);
        
        this.relayEvents(this.picker, ['select']);
        this.on('show', this.picker.focus, this.picker);
        this.on('select', this.menuHide, this);
        if(this.handler){
            this.on('select', this.handler, this.scope || this);
        }
    },

    menuHide : function() {
        if(this.hideOnClick){
            this.hide(true);
        }
    },

    onBeforeShow : function(){
        if(this.picker){
            this.picker.hideMonthPicker(true);
        }
    },

    onShow : function(){
        var el = this.picker.getEl();
        el.setWidth(el.getWidth()); 
    }
 });
 Ext.reg('datemenu', Ext.menu.DateMenu);
 
 Ext.menu.ColorMenu = Ext.extend(Ext.menu.Menu, {
    
    enableScrolling : false,
    
        
    
    
    hideOnClick : true,
    
    cls : 'x-color-menu',
    
    
    paletteId : null,
    
    
    
    
    
    
    
    
    
    
    initComponent : function(){
        Ext.apply(this, {
            plain: true,
            showSeparator: false,
            items: this.palette = new Ext.ColorPalette(Ext.applyIf({
                id: this.paletteId
            }, this.initialConfig))
        });
        this.palette.purgeListeners();
        Ext.menu.ColorMenu.superclass.initComponent.call(this);
        
        this.relayEvents(this.palette, ['select']);
        this.on('select', this.menuHide, this);
        if(this.handler){
            this.on('select', this.handler, this.scope || this);
        }
    },

    menuHide : function(){
        if(this.hideOnClick){
            this.hide(true);
        }
    }
});
Ext.reg('colormenu', Ext.menu.ColorMenu);

Ext.form.Field = Ext.extend(Ext.BoxComponent,  {
    
    
    
    
    
    

    
    invalidClass : 'x-form-invalid',
    
    invalidText : 'The value in this field is invalid',
    
    focusClass : 'x-form-focus',
    
    
    validationEvent : 'keyup',
    
    validateOnBlur : true,
    
    validationDelay : 250,
    
    defaultAutoCreate : {tag: 'input', type: 'text', size: '20', autocomplete: 'off'},
    
    fieldClass : 'x-form-field',
    
    msgTarget : 'qtip',
    
    msgFx : 'normal',
    
    readOnly : false,
    
    disabled : false,
    
    submitValue: true,

    
    isFormField : true,

    
    msgDisplay: '',

    
    hasFocus : false,

    
    initComponent : function(){
        Ext.form.Field.superclass.initComponent.call(this);
        this.addEvents(
            
            'focus',
            
            'blur',
            
            'specialkey',
            
            'change',
            
            'invalid',
            
            'valid'
        );
    },

    
    getName : function(){
        return this.rendered && this.el.dom.name ? this.el.dom.name : this.name || this.id || '';
    },

    
    onRender : function(ct, position){
        if(!this.el){
            var cfg = this.getAutoCreate();

            if(!cfg.name){
                cfg.name = this.name || this.id;
            }
            if(this.inputType){
                cfg.type = this.inputType;
            }
            this.autoEl = cfg;
        }
        Ext.form.Field.superclass.onRender.call(this, ct, position);
        if(this.submitValue === false){
            this.el.dom.removeAttribute('name');
        }
        var type = this.el.dom.type;
        if(type){
            if(type == 'password'){
                type = 'text';
            }
            this.el.addClass('x-form-'+type);
        }
        if(this.readOnly){
            this.setReadOnly(true);
        }
        if(this.tabIndex !== undefined){
            this.el.dom.setAttribute('tabIndex', this.tabIndex);
        }

        this.el.addClass([this.fieldClass, this.cls]);
    },

    
    getItemCt : function(){
        return this.itemCt;
    },

    
    initValue : function(){
        if(this.value !== undefined){
            this.setValue(this.value);
        }else if(!Ext.isEmpty(this.el.dom.value) && this.el.dom.value != this.emptyText){
            this.setValue(this.el.dom.value);
        }
        
        this.originalValue = this.getValue();
    },

    
    isDirty : function() {
        if(this.disabled || !this.rendered) {
            return false;
        }
        return String(this.getValue()) !== String(this.originalValue);
    },

    
    setReadOnly : function(readOnly){
        if(this.rendered){
            this.el.dom.readOnly = readOnly;
        }
        this.readOnly = readOnly;
    },

    
    afterRender : function(){
        Ext.form.Field.superclass.afterRender.call(this);
        this.initEvents();
        this.initValue();
    },

    
    fireKey : function(e){
        if(e.isSpecialKey()){
            this.fireEvent('specialkey', this, e);
        }
    },

    
    reset : function(){
        this.setValue(this.originalValue);
        this.clearInvalid();
    },

    
    initEvents : function(){
        this.mon(this.el, Ext.EventManager.useKeydown ? 'keydown' : 'keypress', this.fireKey,  this);
        this.mon(this.el, 'focus', this.onFocus, this);

        
        
        this.mon(this.el, 'blur', this.onBlur, this, this.inEditor ? {buffer:10} : null);
    },

    
    preFocus: Ext.emptyFn,

    
    onFocus : function(){
        this.preFocus();
        if(this.focusClass){
            this.el.addClass(this.focusClass);
        }
        if(!this.hasFocus){
            this.hasFocus = true;
            
            this.startValue = this.getValue();
            this.fireEvent('focus', this);
        }
    },

    
    beforeBlur : Ext.emptyFn,

    
    onBlur : function(){
        this.beforeBlur();
        if(this.focusClass){
            this.el.removeClass(this.focusClass);
        }
        this.hasFocus = false;
        if(this.validationEvent !== false && (this.validateOnBlur || this.validationEvent == 'blur')){
            this.validate();
        }
        var v = this.getValue();
        if(String(v) !== String(this.startValue)){
            this.fireEvent('change', this, v, this.startValue);
        }
        this.fireEvent('blur', this);
        this.postBlur();
    },

    
    postBlur : Ext.emptyFn,

    
    isValid : function(preventMark){
        if(this.disabled){
            return true;
        }
        var restore = this.preventMark;
        this.preventMark = preventMark === true;
        var v = this.validateValue(this.processValue(this.getRawValue()));
        this.preventMark = restore;
        return v;
    },

    
    validate : function(){
        if(this.disabled || this.validateValue(this.processValue(this.getRawValue()))){
            this.clearInvalid();
            return true;
        }
        return false;
    },

    
    processValue : function(value){
        return value;
    },

    
     validateValue : function(value) {
         
         var error = this.getErrors(value)[0];

         if (error == undefined) {
             return true;
         } else {
             this.markInvalid(error);
             return false;
         }
     },
    
    
    getErrors: function() {
        return [];
    },

    
    getActiveError : function(){
        return this.activeError || '';
    },

    
    markInvalid : function(msg){
        
        if (this.rendered && !this.preventMark) {
            msg = msg || this.invalidText;

            var mt = this.getMessageHandler();
            if(mt){
                mt.mark(this, msg);
            }else if(this.msgTarget){
                this.el.addClass(this.invalidClass);
                var t = Ext.getDom(this.msgTarget);
                if(t){
                    t.innerHTML = msg;
                    t.style.display = this.msgDisplay;
                }
            }
        }
        
        this.setActiveError(msg);
    },
    
    
    clearInvalid : function(){
        
        if (this.rendered && !this.preventMark) {
            this.el.removeClass(this.invalidClass);
            var mt = this.getMessageHandler();
            if(mt){
                mt.clear(this);
            }else if(this.msgTarget){
                this.el.removeClass(this.invalidClass);
                var t = Ext.getDom(this.msgTarget);
                if(t){
                    t.innerHTML = '';
                    t.style.display = 'none';
                }
            }
        }
        
        this.unsetActiveError();
    },

    
    setActiveError: function(msg, suppressEvent) {
        this.activeError = msg;
        if (suppressEvent !== true) this.fireEvent('invalid', this, msg);
    },
    
    
    unsetActiveError: function(suppressEvent) {
        delete this.activeError;
        if (suppressEvent !== true) this.fireEvent('valid', this);
    },

    
    getMessageHandler : function(){
        return Ext.form.MessageTargets[this.msgTarget];
    },

    
    getErrorCt : function(){
        return this.el.findParent('.x-form-element', 5, true) || 
            this.el.findParent('.x-form-field-wrap', 5, true);   
    },

    
    alignErrorEl : function(){
        this.errorEl.setWidth(this.getErrorCt().getWidth(true) - 20);
    },

    
    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.el, 'tl-tr', [2, 0]);
    },

    
    getRawValue : function(){
        var v = this.rendered ? this.el.getValue() : Ext.value(this.value, '');
        if(v === this.emptyText){
            v = '';
        }
        return v;
    },

    
    getValue : function(){
        if(!this.rendered) {
            return this.value;
        }
        var v = this.el.getValue();
        if(v === this.emptyText || v === undefined){
            v = '';
        }
        return v;
    },

    
    setRawValue : function(v){
        return this.rendered ? (this.el.dom.value = (Ext.isEmpty(v) ? '' : v)) : '';
    },

    
    setValue : function(v){
        this.value = v;
        if(this.rendered){
            this.el.dom.value = (Ext.isEmpty(v) ? '' : v);
            this.validate();
        }
        return this;
    },

    
    append : function(v){
         this.setValue([this.getValue(), v].join(''));
    }

    
    

    
});


Ext.form.MessageTargets = {
    'qtip' : {
        mark: function(field, msg){
            field.el.addClass(field.invalidClass);
            field.el.dom.qtip = msg;
            field.el.dom.qclass = 'x-form-invalid-tip';
            if(Ext.QuickTips){ 
                Ext.QuickTips.enable();
            }
        },
        clear: function(field){
            field.el.removeClass(field.invalidClass);
            field.el.dom.qtip = '';
        }
    },
    'title' : {
        mark: function(field, msg){
            field.el.addClass(field.invalidClass);
            field.el.dom.title = msg;
        },
        clear: function(field){
            field.el.dom.title = '';
        }
    },
    'under' : {
        mark: function(field, msg){
            field.el.addClass(field.invalidClass);
            if(!field.errorEl){
                var elp = field.getErrorCt();
                if(!elp){ 
                    field.el.dom.title = msg;
                    return;
                }
                field.errorEl = elp.createChild({cls:'x-form-invalid-msg'});
                field.on('resize', field.alignErrorEl, field);
                field.on('destroy', function(){
                    Ext.destroy(this.errorEl);
                }, field);
            }
            field.alignErrorEl();
            field.errorEl.update(msg);
            Ext.form.Field.msgFx[field.msgFx].show(field.errorEl, field);
        },
        clear: function(field){
            field.el.removeClass(field.invalidClass);
            if(field.errorEl){
                Ext.form.Field.msgFx[field.msgFx].hide(field.errorEl, field);
            }else{
                field.el.dom.title = '';
            }
        }
    },
    'side' : {
        mark: function(field, msg){
            field.el.addClass(field.invalidClass);
            if(!field.errorIcon){
                var elp = field.getErrorCt();
                
                if(!elp){
                    field.el.dom.title = msg;
                    return;
                }
                field.errorIcon = elp.createChild({cls:'x-form-invalid-icon'});
                if (field.ownerCt) {
                    field.ownerCt.on('afterlayout', field.alignErrorIcon, field);
                    field.ownerCt.on('expand', field.alignErrorIcon, field);
                }
                field.on('resize', field.alignErrorIcon, field);
                field.on('destroy', function(){
                    Ext.destroy(this.errorIcon);
                }, field);
            }
            field.alignErrorIcon();
            field.errorIcon.dom.qtip = msg;
            field.errorIcon.dom.qclass = 'x-form-invalid-tip';
            field.errorIcon.show();
        },
        clear: function(field){
            field.el.removeClass(field.invalidClass);
            if(field.errorIcon){
                field.errorIcon.dom.qtip = '';
                field.errorIcon.hide();
            }else{
                field.el.dom.title = '';
            }
        }
    }
};


Ext.form.Field.msgFx = {
    normal : {
        show: function(msgEl, f){
            msgEl.setDisplayed('block');
        },

        hide : function(msgEl, f){
            msgEl.setDisplayed(false).update('');
        }
    },

    slide : {
        show: function(msgEl, f){
            msgEl.slideIn('t', {stopFx:true});
        },

        hide : function(msgEl, f){
            msgEl.slideOut('t', {stopFx:true,useDisplay:true});
        }
    },

    slideRight : {
        show: function(msgEl, f){
            msgEl.fixDisplay();
            msgEl.alignTo(f.el, 'tl-tr');
            msgEl.slideIn('l', {stopFx:true});
        },

        hide : function(msgEl, f){
            msgEl.slideOut('l', {stopFx:true,useDisplay:true});
        }
    }
};
Ext.reg('field', Ext.form.Field);

Ext.form.TextField = Ext.extend(Ext.form.Field,  {
    
    
    
    grow : false,
    
    growMin : 30,
    
    growMax : 800,
    
    vtype : null,
    
    maskRe : null,
    
    disableKeyFilter : false,
    
    allowBlank : true,
    
    minLength : 0,
    
    maxLength : Number.MAX_VALUE,
    
    minLengthText : 'The minimum length for this field is {0}',
    
    maxLengthText : 'The maximum length for this field is {0}',
    
    selectOnFocus : false,
    
    blankText : 'This field is required',
    
    validator : null,
    
    regex : null,
    
    regexText : '',
    
    emptyText : null,
    
    emptyClass : 'x-form-empty-field',

    

    initComponent : function(){
        Ext.form.TextField.superclass.initComponent.call(this);
        this.addEvents(
            
            'autosize',

            
            'keydown',
            
            'keyup',
            
            'keypress'
        );
    },

    
    initEvents : function(){
        Ext.form.TextField.superclass.initEvents.call(this);
        if(this.validationEvent == 'keyup'){
            this.validationTask = new Ext.util.DelayedTask(this.validate, this);
            this.mon(this.el, 'keyup', this.filterValidation, this);
        }
        else if(this.validationEvent !== false && this.validationEvent != 'blur'){
        	this.mon(this.el, this.validationEvent, this.validate, this, {buffer: this.validationDelay});
        }
        if(this.selectOnFocus || this.emptyText){            
            this.mon(this.el, 'mousedown', this.onMouseDown, this);
            
            if(this.emptyText){
                this.applyEmptyText();
            }
        }
        if(this.maskRe || (this.vtype && this.disableKeyFilter !== true && (this.maskRe = Ext.form.VTypes[this.vtype+'Mask']))){
        	this.mon(this.el, 'keypress', this.filterKeys, this);
        }
        if(this.grow){
        	this.mon(this.el, 'keyup', this.onKeyUpBuffered, this, {buffer: 50});
			this.mon(this.el, 'click', this.autoSize, this);
        }
        if(this.enableKeyEvents){
            this.mon(this.el, {
                scope: this,
                keyup: this.onKeyUp,
                keydown: this.onKeyDown,
                keypress: this.onKeyPress
            });
        }
    },
    
    onMouseDown: function(e){
        if(!this.hasFocus){
            this.mon(this.el, 'mouseup', Ext.emptyFn, this, { single: true, preventDefault: true });
        }
    },

    processValue : function(value){
        if(this.stripCharsRe){
            var newValue = value.replace(this.stripCharsRe, '');
            if(newValue !== value){
                this.setRawValue(newValue);
                return newValue;
            }
        }
        return value;
    },

    filterValidation : function(e){
        if(!e.isNavKeyPress()){
            this.validationTask.delay(this.validationDelay);
        }
    },
    
    
    onDisable: function(){
        Ext.form.TextField.superclass.onDisable.call(this);
        if(Ext.isIE){
            this.el.dom.unselectable = 'on';
        }
    },
    
    
    onEnable: function(){
        Ext.form.TextField.superclass.onEnable.call(this);
        if(Ext.isIE){
            this.el.dom.unselectable = '';
        }
    },

    
    onKeyUpBuffered : function(e){
        if(this.doAutoSize(e)){
            this.autoSize();
        }
    },
    
    
    doAutoSize : function(e){
        return !e.isNavKeyPress();
    },

    
    onKeyUp : function(e){
        this.fireEvent('keyup', this, e);
    },

    
    onKeyDown : function(e){
        this.fireEvent('keydown', this, e);
    },

    
    onKeyPress : function(e){
        this.fireEvent('keypress', this, e);
    },

    
    reset : function(){
        Ext.form.TextField.superclass.reset.call(this);
        this.applyEmptyText();
    },

    applyEmptyText : function(){
        if(this.rendered && this.emptyText && this.getRawValue().length < 1 && !this.hasFocus){
            this.setRawValue(this.emptyText);
            this.el.addClass(this.emptyClass);
        }
    },

    
    preFocus : function(){
        var el = this.el;
        if(this.emptyText){
            if(el.dom.value == this.emptyText){
                this.setRawValue('');
            }
            el.removeClass(this.emptyClass);
        }
        if(this.selectOnFocus){
            el.dom.select();
        }
    },

    
    postBlur : function(){
        this.applyEmptyText();
    },

    
    filterKeys : function(e){
        if(e.ctrlKey){
            return;
        }
        var k = e.getKey();
        if(Ext.isGecko && (e.isNavKeyPress() || k == e.BACKSPACE || (k == e.DELETE && e.button == -1))){
            return;
        }
        var cc = String.fromCharCode(e.getCharCode());
        if(!Ext.isGecko && e.isSpecialKey() && !cc){
            return;
        }
        if(!this.maskRe.test(cc)){
            e.stopEvent();
        }
    },

    setValue : function(v){
        if(this.emptyText && this.el && !Ext.isEmpty(v)){
            this.el.removeClass(this.emptyClass);
        }
        Ext.form.TextField.superclass.setValue.apply(this, arguments);
        this.applyEmptyText();
        this.autoSize();
        return this;
    },

    
    getErrors: function(value) {
        var errors = Ext.form.TextField.superclass.getErrors.apply(this, arguments);
        
        value = value || this.processValue(this.getRawValue());        
        
        if (Ext.isFunction(this.validator)) {
            var msg = this.validator(value);
            if (msg !== true) {
                errors.push(msg);
            }
        }
        
        if (value.length < 1 || value === this.emptyText) {
            if (this.allowBlank) {
                
                return errors;
            } else {
                errors.push(this.blankText);
            }
        }
        
        if (!this.allowBlank && (value.length < 1 || value === this.emptyText)) { 
            errors.push(this.blankText);
        }
        
        if (value.length < this.minLength) {
            errors.push(String.format(this.minLengthText, this.minLength));
        }
        
        if (value.length > this.maxLength) {
            errors.push(String.format(this.maxLengthText, this.maxLength));
        }
        
        if (this.vtype) {
            var vt = Ext.form.VTypes;
            if(!vt[this.vtype](value, this)){
                errors.push(this.vtypeText || vt[this.vtype +'Text']);
            }
        }
        
        if (this.regex && !this.regex.test(value)) {
            errors.push(this.regexText);
        }
        
        return errors;
    },

    
    selectText : function(start, end){
        var v = this.getRawValue();
        var doFocus = false;
        if(v.length > 0){
            start = start === undefined ? 0 : start;
            end = end === undefined ? v.length : end;
            var d = this.el.dom;
            if(d.setSelectionRange){
                d.setSelectionRange(start, end);
            }else if(d.createTextRange){
                var range = d.createTextRange();
                range.moveStart('character', start);
                range.moveEnd('character', end-v.length);
                range.select();
            }
            doFocus = Ext.isGecko || Ext.isOpera;
        }else{
            doFocus = true;
        }
        if(doFocus){
            this.focus();
        }
    },

    
    autoSize : function(){
        if(!this.grow || !this.rendered){
            return;
        }
        if(!this.metrics){
            this.metrics = Ext.util.TextMetrics.createInstance(this.el);
        }
        var el = this.el;
        var v = el.dom.value;
        var d = document.createElement('div');
        d.appendChild(document.createTextNode(v));
        v = d.innerHTML;
        Ext.removeNode(d);
        d = null;
        v += '&#160;';
        var w = Math.min(this.growMax, Math.max(this.metrics.getWidth(v) +  10, this.growMin));
        this.el.setWidth(w);
        this.fireEvent('autosize', this, w);
    },
	
	onDestroy: function(){
		if(this.validationTask){
			this.validationTask.cancel();
			this.validationTask = null;
		}
		Ext.form.TextField.superclass.onDestroy.call(this);
	}
});
Ext.reg('textfield', Ext.form.TextField);

Ext.form.TriggerField = Ext.extend(Ext.form.TextField,  {
    
    
    
    defaultAutoCreate : {tag: "input", type: "text", size: "16", autocomplete: "off"},
    
    hideTrigger:false,
    
    editable: true,
    
    readOnly: false,
    
    wrapFocusClass: 'x-trigger-wrap-focus',
    
    autoSize: Ext.emptyFn,
    
    monitorTab : true,
    
    deferHeight : true,
    
    mimicing : false,

    actionMode: 'wrap',

    defaultTriggerWidth: 17,

    
    onResize : function(w, h){
        Ext.form.TriggerField.superclass.onResize.call(this, w, h);
        var tw = this.getTriggerWidth();
        if(Ext.isNumber(w)){
            this.el.setWidth(w - tw);
        }
        this.wrap.setWidth(this.el.getWidth() + tw);
    },

    getTriggerWidth: function(){
        var tw = this.trigger.getWidth();
        if(!this.hideTrigger && !this.readOnly && tw === 0){
            tw = this.defaultTriggerWidth;
        }
        return tw;
    },

    
    alignErrorIcon : function(){
        if(this.wrap){
            this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
        }
    },

    
    onRender : function(ct, position){
        this.doc = Ext.isIE ? Ext.getBody() : Ext.getDoc();
        Ext.form.TriggerField.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.wrap({cls: 'x-form-field-wrap x-form-field-trigger-wrap'});
        this.trigger = this.wrap.createChild(this.triggerConfig ||
                {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.triggerClass});
        this.initTrigger();
        if(!this.width){
            this.wrap.setWidth(this.el.getWidth()+this.trigger.getWidth());
        }
        this.resizeEl = this.positionEl = this.wrap;
    },

    getWidth: function() {
        return(this.el.getWidth() + this.trigger.getWidth());
    },

    updateEditState: function(){
        if(this.rendered){
            if (this.readOnly) {
                this.el.dom.readOnly = true;
                this.el.addClass('x-trigger-noedit');
                this.mun(this.el, 'click', this.onTriggerClick, this);
                this.trigger.setDisplayed(false);
            } else {
                if (!this.editable) {
                    this.el.dom.readOnly = true;
                    this.el.addClass('x-trigger-noedit');
                    this.mon(this.el, 'click', this.onTriggerClick, this);
                } else {
                    this.el.dom.readOnly = false;
                    this.el.removeClass('x-trigger-noedit');
                    this.mun(this.el, 'click', this.onTriggerClick, this);
                }
                this.trigger.setDisplayed(!this.hideTrigger);
            }
            this.onResize(this.width || this.wrap.getWidth());
        }
    },

    setHideTrigger: function(hideTrigger){
        if(hideTrigger != this.hideTrigger){
            this.hideTrigger = hideTrigger;
            this.updateEditState();
        }
    },

    
    setEditable: function(editable){
        if(editable != this.editable){
            this.editable = editable;
            this.updateEditState();
        }
    },

    
    setReadOnly: function(readOnly){
        if(readOnly != this.readOnly){
            this.readOnly = readOnly;
            this.updateEditState();
        }
    },

    afterRender : function(){
        Ext.form.TriggerField.superclass.afterRender.call(this);
        this.updateEditState();
    },

    
    initTrigger : function(){
        this.mon(this.trigger, 'click', this.onTriggerClick, this, {preventDefault:true});
        this.trigger.addClassOnOver('x-form-trigger-over');
        this.trigger.addClassOnClick('x-form-trigger-click');
    },

    
    onDestroy : function(){
        Ext.destroy(this.trigger, this.wrap);
        if (this.mimicing){
            this.doc.un('mousedown', this.mimicBlur, this);
        }
        delete this.doc;
        Ext.form.TriggerField.superclass.onDestroy.call(this);
    },

    
    onFocus : function(){
        Ext.form.TriggerField.superclass.onFocus.call(this);
        if(!this.mimicing){
            this.wrap.addClass(this.wrapFocusClass);
            this.mimicing = true;
            this.doc.on('mousedown', this.mimicBlur, this, {delay: 10});
            if(this.monitorTab){
                this.on('specialkey', this.checkTab, this);
            }
        }
    },

    
    checkTab : function(me, e){
        if(e.getKey() == e.TAB){
            this.triggerBlur();
        }
    },

    
    onBlur : Ext.emptyFn,

    
    mimicBlur : function(e){
        if(!this.isDestroyed && !this.wrap.contains(e.target) && this.validateBlur(e)){
            this.triggerBlur();
        }
    },

    
    triggerBlur : function(){
        this.mimicing = false;
        this.doc.un('mousedown', this.mimicBlur, this);
        if(this.monitorTab && this.el){
            this.un('specialkey', this.checkTab, this);
        }
        Ext.form.TriggerField.superclass.onBlur.call(this);
        if(this.wrap){
            this.wrap.removeClass(this.wrapFocusClass);
        }
    },

    beforeBlur : Ext.emptyFn,

    
    
    validateBlur : function(e){
        return true;
    },

    
    onTriggerClick : Ext.emptyFn

    
    
    
});


Ext.form.TwinTriggerField = Ext.extend(Ext.form.TriggerField, {
    
    
    

    initComponent : function(){
        Ext.form.TwinTriggerField.superclass.initComponent.call(this);

        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
        ]};
    },

    getTrigger : function(index){
        return this.triggers[index];
    },

    initTrigger : function(){
        var ts = this.trigger.select('.x-form-trigger', true);
        var triggerField = this;
        ts.each(function(t, all, index){
            var triggerIndex = 'Trigger'+(index+1);
            t.hide = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = true;
            };
            t.show = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = false;
            };

            if(this['hide'+triggerIndex]){
                t.dom.style.display = 'none';
                this['hidden' + triggerIndex] = true;
            }
            this.mon(t, 'click', this['on'+triggerIndex+'Click'], this, {preventDefault:true});
            t.addClassOnOver('x-form-trigger-over');
            t.addClassOnClick('x-form-trigger-click');
        }, this);
        this.triggers = ts.elements;
    },

    getTriggerWidth: function(){
        var tw = 0;
        Ext.each(this.triggers, function(t, index){
            var triggerIndex = 'Trigger' + (index + 1),
                w = t.getWidth();
            if(w === 0 && !this['hidden' + triggerIndex]){
                tw += this.defaultTriggerWidth;
            }else{
                tw += w;
            }
        }, this);
        return tw;
    },

    
    onDestroy : function() {
        Ext.destroy(this.triggers);
        Ext.form.TwinTriggerField.superclass.onDestroy.call(this);
    },

    
    onTrigger1Click : Ext.emptyFn,
    
    onTrigger2Click : Ext.emptyFn
});
Ext.reg('trigger', Ext.form.TriggerField);

Ext.form.TextArea = Ext.extend(Ext.form.TextField,  {
    
    growMin : 60,
    
    growMax: 1000,
    growAppend : '&#160;\n&#160;',

    enterIsSpecial : false,

    
    preventScrollbars: false,
    

    
    onRender : function(ct, position){
        if(!this.el){
            this.defaultAutoCreate = {
                tag: "textarea",
                style:"width:100px;height:60px;",
                autocomplete: "off"
            };
        }
        Ext.form.TextArea.superclass.onRender.call(this, ct, position);
        if(this.grow){
            this.textSizeEl = Ext.DomHelper.append(document.body, {
                tag: "pre", cls: "x-form-grow-sizer"
            });
            if(this.preventScrollbars){
                this.el.setStyle("overflow", "hidden");
            }
            this.el.setHeight(this.growMin);
        }
    },

    onDestroy : function(){
        Ext.removeNode(this.textSizeEl);
        Ext.form.TextArea.superclass.onDestroy.call(this);
    },

    fireKey : function(e){
        if(e.isSpecialKey() && (this.enterIsSpecial || (e.getKey() != e.ENTER || e.hasModifier()))){
            this.fireEvent("specialkey", this, e);
        }
    },
    
    
    doAutoSize : function(e){
        return !e.isNavKeyPress() || e.getKey() == e.ENTER;
    },

    
    autoSize: function(){
        if(!this.grow || !this.textSizeEl){
            return;
        }
        var el = this.el,
            v = Ext.util.Format.htmlEncode(el.dom.value),
            ts = this.textSizeEl,
            h;
            
        Ext.fly(ts).setWidth(this.el.getWidth());
        if(v.length < 1){
            v = "&#160;&#160;";
        }else{
            v += this.growAppend;
            if(Ext.isIE){
                v = v.replace(/\n/g, '&#160;<br />');
            }
        }
        ts.innerHTML = v;
        h = Math.min(this.growMax, Math.max(ts.offsetHeight, this.growMin));
        if(h != this.lastHeight){
            this.lastHeight = h;
            this.el.setHeight(h);
            this.fireEvent("autosize", this, h);
        }
    }
});
Ext.reg('textarea', Ext.form.TextArea);
Ext.form.NumberField = Ext.extend(Ext.form.TextField,  {
    
    
    
    fieldClass: "x-form-field x-form-num-field",
    
    allowDecimals : true,
    
    decimalSeparator : ".",
    
    decimalPrecision : 2,
    
    allowNegative : true,
    
    minValue : Number.NEGATIVE_INFINITY,
    
    maxValue : Number.MAX_VALUE,
    
    minText : "The minimum value for this field is {0}",
    
    maxText : "The maximum value for this field is {0}",
    
    nanText : "{0} is not a valid number",
    
    baseChars : "0123456789",

    
    initEvents : function(){
        var allowed = this.baseChars + '';
        if (this.allowDecimals) {
            allowed += this.decimalSeparator;
        }
        if (this.allowNegative) {
            allowed += '-';
        }
        this.maskRe = new RegExp('[' + Ext.escapeRe(allowed) + ']');
        Ext.form.NumberField.superclass.initEvents.call(this);
    },
    
    
    getErrors: function(value) {
        var errors = Ext.form.NumberField.superclass.getErrors.apply(this, arguments);
        
        value = value || this.processValue(this.getRawValue());
        
        if (value.length < 1) { 
             return errors;
        }
        
        value = String(value).replace(this.decimalSeparator, ".");
        
        if(isNaN(value)){
            errors.push(String.format(this.nanText, value));
        }
        
        var num = this.parseValue(value);
        
        if(num < this.minValue){
            errors.push(String.format(this.minText, this.minValue));
        }
        
        if(num > this.maxValue){
            errors.push(String.format(this.maxText, this.maxValue));
        }
        
        return errors;
    },

    getValue : function(){
        return this.fixPrecision(this.parseValue(Ext.form.NumberField.superclass.getValue.call(this)));
    },

    setValue : function(v){
    	v = Ext.isNumber(v) ? v : parseFloat(String(v).replace(this.decimalSeparator, "."));
        v = isNaN(v) ? '' : String(v).replace(".", this.decimalSeparator);
        return Ext.form.NumberField.superclass.setValue.call(this, v);
    },
    
    
    setMinValue : function(value){
        this.minValue = Ext.num(value, Number.NEGATIVE_INFINITY);
    },
    
    
    setMaxValue : function(value){
        this.maxValue = Ext.num(value, Number.MAX_VALUE);    
    },

    
    parseValue : function(value){
        value = parseFloat(String(value).replace(this.decimalSeparator, "."));
        return isNaN(value) ? '' : value;
    },

    
    fixPrecision : function(value){
        var nan = isNaN(value);
        if(!this.allowDecimals || this.decimalPrecision == -1 || nan || !value){
           return nan ? '' : value;
        }
        return parseFloat(parseFloat(value).toFixed(this.decimalPrecision));
    },

    beforeBlur : function(){
        var v = this.parseValue(this.getRawValue());
        if(!Ext.isEmpty(v)){
            this.setValue(this.fixPrecision(v));
        }
    }
});
Ext.reg('numberfield', Ext.form.NumberField);
Ext.form.DateField = Ext.extend(Ext.form.TriggerField,  {
    
    format : "m/d/Y",
    
    altFormats : "m/d/Y|n/j/Y|n/j/y|m/j/y|n/d/y|m/j/Y|n/d/Y|m-d-y|m-d-Y|m/d|m-d|md|mdy|mdY|d|Y-m-d",
    
    disabledDaysText : "Disabled",
    
    disabledDatesText : "Disabled",
    
    minText : "The date in this field must be equal to or after {0}",
    
    maxText : "The date in this field must be equal to or before {0}",
    
    invalidText : "{0} is not a valid date - it must be in the format {1}",
    
    triggerClass : 'x-form-date-trigger',
    
    showToday : true,
    
    
    
    
    

    
    defaultAutoCreate : {tag: "input", type: "text", size: "10", autocomplete: "off"},

    
    
    initTime: '12', 

    initTimeFormat: 'H',

    
    safeParse : function(value, format) {
        if (/[gGhH]/.test(format.replace(/(\\.)/g, ''))) {
            
            return Date.parseDate(value, format);
        } else {
            
            var parsedDate = Date.parseDate(value + ' ' + this.initTime, format + ' ' + this.initTimeFormat);
            
            if (parsedDate) return parsedDate.clearTime();
        }
    },

    initComponent : function(){
        Ext.form.DateField.superclass.initComponent.call(this);

        this.addEvents(
            
            'select'
        );

        if(Ext.isString(this.minValue)){
            this.minValue = this.parseDate(this.minValue);
        }
        if(Ext.isString(this.maxValue)){
            this.maxValue = this.parseDate(this.maxValue);
        }
        this.disabledDatesRE = null;
        this.initDisabledDays();
    },

    initEvents: function() {
        Ext.form.DateField.superclass.initEvents.call(this);
        this.keyNav = new Ext.KeyNav(this.el, {
            "down": function(e) {
                this.onTriggerClick();
            },
            scope: this,
            forceKeyDown: true
        });
    },


    
    initDisabledDays : function(){
        if(this.disabledDates){
            var dd = this.disabledDates,
                len = dd.length - 1,
                re = "(?:";

            Ext.each(dd, function(d, i){
                re += Ext.isDate(d) ? '^' + Ext.escapeRe(d.dateFormat(this.format)) + '$' : dd[i];
                if(i != len){
                    re += '|';
                }
            }, this);
            this.disabledDatesRE = new RegExp(re + ')');
        }
    },

    
    setDisabledDates : function(dd){
        this.disabledDates = dd;
        this.initDisabledDays();
        if(this.menu){
            this.menu.picker.setDisabledDates(this.disabledDatesRE);
        }
    },

    
    setDisabledDays : function(dd){
        this.disabledDays = dd;
        if(this.menu){
            this.menu.picker.setDisabledDays(dd);
        }
    },

    
    setMinValue : function(dt){
        this.minValue = (Ext.isString(dt) ? this.parseDate(dt) : dt);
        if(this.menu){
            this.menu.picker.setMinDate(this.minValue);
        }
    },

    
    setMaxValue : function(dt){
        this.maxValue = (Ext.isString(dt) ? this.parseDate(dt) : dt);
        if(this.menu){
            this.menu.picker.setMaxDate(this.maxValue);
        }
    },
    
    
    getErrors: function(value) {
        var errors = Ext.form.DateField.superclass.getErrors.apply(this, arguments);
        
        value = this.formatDate(value || this.processValue(this.getRawValue()));
        
        if (value.length < 1) { 
             return errors;
        }
        
        var svalue = value;
        value = this.parseDate(value);
        if (!value) {
            errors.push(String.format(this.invalidText, svalue, this.format));
            return errors;
        }
        
        var time = value.getTime();
        if (this.minValue && time < this.minValue.getTime()) {
            errors.push(String.format(this.minText, this.formatDate(this.minValue)));
        }
        
        if (this.maxValue && time > this.maxValue.getTime()) {
            errors.push(String.format(this.maxText, this.formatDate(this.maxValue)));
        }
        
        if (this.disabledDays) {
            var day = value.getDay();
            
            for(var i = 0; i < this.disabledDays.length; i++) {
                if (day === this.disabledDays[i]) {
                    errors.push(this.disabledDaysText);
                    break;
                }
            }
        }
        
        var fvalue = this.formatDate(value);
        if (this.disabledDatesRE && this.disabledDatesRE.test(fvalue)) {
            errors.push(String.format(this.disabledDatesText, fvalue));
        }
        
        return errors;
    },

    
    
    validateBlur : function(){
        return !this.menu || !this.menu.isVisible();
    },

    
    getValue : function(){
        return this.parseDate(Ext.form.DateField.superclass.getValue.call(this)) || "";
    },

    
    setValue : function(date){
        return Ext.form.DateField.superclass.setValue.call(this, this.formatDate(this.parseDate(date)));
    },

    
    parseDate : function(value) {
        if(!value || Ext.isDate(value)){
            return value;
        }

        var v = this.safeParse(value, this.format),
            af = this.altFormats,
            afa = this.altFormatsArray;

        if (!v && af) {
            afa = afa || af.split("|");

            for (var i = 0, len = afa.length; i < len && !v; i++) {
                v = this.safeParse(value, afa[i]);
            }
        }
        return v;
    },

    
    onDestroy : function(){
        Ext.destroy(this.menu, this.keyNav);
        Ext.form.DateField.superclass.onDestroy.call(this);
    },

    
    formatDate : function(date){
        return Ext.isDate(date) ? date.dateFormat(this.format) : date;
    },

    
    
    
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.menu == null){
            this.menu = new Ext.menu.DateMenu({
                hideOnClick: false,
                focusOnSelect: false
            });
        }
        this.onFocus();
        Ext.apply(this.menu.picker,  {
            minDate : this.minValue,
            maxDate : this.maxValue,
            disabledDatesRE : this.disabledDatesRE,
            disabledDatesText : this.disabledDatesText,
            disabledDays : this.disabledDays,
            disabledDaysText : this.disabledDaysText,
            format : this.format,
            showToday : this.showToday,
            minText : String.format(this.minText, this.formatDate(this.minValue)),
            maxText : String.format(this.maxText, this.formatDate(this.maxValue))
        });
        this.menu.picker.setValue(this.getValue() || new Date());
        this.menu.show(this.el, "tl-bl?");
        this.menuEvents('on');
    },

    
    menuEvents: function(method){
        this.menu[method]('select', this.onSelect, this);
        this.menu[method]('hide', this.onMenuHide, this);
        this.menu[method]('show', this.onFocus, this);
    },

    onSelect: function(m, d){
        this.setValue(d);
        this.fireEvent('select', this, d);
        this.menu.hide();
    },

    onMenuHide: function(){
        this.focus(false, 60);
        this.menuEvents('un');
    },

    
    beforeBlur : function(){
        var v = this.parseDate(this.getRawValue());
        if(v){
            this.setValue(v);
        }
    }

    
    
    
    
});
Ext.reg('datefield', Ext.form.DateField);
Ext.form.DisplayField = Ext.extend(Ext.form.Field,  {
    validationEvent : false,
    validateOnBlur : false,
    defaultAutoCreate : {tag: "div"},
    
    fieldClass : "x-form-display-field",
    
    htmlEncode: false,

    
    initEvents : Ext.emptyFn,

    isValid : function(){
        return true;
    },

    validate : function(){
        return true;
    },

    getRawValue : function(){
        var v = this.rendered ? this.el.dom.innerHTML : Ext.value(this.value, '');
        if(v === this.emptyText){
            v = '';
        }
        if(this.htmlEncode){
            v = Ext.util.Format.htmlDecode(v);
        }
        return v;
    },

    getValue : function(){
        return this.getRawValue();
    },
    
    getName: function() {
        return this.name;
    },

    setRawValue : function(v){
        if(this.htmlEncode){
            v = Ext.util.Format.htmlEncode(v);
        }
        return this.rendered ? (this.el.dom.innerHTML = (Ext.isEmpty(v) ? '' : v)) : (this.value = v);
    },

    setValue : function(v){
        this.setRawValue(v);
        return this;
    }
    
    
    
    
    
    
});

Ext.reg('displayfield', Ext.form.DisplayField);

Ext.form.ComboBox = Ext.extend(Ext.form.TriggerField, {
    
    
    
    
    

    
    defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
    
    
    
    
    
    
    
    listClass : '',
    
    selectedClass : 'x-combo-selected',
    
    listEmptyText: '',
    
    triggerClass : 'x-form-arrow-trigger',
    
    shadow : 'sides',
    
    listAlign : 'tl-bl?',
    
    maxHeight : 300,
    
    minHeight : 90,
    
    triggerAction : 'query',
    
    minChars : 4,
    
    autoSelect : true,
    
    typeAhead : false,
    
    queryDelay : 500,
    
    pageSize : 0,
    
    selectOnFocus : false,
    
    queryParam : 'query',
    
    loadingText : 'Loading...',
    
    resizable : false,
    
    handleHeight : 8,
    
    allQuery: '',
    
    mode: 'remote',
    
    minListWidth : 70,
    
    forceSelection : false,
    
    typeAheadDelay : 250,
    

    
    lazyInit : true,

    
    clearFilterOnReset : true,

    
    submitValue: undefined,

    

    
    initComponent : function(){
        Ext.form.ComboBox.superclass.initComponent.call(this);
        this.addEvents(
            
            'expand',
            
            'collapse',

            
            'beforeselect',
            
            'select',
            
            'beforequery'
        );
        if(this.transform){
            var s = Ext.getDom(this.transform);
            if(!this.hiddenName){
                this.hiddenName = s.name;
            }
            if(!this.store){
                this.mode = 'local';
                var d = [], opts = s.options;
                for(var i = 0, len = opts.length;i < len; i++){
                    var o = opts[i],
                        value = (o.hasAttribute ? o.hasAttribute('value') : o.getAttributeNode('value').specified) ? o.value : o.text;
                    if(o.selected && Ext.isEmpty(this.value, true)) {
                        this.value = value;
                    }
                    d.push([value, o.text]);
                }
                this.store = new Ext.data.ArrayStore({
                    'id': 0,
                    fields: ['value', 'text'],
                    data : d,
                    autoDestroy: true
                });
                this.valueField = 'value';
                this.displayField = 'text';
            }
            s.name = Ext.id(); 
            if(!this.lazyRender){
                this.target = true;
                this.el = Ext.DomHelper.insertBefore(s, this.autoCreate || this.defaultAutoCreate);
                this.render(this.el.parentNode, s);
            }
            Ext.removeNode(s);
        }
        
        else if(this.store){
            this.store = Ext.StoreMgr.lookup(this.store);
            if(this.store.autoCreated){
                this.displayField = this.valueField = 'field1';
                if(!this.store.expandData){
                    this.displayField = 'field2';
                }
                this.mode = 'local';
            }
        }

        this.selectedIndex = -1;
        if(this.mode == 'local'){
            if(!Ext.isDefined(this.initialConfig.queryDelay)){
                this.queryDelay = 10;
            }
            if(!Ext.isDefined(this.initialConfig.minChars)){
                this.minChars = 0;
            }
        }
    },

    
    onRender : function(ct, position){
        if(this.hiddenName && !Ext.isDefined(this.submitValue)){
            this.submitValue = false;
        }
        Ext.form.ComboBox.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName,
                    id: (this.hiddenId||this.hiddenName)}, 'before', true);

        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }

        if(!this.lazyInit){
            this.initList();
        }else{
            this.on('focus', this.initList, this, {single: true});
        }
    },

    
    initValue : function(){
        Ext.form.ComboBox.superclass.initValue.call(this);
        if(this.hiddenField){
            this.hiddenField.value =
                Ext.value(Ext.isDefined(this.hiddenValue) ? this.hiddenValue : this.value, '');
        }
    },

    getParentZIndex : function(){
        var zindex;
        if (this.ownerCt){
            this.findParentBy(function(ct){
                zindex = parseInt(ct.getPositionEl().getStyle('z-index'), 10);
                return !!zindex;
            });
        }
        return zindex;
    },

    
    initList : function(){
        if(!this.list){
            var cls = 'x-combo-list',
                listParent = Ext.getDom(this.getListParent() || Ext.getBody()),
                zindex = parseInt(Ext.fly(listParent).getStyle('z-index'), 10);

            if (!zindex) {
                zindex = this.getParentZIndex();
            }

            this.list = new Ext.Layer({
                parentEl: listParent,
                shadow: this.shadow,
                cls: [cls, this.listClass].join(' '),
                constrain:false,
                zindex: (zindex || 12000) + 5
            });

            var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
            this.list.setSize(lw, 0);
            this.list.swallowEvent('mousewheel');
            this.assetHeight = 0;
            if(this.syncFont !== false){
                this.list.setStyle('font-size', this.el.getStyle('font-size'));
            }
            if(this.title){
                this.header = this.list.createChild({cls:cls+'-hd', html: this.title});
                this.assetHeight += this.header.getHeight();
            }

            this.innerList = this.list.createChild({cls:cls+'-inner'});
            this.mon(this.innerList, 'mouseover', this.onViewOver, this);
            this.mon(this.innerList, 'mousemove', this.onViewMove, this);
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));

            if(this.pageSize){
                this.footer = this.list.createChild({cls:cls+'-ft'});
                this.pageTb = new Ext.PagingToolbar({
                    store: this.store,
                    pageSize: this.pageSize,
                    renderTo:this.footer
                });
                this.assetHeight += this.footer.getHeight();
            }

            if(!this.tpl){
                
                this.tpl = '<tpl for="."><div class="'+cls+'-item">{' + this.displayField + '}</div></tpl>';
                
            }

            
            this.view = new Ext.DataView({
                applyTo: this.innerList,
                tpl: this.tpl,
                singleSelect: true,
                selectedClass: this.selectedClass,
                itemSelector: this.itemSelector || '.' + cls + '-item',
                emptyText: this.listEmptyText,
                deferEmptyText: false
            });

            this.mon(this.view, {
                containerclick : this.onViewClick,
                click : this.onViewClick,
                scope :this
            });

            this.bindStore(this.store, true);

            if(this.resizable){
                this.resizer = new Ext.Resizable(this.list,  {
                   pinned:true, handles:'se'
                });
                this.mon(this.resizer, 'resize', function(r, w, h){
                    this.maxHeight = h-this.handleHeight-this.list.getFrameWidth('tb')-this.assetHeight;
                    this.listWidth = w;
                    this.innerList.setWidth(w - this.list.getFrameWidth('lr'));
                    this.restrictHeight();
                }, this);

                this[this.pageSize?'footer':'innerList'].setStyle('margin-bottom', this.handleHeight+'px');
            }
        }
    },

    
    getListParent : function() {
        return document.body;
    },

    
    getStore : function(){
        return this.store;
    },

    
    bindStore : function(store, initial){
        if(this.store && !initial){
            if(this.store !== store && this.store.autoDestroy){
                this.store.destroy();
            }else{
                this.store.un('beforeload', this.onBeforeLoad, this);
                this.store.un('load', this.onLoad, this);
                this.store.un('exception', this.collapse, this);
            }
            if(!store){
                this.store = null;
                if(this.view){
                    this.view.bindStore(null);
                }
                if(this.pageTb){
                    this.pageTb.bindStore(null);
                }
            }
        }
        if(store){
            if(!initial) {
                this.lastQuery = null;
                if(this.pageTb) {
                    this.pageTb.bindStore(store);
                }
            }

            this.store = Ext.StoreMgr.lookup(store);
            this.store.on({
                scope: this,
                beforeload: this.onBeforeLoad,
                load: this.onLoad,
                exception: this.collapse
            });

            if(this.view){
                this.view.bindStore(store);
            }
        }
    },

    reset : function(){
        Ext.form.ComboBox.superclass.reset.call(this);
        if(this.clearFilterOnReset && this.mode == 'local'){
            this.store.clearFilter();
        }
    },

    
    initEvents : function(){
        Ext.form.ComboBox.superclass.initEvents.call(this);

        
        this.keyNav = new Ext.KeyNav(this.el, {
            "up" : function(e){
                this.inKeyMode = true;
                this.selectPrev();
            },

            "down" : function(e){
                if(!this.isExpanded()){
                    this.onTriggerClick();
                }else{
                    this.inKeyMode = true;
                    this.selectNext();
                }
            },

            "enter" : function(e){
                this.onViewClick();
            },

            "esc" : function(e){
                this.collapse();
            },

            "tab" : function(e){
                if (this.forceSelection === true) {
                    this.collapse();
                } else {
                    this.onViewClick(false);
                }
                return true;
            },

            scope : this,

            doRelay : function(e, h, hname){
                if(hname == 'down' || this.scope.isExpanded()){
                    
                    var relay = Ext.KeyNav.prototype.doRelay.apply(this, arguments);
                    if(!Ext.isIE && Ext.EventManager.useKeydown){
                        
                        this.scope.fireKey(e);
                    }
                    return relay;
                }
                return true;
            },

            forceKeyDown : true,
            defaultEventAction: 'stopEvent'
        });
        this.queryDelay = Math.max(this.queryDelay || 10,
                this.mode == 'local' ? 10 : 250);
        this.dqTask = new Ext.util.DelayedTask(this.initQuery, this);
        if(this.typeAhead){
            this.taTask = new Ext.util.DelayedTask(this.onTypeAhead, this);
        }
        if(!this.enableKeyEvents){
            this.mon(this.el, 'keyup', this.onKeyUp, this);
        }
    },


    
    onDestroy : function(){
        if (this.dqTask){
            this.dqTask.cancel();
            this.dqTask = null;
        }
        this.bindStore(null);
        Ext.destroy(
            this.resizer,
            this.view,
            this.pageTb,
            this.list
        );
        Ext.destroyMembers(this, 'hiddenField');
        Ext.form.ComboBox.superclass.onDestroy.call(this);
    },

    
    fireKey : function(e){
        if (!this.isExpanded()) {
            Ext.form.ComboBox.superclass.fireKey.call(this, e);
        }
    },

    
    onResize : function(w, h){
        Ext.form.ComboBox.superclass.onResize.apply(this, arguments);
        if(!isNaN(w) && this.isVisible() && this.list){
            this.doResize(w);
        }else{
            this.bufferSize = w;
        }
    },

    doResize: function(w){
        if(!Ext.isDefined(this.listWidth)){
            var lw = Math.max(w, this.minListWidth);
            this.list.setWidth(lw);
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));
        }
    },

    
    onEnable : function(){
        Ext.form.ComboBox.superclass.onEnable.apply(this, arguments);
        if(this.hiddenField){
            this.hiddenField.disabled = false;
        }
    },

    
    onDisable : function(){
        Ext.form.ComboBox.superclass.onDisable.apply(this, arguments);
        if(this.hiddenField){
            this.hiddenField.disabled = true;
        }
    },

    
    onBeforeLoad : function(){
        if(!this.hasFocus){
            return;
        }
        this.innerList.update(this.loadingText ?
               '<div class="loading-indicator">'+this.loadingText+'</div>' : '');
        this.restrictHeight();
        this.selectedIndex = -1;
    },

    
    onLoad : function(){
        if(!this.hasFocus){
            return;
        }
        if(this.store.getCount() > 0 || this.listEmptyText){
            this.expand();
            this.restrictHeight();
            if(this.lastQuery == this.allQuery){
                if(this.editable){
                    this.el.dom.select();
                }

                if(this.autoSelect !== false && !this.selectByValue(this.value, true)){
                    this.select(0, true);
                }
            }else{
                if(this.autoSelect !== false){
                    this.selectNext();
                }
                if(this.typeAhead && this.lastKey != Ext.EventObject.BACKSPACE && this.lastKey != Ext.EventObject.DELETE){
                    this.taTask.delay(this.typeAheadDelay);
                }
            }
        }else{
            this.collapse();
        }

    },

    
    onTypeAhead : function(){
        if(this.store.getCount() > 0){
            var r = this.store.getAt(0);
            var newValue = r.data[this.displayField];
            var len = newValue.length;
            var selStart = this.getRawValue().length;
            if(selStart != len){
                this.setRawValue(newValue);
                this.selectText(selStart, newValue.length);
            }
        }
    },

    
    assertValue  : function(){
        var val = this.getRawValue(),
            rec = this.findRecord(this.displayField, val);

        if(!rec && this.forceSelection){
            if(val.length > 0 && val != this.emptyText){
                this.el.dom.value = Ext.value(this.lastSelectionText, '');
                this.applyEmptyText();
            }else{
                this.clearValue();
            }
        }else{
            if(rec){
                
                
                
                if (val == rec.get(this.displayField) && this.value == rec.get(this.valueField)){
                    return;
                }
                val = rec.get(this.valueField || this.displayField);
            }
            this.setValue(val);
        }
    },

    
    onSelect : function(record, index){
        if(this.fireEvent('beforeselect', this, record, index) !== false){
            this.setValue(record.data[this.valueField || this.displayField]);
            this.collapse();
            this.fireEvent('select', this, record, index);
        }
    },

    
    getName: function(){
        var hf = this.hiddenField;
        return hf && hf.name ? hf.name : this.hiddenName || Ext.form.ComboBox.superclass.getName.call(this);
    },

    
    getValue : function(){
        if(this.valueField){
            return Ext.isDefined(this.value) ? this.value : '';
        }else{
            return Ext.form.ComboBox.superclass.getValue.call(this);
        }
    },

    
    clearValue : function(){
        if(this.hiddenField){
            this.hiddenField.value = '';
        }
        this.setRawValue('');
        this.lastSelectionText = '';
        this.applyEmptyText();
        this.value = '';
    },

    
    setValue : function(v){
        var text = v;
        if(this.valueField){
            var r = this.findRecord(this.valueField, v);
            if(r){
                text = r.data[this.displayField];
            }else if(Ext.isDefined(this.valueNotFoundText)){
                text = this.valueNotFoundText;
            }
        }
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = Ext.value(v, '');
        }
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = v;
        return this;
    },

    
    findRecord : function(prop, value){
        var record;
        if(this.store.getCount() > 0){
            this.store.each(function(r){
                if(r.data[prop] == value){
                    record = r;
                    return false;
                }
            });
        }
        return record;
    },

    
    onViewMove : function(e, t){
        this.inKeyMode = false;
    },

    
    onViewOver : function(e, t){
        if(this.inKeyMode){ 
            return;
        }
        var item = this.view.findItemFromChild(t);
        if(item){
            var index = this.view.indexOf(item);
            this.select(index, false);
        }
    },

    
    onViewClick : function(doFocus){
        var index = this.view.getSelectedIndexes()[0],
            s = this.store,
            r = s.getAt(index);
        if(r){
            this.onSelect(r, index);
        }else {
            this.collapse();
        }
        if(doFocus !== false){
            this.el.focus();
        }
    },


    
    restrictHeight : function(){
        this.innerList.dom.style.height = '';
        var inner = this.innerList.dom,
            pad = this.list.getFrameWidth('tb') + (this.resizable ? this.handleHeight : 0) + this.assetHeight,
            h = Math.max(inner.clientHeight, inner.offsetHeight, inner.scrollHeight),
            ha = this.getPosition()[1]-Ext.getBody().getScroll().top,
            hb = Ext.lib.Dom.getViewHeight()-ha-this.getSize().height,
            space = Math.max(ha, hb, this.minHeight || 0)-this.list.shadowOffset-pad-5;

        h = Math.min(h, space, this.maxHeight);

        this.innerList.setHeight(h);
        this.list.beginUpdate();
        this.list.setHeight(h+pad);
        this.list.alignTo.apply(this.list, [this.el].concat(this.listAlign));
        this.list.endUpdate();
    },

    
    isExpanded : function(){
        return this.list && this.list.isVisible();
    },

    
    selectByValue : function(v, scrollIntoView){
        if(!Ext.isEmpty(v, true)){
            var r = this.findRecord(this.valueField || this.displayField, v);
            if(r){
                this.select(this.store.indexOf(r), scrollIntoView);
                return true;
            }
        }
        return false;
    },

    
    select : function(index, scrollIntoView){
        this.selectedIndex = index;
        this.view.select(index);
        if(scrollIntoView !== false){
            var el = this.view.getNode(index);
            if(el){
                this.innerList.scrollChildIntoView(el, false);
            }
        }

    },

    
    selectNext : function(){
        var ct = this.store.getCount();
        if(ct > 0){
            if(this.selectedIndex == -1){
                this.select(0);
            }else if(this.selectedIndex < ct-1){
                this.select(this.selectedIndex+1);
            }
        }
    },

    
    selectPrev : function(){
        var ct = this.store.getCount();
        if(ct > 0){
            if(this.selectedIndex == -1){
                this.select(0);
            }else if(this.selectedIndex !== 0){
                this.select(this.selectedIndex-1);
            }
        }
    },

    
    onKeyUp : function(e){
        var k = e.getKey();
        if(this.editable !== false && this.readOnly !== true && (k == e.BACKSPACE || !e.isSpecialKey())){

            this.lastKey = k;
            this.dqTask.delay(this.queryDelay);
        }
        Ext.form.ComboBox.superclass.onKeyUp.call(this, e);
    },

    
    validateBlur : function(){
        return !this.list || !this.list.isVisible();
    },

    
    initQuery : function(){
        this.doQuery(this.getRawValue());
    },

    
    beforeBlur : function(){
        this.assertValue();
    },

    
    postBlur  : function(){
        Ext.form.ComboBox.superclass.postBlur.call(this);
        this.collapse();
        this.inKeyMode = false;
    },

    
    doQuery : function(q, forceAll){
        q = Ext.isEmpty(q) ? '' : q;
        var qe = {
            query: q,
            forceAll: forceAll,
            combo: this,
            cancel:false
        };
        if(this.fireEvent('beforequery', qe)===false || qe.cancel){
            return false;
        }
        q = qe.query;
        forceAll = qe.forceAll;
        if(forceAll === true || (q.length >= this.minChars)){
            if(this.lastQuery !== q){
                this.lastQuery = q;
                if(this.mode == 'local'){
                    this.selectedIndex = -1;
                    if(forceAll){
                        this.store.clearFilter();
                    }else{
                        this.store.filter(this.displayField, q);
                    }
                    this.onLoad();
                }else{
                    this.store.baseParams[this.queryParam] = q;
                    this.store.load({
                        params: this.getParams(q)
                    });
                    this.expand();
                }
            }else{
                this.selectedIndex = -1;
                this.onLoad();
            }
        }
    },

    
    getParams : function(q){
        var p = {};
        
        if(this.pageSize){
            p.start = 0;
            p.limit = this.pageSize;
        }
        return p;
    },

    
    collapse : function(){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
        this.fireEvent('collapse', this);
    },

    
    collapseIf : function(e){
        if(!this.isDestroyed && !e.within(this.wrap) && !e.within(this.list)){
            this.collapse();
        }
    },

    
    expand : function(){
        if(this.isExpanded() || !this.hasFocus){
            return;
        }

        if(this.title || this.pageSize){
            this.assetHeight = 0;
            if(this.title){
                this.assetHeight += this.header.getHeight();
            }
            if(this.pageSize){
                this.assetHeight += this.footer.getHeight();
            }
        }

        if(this.bufferSize){
            this.doResize(this.bufferSize);
            delete this.bufferSize;
        }
        this.list.alignTo.apply(this.list, [this.el].concat(this.listAlign));

        
        var listParent = Ext.getDom(this.getListParent() || Ext.getBody()),
            zindex = parseInt(Ext.fly(listParent).getStyle('z-index') ,10);
        if (!zindex){
            zindex = this.getParentZIndex();
        }
        if (zindex) {
            this.list.setZIndex(zindex + 5);
        }
        this.list.show();
        if(Ext.isGecko2){
            this.innerList.setOverflow('auto'); 
        }
        this.mon(Ext.getDoc(), {
            scope: this,
            mousewheel: this.collapseIf,
            mousedown: this.collapseIf
        });
        this.fireEvent('expand', this);
    },

    
    
    
    onTriggerClick : function(){
        if(this.readOnly || this.disabled){
            return;
        }
        if(this.isExpanded()){
            this.collapse();
            this.el.focus();
        }else {
            this.onFocus({});
            if(this.triggerAction == 'all') {
                this.doQuery(this.allQuery, true);
            } else {
                this.doQuery(this.getRawValue());
            }
            this.el.focus();
        }
    }

    
    
    
    

});
Ext.reg('combo', Ext.form.ComboBox);

Ext.form.Checkbox = Ext.extend(Ext.form.Field,  {
    
    focusClass : undefined,
    
    fieldClass : 'x-form-field',
    
    checked : false,
    
    boxLabel: '&#160;',
    
    defaultAutoCreate : { tag: 'input', type: 'checkbox', autocomplete: 'off'},
    
    
    
    

    
    actionMode : 'wrap',

	
    initComponent : function(){
        Ext.form.Checkbox.superclass.initComponent.call(this);
        this.addEvents(
            
            'check'
        );
    },

    
    onResize : function(){
        Ext.form.Checkbox.superclass.onResize.apply(this, arguments);
        if(!this.boxLabel && !this.fieldLabel){
            this.el.alignTo(this.wrap, 'c-c');
        }
    },

    
    initEvents : function(){
        Ext.form.Checkbox.superclass.initEvents.call(this);
        this.mon(this.el, {
            scope: this,
            click: this.onClick,
            change: this.onClick
        });
    },

    
    markInvalid : Ext.emptyFn,
    
    clearInvalid : Ext.emptyFn,

    
    onRender : function(ct, position){
        Ext.form.Checkbox.superclass.onRender.call(this, ct, position);
        if(this.inputValue !== undefined){
            this.el.dom.value = this.inputValue;
        }
        this.wrap = this.el.wrap({cls: 'x-form-check-wrap'});
        if(this.boxLabel){
            this.wrap.createChild({tag: 'label', htmlFor: this.el.id, cls: 'x-form-cb-label', html: this.boxLabel});
        }
        if(this.checked){
            this.setValue(true);
        }else{
            this.checked = this.el.dom.checked;
        }
        
        if(Ext.isIE){
            this.wrap.repaint();
        }
        this.resizeEl = this.positionEl = this.wrap;
    },

    
    onDestroy : function(){
        Ext.destroy(this.wrap);
        Ext.form.Checkbox.superclass.onDestroy.call(this);
    },

    
    initValue : function() {
        this.originalValue = this.getValue();
    },

    
    getValue : function(){
        if(this.rendered){
            return this.el.dom.checked;
        }
        return this.checked;
    },

	
    onClick : function(){
        if(this.el.dom.checked != this.checked){
            this.setValue(this.el.dom.checked);
        }
    },

    
    setValue : function(v){
        var checked = this.checked ;
        this.checked = (v === true || v === 'true' || v == '1' || String(v).toLowerCase() == 'on');
        if(this.rendered){
            this.el.dom.checked = this.checked;
            this.el.dom.defaultChecked = this.checked;
        }
        if(checked != this.checked){
            this.fireEvent('check', this, this.checked);
            if(this.handler){
                this.handler.call(this.scope || this, this, this.checked);
            }
        }
        return this;
    }
});
Ext.reg('checkbox', Ext.form.Checkbox);

Ext.form.CheckboxGroup = Ext.extend(Ext.form.Field, {
    
    
    columns : 'auto',
    
    vertical : false,
    
    allowBlank : true,
    
    blankText : "You must select at least one item in this group",

    
    defaultType : 'checkbox',

    
    groupCls : 'x-form-check-group',

    
    initComponent: function(){
        this.addEvents(
            
            'change'
        );
        this.on('change', this.validate, this);
        Ext.form.CheckboxGroup.superclass.initComponent.call(this);
    },

    
    onRender : function(ct, position){
        if(!this.el){
            var panelCfg = {
                autoEl: {
                    id: this.id
                },
                cls: this.groupCls,
                layout: 'column',
                renderTo: ct,
                bufferResize: false 
            };
            var colCfg = {
                xtype: 'container',
                defaultType: this.defaultType,
                layout: 'form',
                defaults: {
                    hideLabel: true,
                    anchor: '100%'
                }
            };

            if(this.items[0].items){

                

                Ext.apply(panelCfg, {
                    layoutConfig: {columns: this.items.length},
                    defaults: this.defaults,
                    items: this.items
                });
                for(var i=0, len=this.items.length; i<len; i++){
                    Ext.applyIf(this.items[i], colCfg);
                }

            }else{

                
                

                var numCols, cols = [];

                if(typeof this.columns == 'string'){ 
                    this.columns = this.items.length;
                }
                if(!Ext.isArray(this.columns)){
                    var cs = [];
                    for(var i=0; i<this.columns; i++){
                        cs.push((100/this.columns)*.01); 
                    }
                    this.columns = cs;
                }

                numCols = this.columns.length;

                
                for(var i=0; i<numCols; i++){
                    var cc = Ext.apply({items:[]}, colCfg);
                    cc[this.columns[i] <= 1 ? 'columnWidth' : 'width'] = this.columns[i];
                    if(this.defaults){
                        cc.defaults = Ext.apply(cc.defaults || {}, this.defaults);
                    }
                    cols.push(cc);
                };

                
                if(this.vertical){
                    var rows = Math.ceil(this.items.length / numCols), ri = 0;
                    for(var i=0, len=this.items.length; i<len; i++){
                        if(i>0 && i%rows==0){
                            ri++;
                        }
                        if(this.items[i].fieldLabel){
                            this.items[i].hideLabel = false;
                        }
                        cols[ri].items.push(this.items[i]);
                    };
                }else{
                    for(var i=0, len=this.items.length; i<len; i++){
                        var ci = i % numCols;
                        if(this.items[i].fieldLabel){
                            this.items[i].hideLabel = false;
                        }
                        cols[ci].items.push(this.items[i]);
                    };
                }

                Ext.apply(panelCfg, {
                    layoutConfig: {columns: numCols},
                    items: cols
                });
            }

            this.panel = new Ext.Container(panelCfg);
            this.panel.ownerCt = this;
            this.el = this.panel.getEl();

            if(this.forId && this.itemCls){
                var l = this.el.up(this.itemCls).child('label', true);
                if(l){
                    l.setAttribute('htmlFor', this.forId);
                }
            }

            var fields = this.panel.findBy(function(c){
                return c.isFormField;
            }, this);

            this.items = new Ext.util.MixedCollection();
            this.items.addAll(fields);
        }
        Ext.form.CheckboxGroup.superclass.onRender.call(this, ct, position);
    },

    initValue : function(){
        if(this.value){
            this.setValue.apply(this, this.buffered ? this.value : [this.value]);
            delete this.buffered;
            delete this.value;
        }
    },

    afterRender : function(){
        Ext.form.CheckboxGroup.superclass.afterRender.call(this);
        this.eachItem(function(item){
            item.on('check', this.fireChecked, this);
            item.inGroup = true;
        });
    },

    
    doLayout: function(){
        
        if(this.rendered){
            this.panel.forceLayout = this.ownerCt.forceLayout;
            this.panel.doLayout();
        }
    },

    
    fireChecked: function(){
        var arr = [];
        this.eachItem(function(item){
            if(item.checked){
                arr.push(item);
            }
        });
        this.fireEvent('change', this, arr);
    },
    
    
    getErrors: function() {
        var errors = Ext.form.CheckboxGroup.superclass.getErrors.apply(this, arguments);
        
        if (!this.allowBlank) {
            var blank = true;
            
            this.eachItem(function(f){
                if (f.checked) {
                    return (blank = false);
                }
            });
            
            if (blank) errors.push(this.blankText);
        }
        
        return errors;
    },

    
    isDirty: function(){
        
        if (this.disabled || !this.rendered) {
            return false;
        }

        var dirty = false;
        
        this.eachItem(function(item){
            if(item.isDirty()){
                dirty = true;
                return false;
            }
        });
        
        return dirty;
    },

    
    setReadOnly : function(readOnly){
        if(this.rendered){
            this.eachItem(function(item){
                item.setReadOnly(readOnly);
            });
        }
        this.readOnly = readOnly;
    },

    
    onDisable : function(){
        this.eachItem(function(item){
            item.disable();
        });
    },

    
    onEnable : function(){
        this.eachItem(function(item){
            item.enable();
        });
    },

    
    onResize : function(w, h){
        this.panel.setSize(w, h);
        this.panel.doLayout();
    },

    
    reset : function(){
        if (this.originalValue) {
            
            this.eachItem(function(c){
                if(c.setValue){
                    c.setValue(false);
                    c.originalValue = c.getValue();
                }
            });
            
            
            this.resetOriginal = true;
            this.setValue(this.originalValue);
            delete this.resetOriginal;
        } else {
            this.eachItem(function(c){
                if(c.reset){
                    c.reset();
                }
            });
        }
        
        
        (function() {
            this.clearInvalid();
        }).defer(50, this);
    },

    
    setValue: function(){
        if(this.rendered){
            this.onSetValue.apply(this, arguments);
        }else{
            this.buffered = true;
            this.value = arguments;
        }
        return this;
    },

    
    onSetValue: function(id, value){
        if(arguments.length == 1){
            if(Ext.isArray(id)){
                Ext.each(id, function(val, idx){
                    if (Ext.isObject(val) && val.setValue){ 
                        val.setValue(true);
                        if (this.resetOriginal === true) {
                            val.originalValue = val.getValue();
                        }
                    } else { 
                        var item = this.items.itemAt(idx);
                        if(item){
                            item.setValue(val);
                        }
                    }
                }, this);
            }else if(Ext.isObject(id)){
                
                for(var i in id){
                    var f = this.getBox(i);
                    if(f){
                        f.setValue(id[i]);
                    }
                }
            }else{
                this.setValueForItem(id);
            }
        }else{
            var f = this.getBox(id);
            if(f){
                f.setValue(value);
            }
        }
    },

    
    beforeDestroy: function(){
        Ext.destroy(this.panel);
        Ext.form.CheckboxGroup.superclass.beforeDestroy.call(this);

    },

    setValueForItem : function(val){
        val = String(val).split(',');
        this.eachItem(function(item){
            if(val.indexOf(item.inputValue)> -1){
                item.setValue(true);
            }
        });
    },

    
    getBox : function(id){
        var box = null;
        this.eachItem(function(f){
            if(id == f || f.dataIndex == id || f.id == id || f.getName() == id){
                box = f;
                return false;
            }
        });
        return box;
    },

    
    getValue : function(){
        var out = [];
        this.eachItem(function(item){
            if(item.checked){
                out.push(item);
            }
        });
        return out;
    },

    
    eachItem: function(fn, scope) {
        if(this.items && this.items.each){
            this.items.each(fn, scope || this);
        }
    },

    

    
    getRawValue : Ext.emptyFn,

    
    setRawValue : Ext.emptyFn

});

Ext.reg('checkboxgroup', Ext.form.CheckboxGroup);

Ext.form.CompositeField = Ext.extend(Ext.form.Field, {

    
    defaultMargins: '0 5 0 0',

    
    skipLastItemMargin: true,

    
    isComposite: true,

    
    combineErrors: true,

    
    
    initComponent: function() {
        var labels = [],
            items  = this.items,
            item;

        for (var i=0, j = items.length; i < j; i++) {
            item = items[i];

            labels.push(item.fieldLabel);

            
            Ext.apply(item, this.defaults);

            
            if (!(i == j - 1 && this.skipLastItemMargin)) {
                Ext.applyIf(item, {margins: this.defaultMargins});
            }
        }

        this.fieldLabel = this.fieldLabel || this.buildLabel(labels);

        
        this.fieldErrors = new Ext.util.MixedCollection(true, function(item) {
            return item.field;
        });

        this.fieldErrors.on({
            scope  : this,
            add    : this.updateInvalidMark,
            remove : this.updateInvalidMark,
            replace: this.updateInvalidMark
        });

        Ext.form.CompositeField.superclass.initComponent.apply(this, arguments);
    },

    
    onRender: function(ct, position) {
        if (!this.el) {
            
            var innerCt = this.innerCt = new Ext.Container({
                layout  : 'hbox',
                renderTo: ct,
                items   : this.items,
                cls     : 'x-form-composite',
                defaultMargins: '0 3 0 0'
            });

            this.el = innerCt.getEl();

            var fields = innerCt.findBy(function(c) {
                return c.isFormField;
            }, this);

            
            this.items = new Ext.util.MixedCollection();
            this.items.addAll(fields);

            
            
            if (this.combineErrors) {
                this.eachItem(function(field) {
                    Ext.apply(field, {
                        markInvalid : this.onFieldMarkInvalid.createDelegate(this, [field], 0),
                        clearInvalid: this.onFieldClearInvalid.createDelegate(this, [field], 0)
                    });
                });
            }

            
            var l = this.el.parent().parent().child('label', true);
            if (l) {
                l.setAttribute('for', this.items.items[0].id);
            }
        }

        Ext.form.CompositeField.superclass.onRender.apply(this, arguments);
    },

    
    onFieldMarkInvalid: function(field, message) {
        var name  = field.getName(),
            error = {field: name, error: message};

        this.fieldErrors.replace(name, error);

        field.el.addClass(field.invalidClass);
    },

    
    onFieldClearInvalid: function(field) {
        this.fieldErrors.removeKey(field.getName());

        field.el.removeClass(field.invalidClass);
    },

    
    updateInvalidMark: function() {
        var ieStrict = Ext.isIE6 && Ext.isStrict;

        if (this.fieldErrors.length == 0) {
            this.clearInvalid();

            
            if (ieStrict) {
                this.clearInvalid.defer(50, this);
            }
        } else {
            var message = this.buildCombinedErrorMessage(this.fieldErrors.items);

            this.sortErrors();
            this.markInvalid(message);

            
            if (ieStrict) {
                this.markInvalid(message);
            }
        }
    },

    
    validateValue: function() {
        var valid = true;

        this.eachItem(function(field) {
            if (!field.isValid()) valid = false;
        });

        return valid;
    },

    
    buildCombinedErrorMessage: function(errors) {
        var combined = [],
            error;

        for (var i = 0, j = errors.length; i < j; i++) {
            error = errors[i];

            combined.push(String.format("{0}: {1}", error.field, error.error));
        }

        return combined.join("<br />");
    },

    
    sortErrors: function() {
        var fields = this.items;

        this.fieldErrors.sort("ASC", function(a, b) {
            var findByName = function(key) {
                return function(field) {
                    return field.getName() == key;
                };
            };

            var aIndex = fields.findIndexBy(findByName(a.field)),
                bIndex = fields.findIndexBy(findByName(b.field));

            return aIndex < bIndex ? -1 : 1;
        });
    },

    
    reset: function() {
        this.eachItem(function(item) {
            item.reset();
        });

        
        
        (function() {
            this.clearInvalid();
        }).defer(50, this);
    },
    
    
    clearInvalidChildren: function() {
        this.eachItem(function(item) {
            item.clearInvalid();
        });
    },

    
    buildLabel: function(segments) {
        return segments.join(", ");
    },

    
    isDirty: function(){
        
        if (this.disabled || !this.rendered) {
            return false;
        }

        var dirty = false;
        this.eachItem(function(item){
            if(item.isDirty()){
                dirty = true;
                return false;
            }
        });
        return dirty;
    },

    
    eachItem: function(fn, scope) {
        if(this.items && this.items.each){
            this.items.each(fn, scope || this);
        }
    },

    
    onResize: function(adjWidth, adjHeight, rawWidth, rawHeight) {
        var innerCt = this.innerCt;

        if (this.rendered && innerCt.rendered) {
            innerCt.setSize(adjWidth, adjHeight);
        }

        Ext.form.CompositeField.superclass.onResize.apply(this, arguments);
    },

    
    doLayout: function(shallow, force) {
        if (this.rendered) {
            var innerCt = this.innerCt;

            innerCt.forceLayout = this.ownerCt.forceLayout;
            innerCt.doLayout(shallow, force);
        }
    },

    
    beforeDestroy: function(){
        Ext.destroy(this.innerCt);

        Ext.form.CompositeField.superclass.beforeDestroy.call(this);
    },

    
    setReadOnly : function(readOnly) {
        readOnly = readOnly || true;

        if(this.rendered){
            this.eachItem(function(item){
                item.setReadOnly(readOnly);
            });
        }
        this.readOnly = readOnly;
    },

    onShow : function() {
        Ext.form.CompositeField.superclass.onShow.call(this);
        this.doLayout();
    },

    
    onDisable : function(){
        this.eachItem(function(item){
            item.disable();
        });
    },

    
    onEnable : function(){
        this.eachItem(function(item){
            item.enable();
        });
    }
});

Ext.reg('compositefield', Ext.form.CompositeField);

Ext.form.Radio = Ext.extend(Ext.form.Checkbox, {
    inputType: 'radio',

    
    markInvalid : Ext.emptyFn,
    
    clearInvalid : Ext.emptyFn,

    
    getGroupValue : function(){
    	var p = this.el.up('form') || Ext.getBody();
        var c = p.child('input[name='+this.el.dom.name+']:checked', true);
        return c ? c.value : null;
    },

    
    onClick : function(){
    	if(this.el.dom.checked != this.checked){
			var els = this.getCheckEl().select('input[name=' + this.el.dom.name + ']');
			els.each(function(el){
				if(el.dom.id == this.id){
					this.setValue(true);
				}else{
					Ext.getCmp(el.dom.id).setValue(false);
				}
			}, this);
		}
    },

    
    setValue : function(v){
    	if (typeof v == 'boolean') {
            Ext.form.Radio.superclass.setValue.call(this, v);
        } else if (this.rendered) {
            var r = this.getCheckEl().child('input[name=' + this.el.dom.name + '][value=' + v + ']', true);
            if(r){
                Ext.getCmp(r.id).setValue(true);
            }
        }
        return this;
    },

    
    getCheckEl: function(){
        if(this.inGroup){
            return this.el.up('.x-form-radio-group')
        }
        return this.el.up('form') || Ext.getBody();
    }
});
Ext.reg('radio', Ext.form.Radio);

Ext.form.RadioGroup = Ext.extend(Ext.form.CheckboxGroup, {
    
    
    allowBlank : true,
    
    blankText : 'You must select one item in this group',
    
    
    defaultType : 'radio',
    
    
    groupCls : 'x-form-radio-group',
    
    
    
    
    getValue : function(){
        var out = null;
        this.eachItem(function(item){
            if(item.checked){
                out = item;
                return false;
            }
        });
        return out;
    },
    
    
    onSetValue : function(id, value){
        if(arguments.length > 1){
            var f = this.getBox(id);
            if(f){
                f.setValue(value);
                if(f.checked){
                    this.eachItem(function(item){
                        if (item !== f){
                            item.setValue(false);
                        }
                    });
                }
            }
        }else{
            this.setValueForItem(id);
        }
    },
    
    setValueForItem : function(val){
        val = String(val).split(',')[0];
        this.eachItem(function(item){
            item.setValue(val == item.inputValue);
        });
    },
    
    
    fireChecked : function(){
        if(!this.checkTask){
            this.checkTask = new Ext.util.DelayedTask(this.bufferChecked, this);
        }
        this.checkTask.delay(10);
    },
    
    
    bufferChecked : function(){
        var out = null;
        this.eachItem(function(item){
            if(item.checked){
                out = item;
                return false;
            }
        });
        this.fireEvent('change', this, out);
    },
    
    onDestroy : function(){
        if(this.checkTask){
            this.checkTask.cancel();
            this.checkTask = null;
        }
        Ext.form.RadioGroup.superclass.onDestroy.call(this);
    }

});

Ext.reg('radiogroup', Ext.form.RadioGroup);

Ext.form.Hidden = Ext.extend(Ext.form.Field, {
    
    inputType : 'hidden',

    
    onRender : function(){
        Ext.form.Hidden.superclass.onRender.apply(this, arguments);
    },

    
    initEvents : function(){
        this.originalValue = this.getValue();
    },

    
    setSize : Ext.emptyFn,
    setWidth : Ext.emptyFn,
    setHeight : Ext.emptyFn,
    setPosition : Ext.emptyFn,
    setPagePosition : Ext.emptyFn,
    markInvalid : Ext.emptyFn,
    clearInvalid : Ext.emptyFn
});
Ext.reg('hidden', Ext.form.Hidden);
Ext.form.BasicForm = Ext.extend(Ext.util.Observable, {

    constructor: function(el, config){
        Ext.apply(this, config);
        if(Ext.isString(this.paramOrder)){
            this.paramOrder = this.paramOrder.split(/[\s,|]/);
        }
        
        this.items = new Ext.util.MixedCollection(false, function(o){
            return o.getItemId();
        });
        this.addEvents(
            
            'beforeaction',
            
            'actionfailed',
            
            'actioncomplete'
        );

        if(el){
            this.initEl(el);
        }
        Ext.form.BasicForm.superclass.constructor.call(this);
    },

    
    
    
    
    
    
    
    timeout: 30,

    

    
    paramOrder: undefined,

    
    paramsAsHash: false,

    
    waitTitle: 'Please Wait...',

    
    activeAction : null,

    
    trackResetOnLoad : false,

    
    

    
    initEl : function(el){
        this.el = Ext.get(el);
        this.id = this.el.id || Ext.id();
        if(!this.standardSubmit){
            this.el.on('submit', this.onSubmit, this);
        }
        this.el.addClass('x-form');
    },

    
    getEl: function(){
        return this.el;
    },

    
    onSubmit : function(e){
        e.stopEvent();
    },

    
    destroy: function(bound){
        if(bound !== true){
            this.items.each(function(f){
                Ext.destroy(f);
            });
            Ext.destroy(this.el);
        }
        this.items.clear();
        this.purgeListeners();
    },

    
    isValid : function(){
        var valid = true;
        this.items.each(function(f){
           if(!f.validate()){
               valid = false;
           }
        });
        return valid;
    },

    
    isDirty : function(){
        var dirty = false;
        this.items.each(function(f){
           if(f.isDirty()){
               dirty = true;
               return false;
           }
        });
        return dirty;
    },

    
    doAction : function(action, options){
        if(Ext.isString(action)){
            action = new Ext.form.Action.ACTION_TYPES[action](this, options);
        }
        if(this.fireEvent('beforeaction', this, action) !== false){
            this.beforeAction(action);
            action.run.defer(100, action);
        }
        return this;
    },

    
    submit : function(options){
        options = options || {};
        if(this.standardSubmit){
            var v = options.clientValidation === false || this.isValid();
            if(v){
                var el = this.el.dom;
                if(this.url && Ext.isEmpty(el.action)){
                    el.action = this.url;
                }
                el.submit();
            }
            return v;
        }
        var submitAction = String.format('{0}submit', this.api ? 'direct' : '');
        this.doAction(submitAction, options);
        return this;
    },

    
    load : function(options){
        var loadAction = String.format('{0}load', this.api ? 'direct' : '');
        this.doAction(loadAction, options);
        return this;
    },

    
    updateRecord : function(record){
        record.beginEdit();
        var fs = record.fields;
        fs.each(function(f){
            var field = this.findField(f.name);
            if(field){
                record.set(f.name, field.getValue());
            }
        }, this);
        record.endEdit();
        return this;
    },

    
    loadRecord : function(record){
        this.setValues(record.data);
        return this;
    },

    
    beforeAction : function(action){
        
        this.items.each(function(f){
            if(f.isFormField && f.syncValue){
                f.syncValue();
            }
        });
        var o = action.options;
        if(o.waitMsg){
            if(this.waitMsgTarget === true){
                this.el.mask(o.waitMsg, 'x-mask-loading');
            }else if(this.waitMsgTarget){
                this.waitMsgTarget = Ext.get(this.waitMsgTarget);
                this.waitMsgTarget.mask(o.waitMsg, 'x-mask-loading');
            }else{
                Ext.MessageBox.wait(o.waitMsg, o.waitTitle || this.waitTitle);
            }
        }
    },

    
    afterAction : function(action, success){
        this.activeAction = null;
        var o = action.options;
        if(o.waitMsg){
            if(this.waitMsgTarget === true){
                this.el.unmask();
            }else if(this.waitMsgTarget){
                this.waitMsgTarget.unmask();
            }else{
                Ext.MessageBox.updateProgress(1);
                Ext.MessageBox.hide();
            }
        }
        if(success){
            if(o.reset){
                this.reset();
            }
            Ext.callback(o.success, o.scope, [this, action]);
            this.fireEvent('actioncomplete', this, action);
        }else{
            Ext.callback(o.failure, o.scope, [this, action]);
            this.fireEvent('actionfailed', this, action);
        }
    },

    
    findField : function(id) {
        var field = this.items.get(id);

        if (!Ext.isObject(field)) {
            
            var findMatchingField = function(f) {
                if (f.isFormField) {
                    if (f.dataIndex == id || f.id == id || f.getName() == id) {
                        field = f;
                        return false;
                    } else if (f.isComposite && f.rendered) {
                        return f.items.each(findMatchingField);
                    }
                }
            };

            this.items.each(findMatchingField);
        }
        return field || null;
    },


    
    markInvalid : function(errors){
        if (Ext.isArray(errors)) {
            for(var i = 0, len = errors.length; i < len; i++){
                var fieldError = errors[i];
                var f = this.findField(fieldError.id);
                if(f){
                    f.markInvalid(fieldError.msg);
                }
            }
        } else {
            var field, id;
            for(id in errors){
                if(!Ext.isFunction(errors[id]) && (field = this.findField(id))){
                    field.markInvalid(errors[id]);
                }
            }
        }

        return this;
    },

    
    setValues : function(values){
        if(Ext.isArray(values)){ 
            for(var i = 0, len = values.length; i < len; i++){
                var v = values[i];
                var f = this.findField(v.id);
                if(f){
                    f.setValue(v.value);
                    if(this.trackResetOnLoad){
                        f.originalValue = f.getValue();
                    }
                }
            }
        }else{ 
            var field, id;
            for(id in values){
                if(!Ext.isFunction(values[id]) && (field = this.findField(id))){
                    field.setValue(values[id]);
                    if(this.trackResetOnLoad){
                        field.originalValue = field.getValue();
                    }
                }
            }
        }
        return this;
    },

    
    getValues : function(asString){
        var fs = Ext.lib.Ajax.serializeForm(this.el.dom);
        if(asString === true){
            return fs;
        }
        return Ext.urlDecode(fs);
    },

    
    getFieldValues : function(dirtyOnly){
        var o = {},
            n,
            key,
            val;
        this.items.each(function(f) {
            if (dirtyOnly !== true || f.isDirty()) {
                n = f.getName();
                key = o[n];
                val = f.getValue();

                if(Ext.isDefined(key)){
                    if(Ext.isArray(key)){
                        o[n].push(val);
                    }else{
                        o[n] = [key, val];
                    }
                }else{
                    o[n] = val;
                }
            }
        });
        return o;
    },

    
    clearInvalid : function(){
        this.items.each(function(f){
           f.clearInvalid();
        });
        return this;
    },

    
    reset : function(){
        this.items.each(function(f){
            f.reset();
        });
        return this;
    },

    
    add : function(){
        this.items.addAll(Array.prototype.slice.call(arguments, 0));
        return this;
    },

    
    remove : function(field){
        this.items.remove(field);
        return this;
    },

    
    cleanDestroyed : function() {
        this.items.filterBy(function(o) { return !!o.isDestroyed; }).each(this.remove, this);
    },

    
    render : function(){
        this.items.each(function(f){
            if(f.isFormField && !f.rendered && document.getElementById(f.id)){ 
                f.applyToMarkup(f.id);
            }
        });
        return this;
    },

    
    applyToFields : function(o){
        this.items.each(function(f){
           Ext.apply(f, o);
        });
        return this;
    },

    
    applyIfToFields : function(o){
        this.items.each(function(f){
           Ext.applyIf(f, o);
        });
        return this;
    },

    callFieldMethod : function(fnName, args){
        args = args || [];
        this.items.each(function(f){
            if(Ext.isFunction(f[fnName])){
                f[fnName].apply(f, args);
            }
        });
        return this;
    }
});


Ext.BasicForm = Ext.form.BasicForm;

Ext.FormPanel = Ext.extend(Ext.Panel, {
    
    
    
    
    
    
    


    
    minButtonWidth : 75,

    
    labelAlign : 'left',

    
    monitorValid : false,

    
    monitorPoll : 200,

    
    layout : 'form',

    
    initComponent : function(){
        this.form = this.createForm();
        Ext.FormPanel.superclass.initComponent.call(this);

        this.bodyCfg = {
            tag: 'form',
            cls: this.baseCls + '-body',
            method : this.method || 'POST',
            id : this.formId || Ext.id()
        };
        if(this.fileUpload) {
            this.bodyCfg.enctype = 'multipart/form-data';
        }
        this.initItems();

        this.addEvents(
            
            'clientvalidation'
        );

        this.relayEvents(this.form, ['beforeaction', 'actionfailed', 'actioncomplete']);
    },

    
    createForm : function(){
        var config = Ext.applyIf({listeners: {}}, this.initialConfig);
        return new Ext.form.BasicForm(null, config);
    },

    
    initFields : function(){
        var f = this.form;
        var formPanel = this;
        var fn = function(c){
            if(formPanel.isField(c)){
                f.add(c);
            }else if(c.findBy && c != formPanel){
                formPanel.applySettings(c);
                
                if(c.items && c.items.each){
                    c.items.each(fn, this);
                }
            }
        };
        this.items.each(fn, this);
    },

    
    applySettings: function(c){
        var ct = c.ownerCt;
        Ext.applyIf(c, {
            labelAlign: ct.labelAlign,
            labelWidth: ct.labelWidth,
            itemCls: ct.itemCls
        });
    },

    
    getLayoutTarget : function(){
        return this.form.el;
    },

    
    getForm : function(){
        return this.form;
    },

    
    onRender : function(ct, position){
        this.initFields();
        Ext.FormPanel.superclass.onRender.call(this, ct, position);
        this.form.initEl(this.body);
    },

    
    beforeDestroy : function(){
        this.stopMonitoring();
        this.form.destroy(true);
        Ext.FormPanel.superclass.beforeDestroy.call(this);
    },

    
    isField : function(c) {
        return !!c.setValue && !!c.getValue && !!c.markInvalid && !!c.clearInvalid;
    },

    
    initEvents : function(){
        Ext.FormPanel.superclass.initEvents.call(this);
        
        this.on({
            scope: this,
            add: this.onAddEvent,
            remove: this.onRemoveEvent
        });
        if(this.monitorValid){ 
            this.startMonitoring();
        }
    },

    
    onAdd: function(c){
        Ext.FormPanel.superclass.onAdd.call(this, c);
        this.processAdd(c);
    },

    
    onAddEvent: function(ct, c){
        if(ct !== this){
            this.processAdd(c);
        }
    },

    
    processAdd : function(c){
        
        if(this.isField(c)){
            this.form.add(c);
        
        }else if(c.findBy){
            this.applySettings(c);
            this.form.add.apply(this.form, c.findBy(this.isField));
        }
    },

    
    onRemove: function(c){
        Ext.FormPanel.superclass.onRemove.call(this, c);
        this.processRemove(c);
    },

    onRemoveEvent: function(ct, c){
        if(ct !== this){
            this.processRemove(c);
        }
    },

    
    processRemove: function(c){
        if(!this.destroying){
            
            if(this.isField(c)){
                this.form.remove(c);
            
            }else if (c.findBy){
                Ext.each(c.findBy(this.isField), this.form.remove, this.form);
                if (c.isDestroyed) {
                    this.form.cleanDestroyed();
                }
            }
        }
    },

    
    startMonitoring : function(){
        if(!this.validTask){
            this.validTask = new Ext.util.TaskRunner();
            this.validTask.start({
                run : this.bindHandler,
                interval : this.monitorPoll || 200,
                scope: this
            });
        }
    },

    
    stopMonitoring : function(){
        if(this.validTask){
            this.validTask.stopAll();
            this.validTask = null;
        }
    },

    
    load : function(){
        this.form.load.apply(this.form, arguments);
    },

    
    onDisable : function(){
        Ext.FormPanel.superclass.onDisable.call(this);
        if(this.form){
            this.form.items.each(function(){
                 this.disable();
            });
        }
    },

    
    onEnable : function(){
        Ext.FormPanel.superclass.onEnable.call(this);
        if(this.form){
            this.form.items.each(function(){
                 this.enable();
            });
        }
    },

    
    bindHandler : function(){
        var valid = true;
        this.form.items.each(function(f){
            if(!f.isValid(true)){
                valid = false;
                return false;
            }
        });
        if(this.fbar){
            var fitems = this.fbar.items.items;
            for(var i = 0, len = fitems.length; i < len; i++){
                var btn = fitems[i];
                if(btn.formBind === true && btn.disabled === valid){
                    btn.setDisabled(!valid);
                }
            }
        }
        this.fireEvent('clientvalidation', this, valid);
    }
});
Ext.reg('form', Ext.FormPanel);

Ext.form.FormPanel = Ext.FormPanel;

Ext.form.FieldSet = Ext.extend(Ext.Panel, {
    
    
    
    
    
    
    baseCls : 'x-fieldset',
    
    layout : 'form',
    
    animCollapse : false,

    
    onRender : function(ct, position){
        if(!this.el){
            this.el = document.createElement('fieldset');
            this.el.id = this.id;
            if (this.title || this.header || this.checkboxToggle) {
                this.el.appendChild(document.createElement('legend')).className = this.baseCls + '-header';
            }
        }

        Ext.form.FieldSet.superclass.onRender.call(this, ct, position);

        if(this.checkboxToggle){
            var o = typeof this.checkboxToggle == 'object' ?
                    this.checkboxToggle :
                    {tag: 'input', type: 'checkbox', name: this.checkboxName || this.id+'-checkbox'};
            this.checkbox = this.header.insertFirst(o);
            this.checkbox.dom.checked = !this.collapsed;
            this.mon(this.checkbox, 'click', this.onCheckClick, this);
        }
    },

    
    onCollapse : function(doAnim, animArg){
        if(this.checkbox){
            this.checkbox.dom.checked = false;
        }
        Ext.form.FieldSet.superclass.onCollapse.call(this, doAnim, animArg);

    },

    
    onExpand : function(doAnim, animArg){
        if(this.checkbox){
            this.checkbox.dom.checked = true;
        }
        Ext.form.FieldSet.superclass.onExpand.call(this, doAnim, animArg);
    },

    
    onCheckClick : function(){
        this[this.checkbox.dom.checked ? 'expand' : 'collapse']();
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
});
Ext.reg('fieldset', Ext.form.FieldSet);

Ext.form.HtmlEditor = Ext.extend(Ext.form.Field, {
    
    enableFormat : true,
    
    enableFontSize : true,
    
    enableColors : true,
    
    enableAlignments : true,
    
    enableLists : true,
    
    enableSourceEdit : true,
    
    enableLinks : true,
    
    enableFont : true,
    
    createLinkText : 'Please enter the URL for the link:',
    
    defaultLinkValue : 'http:/'+'/',
    
    fontFamilies : [
        'Arial',
        'Courier New',
        'Tahoma',
        'Times New Roman',
        'Verdana'
    ],
    defaultFont: 'tahoma',
    
    defaultValue: (Ext.isOpera || Ext.isIE6) ? '&#160;' : '&#8203;',

    
    actionMode: 'wrap',
    validationEvent : false,
    deferHeight: true,
    initialized : false,
    activated : false,
    sourceEditMode : false,
    onFocus : Ext.emptyFn,
    iframePad:3,
    hideMode:'offsets',
    defaultAutoCreate : {
        tag: "textarea",
        style:"width:500px;height:300px;",
        autocomplete: "off"
    },

    
    initComponent : function(){
        this.addEvents(
            
            'initialize',
            
            'activate',
             
            'beforesync',
             
            'beforepush',
             
            'sync',
             
            'push',
             
            'editmodechange'
        );
    },

    
    createFontOptions : function(){
        var buf = [], fs = this.fontFamilies, ff, lc;
        for(var i = 0, len = fs.length; i< len; i++){
            ff = fs[i];
            lc = ff.toLowerCase();
            buf.push(
                '<option value="',lc,'" style="font-family:',ff,';"',
                    (this.defaultFont == lc ? ' selected="true">' : '>'),
                    ff,
                '</option>'
            );
        }
        return buf.join('');
    },

    
    createToolbar : function(editor){
        var items = [];
        var tipsEnabled = Ext.QuickTips && Ext.QuickTips.isEnabled();


        function btn(id, toggle, handler){
            return {
                itemId : id,
                cls : 'x-btn-icon',
                iconCls: 'x-edit-'+id,
                enableToggle:toggle !== false,
                scope: editor,
                handler:handler||editor.relayBtnCmd,
                clickEvent:'mousedown',
                tooltip: tipsEnabled ? editor.buttonTips[id] || undefined : undefined,
                overflowText: editor.buttonTips[id].title || undefined,
                tabIndex:-1
            };
        }


        if(this.enableFont && !Ext.isSafari2){
            var fontSelectItem = new Ext.Toolbar.Item({
               autoEl: {
                    tag:'select',
                    cls:'x-font-select',
                    html: this.createFontOptions()
               }
            });

            items.push(
                fontSelectItem,
                '-'
            );
        }

        if(this.enableFormat){
            items.push(
                btn('bold'),
                btn('italic'),
                btn('underline')
            );
        }

        if(this.enableFontSize){
            items.push(
                '-',
                btn('increasefontsize', false, this.adjustFont),
                btn('decreasefontsize', false, this.adjustFont)
            );
        }

        if(this.enableColors){
            items.push(
                '-', {
                    itemId:'forecolor',
                    cls:'x-btn-icon',
                    iconCls: 'x-edit-forecolor',
                    clickEvent:'mousedown',
                    tooltip: tipsEnabled ? editor.buttonTips.forecolor || undefined : undefined,
                    tabIndex:-1,
                    menu : new Ext.menu.ColorMenu({
                        allowReselect: true,
                        focus: Ext.emptyFn,
                        value:'000000',
                        plain:true,
                        listeners: {
                            scope: this,
                            select: function(cp, color){
                                this.execCmd('forecolor', Ext.isWebKit || Ext.isIE ? '#'+color : color);
                                this.deferFocus();
                            }
                        },
                        clickEvent:'mousedown'
                    })
                }, {
                    itemId:'backcolor',
                    cls:'x-btn-icon',
                    iconCls: 'x-edit-backcolor',
                    clickEvent:'mousedown',
                    tooltip: tipsEnabled ? editor.buttonTips.backcolor || undefined : undefined,
                    tabIndex:-1,
                    menu : new Ext.menu.ColorMenu({
                        focus: Ext.emptyFn,
                        value:'FFFFFF',
                        plain:true,
                        allowReselect: true,
                        listeners: {
                            scope: this,
                            select: function(cp, color){
                                if(Ext.isGecko){
                                    this.execCmd('useCSS', false);
                                    this.execCmd('hilitecolor', color);
                                    this.execCmd('useCSS', true);
                                    this.deferFocus();
                                }else{
                                    this.execCmd(Ext.isOpera ? 'hilitecolor' : 'backcolor', Ext.isWebKit || Ext.isIE ? '#'+color : color);
                                    this.deferFocus();
                                }
                            }
                        },
                        clickEvent:'mousedown'
                    })
                }
            );
        }

        if(this.enableAlignments){
            items.push(
                '-',
                btn('justifyleft'),
                btn('justifycenter'),
                btn('justifyright')
            );
        }

        if(!Ext.isSafari2){
            if(this.enableLinks){
                items.push(
                    '-',
                    btn('createlink', false, this.createLink)
                );
            }

            if(this.enableLists){
                items.push(
                    '-',
                    btn('insertorderedlist'),
                    btn('insertunorderedlist')
                );
            }
            if(this.enableSourceEdit){
                items.push(
                    '-',
                    btn('sourceedit', true, function(btn){
                        this.toggleSourceEdit(!this.sourceEditMode);
                    })
                );
            }
        }

        
        var tb = new Ext.Toolbar({
            renderTo: this.wrap.dom.firstChild,
            items: items
        });

        if (fontSelectItem) {
            this.fontSelect = fontSelectItem.el;

            this.mon(this.fontSelect, 'change', function(){
                var font = this.fontSelect.dom.value;
                this.relayCmd('fontname', font);
                this.deferFocus();
            }, this);
        }

        
        this.mon(tb.el, 'click', function(e){
            e.preventDefault();
        });

        this.tb = tb;
        this.tb.doLayout();
    },

    onDisable: function(){
        this.wrap.mask();
        Ext.form.HtmlEditor.superclass.onDisable.call(this);
    },

    onEnable: function(){
        this.wrap.unmask();
        Ext.form.HtmlEditor.superclass.onEnable.call(this);
    },

    setReadOnly: function(readOnly){

        Ext.form.HtmlEditor.superclass.setReadOnly.call(this, readOnly);
        if(this.initialized){
            if(Ext.isIE){
                this.getEditorBody().contentEditable = !readOnly;
            }else{
                this.setDesignMode(!readOnly);
            }
            var bd = this.getEditorBody();
            if(bd){
                bd.style.cursor = this.readOnly ? 'default' : 'text';
            }
            this.disableItems(readOnly);
        }
    },

    
    getDocMarkup : function(){
        var h = Ext.fly(this.iframe).getHeight() - this.iframePad * 2;
        return String.format('<html><head><style type="text/css">body{border: 0; margin: 0; padding: {0}px; height: {1}px; cursor: text}</style></head><body></body></html>', this.iframePad, h);
    },

    
    getEditorBody : function(){
        var doc = this.getDoc();
        return doc.body || doc.documentElement;
    },

    
    getDoc : function(){
        return Ext.isIE ? this.getWin().document : (this.iframe.contentDocument || this.getWin().document);
    },

    
    getWin : function(){
        return Ext.isIE ? this.iframe.contentWindow : window.frames[this.iframe.name];
    },

    
    onRender : function(ct, position){
        Ext.form.HtmlEditor.superclass.onRender.call(this, ct, position);
        this.el.dom.style.border = '0 none';
        this.el.dom.setAttribute('tabIndex', -1);
        this.el.addClass('x-hidden');
        if(Ext.isIE){ 
            this.el.applyStyles('margin-top:-1px;margin-bottom:-1px;');
        }
        this.wrap = this.el.wrap({
            cls:'x-html-editor-wrap', cn:{cls:'x-html-editor-tb'}
        });

        this.createToolbar(this);

        this.disableItems(true);

        this.tb.doLayout();

        this.createIFrame();

        if(!this.width){
            var sz = this.el.getSize();
            this.setSize(sz.width, this.height || sz.height);
        }
        this.resizeEl = this.positionEl = this.wrap;
    },

    createIFrame: function(){
        var iframe = document.createElement('iframe');
        iframe.name = Ext.id();
        iframe.frameBorder = '0';
        iframe.style.overflow = 'auto';

        this.wrap.dom.appendChild(iframe);
        this.iframe = iframe;

        this.monitorTask = Ext.TaskMgr.start({
            run: this.checkDesignMode,
            scope: this,
            interval:100
        });
    },

    initFrame : function(){
        Ext.TaskMgr.stop(this.monitorTask);
        var doc = this.getDoc();
        this.win = this.getWin();

        doc.open();
        doc.write(this.getDocMarkup());
        doc.close();

        var task = { 
            run : function(){
                var doc = this.getDoc();
                if(doc.body || doc.readyState == 'complete'){
                    Ext.TaskMgr.stop(task);
                    this.setDesignMode(true);
                    this.initEditor.defer(10, this);
                }
            },
            interval : 10,
            duration:10000,
            scope: this
        };
        Ext.TaskMgr.start(task);
    },


    checkDesignMode : function(){
        if(this.wrap && this.wrap.dom.offsetWidth){
            var doc = this.getDoc();
            if(!doc){
                return;
            }
            if(!doc.editorInitialized || this.getDesignMode() != 'on'){
                this.initFrame();
            }
        }
    },

    
    setDesignMode : function(mode){
        var doc ;
        if(doc = this.getDoc()){
            if(this.readOnly){
                mode = false;
            }
            doc.designMode = (/on|true/i).test(String(mode).toLowerCase()) ?'on':'off';
        }

    },

    
    getDesignMode : function(){
        var doc = this.getDoc();
        if(!doc){ return ''; }
        return String(doc.designMode).toLowerCase();

    },

    disableItems: function(disabled){
        if(this.fontSelect){
            this.fontSelect.dom.disabled = disabled;
        }
        this.tb.items.each(function(item){
            if(item.getItemId() != 'sourceedit'){
                item.setDisabled(disabled);
            }
        });
    },

    
    onResize : function(w, h){
        Ext.form.HtmlEditor.superclass.onResize.apply(this, arguments);
        if(this.el && this.iframe){
            if(Ext.isNumber(w)){
                var aw = w - this.wrap.getFrameWidth('lr');
                this.el.setWidth(aw);
                this.tb.setWidth(aw);
                this.iframe.style.width = Math.max(aw, 0) + 'px';
            }
            if(Ext.isNumber(h)){
                var ah = h - this.wrap.getFrameWidth('tb') - this.tb.el.getHeight();
                this.el.setHeight(ah);
                this.iframe.style.height = Math.max(ah, 0) + 'px';
                var bd = this.getEditorBody();
                if(bd){
                    bd.style.height = Math.max((ah - (this.iframePad*2)), 0) + 'px';
                }
            }
        }
    },

    
    toggleSourceEdit : function(sourceEditMode){
        var iframeHeight,
            elHeight,
            ls;

        if (sourceEditMode === undefined) {
            sourceEditMode = !this.sourceEditMode;
        }
        this.sourceEditMode = sourceEditMode === true;
        var btn = this.tb.getComponent('sourceedit');

        if (btn.pressed !== this.sourceEditMode) {
            btn.toggle(this.sourceEditMode);
            if (!btn.xtbHidden) {
                return;
            }
        }
        if (this.sourceEditMode) {
            
            ls = this.getSize();

            iframeHeight = Ext.get(this.iframe).getHeight();

            this.disableItems(true);
            this.syncValue();
            this.iframe.className = 'x-hidden';
            this.el.removeClass('x-hidden');
            this.el.dom.removeAttribute('tabIndex');
            this.el.focus();
            this.el.dom.style.height = iframeHeight + 'px';
        }
        else {
            elHeight = parseInt(this.el.dom.style.height, 10);
            if (this.initialized) {
                this.disableItems(this.readOnly);
            }
            this.pushValue();
            this.iframe.className = '';
            this.el.addClass('x-hidden');
            this.el.dom.setAttribute('tabIndex', -1);
            this.deferFocus();

            this.setSize(ls);
            this.iframe.style.height = elHeight + 'px';
        }
        this.fireEvent('editmodechange', this, this.sourceEditMode);
    },

    
    createLink : function() {
        var url = prompt(this.createLinkText, this.defaultLinkValue);
        if(url && url != 'http:/'+'/'){
            this.relayCmd('createlink', url);
        }
    },

    
    initEvents : function(){
        this.originalValue = this.getValue();
    },

    
    markInvalid : Ext.emptyFn,

    
    clearInvalid : Ext.emptyFn,

    
    setValue : function(v){
        Ext.form.HtmlEditor.superclass.setValue.call(this, v);
        this.pushValue();
        return this;
    },

    
    cleanHtml: function(html) {
        html = String(html);
        if(Ext.isWebKit){ 
            html = html.replace(/\sclass="(?:Apple-style-span|khtml-block-placeholder)"/gi, '');
        }

        
        if(html.charCodeAt(0) == this.defaultValue.replace(/\D/g, '')){
            html = html.substring(1);
        }
        return html;
    },

    
    syncValue : function(){
        if(this.initialized){
            var bd = this.getEditorBody();
            var html = bd.innerHTML;
            if(Ext.isWebKit){
                var bs = bd.getAttribute('style'); 
                var m = bs.match(/text-align:(.*?);/i);
                if(m && m[1]){
                    html = '<div style="'+m[0]+'">' + html + '</div>';
                }
            }
            html = this.cleanHtml(html);
            if(this.fireEvent('beforesync', this, html) !== false){
                this.el.dom.value = html;
                this.fireEvent('sync', this, html);
            }
        }
    },

    
    getValue : function() {
        this[this.sourceEditMode ? 'pushValue' : 'syncValue']();
        return Ext.form.HtmlEditor.superclass.getValue.call(this);
    },

    
    pushValue : function(){
        if(this.initialized){
            var v = this.el.dom.value;
            if(!this.activated && v.length < 1){
                v = this.defaultValue;
            }
            if(this.fireEvent('beforepush', this, v) !== false){
                this.getEditorBody().innerHTML = v;
                if(Ext.isGecko){
                    
                    this.setDesignMode(false);  
                    this.setDesignMode(true);
                }
                this.fireEvent('push', this, v);
            }

        }
    },

    
    deferFocus : function(){
        this.focus.defer(10, this);
    },

    
    focus : function(){
        if(this.win && !this.sourceEditMode){
            this.win.focus();
        }else{
            this.el.focus();
        }
    },

    
    initEditor : function(){
        
        try{
            var dbody = this.getEditorBody(),
                ss = this.el.getStyles('font-size', 'font-family', 'background-image', 'background-repeat', 'background-color', 'color'),
                doc,
                fn;

            ss['background-attachment'] = 'fixed'; 
            dbody.bgProperties = 'fixed'; 

            Ext.DomHelper.applyStyles(dbody, ss);

            doc = this.getDoc();

            if(doc){
                try{
                    Ext.EventManager.removeAll(doc);
                }catch(e){}
            }

            
            fn = this.onEditorEvent.createDelegate(this);
            Ext.EventManager.on(doc, {
                mousedown: fn,
                dblclick: fn,
                click: fn,
                keyup: fn,
                buffer:100
            });

            if(Ext.isGecko){
                Ext.EventManager.on(doc, 'keypress', this.applyCommand, this);
            }
            if(Ext.isIE || Ext.isWebKit || Ext.isOpera){
                Ext.EventManager.on(doc, 'keydown', this.fixKeys, this);
            }
            doc.editorInitialized = true;
            this.initialized = true;
            this.pushValue();
            this.setReadOnly(this.readOnly);
            this.fireEvent('initialize', this);
        }catch(e){}
    },

    
    onDestroy : function(){
        if(this.monitorTask){
            Ext.TaskMgr.stop(this.monitorTask);
        }
        if(this.rendered){
            Ext.destroy(this.tb);
            var doc = this.getDoc();
            if(doc){
                try{
                    Ext.EventManager.removeAll(doc);
                    for (var prop in doc){
                        delete doc[prop];
                    }
                }catch(e){}
            }
            if(this.wrap){
                this.wrap.dom.innerHTML = '';
                this.wrap.remove();
            }
        }

        if(this.el){
            this.el.removeAllListeners();
            this.el.remove();
        }
        this.purgeListeners();
    },

    
    onFirstFocus : function(){
        this.activated = true;
        this.disableItems(this.readOnly);
        if(Ext.isGecko){ 
            this.win.focus();
            var s = this.win.getSelection();
            if(!s.focusNode || s.focusNode.nodeType != 3){
                var r = s.getRangeAt(0);
                r.selectNodeContents(this.getEditorBody());
                r.collapse(true);
                this.deferFocus();
            }
            try{
                this.execCmd('useCSS', true);
                this.execCmd('styleWithCSS', false);
            }catch(e){}
        }
        this.fireEvent('activate', this);
    },

    
    adjustFont: function(btn){
        var adjust = btn.getItemId() == 'increasefontsize' ? 1 : -1,
            doc = this.getDoc(),
            v = parseInt(doc.queryCommandValue('FontSize') || 2, 10);
        if((Ext.isSafari && !Ext.isSafari2) || Ext.isChrome || Ext.isAir){
            
            
            if(v <= 10){
                v = 1 + adjust;
            }else if(v <= 13){
                v = 2 + adjust;
            }else if(v <= 16){
                v = 3 + adjust;
            }else if(v <= 18){
                v = 4 + adjust;
            }else if(v <= 24){
                v = 5 + adjust;
            }else {
                v = 6 + adjust;
            }
            v = v.constrain(1, 6);
        }else{
            if(Ext.isSafari){ 
                adjust *= 2;
            }
            v = Math.max(1, v+adjust) + (Ext.isSafari ? 'px' : 0);
        }
        this.execCmd('FontSize', v);
    },

    
    onEditorEvent : function(e){
        this.updateToolbar();
    },


    
    updateToolbar: function(){

        if(this.readOnly){
            return;
        }

        if(!this.activated){
            this.onFirstFocus();
            return;
        }

        var btns = this.tb.items.map,
            doc = this.getDoc();

        if(this.enableFont && !Ext.isSafari2){
            var name = (doc.queryCommandValue('FontName')||this.defaultFont).toLowerCase();
            if(name != this.fontSelect.dom.value){
                this.fontSelect.dom.value = name;
            }
        }
        if(this.enableFormat){
            btns.bold.toggle(doc.queryCommandState('bold'));
            btns.italic.toggle(doc.queryCommandState('italic'));
            btns.underline.toggle(doc.queryCommandState('underline'));
        }
        if(this.enableAlignments){
            btns.justifyleft.toggle(doc.queryCommandState('justifyleft'));
            btns.justifycenter.toggle(doc.queryCommandState('justifycenter'));
            btns.justifyright.toggle(doc.queryCommandState('justifyright'));
        }
        if(!Ext.isSafari2 && this.enableLists){
            btns.insertorderedlist.toggle(doc.queryCommandState('insertorderedlist'));
            btns.insertunorderedlist.toggle(doc.queryCommandState('insertunorderedlist'));
        }

        Ext.menu.MenuMgr.hideAll();

        this.syncValue();
    },

    
    relayBtnCmd : function(btn){
        this.relayCmd(btn.getItemId());
    },

    
    relayCmd : function(cmd, value){
        (function(){
            this.focus();
            this.execCmd(cmd, value);
            this.updateToolbar();
        }).defer(10, this);
    },

    
    execCmd : function(cmd, value){
        var doc = this.getDoc();
        doc.execCommand(cmd, false, value === undefined ? null : value);
        this.syncValue();
    },

    
    applyCommand : function(e){
        if(e.ctrlKey){
            var c = e.getCharCode(), cmd;
            if(c > 0){
                c = String.fromCharCode(c);
                switch(c){
                    case 'b':
                        cmd = 'bold';
                    break;
                    case 'i':
                        cmd = 'italic';
                    break;
                    case 'u':
                        cmd = 'underline';
                    break;
                }
                if(cmd){
                    this.win.focus();
                    this.execCmd(cmd);
                    this.deferFocus();
                    e.preventDefault();
                }
            }
        }
    },

    
    insertAtCursor : function(text){
        if(!this.activated){
            return;
        }
        if(Ext.isIE){
            this.win.focus();
            var doc = this.getDoc(),
                r = doc.selection.createRange();
            if(r){
                r.pasteHTML(text);
                this.syncValue();
                this.deferFocus();
            }
        }else{
            this.win.focus();
            this.execCmd('InsertHTML', text);
            this.deferFocus();
        }
    },

    
    fixKeys : function(){ 
        if(Ext.isIE){
            return function(e){
                var k = e.getKey(),
                    doc = this.getDoc(),
                        r;
                if(k == e.TAB){
                    e.stopEvent();
                    r = doc.selection.createRange();
                    if(r){
                        r.collapse(true);
                        r.pasteHTML('&nbsp;&nbsp;&nbsp;&nbsp;');
                        this.deferFocus();
                    }
                }else if(k == e.ENTER){
                    r = doc.selection.createRange();
                    if(r){
                        var target = r.parentElement();
                        if(!target || target.tagName.toLowerCase() != 'li'){
                            e.stopEvent();
                            r.pasteHTML('<br />');
                            r.collapse(false);
                            r.select();
                        }
                    }
                }
            };
        }else if(Ext.isOpera){
            return function(e){
                var k = e.getKey();
                if(k == e.TAB){
                    e.stopEvent();
                    this.win.focus();
                    this.execCmd('InsertHTML','&nbsp;&nbsp;&nbsp;&nbsp;');
                    this.deferFocus();
                }
            };
        }else if(Ext.isWebKit){
            return function(e){
                var k = e.getKey();
                if(k == e.TAB){
                    e.stopEvent();
                    this.execCmd('InsertText','\t');
                    this.deferFocus();
                }else if(k == e.ENTER){
                    e.stopEvent();
                    this.execCmd('InsertHtml','<br /><br />');
                    this.deferFocus();
                }
             };
        }
    }(),

    
    getToolbar : function(){
        return this.tb;
    },

    
    buttonTips : {
        bold : {
            title: 'Bold (Ctrl+B)',
            text: 'Make the selected text bold.',
            cls: 'x-html-editor-tip'
        },
        italic : {
            title: 'Italic (Ctrl+I)',
            text: 'Make the selected text italic.',
            cls: 'x-html-editor-tip'
        },
        underline : {
            title: 'Underline (Ctrl+U)',
            text: 'Underline the selected text.',
            cls: 'x-html-editor-tip'
        },
        increasefontsize : {
            title: 'Grow Text',
            text: 'Increase the font size.',
            cls: 'x-html-editor-tip'
        },
        decreasefontsize : {
            title: 'Shrink Text',
            text: 'Decrease the font size.',
            cls: 'x-html-editor-tip'
        },
        backcolor : {
            title: 'Text Highlight Color',
            text: 'Change the background color of the selected text.',
            cls: 'x-html-editor-tip'
        },
        forecolor : {
            title: 'Font Color',
            text: 'Change the color of the selected text.',
            cls: 'x-html-editor-tip'
        },
        justifyleft : {
            title: 'Align Text Left',
            text: 'Align text to the left.',
            cls: 'x-html-editor-tip'
        },
        justifycenter : {
            title: 'Center Text',
            text: 'Center text in the editor.',
            cls: 'x-html-editor-tip'
        },
        justifyright : {
            title: 'Align Text Right',
            text: 'Align text to the right.',
            cls: 'x-html-editor-tip'
        },
        insertunorderedlist : {
            title: 'Bullet List',
            text: 'Start a bulleted list.',
            cls: 'x-html-editor-tip'
        },
        insertorderedlist : {
            title: 'Numbered List',
            text: 'Start a numbered list.',
            cls: 'x-html-editor-tip'
        },
        createlink : {
            title: 'Hyperlink',
            text: 'Make the selected text a hyperlink.',
            cls: 'x-html-editor-tip'
        },
        sourceedit : {
            title: 'Source Edit',
            text: 'Switch to source editing mode.',
            cls: 'x-html-editor-tip'
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
});
Ext.reg('htmleditor', Ext.form.HtmlEditor);

Ext.form.TimeField = Ext.extend(Ext.form.ComboBox, {
    
    minValue : undefined,
    
    maxValue : undefined,
    
    minText : "The time in this field must be equal to or after {0}",
    
    maxText : "The time in this field must be equal to or before {0}",
    
    invalidText : "{0} is not a valid time",
    
    format : "g:i A",
    
    altFormats : "g:ia|g:iA|g:i a|g:i A|h:i|g:i|H:i|ga|ha|gA|h a|g a|g A|gi|hi|gia|hia|g|H|gi a|hi a|giA|hiA|gi A|hi A",
    
    increment: 15,

    
    mode: 'local',
    
    triggerAction: 'all',
    
    typeAhead: false,

    
    
    
    initDate: '1/1/2008',

    initDateFormat: 'j/n/Y',

    
    initComponent : function(){
        if(Ext.isDefined(this.minValue)){
            this.setMinValue(this.minValue, true);
        }
        if(Ext.isDefined(this.maxValue)){
            this.setMaxValue(this.maxValue, true);
        }
        if(!this.store){
            this.generateStore(true);
        }
        Ext.form.TimeField.superclass.initComponent.call(this);
    },

    
    setMinValue: function(value,  initial){
        this.setLimit(value, true, initial);
        return this;
    },

    
    setMaxValue: function(value,  initial){
        this.setLimit(value, false, initial);
        return this;
    },

    
    generateStore: function(initial){
        var min = this.minValue || new Date(this.initDate).clearTime(),
            max = this.maxValue || new Date(this.initDate).clearTime().add('mi', (24 * 60) - 1),
            times = [];

        while(min <= max){
            times.push(min.dateFormat(this.format));
            min = min.add('mi', this.increment);
        }
        this.bindStore(times, initial);
    },

    
    setLimit: function(value, isMin, initial){
        var d;
        if(Ext.isString(value)){
            d = this.parseDate(value);
        }else if(Ext.isDate(value)){
            d = value;
        }
        if(d){
            var val = new Date(this.initDate).clearTime();
            val.setHours(d.getHours(), d.getMinutes(), d.getSeconds(), d.getMilliseconds());
            this[isMin ? 'minValue' : 'maxValue'] = val;
            if(!initial){
                this.generateStore();
            }
        }
    },

    
    getValue : function(){
        var v = Ext.form.TimeField.superclass.getValue.call(this);
        return this.formatDate(this.parseDate(v)) || '';
    },

    
    setValue : function(value){
        return Ext.form.TimeField.superclass.setValue.call(this, this.formatDate(this.parseDate(value)));
    },

    
    validateValue : Ext.form.DateField.prototype.validateValue,

    formatDate : Ext.form.DateField.prototype.formatDate,

    parseDate: function(value) {
        if (!value || Ext.isDate(value)) {
            return value;
        }

        var id = this.initDate + ' ',
            idf = this.initDateFormat + ' ',
            v = Date.parseDate(id + value, idf + this.format), 
            af = this.altFormats;

        if (!v && af) {
            if (!this.altFormatsArray) {
                this.altFormatsArray = af.split("|");
            }
            for (var i = 0, afa = this.altFormatsArray, len = afa.length; i < len && !v; i++) {
                v = Date.parseDate(id + value, idf + afa[i]);
            }
        }

        return v;
    }
});
Ext.reg('timefield', Ext.form.TimeField);
Ext.form.SliderField = Ext.extend(Ext.form.Field, {
    
    
    useTips : true,
    
    
    tipText : null,
    
    
    actionMode: 'wrap',
    
    
    initComponent : function() {
        var cfg = Ext.copyTo({
            id: this.id + '-slider'
        }, this.initialConfig, ['vertical', 'minValue', 'maxValue', 'decimalPrecision', 'keyIncrement', 'increment', 'clickToChange', 'animate']);
        
        
        if (this.useTips) {
            var plug = this.tipText ? {getText: this.tipText} : {};
            cfg.plugins = [new Ext.slider.Tip(plug)];
        }
        this.slider = new Ext.Slider(cfg);
        Ext.form.SliderField.superclass.initComponent.call(this);
    },    
    
    
    onRender : function(ct, position){
        this.autoCreate = {
            id: this.id,
            name: this.name,
            type: 'hidden',
            tag: 'input'    
        };
        Ext.form.SliderField.superclass.onRender.call(this, ct, position);
        this.wrap = this.el.wrap({cls: 'x-form-field-wrap'});
        this.resizeEl = this.positionEl = this.wrap;
        this.slider.render(this.wrap);
    },
    
    
    onResize : function(w, h, aw, ah){
        Ext.form.SliderField.superclass.onResize.call(this, w, h, aw, ah);
        this.slider.setSize(w, h);    
    },
    
    
    initEvents : function(){
        Ext.form.SliderField.superclass.initEvents.call(this);
        this.slider.on('change', this.onChange, this);   
    },
    
    
    onChange : function(slider, v){
        this.setValue(v, undefined, true);
    },
    
    
    onEnable : function(){
        Ext.form.SliderField.superclass.onEnable.call(this);
        this.slider.enable();
    },
    
    
    onDisable : function(){
        Ext.form.SliderField.superclass.onDisable.call(this);
        this.slider.disable();    
    },
    
    
    beforeDestroy : function(){
        Ext.destroy(this.slider);
        Ext.form.SliderField.superclass.beforeDestroy.call(this);
    },
    
    
    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.slider.el, 'tl-tr', [2, 0]);
    },
    
    
    setMinValue : function(v){
        this.slider.setMinValue(v);
        return this;    
    },
    
    
    setMaxValue : function(v){
        this.slider.setMaxValue(v);
        return this;    
    },
    
    
    setValue : function(v, animate,  silent){
        
        
        if(!silent){
            this.slider.setValue(v, animate);
        }
        return Ext.form.SliderField.superclass.setValue.call(this, this.slider.getValue());
    },
    
    
    getValue : function(){
        return this.slider.getValue();    
    }
});

Ext.reg('sliderfield', Ext.form.SliderField);
Ext.form.Label = Ext.extend(Ext.BoxComponent, {
    
    
    

    
    onRender : function(ct, position){
        if(!this.el){
            this.el = document.createElement('label');
            this.el.id = this.getId();
            this.el.innerHTML = this.text ? Ext.util.Format.htmlEncode(this.text) : (this.html || '');
            if(this.forId){
                this.el.setAttribute('for', this.forId);
            }
        }
        Ext.form.Label.superclass.onRender.call(this, ct, position);
    },

    
    setText : function(t, encode){
        var e = encode === false;
        this[!e ? 'text' : 'html'] = t;
        delete this[e ? 'text' : 'html'];
        if(this.rendered){
            this.el.dom.innerHTML = encode !== false ? Ext.util.Format.htmlEncode(t) : t;
        }
        return this;
    }
});

Ext.reg('label', Ext.form.Label);
Ext.form.Action = function(form, options){
    this.form = form;
    this.options = options || {};
};


Ext.form.Action.CLIENT_INVALID = 'client';

Ext.form.Action.SERVER_INVALID = 'server';

Ext.form.Action.CONNECT_FAILURE = 'connect';

Ext.form.Action.LOAD_FAILURE = 'load';

Ext.form.Action.prototype = {














    type : 'default',

 
 

    
    run : function(options){

    },

    
    success : function(response){

    },

    
    handleResponse : function(response){

    },

    
    failure : function(response){
        this.response = response;
        this.failureType = Ext.form.Action.CONNECT_FAILURE;
        this.form.afterAction(this, false);
    },

    
    
    
    processResponse : function(response){
        this.response = response;
        if(!response.responseText && !response.responseXML){
            return true;
        }
        this.result = this.handleResponse(response);
        return this.result;
    },

    
    getUrl : function(appendParams){
        var url = this.options.url || this.form.url || this.form.el.dom.action;
        if(appendParams){
            var p = this.getParams();
            if(p){
                url = Ext.urlAppend(url, p);
            }
        }
        return url;
    },

    
    getMethod : function(){
        return (this.options.method || this.form.method || this.form.el.dom.method || 'POST').toUpperCase();
    },

    
    getParams : function(){
        var bp = this.form.baseParams;
        var p = this.options.params;
        if(p){
            if(typeof p == "object"){
                p = Ext.urlEncode(Ext.applyIf(p, bp));
            }else if(typeof p == 'string' && bp){
                p += '&' + Ext.urlEncode(bp);
            }
        }else if(bp){
            p = Ext.urlEncode(bp);
        }
        return p;
    },

    
    createCallback : function(opts){
        var opts = opts || {};
        return {
            success: this.success,
            failure: this.failure,
            scope: this,
            timeout: (opts.timeout*1000) || (this.form.timeout*1000),
            upload: this.form.fileUpload ? this.success : undefined
        };
    }
};


Ext.form.Action.Submit = function(form, options){
    Ext.form.Action.Submit.superclass.constructor.call(this, form, options);
};

Ext.extend(Ext.form.Action.Submit, Ext.form.Action, {
    
    
    type : 'submit',

    
    run : function(){
        var o = this.options,
            method = this.getMethod(),
            isGet = method == 'GET';
        if(o.clientValidation === false || this.form.isValid()){
            if (o.submitEmptyText === false) {
                var fields = this.form.items,
                    emptyFields = [];
                fields.each(function(f) {
                    if (f.el.getValue() == f.emptyText) {
                        emptyFields.push(f);
                        f.el.dom.value = "";
                    }
                });
            }
            Ext.Ajax.request(Ext.apply(this.createCallback(o), {
                form:this.form.el.dom,
                url:this.getUrl(isGet),
                method: method,
                headers: o.headers,
                params:!isGet ? this.getParams() : null,
                isUpload: this.form.fileUpload
            }));
            if (o.submitEmptyText === false) {
                Ext.each(emptyFields, function(f) {
                    if (f.applyEmptyText) {
                        f.applyEmptyText();
                    }
                });
            }
        }else if (o.clientValidation !== false){ 
            this.failureType = Ext.form.Action.CLIENT_INVALID;
            this.form.afterAction(this, false);
        }
    },

    
    success : function(response){
        var result = this.processResponse(response);
        if(result === true || result.success){
            this.form.afterAction(this, true);
            return;
        }
        if(result.errors){
            this.form.markInvalid(result.errors);
        }
        this.failureType = Ext.form.Action.SERVER_INVALID;
        this.form.afterAction(this, false);
    },

    
    handleResponse : function(response){
        if(this.form.errorReader){
            var rs = this.form.errorReader.read(response);
            var errors = [];
            if(rs.records){
                for(var i = 0, len = rs.records.length; i < len; i++) {
                    var r = rs.records[i];
                    errors[i] = r.data;
                }
            }
            if(errors.length < 1){
                errors = null;
            }
            return {
                success : rs.success,
                errors : errors
            };
        }
        return Ext.decode(response.responseText);
    }
});



Ext.form.Action.Load = function(form, options){
    Ext.form.Action.Load.superclass.constructor.call(this, form, options);
    this.reader = this.form.reader;
};

Ext.extend(Ext.form.Action.Load, Ext.form.Action, {
    
    type : 'load',

    
    run : function(){
        Ext.Ajax.request(Ext.apply(
                this.createCallback(this.options), {
                    method:this.getMethod(),
                    url:this.getUrl(false),
                    headers: this.options.headers,
                    params:this.getParams()
        }));
    },

    
    success : function(response){
        var result = this.processResponse(response);
        if(result === true || !result.success || !result.data){
            this.failureType = Ext.form.Action.LOAD_FAILURE;
            this.form.afterAction(this, false);
            return;
        }
        this.form.clearInvalid();
        this.form.setValues(result.data);
        this.form.afterAction(this, true);
    },

    
    handleResponse : function(response){
        if(this.form.reader){
            var rs = this.form.reader.read(response);
            var data = rs.records && rs.records[0] ? rs.records[0].data : null;
            return {
                success : rs.success,
                data : data
            };
        }
        return Ext.decode(response.responseText);
    }
});




Ext.form.Action.DirectLoad = Ext.extend(Ext.form.Action.Load, {
    constructor: function(form, opts) {
        Ext.form.Action.DirectLoad.superclass.constructor.call(this, form, opts);
    },
    type : 'directload',

    run : function(){
        var args = this.getParams();
        args.push(this.success, this);
        this.form.api.load.apply(window, args);
    },

    getParams : function() {
        var buf = [], o = {};
        var bp = this.form.baseParams;
        var p = this.options.params;
        Ext.apply(o, p, bp);
        var paramOrder = this.form.paramOrder;
        if(paramOrder){
            for(var i = 0, len = paramOrder.length; i < len; i++){
                buf.push(o[paramOrder[i]]);
            }
        }else if(this.form.paramsAsHash){
            buf.push(o);
        }
        return buf;
    },
    
    
    
    processResponse : function(result) {
        this.result = result;
        return result;
    },

    success : function(response, trans){
        if(trans.type == Ext.Direct.exceptions.SERVER){
            response = {};
        }
        Ext.form.Action.DirectLoad.superclass.success.call(this, response);
    }
});


Ext.form.Action.DirectSubmit = Ext.extend(Ext.form.Action.Submit, {
    constructor : function(form, opts) {
        Ext.form.Action.DirectSubmit.superclass.constructor.call(this, form, opts);
    },
    type : 'directsubmit',
    
    run : function(){
        var o = this.options;
        if(o.clientValidation === false || this.form.isValid()){
            
            
            this.success.params = this.getParams();
            this.form.api.submit(this.form.el.dom, this.success, this);
        }else if (o.clientValidation !== false){ 
            this.failureType = Ext.form.Action.CLIENT_INVALID;
            this.form.afterAction(this, false);
        }
    },

    getParams : function() {
        var o = {};
        var bp = this.form.baseParams;
        var p = this.options.params;
        Ext.apply(o, p, bp);
        return o;
    },
    
    
    
    processResponse : function(result) {
        this.result = result;
        return result;
    },

    success : function(response, trans){
        if(trans.type == Ext.Direct.exceptions.SERVER){
            response = {};
        }
        Ext.form.Action.DirectSubmit.superclass.success.call(this, response);
    }
});

Ext.form.Action.ACTION_TYPES = {
    'load' : Ext.form.Action.Load,
    'submit' : Ext.form.Action.Submit,
    'directload' : Ext.form.Action.DirectLoad,
    'directsubmit' : Ext.form.Action.DirectSubmit
};

Ext.form.VTypes = function(){
    
    var alpha = /^[a-zA-Z_]+$/,
        alphanum = /^[a-zA-Z0-9_]+$/,
        email = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/,
        url = /(((^https?)|(^ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i;

    
    return {
        
        'email' : function(v){
            return email.test(v);
        },
        
        'emailText' : 'This field should be an e-mail address in the format "user@example.com"',
        
        'emailMask' : /[a-z0-9_\.\-@\+]/i,

        
        'url' : function(v){
            return url.test(v);
        },
        
        'urlText' : 'This field should be a URL in the format "http:/'+'/www.example.com"',

        
        'alpha' : function(v){
            return alpha.test(v);
        },
        
        'alphaText' : 'This field should only contain letters and _',
        
        'alphaMask' : /[a-z_]/i,

        
        'alphanum' : function(v){
            return alphanum.test(v);
        },
        
        'alphanumText' : 'This field should only contain letters, numbers and _',
        
        'alphanumMask' : /[a-z0-9_]/i
    };
}();

Ext.grid.GridPanel = Ext.extend(Ext.Panel, {
    
    autoExpandColumn : false,
    
    autoExpandMax : 1000,
    
    autoExpandMin : 50,
    
    columnLines : false,
    
    
    
    
    
    ddText : '{0} selected row{1}',
    
    deferRowRender : true,
    
    
    
    enableColumnHide : true,
    
    enableColumnMove : true,
    
    enableDragDrop : false,
    
    enableHdMenu : true,
    
    
    loadMask : false,
    
    
    minColumnWidth : 25,
    
    
    
    
    stripeRows : false,
    
    trackMouseOver : true,
    
    stateEvents : ['columnmove', 'columnresize', 'sortchange', 'groupchange'],
    
    view : null,

    
    bubbleEvents: [],

    

    
    rendered : false,
    
    viewReady : false,

    
    initComponent : function(){
        Ext.grid.GridPanel.superclass.initComponent.call(this);

        if(this.columnLines){
            this.cls = (this.cls || '') + ' x-grid-with-col-lines';
        }
        
        
        this.autoScroll = false;
        this.autoWidth = false;

        if(Ext.isArray(this.columns)){
            this.colModel = new Ext.grid.ColumnModel(this.columns);
            delete this.columns;
        }

        
        if(this.ds){
            this.store = this.ds;
            delete this.ds;
        }
        if(this.cm){
            this.colModel = this.cm;
            delete this.cm;
        }
        if(this.sm){
            this.selModel = this.sm;
            delete this.sm;
        }
        this.store = Ext.StoreMgr.lookup(this.store);

        this.addEvents(
            
            
            'click',
            
            'dblclick',
            
            'contextmenu',
            
            'mousedown',
            
            'mouseup',
            
            'mouseover',
            
            'mouseout',
            
            'keypress',
            
            'keydown',

            
            
            'cellmousedown',
            
            'rowmousedown',
            
            'headermousedown',

            
            'groupmousedown',

            
            'rowbodymousedown',

            
            'containermousedown',

            
            'cellclick',
            
            'celldblclick',
            
            'rowclick',
            
            'rowdblclick',
            
            'headerclick',
            
            'headerdblclick',
            
            'groupclick',
            
            'groupdblclick',
            
            'containerclick',
            
            'containerdblclick',

            
            'rowbodyclick',
            
            'rowbodydblclick',

            
            'rowcontextmenu',
            
            'cellcontextmenu',
            
            'headercontextmenu',
            
            'groupcontextmenu',
            
            'containercontextmenu',
            
            'rowbodycontextmenu',
            
            'bodyscroll',
            
            'columnresize',
            
            'columnmove',
            
            'sortchange',
            
            'groupchange',
            
            'reconfigure',
            
            'viewready'
        );
    },

    
    onRender : function(ct, position){
        Ext.grid.GridPanel.superclass.onRender.apply(this, arguments);

        var c = this.getGridEl();

        this.el.addClass('x-grid-panel');

        this.mon(c, {
            scope: this,
            mousedown: this.onMouseDown,
            click: this.onClick,
            dblclick: this.onDblClick,
            contextmenu: this.onContextMenu
        });

        this.relayEvents(c, ['mousedown','mouseup','mouseover','mouseout','keypress', 'keydown']);

        var view = this.getView();
        view.init(this);
        view.render();
        this.getSelectionModel().init(this);
    },

    
    initEvents : function(){
        Ext.grid.GridPanel.superclass.initEvents.call(this);

        if(this.loadMask){
            this.loadMask = new Ext.LoadMask(this.bwrap,
                    Ext.apply({store:this.store}, this.loadMask));
        }
    },

    initStateEvents : function(){
        Ext.grid.GridPanel.superclass.initStateEvents.call(this);
        this.mon(this.colModel, 'hiddenchange', this.saveState, this, {delay: 100});
    },

    applyState : function(state){
        var cm = this.colModel,
            cs = state.columns,
            store = this.store,
            s,
            c,
            oldIndex;

        if(cs){
            for(var i = 0, len = cs.length; i < len; i++){
                s = cs[i];
                c = cm.getColumnById(s.id);
                if(c){
                    c.hidden = s.hidden;
                    c.width = s.width;
                    oldIndex = cm.getIndexById(s.id);
                    if(oldIndex != i){
                        cm.moveColumn(oldIndex, i);
                    }
                }
            }
        }
        if(store){
            s = state.sort;
            if(s){
                store[store.remoteSort ? 'setDefaultSort' : 'sort'](s.field, s.direction);
            }
            s = state.group;
            if(store.groupBy){
                if(s){
                    store.groupBy(s);
                }else{
                    store.clearGrouping();
                }
            }

        }
        var o = Ext.apply({}, state);
        delete o.columns;
        delete o.sort;
        Ext.grid.GridPanel.superclass.applyState.call(this, o);
    },

    getState : function(){
        var o = {columns: []},
            store = this.store,
            ss,
            gs;

        for(var i = 0, c; (c = this.colModel.config[i]); i++){
            o.columns[i] = {
                id: c.id,
                width: c.width
            };
            if(c.hidden){
                o.columns[i].hidden = true;
            }
        }
        if(store){
            ss = store.getSortState();
            if(ss){
                o.sort = ss;
            }
            if(store.getGroupState){
                gs = store.getGroupState();
                if(gs){
                    o.group = gs;
                }
            }
        }
        return o;
    },

    
    afterRender : function(){
        Ext.grid.GridPanel.superclass.afterRender.call(this);
        var v = this.view;
        this.on('bodyresize', v.layout, v);
        v.layout();
        if(this.deferRowRender){
            if (!this.deferRowRenderTask){
                this.deferRowRenderTask = new Ext.util.DelayedTask(v.afterRender, this.view);
            }
            this.deferRowRenderTask.delay(10);
        }else{
            v.afterRender();
        }
        this.viewReady = true;
    },

    
    reconfigure : function(store, colModel){
        var rendered = this.rendered;
        if(rendered){
            if(this.loadMask){
                this.loadMask.destroy();
                this.loadMask = new Ext.LoadMask(this.bwrap,
                        Ext.apply({}, {store:store}, this.initialConfig.loadMask));
            }
        }
        if(this.view){
            this.view.initData(store, colModel);
        }
        this.store = store;
        this.colModel = colModel;
        if(rendered){
            this.view.refresh(true);
        }
        this.fireEvent('reconfigure', this, store, colModel);
    },

    
    onDestroy : function(){
        if (this.deferRowRenderTask && this.deferRowRenderTask.cancel){
            this.deferRowRenderTask.cancel();
        }
        if(this.rendered){
            Ext.destroy(this.view, this.loadMask);
        }else if(this.store && this.store.autoDestroy){
            this.store.destroy();
        }
        Ext.destroy(this.colModel, this.selModel);
        this.store = this.selModel = this.colModel = this.view = this.loadMask = null;
        Ext.grid.GridPanel.superclass.onDestroy.call(this);
    },

    
    processEvent : function(name, e){
        this.view.processEvent(name, e);
    },

    
    onClick : function(e){
        this.processEvent('click', e);
    },

    
    onMouseDown : function(e){
        this.processEvent('mousedown', e);
    },

    
    onContextMenu : function(e, t){
        this.processEvent('contextmenu', e);
    },

    
    onDblClick : function(e){
        this.processEvent('dblclick', e);
    },

    
    walkCells : function(row, col, step, fn, scope){
        var cm    = this.colModel,
            clen  = cm.getColumnCount(),
            ds    = this.store,
            rlen  = ds.getCount(),
            first = true;

        if(step < 0){
            if(col < 0){
                row--;
                first = false;
            }
            while(row >= 0){
                if(!first){
                    col = clen-1;
                }
                first = false;
                while(col >= 0){
                    if(fn.call(scope || this, row, col, cm) === true){
                        return [row, col];
                    }
                    col--;
                }
                row--;
            }
        } else {
            if(col >= clen){
                row++;
                first = false;
            }
            while(row < rlen){
                if(!first){
                    col = 0;
                }
                first = false;
                while(col < clen){
                    if(fn.call(scope || this, row, col, cm) === true){
                        return [row, col];
                    }
                    col++;
                }
                row++;
            }
        }
        return null;
    },

    
    getGridEl : function(){
        return this.body;
    },

    
    stopEditing : Ext.emptyFn,

    
    getSelectionModel : function(){
        if(!this.selModel){
            this.selModel = new Ext.grid.RowSelectionModel(
                    this.disableSelection ? {selectRow: Ext.emptyFn} : null);
        }
        return this.selModel;
    },

    
    getStore : function(){
        return this.store;
    },

    
    getColumnModel : function(){
        return this.colModel;
    },

    
    getView : function(){
        if(!this.view){
            this.view = new Ext.grid.GridView(this.viewConfig);
        }
        return this.view;
    },
    
    getDragDropText : function(){
        var count = this.selModel.getCount();
        return String.format(this.ddText, count, count == 1 ? '' : 's');
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    



    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
});
Ext.reg('grid', Ext.grid.GridPanel);
Ext.grid.GridView = Ext.extend(Ext.util.Observable, {
    

    

    

    

    

    
    deferEmptyText : true,

    
    scrollOffset : undefined,

    
    autoFill : false,

    
    forceFit : false,

    
    sortClasses : ['sort-asc', 'sort-desc'],

    
    sortAscText : 'Sort Ascending',

    
    sortDescText : 'Sort Descending',

    
    columnsText : 'Columns',

    
    selectedRowClass : 'x-grid3-row-selected',

    
    borderWidth : 2,
    tdClass : 'x-grid3-cell',
    hdCls : 'x-grid3-hd',
    markDirty : true,

    
    cellSelectorDepth : 4,
    
    rowSelectorDepth : 10,

    
    rowBodySelectorDepth : 10,

    
    cellSelector : 'td.x-grid3-cell',
    
    rowSelector : 'div.x-grid3-row',

    
    rowBodySelector : 'div.x-grid3-row-body',

    
    firstRowCls: 'x-grid3-row-first',
    lastRowCls: 'x-grid3-row-last',
    rowClsRe: /(?:^|\s+)x-grid3-row-(first|last|alt)(?:\s+|$)/g,

    constructor : function(config){
        Ext.apply(this, config);
        
        this.addEvents(
            
            'beforerowremoved',
            
            'beforerowsinserted',
            
            'beforerefresh',
            
            'rowremoved',
            
            'rowsinserted',
            
            'rowupdated',
            
            'refresh'
        );
        Ext.grid.GridView.superclass.constructor.call(this);
    },

    

    
    initTemplates : function(){
        var ts = this.templates || {};
        if(!ts.master){
            ts.master = new Ext.Template(
                '<div class="x-grid3" hidefocus="true">',
                    '<div class="x-grid3-viewport">',
                        '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{ostyle}">{header}</div></div><div class="x-clear"></div></div>',
                        '<div class="x-grid3-scroller"><div class="x-grid3-body" style="{bstyle}">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
                    '</div>',
                    '<div class="x-grid3-resize-marker">&#160;</div>',
                    '<div class="x-grid3-resize-proxy">&#160;</div>',
                '</div>'
            );
        }

        if(!ts.header){
            ts.header = new Ext.Template(
                '<table border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                '<thead><tr class="x-grid3-hd-row">{cells}</tr></thead>',
                '</table>'
            );
        }

        if(!ts.hcell){
            ts.hcell = new Ext.Template(
                '<td class="x-grid3-hd x-grid3-cell x-grid3-td-{id} {css}" style="{style}"><div {tooltip} {attr} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">', this.grid.enableHdMenu ? '<a class="x-grid3-hd-btn" href="#"></a>' : '',
                '{value}<img class="x-grid3-sort-icon" src="', Ext.BLANK_IMAGE_URL, '" />',
                '</div></td>'
            );
        }

        if(!ts.body){
            ts.body = new Ext.Template('{rows}');
        }

        if(!ts.row){
            ts.row = new Ext.Template(
                '<div class="x-grid3-row {alt}" style="{tstyle}"><table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                '<tbody><tr>{cells}</tr>',
                (this.enableRowBody ? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>' : ''),
                '</tbody></table></div>'
            );
        }

        if(!ts.cell){
            ts.cell = new Ext.Template(
                    '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}" tabIndex="0" {cellAttr}>',
                    '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',
                    '</td>'
                    );
        }

        for(var k in ts){
            var t = ts[k];
            if(t && Ext.isFunction(t.compile) && !t.compiled){
                t.disableFormats = true;
                t.compile();
            }
        }

        this.templates = ts;
        this.colRe = new RegExp('x-grid3-td-([^\\s]+)', '');
    },

    
    fly : function(el){
        if(!this._flyweight){
            this._flyweight = new Ext.Element.Flyweight(document.body);
        }
        this._flyweight.dom = el;
        return this._flyweight;
    },

    
    getEditorParent : function(){
        return this.scroller.dom;
    },

    
    initElements : function(){
        var E = Ext.Element;

        var el = this.grid.getGridEl().dom.firstChild;
        var cs = el.childNodes;

        this.el = new E(el);

        this.mainWrap = new E(cs[0]);
        this.mainHd = new E(this.mainWrap.dom.firstChild);

        if(this.grid.hideHeaders){
            this.mainHd.setDisplayed(false);
        }

        this.innerHd = this.mainHd.dom.firstChild;
        this.scroller = new E(this.mainWrap.dom.childNodes[1]);
        if(this.forceFit){
            this.scroller.setStyle('overflow-x', 'hidden');
        }
        
        this.mainBody = new E(this.scroller.dom.firstChild);

        this.focusEl = new E(this.scroller.dom.childNodes[1]);
        this.focusEl.swallowEvent('click', true);

        this.resizeMarker = new E(cs[1]);
        this.resizeProxy = new E(cs[2]);
    },

    
    getRows : function(){
        return this.hasRows() ? this.mainBody.dom.childNodes : [];
    },

    

    
    findCell : function(el){
        if(!el){
            return false;
        }
        return this.fly(el).findParent(this.cellSelector, this.cellSelectorDepth);
    },

    
    findCellIndex : function(el, requiredCls){
        var cell = this.findCell(el);
        if(cell && (!requiredCls || this.fly(cell).hasClass(requiredCls))){
            return this.getCellIndex(cell);
        }
        return false;
    },

    
    getCellIndex : function(el){
        if(el){
            var m = el.className.match(this.colRe);
            if(m && m[1]){
                return this.cm.getIndexById(m[1]);
            }
        }
        return false;
    },

    
    findHeaderCell : function(el){
        var cell = this.findCell(el);
        return cell && this.fly(cell).hasClass(this.hdCls) ? cell : null;
    },

    
    findHeaderIndex : function(el){
        return this.findCellIndex(el, this.hdCls);
    },

    
    findRow : function(el){
        if(!el){
            return false;
        }
        return this.fly(el).findParent(this.rowSelector, this.rowSelectorDepth);
    },

    
    findRowIndex : function(el){
        var r = this.findRow(el);
        return r ? r.rowIndex : false;
    },

    
    findRowBody : function(el){
        if(!el){
            return false;
        }
        return this.fly(el).findParent(this.rowBodySelector, this.rowBodySelectorDepth);
    },

    

    
    getRow : function(row){
        return this.getRows()[row];
    },

    
    getCell : function(row, col){
        return this.getRow(row).getElementsByTagName('td')[col];
    },

    
    getHeaderCell : function(index){
        return this.mainHd.dom.getElementsByTagName('td')[index];
    },

    

    
    addRowClass : function(row, cls){
        var r = this.getRow(row);
        if(r){
            this.fly(r).addClass(cls);
        }
    },

    
    removeRowClass : function(row, cls){
        var r = this.getRow(row);
        if(r){
            this.fly(r).removeClass(cls);
        }
    },

    
    removeRow : function(row){
        Ext.removeNode(this.getRow(row));
        this.syncFocusEl(row);
    },

    
    removeRows : function(firstRow, lastRow){
        var bd = this.mainBody.dom;
        for(var rowIndex = firstRow; rowIndex <= lastRow; rowIndex++){
            Ext.removeNode(bd.childNodes[firstRow]);
        }
        this.syncFocusEl(firstRow);
    },

    

    
    getScrollState : function(){
        var sb = this.scroller.dom;
        return {left: sb.scrollLeft, top: sb.scrollTop};
    },

    
    restoreScroll : function(state){
        var sb = this.scroller.dom;
        sb.scrollLeft = state.left;
        sb.scrollTop = state.top;
    },

    
    scrollToTop : function(){
        this.scroller.dom.scrollTop = 0;
        this.scroller.dom.scrollLeft = 0;
    },

    
    syncScroll : function(){
        this.syncHeaderScroll();
        var mb = this.scroller.dom;
        this.grid.fireEvent('bodyscroll', mb.scrollLeft, mb.scrollTop);
    },

    
    syncHeaderScroll : function(){
        var mb = this.scroller.dom;
        this.innerHd.scrollLeft = mb.scrollLeft;
        this.innerHd.scrollLeft = mb.scrollLeft; 
    },

    
    updateSortIcon : function(col, dir){
        var sc = this.sortClasses;
        var hds = this.mainHd.select('td').removeClass(sc);
        hds.item(col).addClass(sc[dir == 'DESC' ? 1 : 0]);
    },

    
    updateAllColumnWidths : function(){
        var tw   = this.getTotalWidth(),
            clen = this.cm.getColumnCount(),
            ws   = [],
            len,
            i;

        for(i = 0; i < clen; i++){
            ws[i] = this.getColumnWidth(i);
        }

        this.innerHd.firstChild.style.width = this.getOffsetWidth();
        this.innerHd.firstChild.firstChild.style.width = tw;
        this.mainBody.dom.style.width = tw;

        for(i = 0; i < clen; i++){
            var hd = this.getHeaderCell(i);
            hd.style.width = ws[i];
        }

        var ns = this.getRows(), row, trow;
        for(i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = tw;
            if(row.firstChild){
                row.firstChild.style.width = tw;
                trow = row.firstChild.rows[0];
                for (var j = 0; j < clen; j++) {
                   trow.childNodes[j].style.width = ws[j];
                }
            }
        }

        this.onAllColumnWidthsUpdated(ws, tw);
    },

    
    updateColumnWidth : function(col, width){
        var w = this.getColumnWidth(col);
        var tw = this.getTotalWidth();
        this.innerHd.firstChild.style.width = this.getOffsetWidth();
        this.innerHd.firstChild.firstChild.style.width = tw;
        this.mainBody.dom.style.width = tw;
        var hd = this.getHeaderCell(col);
        hd.style.width = w;

        var ns = this.getRows(), row;
        for(var i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = tw;
            if(row.firstChild){
                row.firstChild.style.width = tw;
                row.firstChild.rows[0].childNodes[col].style.width = w;
            }
        }

        this.onColumnWidthUpdated(col, w, tw);
    },

    
    updateColumnHidden : function(col, hidden){
        var tw = this.getTotalWidth();
        this.innerHd.firstChild.style.width = this.getOffsetWidth();
        this.innerHd.firstChild.firstChild.style.width = tw;
        this.mainBody.dom.style.width = tw;
        var display = hidden ? 'none' : '';

        var hd = this.getHeaderCell(col);
        hd.style.display = display;

        var ns = this.getRows(), row;
        for(var i = 0, len = ns.length; i < len; i++){
            row = ns[i];
            row.style.width = tw;
            if(row.firstChild){
                row.firstChild.style.width = tw;
                row.firstChild.rows[0].childNodes[col].style.display = display;
            }
        }

        this.onColumnHiddenUpdated(col, hidden, tw);
        delete this.lastViewWidth; 
        this.layout();
    },

    
    doRender : function(columns, records, store, startRow, colCount, stripe) {
        var templates    = this.templates,
            cellTemplate = templates.cell,
            rowTemplate  = templates.row,
            last         = colCount - 1;

        var tstyle = 'width:' + this.getTotalWidth() + ';';

        
        var rowBuffer = [],
            colBuffer = [],
            rowParams = {tstyle: tstyle},
            meta      = {},
            column,
            record;

        
        for (var j = 0, len = records.length; j < len; j++) {
            record    = records[j];
            colBuffer = [];

            var rowIndex = j + startRow;

            
            for (var i = 0; i < colCount; i++) {
                column = columns[i];

                meta.id    = column.id;
                meta.css   = i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                meta.attr  = meta.cellAttr = '';
                meta.style = column.style;
                meta.value = column.renderer.call(column.scope, record.data[column.name], meta, record, rowIndex, i, store);

                if (Ext.isEmpty(meta.value)) {
                    meta.value = '&#160;';
                }

                if (this.markDirty && record.dirty && Ext.isDefined(record.modified[column.name])) {
                    meta.css += ' x-grid3-dirty-cell';
                }

                colBuffer[colBuffer.length] = cellTemplate.apply(meta);
            }

            
            var alt = [];

            if (stripe && ((rowIndex + 1) % 2 === 0)) {
                alt[0] = 'x-grid3-row-alt';
            }

            if (record.dirty) {
                alt[1] = ' x-grid3-dirty-row';
            }

            rowParams.cols = colCount;

            if (this.getRowClass) {
                alt[2] = this.getRowClass(record, rowIndex, rowParams, store);
            }

            rowParams.alt   = alt.join(' ');
            rowParams.cells = colBuffer.join('');

            rowBuffer[rowBuffer.length] = rowTemplate.apply(rowParams);
        }

        return rowBuffer.join('');
    },

    
    processRows : function(startRow, skipStripe) {
        if (!this.ds || this.ds.getCount() < 1) {
            return;
        }

        var rows = this.getRows(),
            len  = rows.length,
            i, r;

        skipStripe = skipStripe || !this.grid.stripeRows;
        startRow   = startRow   || 0;

        for (i = 0; i<len; i++) {
            r = rows[i];
            if (r) {
                r.rowIndex = i;
                if (!skipStripe) {
                    r.className = r.className.replace(this.rowClsRe, ' ');
                    if ((i + 1) % 2 === 0){
                        r.className += ' x-grid3-row-alt';
                    }
                }
            }
        }

        
        if (startRow === 0) {
            Ext.fly(rows[0]).addClass(this.firstRowCls);
        }

        Ext.fly(rows[rows.length - 1]).addClass(this.lastRowCls);
    },

    afterRender : function(){
        if(!this.ds || !this.cm){
            return;
        }
        this.mainBody.dom.innerHTML = this.renderRows() || '&#160;';
        this.processRows(0, true);

        if(this.deferEmptyText !== true){
            this.applyEmptyText();
        }
        this.grid.fireEvent('viewready', this.grid);
    },

    
    renderUI : function() {
        var templates = this.templates,
            header    = this.renderHeaders(),
            body      = templates.body.apply({rows:'&#160;'});

        var html = templates.master.apply({
            body  : body,
            header: header,
            ostyle: 'width:' + this.getOffsetWidth() + ';',
            bstyle: 'width:' + this.getTotalWidth()  + ';'
        });

        var g = this.grid;

        g.getGridEl().dom.innerHTML = html;

        this.initElements();

        
        Ext.fly(this.innerHd).on('click', this.handleHdDown, this);

        this.mainHd.on({
            scope    : this,
            mouseover: this.handleHdOver,
            mouseout : this.handleHdOut,
            mousemove: this.handleHdMove
        });

        this.scroller.on('scroll', this.syncScroll,  this);
        if (g.enableColumnResize !== false) {
            this.splitZone = new Ext.grid.GridView.SplitDragZone(g, this.mainHd.dom);
        }

        if (g.enableColumnMove) {
            this.columnDrag = new Ext.grid.GridView.ColumnDragZone(g, this.innerHd);
            this.columnDrop = new Ext.grid.HeaderDropZone(g, this.mainHd.dom);
        }

        if (g.enableHdMenu !== false) {
            this.hmenu = new Ext.menu.Menu({id: g.id + '-hctx'});
            this.hmenu.add(
                {itemId:'asc',  text: this.sortAscText,  cls: 'xg-hmenu-sort-asc'},
                {itemId:'desc', text: this.sortDescText, cls: 'xg-hmenu-sort-desc'}
            );

            if (g.enableColumnHide !== false) {
                this.colMenu = new Ext.menu.Menu({id:g.id + '-hcols-menu'});
                this.colMenu.on({
                    scope     : this,
                    beforeshow: this.beforeColMenuShow,
                    itemclick : this.handleHdMenuClick
                });
                this.hmenu.add('-', {
                    itemId:'columns',
                    hideOnClick: false,
                    text: this.columnsText,
                    menu: this.colMenu,
                    iconCls: 'x-cols-icon'
                });
            }

            this.hmenu.on('itemclick', this.handleHdMenuClick, this);
        }

        if (g.trackMouseOver) {
            this.mainBody.on({
                scope    : this,
                mouseover: this.onRowOver,
                mouseout : this.onRowOut
            });
        }

        if (g.enableDragDrop || g.enableDrag) {
            this.dragZone = new Ext.grid.GridDragZone(g, {
                ddGroup : g.ddGroup || 'GridDD'
            });
        }

        this.updateHeaderSortState();
    },

    
    processEvent : function(name, e) {
        var t = e.getTarget(),
            g = this.grid,
            header = this.findHeaderIndex(t);
        g.fireEvent(name, e);
        if (header !== false) {
            g.fireEvent('header' + name, g, header, e);
        } else {
            var row = this.findRowIndex(t),
                cell,
                body;
            if (row !== false) {
                g.fireEvent('row' + name, g, row, e);
                cell = this.findCellIndex(t);
                if (cell !== false) {
                    g.fireEvent('cell' + name, g, row, cell, e);
                } else {
                    body = this.findRowBody(t);
                    if (body) {
                        g.fireEvent('rowbody' + name, g, row, e);
                    }
                }
            } else {
                g.fireEvent('container' + name, g, e);
            }
        }
    },

    
    layout : function() {
        if(!this.mainBody){
            return; 
        }
        var g = this.grid;
        var c = g.getGridEl();
        var csize = c.getSize(true);
        var vw = csize.width;

        if(!g.hideHeaders && (vw < 20 || csize.height < 20)){ 
            return;
        }

        if(g.autoHeight){
            this.scroller.dom.style.overflow = 'visible';
            if(Ext.isWebKit){
                this.scroller.dom.style.position = 'static';
            }
        }else{
            this.el.setSize(csize.width, csize.height);

            var hdHeight = this.mainHd.getHeight();
            var vh = csize.height - (hdHeight);

            this.scroller.setSize(vw, vh);
            if(this.innerHd){
                this.innerHd.style.width = (vw)+'px';
            }
        }
        if(this.forceFit){
            if(this.lastViewWidth != vw){
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        }else {
            this.autoExpand();
            this.syncHeaderScroll();
        }
        this.onLayout(vw, vh);
    },

    
    
    onLayout : function(vw, vh){
        
    },

    onColumnWidthUpdated : function(col, w, tw){
        
    },

    onAllColumnWidthsUpdated : function(ws, tw){
        
    },

    onColumnHiddenUpdated : function(col, hidden, tw){
        
    },

    updateColumnText : function(col, text){
        
    },

    afterMove : function(colIndex){
        
    },

    
    
    init : function(grid){
        this.grid = grid;

        this.initTemplates();
        this.initData(grid.store, grid.colModel);
        this.initUI(grid);
    },

    
    getColumnId : function(index){
      return this.cm.getColumnId(index);
    },

    
    getOffsetWidth : function() {
        return (this.cm.getTotalWidth() + this.getScrollOffset()) + 'px';
    },

    getScrollOffset: function(){
        return Ext.num(this.scrollOffset, Ext.getScrollBarWidth());
    },

    
    renderHeaders : function() {
        var cm   = this.cm,
            ts   = this.templates,
            ct   = ts.hcell,
            cb   = [],
            p    = {},
            len  = cm.getColumnCount(),
            last = len - 1;

        for (var i = 0; i < len; i++) {
            p.id = cm.getColumnId(i);
            p.value = cm.getColumnHeader(i) || '';
            p.style = this.getColumnStyle(i, true);
            p.tooltip = this.getColumnTooltip(i);
            p.css = i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');

            if (cm.config[i].align == 'right') {
                p.istyle = 'padding-right:16px';
            } else {
                delete p.istyle;
            }
            cb[cb.length] = ct.apply(p);
        }
        return ts.header.apply({cells: cb.join(''), tstyle:'width:'+this.getTotalWidth()+';'});
    },

    
    getColumnTooltip : function(i){
        var tt = this.cm.getColumnTooltip(i);
        if(tt){
            if(Ext.QuickTips.isEnabled()){
                return 'ext:qtip="'+tt+'"';
            }else{
                return 'title="'+tt+'"';
            }
        }
        return '';
    },

    
    beforeUpdate : function(){
        this.grid.stopEditing(true);
    },

    
    updateHeaders : function(){
        this.innerHd.firstChild.innerHTML = this.renderHeaders();
        this.innerHd.firstChild.style.width = this.getOffsetWidth();
        this.innerHd.firstChild.firstChild.style.width = this.getTotalWidth();
    },

    
    focusRow : function(row){
        this.focusCell(row, 0, false);
    },

    
    focusCell : function(row, col, hscroll){
        this.syncFocusEl(this.ensureVisible(row, col, hscroll));
        if(Ext.isGecko){
            this.focusEl.focus();
        }else{
            this.focusEl.focus.defer(1, this.focusEl);
        }
    },

    resolveCell : function(row, col, hscroll){
        if(!Ext.isNumber(row)){
            row = row.rowIndex;
        }
        if(!this.ds){
            return null;
        }
        if(row < 0 || row >= this.ds.getCount()){
            return null;
        }
        col = (col !== undefined ? col : 0);

        var rowEl = this.getRow(row),
            cm = this.cm,
            colCount = cm.getColumnCount(),
            cellEl;
        if(!(hscroll === false && col === 0)){
            while(col < colCount && cm.isHidden(col)){
                col++;
            }
            cellEl = this.getCell(row, col);
        }

        return {row: rowEl, cell: cellEl};
    },

    getResolvedXY : function(resolved){
        if(!resolved){
            return null;
        }
        var s = this.scroller.dom, c = resolved.cell, r = resolved.row;
        return c ? Ext.fly(c).getXY() : [this.el.getX(), Ext.fly(r).getY()];
    },

    syncFocusEl : function(row, col, hscroll){
        var xy = row;
        if(!Ext.isArray(xy)){
            row = Math.min(row, Math.max(0, this.getRows().length-1));
            if (isNaN(row)) {
                return;
            }
            xy = this.getResolvedXY(this.resolveCell(row, col, hscroll));
        }
        this.focusEl.setXY(xy||this.scroller.getXY());
    },

    ensureVisible : function(row, col, hscroll){
        var resolved = this.resolveCell(row, col, hscroll);
        if(!resolved || !resolved.row){
            return;
        }

        var rowEl = resolved.row,
            cellEl = resolved.cell,
            c = this.scroller.dom,
            ctop = 0,
            p = rowEl,
            stop = this.el.dom;

        while(p && p != stop){
            ctop += p.offsetTop;
            p = p.offsetParent;
        }

        ctop -= this.mainHd.dom.offsetHeight;
        stop = parseInt(c.scrollTop, 10);

        var cbot = ctop + rowEl.offsetHeight,
            ch = c.clientHeight,
            sbot = stop + ch;


        if(ctop < stop){
          c.scrollTop = ctop;
        }else if(cbot > sbot){
            c.scrollTop = cbot-ch;
        }

        if(hscroll !== false){
            var cleft = parseInt(cellEl.offsetLeft, 10);
            var cright = cleft + cellEl.offsetWidth;

            var sleft = parseInt(c.scrollLeft, 10);
            var sright = sleft + c.clientWidth;
            if(cleft < sleft){
                c.scrollLeft = cleft;
            }else if(cright > sright){
                c.scrollLeft = cright-c.clientWidth;
            }
        }
        return this.getResolvedXY(resolved);
    },

    
    insertRows : function(dm, firstRow, lastRow, isUpdate) {
        var last = dm.getCount() - 1;
        if( !isUpdate && firstRow === 0 && lastRow >= last) {
            this.fireEvent('beforerowsinserted', this, firstRow, lastRow);
                this.refresh();
            this.fireEvent('rowsinserted', this, firstRow, lastRow);
        } else {
            if (!isUpdate) {
                this.fireEvent('beforerowsinserted', this, firstRow, lastRow);
            }
            var html = this.renderRows(firstRow, lastRow),
                before = this.getRow(firstRow);
            if (before) {
                if(firstRow === 0){
                    Ext.fly(this.getRow(0)).removeClass(this.firstRowCls);
                }
                Ext.DomHelper.insertHtml('beforeBegin', before, html);
            } else {
                var r = this.getRow(last - 1);
                if(r){
                    Ext.fly(r).removeClass(this.lastRowCls);
                }
                Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
            }
            if (!isUpdate) {
                this.fireEvent('rowsinserted', this, firstRow, lastRow);
                this.processRows(firstRow);
            } else if (firstRow === 0 || firstRow >= last) {
                
                Ext.fly(this.getRow(firstRow)).addClass(firstRow === 0 ? this.firstRowCls : this.lastRowCls);
            }
        }
        this.syncFocusEl(firstRow);
    },

    
    deleteRows : function(dm, firstRow, lastRow){
        if(dm.getRowCount()<1){
            this.refresh();
        }else{
            this.fireEvent('beforerowsdeleted', this, firstRow, lastRow);

            this.removeRows(firstRow, lastRow);

            this.processRows(firstRow);
            this.fireEvent('rowsdeleted', this, firstRow, lastRow);
        }
    },

    
    getColumnStyle : function(col, isHeader){
        var style = !isHeader ? (this.cm.config[col].css || '') : '';
        style += 'width:'+this.getColumnWidth(col)+';';
        if(this.cm.isHidden(col)){
            style += 'display:none;';
        }
        var align = this.cm.config[col].align;
        if(align){
            style += 'text-align:'+align+';';
        }
        return style;
    },

    
    getColumnWidth : function(col){
        var w = this.cm.getColumnWidth(col);
        if(Ext.isNumber(w)){
            return (Ext.isBorderBox || (Ext.isWebKit && !Ext.isSafari2) ? w : (w - this.borderWidth > 0 ? w - this.borderWidth : 0)) + 'px';
        }
        return w;
    },

    
    getTotalWidth : function(){
        return this.cm.getTotalWidth()+'px';
    },

    
    fitColumns : function(preventRefresh, onlyExpand, omitColumn){
        var cm = this.cm, i;
        var tw = cm.getTotalWidth(false);
        var aw = this.grid.getGridEl().getWidth(true)-this.getScrollOffset();

        if(aw < 20){ 
            return;
        }
        var extra = aw - tw;

        if(extra === 0){
            return false;
        }

        var vc = cm.getColumnCount(true);
        var ac = vc-(Ext.isNumber(omitColumn) ? 1 : 0);
        if(ac === 0){
            ac = 1;
            omitColumn = undefined;
        }
        var colCount = cm.getColumnCount();
        var cols = [];
        var extraCol = 0;
        var width = 0;
        var w;
        for (i = 0; i < colCount; i++){
            if(!cm.isHidden(i) && !cm.isFixed(i) && i !== omitColumn){
                w = cm.getColumnWidth(i);
                cols.push(i);
                extraCol = i;
                cols.push(w);
                width += w;
            }
        }
        var frac = (aw - cm.getTotalWidth())/width;
        while (cols.length){
            w = cols.pop();
            i = cols.pop();
            cm.setColumnWidth(i, Math.max(this.grid.minColumnWidth, Math.floor(w + w*frac)), true);
        }

        if((tw = cm.getTotalWidth(false)) > aw){
            var adjustCol = ac != vc ? omitColumn : extraCol;
             cm.setColumnWidth(adjustCol, Math.max(1,
                     cm.getColumnWidth(adjustCol)- (tw-aw)), true);
        }

        if(preventRefresh !== true){
            this.updateAllColumnWidths();
        }


        return true;
    },

    
    autoExpand : function(preventUpdate){
        var g = this.grid, cm = this.cm;
        if(!this.userResized && g.autoExpandColumn){
            var tw = cm.getTotalWidth(false);
            var aw = this.grid.getGridEl().getWidth(true)-this.getScrollOffset();
            if(tw != aw){
                var ci = cm.getIndexById(g.autoExpandColumn);
                var currentWidth = cm.getColumnWidth(ci);
                var cw = Math.min(Math.max(((aw-tw)+currentWidth), g.autoExpandMin), g.autoExpandMax);
                if(cw != currentWidth){
                    cm.setColumnWidth(ci, cw, true);
                    if(preventUpdate !== true){
                        this.updateColumnWidth(ci, cw);
                    }
                }
            }
        }
    },

    
    getColumnData : function(){
        
        var cs       = [],
            cm       = this.cm,
            colCount = cm.getColumnCount();

        for (var i = 0; i < colCount; i++) {
            var name = cm.getDataIndex(i);

            cs[i] = {
                name    : (!Ext.isDefined(name) ? this.ds.fields.get(i).name : name),
                renderer: cm.getRenderer(i),
                scope   : cm.getRendererScope(i),
                id      : cm.getColumnId(i),
                style   : this.getColumnStyle(i)
            };
        }

        return cs;
    },

    
    renderRows : function(startRow, endRow){
        
        var g = this.grid, cm = g.colModel, ds = g.store, stripe = g.stripeRows;
        var colCount = cm.getColumnCount();

        if(ds.getCount() < 1){
            return '';
        }

        var cs = this.getColumnData();

        startRow = startRow || 0;
        endRow = !Ext.isDefined(endRow) ? ds.getCount()-1 : endRow;

        
        var rs = ds.getRange(startRow, endRow);

        return this.doRender(cs, rs, ds, startRow, colCount, stripe);
    },

    
    renderBody : function(){
        var markup = this.renderRows() || '&#160;';
        return this.templates.body.apply({rows: markup});
    },

    
    refreshRow : function(record){
        var ds = this.ds, index;
        if(Ext.isNumber(record)){
            index = record;
            record = ds.getAt(index);
            if(!record){
                return;
            }
        }else{
            index = ds.indexOf(record);
            if(index < 0){
                return;
            }
        }
        this.insertRows(ds, index, index, true);
        this.getRow(index).rowIndex = index;
        this.onRemove(ds, record, index+1, true);
        this.fireEvent('rowupdated', this, index, record);
    },

    
    refresh : function(headersToo){
        this.fireEvent('beforerefresh', this);
        this.grid.stopEditing(true);

        var result = this.renderBody();
        this.mainBody.update(result).setWidth(this.getTotalWidth());
        if(headersToo === true){
            this.updateHeaders();
            this.updateHeaderSortState();
        }
        this.processRows(0, true);
        this.layout();
        this.applyEmptyText();
        this.fireEvent('refresh', this);
    },

    
    applyEmptyText : function(){
        if(this.emptyText && !this.hasRows()){
            this.mainBody.update('<div class="x-grid-empty">' + this.emptyText + '</div>');
        }
    },

    
    updateHeaderSortState : function(){
        var state = this.ds.getSortState();
        if (!state) {
            return;
        }

        if (!this.sortState || (this.sortState.field != state.field || this.sortState.direction != state.direction)) {
            this.grid.fireEvent('sortchange', this.grid, state);
        }

        this.sortState = state;

        var sortColumn = this.cm.findColumnIndex(state.field);
        if (sortColumn != -1){
            var sortDir = state.direction;
            this.updateSortIcon(sortColumn, sortDir);
        }
    },

    
    clearHeaderSortState : function(){
        if (!this.sortState) {
            return;
        }
        this.grid.fireEvent('sortchange', this.grid, null);
        this.mainHd.select('td').removeClass(this.sortClasses);
        delete this.sortState;
    },

    
    destroy : function(){
        if (this.scrollToTopTask && this.scrollToTopTask.cancel){
            this.scrollToTopTask.cancel();
        }
        if(this.colMenu){
            Ext.menu.MenuMgr.unregister(this.colMenu);
            this.colMenu.destroy();
            delete this.colMenu;
        }
        if(this.hmenu){
            Ext.menu.MenuMgr.unregister(this.hmenu);
            this.hmenu.destroy();
            delete this.hmenu;
        }

        this.initData(null, null);
        this.purgeListeners();
        Ext.fly(this.innerHd).un("click", this.handleHdDown, this);

        if(this.grid.enableColumnMove){
            Ext.destroy(
                this.columnDrag.el,
                this.columnDrag.proxy.ghost,
                this.columnDrag.proxy.el,
                this.columnDrop.el,
                this.columnDrop.proxyTop,
                this.columnDrop.proxyBottom,
                this.columnDrag.dragData.ddel,
                this.columnDrag.dragData.header
            );
            if (this.columnDrag.proxy.anim) {
                Ext.destroy(this.columnDrag.proxy.anim);
            }
            delete this.columnDrag.proxy.ghost;
            delete this.columnDrag.dragData.ddel;
            delete this.columnDrag.dragData.header;
            this.columnDrag.destroy();
            delete Ext.dd.DDM.locationCache[this.columnDrag.id];
            delete this.columnDrag._domRef;

            delete this.columnDrop.proxyTop;
            delete this.columnDrop.proxyBottom;
            this.columnDrop.destroy();
            delete Ext.dd.DDM.locationCache["gridHeader" + this.grid.getGridEl().id];
            delete this.columnDrop._domRef;
            delete Ext.dd.DDM.ids[this.columnDrop.ddGroup];
        }

        if (this.splitZone){ 
            this.splitZone.destroy();
            delete this.splitZone._domRef;
            delete Ext.dd.DDM.ids["gridSplitters" + this.grid.getGridEl().id];
        }

        Ext.fly(this.innerHd).removeAllListeners();
        Ext.removeNode(this.innerHd);
        delete this.innerHd;

        Ext.destroy(
            this.el,
            this.mainWrap,
            this.mainHd,
            this.scroller,
            this.mainBody,
            this.focusEl,
            this.resizeMarker,
            this.resizeProxy,
            this.activeHdBtn,
            this.dragZone,
            this.splitZone,
            this._flyweight
        );

        delete this.grid.container;

        if(this.dragZone){
            this.dragZone.destroy();
        }

        Ext.dd.DDM.currentTarget = null;
        delete Ext.dd.DDM.locationCache[this.grid.getGridEl().id];

        Ext.EventManager.removeResizeListener(this.onWindowResize, this);
    },

    
    onDenyColumnHide : function(){

    },

    
    render : function(){
        if(this.autoFill){
            var ct = this.grid.ownerCt;
            if (ct && ct.getLayout()){
                ct.on('afterlayout', function(){
                    this.fitColumns(true, true);
                    this.updateHeaders();
                }, this, {single: true});
            }else{
                this.fitColumns(true, true);
            }
        }else if(this.forceFit){
            this.fitColumns(true, false);
        }else if(this.grid.autoExpandColumn){
            this.autoExpand(true);
        }

        this.renderUI();
    },

    
    
    initData : function(ds, cm){
        if(this.ds){
            this.ds.un('load', this.onLoad, this);
            this.ds.un('datachanged', this.onDataChange, this);
            this.ds.un('add', this.onAdd, this);
            this.ds.un('remove', this.onRemove, this);
            this.ds.un('update', this.onUpdate, this);
            this.ds.un('clear', this.onClear, this);
            if(this.ds !== ds && this.ds.autoDestroy){
                this.ds.destroy();
            }
        }
        if(ds){
            ds.on({
                scope: this,
                load: this.onLoad,
                datachanged: this.onDataChange,
                add: this.onAdd,
                remove: this.onRemove,
                update: this.onUpdate,
                clear: this.onClear
            });
        }
        this.ds = ds;

        if(this.cm){
            this.cm.un('configchange', this.onColConfigChange, this);
            this.cm.un('widthchange', this.onColWidthChange, this);
            this.cm.un('headerchange', this.onHeaderChange, this);
            this.cm.un('hiddenchange', this.onHiddenChange, this);
            this.cm.un('columnmoved', this.onColumnMove, this);
        }
        if(cm){
            delete this.lastViewWidth;
            cm.on({
                scope: this,
                configchange: this.onColConfigChange,
                widthchange: this.onColWidthChange,
                headerchange: this.onHeaderChange,
                hiddenchange: this.onHiddenChange,
                columnmoved: this.onColumnMove
            });
        }
        this.cm = cm;
    },

    
    onDataChange : function(){
        this.refresh();
        this.updateHeaderSortState();
        this.syncFocusEl(0);
    },

    
    onClear : function(){
        this.refresh();
        this.syncFocusEl(0);
    },

    
    onUpdate : function(ds, record){
        this.refreshRow(record);
    },

    
    onAdd : function(ds, records, index){
        this.insertRows(ds, index, index + (records.length-1));
    },

    
    onRemove : function(ds, record, index, isUpdate){
        if(isUpdate !== true){
            this.fireEvent('beforerowremoved', this, index, record);
        }
        this.removeRow(index);
        if(isUpdate !== true){
            this.processRows(index);
            this.applyEmptyText();
            this.fireEvent('rowremoved', this, index, record);
        }
    },

    
    onLoad : function(){
        if (Ext.isGecko){
            if (!this.scrollToTopTask) {
                this.scrollToTopTask = new Ext.util.DelayedTask(this.scrollToTop, this);
            }
            this.scrollToTopTask.delay(1);
        }else{
            this.scrollToTop();
        }
    },

    
    onColWidthChange : function(cm, col, width){
        this.updateColumnWidth(col, width);
    },

    
    onHeaderChange : function(cm, col, text){
        this.updateHeaders();
    },

    
    onHiddenChange : function(cm, col, hidden){
        this.updateColumnHidden(col, hidden);
    },

    
    onColumnMove : function(cm, oldIndex, newIndex){
        this.indexMap = null;
        var s = this.getScrollState();
        this.refresh(true);
        this.restoreScroll(s);
        this.afterMove(newIndex);
        this.grid.fireEvent('columnmove', oldIndex, newIndex);
    },

    
    onColConfigChange : function(){
        delete this.lastViewWidth;
        this.indexMap = null;
        this.refresh(true);
    },

    
    
    initUI : function(grid){
        grid.on('headerclick', this.onHeaderClick, this);
    },

    
    initEvents : function(){
    },

    
    onHeaderClick : function(g, index){
        if(this.headersDisabled || !this.cm.isSortable(index)){
            return;
        }
        g.stopEditing(true);
        g.store.sort(this.cm.getDataIndex(index));
    },

    
    onRowOver : function(e, t){
        var row;
        if((row = this.findRowIndex(t)) !== false){
            this.addRowClass(row, 'x-grid3-row-over');
        }
    },

    
    onRowOut : function(e, t){
        var row;
        if((row = this.findRowIndex(t)) !== false && !e.within(this.getRow(row), true)){
            this.removeRowClass(row, 'x-grid3-row-over');
        }
    },

    
    handleWheel : function(e){
        e.stopPropagation();
    },

    
    onRowSelect : function(row){
        this.addRowClass(row, this.selectedRowClass);
    },

    
    onRowDeselect : function(row){
        this.removeRowClass(row, this.selectedRowClass);
    },

    
    onCellSelect : function(row, col){
        var cell = this.getCell(row, col);
        if(cell){
            this.fly(cell).addClass('x-grid3-cell-selected');
        }
    },

    
    onCellDeselect : function(row, col){
        var cell = this.getCell(row, col);
        if(cell){
            this.fly(cell).removeClass('x-grid3-cell-selected');
        }
    },

    
    onColumnSplitterMoved : function(i, w){
        this.userResized = true;
        var cm = this.grid.colModel;
        cm.setColumnWidth(i, w, true);

        if(this.forceFit){
            this.fitColumns(true, false, i);
            this.updateAllColumnWidths();
        }else{
            this.updateColumnWidth(i, w);
            this.syncHeaderScroll();
        }

        this.grid.fireEvent('columnresize', i, w);
    },

    
    handleHdMenuClick : function(item){
        var index = this.hdCtxIndex,
            cm = this.cm,
            ds = this.ds,
            id = item.getItemId();
        switch(id){
            case 'asc':
                ds.sort(cm.getDataIndex(index), 'ASC');
                break;
            case 'desc':
                ds.sort(cm.getDataIndex(index), 'DESC');
                break;
            default:
                index = cm.getIndexById(id.substr(4));
                if(index != -1){
                    if(item.checked && cm.getColumnsBy(this.isHideableColumn, this).length <= 1){
                        this.onDenyColumnHide();
                        return false;
                    }
                    cm.setHidden(index, item.checked);
                }
        }
        return true;
    },

    
    isHideableColumn : function(c){
        return !c.hidden;
    },

    
    beforeColMenuShow : function(){
        var cm = this.cm,  colCount = cm.getColumnCount();
        this.colMenu.removeAll();
        for(var i = 0; i < colCount; i++){
            if(cm.config[i].hideable !== false){
                this.colMenu.add(new Ext.menu.CheckItem({
                    itemId: 'col-'+cm.getColumnId(i),
                    text: cm.getColumnHeader(i),
                    checked: !cm.isHidden(i),
                    hideOnClick:false,
                    disabled: cm.config[i].hideable === false
                }));
            }
        }
    },

    
    handleHdDown : function(e, t){
        if(Ext.fly(t).hasClass('x-grid3-hd-btn')){
            e.stopEvent();
            var hd = this.findHeaderCell(t);
            Ext.fly(hd).addClass('x-grid3-hd-menu-open');
            var index = this.getCellIndex(hd);
            this.hdCtxIndex = index;
            var ms = this.hmenu.items, cm = this.cm;
            ms.get('asc').setDisabled(!cm.isSortable(index));
            ms.get('desc').setDisabled(!cm.isSortable(index));
            this.hmenu.on('hide', function(){
                Ext.fly(hd).removeClass('x-grid3-hd-menu-open');
            }, this, {single:true});
            this.hmenu.show(t, 'tl-bl?');
        }
    },

    
    handleHdOver : function(e, t){
        var hd = this.findHeaderCell(t);
        if(hd && !this.headersDisabled){
            this.activeHdRef = t;
            this.activeHdIndex = this.getCellIndex(hd);
            var fly = this.fly(hd);
            this.activeHdRegion = fly.getRegion();
            if(!this.cm.isMenuDisabled(this.activeHdIndex)){
                fly.addClass('x-grid3-hd-over');
                this.activeHdBtn = fly.child('.x-grid3-hd-btn');
                if(this.activeHdBtn){
                    this.activeHdBtn.dom.style.height = (hd.firstChild.offsetHeight-1)+'px';
                }
            }
        }
    },

    
    handleHdMove : function(e, t){
        var hd = this.findHeaderCell(this.activeHdRef);
        if(hd && !this.headersDisabled){
            var hw = this.splitHandleWidth || 5,
                r = this.activeHdRegion,
                x = e.getPageX(),
                ss = hd.style,
                cur = '';
            if(this.grid.enableColumnResize !== false){
                if(x - r.left <= hw && this.cm.isResizable(this.activeHdIndex-1)){
                    cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'e-resize' : 'col-resize'; 
                }else if(r.right - x <= (!this.activeHdBtn ? hw : 2) && this.cm.isResizable(this.activeHdIndex)){
                    cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'w-resize' : 'col-resize';
                }
            }
            ss.cursor = cur;
        }
    },

    
    handleHdOut : function(e, t){
        var hd = this.findHeaderCell(t);
        if(hd && (!Ext.isIE || !e.within(hd, true))){
            this.activeHdRef = null;
            this.fly(hd).removeClass('x-grid3-hd-over');
            hd.style.cursor = '';
        }
    },

    
    hasRows : function(){
        var fc = this.mainBody.dom.firstChild;
        return fc && fc.nodeType == 1 && fc.className != 'x-grid-empty';
    },

    
    bind : function(d, c){
        this.initData(d, c);
    }
});




Ext.grid.GridView.SplitDragZone = Ext.extend(Ext.dd.DDProxy, {
    
    constructor: function(grid, hd){
        this.grid = grid;
        this.view = grid.getView();
        this.marker = this.view.resizeMarker;
        this.proxy = this.view.resizeProxy;
        Ext.grid.GridView.SplitDragZone.superclass.constructor.call(this, hd,
            'gridSplitters' + this.grid.getGridEl().id, {
            dragElId : Ext.id(this.proxy.dom), resizeFrame:false
        });
        this.scroll = false;
        this.hw = this.view.splitHandleWidth || 5;
    },

    b4StartDrag : function(x, y){
        this.dragHeadersDisabled = this.view.headersDisabled;
        this.view.headersDisabled = true;
        var h = this.view.mainWrap.getHeight();
        this.marker.setHeight(h);
        this.marker.show();
        this.marker.alignTo(this.view.getHeaderCell(this.cellIndex), 'tl-tl', [-2, 0]);
        this.proxy.setHeight(h);
        var w = this.cm.getColumnWidth(this.cellIndex),
            minw = Math.max(w-this.grid.minColumnWidth, 0);
        this.resetConstraints();
        this.setXConstraint(minw, 1000);
        this.setYConstraint(0, 0);
        this.minX = x - minw;
        this.maxX = x + 1000;
        this.startPos = x;
        Ext.dd.DDProxy.prototype.b4StartDrag.call(this, x, y);
    },

    allowHeaderDrag : function(e){
        return true;
    },

    handleMouseDown : function(e){
        var t = this.view.findHeaderCell(e.getTarget());
        if(t && this.allowHeaderDrag(e)){
            var xy = this.view.fly(t).getXY(), 
                x = xy[0], 
                y = xy[1],
                exy = e.getXY(), ex = exy[0],
                w = t.offsetWidth, adjust = false;
                
            if((ex - x) <= this.hw){
                adjust = -1;
            }else if((x+w) - ex <= this.hw){
                adjust = 0;
            }
            if(adjust !== false){
                this.cm = this.grid.colModel;
                var ci = this.view.getCellIndex(t);
                if(adjust == -1){
                  if (ci + adjust < 0) {
                    return;
                  }
                    while(this.cm.isHidden(ci+adjust)){
                        --adjust;
                        if(ci+adjust < 0){
                            return;
                        }
                    }
                }
                this.cellIndex = ci+adjust;
                this.split = t.dom;
                if(this.cm.isResizable(this.cellIndex) && !this.cm.isFixed(this.cellIndex)){
                    Ext.grid.GridView.SplitDragZone.superclass.handleMouseDown.apply(this, arguments);
                }
            }else if(this.view.columnDrag){
                this.view.columnDrag.callHandleMouseDown(e);
            }
        }
    },

    endDrag : function(e){
        this.marker.hide();
        var v = this.view,
            endX = Math.max(this.minX, e.getPageX()),
            diff = endX - this.startPos,
            disabled = this.dragHeadersDisabled;
            
        v.onColumnSplitterMoved(this.cellIndex, this.cm.getColumnWidth(this.cellIndex)+diff);
        setTimeout(function(){
            v.headersDisabled = disabled;
        }, 50);
    },

    autoOffset : function(){
        this.setDelta(0,0);
    }
});


Ext.grid.HeaderDragZone = Ext.extend(Ext.dd.DragZone, {
    maxDragWidth: 120,
    
    constructor : function(grid, hd, hd2){
        this.grid = grid;
        this.view = grid.getView();
        this.ddGroup = "gridHeader" + this.grid.getGridEl().id;
        Ext.grid.HeaderDragZone.superclass.constructor.call(this, hd);
        if(hd2){
            this.setHandleElId(Ext.id(hd));
            this.setOuterHandleElId(Ext.id(hd2));
        }
        this.scroll = false;
    },
    
    getDragData : function(e){
        var t = Ext.lib.Event.getTarget(e),
            h = this.view.findHeaderCell(t);
        if(h){
            return {ddel: h.firstChild, header:h};
        }
        return false;
    },

    onInitDrag : function(e){
        
        this.dragHeadersDisabled = this.view.headersDisabled;
        this.view.headersDisabled = true;
        var clone = this.dragData.ddel.cloneNode(true);
        clone.id = Ext.id();
        clone.style.width = Math.min(this.dragData.header.offsetWidth,this.maxDragWidth) + "px";
        this.proxy.update(clone);
        return true;
    },

    afterValidDrop : function(){
        this.completeDrop();
    },

    afterInvalidDrop : function(){
        this.completeDrop();
    },
    
    completeDrop: function(){
        var v = this.view,
            disabled = this.dragHeadersDisabled;
        setTimeout(function(){
            v.headersDisabled = disabled;
        }, 50);
    }
});



Ext.grid.HeaderDropZone = Ext.extend(Ext.dd.DropZone, {
    proxyOffsets : [-4, -9],
    fly: Ext.Element.fly,
    
    constructor : function(grid, hd, hd2){
        this.grid = grid;
        this.view = grid.getView();
        
        this.proxyTop = Ext.DomHelper.append(document.body, {
            cls:"col-move-top", html:"&#160;"
        }, true);
        this.proxyBottom = Ext.DomHelper.append(document.body, {
            cls:"col-move-bottom", html:"&#160;"
        }, true);
        this.proxyTop.hide = this.proxyBottom.hide = function(){
            this.setLeftTop(-100,-100);
            this.setStyle("visibility", "hidden");
        };
        this.ddGroup = "gridHeader" + this.grid.getGridEl().id;
        
        
        Ext.grid.HeaderDropZone.superclass.constructor.call(this, grid.getGridEl().dom);
    },

    getTargetFromEvent : function(e){
        var t = Ext.lib.Event.getTarget(e),
            cindex = this.view.findCellIndex(t);
        if(cindex !== false){
            return this.view.getHeaderCell(cindex);
        }
    },

    nextVisible : function(h){
        var v = this.view, cm = this.grid.colModel;
        h = h.nextSibling;
        while(h){
            if(!cm.isHidden(v.getCellIndex(h))){
                return h;
            }
            h = h.nextSibling;
        }
        return null;
    },

    prevVisible : function(h){
        var v = this.view, cm = this.grid.colModel;
        h = h.prevSibling;
        while(h){
            if(!cm.isHidden(v.getCellIndex(h))){
                return h;
            }
            h = h.prevSibling;
        }
        return null;
    },

    positionIndicator : function(h, n, e){
        var x = Ext.lib.Event.getPageX(e),
            r = Ext.lib.Dom.getRegion(n.firstChild),
            px, 
            pt, 
            py = r.top + this.proxyOffsets[1];
        if((r.right - x) <= (r.right-r.left)/2){
            px = r.right+this.view.borderWidth;
            pt = "after";
        }else{
            px = r.left;
            pt = "before";
        }

        if(this.grid.colModel.isFixed(this.view.getCellIndex(n))){
            return false;
        }

        px +=  this.proxyOffsets[0];
        this.proxyTop.setLeftTop(px, py);
        this.proxyTop.show();
        if(!this.bottomOffset){
            this.bottomOffset = this.view.mainHd.getHeight();
        }
        this.proxyBottom.setLeftTop(px, py+this.proxyTop.dom.offsetHeight+this.bottomOffset);
        this.proxyBottom.show();
        return pt;
    },

    onNodeEnter : function(n, dd, e, data){
        if(data.header != n){
            this.positionIndicator(data.header, n, e);
        }
    },

    onNodeOver : function(n, dd, e, data){
        var result = false;
        if(data.header != n){
            result = this.positionIndicator(data.header, n, e);
        }
        if(!result){
            this.proxyTop.hide();
            this.proxyBottom.hide();
        }
        return result ? this.dropAllowed : this.dropNotAllowed;
    },

    onNodeOut : function(n, dd, e, data){
        this.proxyTop.hide();
        this.proxyBottom.hide();
    },

    onNodeDrop : function(n, dd, e, data){
        var h = data.header;
        if(h != n){
            var cm = this.grid.colModel,
                x = Ext.lib.Event.getPageX(e),
                r = Ext.lib.Dom.getRegion(n.firstChild),
                pt = (r.right - x) <= ((r.right-r.left)/2) ? "after" : "before",
                oldIndex = this.view.getCellIndex(h),
                newIndex = this.view.getCellIndex(n);
            if(pt == "after"){
                newIndex++;
            }
            if(oldIndex < newIndex){
                newIndex--;
            }
            cm.moveColumn(oldIndex, newIndex);
            return true;
        }
        return false;
    }
});

Ext.grid.GridView.ColumnDragZone = Ext.extend(Ext.grid.HeaderDragZone, {
    
    constructor : function(grid, hd){
        Ext.grid.GridView.ColumnDragZone.superclass.constructor.call(this, grid, hd, null);
        this.proxy.el.addClass('x-grid3-col-dd');
    },
    
    handleMouseDown : function(e){
    },

    callHandleMouseDown : function(e){
        Ext.grid.GridView.ColumnDragZone.superclass.handleMouseDown.call(this, e);
    }
});

Ext.grid.SplitDragZone = Ext.extend(Ext.dd.DDProxy, {
    fly: Ext.Element.fly,
    
    constructor : function(grid, hd, hd2){
        this.grid = grid;
        this.view = grid.getView();
        this.proxy = this.view.resizeProxy;
        Ext.grid.SplitDragZone.superclass.constructor.call(this, hd,
            "gridSplitters" + this.grid.getGridEl().id, {
            dragElId : Ext.id(this.proxy.dom), resizeFrame:false
        });
        this.setHandleElId(Ext.id(hd));
        this.setOuterHandleElId(Ext.id(hd2));
        this.scroll = false;
    },

    b4StartDrag : function(x, y){
        this.view.headersDisabled = true;
        this.proxy.setHeight(this.view.mainWrap.getHeight());
        var w = this.cm.getColumnWidth(this.cellIndex);
        var minw = Math.max(w-this.grid.minColumnWidth, 0);
        this.resetConstraints();
        this.setXConstraint(minw, 1000);
        this.setYConstraint(0, 0);
        this.minX = x - minw;
        this.maxX = x + 1000;
        this.startPos = x;
        Ext.dd.DDProxy.prototype.b4StartDrag.call(this, x, y);
    },


    handleMouseDown : function(e){
        var ev = Ext.EventObject.setEvent(e);
        var t = this.fly(ev.getTarget());
        if(t.hasClass("x-grid-split")){
            this.cellIndex = this.view.getCellIndex(t.dom);
            this.split = t.dom;
            this.cm = this.grid.colModel;
            if(this.cm.isResizable(this.cellIndex) && !this.cm.isFixed(this.cellIndex)){
                Ext.grid.SplitDragZone.superclass.handleMouseDown.apply(this, arguments);
            }
        }
    },

    endDrag : function(e){
        this.view.headersDisabled = false;
        var endX = Math.max(this.minX, Ext.lib.Event.getPageX(e));
        var diff = endX - this.startPos;
        this.view.onColumnSplitterMoved(this.cellIndex, this.cm.getColumnWidth(this.cellIndex)+diff);
    },

    autoOffset : function(){
        this.setDelta(0,0);
    }
});
Ext.grid.GridDragZone = function(grid, config){
    this.view = grid.getView();
    Ext.grid.GridDragZone.superclass.constructor.call(this, this.view.mainBody.dom, config);
    this.scroll = false;
    this.grid = grid;
    this.ddel = document.createElement('div');
    this.ddel.className = 'x-grid-dd-wrap';
};

Ext.extend(Ext.grid.GridDragZone, Ext.dd.DragZone, {
    ddGroup : "GridDD",

    
    getDragData : function(e){
        var t = Ext.lib.Event.getTarget(e);
        var rowIndex = this.view.findRowIndex(t);
        if(rowIndex !== false){
            var sm = this.grid.selModel;
            if(!sm.isSelected(rowIndex) || e.hasModifier()){
                sm.handleMouseDown(this.grid, rowIndex, e);
            }
            return {grid: this.grid, ddel: this.ddel, rowIndex: rowIndex, selections:sm.getSelections()};
        }
        return false;
    },

    
    onInitDrag : function(e){
        var data = this.dragData;
        this.ddel.innerHTML = this.grid.getDragDropText();
        this.proxy.update(this.ddel);
        
    },

    
    afterRepair : function(){
        this.dragging = false;
    },

    
    getRepairXY : function(e, data){
        return false;
    },

    onEndDrag : function(data, e){
        
    },

    onValidDrop : function(dd, e, id){
        
        this.hideProxy();
    },

    beforeInvalidDrop : function(e, id){

    }
});

Ext.grid.ColumnModel = Ext.extend(Ext.util.Observable, {
    
    defaultWidth: 100,
    
    defaultSortable: false,
    
    

    constructor : function(config){
        
	    if(config.columns){
	        Ext.apply(this, config);
	        this.setConfig(config.columns, true);
	    }else{
	        this.setConfig(config, true);
	    }
	    this.addEvents(
	        
	        "widthchange",
	        
	        "headerchange",
	        
	        "hiddenchange",
	        
	        "columnmoved",
	        
	        "configchange"
	    );
	    Ext.grid.ColumnModel.superclass.constructor.call(this);
    },

    
    getColumnId : function(index){
        return this.config[index].id;
    },

    getColumnAt : function(index){
        return this.config[index];
    },

    
    setConfig : function(config, initial){
        var i, c, len;
        if(!initial){ 
            delete this.totalWidth;
            for(i = 0, len = this.config.length; i < len; i++){
                c = this.config[i];
                if(c.setEditor){
                    
                    c.setEditor(null);
                }
            }
        }

        
        this.defaults = Ext.apply({
            width: this.defaultWidth,
            sortable: this.defaultSortable
        }, this.defaults);

        this.config = config;
        this.lookup = {};

        for(i = 0, len = config.length; i < len; i++){
            c = Ext.applyIf(config[i], this.defaults);
            
            if(Ext.isEmpty(c.id)){
                c.id = i;
            }
            if(!c.isColumn){
                var Cls = Ext.grid.Column.types[c.xtype || 'gridcolumn'];
                c = new Cls(c);
                config[i] = c;
            }
            this.lookup[c.id] = c;
        }
        if(!initial){
            this.fireEvent('configchange', this);
        }
    },

    
    getColumnById : function(id){
        return this.lookup[id];
    },

    
    getIndexById : function(id){
        for(var i = 0, len = this.config.length; i < len; i++){
            if(this.config[i].id == id){
                return i;
            }
        }
        return -1;
    },

    
    moveColumn : function(oldIndex, newIndex){
        var c = this.config[oldIndex];
        this.config.splice(oldIndex, 1);
        this.config.splice(newIndex, 0, c);
        this.dataMap = null;
        this.fireEvent("columnmoved", this, oldIndex, newIndex);
    },

    
    getColumnCount : function(visibleOnly){
        if(visibleOnly === true){
            var c = 0;
            for(var i = 0, len = this.config.length; i < len; i++){
                if(!this.isHidden(i)){
                    c++;
                }
            }
            return c;
        }
        return this.config.length;
    },

    
    getColumnsBy : function(fn, scope){
        var r = [];
        for(var i = 0, len = this.config.length; i < len; i++){
            var c = this.config[i];
            if(fn.call(scope||this, c, i) === true){
                r[r.length] = c;
            }
        }
        return r;
    },

    
    isSortable : function(col){
        return !!this.config[col].sortable;
    },

    
    isMenuDisabled : function(col){
        return !!this.config[col].menuDisabled;
    },

    
    getRenderer : function(col){
        if(!this.config[col].renderer){
            return Ext.grid.ColumnModel.defaultRenderer;
        }
        return this.config[col].renderer;
    },

    getRendererScope : function(col){
        return this.config[col].scope;
    },

    
    setRenderer : function(col, fn){
        this.config[col].renderer = fn;
    },

    
    getColumnWidth : function(col){
        return this.config[col].width;
    },

    
    setColumnWidth : function(col, width, suppressEvent){
        this.config[col].width = width;
        this.totalWidth = null;
        if(!suppressEvent){
             this.fireEvent("widthchange", this, col, width);
        }
    },

    
    getTotalWidth : function(includeHidden){
        if(!this.totalWidth){
            this.totalWidth = 0;
            for(var i = 0, len = this.config.length; i < len; i++){
                if(includeHidden || !this.isHidden(i)){
                    this.totalWidth += this.getColumnWidth(i);
                }
            }
        }
        return this.totalWidth;
    },

    
    getColumnHeader : function(col){
        return this.config[col].header;
    },

    
    setColumnHeader : function(col, header){
        this.config[col].header = header;
        this.fireEvent("headerchange", this, col, header);
    },

    
    getColumnTooltip : function(col){
            return this.config[col].tooltip;
    },
    
    setColumnTooltip : function(col, tooltip){
            this.config[col].tooltip = tooltip;
    },

    
    getDataIndex : function(col){
        return this.config[col].dataIndex;
    },

    
    setDataIndex : function(col, dataIndex){
        this.config[col].dataIndex = dataIndex;
    },

    
    findColumnIndex : function(dataIndex){
        var c = this.config;
        for(var i = 0, len = c.length; i < len; i++){
            if(c[i].dataIndex == dataIndex){
                return i;
            }
        }
        return -1;
    },

    
    isCellEditable : function(colIndex, rowIndex){
        var c = this.config[colIndex],
            ed = c.editable;

        
        return !!(ed || (!Ext.isDefined(ed) && c.editor));
    },

    
    getCellEditor : function(colIndex, rowIndex){
        return this.config[colIndex].getCellEditor(rowIndex);
    },

    
    setEditable : function(col, editable){
        this.config[col].editable = editable;
    },

    
    isHidden : function(colIndex){
        return !!this.config[colIndex].hidden; 
    },

    
    isFixed : function(colIndex){
        return !!this.config[colIndex].fixed;
    },

    
    isResizable : function(colIndex){
        return colIndex >= 0 && this.config[colIndex].resizable !== false && this.config[colIndex].fixed !== true;
    },
    
    setHidden : function(colIndex, hidden){
        var c = this.config[colIndex];
        if(c.hidden !== hidden){
            c.hidden = hidden;
            this.totalWidth = null;
            this.fireEvent("hiddenchange", this, colIndex, hidden);
        }
    },

    
    setEditor : function(col, editor){
        this.config[col].setEditor(editor);
    },

    
    destroy : function(){
        var c;
        for(var i = 0, len = this.config.length; i < len; i++){
            c = this.config[i];
            if(c.setEditor){
                c.setEditor(null);
            }
        }
        this.purgeListeners();
    }
});


Ext.grid.ColumnModel.defaultRenderer = function(value){
    if(typeof value == "string" && value.length < 1){
        return "&#160;";
    }
    return value;
};
Ext.grid.AbstractSelectionModel = Ext.extend(Ext.util.Observable,  {
    

    constructor : function(){
        this.locked = false;
        Ext.grid.AbstractSelectionModel.superclass.constructor.call(this);
    },

    
    init : function(grid){
        this.grid = grid;
        if(this.lockOnInit){
            delete this.lockOnInit;
            this.locked = false;
            this.lock();
        }
        this.initEvents();
    },

    
    lock : function(){
        if(!this.locked){
            this.locked = true;
            
            var g = this.grid;
            if(g){
                g.getView().on({
                    scope: this,
                    beforerefresh: this.sortUnLock,
                    refresh: this.sortLock
                });
            }else{
                this.lockOnInit = true;
            }
        }
    },

    
    sortLock : function() {
        this.locked = true;
    },

    
    sortUnLock : function() {
        this.locked = false;
    },

    
    unlock : function(){
        if(this.locked){
            this.locked = false;
            var g = this.grid,
                gv;
                
            
            if(g){
                gv = g.getView();
                gv.un('beforerefresh', this.sortUnLock, this);
                gv.un('refresh', this.sortLock, this);    
            }else{
                delete this.lockOnInit;
            }
        }
    },

    
    isLocked : function(){
        return this.locked;
    },

    destroy: function(){
        this.unlock();
        this.purgeListeners();
    }
});
Ext.grid.RowSelectionModel = Ext.extend(Ext.grid.AbstractSelectionModel,  {
    
    singleSelect : false,
    
    constructor : function(config){
        Ext.apply(this, config);
        this.selections = new Ext.util.MixedCollection(false, function(o){
            return o.id;
        });

        this.last = false;
        this.lastActive = false;

        this.addEvents(
	        
	        'selectionchange',
	        
	        'beforerowselect',
	        
	        'rowselect',
	        
	        'rowdeselect'
        );
        Ext.grid.RowSelectionModel.superclass.constructor.call(this);
    },

    
    
    initEvents : function(){

        if(!this.grid.enableDragDrop && !this.grid.enableDrag){
            this.grid.on('rowmousedown', this.handleMouseDown, this);
        }

        this.rowNav = new Ext.KeyNav(this.grid.getGridEl(), {
            'up' : function(e){
                if(!e.shiftKey || this.singleSelect){
                    this.selectPrevious(false);
                }else if(this.last !== false && this.lastActive !== false){
                    var last = this.last;
                    this.selectRange(this.last,  this.lastActive-1);
                    this.grid.getView().focusRow(this.lastActive);
                    if(last !== false){
                        this.last = last;
                    }
                }else{
                    this.selectFirstRow();
                }
            },
            'down' : function(e){
                if(!e.shiftKey || this.singleSelect){
                    this.selectNext(false);
                }else if(this.last !== false && this.lastActive !== false){
                    var last = this.last;
                    this.selectRange(this.last,  this.lastActive+1);
                    this.grid.getView().focusRow(this.lastActive);
                    if(last !== false){
                        this.last = last;
                    }
                }else{
                    this.selectFirstRow();
                }
            },
            scope: this
        });

        this.grid.getView().on({
            scope: this,
            refresh: this.onRefresh,
            rowupdated: this.onRowUpdated,
            rowremoved: this.onRemove
        });
    },

    
    onRefresh : function(){
        var ds = this.grid.store, index;
        var s = this.getSelections();
        this.clearSelections(true);
        for(var i = 0, len = s.length; i < len; i++){
            var r = s[i];
            if((index = ds.indexOfId(r.id)) != -1){
                this.selectRow(index, true);
            }
        }
        if(s.length != this.selections.getCount()){
            this.fireEvent('selectionchange', this);
        }
    },

    
    onRemove : function(v, index, r){
        if(this.selections.remove(r) !== false){
            this.fireEvent('selectionchange', this);
        }
    },

    
    onRowUpdated : function(v, index, r){
        if(this.isSelected(r)){
            v.onRowSelect(index);
        }
    },

    
    selectRecords : function(records, keepExisting){
        if(!keepExisting){
            this.clearSelections();
        }
        var ds = this.grid.store;
        for(var i = 0, len = records.length; i < len; i++){
            this.selectRow(ds.indexOf(records[i]), true);
        }
    },

    
    getCount : function(){
        return this.selections.length;
    },

    
    selectFirstRow : function(){
        this.selectRow(0);
    },

    
    selectLastRow : function(keepExisting){
        this.selectRow(this.grid.store.getCount() - 1, keepExisting);
    },

    
    selectNext : function(keepExisting){
        if(this.hasNext()){
            this.selectRow(this.last+1, keepExisting);
            this.grid.getView().focusRow(this.last);
            return true;
        }
        return false;
    },

    
    selectPrevious : function(keepExisting){
        if(this.hasPrevious()){
            this.selectRow(this.last-1, keepExisting);
            this.grid.getView().focusRow(this.last);
            return true;
        }
        return false;
    },

    
    hasNext : function(){
        return this.last !== false && (this.last+1) < this.grid.store.getCount();
    },

    
    hasPrevious : function(){
        return !!this.last;
    },


    
    getSelections : function(){
        return [].concat(this.selections.items);
    },

    
    getSelected : function(){
        return this.selections.itemAt(0);
    },

    
    each : function(fn, scope){
        var s = this.getSelections();
        for(var i = 0, len = s.length; i < len; i++){
            if(fn.call(scope || this, s[i], i) === false){
                return false;
            }
        }
        return true;
    },

    
    clearSelections : function(fast){
        if(this.isLocked()){
            return;
        }
        if(fast !== true){
            var ds = this.grid.store;
            var s = this.selections;
            s.each(function(r){
                this.deselectRow(ds.indexOfId(r.id));
            }, this);
            s.clear();
        }else{
            this.selections.clear();
        }
        this.last = false;
    },


    
    selectAll : function(){
        if(this.isLocked()){
            return;
        }
        this.selections.clear();
        for(var i = 0, len = this.grid.store.getCount(); i < len; i++){
            this.selectRow(i, true);
        }
    },

    
    hasSelection : function(){
        return this.selections.length > 0;
    },

    
    isSelected : function(index){
        var r = Ext.isNumber(index) ? this.grid.store.getAt(index) : index;
        return (r && this.selections.key(r.id) ? true : false);
    },

    
    isIdSelected : function(id){
        return (this.selections.key(id) ? true : false);
    },

    
    handleMouseDown : function(g, rowIndex, e){
        if(e.button !== 0 || this.isLocked()){
            return;
        }
        var view = this.grid.getView();
        if(e.shiftKey && !this.singleSelect && this.last !== false){
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last; 
            view.focusRow(rowIndex);
        }else{
            var isSelected = this.isSelected(rowIndex);
            if(e.ctrlKey && isSelected){
                this.deselectRow(rowIndex);
            }else if(!isSelected || this.getCount() > 1){
                this.selectRow(rowIndex, e.ctrlKey || e.shiftKey);
                view.focusRow(rowIndex);
            }
        }
    },

    
    selectRows : function(rows, keepExisting){
        if(!keepExisting){
            this.clearSelections();
        }
        for(var i = 0, len = rows.length; i < len; i++){
            this.selectRow(rows[i], true);
        }
    },

    
    selectRange : function(startRow, endRow, keepExisting){
        var i;
        if(this.isLocked()){
            return;
        }
        if(!keepExisting){
            this.clearSelections();
        }
        if(startRow <= endRow){
            for(i = startRow; i <= endRow; i++){
                this.selectRow(i, true);
            }
        }else{
            for(i = startRow; i >= endRow; i--){
                this.selectRow(i, true);
            }
        }
    },

    
    deselectRange : function(startRow, endRow, preventViewNotify){
        if(this.isLocked()){
            return;
        }
        for(var i = startRow; i <= endRow; i++){
            this.deselectRow(i, preventViewNotify);
        }
    },

    
    selectRow : function(index, keepExisting, preventViewNotify){
        if(this.isLocked() || (index < 0 || index >= this.grid.store.getCount()) || (keepExisting && this.isSelected(index))){
            return;
        }
        var r = this.grid.store.getAt(index);
        if(r && this.fireEvent('beforerowselect', this, index, keepExisting, r) !== false){
            if(!keepExisting || this.singleSelect){
                this.clearSelections();
            }
            this.selections.add(r);
            this.last = this.lastActive = index;
            if(!preventViewNotify){
                this.grid.getView().onRowSelect(index);
            }
            this.fireEvent('rowselect', this, index, r);
            this.fireEvent('selectionchange', this);
        }
    },

    
    deselectRow : function(index, preventViewNotify){
        if(this.isLocked()){
            return;
        }
        if(this.last == index){
            this.last = false;
        }
        if(this.lastActive == index){
            this.lastActive = false;
        }
        var r = this.grid.store.getAt(index);
        if(r){
            this.selections.remove(r);
            if(!preventViewNotify){
                this.grid.getView().onRowDeselect(index);
            }
            this.fireEvent('rowdeselect', this, index, r);
            this.fireEvent('selectionchange', this);
        }
    },

    
    restoreLast : function(){
        if(this._last){
            this.last = this._last;
        }
    },

    
    acceptsNav : function(row, col, cm){
        return !cm.isHidden(col) && cm.isCellEditable(col, row);
    },

    
    onEditorKey : function(field, e){
        var k = e.getKey(), 
            newCell, 
            g = this.grid, 
            last = g.lastEdit,
            ed = g.activeEditor,
            ae, last, r, c;
        var shift = e.shiftKey;
        if(k == e.TAB){
            e.stopEvent();
            ed.completeEdit();
            if(shift){
                newCell = g.walkCells(ed.row, ed.col-1, -1, this.acceptsNav, this);
            }else{
                newCell = g.walkCells(ed.row, ed.col+1, 1, this.acceptsNav, this);
            }
        }else if(k == e.ENTER){
            if(this.moveEditorOnEnter !== false){
                if(shift){
                    newCell = g.walkCells(last.row - 1, last.col, -1, this.acceptsNav, this);
                }else{
                    newCell = g.walkCells(last.row + 1, last.col, 1, this.acceptsNav, this);
                }
            }
        }
        if(newCell){
            r = newCell[0];
            c = newCell[1];

            if(last.row != r){
                this.selectRow(r); 
            }

            if(g.isEditor && g.editing){ 
                ae = g.activeEditor;
                if(ae && ae.field.triggerBlur){
                    
                    ae.field.triggerBlur();
                }
            }
            g.startEditing(r, c);
        }
    },
    
    destroy : function(){
        if(this.rowNav){
            this.rowNav.disable();
            this.rowNav = null;
        }
        Ext.grid.RowSelectionModel.superclass.destroy.call(this);
    }
});
Ext.grid.Column = Ext.extend(Object, {
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    isColumn : true,

    constructor : function(config){
        Ext.apply(this, config);

        if(Ext.isString(this.renderer)){
            this.renderer = Ext.util.Format[this.renderer];
        }else if(Ext.isObject(this.renderer)){
            this.scope = this.renderer.scope;
            this.renderer = this.renderer.fn;
        }
        if(!this.scope){
            this.scope = this;
        }

        var ed = this.editor;
        delete this.editor;
        this.setEditor(ed);
    },

    
    renderer : function(value){
        if(Ext.isString(value) && value.length < 1){
            return '&#160;';
        }
        return value;
    },

    
    getEditor: function(rowIndex){
        return this.editable !== false ? this.editor : null;
    },

    
    setEditor : function(editor){
        var ed = this.editor;
        if(ed){
            if(ed.gridEditor){
                ed.gridEditor.destroy();
                delete ed.gridEditor;
            }else{
                ed.destroy();
            }
        }
        this.editor = null;
        if(editor){
            
            if(!editor.isXType){
                editor = Ext.create(editor, 'textfield');
            }
            this.editor = editor;
        }
    },

    
    getCellEditor: function(rowIndex){
        var ed = this.getEditor(rowIndex);
        if(ed){
            if(!ed.startEdit){
                if(!ed.gridEditor){
                    ed.gridEditor = new Ext.grid.GridEditor(ed);
                }
                ed = ed.gridEditor;
            }
        }
        return ed;
    }
});


Ext.grid.BooleanColumn = Ext.extend(Ext.grid.Column, {
    
    trueText: 'true',
    
    falseText: 'false',
    
    undefinedText: '&#160;',

    constructor: function(cfg){
        Ext.grid.BooleanColumn.superclass.constructor.call(this, cfg);
        var t = this.trueText, f = this.falseText, u = this.undefinedText;
        this.renderer = function(v){
            if(v === undefined){
                return u;
            }
            if(!v || v === 'false'){
                return f;
            }
            return t;
        };
    }
});


Ext.grid.NumberColumn = Ext.extend(Ext.grid.Column, {
    
    format : '0,000.00',
    constructor: function(cfg){
        Ext.grid.NumberColumn.superclass.constructor.call(this, cfg);
        this.renderer = Ext.util.Format.numberRenderer(this.format);
    }
});


Ext.grid.DateColumn = Ext.extend(Ext.grid.Column, {
    
    format : 'm/d/Y',
    constructor: function(cfg){
        Ext.grid.DateColumn.superclass.constructor.call(this, cfg);
        this.renderer = Ext.util.Format.dateRenderer(this.format);
    }
});


Ext.grid.TemplateColumn = Ext.extend(Ext.grid.Column, {
    
    constructor: function(cfg){
        Ext.grid.TemplateColumn.superclass.constructor.call(this, cfg);
        var tpl = (!Ext.isPrimitive(this.tpl) && this.tpl.compile) ? this.tpl : new Ext.XTemplate(this.tpl);
        this.renderer = function(value, p, r){
            return tpl.apply(r.data);
        };
        this.tpl = tpl;
    }
});


Ext.grid.Column.types = {
    gridcolumn : Ext.grid.Column,
    booleancolumn: Ext.grid.BooleanColumn,
    numbercolumn: Ext.grid.NumberColumn,
    datecolumn: Ext.grid.DateColumn,
    templatecolumn: Ext.grid.TemplateColumn
};
Ext.grid.RowNumberer = Ext.extend(Object, {
    
    header: "",
    
    width: 23,
    
    sortable: false,
    
    constructor : function(config){
        Ext.apply(this, config);
        if(this.rowspan){
            this.renderer = this.renderer.createDelegate(this);
        }
    },

    
    fixed:true,
    hideable: false,
    menuDisabled:true,
    dataIndex: '',
    id: 'numberer',
    rowspan: undefined,

    
    renderer : function(v, p, record, rowIndex){
        if(this.rowspan){
            p.cellAttr = 'rowspan="'+this.rowspan+'"';
        }
        return rowIndex+1;
    }
});
Ext.grid.CheckboxSelectionModel = Ext.extend(Ext.grid.RowSelectionModel, {

    
    
    header : '<div class="x-grid3-hd-checker">&#160;</div>',
    
    width : 20,
    
    sortable : false,

    
    menuDisabled : true,
    fixed : true,
    hideable: false,
    dataIndex : '',
    id : 'checker',

    constructor : function(){
        Ext.grid.CheckboxSelectionModel.superclass.constructor.apply(this, arguments);

        if(this.checkOnly){
            this.handleMouseDown = Ext.emptyFn;
        }
    },

    
    initEvents : function(){
        Ext.grid.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.innerHd).on('mousedown', this.onHdMouseDown, this);

        }, this);
    },

    
    
    handleMouseDown : function() {
        Ext.grid.CheckboxSelectionModel.superclass.handleMouseDown.apply(this, arguments);
        this.mouseHandled = true;
    },

    
    onMouseDown : function(e, t){
        if(e.button === 0 && t.className == 'x-grid3-row-checker'){ 
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');

            
            if(!this.mouseHandled && row){
                var index = row.rowIndex;
                if(this.isSelected(index)){
                    this.deselectRow(index);
                }else{
                    this.selectRow(index, true);
                    this.grid.getView().focusRow(index);
                }
            }
        }
        this.mouseHandled = false;
    },

    
    onHdMouseDown : function(e, t){
        if(t.className == 'x-grid3-hd-checker'){
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if(isChecked){
                hd.removeClass('x-grid3-hd-checker-on');
                this.clearSelections();
            }else{
                hd.addClass('x-grid3-hd-checker-on');
                this.selectAll();
            }
        }
    },

    
    renderer : function(v, p, record){
        return '<div class="x-grid3-row-checker">&#160;</div>';
    }
});
Ext.grid.CellSelectionModel = Ext.extend(Ext.grid.AbstractSelectionModel,  {
    
    constructor : function(config){
        Ext.apply(this, config);

	    this.selection = null;
	
	    this.addEvents(
	        
	        "beforecellselect",
	        
	        "cellselect",
	        
	        "selectionchange"
	    );
	
	    Ext.grid.CellSelectionModel.superclass.constructor.call(this);
    },

    
    initEvents : function(){
        this.grid.on('cellmousedown', this.handleMouseDown, this);
        this.grid.on(Ext.EventManager.useKeydown ? 'keydown' : 'keypress', this.handleKeyDown, this);
        this.grid.getView().on({
            scope: this,
            refresh: this.onViewChange,
            rowupdated: this.onRowUpdated,
            beforerowremoved: this.clearSelections,
            beforerowsinserted: this.clearSelections
        });
        if(this.grid.isEditor){
            this.grid.on('beforeedit', this.beforeEdit,  this);
        }
    },

	
    beforeEdit : function(e){
        this.select(e.row, e.column, false, true, e.record);
    },

	
    onRowUpdated : function(v, index, r){
        if(this.selection && this.selection.record == r){
            v.onCellSelect(index, this.selection.cell[1]);
        }
    },

	
    onViewChange : function(){
        this.clearSelections(true);
    },

	
    getSelectedCell : function(){
        return this.selection ? this.selection.cell : null;
    },

    
    clearSelections : function(preventNotify){
        var s = this.selection;
        if(s){
            if(preventNotify !== true){
                this.grid.view.onCellDeselect(s.cell[0], s.cell[1]);
            }
            this.selection = null;
            this.fireEvent("selectionchange", this, null);
        }
    },

    
    hasSelection : function(){
        return this.selection ? true : false;
    },

    
    handleMouseDown : function(g, row, cell, e){
        if(e.button !== 0 || this.isLocked()){
            return;
        }
        this.select(row, cell);
    },

    
    select : function(rowIndex, colIndex, preventViewNotify, preventFocus,  r){
        if(this.fireEvent("beforecellselect", this, rowIndex, colIndex) !== false){
            this.clearSelections();
            r = r || this.grid.store.getAt(rowIndex);
            this.selection = {
                record : r,
                cell : [rowIndex, colIndex]
            };
            if(!preventViewNotify){
                var v = this.grid.getView();
                v.onCellSelect(rowIndex, colIndex);
                if(preventFocus !== true){
                    v.focusCell(rowIndex, colIndex);
                }
            }
            this.fireEvent("cellselect", this, rowIndex, colIndex);
            this.fireEvent("selectionchange", this, this.selection);
        }
    },

	
    isSelectable : function(rowIndex, colIndex, cm){
        return !cm.isHidden(colIndex);
    },
    
    
    onEditorKey: function(field, e){
        if(e.getKey() == e.TAB){
            this.handleKeyDown(e);
        }
    },

    
    handleKeyDown : function(e){
        if(!e.isNavKeyPress()){
            return;
        }
        
        var k = e.getKey(),
            g = this.grid,
            s = this.selection,
            sm = this,
            walk = function(row, col, step){
                return g.walkCells(
                    row,
                    col,
                    step,
                    g.isEditor && g.editing ? sm.acceptsNav : sm.isSelectable, 
                    sm
                );
            },
            cell, newCell, r, c, ae;

        switch(k){
            case e.ESC:
            case e.PAGE_UP:
            case e.PAGE_DOWN:
                
                break;
            default:
                
                e.stopEvent();
                break;
        }

        if(!s){
            cell = walk(0, 0, 1); 
            if(cell){
                this.select(cell[0], cell[1]);
            }
            return;
        }

        cell = s.cell;  
        r = cell[0];    
        c = cell[1];    
        
        switch(k){
            case e.TAB:
                if(e.shiftKey){
                    newCell = walk(r, c - 1, -1);
                }else{
                    newCell = walk(r, c + 1, 1);
                }
                break;
            case e.DOWN:
                newCell = walk(r + 1, c, 1);
                break;
            case e.UP:
                newCell = walk(r - 1, c, -1);
                break;
            case e.RIGHT:
                newCell = walk(r, c + 1, 1);
                break;
            case e.LEFT:
                newCell = walk(r, c - 1, -1);
                break;
            case e.ENTER:
                if (g.isEditor && !g.editing) {
                    g.startEditing(r, c);
                    return;
                }
                break;
        }

        if(newCell){
            
            r = newCell[0];
            c = newCell[1];

            this.select(r, c); 

            if(g.isEditor && g.editing){ 
                ae = g.activeEditor;
                if(ae && ae.field.triggerBlur){
                    
                    ae.field.triggerBlur();
                }
                g.startEditing(r, c);
            }
        }
    },

    acceptsNav : function(row, col, cm){
        return !cm.isHidden(col) && cm.isCellEditable(col, row);
    }
});
Ext.grid.EditorGridPanel = Ext.extend(Ext.grid.GridPanel, {
    
    clicksToEdit: 2,

    
    forceValidation: false,

    
    isEditor : true,
    
    detectEdit: false,

    
    autoEncode : false,

    
    
    trackMouseOver: false, 

    
    initComponent : function(){
        Ext.grid.EditorGridPanel.superclass.initComponent.call(this);

        if(!this.selModel){
            
            this.selModel = new Ext.grid.CellSelectionModel();
        }

        this.activeEditor = null;

        this.addEvents(
            
            "beforeedit",
            
            "afteredit",
            
            "validateedit"
        );
    },

    
    initEvents : function(){
        Ext.grid.EditorGridPanel.superclass.initEvents.call(this);

        this.getGridEl().on('mousewheel', this.stopEditing.createDelegate(this, [true]), this);
        this.on('columnresize', this.stopEditing, this, [true]);

        if(this.clicksToEdit == 1){
            this.on("cellclick", this.onCellDblClick, this);
        }else {
            var view = this.getView();
            if(this.clicksToEdit == 'auto' && view.mainBody){
                view.mainBody.on('mousedown', this.onAutoEditClick, this);
            }
            this.on('celldblclick', this.onCellDblClick, this);
        }
    },

    onResize : function(){
        Ext.grid.EditorGridPanel.superclass.onResize.apply(this, arguments);
        var ae = this.activeEditor;
        if(this.editing && ae){
            ae.realign(true);
        }
    },

    
    onCellDblClick : function(g, row, col){
        this.startEditing(row, col);
    },

    
    onAutoEditClick : function(e, t){
        if(e.button !== 0){
            return;
        }
        var row = this.view.findRowIndex(t),
            col = this.view.findCellIndex(t);
        if(row !== false && col !== false){
            this.stopEditing();
            if(this.selModel.getSelectedCell){ 
                var sc = this.selModel.getSelectedCell();
                if(sc && sc[0] === row && sc[1] === col){
                    this.startEditing(row, col);
                }
            }else{
                if(this.selModel.isSelected(row)){
                    this.startEditing(row, col);
                }
            }
        }
    },

    
    onEditComplete : function(ed, value, startValue){
        this.editing = false;
        this.lastActiveEditor = this.activeEditor;
        this.activeEditor = null;

        var r = ed.record,
            field = this.colModel.getDataIndex(ed.col);
        value = this.postEditValue(value, startValue, r, field);
        if(this.forceValidation === true || String(value) !== String(startValue)){
            var e = {
                grid: this,
                record: r,
                field: field,
                originalValue: startValue,
                value: value,
                row: ed.row,
                column: ed.col,
                cancel:false
            };
            if(this.fireEvent("validateedit", e) !== false && !e.cancel && String(value) !== String(startValue)){
                r.set(field, e.value);
                delete e.cancel;
                this.fireEvent("afteredit", e);
            }
        }
        this.view.focusCell(ed.row, ed.col);
    },

    
    startEditing : function(row, col){
        this.stopEditing();
        if(this.colModel.isCellEditable(col, row)){
            this.view.ensureVisible(row, col, true);
            var r = this.store.getAt(row),
                field = this.colModel.getDataIndex(col),
                e = {
                    grid: this,
                    record: r,
                    field: field,
                    value: r.data[field],
                    row: row,
                    column: col,
                    cancel:false
                };
            if(this.fireEvent("beforeedit", e) !== false && !e.cancel){
                this.editing = true;
                var ed = this.colModel.getCellEditor(col, row);
                if(!ed){
                    return;
                }
                if(!ed.rendered){
                    ed.parentEl = this.view.getEditorParent(ed);
                    ed.on({
                        scope: this,
                        render: {
                            fn: function(c){
                                c.field.focus(false, true);
                            },
                            single: true,
                            scope: this
                        },
                        specialkey: function(field, e){
                            this.getSelectionModel().onEditorKey(field, e);
                        },
                        complete: this.onEditComplete,
                        canceledit: this.stopEditing.createDelegate(this, [true])
                    });
                }
                Ext.apply(ed, {
                    row     : row,
                    col     : col,
                    record  : r
                });
                this.lastEdit = {
                    row: row,
                    col: col
                };
                this.activeEditor = ed;
                
                
                ed.selectSameEditor = (this.activeEditor == this.lastActiveEditor);
                var v = this.preEditValue(r, field);
                ed.startEdit(this.view.getCell(row, col).firstChild, Ext.isDefined(v) ? v : '');

                
                (function(){
                    delete ed.selectSameEditor;
                }).defer(50);
            }
        }
    },

    
    preEditValue : function(r, field){
        var value = r.data[field];
        return this.autoEncode && Ext.isString(value) ? Ext.util.Format.htmlDecode(value) : value;
    },

    
    postEditValue : function(value, originalValue, r, field){
        return this.autoEncode && Ext.isString(value) ? Ext.util.Format.htmlEncode(value) : value;
    },

    
    stopEditing : function(cancel){
        if(this.editing){
            
            var ae = this.lastActiveEditor = this.activeEditor;
            if(ae){
                ae[cancel === true ? 'cancelEdit' : 'completeEdit']();
                this.view.focusCell(ae.row, ae.col);
            }
            this.activeEditor = null;
        }
        this.editing = false;
    }
});
Ext.reg('editorgrid', Ext.grid.EditorGridPanel);

Ext.grid.GridEditor = function(field, config){
    Ext.grid.GridEditor.superclass.constructor.call(this, field, config);
    field.monitorTab = false;
};

Ext.extend(Ext.grid.GridEditor, Ext.Editor, {
    alignment: "tl-tl",
    autoSize: "width",
    hideEl : false,
    cls: "x-small-editor x-grid-editor",
    shim:false,
    shadow:false
});
Ext.grid.PropertyRecord = Ext.data.Record.create([
    {name:'name',type:'string'}, 'value'
]);


Ext.grid.PropertyStore = Ext.extend(Ext.util.Observable, {
    
    constructor : function(grid, source){
        this.grid = grid;
        this.store = new Ext.data.Store({
            recordType : Ext.grid.PropertyRecord
        });
        this.store.on('update', this.onUpdate,  this);
        if(source){
            this.setSource(source);
        }
        Ext.grid.PropertyStore.superclass.constructor.call(this);    
    },
    
    
    setSource : function(o){
        this.source = o;
        this.store.removeAll();
        var data = [];
        for(var k in o){
            if(this.isEditableValue(o[k])){
                data.push(new Ext.grid.PropertyRecord({name: k, value: o[k]}, k));
            }
        }
        this.store.loadRecords({records: data}, {}, true);
    },

    
    onUpdate : function(ds, record, type){
        if(type == Ext.data.Record.EDIT){
            var v = record.data.value;
            var oldValue = record.modified.value;
            if(this.grid.fireEvent('beforepropertychange', this.source, record.id, v, oldValue) !== false){
                this.source[record.id] = v;
                record.commit();
                this.grid.fireEvent('propertychange', this.source, record.id, v, oldValue);
            }else{
                record.reject();
            }
        }
    },

    
    getProperty : function(row){
       return this.store.getAt(row);
    },

    
    isEditableValue: function(val){
        return Ext.isPrimitive(val) || Ext.isDate(val);
    },

    
    setValue : function(prop, value, create){
        var r = this.getRec(prop);
        if(r){
            r.set('value', value);
            this.source[prop] = value;
        }else if(create){
            
            this.source[prop] = value;
            r = new Ext.grid.PropertyRecord({name: prop, value: value}, prop);
            this.store.add(r);

        }
    },
    
    
    remove : function(prop){
        var r = this.getRec(prop);
        if(r){
            this.store.remove(r);
            delete this.source[prop];
        }
    },
    
    
    getRec : function(prop){
        return this.store.getById(prop);
    },

    
    getSource : function(){
        return this.source;
    }
});


Ext.grid.PropertyColumnModel = Ext.extend(Ext.grid.ColumnModel, {
    
    nameText : 'Name',
    valueText : 'Value',
    dateFormat : 'm/j/Y',
    trueText: 'true',
    falseText: 'false',
    
    constructor : function(grid, store){
        var g = Ext.grid,
	        f = Ext.form;
	        
	    this.grid = grid;
	    g.PropertyColumnModel.superclass.constructor.call(this, [
	        {header: this.nameText, width:50, sortable: true, dataIndex:'name', id: 'name', menuDisabled:true},
	        {header: this.valueText, width:50, resizable:false, dataIndex: 'value', id: 'value', menuDisabled:true}
	    ]);
	    this.store = store;
	
	    var bfield = new f.Field({
	        autoCreate: {tag: 'select', children: [
	            {tag: 'option', value: 'true', html: this.trueText},
	            {tag: 'option', value: 'false', html: this.falseText}
	        ]},
	        getValue : function(){
	            return this.el.dom.value == 'true';
	        }
	    });
	    this.editors = {
	        'date' : new g.GridEditor(new f.DateField({selectOnFocus:true})),
	        'string' : new g.GridEditor(new f.TextField({selectOnFocus:true})),
	        'number' : new g.GridEditor(new f.NumberField({selectOnFocus:true, style:'text-align:left;'})),
	        'boolean' : new g.GridEditor(bfield, {
	            autoSize: 'both'
	        })
	    };
	    this.renderCellDelegate = this.renderCell.createDelegate(this);
	    this.renderPropDelegate = this.renderProp.createDelegate(this);
    },

    
    renderDate : function(dateVal){
        return dateVal.dateFormat(this.dateFormat);
    },

    
    renderBool : function(bVal){
        return this[bVal ? 'trueText' : 'falseText'];
    },

    
    isCellEditable : function(colIndex, rowIndex){
        return colIndex == 1;
    },

    
    getRenderer : function(col){
        return col == 1 ?
            this.renderCellDelegate : this.renderPropDelegate;
    },

    
    renderProp : function(v){
        return this.getPropertyName(v);
    },

    
    renderCell : function(val, meta, rec){
        var renderer = this.grid.customRenderers[rec.get('name')];
        if(renderer){
            return renderer.apply(this, arguments);
        }
        var rv = val;
        if(Ext.isDate(val)){
            rv = this.renderDate(val);
        }else if(typeof val == 'boolean'){
            rv = this.renderBool(val);
        }
        return Ext.util.Format.htmlEncode(rv);
    },

    
    getPropertyName : function(name){
        var pn = this.grid.propertyNames;
        return pn && pn[name] ? pn[name] : name;
    },

    
    getCellEditor : function(colIndex, rowIndex){
        var p = this.store.getProperty(rowIndex),
            n = p.data.name, 
            val = p.data.value;
        if(this.grid.customEditors[n]){
            return this.grid.customEditors[n];
        }
        if(Ext.isDate(val)){
            return this.editors.date;
        }else if(typeof val == 'number'){
            return this.editors.number;
        }else if(typeof val == 'boolean'){
            return this.editors['boolean'];
        }else{
            return this.editors.string;
        }
    },

    
    destroy : function(){
        Ext.grid.PropertyColumnModel.superclass.destroy.call(this);
        for(var ed in this.editors){
            Ext.destroy(this.editors[ed]);
        }
    }
});


Ext.grid.PropertyGrid = Ext.extend(Ext.grid.EditorGridPanel, {
    
    
    
    
    

    
    enableColumnMove:false,
    stripeRows:false,
    trackMouseOver: false,
    clicksToEdit:1,
    enableHdMenu : false,
    viewConfig : {
        forceFit:true
    },

    
    initComponent : function(){
        this.customRenderers = this.customRenderers || {};
        this.customEditors = this.customEditors || {};
        this.lastEditRow = null;
        var store = new Ext.grid.PropertyStore(this);
        this.propStore = store;
        var cm = new Ext.grid.PropertyColumnModel(this, store);
        store.store.sort('name', 'ASC');
        this.addEvents(
            
            'beforepropertychange',
            
            'propertychange'
        );
        this.cm = cm;
        this.ds = store.store;
        Ext.grid.PropertyGrid.superclass.initComponent.call(this);

		this.mon(this.selModel, 'beforecellselect', function(sm, rowIndex, colIndex){
            if(colIndex === 0){
                this.startEditing.defer(200, this, [rowIndex, 1]);
                return false;
            }
        }, this);
    },

    
    onRender : function(){
        Ext.grid.PropertyGrid.superclass.onRender.apply(this, arguments);

        this.getGridEl().addClass('x-props-grid');
    },

    
    afterRender: function(){
        Ext.grid.PropertyGrid.superclass.afterRender.apply(this, arguments);
        if(this.source){
            this.setSource(this.source);
        }
    },

    
    setSource : function(source){
        this.propStore.setSource(source);
    },

    
    getSource : function(){
        return this.propStore.getSource();
    },
    
    
    setProperty : function(prop, value, create){
        this.propStore.setValue(prop, value, create);    
    },
    
    
    removeProperty : function(prop){
        this.propStore.remove(prop);
    }

    
    
    
    
});
Ext.reg("propertygrid", Ext.grid.PropertyGrid);

Ext.grid.GroupingView = Ext.extend(Ext.grid.GridView, {

    
    groupByText : 'Group By This Field',
    
    showGroupsText : 'Show in Groups',
    
    hideGroupedColumn : false,
    
    showGroupName : true,
    
    startCollapsed : false,
    
    enableGrouping : true,
    
    enableGroupingMenu : true,
    
    enableNoGroups : true,
    
    emptyGroupText : '(None)',
    
    ignoreAdd : false,
    
    groupTextTpl : '{text}',

    
    groupMode: 'value',

    

    
    initTemplates : function(){
        Ext.grid.GroupingView.superclass.initTemplates.call(this);
        this.state = {};

        var sm = this.grid.getSelectionModel();
        sm.on(sm.selectRow ? 'beforerowselect' : 'beforecellselect',
                this.onBeforeRowSelect, this);

        if(!this.startGroup){
            this.startGroup = new Ext.XTemplate(
                '<div id="{groupId}" class="x-grid-group {cls}">',
                    '<div id="{groupId}-hd" class="x-grid-group-hd" style="{style}"><div class="x-grid-group-title">', this.groupTextTpl ,'</div></div>',
                    '<div id="{groupId}-bd" class="x-grid-group-body">'
            );
        }
        this.startGroup.compile();

        if (!this.endGroup) {
            this.endGroup = '</div></div>';
        }
    },

    
    findGroup : function(el){
        return Ext.fly(el).up('.x-grid-group', this.mainBody.dom);
    },

    
    getGroups : function(){
        return this.hasRows() ? this.mainBody.dom.childNodes : [];
    },

    
    onAdd : function(ds, records, index) {
        if (this.canGroup() && !this.ignoreAdd) {
            var ss = this.getScrollState();
            this.fireEvent('beforerowsinserted', ds, index, index + (records.length-1));
            this.refresh();
            this.restoreScroll(ss);
            this.fireEvent('rowsinserted', ds, index, index + (records.length-1));
        } else if (!this.canGroup()) {
            Ext.grid.GroupingView.superclass.onAdd.apply(this, arguments);
        }
    },

    
    onRemove : function(ds, record, index, isUpdate){
        Ext.grid.GroupingView.superclass.onRemove.apply(this, arguments);
        var g = document.getElementById(record._groupId);
        if(g && g.childNodes[1].childNodes.length < 1){
            Ext.removeNode(g);
        }
        this.applyEmptyText();
    },

    
    refreshRow : function(record){
        if(this.ds.getCount()==1){
            this.refresh();
        }else{
            this.isUpdating = true;
            Ext.grid.GroupingView.superclass.refreshRow.apply(this, arguments);
            this.isUpdating = false;
        }
    },

    
    beforeMenuShow : function(){
        var item, items = this.hmenu.items, disabled = this.cm.config[this.hdCtxIndex].groupable === false;
        if((item = items.get('groupBy'))){
            item.setDisabled(disabled);
        }
        if((item = items.get('showGroups'))){
            item.setDisabled(disabled);
            item.setChecked(this.enableGrouping, true);
        }
    },

    
    renderUI : function(){
        Ext.grid.GroupingView.superclass.renderUI.call(this);
        this.mainBody.on('mousedown', this.interceptMouse, this);

        if(this.enableGroupingMenu && this.hmenu){
            this.hmenu.add('-',{
                itemId:'groupBy',
                text: this.groupByText,
                handler: this.onGroupByClick,
                scope: this,
                iconCls:'x-group-by-icon'
            });
            if(this.enableNoGroups){
                this.hmenu.add({
                    itemId:'showGroups',
                    text: this.showGroupsText,
                    checked: true,
                    checkHandler: this.onShowGroupsClick,
                    scope: this
                });
            }
            this.hmenu.on('beforeshow', this.beforeMenuShow, this);
        }
    },

    processEvent: function(name, e){
        Ext.grid.GroupingView.superclass.processEvent.call(this, name, e);
        var hd = e.getTarget('.x-grid-group-hd', this.mainBody);
        if(hd){
            
            var field = this.getGroupField(),
                prefix = this.getPrefix(field),
                groupValue = hd.id.substring(prefix.length);

            
            groupValue = groupValue.substr(0, groupValue.length - 3);
            if(groupValue){
                this.grid.fireEvent('group' + name, this.grid, field, groupValue, e);
            }
        }

    },

    
    onGroupByClick : function(){
        this.enableGrouping = true;
        this.grid.store.groupBy(this.cm.getDataIndex(this.hdCtxIndex));
        this.grid.fireEvent('groupchange', this, this.grid.store.getGroupState());
        this.beforeMenuShow(); 
        this.refresh();
    },

    
    onShowGroupsClick : function(mi, checked){
        this.enableGrouping = checked;
        if(checked){
            this.onGroupByClick();
        }else{
            this.grid.store.clearGrouping();
            this.grid.fireEvent('groupchange', this, null);
        }
    },

    
    toggleRowIndex : function(rowIndex, expanded){
        if(!this.canGroup()){
            return;
        }
        var row = this.getRow(rowIndex);
        if(row){
            this.toggleGroup(this.findGroup(row), expanded);
        }
    },

    
    toggleGroup : function(group, expanded){
        var gel = Ext.get(group);
        expanded = Ext.isDefined(expanded) ? expanded : gel.hasClass('x-grid-group-collapsed');
        if(this.state[gel.id] !== expanded){
            this.grid.stopEditing(true);
            this.state[gel.id] = expanded;
            gel[expanded ? 'removeClass' : 'addClass']('x-grid-group-collapsed');
        }
    },

    
    toggleAllGroups : function(expanded){
        var groups = this.getGroups();
        for(var i = 0, len = groups.length; i < len; i++){
            this.toggleGroup(groups[i], expanded);
        }
    },

    
    expandAllGroups : function(){
        this.toggleAllGroups(true);
    },

    
    collapseAllGroups : function(){
        this.toggleAllGroups(false);
    },

    
    interceptMouse : function(e){
        var hd = e.getTarget('.x-grid-group-hd', this.mainBody);
        if(hd){
            e.stopEvent();
            this.toggleGroup(hd.parentNode);
        }
    },

    
    getGroup : function(v, r, groupRenderer, rowIndex, colIndex, ds){
        var g = groupRenderer ? groupRenderer(v, {}, r, rowIndex, colIndex, ds) : String(v);
        if(g === '' || g === '&#160;'){
            g = this.cm.config[colIndex].emptyGroupText || this.emptyGroupText;
        }
        return g;
    },

    
    getGroupField : function(){
        return this.grid.store.getGroupState();
    },

    
    afterRender : function(){
        if(!this.ds || !this.cm){
            return;
        }
        Ext.grid.GroupingView.superclass.afterRender.call(this);
        if(this.grid.deferRowRender){
            this.updateGroupWidths();
        }
    },

    
    renderRows : function(){
        var groupField = this.getGroupField();
        var eg = !!groupField;
        
        if(this.hideGroupedColumn) {
            var colIndex = this.cm.findColumnIndex(groupField),
                hasLastGroupField = Ext.isDefined(this.lastGroupField);
            if(!eg && hasLastGroupField){
                this.mainBody.update('');
                this.cm.setHidden(this.cm.findColumnIndex(this.lastGroupField), false);
                delete this.lastGroupField;
            }else if (eg && !hasLastGroupField){
                this.lastGroupField = groupField;
                this.cm.setHidden(colIndex, true);
            }else if (eg && hasLastGroupField && groupField !== this.lastGroupField) {
                this.mainBody.update('');
                var oldIndex = this.cm.findColumnIndex(this.lastGroupField);
                this.cm.setHidden(oldIndex, false);
                this.lastGroupField = groupField;
                this.cm.setHidden(colIndex, true);
            }
        }
        return Ext.grid.GroupingView.superclass.renderRows.apply(
                    this, arguments);
    },

    
    doRender : function(cs, rs, ds, startRow, colCount, stripe){
        if(rs.length < 1){
            return '';
        }

        if(!this.canGroup() || this.isUpdating){
            return Ext.grid.GroupingView.superclass.doRender.apply(this, arguments);
        }

        var groupField = this.getGroupField(),
            colIndex = this.cm.findColumnIndex(groupField),
            g,
            gstyle = 'width:' + this.getTotalWidth() + ';',
            cfg = this.cm.config[colIndex],
            groupRenderer = cfg.groupRenderer || cfg.renderer,
            prefix = this.showGroupName ? (cfg.groupName || cfg.header)+': ' : '',
            groups = [],
            curGroup, i, len, gid;

        for(i = 0, len = rs.length; i < len; i++){
            var rowIndex = startRow + i,
                r = rs[i],
                gvalue = r.data[groupField];

                g = this.getGroup(gvalue, r, groupRenderer, rowIndex, colIndex, ds);
            if(!curGroup || curGroup.group != g){
                gid = this.constructId(gvalue, groupField, colIndex);
                
                
                this.state[gid] = !(Ext.isDefined(this.state[gid]) ? !this.state[gid] : this.startCollapsed);
                curGroup = {
                    group: g,
                    gvalue: gvalue,
                    text: prefix + g,
                    groupId: gid,
                    startRow: rowIndex,
                    rs: [r],
                    cls: this.state[gid] ? '' : 'x-grid-group-collapsed',
                    style: gstyle
                };
                groups.push(curGroup);
            }else{
                curGroup.rs.push(r);
            }
            r._groupId = gid;
        }

        var buf = [];
        for(i = 0, len = groups.length; i < len; i++){
            g = groups[i];
            this.doGroupStart(buf, g, cs, ds, colCount);
            buf[buf.length] = Ext.grid.GroupingView.superclass.doRender.call(
                    this, cs, g.rs, ds, g.startRow, colCount, stripe);

            this.doGroupEnd(buf, g, cs, ds, colCount);
        }
        return buf.join('');
    },

    
    getGroupId : function(value){
        var field = this.getGroupField();
        return this.constructId(value, field, this.cm.findColumnIndex(field));
    },

    
    constructId : function(value, field, idx){
        var cfg = this.cm.config[idx],
            groupRenderer = cfg.groupRenderer || cfg.renderer,
            val = (this.groupMode == 'value') ? value : this.getGroup(value, {data:{}}, groupRenderer, 0, idx, this.ds);

        return this.getPrefix(field) + Ext.util.Format.htmlEncode(val);
    },

    
    canGroup  : function(){
        return this.enableGrouping && !!this.getGroupField();
    },

    
    getPrefix: function(field){
        return this.grid.getGridEl().id + '-gp-' + field + '-';
    },

    
    doGroupStart : function(buf, g, cs, ds, colCount){
        buf[buf.length] = this.startGroup.apply(g);
    },

    
    doGroupEnd : function(buf, g, cs, ds, colCount){
        buf[buf.length] = this.endGroup;
    },

    
    getRows : function(){
        if(!this.canGroup()){
            return Ext.grid.GroupingView.superclass.getRows.call(this);
        }
        var r = [],
            gs = this.getGroups(),
            g,
            i = 0,
            len = gs.length,
            j,
            jlen;
        for(; i < len; ++i){
            g = gs[i].childNodes[1];
            if(g){
                g = g.childNodes;
                for(j = 0, jlen = g.length; j < jlen; ++j){
                    r[r.length] = g[j];
                }
            }
        }
        return r;
    },

    
    updateGroupWidths : function(){
        if(!this.canGroup() || !this.hasRows()){
            return;
        }
        var tw = Math.max(this.cm.getTotalWidth(), this.el.dom.offsetWidth-this.getScrollOffset()) +'px';
        var gs = this.getGroups();
        for(var i = 0, len = gs.length; i < len; i++){
            gs[i].firstChild.style.width = tw;
        }
    },

    
    onColumnWidthUpdated : function(col, w, tw){
        Ext.grid.GroupingView.superclass.onColumnWidthUpdated.call(this, col, w, tw);
        this.updateGroupWidths();
    },

    
    onAllColumnWidthsUpdated : function(ws, tw){
        Ext.grid.GroupingView.superclass.onAllColumnWidthsUpdated.call(this, ws, tw);
        this.updateGroupWidths();
    },

    
    onColumnHiddenUpdated : function(col, hidden, tw){
        Ext.grid.GroupingView.superclass.onColumnHiddenUpdated.call(this, col, hidden, tw);
        this.updateGroupWidths();
    },

    
    onLayout : function(){
        this.updateGroupWidths();
    },

    
    onBeforeRowSelect : function(sm, rowIndex){
        this.toggleRowIndex(rowIndex, true);
    }
});

Ext.grid.GroupingView.GROUP_ID = 1000;
