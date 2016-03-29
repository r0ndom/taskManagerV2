$( document ).ready(function() {
    console.log( "auth page ready");
    signIn();
});


function signIn() {
    $("#sign_in").click(function () {
        console.log("request to auth page");
        $.ajax({
            url: "/login",
            method: "POST",
            success: function (url) {
                console.log("url = " + url);
                window.location.href = url;
            }
        });
    });
}
