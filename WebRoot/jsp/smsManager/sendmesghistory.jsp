<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%
//·ÀÖ¹ä¯ÀÀÆ÷»º´æ±¾Ò³Ãæ
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/jsp/smsManager/ext/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="../jsp/smsManager/ext/ui/icon.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/ui/Util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/ui/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/ui/sms/MessageSendWindow.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/smsManager/ext/ui/sms/sendrecord.js"></script>
<head>
<title>ÒÑ·¢ËÍ¶ÌÐÅ¼ÇÂ¼</title>
<style type="text/css">
        #demo-ct .x-table-layout-cell {
            padding: 20px;
        }
    </style>

</head>
<body>
 <div id="grid-example"></div>

</body>
</html>