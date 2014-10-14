<%@ page session="true" contentType="text/html; charset=GBK" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jp:mondrianQuery id="query01" jdbcDriver="com.mysql.jdbc.Driver" jdbcUrl="jdbc:mysql://localhost/olap?user=root&password=" catalogUri="/WEB-INF/queries/apollo.xml">

select
  {[Measures].[销售（营业）收入净额], [Measures].[销售（营业）成本], [Measures].[销售利润], [Measures].[投资收益]} on columns,
  {[所在地区]} ON rows
from [资产负债]
</jp:mondrianQuery>

<c:set var="title01" scope="session"></c:set>
