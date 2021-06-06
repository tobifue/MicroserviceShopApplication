
let lastId;

$("button").click(function (e) {
    e.preventDefault();
    lastId = $(this).val();
    $.ajax({
        type: "POST",
        url: "/recom",
        //        dataType: "text/plain",
        data: {
            id: $(this).val(), // < note use of 'this' here
            access_token: $("#access_token").val()
        },
        success: function (result) {
            console.log(lastId)
            var paragraph = document.getElementById(lastId);
            if (result.length > 20)
                paragraph.textContent = 'not found';
            else
                paragraph.textContent = result;
        },
        error: function (result) {
            alert('error');
        }
    });
});

console.log("client works");