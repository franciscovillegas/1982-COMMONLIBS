function loadMenus() {
    /*fondo: C7CBE0
      borde: 9FA5CD
      font: 2D2549
      overfondo: 2D2549
      overfont: C7CBE0
      this.menuBorder = 1;
      this.menuBorderBgColor = "#777777";
    */
    //---------------------Recursos Humanos
    window.rrhh = new Menu("rrhh");
	rrhh.addMenuItem("Misión", "javascript:AbrirMision();Descanso();");
	rrhh.addMenuItem("Marco Valórico", "javascript:AbrirMarcoValorico();Descanso();");
	rrhh.addMenuItem("Código de ética", "javascript:AbrirCodEtica();Descanso();");
    rrhh.addMenuItem("Políticas", "self.window.location='../servlet/ListaPublicaciones?htm=rrhh/politicas.htm'");
	rrhh.addMenuItem("Conózcanos", "javascript:AbrirOrganigrama();Descanso();");
	rrhh.addMenuItem("E-Mail", "self.window.location='../servlet/Tool?htm=mail/mail.html'");
	//rrhh.addMenuItem("Publicar Políticas", "self.window.location='../servlet/Tool?htm=publicador/menu_publicador.htm'");
    /*rrhh.addMenuItem("Nuestra Misión", "top.window.location='http://developer.netscape.com/openstudio/'")
    rrhh.addMenuItem("Marco Valórico", "top.window.location='http://home.netscape.com'");
    rrhh.addMenuItem("Organigrama", "top.window.location='http://home.netscape.com/download/'");
    rrhh.addMenuItem("Publicaciones", "top.window.location='http://home.netscape.com/download/'");
    rrhh.addMenuItem("Formularios", "top.window.location='http://home.netscape.com/download/'");
    rrhh.addMenuItem("Políticas", "top.window.location='http://home.netscape.com/download/'");
    rrhh.addMenuItem("Noticias", "top.window.location='http://home.netscape.com/download/'");*/

    rrhh.fontColor = "white";
    rrhh.fontFamily = "verdana";
    rrhh.fontSize="10";
    rrhh.menuBorder = 2;
    rrhh.menuBorderBgColor = "#9FA5CD";
    rrhh.menuItemBgColor = "#9FA5CD";
    rrhh.menuHiliteBgColor = "#2D2549";
    rrhh.bgColor = "#C7CBE0";
    rrhh.menuItemWidth=100;
	rrhh.menuItemHeight=15;
	rrhh.disableDrag = true;
    
    window.tipos = new Menu("Solicitud de Vacaciones");
    tipos.addMenuItem("Feriado Legal", "javascript:AbrirSolLegal();Descanso();");
	tipos.addMenuItem("Compensación Feriado Progresivo", "javascript:AbrirSolProgresivo();Descanso();");
	tipos.fontColor = "white";
    tipos.fontFamily = "verdana";
    tipos.fontSize="10";
    tipos.menuBorder = 2;
    tipos.menuBorderBgColor = "#9FA5CD";
    tipos.menuItemBgColor = "#9FA5CD";
    tipos.menuHiliteBgColor = "#2D2549";
    tipos.bgColor = "#C7CBE0";
    tipos.menuItemWidth=150;
	tipos.menuItemHeight=30;
	tipos.disableDrag = true;
	//---------------------Mis Datos
    window.misdatos = new Menu("misdatos");
    misdatos.addMenuItem("Remuneraciones", "self.window.location='../servlet/InitCertif'");
	misdatos.addMenuItem("Cartola Vacaciones", "javascript:AbrirCartolaVaca();Descanso();");
    misdatos.addMenuItem(tipos);
	misdatos.addMenuItem("Cot. Previsionales", "javascript:AbrirCotPrevision();Descanso();");
    misdatos.addMenuItem("Certificados", "self.window.location='../servlet/Tool?htm=certificados/selec_certificados.html'");
	//misdatos.addMenuItem("Cot. Previsionales", "self.window.location='../servlet/Sit_Previsional'");
    //misdatos.addMenuItem("Grupo Familiar", "self.window.location='../servlet/GrupoFamiliar'");

    misdatos.fontColor = "white";
    misdatos.fontFamily = "verdana";
    misdatos.fontSize="9";
    misdatos.menuBorder = 2;
    misdatos.menuBorderBgColor = "#9FA5CD";
    misdatos.menuItemBgColor = "#9FA5CD";
    misdatos.menuHiliteBgColor = "#2D2549";
    misdatos.bgColor = "#C7CBE0";
    misdatos.menuItemWidth=150;
	misdatos.menuItemHeight=15;
	misdatos.disableDrag = true;

    //---------------------Diario Mural
    window.mural = new Menu("mural");
    mural.addMenuItem("Actualidad", "javascript:AbrirActualidad();Descanso();");
    mural.addMenuItem("Ind. Económicos",  "self.window.location='../servlet/Tool?htm=rrhh/indicadores.htm'")
	mural.addMenuItem("Links de Interés", "self.window.location='../servlet/Tool?htm=rrhh/links.htm'");
    mural.addMenuItem("Eventos", "javascript:AbrirEventos();");
   
    mural.fontColor = "white";
    mural.fontFamily = "verdana";
    mural.fontSize="10";
    mural.menuBorder = 2;
    mural.menuBorderBgColor = "#9FA5CD";
    mural.menuItemBgColor = "#9FA5CD";
    mural.menuHiliteBgColor = "#2D2549";
    mural.bgColor = "#C7CBE0";
    mural.menuItemWidth=100;
	mural.menuItemHeight=15;
	mural.disableDrag = true;
	
	//---------------------Publicaciones
    window.publi = new Menu("publi");
    publi.addMenuItem("Diario Mural", "javascript:AbrirQSDiario();Descanso();");
	publi.addMenuItem("Revista", "javascript:AbrirQSRevista();Descanso();");
	publi.addMenuItem("Noticias", "javascript:AbrirQSNoticias();Descanso();");
	publi.addMenuItem("Beneficios", "javascript:AbrirQSBenef();Descanso();");
	   
    publi.fontColor = "white";
    publi.fontFamily = "verdana";
    publi.fontSize="10";
    publi.menuBorder = 2;
    publi.menuBorderBgColor = "#9FA5CD";
    publi.menuItemBgColor = "#9FA5CD";
    publi.menuHiliteBgColor = "#2D2549";
    publi.bgColor = "#C7CBE0";
    publi.menuItemWidth=100;
	publi.menuItemHeight=15;
	publi.disableDrag = true;
    
    //---------------------Efemerides
    window.efemerides = new Menu("efemerides");
    efemerides.addMenuItem("Cumpleaños", "self.window.location='../servlet/Cumple?hoy=1'");
    efemerides.addMenuItem("Bienvenidas", "self.window.location='../servlet/S_Welcome'")
    efemerides.addMenuItem("Despedidas", "self.window.location='../servlet/S_Finish'");
    efemerides.addMenuItem("Santoral", "self.window.location='../servlet/Santoral'");
    efemerides.addMenuItem("Matrimonios", "self.window.location='../servlet/Matrimonios'");
    efemerides.addMenuItem("Nacimientos", "self.window.location='../servlet/Nacimientos'");
    efemerides.addMenuItem("Defunciones", "self.window.location='../servlet/Defunciones'");
   
    efemerides.fontColor = "white";
    efemerides.fontFamily = "verdana";
    efemerides.fontSize="10";
    efemerides.menuBorder = 2;
    efemerides.menuBorderBgColor = "#9FA5CD";
    efemerides.menuItemBgColor = "#9FA5CD";
    efemerides.menuHiliteBgColor = "#2D2549";
    efemerides.bgColor = "#C7CBE0";
    efemerides.menuItemWidth=80;
	efemerides.menuItemHeight=15;
	efemerides.disableDrag = true;
    
    //---------------------Herramientas
    window.herramientas = new Menu("herramientas");
    herramientas.addMenuItem("Búsqueda Alfabética", "javascript:AbrirAlfabetic();Descanso();");//
    herramientas.addMenuItem("Búsqueda Gráfica", "javascript:AbrirBGaleria();Descanso();")
    //herramientas.addMenuItem("E-Mail", "self.window.location='../servlet/Tool?htm=mail/mail.html'");
    //herramientas.addMenuItem("Búsqueda Orgánica", "javascript:AbrirBOrganica();");//
	   
    herramientas.fontColor = "white";
    herramientas.fontFamily = "verdana";
    herramientas.fontSize="10";
    herramientas.menuBorder = 2;
    herramientas.menuBorderBgColor = "#9FA5CD";
    herramientas.menuItemBgColor = "#9FA5CD";
    herramientas.menuHiliteBgColor = "#2D2549";
    herramientas.bgColor = "#C7CBE0";
    herramientas.menuItemWidth=120;
	herramientas.menuItemHeight=15;
	herramientas.disableDrag = true;
	
	//---------------------Formularios
    window.formularios = new Menu("formularios");
    formularios.addMenuItem("Beneficios", "javascript:AbrirFormBeneficios();Descanso();");
	formularios.addMenuItem("Permisos", "javascript:AbrirFormPermisos();Descanso();")
    	   
    formularios.fontColor = "white";
    formularios.fontFamily = "verdana";
    formularios.fontSize="10";
    formularios.menuBorder = 2;
    formularios.menuBorderBgColor = "#9FA5CD";
    formularios.menuItemBgColor = "#9FA5CD";
    formularios.menuHiliteBgColor = "#2D2549";
    formularios.bgColor = "#C7CBE0";
    formularios.menuItemWidth=80;
	formularios.menuItemHeight=15;
	formularios.disableDrag = true;
    
    rrhh.writeMenus();
}

