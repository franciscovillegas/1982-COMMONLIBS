function procEst()
{

	var lista = document.form1.destino;
	i = document.form1.destino.selectedIndex;
	//if (i != 0) {
		var dropdownObjectPath = document.form1.ciudad;
		var wichDropdown = "ciudad";
		var withWhat = lista.options[lista.selectedIndex].value;

		populateOptions(wichDropdown, withWhat);
	  //}
}


function populateOptions(wichDropdown, withWhat)
{

	//alert("cual: "+withWhat);
	o = new Array;
	i=0;

	if (withWhat == "0") {
		o[i++]=new Option("(Indicar pa�s)", "0");
		}

	if (withWhat == "1")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Buenos Aires", "Buenos Aires");
        o[i++]=new Option("Catamarca", "Catamarca");
        o[i++]=new Option("Chaco", "Chaco");
        o[i++]=new Option("Chubut", "Chubut");
        o[i++]=new Option("Corrientes", "Corrientes");
        o[i++]=new Option("C�rdoba", "C�rdoba");
        o[i++]=new Option("Entre R�os", "Entre R�os");
        o[i++]=new Option("Jujuy", "Jujuy");
        o[i++]=new Option("La Pampa", "La Pampa");
        o[i++]=new Option("La Rioja", "La Rioja");
        o[i++]=new Option("Mendoza", "Mendoza");
        o[i++]=new Option("Misiones", "Misiones");
        o[i++]=new Option("Neuqu�n", "Neuqu�n");
        o[i++]=new Option("R�o Negro", "R�o Negro");
        o[i++]=new Option("Salta", "Salta");
        o[i++]=new Option("San Juan", "San Juan");
        o[i++]=new Option("San Luis", "San Luis");
        o[i++]=new Option("Santa Cruz", "Santa Cruz");
        o[i++]=new Option("Santa Fe", "Santa Fe");
        o[i++]=new Option("Santiago del Estero", "Santiago del Estero");
        o[i++]=new Option("Tierra del Fuego", "Tierra del Fuego");
        o[i++]=new Option("Tucuman", "Tucuman");
	}
	if (withWhat == "2")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Chuquisaca", "Chuquisaca");
        o[i++]=new Option("Cochabamba", "Cochabamba");
        o[i++]=new Option("La Paz", "La Paz");
        o[i++]=new Option("Oruro", "Oruro");
        o[i++]=new Option("Potos�", "Potos�");
        o[i++]=new Option("Santa Cruz", "anta Cruz");
        o[i++]=new Option("Tarija", "Tarija");
	}
	if (withWhat == "3")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Acre", "Acre");
        o[i++]=new Option("Alagoas", "Alagoas");
        o[i++]=new Option("Amap�", "Amap�");
        o[i++]=new Option("Amazonas", "Amazonas");
        o[i++]=new Option("Bahia", "Bahia");
        o[i++]=new Option("Cear�", "Cear�");
        o[i++]=new Option("Distrito Federal", "Distrito Federal");
        o[i++]=new Option("Esp�rito Santo", "Esp�rito Santo");
        o[i++]=new Option("Goi�s", "Goi�s");
        o[i++]=new Option("Maranh�o", "Maranh�o");
        o[i++]=new Option("Mato Grosso", "Mato Grosso");
        o[i++]=new Option("Mato Grosso do Sul", "Mato Grosso do Sul");
        o[i++]=new Option("Minas Gerais", "Minas Gerais");
        o[i++]=new Option("Par�", "Par�");
        o[i++]=new Option("Para�ba", "Para�ba");
        o[i++]=new Option("Paran�", "Paran�");
        o[i++]=new Option("Pernambuco", "Pernambuco");
        o[i++]=new Option("Piau�", "Piau�");
        o[i++]=new Option("Rio de Janeiro", "Rio de Janeiro");
        o[i++]=new Option("Rio Grande do Norte", "Rio Grande do Norte");
        o[i++]=new Option("Rio Grande do Sul", "Rio Grande do Sul");
        o[i++]=new Option("S�o Paulo", "55");
      	}
	if (withWhat == "4")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Antofagasta", "Antofagasta");
        o[i++]=new Option("Atacama", "Atacama");
        o[i++]=new Option("Biob�o", "Biob�o");
        o[i++]=new Option("Coquimbo", "Coquimbo");
        o[i++]=new Option("Isla de Pascua", "Isla de Pascua");
        o[i++]=new Option("La Araucan�a", "La Araucan�a");
        o[i++]=new Option("Lib. Gral. B. O�Higgins", "Lib. Gral. B. O�Higgins");
        o[i++]=new Option("Los Lagos", "Los Lagos");
        o[i++]=new Option("Mag. y de la Ant�rtica", "Mag. y de la Ant�rtica");
        o[i++]=new Option("Maule", "Maule");
        o[i++]=new Option("Santiago", "Santiago");
        o[i++]=new Option("Tarapac�", "Tarapac�");
        o[i++]=new Option("Valpara�so", "Valpara�so");
	}
	if (withWhat == "5")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Antioquia", "Antioquia");
        o[i++]=new Option("Arauca", "Arauca");
        o[i++]=new Option("Atl�ntico", "Atl�ntico");
        o[i++]=new Option("Bol�var", "Bol�var");
        o[i++]=new Option("Boyac�", "Boyac�");
        o[i++]=new Option("Caldas", "Caldas");
        o[i++]=new Option("Casanare", "Casanare");
        o[i++]=new Option("Cauca", "Cauca");
        o[i++]=new Option("Cesar", "Cesar");
        o[i++]=new Option("C�rdoba", "C�rdoba");
        o[i++]=new Option("Cundinamarca", "Cundinamarca");
        o[i++]=new Option("Huila", "Huila");
        o[i++]=new Option("Magdalena", "Magdalena");
        o[i++]=new Option("Meta", "Meta");
        o[i++]=new Option("Nari�o", "Nari�o");
        o[i++]=new Option("Norte de Santander", "Norte de Santander");
        o[i++]=new Option("Quind�o", "Quind�o");
        o[i++]=new Option("Risaralda", "Risaralda");
        o[i++]=new Option("San Andr�s y Providencia", "San Andr�s y Providencia");
        o[i++]=new Option("SantaF� de Bogot�", "SantaF� de Bogot�");
        o[i++]=new Option("Santander", "Santander");
        o[i++]=new Option("Sucre", "Sucre");
        o[i++]=new Option("Tolima", "Tolima");
        o[i++]=new Option("Valle del Cauca", "Valle del Cauca");
	}
	if (withWhat == "6")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Alajuela", "Alajuela");
        o[i++]=new Option("Cartago", "Cartago");
        o[i++]=new Option("Guanacaste", "Guanacaste");
        o[i++]=new Option("Guanacaste", "Guanacaste");
        o[i++]=new Option("Lim�n", "Lim�n");
        o[i++]=new Option("Puntarenas", "Puntarenas");
        o[i++]=new Option("San Jos�", "San Jos�");
	}
	if (withWhat == "7")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("La Habana", "La Habana");
        o[i++]=new Option("Matanzas", "Matanzas");
	}
	if (withWhat == "8")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Azuay", "Azuay");
        o[i++]=new Option("Ca�ar", "Ca�ar");
        o[i++]=new Option("Chimborazo", "Chimborazo");
        o[i++]=new Option("El Oro", "El Oro");
        o[i++]=new Option("Guayas", "Guayas");
        o[i++]=new Option("Imbabura", "Imbabura");
        o[i++]=new Option("Loja", "Loja");
        o[i++]=new Option("Manab�", "Manab�");
        o[i++]=new Option("Pichincha", "Pichincha");
        o[i++]=new Option("Tungurahua", "Tungurahua");
	}
	if (withWhat == "9")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("La Libertad", "La Libertad");
        o[i++]=new Option("San Salvador", "San Salvador");
	}
	if (withWhat == "10")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Alava", "Alava");
        o[i++]=new Option("Albacete", "Albacete");
        o[i++]=new Option("Alicante", "Alicante");
        o[i++]=new Option("Almer�a", "Almer�a");
        o[i++]=new Option("Asturias", "Asturias");
        o[i++]=new Option("�vila", "�vila");
        o[i++]=new Option("Badajoz", "Badajoz");
        o[i++]=new Option("Baleares", "Baleares");
        o[i++]=new Option("Barcelona", "Barcelona");
        o[i++]=new Option("Burgos", "Burgos");
        o[i++]=new Option("Cantabria", "Cantabria");
        o[i++]=new Option("Castell�n", "Castell�n");
        o[i++]=new Option("Ceuta", "Ceuta");
        o[i++]=new Option("Ciudad Real", "Ciudad Real");
        o[i++]=new Option("Cuenca", "Cuenca");
        o[i++]=new Option("C�ceres", "C�ceres");
        o[i++]=new Option("C�diz", "C�diz");
        o[i++]=new Option("C�rdoba", "C�rdoba");
        o[i++]=new Option("Gerona", "Gerona");
        o[i++]=new Option("Granada", "Granada");
        o[i++]=new Option("Guadalajara", "Guadalajara");
        o[i++]=new Option("Guip�zcoa", "Guip�zcoa");
        o[i++]=new Option("Huelva", "Huelva");
        o[i++]=new Option("Huesca", "Huesca");
        o[i++]=new Option("Ja�n", "Ja�n");
        o[i++]=new Option("La Coru�a", "La Coru�a");
        o[i++]=new Option("La Rioja", "La Rioja");
        o[i++]=new Option("Las Palmas", "Las Palmas");
        o[i++]=new Option("Le�n", "Le�n");
        o[i++]=new Option("Lugo", "Lugo");
        o[i++]=new Option("L�rida", "L�rida");
        o[i++]=new Option("Madrid", "Madrid");
        o[i++]=new Option("Melilla", "Melilla");
        o[i++]=new Option("Murcia", "Murcia");
        o[i++]=new Option("M�laga", "M�laga");
        o[i++]=new Option("Navarra", "Navarra");
        o[i++]=new Option("Orense", "Orense");
        o[i++]=new Option("Palencia", "Palencia");
        o[i++]=new Option("Pontevedra", "Pontevedra");
        o[i++]=new Option("Salamanca", "Salamanca");
        o[i++]=new Option("Santa Cruz de Tenerife", "Santa Cruz de Tenerife");
        o[i++]=new Option("Segovia", "Segovia");
        o[i++]=new Option("Sevilla", "Sevilla");
        o[i++]=new Option("Soria", "Soria");
        o[i++]=new Option("Tarragona", "Tarragona");
        o[i++]=new Option("Teruel", "Teruel");
        o[i++]=new Option("Toledo", "Toledo");
        o[i++]=new Option("Valencia", "Valencia");
        o[i++]=new Option("Valladolid", "Valladolid");
        o[i++]=new Option("Vizcaya", "Vizcaya");
        o[i++]=new Option("Zamora", "Zamora");
        o[i++]=new Option("Zaragoza", "Zaragoza");
	}

	if (withWhat == "12")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Guatemala", "Guatemala");
        o[i++]=new Option("Huehuetenango", "Huehuetenango");
        o[i++]=new Option("Jalapa", "Jalapa");
        o[i++]=new Option("Sacatep�quez", "Sacatep�quez");
        o[i++]=new Option("Solol�", "Solol�");
	}

	if (withWhat == "13")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Atl�ntida", "Atl�ntida");
        o[i++]=new Option("Cort�s", "Cort�s");
        o[i++]=new Option("Francisco Moraz�n", "Francisco Moraz�n");
	}

	if (withWhat == "14")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Aguascalientes", "Aguascalientes");
        o[i++]=new Option("Baja California", "Baja California");
        o[i++]=new Option("Baja California Sur", "Baja California Sur");
        o[i++]=new Option("Campeche", "Campeche");
        o[i++]=new Option("Chiapas", "Chiapas");
        o[i++]=new Option("Chihuahua", "Chihuahua");
        o[i++]=new Option("Ciudad de M�xico", "Ciudad de M�xico");
        o[i++]=new Option("Coahuila de Zaragoza", "Coahuila de Zaragoza");
        o[i++]=new Option("Colima", "Colima");
        o[i++]=new Option("Durango", "Durango");
        o[i++]=new Option("Estado de M�xico", "Estado de M�xico");
        o[i++]=new Option("Guanajuato", "Guanajuato");
        o[i++]=new Option("Guerrero", "Guerrero");
        o[i++]=new Option("Hidalgo", "Hidalgo");
        o[i++]=new Option("Jalisco", "Jalisco");
        o[i++]=new Option("Michoac�n", "Michoac�n");
        o[i++]=new Option("Morelos", "Morelos");
        o[i++]=new Option("Nayarit", "Nayarit");
        o[i++]=new Option("Nuevo Le�n", "Nuevo Le�n");
        o[i++]=new Option("Oaxaca", "Oaxaca");
        o[i++]=new Option("Puebla", "Puebla");
        o[i++]=new Option("Quer�taro de Arteaga", "Quer�taro de Arteaga");
        o[i++]=new Option("Quintana Roo", "Quintana Roo");
        o[i++]=new Option("San Luis Potos�", "San Luis Potos�");
        o[i++]=new Option("Sinaloa", "Sinaloa");
        o[i++]=new Option("Sonora", "Sonora");
        o[i++]=new Option("Tabasco", "Tabasco");
        o[i++]=new Option("Tamaulipas", "Tamaulipas");
        o[i++]=new Option("Tlaxcala", "Tlaxcala");
        o[i++]=new Option("Veracruz", "Veracruz");
        o[i++]=new Option("Yucat�n", "Yucat�n");
        o[i++]=new Option("Zacatecas", "Zacatecas");
	}

	if (withWhat == "15")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Chontales", "Chontales");
        o[i++]=new Option("Managua", "Managua");
	}

	if (withWhat == "16")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Col�n", "Col�n");
        o[i++]=new Option("Herrera", "Herrera");
        o[i++]=new Option("Panam�", "Panam�");
	}

	if (withWhat == "17")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Alto Paran�", "Alto Paran�");
        o[i++]=new Option("Caaguaz�", "Caaguaz�");
        o[i++]=new Option("Central", "Central");
        o[i++]=new Option("Guair�", "Guair�");
        o[i++]=new Option("Itap�a", "Itap�a");
	}

	if (withWhat == "18")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Ancash", "Ancash");
        o[i++]=new Option("Arequipa", "Arequipa");
        o[i++]=new Option("Callao", "Callao");
        o[i++]=new Option("Cusco", "Cusco");
        o[i++]=new Option("Huancavelica", "Huancavelica");
        o[i++]=new Option("Ica", "Ica");
        o[i++]=new Option("Jun�n", "Jun�n");
        o[i++]=new Option("La Libertad", "La Libertad");
        o[i++]=new Option("Lambayeque", "Lambayeque");
        o[i++]=new Option("Lima", "Lima");
        o[i++]=new Option("Loreto", "Loreto");
        o[i++]=new Option("Piura", "Piura");
        o[i++]=new Option("Puno", "Puno");
        o[i++]=new Option("Tacna", "Tacna");
	}

	if (withWhat == "19")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Benedita", "Acores");
        o[i++]=new Option("Castelo Branco", "Algarve");
        o[i++]=new Option("Lisboa", "Alto Alentejo");
        o[i++]=new Option("Matosinhos", "Baixo Alentejo");
        o[i++]=new Option("Oporto", "Beira Alta");
        o[i++]=new Option("Porto Moniz ", "Beira Baixa");
        
	}


	if (withWhat == "20")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Adjuntas", "Adjuntas");
        o[i++]=new Option("Aguada", "Aguada");
        o[i++]=new Option("Aguadilla", "Aguadilla");
        o[i++]=new Option("Aguas Buenas", "Aguas Buenas");
        o[i++]=new Option("Aibonito", "Aibonito");
        o[i++]=new Option("A�asco", "A�asco");
        o[i++]=new Option("Arecibo", "Arecibo");
        o[i++]=new Option("Barceloneta", "Barceloneta");
        o[i++]=new Option("Barranquitas", "Barranquitas");
        o[i++]=new Option("Bayam�n", "Bayam�n");
        o[i++]=new Option("Cabo Rojo", "Cabo Rojo");
        o[i++]=new Option("Caguas", "Caguas");
        o[i++]=new Option("Camuy", "Camuy");
        o[i++]=new Option("Can�vanas", "Can�vanas");
        o[i++]=new Option("Carolina", "Carolina");
        o[i++]=new Option("Cata�o", "Cata�o");
        o[i++]=new Option("Cayey", "Cayey");
        o[i++]=new Option("Ceiba", "Ceiba");
        o[i++]=new Option("Ciales", "Ciales");
        o[i++]=new Option("Cidra", "Cidra");
        o[i++]=new Option("Coamo", "Coamo");
        o[i++]=new Option("Comer�o", "Comer�o");
        o[i++]=new Option("Corozal", "Corozal");
        o[i++]=new Option("Culebra", "Culebra");
        o[i++]=new Option("Dorado", "Dorado");
        o[i++]=new Option("Fajardo", "Fajardo");
        o[i++]=new Option("Florida", "Florida");
        o[i++]=new Option("Gu�nica", "Gu�nica");
        o[i++]=new Option("Guayama", "Guayama");
        o[i++]=new Option("Guayanilla", "Guayanilla");
        o[i++]=new Option("Guaynabo", "Guaynabo");
        o[i++]=new Option("Gurabo", "Gurabo");
        o[i++]=new Option("Hatillo", "Hatillo");
        o[i++]=new Option("Hormigueros", "Hormigueros");
        o[i++]=new Option("Humacao", "Humacao");
        o[i++]=new Option("Isabela", "Isabela");
        o[i++]=new Option("Jayuya", "Jayuya");
        o[i++]=new Option("Juana D�az", "Juana D�az");
        o[i++]=new Option("Juncos", "Juncos");
        o[i++]=new Option("Lajas", "Lajas");
        o[i++]=new Option("Lares", "Lares");
        o[i++]=new Option("Las Mar�as", "Las Mar�as");
        o[i++]=new Option("Las Piedras", "Las Piedras");
        o[i++]=new Option("Lo�za", "Lo�za");
        o[i++]=new Option("Luquillo", "Luquillo");
        o[i++]=new Option("Manat�", "Manat�");
        o[i++]=new Option("Maricao", "Maricao");
        o[i++]=new Option("Maunabo", "Maunabo");
        o[i++]=new Option("Mayaguez", "Mayaguez");
        o[i++]=new Option("Moca", "Moca");
        o[i++]=new Option("Morovis", "Morovis");
        o[i++]=new Option("Naguabo", "Naguabo");
        o[i++]=new Option("Naranjito", "Naranjito");
        o[i++]=new Option("Orocovis", "Orocovis");
        o[i++]=new Option("Patillas", "Patillas");
        o[i++]=new Option("Pe�uelas", "Pe�uelas");
        o[i++]=new Option("Ponce", "Ponce");
        o[i++]=new Option("Quebradillas", "Quebradillas");
        o[i++]=new Option("Rinc�n", "Rinc�n");
        o[i++]=new Option("R�o Grande", "R�o Grande");
        o[i++]=new Option("S�bana Grande", "S�bana Grande");
        o[i++]=new Option("Salinas", "Salinas");
        o[i++]=new Option("San Germ�n", "San Germ�n");
        o[i++]=new Option("San Juan", "San Juan");
        o[i++]=new Option("San Lorenzo", "San Lorenzo");
        o[i++]=new Option("San Sebasti�n", "San Sebasti�n");
        o[i++]=new Option("Santa Isabel", "Santa Isabel");
        o[i++]=new Option("Toa Alta", "Toa Alta");
        o[i++]=new Option("Toa Baja", "Toa Baja");
        o[i++]=new Option("Trujillo Alto", "Trujillo Alto");
        o[i++]=new Option("Utuado", "Utuado");
        o[i++]=new Option("Vega Alta", "Vega Alta");
        o[i++]=new Option("Vega Baja", "Vega Baja");
        o[i++]=new Option("Vieques", "Vieques");
        o[i++]=new Option("Villalba", "Villalba");
        o[i++]=new Option("Yabucoa", "Yabucoa");
        o[i++]=new Option("Yauco", "Yauco");
	}

	if (withWhat == "21")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Distrito Nacional", "Distrito Nacional");
        o[i++]=new Option("La Vega", "La Vega");
        o[i++]=new Option("Puerto Plata", "Puerto Plata");
        o[i++]=new Option("Salcedo", "Salcedo");
        o[i++]=new Option("San Pedro de Macor�s", "San Pedro de Macor�s");
        o[i++]=new Option("Santiago", "Santiago");
	}

	if (withWhat == "22")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Artigas", "Artigas");
        o[i++]=new Option("Canelones", "Canelones");
        o[i++]=new Option("Colonia", "Colonia");
        o[i++]=new Option("Flores", "Flores");
        o[i++]=new Option("Florida", "Florida");
        o[i++]=new Option("Maldonado", "Maldonado");
        o[i++]=new Option("Montevideo", "Montevideo");
        o[i++]=new Option("Paysand�", "Paysand�");
        o[i++]=new Option("Rocha", "Rocha");
        o[i++]=new Option("Salto", "Salto");
        o[i++]=new Option("San Jos�", "San Jos�");
        o[i++]=new Option("Treinta y Tres", "Treinta y Tres");
	}

	if (withWhat == "23")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Anzo�tegui", "Anzo�tegui");
        o[i++]=new Option("Aragua", "Aragua");
        o[i++]=new Option("Bol�var", "Bol�var");
        o[i++]=new Option("Carabobo", "Carabobo");
        o[i++]=new Option("Distrito Federal", "Distrito Federal");
        o[i++]=new Option("Falc�n", "Falc�n");
        o[i++]=new Option("M�rida", "M�rida");
        o[i++]=new Option("Miranda", "Miranda");
        o[i++]=new Option("Monagas", "Monagas");
        o[i++]=new Option("Nueva Esparta", "Nueva Esparta");
        o[i++]=new Option("Portuguesa", "Portuguesa");
        o[i++]=new Option("Sucre", "Sucre");
        o[i++]=new Option("T�chira", "T�chira");
        o[i++]=new Option("Trujillo", "Trujillo");
        o[i++]=new Option("Vargas", "Vargas");
        o[i++]=new Option("Yaracuy", "Yaracuy");
        o[i++]=new Option("Zulia", "Zulia");
	}
     
	if (withWhat == "11")
	{
	o[i++]=new Option("(Indicar Ciudad)", "0");
        o[i++]=new Option("Alabama", "Alabama");
        o[i++]=new Option("Alaska", "Alaska");
        o[i++]=new Option("Arizona", "Arizona");
        o[i++]=new Option("Arkansas", "Arkansas");
        o[i++]=new Option("California", "California");
        o[i++]=new Option("Colorado", "Colorado");
        o[i++]=new Option("Connecticut", "Connecticut");
        o[i++]=new Option("Delaware", "Delaware");
        o[i++]=new Option("Florida", "Florida");
        o[i++]=new Option("Georgia", "Georgia");
        o[i++]=new Option("Hawaii", "Hawaii");
        o[i++]=new Option("Idaho", "Idaho");
        o[i++]=new Option("Illinois", "Illinois");
        o[i++]=new Option("Indiana", "Indiana");
        o[i++]=new Option("Iowa", "Iowa");
        o[i++]=new Option("Kansas", "Kansas");
        o[i++]=new Option("Kentucky", "Kentucky");
        o[i++]=new Option("Louisiana", "Louisiana");
        o[i++]=new Option("Maine", "Maine");
        o[i++]=new Option("Maryland", "Maryland");
        o[i++]=new Option("Massachusetts", "Massachusetts");
        o[i++]=new Option("Michigan", "Michigan");
        o[i++]=new Option("Minnesota", "Minnesota");
        o[i++]=new Option("Mississippi", "Mississippi");
        o[i++]=new Option("Missouri", "Missouri");
        o[i++]=new Option("Montana", "Montana");
        o[i++]=new Option("Nebraska", "Nebraska");
        o[i++]=new Option("Nevada", "Nevada");
        o[i++]=new Option("New Hampshire", "New Hampshire");
        o[i++]=new Option("New Jersey", "New Jersey");
        o[i++]=new Option("New Mexico", "New Mexico");
        o[i++]=new Option("New York", "New York");
        o[i++]=new Option("North Carolina", "North Carolina");
        o[i++]=new Option("North Dakota", "North Dakota");
        o[i++]=new Option("Ohio", "Ohio");
        o[i++]=new Option("Oklahoma", "Oklahoma");
        o[i++]=new Option("Oregon", "Oregon");
        o[i++]=new Option("Pennsylvania", "Pennsylvania");
        o[i++]=new Option("Rhode Island", "Rhode Island");
        o[i++]=new Option("South Carolina", "South Carolina");
        o[i++]=new Option("South Dakota", "South Dakota");
        o[i++]=new Option("Tennessee", "Tennessee");
        o[i++]=new Option("Texas", "Texas");
        o[i++]=new Option("Utah", "Utah");
        o[i++]=new Option("Vermont", "Vermont");
        o[i++]=new Option("Virginia", "Virginia");
        o[i++]=new Option("Washington", "Washington");
        o[i++]=new Option("Washington,D.C.", "Washington,D.C.");
        o[i++]=new Option("West Virginia", "West Virginia");
        o[i++]=new Option("Wisconsin", "Wisconsin");
        o[i++]=new Option("Wyoming", "Wyoming");

	}
	
	if (withWhat == "24")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Calgary", "Calgary");
	        o[i++]=new Option("Canmore Nordic Centre", "Canmore Nordic Centre");
	        o[i++]=new Option("Montreal", "Montreal");
	        o[i++]=new Option("Ottawa", "Ottawa");
	        o[i++]=new Option("Quebec", "Quebec");
	        o[i++]=new Option("Toronto", "Toronto");
	        o[i++]=new Option("Vancouver", "Vancouver");
	        o[i++]=new Option("Winnipeg", "Winnipeg");
     	}
	if (withWhat == "25")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Cannes", "Cannes");
	        o[i++]=new Option("Chamonix", "Chamonix");
	        o[i++]=new Option("Dijon", "Dijon");
	        o[i++]=new Option("Estrasburgo", "Estrasburgo");
	        o[i++]=new Option("Le Mans", "Le Mans");
	        o[i++]=new Option("Lourdes", "Lourdes");
	        o[i++]=new Option("Lyon", "Lyon");
	        o[i++]=new Option("Marsella", "Marsella");
	        o[i++]=new Option("Nantes", "Nantes");
	        o[i++]=new Option("Par�s", "Par�s");
	        o[i++]=new Option("Reims", "Reims");
	        o[i++]=new Option("Versalles", "Versalles");
     	}
     	
     	if (withWhat == "26")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Helsinki ", "Helsinki");
	       
     	}
     	
     	if (withWhat == "27")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Atenas ", "Atenas");
	       
     	}
                      
              if (withWhat == "28")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Amsterdam ", "Amsterdam");
	        o[i++]=new Option("La Haya ", "La Haya");
	        o[i++]=new Option("Rotterdam ", "Rotterdam");
	        o[i++]=new Option("Utrecht ", "Utrecht");	       
     	}              
     	
     	 if (withWhat == "29")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Cork ", "Amsterdam");
	        o[i++]=new Option("Dubl�n", "La Haya");
	        o[i++]=new Option("Killarney ", "Rotterdam");
	}
	
     	if (withWhat == "30")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("B�rgamo", "B�rgamo");
	        o[i++]=new Option("Bolonia", "Bolonia");
	        o[i++]=new Option("Cagliari ", "Cagliari");
	        o[i++]=new Option("Florencia", "Florencia");
	        o[i++]=new Option("Imola", "Imola");
	        o[i++]=new Option("Mil�n ", "Mil�n");
	         o[i++]=new Option("N�poles", "N�poles");
	        o[i++]=new Option("Parma", "Parma");
	        o[i++]=new Option("Pisa ", "Pisa");
	        o[i++]=new Option("Roma", "Roma");
	        o[i++]=new Option("Tur�n", "Tur�n");
	        o[i++]=new Option("Venecia ", "Venecia");
	         o[i++]=new Option("Verona ", "Verona");
	         o[i++]=new Option("Vicenza ", "Vicenza");
	 }
	 
	  if (withWhat == "31")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Fujinomiya ", "Fujinomiya");
	        o[i++]=new Option("Tokio", "Tokio");
	}

                 if (withWhat == "32")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Bergen ", "Bergen");
	        o[i++]=new Option("Oslo", "Oslo");
	}
	
	   if (withWhat == "34")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Banbury ", "Banbury");
	        o[i++]=new Option("Edimburgo", "Edimburgo");
	         o[i++]=new Option("Leeds ", "Leeds");
	        o[i++]=new Option("Londres", "Londres");
	         o[i++]=new Option("Lurgan ", "Lurgan");
	        o[i++]=new Option("Plymouth", "Plymouth");
	        o[i++]=new Option("Rhyl", "Rhyl");
	         o[i++]=new Option("West Bromwich ", "West Bromwich");
	        o[i++]=new Option("Windsor", "Windsor");
	}
	
	 if (withWhat == "35")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Bucarest  ", "Bucarest");
	}
	
	  if (withWhat == "36")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Mosc�  ", "Mosc�");
	        o[i++]=new Option("San Petersburgo ", "San Petersburgo");
	}
            
            if (withWhat == "37")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Ginebra   ", "Ginebra");
	        o[i++]=new Option("Ibach Lugano", "Ibach Lugano");
	}
	
	 if (withWhat == "38")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Taipei   ", "Taipei ");
	      
	}
	
	 if (withWhat == "39")
	{       o[i++]=new Option("(Indicar Ciudad)", "0");
	        o[i++]=new Option("Belgrado    ", "Belgrado  ");
	      
	}

             

                                                                          
	if (i==0) {                                                       
		alert(i + " " + "Error!!!");                              
		}                                                         
	else{                                                             
		dropdownObjectPath = document.form1.ciudad;
		eval(document.form1.ciudad.length=o.length);
		largestwidth=0;
		for (i=0; i < o.length; i++)
			{
			  eval(document.form1.ciudad.options[i]=o[i]);
			  if (o[i].text.length > largestwidth) {
			     largestwidth=o[i].text.length;    }
	        }
		eval(document.form1.ciudad.length=o.length);
		//eval(document.form1.ciudad.options[0].selected=true);
	}

}