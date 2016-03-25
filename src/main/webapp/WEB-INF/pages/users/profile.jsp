<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<spring:message code="messages.create" var="createMessage"/>
<spring:message code="messages.name" var="nameMessage"/>
<spring:message code="messages.surname" var="surnameMessage"/>
<spring:message code="messages.ldap" var="ldapMessage"/>
<spring:message code="messages.role" var="roleMessage"/>

<tag:layout>
    <div id="mainPage">
        <jsp:include page="../commons/header.jsp"/>
        <style>
            label {
                padding-top: 10px;
            }
        </style>
        <div id="wrapper">
            <div class="container">
                <div class="row col-md-6 col-md-offset-4" style="margin-top: 10%">
                        <div class="row">
                            <div class="col-md-2">
                                <label>${ldapMessage}</label>
                            </div>
                            <div class="col-md-6">
                                <label>${user.ldap}</label>
                            </div>
                        </div>
                        <p></p>
                        <div class="row">
                            <div class="col-md-2">
                                <label>${nameMessage}</label>
                            </div>
                            <div class="col-md-6">
                                <label>${user.firstName}</label>
                            </div>
                        </div>
                        <p></p>
                        <div class="row">
                            <div class="col-md-2">
                                <label>${surnameMessage}</label>
                            </div>
                            <div class="col-md-6">
                                <label>${user.lastName}</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-2">
                                <label>${roleMessage}</label>
                            </div>
                            <div class="col-md-6">
                                <label>${user.role}</label>
                            </div>
                        </div>
                        <p></p>
                        <div class="row"><div class="col-md-8">
                            <input class="btn btn-success" style="width: 100%; height: 40px"
                                   value="Редактировать" onclick="window.location.href = '/app/users/edit'"></div></div>
                </div>
            </div>
        </div>
        <jsp:include page="../commons/footer.jsp"/>
    </div>
</tag:layout>