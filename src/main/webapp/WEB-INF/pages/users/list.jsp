<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title></title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap-theme.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/users.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/modal.js"></script>
    </head>
    <body>
        <div class="container">

            <ul class="nav nav-tabs tabs-up" id="users">
                <li><a href="/users/data?type=requests" data-target="#requests" class="media_node active span" id="requests_tab" data-toggle="tabajax" rel="tooltip"> Заявки </a></li>
                <li><a href="/users/data?type=all" data-target="#all" class="media_node span" id="all_tab" data-toggle="tabajax" rel="tooltip"> Все</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="requests">

                </div>
                <div class="tab-pane" id="all">

                </div>
            </div>
        </div>


    </body>
</html>