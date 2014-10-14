<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.DictionaryServlet" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.dictionary.*" %>
<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>���������ֵ�</title>
</head>
<body>
<table class="clsTaskFrameTable" border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            �����ֵ����-><font class="clsLightColorOfTask">���ô����ֵ�</font>
         </td>
       </tr>
       <tr>
         <td colspan=8><br>
            <img src="../img/infomation.bmp">&nbsp;��ҳ��������������ֵ�ķ�����ɾ��.
         </td>
       </tr>
       <tr>
         <td colspan=8>
            &nbsp;
         </td>
       </tr>
  <tr>
         <form action="dictionary?operation=<%= DictionaryServlet.ADD_DICTIONARY %>" method="post" enctype="multipart/form-data" name="form1">
                <td width="1%" colspan=8>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               ���������ֵ䣺&nbsp;&nbsp;&nbsp;<input type="file" name="file"/>
                &nbsp;<input type="submit"value="����" onclick="return checkValue()"/>
              </td>
         </form>
  </tr>
  <tr height=20 class="clsTrContext">
    <td align=center colspan=8>�����ֵ��б�</td>
  </tr>
 <tr class="clsTrHeader" height=20px>
    <td width=20% align=center>�ֵ�����</td>
    <td width=20% align=center>����</td>
  </tr>
<%
        Iterator _dictionaries = (Iterator)request.getAttribute("dictionaries");
        for(int i=0; _dictionaries.hasNext(); i++)
       {
          cn.com.youtong.apollo.dictionary.Dictionary dictionary = (cn.com.youtong.apollo.dictionary.Dictionary)_dictionaries.next();
%>
  <tr height=20 <%=(((i%2)==1)?"class=\"TrDark\"":"class=\"TrLight\"")%>>
    <td width=20% height=20 align=center><%= dictionary.getName() %>&nbsp;</td>
    <td width=20% height=20 align=center>
    &nbsp;<a href="javascript:deleteDictionary('<%= dictionary.id() %>','<%= dictionary.getName() %>')">ɾ��</a></td>
</tr>
<%
    }
%>
</table>


<SCRIPT language="javascript">
function checkValue()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ�����Ĵ����ֵ��ļ�");
        return false;
    }
    return true;
}
function addDictionary()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ����Ĵ����ֵ��ļ�");
        return false;
    }
    return true;
}

//��������ֵ�
function addDictionary(){
    var url = "dictionary?operation=<%=DictionaryServlet.SHOW_DICTIONARY_ADD_PAGE%>";
    window.location=url;
}
//ɾ�������ֵ���Ŀ
function deleteDictionary(id,name){
  if(confirm("��ȷ��Ҫɾ��<"+name+">��Ŀ��"))
    {
      window.location = "dictionary?operation=<%= DictionaryServlet.DELETE__DICTIONARY_ITEM %>"
            + "&dictionaryID=" + id;
    }
}
</SCRIPT>


</body>
</html>
