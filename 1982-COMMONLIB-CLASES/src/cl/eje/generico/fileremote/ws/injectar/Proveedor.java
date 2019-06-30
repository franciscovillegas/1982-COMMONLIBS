package cl.eje.generico.fileremote.ws.injectar;

/**
 * @author Pancho
 * @deprecated
 * */
public class Proveedor {

//	String xml = "";
//	String rutProveedor = "";
//	int idCiudad = -1;
//
//	public Proveedor(String xml, String rutProveedor) {
//		this.xml = xml;
//		this.rutProveedor = rutProveedor;
//	}
//
//	public boolean crearProveedor() {
//
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder;
//		Document doc;
//		double idReq = 0;
//		try {
//			dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.toString().getBytes("utf-8"))));
//
//			String rutEmisor = getNodeText(doc, "RutEmisor");
//			String giroEmis = getNodeText(doc, "GiroEmis");
//			String razonSocial = getNodeText(doc, "RznSoc");
//			String telefono = getNodeText(doc, "Telefono");
//			String dirOrigen = getNodeText(doc, "DirOrigen");
//			String cmnaOrigen = getNodeText(doc, "CmnaOrigen");
//			String ciudadOrigen = getNodeText(doc, "CiudadOrigen");
//			int idComuna = -1;
//			int idProveedor = -1;
//			int idRegion = -1;
//			int idCondpago = 6;
//			int idFormapago = 4;
//			int vigencia = 1;
//			String sql = "INSERT INTO eje_wfgen_proveedor"
//					+ "(id_portal, nif, nombre_fantasia, razon_social, direccion, id_pais, id_ciudad, id_comuna, telefono, id_region, id_condpago, id_formapago, vigencia) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//			// VALUES (<id_portal, int,>
//			// ,<nif, varchar(20),>
//			// ,<nombre_fantasia, varchar(150),>
//			// ,<razon_social, varchar(150),>
//			// ,<direccion, varchar(150),>
//			// ,<id_pais, int,>
//			// ,<id_ciudad, int,>
//			// ,<id_comuna, int,>
//			// ,<codigo_postal, int,>
//			// ,<telefono, numeric(18,0),>
//			// ,<id_region, int,>)
//			//
//			idComuna = obtenerIdComuna(cmnaOrigen);
//			if (idComuna < 1)
//				idComuna = obtenerIdComuna(ciudadOrigen);
//
//			idRegion = obtenerIdRegion(idCiudad);
//
//			String rutSinDv = rutEmisor.split("-")[0];
//			Integer idPortal = Integer.valueOf(rutSinDv);
//			if (idPortal != null) {
//				Object[] params = { idPortal, rutEmisor, giroEmis, razonSocial, dirOrigen, 1, idCiudad, idComuna,
//						telefono, idRegion, idCondpago, idFormapago, vigencia };
//
//				try {
//					idReq = ConsultaTool.getInstance().insertIdentity("wf", sql, params);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ParserConfigurationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SAXException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return idReq > 0;
//	}
//
//	public boolean existeProveedor() {
//
//		double idReq = 0;
//		boolean ok = false;
//
//		StringBuilder sql = new StringBuilder("SELECT * FROM  eje_wfgen_proveedor").append("  WHERE nif = ?");
//		Object[] params = { rutProveedor };
//		ConsultaData data;
//
//		try {
//			System.out.println("asignar servicio");
//			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
//			ok = data.size() > 0;
//			if (data != null && data.next()) {
//				ok = true;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return ok;
//
//	}
//
//	int obtenerIdComuna(String nombreComuna) {
//
//		StringBuilder sql = new StringBuilder(" SELECT id_comuna, id_ciudad FROM eje_wfgen_comuna ")
//				.append(" WHERE  LCASE(nombre) LIKE LCASE(?)");
//		int idComuna = -1;
//		Object[] params = { nombreComuna };
//		ConsultaData data;
//		try {
//
//			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
//			if (data != null && data.next()) {
//				idComuna = data.getInt("id_comuna");
//				idCiudad = data.getInt("id_ciudad");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return idComuna;
//
//	}
//
//	int obtenerIdRegion(int idCiudad) {
//
//		StringBuilder sql = new StringBuilder(" SELECT id_region FROM eje_wfgen_ciudad ")
//				.append(" WHERE id_ciudad = ?");
//		int idRegion = -1;
//		Object[] params = { idCiudad };
//		ConsultaData data;
//		try {
//
//			data = ConsultaTool.getInstance().getData("wf", sql.toString(), params);
//			if (data != null && data.next()) {
//				idRegion = data.getInt("id_region");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return idRegion;
//
//	}
//
//	private String getNodeText(Document doc, String tagName) {
//		NodeList lista = doc.getElementsByTagName(tagName);
//		if (lista != null && lista.getLength() > 0) {
//			return lista.item(0).getTextContent();
//		}
//		return null;
//	}
//
//	private String getNodePropertyValue(Document doc, String tagName, String property) {
//		NodeList lista = doc.getElementsByTagName(tagName);
//
//		if (lista != null && lista.getLength() > 0) {
//			NamedNodeMap nodeMap = lista.item(0).getAttributes();
//			return nodeMap.getNamedItem(property).getNodeValue();
//
//		}
//
//		return null;
//	}
}
