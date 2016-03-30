$( document ).ready(function() {
    console.log("users page ready");
    toggleTab();
});

//TODO need to add pagination

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
                var buttons = [createButtonWithModal("assign_role", "Добавить роль", "#addRoleModal")];
                result += createRow(id, emails, roles, buttons);
            });
            $(targ).html(createTable(result));
        });

        $this.tab('show');
        return false;
    });
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