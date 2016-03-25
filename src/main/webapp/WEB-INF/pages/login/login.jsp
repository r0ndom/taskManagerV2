<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tag:layout>
    <title>Вход</title>

    <script>
        function showPasswordLogin() {
            var password = $('#password');
            if (password.get(0).type =='password') {
                password.get(0).type='text';
            } else {
                password.get(0).type='password';
            }
        }
    </script>

    <style>
        .error {
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
            color: #a94442;
            background-color: #f2dede;
            border-color: #ebccd1;
        }
    </style>

    <div id="wrapper">
        <div class="container">
            <div class="row col-md-6 col-md-offset-4" style="margin-top: 10%">
                <form name='loginForm' action="<c:url value='/j_spring_security_check' />" method='POST' style="margin-left: 30px">
                    <div class="row">
                        <div class="col-md-7">
                            <c:if test="${not empty error}">
                                <div class="error">${error}</div>
                            </c:if>
                        </div>
                    </div>
                    <div class="row"><div class="col-md-7" style="margin-bottom: 10px;">
                        <input class="col-md-3 form-control" id="username" name="username"
                               placeholder="Логин" type="text" style="height: 38px;">
                    </div></div>
                    <div class="row"><div class="col-md-7" style="margin-bottom: 10px;">
                        <input class="col-md-3 form-control" id="password" name="password"
                               placeholder="Пароль" type="password" style="height: 38px;">
                    </div></div>
                    <div class="row" style="margin-bottom: 5px">
                        <div class="col-md-4" style="display: inline; width: 100%">
                            <input type="checkbox" name="showPassword" onchange="showPasswordLogin();"> Показать пароль
                        </div>
                    </div>
                    <div class="row"><div class="col-md-7">
                        <input class="btn btn-primary" style="width: 100%; height: 40px" value="Вход" type="submit"></div></div>
                </form>
            </div>
        </div>
    </div>
</tag:layout>