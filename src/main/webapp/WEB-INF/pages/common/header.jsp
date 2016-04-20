<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<spring:message code="messages.exit" var="messageExit"/>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">

  <div class="navbar-inner">
    <div class="container-fluid">
      <form action="/j_spring_security_logout" method="post" id="logoutForm">
        <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
        <button id="submitButton" type="submit" class="navbar-text navbar-right submitButton"
                style="padding-right: 40px; padding-top: 10px">
          <b>${messageExit}</b>
          <img src="${pageContext.request.contextPath}/resources/img/exit.png"/>
        </button>
      </form>
        <input class="btn btn-success" value="Профиль" onclick="window.location.href = '/app/users/profile/'">
    </div>
  </div>
</nav>