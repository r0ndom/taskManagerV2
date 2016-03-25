<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:message code="messages.ldap" var="ldapMessage"/>
<spring:message code="messages.name" var="nameMessage"/>
<spring:message code="messages.role" var="roleMessage"/>

<table class="table">
    <tr>
        <td>${ldapMessage}</td>
        <td>${nameMessage}</td>
        <td>${roleMessage}</td>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr id="${user.id}">
            <td id="ldap">${user.ldap}</td>
            <td id="name">${user.firstName} ${user.lastName}</td>
            <td id="role">${user.role.roleViewName}</td>
        </tr>
    </c:forEach>
</table>