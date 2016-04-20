<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<spring:message code="messages.name" var="nameMessage"/>
<spring:message code="messages.status" var="statusMessage"/>
<spring:message code="messages.executor" var="executorMessage"/>
<spring:message code="messages.author" var="authorMessage"/>

<tag:layout>
  <div id="mainPage">
    <jsp:include page="../commons/header.jsp"/>
    <div class="container">
      <div style="margin-left: 5%; margin-right: 5%;">
        <table class="table">
          <tr>
            <td>${nameMessage}</td>
            <td>${statusMessage}</td>
            <td>${executorMessage}</td>
            <td>${authorMessage}</td>
          </tr>
          <c:forEach var="task" items="${tasks}">
            <tr id="${task.id}">
              <td id="name">${task.name}</td>
              <td id="status">${task.status}</td>
              <td id="executor">${task.executor}</td>
              <td id="author">${task.author}</td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </div>
    <jsp:include page="../commons/footer.jsp"/>
  </div>
</tag:layout>


