<%@ page session="true" contentType="text/html; charset=GBK" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/olap?user=root&password=" catalogUri="/WEB-INF/queries/apollo.xml">

select
  {[Measures].[���ۣ�Ӫҵ�����뾻��], [Measures].[���ۣ�Ӫҵ���ɱ�], [Measures].[��������], [Measures].[Ͷ������]} on columns,
  {[���ڵ���]} ON rows
from [�ʲ���ծ]
</jp:mondrianQuery>

<c:set var="title01" scope="session"></c:set>
