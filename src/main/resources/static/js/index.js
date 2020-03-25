function parsePattern() {
    var pattern = $("#log-pattern").val();
    $.ajax({
        url: '/api/v1/parse/pattern?pattern=' + encodeURIComponent(pattern),
        method: 'get',
        success: function (result) {
            $("#log-regular").text(result);
        },
        error: function () {
            $("#log-regular").text('解析失败，请检查pattern是否正确');
        }
    })
}
