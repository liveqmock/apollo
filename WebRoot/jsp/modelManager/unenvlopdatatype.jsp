<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>选择节点</title>
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

	<p><input type="radio" value="0" name="seltype" checked><span lang="zh-cn">选中节点</span></p>
	<p><input type="radio" value="1" name="seltype">选中节点和选中节点的直接下级节点</p>
	<p><input type="radio" value="2" name="seltype">选中节点和选中节点的全部下级节点</p>
	<p align="center">
	<input type="button" value="确定" name="btnOk" onClick="selunenvloptype();">
	<input type="button" value="取消" name="btnCancle"  onClick="cancle();"></p>
</form>

</body>

</html>