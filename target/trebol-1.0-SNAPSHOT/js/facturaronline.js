$(
function () { 
$("[data-toggle='tooltip']").tooltip({'placement': 'right'}); 
$("[data-toggle='popover']").popover({trigger: 'hover','placement': 'bottom'});
}

);

//AFTER AJAX REFRESH
$(document).ajaxComplete(function() {
$("[data-toggle='tooltip']").tooltip({'placement': 'right'}); 
$("[data-toggle='popover']").popover({trigger: 'hover','placement': 'bottom'});
});

