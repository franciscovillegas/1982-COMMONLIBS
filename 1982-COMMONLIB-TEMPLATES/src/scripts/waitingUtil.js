$( document ).ready(function() {
	initDialogWaiting();
});

function initDialogWaiting() {
			console.log("initDialogWaiting...");

			if( $("#dialog-waiting").length > 0) {
				$("#dialog-waiting").remove();
			}
			
			var div = "<div id='dialog-waiting' title='Sistema'>";
			div += "<label for='name'>Esperando al servidor, por favor espere un momento...</label>";
			div += "</div>";
			
			$("body").append(div);
			
			
			$( "#dialog-waiting" ).dialog({
			      autoOpen: false,
			      height: 100,
			      width: 250,
			      modal: true,
			      resizable: false,  
			      buttons: {
				       
			      },
			      close: function() {

			      },
			      open: function()   { 	
			    	  $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
		          }
			});
}

function openWaiting() {
	$( "#dialog-waiting" ).dialog({
	      autoOpen: false,
	      height: 100,
	      width: 250,
	      modal: true,
	      resizable: false,  
	      buttons: {
		       
	      },
	      close: function() {

	      },
	      open: function()   { 	
	    	  $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
          }
	    }).dialog("open" );
}


function closeWaiting() {
	$( "#dialog-waiting" ).dialog().dialog("close" );
}