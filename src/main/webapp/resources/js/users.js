$( document ).ready(function() {
    console.log("users page ready");
    toggleTab();
});

//TODO need to add pagination
//TODO add back end validation
function toggleTab() {
    $('[data-toggle="tabajax"]').click(function(e) {
        var $this = $(this),
            loadurl = $this.attr('href'),
            targ = $this.attr('data-target');

        $.getJSON(loadurl, function(data) {
            var result = "";
            result += createStrRow("Id", "Emails", "Роли", "Действия");
            data.forEach(function(item, i, data) {
                var id = item["id"];
                var emails = item["emails"];
                var roles = item["stringRoles"];
                var content = createCheckBox("permittedRoles" + id, getPermittedRoles(roles));
                var elements = [createButtonWithModal("assign_role" + id, "Добавить роль", "#addRoleModal" + id)];
                var addRoleButton = createButton("addRole" + id, "Добавить");
                var addRole = createModal("addRoleModal"  + id, "Добавить роль", content, addRoleButton);
                $(".container").append(addRole);
                addListener(id);
                result += createRow(id, emails, roles, elements);
            });
            $(targ).html(createTable(result));

        });

        $this.tab('show');
        return false;
    });
}

function getPermittedRoles(roles) {
    var allRoles = ["Разработчик", "Менеджер", "Администратор", "Тестировщик", "Гость", "Администратор приложения"];
    var diff = allRoles.filter(function(x) { return roles.indexOf(x) < 0 });
    return diff;
}

function addListener(id) {
    $("#addRole" + id).click(function () {
        var selected = [];
        $('#permittedRoles' + id + ' input:checked').each(function() {
            selected.push($(this).val());
        });
        if (selected.length > 0) {
            var data = {userId: id, roles: selected};
            var json = JSON.stringify(data);
            $.ajax({
                url: '/users/add/role',
                type: 'POST',
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function () {
                    window.location.href = "/users";
                }
            });
        } else {
            //TODO add popup
        }
    });
}

function createCheckBox(id, data) {
    var result = "";
    result += "<div id=\"" + id + "\" class=\"checkbox\">";
    var oLabel = "<label>";
    var cLabel = "</label>";
    for (var i = 0; i < data.length; i++) {
        var input = "<input id=\""+ data[i] + "\" type=\"checkbox\" value=\"" +  data[i] + "\">"  + data[i];
        result += ("<br>" + oLabel + input +cLabel);
    }
    result += "</div>";
    return result;

}

function createButton(id, text) {
    var idPart = "<button id=\"" + id ;
    var textPart = "\" class=\"btn btn-primary\">" + text;
    var end = "</button>";
    return idPart + textPart + end;
}

function createButtonWithModal(id, text, modal) {
    var idPart = "<button id=\"" + id ;
    var modalPart = "\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"" + modal;
    var textPart = "\">" + text;
    var end = "</button>";
    return idPart + modalPart + textPart + end;
}


function createRow(id, emails, roles, buttons){
    return "<tr>" + createColumn(id, emails, roles, buttons) + "</tr>";
}

//TODO refactor this
function createStrRow(param1, param2 , param3, param4) {
    var td = "<td>";
    var cTd = "</td>";
    var result = "";
    result += (td + param1 + cTd);
    result += (td + param2 + cTd);
    result += (td + param3 + cTd);
    result += (td + param4 + cTd);
    return "<tr>" + result + "</tr>";
}

function createColumn(id, emails, roles, buttons) {
    var td = "<td>";
    var cTd = "</td>";
    var result = "";
    result += (td + id + cTd);
    result += printArrayInTag(td, cTd, emails);
    result += printArrayInTag(td, cTd, roles);
    result += printArrayInTag(td, cTd, buttons);
    return result;
}

function printArrayInTag(open, close, data) {
    var result = "";
    result += open;
    for (var i = 0; i < data.length; i++) {
        result += (data[i] +'\n');
    }
    result += close;
    return result;
}

function createTable(content) {
    var table = "<table class=\"table\">";
    var cTable = "</table>";
    return table + content + cTable;
}