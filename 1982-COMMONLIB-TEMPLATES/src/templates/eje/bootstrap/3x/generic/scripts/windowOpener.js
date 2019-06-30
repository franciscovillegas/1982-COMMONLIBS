function openIframe(path, cannonicalName) {
//		console.log("cn:"+cannonicalName);
//		console.log(path);
		var rand = Math.random();
		
		if(path != null && path.indexOf("?") != -1) {
			path = path + "&clase="+cannonicalName+"&r="+rand;
		}
		else {
			path = path + "?clase="+cannonicalName+"&r="+rand;
		}
		
		$("#contenedor-principal").slideUp("fast", function() {
			$("#contenedor-principal").hide();
			$("#contenedor-principal").load(path, function() {
				$("#contenedor-principal").slideDown(100);
			});	
	
		});
	}