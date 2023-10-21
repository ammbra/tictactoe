$(document).ready(function () {

    $("#btn-player-logout").click(function () {
        $("#form-logout").submit();
    });

    if ($("#game_over").val() !== "true") {
        $(".board-cell.free").click(function (event) {
            $("#location").val(event.target.id);
            $("#mark").submit();
        });
    }

    $("#btn-again").click(function () {
        $("#again").val("true");
        $("#mark").submit();
    });


});