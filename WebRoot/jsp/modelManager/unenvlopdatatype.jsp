<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>ѡ��ڵ�</title>
</head>
<SCRIPT LANGUAGE="JavaScript">

   function selunenvloptype()
   {
        var retValue=new Array();
		var elms=frmmain.seltype;
		var selType=0;
	
		for(var i=0;i<elms.length;i++)
	   {
			if(elms[i].checked)
				selType=elms[i].value;

	   }

       retValue[0]=selType;
       returnValue =retValue;
	   close();

   }

   function cancle()
   {
    	returnValue=null;
		close();
   }

</SCRIPT>
<body>

<form method="POST" name="frmmain">

	<p><input type="radio" value="0" name="seltype" checked><span lang="zh-cn">ѡ�нڵ�</span></p>
	<p><input type="radio" value="1" name="seltype">ѡ�нڵ��ѡ�нڵ��ֱ���¼��ڵ�</p>
	<p><input type="radio" value="2" name="seltype">ѡ�нڵ��ѡ�нڵ��ȫ���¼��ڵ�</p>
	<p align="center">
	<input type="button" value="ȷ��" name="btnOk" onClick="selunenvloptype();">
	<input type="button" value="ȡ��" name="btnCancle"  onClick="cancle();"></p>
</form>

</body>

</html>