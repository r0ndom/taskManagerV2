function createModal(id, title, content, buttons) {
    var modal = "<div id=\"" + id + "\" class=\"modal fade\" role=\"dialog\">";
    var modalDialog = "<div class=\"modal-dialog\">";
    var endDiv = "</div>";
    var modalHeader = createModalHeader(title);
    var modalBody = createModalBody(content);
    var modalFooter = createModalFooter(buttons);
    var modalContent = createModalContent(modalHeader + modalBody + modalFooter);
    return modal + modalDialog + modalContent + endDiv + endDiv;
}

function createModalHeader(title) {
    var modalHeader = "<div class=\"modal-header\">";
    var endDiv = "</div>";
    var closeButton = "<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>";
    var modalTitle = "<h4 class=\"modal-title\">" + title + "</h4>";
    return modalHeader + closeButton + modalTitle + endDiv;
}

function createModalBody(content) {
    var div = "<div class=\"modal-body\">";
    var endDiv = "</div>";
    return div + content + endDiv;
}

function createModalFooter(buttons) {
    var div = "<div class=\"modal-footer\">";
    var closeButton = "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Закрыть</button>";
    var endDiv = "</div>";
    return div + buttons + closeButton + endDiv;
}

function createModalContent(content) {
    var div = "<div class=\"modal-content\">";
    var endDiv = "</div>";
    return div + content + endDiv;
}




