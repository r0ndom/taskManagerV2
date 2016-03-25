<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<spring:message code="messages.status" var="statusMessage"/>
<spring:message code="messages.executor" var="executorMessage"/>
<spring:message code="messages.author" var="authorMessage"/>
<spring:message code="messages.search" var="searchMessage"/>

<form:form method="POST" commandName="taskSearchFilter" action="/app/tasks/">
    <div class="row">
        <div class="col-md-3">
            <label>${statusMessage}:</label>
            <form:select cssClass="form-control" path="status">
                <form:option value="" label="Все"/>
                <form:options items="${statusList}" />
            </form:select>
        </div>
        <div class="col-md-3">
            <label>${executorMessage}:</label>
            <form:select cssClass="form-control" path="executor">
                <form:option value="" label="Все"/>
                <form:options items="${executorList}" itemValue="ldap" />
            </form:select>
        </div>
        <div class="col-md-3">
            <label>${authorMessage}:</label>
            <form:select cssClass="form-control" path="author">
                <form:option value="" label="Все"/>
                <form:options items="${authorList}" itemValue="ldap" />
            </form:select>
        </div>
        <div class="col-md-3">
            <input class="btn btn-success" style="margin-top: 25px" value="${searchMessage}" type="submit">
        </div>
    </div>
</form:form>