/***********************************************************************
 * (C) SCC Informationssysteme GmbH 2000 - 2003                        *
 ***********************************************************************/

var ie    = Browser.isIE();
var ns    = Browser.isNS();
var opera = Browser.isOpera();

/*
+ ---------------------------------------------------------------------------------+
| Purpose....: Initialize the eventhandlers needed by the common controls framework
|
| Date        Author            Notice
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function init() {
	// initialize Eventhandlers for MouseOver, MouseOut-Events
	setupEventHandler();
}


/*
+ ---------------------------------------------------------------------------------+
| Purpose....: Captures the events needed by the common controls framework and
|              registers the Eventhandlers
|
| Date        Author            Notice
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function setupEventHandler() {

	if (ie || opera) {
		// register Mouse-Handler in IE
		document.onmouseover = HandleMouseover;
		document.onmouseout  = HandleMouseout;
	} else if (ns) {
		// Event-Capturing in NS
		window.captureEvents(Event.MOUSEOVER | Event.MOUSEOUT);
		window.onmouseover = HandleMouseover;
		window.onmouseout  = HandleMouseout;
	}
}


/*
+ ---------------------------------------------------------------------------------+
| Purpose..:  Eventhandler for HandleMouseover
|             Changes the image for a button/menuitem, if the user moves the coursor
|             over the button/menuitem (element).
|             For this buttons must of type 'img' or 'input' and the id must start with "btn".
|             - the image (gif) with the ending 1.gif stands for an active element
|             - the image (gif) with the ending 3.gif stands for an active element hover
|             - the image (gif) with the ending 5.gif stands for a selected element
|             - the image (gif) with the ending 6.gif stands for a selected element (hover)
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function HandleMouseover(event) {
	var eSrc = null;
	if (ie || opera) {
		eSrc = window.event.srcElement;
	}
	else if(ns) {
		eSrc = event.target;
	}

	if (null == eSrc || null == eSrc.tagName ) return;

	if (("IMG" == eSrc.tagName.toUpperCase() || "INPUT" == eSrc.tagName.toUpperCase()) && eSrc.id.indexOf('btn') != -1) {
		if (eSrc.src.lastIndexOf('1.gif') != -1) {
			eSrc.src = eSrc.src.substr(0, eSrc.src.indexOf('.gif')-1) + '3.gif';
		}
		if (eSrc.src.lastIndexOf('5.gif') != -1) {
			eSrc.src = eSrc.src.substr(0, eSrc.src.indexOf('.gif')-1) + '6.gif';
		}
	}
}
/*
+ ---------------------------------------------------------------------------------+
| Purpose..:  Eventhandler for HandleMouseout
|             Changes the image for a button/menuitem, if the user moves the coursor
|             away from the button/menuitem (element).
|             For this buttons must of type 'img' or 'input' and the id must start with "btn".
|             - the image (gif) with the ending 1.gif stands for an active element
|             - the image (gif) with the ending 3.gif stands for an active element hover
|             - the image (gif) with the ending 5.gif stands for a selected element
|             - the image (gif) with the ending 6.gif stands for a selected element (hover)
|
| Date        Author            Notice
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function HandleMouseout(event) {
	var eSrc = null;
	if (ie || opera) {
		eSrc = window.event.srcElement;
	}
	else if(ns) {
		eSrc = event.target;
	}

	if (null == eSrc || null == eSrc.tagName ) return;
	
	if (("IMG" == eSrc.tagName.toUpperCase() || "INPUT" == eSrc.tagName.toUpperCase()) && eSrc.id.indexOf('btn') != -1) {
		// HOver only for unselected images
		if (eSrc.src.lastIndexOf('3.gif') != -1) {
			eSrc.src = eSrc.src.substr(0, eSrc.src.indexOf('.gif')-1) + '1.gif';
		}
		if (eSrc.src.lastIndexOf('6.gif') != -1) {
			eSrc.src = eSrc.src.substr(0, eSrc.src.indexOf('.gif')-1) + '5.gif';
		}
	}
}


/*
+ ---------------------------------------------------------------------------------+
| Purpose..:  Sets the color in a row of the Listcontrol if the cursors is moved
|             over the row.
|
| Date        Author            Notice
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function high(obj) {
	obj.style.background = 'rgb(220,232,236)';
	obj.style.border = '1px solid red';
}

function low(obj, isEvenLine) {
	if (obj.className == 'odd') {
		obj.style.background='white';
	}
	else if (obj.className == 'even') {
		obj.style.background='rgb(237,239,240)';
	}
}
