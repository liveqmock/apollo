/***********************************************************************
 * (C) SCC Informationssysteme GmBH 2000 - 2003                        *
 ***********************************************************************/

/***********************************************************************
 * Name:
 *        TabSet.js
 *
 * Function:
 *        Provide a Dynamic TabSet in a web browser.
 *
 * Author:
 *        G Schulz
 *
 * Status:
 *        Version 1, Release 1
 *
 * Environment:
 *        This is a PLATFORM-INDEPENDENT source file. As such it may
 *        contain no dependencies on any specific operating system
 *        environment or hardware platform.
 *
 * Description:
 *        ...
 *
 * TESTED ON:  - InternetExplorer   > 5.0
 *             - Netscape Navigator > 7.0
 *
 ***********************************************************************/


/*
+ ---------------------------------------------------------------------------------+
| Object....: Tab (id, label, selected, tooltip, disabled)
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 04.03.2003  G.Schulz (SCC)    first version
| 27.12.2003  G.Schulz (SCC)    Height, Width for the additinal icon added
|
+ ---------------------------------------------------------------------------------+
*/
function Tab(id, label, selected, tooltip, disabled, icon, iconwidth, iconheight) {
	// Note: if changed the constructor of TabSet_addTab(...) must also be changed!
	this.id         = id;	                                           // identifier for the tab
	this.parent     = null;                                            // the TabSet object containing the Tab (parent)
	this.label      = label;                                           // text to display on the tab
	this.selected   = (arguments.length >= 3) ? selected : false;      // indicates that the tab is selected
	this.tooltip    = tooltip;                                         // ToolTip
	this.disabled   = (arguments.length >= 5) ? disabled : false;      // false if the Tabe can not be selected
	this.icon       = icon;                                            // optional icon in front of the tab
	this.iconwidth  = iconwidth;                                       // the width of the optional icon
	this.iconheight = iconheight;                                      // the height of the optional icon
}
function Tab_getId() {
	return this.id;
}
function Tab_getLabel() {
	return this.label;
}
function Tab_getTooltip() {
	return this.tooltip;
}
function Tab_isDisabled() {
	return this.disabled;
}
function Tab_isSelected() {
	return this.selected;
}
function Tab_setParent(obj) {
	if (obj instanceof TabSet) {
		this.parent = obj;
	}
}
function Tab_toString() {
	var out = '';
	out += '******* TabSet *********' + LF
	out += 'Id.........: ' + this.id + LF;
	out += 'Label......: ' + this.label + LF;
	out += 'Selected...: ' + this.selected + LF;
	out += 'Tooltip....: ' + this.tooltip + LF;
	out += 'Disabled...: ' + this.disabled + LF;
	out += 'Icon.......: ' + this.icon + LF;
	out += 'Icon width.: ' + this.iconwidth + LF;
	out += 'Icon height: ' + this.iconheight;
	return out;
}
new Tab();
Tab.prototype.getId        = Tab_getId;
Tab.prototype.getLabel     = Tab_getLabel;
Tab.prototype.getTooltip   = Tab_getTooltip;
Tab.prototype.isSelected   = Tab_isSelected;
Tab.prototype.setParent    = Tab_setParent;
Tab.prototype.isDisabled   = Tab_isDisabled;
Tab.prototype.toString     = Tab_toString;


