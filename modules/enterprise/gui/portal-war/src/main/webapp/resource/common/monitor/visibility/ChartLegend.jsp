<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/hq.tld" prefix="hq" %>

<table width="100%" cellpadding="3" cellspacing="0" border="0">
  <tr>
    <td width="100%">
      <b><fmt:message key="resource.common.monitor.visibility.chart.ResourceAndControlActionKeyLabel"/></b><br>

      <c:forEach var="resource" varStatus="resStatus" items="${checkedResources}">
      <fmt:formatNumber var="imgidx" pattern="00" value="${resStatus.index + 1}"/>
      <p><b><fmt:message key="resource.common.monitor.visibility.chart.ResourceLabel"/></b>
      <html:img page="/images/icon_resource_${imgidx}.gif" width="11" height="11" border="0"/>
      <c:out value="${resource.name}"/><br>
      <c:forEach var="event" varStatus="evStatus" items="${chartLegend[resStatus.index]}">
      <b>(<c:out value="${event.id}"/>)</b>
      <c:out value="${event.detail}"/> -
      <hq:dateFormatter value="${event.timestamp.time}"/><c:if test="${! evStatus.last}">,</c:if><br/>
      </c:forEach></p>
      </c:forEach>
    </td>
  </tr>
</table>
