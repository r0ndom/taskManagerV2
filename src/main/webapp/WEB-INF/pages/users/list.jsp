<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title></title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap-combined.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/users.js"></script>
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

        <div id="addRoleModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Modal Header</h4>
                    </div>
                    <div class="modal-body">
                        <p>Some text in the modal.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>