/*
+ ---------------------------------------------------------------------------------+
| Object.....: TabSet (id)
| Function...: Generates a TabSet Object
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 04.03.2003  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function TabSet(id) {
	this.id        = id;                       // Id of the TabSets
	this.tabs      = new Array();              // Buffer to hold the tab pages
	this.length    = 0;                        // number of tap pages
	this.runAt     = RunAt.SERVER;             // indicates if a click on a tab page should make a server roundtrip or not.
}
function TabSet_getId() {
	return this.id;
}
function TabSet_addTab(obj, label, selected, tooltip, disabled, icon, iconwidth, iconheight) {
	var tab = null;

	// Workaround for Function Overloading in JS
	if (obj instanceof Tab) {
		tab = obj;
	} else {
		var id = obj;
		tab = new Tab(id, label, selected, tooltip, disabled, icon, iconwidth, iconheight)
	}
	
//	this.tabs.push(tab);
	this.tabs[this.tabs.length] = tab;
	tab.setParent(this);
	this.length = this.tabs.length;
}
function TabSet_getTab(index) {
	// Check overflow
	if (index < 0 || index > this.length) {
		return null;
	} else {
		return this.tabs[index];
	}
}
function TabSet_selectTab(index) {
	// check Index
	if (index < 0 || index > this.length) {
		return;
	}
	// select the tab
	for (var i=0; i < this.length; i++) {
		this.tabs[i]['selected'] = (index == i) ? true : false;
	}
}
function TabSet_selectTabById(id) {
	for (var i=0; i < this.length; i++) {
		this.tabs[i]['selected'] = (this.tabs[i]['id'] == id) ? true : false;
	}
}
function TabSet_getSelectedIndex() {
	for (var i=0; i < this.length; i++) {
		if (this.tabs[i]['selected'] == true) return i;
	}
	return -1;
}
function TabSet_setRunAt(obj) {
	this.runAt = obj;
}
function TabSet_toString() {
	var out = '';
	out += '******* TabSet *********' + LF
	out += 'Id.........: ' + this.id + LF;
	out += 'Length.....: ' + this.length + LF;
	out += 'RunAt......: ' + this.runAt;
	return out;
}
new TabSet();
TabSet.prototype.getId				= TabSet_getId;
TabSet.prototype.selectTab			= TabSet_selectTab;
TabSet.prototype.selectTabById		= TabSet_selectTabById;
TabSet.prototype.getSelectedIndex	= TabSet_getSelectedIndex;
TabSet.prototype.addTab				= TabSet_addTab;
TabSet.prototype.getTab				= TabSet_getTab;
TabSet.prototype.setRunAt			= TabSet_setRunAt;
TabSet.prototype.toString			= TabSet_toString;


/*
+ ---------------------------------------------------------------------------------+
| Object....: TabSetPainterData(tabSet, resPath, bgColor, maxVisibleTabs, maxLabelLength)
| Fubction..: Holds the Information needed by the TabSetPainter to draw a TabSet
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 04.03.2003  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function TabSetPainterData(tabSet, resPath, bgColor, maxVisibleTabs, maxLabelLength) {
	this.TAB_BGCOLOR	    = bgColor;	//'C4C8C9'; see stylesheet
	RESPATH                 = resPath;  //'fw/def/image/tab/';
	LABEL_SUFFIX            = '..';
	// Prefix for DIV-Tag
	DIV_PREVIX              = 'tabset_';
	// StyleSheet
	CSS_TABLE	= 'tsc';
	// Scroll Butttons
	ARROW_LEFT              = RESPATH + 'btnArrow_left1.gif';
	ARROW_RIGHT             = RESPATH + 'btnArrow_right1.gif';
	// Scroll Buttons deaktiviert
	ARROW_LEFT_DISABLED     = RESPATH + 'btnArrow_left2.gif';
	ARROW_RIGHT_DISABLED    = RESPATH + 'btnArrow_right2.gif';
	// More-Tabs Image
	TAB_PREV                = RESPATH + 'tabMoreL.gif';
	TAB_PREV_EMPTY          = RESPATH + 'tabMoreL_empty.gif';
	TAB_NEXT                = RESPATH + 'tabMoreR.gif';
	// Tab unselektiert
	TAB_L                   = RESPATH + 'tabL.gif';
	TAB_R                   = RESPATH + 'tabR.gif';
	TAB_BG                  = RESPATH + 'tabBg.gif';
	// Tab selektiert
	this.TAB_LSEL           = RESPATH + 'tabLSel_'  + this.TAB_BGCOLOR + '.gif';
	this.TAB_RSEL           = RESPATH + 'tabRSel_'  + this.TAB_BGCOLOR + '.gif';
	this.TAB_BGSEL          = RESPATH + 'tabBgSel_' + this.TAB_BGCOLOR + '.gif';
	// Tab disabled
	TAB_DISL                = RESPATH + 'tabDisL.gif';
	TAB_DISR                = RESPATH + 'tabDisR.gif';
	TAB_DISBG               = RESPATH + 'tabDisBg.gif';

	this.tabSet             = tabSet;               // TabSet object containing the tab pages
	this.maxVisibleTabs     = maxVisibleTabs;       // maximal number of tabpages to show
	this.maxLabelLength     = maxLabelLength;       // maximal length of the labels on the tab page
	this.currentPos         = -1;                   // actual position when browsing the tabset
	
	// Init-Block. Only initialize when
	// the Object is realy used.
	if (null != tabSet) {
		
		// Bei RunAtClient
		this.currentPos     = (-1 == tabSet.getSelectedIndex()) ? 0 : tabSet.getSelectedIndex();
		// Bei RunAtServer
		// aus hiddendata
		
		this.maxLabelLength = (-1 == maxLabelLength) ? 900 : maxLabelLength;
		this.maxVisibleTabs = (-1 == maxVisibleTabs) ? tabSet.length : maxVisibleTabs;
	}
}
new TabSetPainterData();


/*
+ ---------------------------------------------------------------------------------+
| Object....: TabSetPainter();
| Function..: Responsible for drawing the TabSet
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 04.03.2003  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function TabSetPainter() {}
function TabSetPainter_browse(tspData, direction) {
	var pos = tspData['currentPos'];
	var tabSet = tspData['tabSet'];
	var maxVisibleTabs = tspData['maxVisibleTabs'];
	
	if (direction.toUpperCase() == 'PREV') {
		tspData['currentPos'] = (pos - 1 <= 0) ? 0 : pos - 1;
	}
	else if (direction.toUpperCase() == 'NEXT') {
		tspData['currentPos'] = (pos + maxVisibleTabs + 1 > tabSet.length) ? pos : pos + 1;
	}
	// neu zeichnen
	this.render(tspData);
}
function TabSetPainter_render(tspData) {
	var maxVisibleTabs	= tspData['maxVisibleTabs'];
	var maxLabelLength	= tspData['maxLabelLength'];
	var tabSet			= tspData['tabSet'];
	var	currentPos		= tspData['currentPos'];
	
	// create Documentfragment
	var doc = document.createDocumentFragment();
	
	//create Table
	var table = document.createElement('Table');
	table.cellSpacing = 0;
	table.cellPadding = 0;
	table.border = 0;
	table.className = CSS_TABLE;

	doc.appendChild(table);

	// create Row
	var row = table.insertRow(0);

	this.createScrollButtons(row, tspData, 'L');
	this.createScrollButtons(row, tspData, 'R');
	this.createImgNodeMore(row, tspData, 'L');
	
	var first = (tabSet.length <= maxVisibleTabs) ? 0 : currentPos;
	var last = (currentPos + maxVisibleTabs >= tabSet.length) ? tabSet.length: currentPos + maxVisibleTabs;
	
	// Create a new TabSet
	for (var i=first; i < last; i++) {
		var tab = tabSet.getTab(i);
		this.createImgNode(row, tab, tspData, 'L');
		this.createTabNode(row, tab, tspData);
		this.createImgNode(row, tab, tspData, 'R');
	}
	
	this.createImgNodeMore(row, tspData, 'R');
	//this.createScrollButtons(row, tspData, 'R');
	this.createDetailNode(row, tspData);
	
	// get the DIV-Element from the form, where the
	// TabSet should be inserted / replaced
	var div = this.getTabSetNode(tabSet.getId());
	if ( div.hasChildNodes() ) {
		//	div.replaceChild(doc, div.childNodes[0]);  // Problems if used with SSL and IE
		div.innerHTML = "";
		div.appendChild(table);
	} else {
		div.appendChild(table);
	}
	
	// Falls das Tabset nur ClientSeitig arbeiten soll
	// muessen noch alle uebrigen Tabs ausgeblendt werden.
	if (tabSet.runAt == RunAt.CLIENT) {
		TabSetPainter.displayTab(tabSet.getId(), tabSet.getTab(tabSet.getSelectedIndex()).getId());
	}
}
function TabSetPainter_createTabNode(row, tab, tspData) {
	var cell = null;
	var bgImage = '';
	var className = '';
	var maxLabelLength = tspData['maxLabelLength'];
	
	if (tab.isDisabled()) {
		bgImage = TAB_DISBG;
		className = 'disabled';
	} else {
		bgImage = tab.isSelected() ? tspData['TAB_BGSEL'] : TAB_BG;
		className = tab.isSelected() ? 'sel' : 'unsel';
	}
	
	// Draw an optional icon in front of the Text in the Tab
	if (null != tab['icon']) {
		cell = row.insertCell(row.cells.length);
		var icon = tab['icon'];
		var width = tab['iconwidth'];
		var height = tab['iconheight'];

		var img = document.createElement('Image');
		img.setAttribute('src', icon);
		img.setAttribute('border', 0);
		img.setAttribute('width', width);
		img.setAttribute('height', height);
		cell.setAttribute('background', bgImage);
		cell.setAttribute('align', 'middle');
		cell.appendChild(img);
		cell.style.paddingRight='3px';	// matchs &nbsp;
	}
	
	// Draw the Label
	cell = row.insertCell(row.cells.length);
	cell.setAttribute('background', bgImage);
	cell.noWrap = true;
	cell.className = className;

	var span = document.createElement('Span');
	span.title = (tab.getTooltip()!= null) ? tab.getTooltip() : tab.getLabel();
	span.onclick = function() {
			var tabSet = tspData['tabSet'];
			var tabSetId = tab.parent.getId();
			// Hidden Field fuer eventuellen ServerRoundtrip updaten
			var hidden = document.getElementById(tabSetId);
			if (null != hidden) {
				hidden.value= tab.getId(); // + '.' + tspData['currentPos'];
			}

			// Aktuelle Position in Hidden Field ablegen, damit
			// die neu selektierte Tabe an der gleichen stelle 
			// geoffnet werden kann.
			// ToDo .... 

			if (tabSet.runAt == RunAt.SERVER) {
				// if a server roundtrip is required, we must submit the Form
				var form = TabSetPainter.getFormElement(document.getElementById(DIV_PREVIX + tabSetId));
				
				if (null != form) {
					// set the request parameter to identify the tab, so that the
					// eventhandler can be called
					form.action += '?ctrl=' + tabSet.getId() + '&action=TabClick&param=' + tab.getId();
					form.submit();
				} else {
					var msg = ''
					msg += 'Error: TabSet ' + tabSet.getId() + ' is not embedded in a form!\n';
					msg += 'If you want to use the client side scrolling feature you must\n';
					msg += 'embedd the TabSetControl in a HTML-Form!';
					alert(msg);
				}
			} else if (tabSet.runAt == RunAt.CLIENT) {
				// TabSetLeiste neu zeichnen und nicht
				// angezeigte Tabs ausblenden
				tabSet.selectTabById(tab.getId());
				TabSetPainter.render(tspData);
			}
		};
	span.appendChild(document.createTextNode(this.clipLabel(tab, maxLabelLength)));
	cell.appendChild(span);
}
function TabSetPainter_clipLabel(tab, maxLabelLength) {
	var label = tab.getLabel();

	if (label.length > (maxLabelLength + 2) ) {
		label = label.slice(0, maxLabelLength) + LABEL_SUFFIX;
	}
	return label;
}
function TabSetPainter_createImgNode(row, tab, tspData, side) {
	var img = document.createElement('Img');

	if (side.toUpperCase() == 'R' && tab.isDisabled()) {
		img.src = TAB_DISR;
	}
	else if (side.toUpperCase() == 'L' && tab.isDisabled()) {
		img.src = TAB_DISL;
	}
	else if (side.toUpperCase() == 'R' && tab.isSelected()) {
		img.src = tspData['TAB_RSEL'];
	}
	else if (side.toUpperCase() == 'R' && !tab.isSelected()) {
		img.src = TAB_R;
	}
	else if (side.toUpperCase() == 'L' && tab.isSelected()) {
		img.src = tspData['TAB_LSEL'];
	}
	else if (side.toUpperCase() == 'L' && !tab.isSelected()) {
		img.src = TAB_L;
	}
	
	row.insertCell(row.cells.length).appendChild(img);
}
function TabSetPainter_createScrollButtons(row, tspData, side) {
	var maxVisibleTabs = tspData['maxVisibleTabs'];
	var maxLabelLength = tspData['maxLabelLength'];
	var currentPos     = tspData['currentPos'];
	var tabSet         = tspData['tabSet'];

	var imgBtn = document.createElement('Img');

	pos = (tabSet.length <= maxVisibleTabs) ? 0 : currentPos;
//	if (tabSet.length <= maxVisibleTabs) return;

	if (side.toUpperCase() == 'L' && pos > 0) {
		imgBtn.src = ARROW_LEFT;
		imgBtn.border = 0;
		imgBtn.id = 'btnNextTab_' + tabSet.getId();
		imgBtn.onclick = function() { TabSetPainter.browse(tspData, 'PREV'); };
		imgBtn.style.cursor = 'hand';
		row.insertCell(row.cells.length).appendChild(imgBtn);
	} else if (side.toUpperCase() == 'L' &&  maxVisibleTabs < tabSet.length){
		imgBtn.src = ARROW_LEFT_DISABLED;
		imgBtn.border = 0;
		row.insertCell(row.cells.length).appendChild(imgBtn);
	}
	
	if ((side.toUpperCase() == 'R') && (currentPos + maxVisibleTabs < tabSet.length) ) {
		imgBtn.src = ARROW_RIGHT;
		imgBtn.border = 0;
		imgBtn.id = 'btnPrevTab_' + tabSet.getId();
		imgBtn.onclick = function() { TabSetPainter.browse(tspData, 'NEXT'); };
		imgBtn.style.cursor = 'hand';
		var cell = row.insertCell(row.cells.length);
		cell.style.paddingLeft = '1px';
		cell.appendChild(imgBtn);
	} else if (side.toUpperCase() == 'R' && pos > 0) {
		imgBtn.src = ARROW_RIGHT_DISABLED;
		imgBtn.border = 0;
		var cell = row.insertCell(row.cells.length);
		cell.style.paddingLeft = '1px';
		cell.appendChild(imgBtn);
	}
}
function TabSetPainter_createImgNodeMore(row, tspData, side) {
	var maxVisibleTabs	= tspData['maxVisibleTabs'];
	var maxLabelLength	= tspData['maxLabelLength'];
	var currentPos		= tspData['currentPos'];
	var tabSet			= tspData['tabSet'];
	
	var imgTab = document.createElement('Img');
	
	pos = (tabSet.length <= maxVisibleTabs) ? 0 : currentPos;

	if (side.toUpperCase() == 'L' && pos > 0) {
		imgTab.src = TAB_PREV;
		imgTab.border = 0;
		var cell = row.insertCell(row.cells.length);
		cell.className = 'tabScrollBtnL';
		cell.appendChild(imgTab);
	} else if (side.toUpperCase() == 'L' &&  maxVisibleTabs < tabSet.length) {
		imgTab.src = TAB_PREV_EMPTY;
		imgTab.border = 0;
		var cell = row.insertCell(row.cells.length);
		cell.appendChild(imgTab);
	}
	
	if ((side.toUpperCase() == 'R') && (currentPos + maxVisibleTabs < tabSet.length) ) {
		imgTab.src = TAB_NEXT;
		imgTab.border = 0;
		var cell = row.insertCell(row.cells.length);
		cell.className = 'tabScrollBtnR';
		cell.appendChild(imgTab);
	}
}
function TabSetPainter_createDetailNode(row, tspData) {
	var tabSet = tspData['tabSet'];
	var maxVisibleTabs = tspData['maxVisibleTabs'];
	var currentPos = tspData['currentPos'];
	
	var start = currentPos + 1;
	var end = (currentPos + maxVisibleTabs > tabSet.length) ? tabSet.length : currentPos + maxVisibleTabs;
	var detail = start + '...' + end +' from ' + tabSet.length;
	
	if (tabSet.length <= maxVisibleTabs) return;
	
	var span = document.createElement('Span');
	span.appendChild(document.createTextNode(detail));
	span.className = 'tabDetail';
	
	var cell = row.insertCell(row.cells.length);
	cell.noWrap = true;
	cell.appendChild(span);
}
function TabSetPainter_getTabSetNode(id) {
	return document.getElementById(DIV_PREVIX + id);
}
function TabSetPainter_getFormElement(node) {
	// Search the Form wich embeds the DIV-Element
	var parent = node.parentNode;
	
	if (null == parent) return null;
	
	if (parent.nodeName == 'FORM' ) {
		return parent;
	} else {
		return arguments.callee(parent);
	}
}
function TabSetPainter_displayTab(tabSetId, tabId) {
	var spanId = 'tab_' + tabSetId + '_' + tabId;
	var nodes = document.getElementsByTagName('Span');

	for (var i=0; i < nodes.length; i++) {
		if (nodes[i].id == spanId) {
			nodes[i].style.display = 'block';
		}
		else if (nodes[i].id.indexOf('tab_' + tabSetId) != -1) {
			nodes[i].style.display = 'none';
		}
	}
}
function TabSetPainter_addOnClickHandler() {

}
new TabSetPainter();
TabSetPainter.browse               = TabSetPainter_browse;
TabSetPainter.render               = TabSetPainter_render;
TabSetPainter.clipLabel            = TabSetPainter_clipLabel;
TabSetPainter.createTabNode        = TabSetPainter_createTabNode;
TabSetPainter.createImgNode        = TabSetPainter_createImgNode;
TabSetPainter.createImgNodeMore    = TabSetPainter_createImgNodeMore;
TabSetPainter.createScrollButtons  = TabSetPainter_createScrollButtons;
TabSetPainter.getTabSetNode        = TabSetPainter_getTabSetNode;
TabSetPainter.createDetailNode     = TabSetPainter_createDetailNode;
TabSetPainter.getFormElement       = TabSetPainter_getFormElement;
TabSetPainter.displayTab           = TabSetPainter_displayTab;
