function Modal(width,height) {
		this.width = width;
		this.height= height;
		this.idDiv = "";
		
		Modal.prototype.build = function() {

			this.idDiv = "div" + Math.floor(Math.random()*100000000);
			var divStr = "";
			divStr +=  "<table border='0' width = '"+width+"px' height='"+height+"px' cellpadding='1' cellspacing='1'> ";
			divStr +=  "	<tr style='height: 28px'> ";
			divStr +=  "		<td id='titulo_"+this.idDiv+"'>";
			divStr +=  "		</td>";
			divStr +=  "		<td colspan='1' align='right'> ";
			divStr +=  "			<a href=\"javascript:;\" onClick=\"$('#"+this.idDiv+"').css('display','none');\"> ";
			divStr +=  "				<img src='Tool?images/modal/cerrar.png' border='0' title='Cierra Ventana' width='20' height='20' /> ";
			divStr +=  "			</a> ";
			divStr +=  "		</td> ";
			divStr +=  "	</tr>";
			divStr +=  "	<tr>";
			divStr +=  "		<td id='html_"+this.idDiv+"'>";
			divStr +=  "		</td>";
			divStr +=  "	<tr>";
			divStr +=  "</table>";

			$("body").append("<div id='"+this.idDiv+"' style='display: none;z-index:99;'>"+divStr+"</div>");

			
			$("#"+this.idDiv).css("z-index","99");
			$("#"+this.idDiv).css("background-color","#C3E7F9");
			$("#"+this.idDiv).css("height", height+"px");
			$("#"+this.idDiv).css("width", width+"px");
			$("#"+this.idDiv).css("border-style", "solid");
			$("#"+this.idDiv).css("border-width", "2px");

			this.centrar();
		};

		Modal.prototype.setTitulo = function(titulo) {
			$("#titulo_"+this.idDiv).html(titulo);
		};

		Modal.prototype.setHtml = function(html) {
			$("#html_"+this.idDiv).html(html);
		}
			

		Modal.prototype.show = function() {
			$("#"+this.idDiv).show();
		};

		Modal.prototype.hide = function() {
			$("#"+this.idDiv).css('display','none');
		};

		Modal.prototype.centrar = function () {
		    $("#"+this.idDiv).css("position","absolute");
		    $("#"+this.idDiv).css("top", ( $(window).height() - $("#"+this.idDiv).height() ) / 2+$(window).scrollTop() + "px");
		    $("#"+this.idDiv).css("left", ( $(window).width() - $("#"+this.idDiv).width() ) / 2+$(window).scrollLeft() + "px");
		}
	}