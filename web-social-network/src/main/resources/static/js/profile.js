var file;
function dragAndDrop(event) {
    event.preventDefault();
    event.stopPropagation();
    var fileName = URL.createObjectURL(event.dataTransfer.files[0]);
    var $preview = $(".image-drop");
    if($("div.image-drop img").length > 0 ) {
        $("div.image-drop > img").remove();
    } else{
        $("div.image-drop > p").remove();
    }
    var previewImg = document.createElement("img");
    previewImg.setAttribute("class", "img-circle img-thumbnail image-upload uploaded-image");
    previewImg.setAttribute("src", fileName);
    $preview.append(previewImg);
    file = event.dataTransfer.files[0];
    dragLeave(event);
};
function dragEnter(event) {
    event.preventDefault();
    event.stopPropagation();
    var $div = $("div.image-drop");
    $div.removeClass("border-dotted");
    $div.addClass("border-dotted-big");
    return false;
};
function dragLeave(event) {
    event.preventDefault();
    event.stopPropagation();
    var $div = $("div.image-drop");
    $div.removeClass("border-dotted-big");
    $div.addClass("border-dotted");
    return false;
};
$("button.btn-left").click(function (event) {
    event.preventDefault();
    var formData = new FormData();
    console.log(file);
    formData.append("imagefile", file);
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/user/uploadImage',
        type: 'POST',
        enctype: 'multipart/form-data',
        headers: { "X-CSRF-TOKEN": token },
        data: formData,
        processData: false,
        contentType: false,
        error: function () {
            alert("Please, try to use image smaller size");
        }
    }).then(function () {
        window.location.reload();
    })
});
$(function() {
    $.datepicker.regional['en'] = {
        closeText: 'Close',
        prevText: 'Prev',
        nextText: 'Next',
        currentText: 'Today',
        monthNames: ['01', '02', '03', '04', '05', '06',
            '07', '08', '09', '10', '11', '12'],
        monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
        weekHeader: 'Week',
        dateFormat: 'yy-MM-dd',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: '',};
    $.datepicker.setDefaults($.datepicker.regional['en']);
    var options = {
        changeMonth: true,
        changeYear: true,
        highlightWeek: true,
        yearRange: "-100:+0"
    };
    $( "#datepicker" ).datepicker(options);
});