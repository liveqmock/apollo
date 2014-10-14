<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.DictionaryServlet" %>
<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>

<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<title>代码字典管理</title>
<STYLE TYPE='text/css'>
   A:hover{text-decoration:underline;color: black}
</STYLE>
</head>
<body>
<%
  String selectedList = "";
  String selectedtree = "";
  if((String)request.getAttribute("displayMode")=="tree"){
    selectedtree = "checked";
  }else{
    selectedList = "checked";
  }
%>
  <table width=100% border=0 height=6%>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            代码字典管理-><font class="clsLightColorOfTask"><%=request.getAttribute("dictionaryName")%></font>
            &nbsp;&nbsp;&nbsp;
            <input type=radio name=radio value="" onclick="dispalylist()" <%=selectedList%>>列表显示
            &nbsp;&nbsp;&nbsp;
            <input type=radio name=radio value="" onclick="dispalyTree()" <%=selectedtree%>>树型显示
         </td>
       </tr>
</table>
<div style="height:93%;OVERFLOW:auto">
  <table width=100% border=0>
<%
  if((String)request.getAttribute("displayMode")=="list"){
  java.util.Iterator _entries = (java.util.Iterator)request.getAttribute("entries");
  if(_entries!=null){
%>
    <tr  class="clsTrHeader">
      <td width=25% align="center">代码</TD>
      <td width=85% align="left">代码含义</TD>
    </TR>
<%
   int i = 0;
   while(_entries.hasNext()){
      java.util.Map.Entry dictionaryEntry = (java.util.Map.Entry)_entries.next();
%>
  <tr align="left" <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
    <td width=25% align="center" nowrap><%=dictionaryEntry.getKey()%></TD>
    <td width=85% align="left" nowrap><%=dictionaryEntry.getValue()%></TD>
  </TR>
<%i++;} }}else{%>
   <tr><td><%=(String)request.getAttribute("defaultDictionaryTree")%></td></tr>
<%}%>
</TABLE>
</div>
<script language="javascript">
function dispalyTree(){
      var url = "dictionary?operation=<%=DictionaryServlet.SHOW_DICTIONARY_TREE%>";
      url += "&dictionaryID="+<%=(String)request.getAttribute("dictionaryID")%>;
      url += "&dictionaryName="+encodeURIComponent("<%=(String)request.getAttribute("dictionaryName")%>");
      window.location = encodeURI(url);
}
function dispalylist(){
      var url = "dictionary?operation=<%=DictionaryServlet.SHOW_DICTIONARY_ENTRY_PAGE%>";
      url += "&dictionaryID="+<%=(String)request.getAttribute("dictionaryID")%>;
      url += "&dictionaryName="+encodeURIComponent("<%=(String)request.getAttribute("dictionaryName")%>");
      window.location = encodeURI(url);
}
function changeUnit(index){
}
</script>
</body>
</html>
