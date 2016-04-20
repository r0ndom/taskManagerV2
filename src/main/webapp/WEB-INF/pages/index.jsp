<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<spring:message code="messages.create" var="createMessage"/>
<spring:message code="messages.users" var="usersMessage"/>
<spring:message code="messages.archive" var="archiveMessage"/>

<tag:layout>
    <div id="mainPage">
        <jsp:include page="commons/header.jsp"/>
        <div class="container">
            <div style="margin-left: 5%; margin-right: 5%;">
                <jsp:include page="search/searchFilter.jsp"/>
                <input class="btn btn-success" style="margin-top: 25px" value="${createMessage}" onclick="window.location.href = '/app/tasks/create/';">
                <input class="btn btn-success" style="margin-top: 25px; display: inline;" value="${archiveMessage}" onclick="window.location.href = '/app/tasks/archive/';">
                <form action="/app/tasks/sort/" method="post" style="display: inline;">
                    <input class="btn btn-primary" style="margin-top: 25px" value="Сортировать по убыванию" type="submit"/>
                </form>
                <form action="/app/tasks/sort/" method="post" style="display: inline;">
                    <input name="str" value="natural" hidden/>
                    <input class="btn btn-primary" style="margin-top: 25px" value="Обычный порядок" type="submit"/>
                </form>
                <form action="/app/export" method="post" style="display: inline;">
                    <input class="btn btn-primary" style="margin-top: 25px" value="Export" type="submit"/>
                </form>
                <jsp:include page="search/searchTable.jsp"/>
            </div>
        </div>
        <jsp:include page="commons/footer.jsp"/>
    </div>
</tag:layout>