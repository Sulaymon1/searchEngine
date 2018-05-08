function showSmallMenu() {
	if($(window).scrollTop() > 160) {
		$('.container .menu-small').show();
	} else {
		$('.container .menu-small').hide();
	}
}

$( document ).ready(function() {

	showSmallMenu();

	$('.row-main .action').on('click', function() {
		var thisElement = $(this);
		thisElement.parents('.row-main').toggleClass('toggled').children('.children').slideToggle(400);
	});

	$('.row-main .children td .delete').on('click', function() {
		$(this).parents('tr').fadeOut(400);
	});

	$('.row-main .parent .delete').on('click', function() {
		$(this).parents('.row-main').fadeOut(400);
	});

	$('.menu-btn-inventory').on('click', function() {
		$('html, body').animate({scrollTop:0}, 400);
	});

});

$( window ).scroll(function() {
	showSmallMenu();
});
