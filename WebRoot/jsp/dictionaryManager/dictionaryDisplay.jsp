<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.DictionaryServlet" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.dictionary.*" %>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>代码字典管理</title>
<STYLE TYPE='text/css'>
   A:hover{text-decoration:underline;color: black}
.clsDictionaryMenu{
    BACKGROUND-COLOR: #e7e7e4;
    BORDER-right: 1 solid #c9cfe9;
    BORDER-left: 1 solid #c9cfe9;
}
</STYLE>
</head>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />


<table style="height:82%;width=100%" border=0 cellpadding=0>
<tr>
  <td width=14% valign=top class="clsDictionaryMenu">
          <table border=0 width=100% cellpadding=4 style="border-collapse: collapse">
            <tr height=35>
              <td align="center">
                 <font id='firstLink' color='#f2f2f0'>→</font><a href="javascript:showPublishDictionaryPage()" style="color:blue">设置代码字典</a>&nbsp;&nbsp;
              </td>
            </tr>
              <%
                Iterator _dictionaries = (Iterator)request.getAttribute("dictionaries");
                for(int i=0; _dictionaries.hasNext(); i++)
               {
                  cn.com.youtong.apollo.dictionary.Dictionary dictionary = (cn.com.youtong.apollo.dictionary.Dictionary)_dictionaries.next();
              %>
                <tr>
                  <td align="center" style='BACKGROUND-COLOR: #f2f2f0'>
                    <font id='<%=dictionary.id()%>' color='#f2f2f0'>→</font><a href="javascript:displayEntries('<%=dictionary.id()%>','<%=dictionary.getName()%>')" style="color:blue"><%=dictionary.getName()%></a>&nbsp;&nbsp;
                  </td>
                </tr>
              <%}%>

         </table>

    </td>

    <td>
         <iframe id="EntryFrame" width=100% FRAMEBORDER=0 height=100% scrolling=no></iframe>
     </td>


</table>


  <jsp:include page= "/jsp/footer.jsp" />

<SCRIPT language="javascript">
function showPublishDictionaryPage(){
  setArrow("firstLink");
  var url = "dictionary?operation=<%=DictionaryServlet.SHOW_DICTIONARY_PUBLISH_PAGE%>";
  EntryFrame.location = url;
}


//显示数据字典条目
function displayEntries(id,name){
      setArrow(id);
      var url = "dictionary?operation=<%=DictionaryServlet.SHOW_DICTIONARY_ENTRY_PAGE%>";
      url += "&dictionaryID="+id+"&dictionaryName="+encodeURIComponent(name);
      EntryFrame.location = encodeURI(url);
}
function setArrow(index){
   var oFonts = document.body.all.tags("FONT");
   for(var i=0;i<oFonts.length;i++){
         if(oFonts[i].innerText == "→"){
           oFonts[i].color = "#f2f2f0";
         }
   }
   document.getElementById(index).color = "#darkorange";
}
showPublishDictionaryPage();
</SCRIPT>


</body>
</html>
