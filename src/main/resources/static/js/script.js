window.addEventListener("DOMContentLoaded", function () {
    var over = document.getElementById("game-over").value;

    document.getElementById("btn-again").addEventListener("click", function () {
        document.getElementById("again").value = true;
        document.getElementById("mark").submit();
    });

    document.getElementById("btn-player-logout").addEventListener("click", function () {
            document.getElementById("form-logout").submit();
    });

    if (over !== "true") {
        var elements = document.getElementsByClassName("board-cell free");
        Array.prototype.forEach.call(elements, function(el) {
            el.addEventListener("click", function (event) {
                document.getElementById("coordinate").value = event.target.id;
                document.getElementById("mark").submit();
            });
        });
    }
});