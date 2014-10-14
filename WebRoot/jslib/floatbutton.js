function floatButton(id, text, action, imageOff, imageOver,
	width, height, title, bgColor, fontFamily, fontSize, fontColor)
{
    this.id			= id;			//用于给这个button指定唯一ID，必须和实例名一致
    this.text		= text;			//button上的文字
    this.action		= action;		//点击button后触发的动作，可为空
    this.imageOff	= imageOff;		//平时button的图标，可为空
    this.imageOver	= imageOver;	//鼠标移到button上的图标，可为空
    this.width		= width;		//button长，格式推荐为12px
    this.height		= height;		//button
    this.title		= title;		//tooltip
    this.bgColor	= bgColor;		//button背景色，缺省为透明
    this.fontFamily	= fontFamily;	//button字体
    this.fontSize	= fontSize;		//button字体大小，格式推荐为12px
    this.fontColor	= fontColor;	//button字体颜色
    //定义对象的方法
    this.show		= showButton;
    this.over		= overButton;
    this.off		= offButton;
    this.down		= downButton;
    this.up		= upButton;
    this.fire		= fireAction;
}

function showButton(){
	var strHTML = '';
	strHTML += '<span id="'+ this.id + '" class="ButtonNormal" ';
	strHTML += 'onmouseover="' + this.id + '.over()" onmouseout="' + this.id + '.off()" ';
	strHTML += 'onmouseup="' + this.id + '.up()" onmousedown="' + this.id + '.down()" ';
 	if (this.title != null)
		strHTML += ' title="' + this.title + '"';
 	if (this.action != null)
		strHTML += ' onclick="' + this.id + '.fire()"';
	strHTML += ' style="';
	if (this.width != null)
		strHTML += 'width:' + this.width + ';';
	if (this.height != null)
		strHTML += 'height:' + this.height + ';';
	if (this.bgColor != null)
		strHTML += 'background-color:' + this.bgColor + ';';
	if (this.fontFamily != null)
		strHTML += 'font-family:' + this.fontFamily + ';';
	if (this.fontSize != null)
		strHTML += 'font-size:' + this.fontSize + ';';
	if (this.fontColor != null)
		strHTML += 'color:' + this.fontColor + ';';
	strHTML += 'vertical-align:middle"><center>';
	if (this.imageOff != null)
		strHTML += '<img align="absMiddle" id="' + this.id + '_image" src="' + this.imageOff + '" style="vertical-align:bottom"> ';
	strHTML += this.text;
	strHTML += '</center></span>';
	//alert(strHTML);
	document.write(strHTML);
}

function fireAction(){
	eval(this.action);
}

function overButton(){
	if (typeof(document.all(this.id)) == "object")
		document.all(this.id).className = "ButtonUp";
	if (this.imageOver != null)
		if (typeof(document.all(this.id + "_image")) == "object")
			document.all(this.id + "_image").src = this.imageOver;
}

function offButton()
{
	if (typeof(document.all(this.id)) == "object")
		document.all(this.id).className = "ButtonNormal";
	if (this.imageOff != null)
		if (typeof(document.all(this.id + "_image")) == "object")
			document.all(this.id + "_image").src = this.imageOff;
}

function downButton()
{
	if (typeof(document.all(this.id)) == "object")
		document.all(this.id).className = "ButtonDown";
}

function upButton()
{
	if (typeof(document.all(this.id)) == "object")
		document.all(this.id).className = "ButtonUp";
}
