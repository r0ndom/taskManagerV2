<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="messages.company" var="companyMessage"/>

<div class="navbar-fixed-bottom row-fluid">
  <div class="navbar-inner" style="text-align: center;">
    <p style="padding-bottom: 30px">
      <span class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></span>&nbsp;${companyMessage}
    </p>
  </div>
</div>
