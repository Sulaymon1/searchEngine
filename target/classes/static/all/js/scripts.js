
$( document ).ready(function() {

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

});