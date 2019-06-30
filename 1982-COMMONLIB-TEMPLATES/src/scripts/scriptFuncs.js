	function PageUtil() {
		this.refDocument = null;
		this.print = false;
		
		this.setPrint = function(val) {
			this.toConsole("setPrint("+val+");");
			this.print = val;
		};
		this.getPrint = function() {
			return this.print;
		};
		this.setDocument =  function(doc) {
			this.toConsole("setDocument("+doc+");");
			this.refDocument = doc;
		};
		this.getDocument = function() {
			if(this.refDocument == null) {
				return document;
			}
			else {
				return this.refDocument;
			}
		};
		this.getAndChargeScript = function(src,interestingPath, func) {
			var existe = this.getScript(src,interestingPath);
			
			if(!existe) {
				this.toConsole("[script]Cargando->"+interestingPath);
				this.loadScript(src,func);
			}
			else {
				this.toConsole("[script]Ya existe->"+src);
				var funcion = eval(func);
				funcion();
			}


		};
		this.getAndChargeStyle = function(src,interestingPath, func) {
			var existe = this.getStyle(src,interestingPath);
			
			if(!existe) {
				this.toConsole("[style]Ya existe->"+existe);
				this.loadStyle(src,func);
			}
			else {
				this.toConsole("[style]Cargando->"+src);
				var funcion = eval(func);
				funcion();
			}


		};
		this.toConsole = function (o) {
			try {
				if(this.print == true) {
					if(window.console) {
						if(typeof console.log == "function") {
							console.log(o);
						}
					}
				}
			}
			catch(e) {
				
			}
		};
		this.loadScript = function(url, callback) {
		    // adding the script tag to the head as suggested before
			   var head = this.getDocument().getElementsByTagName('head')[0];
			   var script = this.getDocument().createElement('script');
			   script.type = 'text/javascript';
			   script.src = url;

			   // then bind the event to the callback function 
			   // there are several events for cross browser compatibility
			   script.onreadystatechange = function () {
				      if (this.readyState == 'complete' || this.readyState == 'loaded' ) {
				    	  var func = eval(callback);
				    	  func();
				      }
			   };
			   script.onload = callback;

			   // fire the loadingx	
			   head.appendChild(script);
		};
		this.loadStyle = function(url, callback) {
		    // adding the script tag to the head as suggested before
			   var head = this.getDocument().getElementsByTagName('head')[0];
			   var style = this.getDocument().createElement('link');
			   style.type = 'text/css';
			   style.rel="stylesheet";
			   style.href = url;

			   // then bind the event to the callback function 
			   // there are several events for cross browser compatibility
			   style.onreadystatechange = function () {
				      if (this.readyState == 'complete' || this.readyState == 'loaded' ) {
				    	  var func = eval(callback);
				    	  func();
				      }
			   };
			   style.onload = callback;

			   // fire the loadingx	
			   head.appendChild(style);
		};
		this.getScript =  function(src,interestingPath){
	    
		    for(var i=0,scs; scs=this.getDocument().getElementsByTagName('script')[i];i++){	        
		    	if(scs.src.indexOf(interestingPath)>-1){
		    		return true;
		    	}
		    } 
		    
		    return false;
	    };
	    this.getStyle =  function(src,interestingPath){
		    
		    for(var i=0,scs; scs=this.getDocument().getElementsByTagName('link')[i];i++){	        
		    	if(scs.href.indexOf(interestingPath)>-1){
		    		return true;
		    	}
		    } 
		    
		    return false;
	    };	    
		
	};
	    
    
    
	function loadScript(url, callback) {
	    // adding the script tag to the head as suggested before
	   var head = document.getElementsByTagName('head')[0];
	   var script = document.createElement('script');
	   script.type = 'text/javascript';
	   script.src = url;

	   // then bind the event to the callback function 
	   // there are several events for cross browser compatibility
	   script.onreadystatechange = function () {
		      if (this.readyState == 'complete' || this.readyState == 'loaded' ) {
		    	  var func = eval(callback);
		    	  func();
		      }
	   }
	   script.onload = callback;

	   // fire the loadingx	
	   head.appendChild(script);
	}
	

	function getAndChargeScript(src,interestingPath, func) {

		if(!getScript(src,interestingPath)) {
			loadScript(src,func);
			toConsole("Validando->"+getScript(src,interestingPath));
		}
		else {
			toConsole("Cargando->"+src);		
			var funcion = eval(func);
			funcion();
		}


	}
	
	function toConsole(o) {
		try {
			
			if(window.console) {
				if(typeof console.log == "function") {
					console.log(o);
				}
			}

		}
		catch(e) {
			
		}
	}

	function getScript(src,interestingPath){
	    var ok = false;
	    
	    for(var i=0,scs; scs=document.getElementsByTagName('script')[i];i++){	        
	    	if(scs.src.indexOf(interestingPath)>-1){
	    		ok = true;
	    	}
	    } 
	    
	    return ok;
    }
	
	