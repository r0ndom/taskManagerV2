$( document ).ready(function() {
    console.log("schema page ready");
    downloadImage();
});

function downloadImage() {
    $.ajax({
        url: "/schema/image",
        method: "GET",
        success: function (data) {
            $(".container").html(createImage(data));
        }
    });
}

function createImage(data) {
    return "<img src=\"data:image/png;base64," + data + "\">";
}
