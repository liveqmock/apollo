<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<title>�ϱ�����</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/jquery-1.5.1.min.js"></script>
<script type="text/javascript">
	function publishModel(){
	    if(form1.file.value == ""){
	        alert("��ѡ��Ҫ�ϱ��ı��������ļ�");
	        return false;
	    }
	    $("#hiddenlayer").html("���ֲ㿪ʼ!");
	    showBg('massage_box','massage_content');
	    //return false;
	    return true;
	}
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
</head>
<body>
<div id="main"> 
	<form action="model?operation=<%= ModelServlet.UPLOAD_MODEL %>" method="post" enctype="multipart/form-data" name="form1">
          �ϴ����������ļ���
         <input type="file" name="file"/>
         <!-- 
         <input type="button" onclick="" value="��ʾ�ȴ���Ϣ"> 
          -->
         <input type="submit"value="�ϱ�" onclick="return publishModel()"/></td>
     </form>
</div> 
<!-- JS���ֲ� --> 
<div id="fullbg"></div> 
<!-- end JS���ֲ� -->
<div id="massage_box" onclick="hiddenMessage();">  
    <div class="massage">  
        <div class="header" >  
            <div style="display: inline; width: 150px; position: absolute">  
                �����ϱ��� ... ...  <br/>
            </div>
        </div>  
        <div style="margin-top: 20px; margin-left: 20px; width: 128px; height: 128px; float: left;">  
            <img src="../images/cxz_ly.gif" />  
        </div>  
        <div  id="massage_content" style="margin-top: 50px; width: 136px; height: 128px; float: right;">  
            �ϱ����������ѷ���  
           <br/>  
            �ȴ������������ϱ����ݽ��  
        </div>  
    </div>  
</div>
</body>
</html>
