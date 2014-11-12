<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<script type="text/javascript" src="../jslib/jquery-1.5.1.min.js"></script>
<title>��������</title>
</head>
<script language="JavaScript">
function downloadModel()
{
    var unitID = getCheckedValues();
    if(unitID == "")
    {
        alert("��ѡ��λ");
        return false;
    }
    form1.unitID.value = unitID;
<%Task task = (Task)request.getAttribute("task");
  if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("��ѡ��ʱ��");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
  if(document.getElementById('timeOfTask').value==""){
    alert("��ѡ��ʱ��");
    return;
  }
  var flag=false;
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
       form1.taskTimeID.value=dateInfos[i][0];
       flag = true;
    }
  }
  if(!flag){
       alert(document.getElementById('timeOfTask').value+"û������,������ѡ��һ������!");
       document.getElementById('timeOfTask').value = "";
       return;
    }
<%}%>
    form1.submit();
     $("#hiddenlayer").html("���ֲ㿪ʼ!");
	    showBg('massage_box','massage_content');
}
function submitForm(){}
</script>


<script type="text/javascript"> 
//��ʾ��ɫJS���ֲ� 
function showBg(ct,content){ 
	var bH=$("body").height(); 
	var bW=$("body").width()+16; 
	var objWH=getObjWh(ct); 
	$("#fullbg").css({width:bW,height:bH,display:"block"}); 
	var tbT=objWH.split("|")[0]+"px"; 
	var tbL=objWH.split("|")[1]+"px";
	
	tbT = "100 px";
	tbL = "300 px"; 
	$("#"+ct).css({top:tbT,left:tbL,display:"block"});  
	$(window).scroll(function(){resetBg()}); 
	$(window).resize(function(){resetBg()}); 
} 
function getObjWh(obj){ 
	var st=document.documentElement.scrollTop;//�������ඥ���ľ��� 
	var sl=document.documentElement.scrollLeft;//����������ߵľ��� 
	var ch=document.documentElement.clientHeight;//��Ļ�ĸ߶� 
	var cw=document.documentElement.clientWidth;//��Ļ�Ŀ�� 
	var objH=$("#"+obj).height();//��������ĸ߶� 
	var objW=$("#"+obj).width();//��������Ŀ�� 

	var objT=Number(st)+(Number(ch)-Number(objH))/2; 
	var objL=Number(sl)+(Number(cw)-Number(objW))/2; 
	return objT+"|"+objL; 
} 
function resetBg(){ 
var fullbg=$("#fullbg").css("display"); 
if(fullbg=="block"){ 
var bH2=$("body").height(); 
var bW2=$("body").width()+16; 
$("#fullbg").css({width:bW2,height:bH2}); 
var objV=getObjWh("dialog"); 
var tbT=objV.split("|")[0]+"px"; 
var tbL=objV.split("|")[1]+"px"; 
$("#dialog").css({top:tbT,left:tbL}); 
} 
} 
</script> 
<style type="text/css"> 
*{ 
	font-family:Arial, Helvetica, sans-serif; 
	font-size:12px; 
} 
#fullbg{ 
	background-color: Gray; 
	display:none; 
	z-index:3; 
	position:absolute; 
	left:0px; 
	top:0px; 
	filter:Alpha(Opacity=30); 
	/* IE */ 
	-moz-opacity:0.4; 
	/* Moz + FF */ 
	opacity: 0.4; 
} 

#dialog { 
	position:absolute; 
	width:200px; 
	height:200px; 
	background:#F00; 
	display: none; 
	z-index: 5; 
} 
#massage_box { 
	position:absolute; 
	width: 350px;  
	height: 130px;  
	filter: dropshadow(color = #666666, offx = 3, offy =3, positive =2); 
	display: none; 
	z-index: 5; 
} 
#main { 
	margin-top:100px;
	margin-left:50px;
	height: 400px; 
} 
#mask {  
    position: absolute;  
    top: 0;  
    left: 0;  
    width: expression(body.clientWidth);  
    height: expression(body.scrollHeight > body.clientHeight ? body.scrollHeight : body.clientHeight);  
    background: #666;  
    filter: ALPHA(opacity = 60);  
    z-index: 1;  
    visibility: hidden  
}  
  
.massage {  
    border: #036 solid;  
    border-width: 1 1 3 1;  
    width: 95%;  
    height: 95%;  
    background: #fff;  
    color: #036;  
    font-size: 12px;  
    line-height: 150%  
}  
  
.header {  
    background: #554295;  
    height: 10%;  
    font-family: Verdana, Arial, Helvetica, sans-serif;  
    font-size: 12px;  
    padding: 3 5 0 5;  
    color: #fff  
}  
</style>


<body>


   <form action="model?operation=<%= ModelServlet.DOWNLOAD_MODEL %>" method="post" name="form1">
<table align=center height=500 height=100% border=0>
     <input type="hidden" name="taskTimeID" value="">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            ���ݴ���-><font class="clsLightColorOfTask">��������</font>
         </td>
       </tr>
          <tr height=5% class="clsTrContext">
                    <td width=66% align=center>
              ����ʱ��:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
                        &nbsp;&nbsp;<input type="checkbox" name="isRecursive" value="true"/>���������¼���λ����
&nbsp;&nbsp;&nbsp;
              <button value="���ر�������" onclick="downloadModel()">��������</button>&nbsp;&nbsp;&nbsp;</td>
          </tr>
        <tr height=95% class="clsTrContext">

            <td align="center" width=100% colspan=2>
              <input type="hidden" name="unitID" value=""/>
              <table height=100% width=54%>
                <tr><td align="">
                   <div class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
                </td></tr>
              </table>
            </td>
        </tr>
</table>
</form>

<!-- JS���ֲ� --> 
<div id="fullbg"></div> 
<!-- end JS���ֲ� -->
<div id="massage_box" onclick="hiddenMessage();">  
    <div class="massage">  
        <div class="header" >  
            <div style="display: inline; width: 150px; position: absolute">  
                	�������ع����� ... ...  <br/>
            </div>
        </div>  
        <div style="margin-top: 20px; margin-left: 20px; width: 128px; height: 128px; float: left;">  
            <img src="../images/cxz_ly.gif" />  
        </div>  
        <div  id="massage_content" style="margin-top: 50px; width: 136px; height: 128px; float: right;">  
            �������������ѷ���  
           <br/>  
            �������fap�ļ�������<a href="#" onclick="javascript:location.reload();" style="color:red">ˢ��</a>��ť, ����ˢ��ҳ���������������  
        </div>  
    </div>  
</div>
</body>
</html>
