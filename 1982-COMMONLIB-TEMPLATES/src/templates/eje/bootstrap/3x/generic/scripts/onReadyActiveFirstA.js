$(function() {
	
	$(".nav a").each(function(pos, obj) {
		if(pos == 0) {
			$(obj).click();
			$(obj).trigger("click");
		}
	});
});