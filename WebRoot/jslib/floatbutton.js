function floatButton(id, text, action, imageOff, imageOver,
	width, height, title, bgColor, fontFamily, fontSize, fontColor)
{
    this.id			= id;			//���ڸ����buttonָ��ΨһID�������ʵ����һ��
    this.text		= text;			//button�ϵ�����
    this.action		= action;		//���button�󴥷��Ķ�������Ϊ��
    this.imageOff	= imageOff;		//ƽʱbutton��ͼ�꣬��Ϊ��
    this.imageOver	= imageOver;	//����Ƶ�button�ϵ�ͼ�꣬��Ϊ��
    this.width		= width;		//button������ʽ�Ƽ�Ϊ12px
    this.height		= height;		//button
    this.title		= title;		//tooltip
    this.bgColor	= bgColor;		//button����ɫ��ȱʡΪ͸��
    this.fontFamily	= fontFamily;	//button����
    this.fontSize	= fontSize;		//button�����С����ʽ�Ƽ�Ϊ12px
    this.fontColor	= fontColor;	//button������ɫ
    //�������ķ���
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
