package portal.com.eje.frontcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NotImplementedException;

import com.thoughtworks.xstream.XStream;

import cl.eje.bootstrap.ifaces.IPageResource;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPageRenderer;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.fileupload.FileService;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.ioutils.EnumDataEscape;
import portal.com.eje.frontcontroller.ioutils.IIOBootstrap;
import portal.com.eje.frontcontroller.ioutils.IOBootstrap;
import portal.com.eje.frontcontroller.ioutils.IOUtilBolsaDeGatos;
import portal.com.eje.frontcontroller.ioutils.IOUtilCsv;
import portal.com.eje.frontcontroller.ioutils.IOUtilDomain;
import portal.com.eje.frontcontroller.ioutils.IOUtilExcel;
import portal.com.eje.frontcontroller.ioutils.IOUtilFile;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.frontcontroller.ioutils.IOUtilHtmlMessage;
import portal.com.eje.frontcontroller.ioutils.IOUtilImage;
import portal.com.eje.frontcontroller.ioutils.IOUtilJson;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.IOUtilRedirectLogged;
import portal.com.eje.frontcontroller.ioutils.IOUtilSencha;
import portal.com.eje.frontcontroller.ioutils.IOUtilSenchaTree;
import portal.com.eje.frontcontroller.ioutils.IOUtilTracking;
import portal.com.eje.frontcontroller.ioutils.IOUtilXml;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.IDomainDetecter;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.TreeGenerico;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;
import portal.com.eje.portal.pdf.HttpFlow;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.httpservlet.WriterDestination;
import portal.com.eje.tools.ArrayFactory;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.excel.xlsx.IXlsx2Row;
import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;

public class IOClaseWeb extends IOClaseWebLight implements WriterDestination, IDomainDetecter {
	protected TransactionConnection cons;
	private IIOBootstrap bootstrap;

	public IOClaseWeb(MyHttpServlet myHttpServlet, HttpServletRequest req, HttpServletResponse resp) {
		super(myHttpServlet, req, resp);
	}

	public IOClaseWeb(TransactionConnection cons, MyHttpServlet myHttpServlet, HttpServletRequest req, HttpServletResponse resp) {
		super(myHttpServlet, req, resp);
		this.cons = cons;
	}

	
	/**
	 * Crea una tabla con la información del archivo excel subido, el parámetro
	 * fileInput indica el nombre del parámetro que tiene el archivo Excel
	 * 
	 * @throws Exception
	 * @revision 25-05-2018 Pancho
	 */
	public double createBolsaDeGatosByRut(String paramNameFileInput, int idtipo, String tipo, String descripcion,
			int periodo) throws Exception {
		return IOUtilBolsaDeGatos.getIntance().createBolsaDeGatosByRut(this, paramNameFileInput, idtipo, tipo, descripcion, periodo);
	}

	/**
	 * Desencrypta valor
	 * @revision 25-05-2018 Pancho
	 */
	public String decryptValue(String toDesencrypt) {

		return IOUtilParam.getInstance().decryptValue(this, toDesencrypt);
	}

	/**
	 * Genera un valor encriptado
	 * @revision 25-05-2018 Pancho
	 */
	public String encryptValue(double toEncrypt) {
		return encryptValue(String.valueOf(toEncrypt));
	}
	
	/**
	 * Genera un valor encriptado
	 * @revision 25-05-2018 Pancho
	 */
	public String encryptValue(String toEncrypt) {
		return IOUtilParam.getInstance().encryptValue(this, toEncrypt);
	}

	/**
	 * Genera un valor encryptado para ser transmitido por URL
	 * @revision 25-05-2018 Pancho
	 */
	public String escapeEncryptValue(String toEncrypt) {
		return IOUtilParam.getInstance().escapeEncryptValue(this, toEncrypt);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public boolean existParamFile(String nomParam) {
		return IOUtilParam.getInstance().existParamFile(this, nomParam);
	}

	/**
	 * Transaction Connection no necesita freeConnection
	 * @deprecated
	 * @author Pancho
	 * @since 17-05-2019
	 * */
	public void freeConnection(String key, Connection conn) {
		
	}

	
	public Connection getConnection(String key) {
		return getTransactionConnection().getConnection(key);
	}

	/**
	 * Retorna /cliente/modulo
	 * @revision 25-05-2018 Pancho
	 */

	public String getContext() {
		return IOUtilDomain.getIntance().getContext();
	}

	/**
	 * Retorna /cliente
	 * @revision 25-05-2018 Pancho
	 */
	public String getContextCliente() {
		return IOUtilDomain.getIntance().getContextCliente();
	}

	/**
	 * Retorna /cliente/modulo
	 * @revision 25-05-2018 Pancho
	 */
	public String getContextModulo() {
		return IOUtilDomain.getIntance().getContextModulo();
	}

	/**
	 * Retorna http://www.peoplemanager.cl/ , bajo el protocolo https si es necesario.
	 * @revision 25-05-2018 Pancho
	 */
	public String getDomain() {
		return IOUtilDomain.getIntance().getDomain(this);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public FileService getFileService() {
		return IOUtilFile.getInstance().getFileService(this);

	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public JSarrayDataOut getJSArray(ConsultaData data) {
		return IOUtilSencha.getInstance().getJSArray(this, data);
	}

	public String[] getParamArray(String nomParam) {
		return IOUtilParam.getInstance().getParamArray(this, nomParam);
	}

	/**
	 * Retorna un Objeto ArrayFactory conteniendo a todos los items del
	 * parámetro que están separados por coma.<br/>
	 * El string que viene bajo el nombre del parámetro indicado debe estar
	 * limpio, en donde, cada item de la lista está separado por coma. br/>
	 * <ul>
	 * <li>Ejempo: 1,2,3,4,5,6,7,8....,99999</li>
	 * </ul>
	 * @revision 25-05-2018 Pancho
	 */
	@SuppressWarnings("deprecation")
	public ArrayFactory getParamArrayFactory(String nomParam) {
		IOUtilParam util = getUtil(IOUtilParam.class);
		return util.getParamArrayFactory(this, nomParam);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public cl.ejedigital.tool.strings.ArrayFactory getParamArrayFactory2(String nomParam) {
		return IOUtilParam.getInstance().getParamArrayFactory2(this, nomParam);
	}

	public <T> Collection<T> getParamCollection(String nameParam,Class<T> clazz) {
		return IOUtilParam.getInstance().getParamCollection(this, nameParam, clazz);
	}
	
	public <T>Collection<T> getParamCollectionEncrypted(String paramName, Class<T> clazz) {
		return IOUtilParam.getInstance().getParamCollectionEncrypted(this, paramName, clazz);
	}

	public <T> T getParamCollectionSingleEncrypted(String paramName, Class<T> clazz) {
		return IOUtilParam.getInstance().getParamCollectionSingleEncrypted(this, paramName, clazz);
	}
	
	public <T> T getParamCollectionSingle(String nameParam,Class<T> clazz) {
		return IOUtilParam.getInstance().getParamCollectionSingle(this, nameParam, clazz);
	}
	
	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public ConsultaData getParamConsultaData(String nomParam) {
		return IOUtilParam.getInstance().getParamConsultaData(this, nomParam);
	}

	/**
	 * 
	 * Obtiene el parámetro fecha en el formato deseado, siempre será en String
	 * por lo que se deberá entregar el formato de la fecha a obtener<br/>
	 * fecha_iniciofin: {27/06/2017}
	 * 
	 * @since 2017-06-22
	 * @author Pancho
	 * @revision 25-05-2018 Pancho
	 */
	public String getParamDateAsString(String paramName, String formatIn, String formatOut, String defaultValue) {
		return IOUtilParam.getInstance().getParamDateAsString(this, paramName, formatIn, formatOut, defaultValue);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */

	public Double getParamDouble(String nomParam, Double defValue) {
		return IOUtilParam.getInstance().getParamDouble(this, nomParam, defValue);
	}

	/**
	 * Al momento de recibir un archivo por métoo POST, con
	 * enctype="multipart/form-data", es posible recuperar el archivo Excel a
	 * través de este método, </br>
	 * La hoja indicada por su posición entendiendo que parte desde 0 se
	 * transformará en el objeto ConsultaData. </br>
	 * Todos los valores serán retornados en formato String, las fechas en el
	 * caso que existan serán transformadas en ANSII, en el caso de no
	 * incorporar horas, estas serán transformadas en 12:00. (Medio día) El
	 * desarrollador deberá tener claro que a la hoja indicada debe tener un
	 * límite manejable, ya que es posible que el archivo Excel al ser muy
	 * grande sature la memoria al construir el objeto “ConsultaData”, en el
	 * caso que esto ocurra se recomienda utilizar el método del mismo nombre
	 * pero que como parámetro recibe un manipulador de filas.
	 * 
	 * @param hojaPosition:
	 *            0 a n
	 * @since 2015 Abril 31
	 * @author Francisco
	 * @throws Exception
	 * 
	 * 
	 * 
	 */

	public ConsultaData getParamExcel(String nameParam, boolean saltaPrimeraFila, Integer hojaPosition,
			Map<String, CellExcelBasicTypes> definition) throws Exception {
		return IOUtilExcel.getIntance().getParamExcelPrivate(this, nameParam, saltaPrimeraFila, hojaPosition, definition);
	}

	/**
	 * Al momento de recibir un archivo por métoo POST, con
	 * enctype="multipart/form-data", es posible recuperar el archivo Excel a
	 * través de este método, </br>
	 * La hoja indicada por su posición entendiendo que parte desde 0 será
	 * manipulada por la implementación de IXlsx2Row
	 * 
	 * 
	 * @since 2015 Abril 31
	 * @author Francisco
	 * 
	 */
	public void getParamExcel(String nameParam, boolean saltaPrimeraFila, String hojaName, int cantColumnas,
			IXlsx2Row manipulador, Map<String, CellExcelBasicTypes> definition) {
		IOUtilExcel.getIntance().getParamExcel(this, nameParam, saltaPrimeraFila, hojaName, cantColumnas, manipulador, definition);
	}

	public ConsultaData getParamExcel(String nameParam, boolean saltaPrimeraFila, String hojaName,
			Map<String, CellExcelBasicTypes> definition) throws Exception {
		return IOUtilExcel.getIntance().getParamExcelPrivate(this, nameParam, saltaPrimeraFila, hojaName, definition);
	}

	/**
	 * Entrega un objeto ConsultaData para la hoja indicada del archivo excel
	 * enviado a servidor.<br/>
	 * Siempre tomará el nombre de columna como indicador del campo, por norma
	 * del Objeto ConsultaData los nombre se transformarán a minúsculas.<br/>
	 * 
	 * @author SUPER-PANCHO
	 * @since 2016-03-04
	 * 
	 */
	public ConsultaData getParamExcel_ColRenamed(String nameParam, Integer hojaPosition,
			Map<String, CellExcelBasicTypes> definition, boolean useColumnNames) throws Exception {
		return IOUtilExcel.getIntance().getParamExcel_ColRenamed(this, nameParam, hojaPosition, definition, useColumnNames);
	}
	
	public List<String> getParamExcelSheetsName(String nameParam) throws Exception {
		return IOUtilExcel.getIntance().getParamExcelSheetsName(this, nameParam);
	}

	public File getParamFile(String nomParam) {
		return IOUtilParam.getInstance().getParamFile(this, nomParam);
	}

	/**
	 * Retorna una lista de objetos tipo String.<br/>
	 * El string que viene bajo el nombre del parámetro indicado debe estar
	 * limpio, en donde, cada item de la lista está separado por coma. br/>
	 * <ul>
	 * <li>Ejempo: 1,2,3,4,5,6,7,8....,99999</li>
	 * </ul>
	 * @since 25-05-2018 
	 * @author Pancho
	 */
	public List<String> getParamList(String nomParam) {

		return IOUtilParam.getInstance().getParamList(this, nomParam);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public List<Map<String, String>> getParamListFromJavascript(String paramName) {
		return IOUtilParam.getInstance().getParamListFromJavascript(this, paramName);
	}

	public Map<String, String> getParamMapFromJavascript(String paramName) {
		return IOUtilParam.getInstance().getParamMapFromJavascript(this, paramName);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public int getParamNumEncrypted(String nomParam, int defValue) {
		return IOUtilParam.getInstance().getParamNumEncrypted(this, nomParam, defValue);
	}

	/**
	 * Get valor Encriptado
	 * 
	 * @since 08-05-2018
	 * @author Pancho
	 * @revision 25-05-2018 Pancho
	 */
	public String getParamStringEncrypted(String nomParam, String defValue) {
		return IOUtilParam.getInstance().getParamStringEncrypted(this, nomParam, defValue);
	}

	public IIOSenchaJsonTree getSenchaJsonTree() {
		return IOUtilSenchaTree.getIntance().getSenchaJsonTree(this);
	}


	/**
	 * Retorna el flujo de al juntar un modelo SimpleHash con una vista
	 * TemplatePath
	 * 
	 * @since 23-05-2018
	 * @author Pancho
	 * @revision 25-05-2018 Pancho
	 */
	public HttpFlow getTemplateFlow(String templatePath, SimpleHash modelRoot) {
		return IOUtilFreemarker.getInstance().getTemplateFlow(this, templatePath, modelRoot);
	}

	/**
	 * Retorna el flujo de al juntar un modelo SimpleHash con una vista
	 * TemplatePath<br/>
	 * Debe retornar un HttpFlow
	 * 
	 * @deprecated
	 * @since 23-05-2018
	 * @author Pancho
	 */
	public String getTemplateProcces(String templatePath, SimpleHash modelRoot) {
		return IOUtilFreemarker.getInstance().getTemplateProcces(this, templatePath, modelRoot);
	}

	/**
	 * Retorna http://www.peoplemanager.cl/cliente/modulo, bajo el protocolo
	 * https si es necesario.
	 */
	public String getUrlContext() {
		return IOUtilDomain.getIntance().getUrlContext(this);
	}

	/**
	 * @since 02-agosto-2016
	 * @author Francisco
	 * 
	 *         Importa por a través de la implementación de FileService el
	 *         archivo que viene con el parámetro indicado.<br/>
	 *         En lo mismo que ejecutar la siguientes lineas, suponiendo que le
	 *         nombre del parámetro es <b>"file"</b> y el tipo de archivo que
	 *         contiene es la <b>"imagen del trabajador"</b>: <br/>
	 *         <br/>
	 *         <i> File f = io.getFile("file");<br/>
	 *         <br/>
	 *         FileService fs = new FileService(io.getServletContext());<br/>
	 *         Integer newID = fs.addFile(io.getUsuario().getRutIdInt(), f,
	 *         "fotografía_trabajador")<br/>
	 *         <br/>
	 *         return newID;<br/>
	 *         </i> <br/>
	 *         <b>Es importante mencionar que si no es posible crear la relación
	 *         , sea por que no viene archivo o algún otro problema, el método
	 *         retornará null.</b>
	 * @revision 25-05-2018 Pancho
	 */

	public Integer importParamFile(String nomParam, String tipArchivoCargado) {
		return IOUtilFile.getInstance().importParamFile(this, nomParam, tipArchivoCargado);
	}

	public Integer importParamImagen(String paramName, String descripcion, Integer w, Integer h,
			boolean forceProportions) throws IOException {
 		return IOUtilImage.getIntance().importParamImagen(this, paramName, descripcion, w, h, forceProportions);
	}

	/**
	 * Bajo la nueva filosofía de contrucción: modulo-thing-accion, deberá
	 * ocuparse le método insTrackingModulo
	 * 
	 * @see insTrackingModulo(Class clazz, String accion, String descripcion,
	 *      String datos)
	 * @deprecated
	 */
	public void insTracking(String descripcion, String datos) {
		IOUtilTracking.getIntance().insTracking(this, descripcion, datos);
	}

	/**
	 * Bajo la nueva filosofía de contrucción: modulo-thing-accion, deberá
	 * ocuparse le método insTrackingModulo
	 * 
	 * @see insTrackingModulo(Class clazz, String accion, String descripcion,
	 *      String datos)
	 * @deprecated
	 */
	public void insTracking(String direcRel, String descripcion, String datos) {
		IOUtilTracking.getIntance().insTracking(this, direcRel, descripcion, datos);
	}

	/*
	 * Genera un registro de tracking cuando se llama al módulod e forma
	 * insegura
	 */
	public void insTrackingModulo_Inseguro(int idUsuario, Class<?> clazz, String accion, String descripcion,
			String datos) {
		IOUtilTracking.getIntance().insTrackingModulo_Inseguro(this, idUsuario, clazz, accion, descripcion, datos);
	}

	/**
	 * Imprime por consola la definición completa de parámetros
	 * 
	 * @since 3 mayo 2016
	 * @author Francisco
	 */
	public void printParams() {
		IOUtilParam.getInstance().printParams(this);
	}

	/**
	 * Imprime a través del parámetro entregado la definición completa de
	 * parámetros
	 * 
	 * @since 21 Sept 2015
	 * @author Francisco
	 * @revision 25-05-2018 Pancho
	 */

	public void printParams(PrintStream out) {
		IOUtilParam.getInstance().printParams(this, out);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public void printParams(String paramName) {
		IOUtilParam.getInstance().printParams(this, paramName);
	}

	/**
	 * Borra bolsa de gatos
	 * 
	 */
	public boolean removeBolsaDeGatosByRut(int idUpload) throws SQLException {
		return IOUtilBolsaDeGatos.getIntance().removeBolsaDeGatosByRut(this, idUpload);
	}

	public BufferedImage resize(BufferedImage img, int newW, int newH) {
		return IOUtilImage.getIntance().resize(img, newW, newH);
	}

	public boolean retCSV(ConsultaData data, String fileName) {
		return IOUtilCsv.getIntance().retCSV(this, data, fileName);

	}

	public boolean retExcel(ConsultaData data, String fileName) {
		return retExcel(data, fileName, getCronometro(), null, new HashMap<String, String>());
	}

	public boolean retExcel(ConsultaData data, String fileName, Cronometro cro) {

		return retExcel(data, fileName, cro, null, new HashMap<String, String>());
	}

	public boolean retExcel(ConsultaData data, String fileName, Cronometro cro, Integer rowHeight) {
		return retExcel(data, fileName, cro, rowHeight, null);
	}

	public boolean retExcel(ConsultaData data, String fileName, Cronometro cro, Integer rowHeight,
			Map<String, String> rowMapping) {
		return retExcel(data, fileName, cro, rowHeight, rowMapping, null);
	}

	public boolean retExcel(ConsultaData data, String fileName, Cronometro cro, Integer rowHeight,
			Map<String, String> rowMapping, String strTemplate) {
		return IOUtilExcel.getIntance().retExcel(this, data, fileName, cro, rowHeight, rowMapping, strTemplate);
	}

	public boolean retExcel(ConsultaData data, String fileName, Integer rowHeight) {
		return retExcel(data, fileName, getCronometro(), rowHeight, new HashMap<String, String>());
	}

	public boolean retExcel(ConsultaData data, String fileName, Map<String, String> rowMapping) {
		return retExcel(data, fileName, getCronometro(), null, rowMapping);
	}

	public boolean retExcel(ConsultaData data, String fileName, String strTemplate) {
		return retExcel(data, fileName, getCronometro(), null, new HashMap<String, String>(), strTemplate);
	}

	public boolean retExcel(String templatePath, String fileName) {
		return IOUtilExcel.getIntance().retExcel(this, templatePath, fileName);
	}

	public boolean retExcel(String templatePath, String fileName, SimpleHash modelRoot) {
		return IOUtilExcel.getIntance().retExcel(this, templatePath, fileName, modelRoot);
	}

	public boolean retExcelConError(String fileName) {
		return IOUtilExcel.getIntance().retExcelConError(this, fileName);
	}

	/*
	 * Retorna lo mismo que : new Gson().fromJson(getParamString(paramName,
	 * null), List.class);
	 */

	public boolean retJson(ConsultaData cData) {
		return IOUtilJson.getIntance().retJson(this, cData);
	}

	public boolean retJson(DataList lista) {
		return IOUtilJson.getIntance().retJson(this, lista);
	}

	public boolean retJson(Map<String, String> map) {
		return IOUtilJson.getIntance().retJson(this, map);
	}

	/**
	 * Retorna un json desde una consulta SQL, actúa de forma directa.
	 * 
	 * @since 2015-07-17
	 * @author Francisco
	 */

	public void retJsonFromSelect(String jndi, String sql) {
		IOUtilJson.getIntance().retJsonFromSelect(this, jndi, sql, null);
	}

	/**
	 * Retorna un json desde una consulta SQL, actúa de forma directa.
	 * 
	 * @since 2015-07-17
	 * @author Francisco
	 */

	public void retJsonFromSelect(String jndi, String sql, Object[] params) {
		IOUtilJson.getIntance().retJsonFromSelect(this, jndi, sql, params);
	}

	public void retPageWithMessage(EMessageHtmlLevel lvl, String message) {
		this.retPageWithMessage(lvl, message, null);
	}

	public void retPageWithMessage(EMessageHtmlLevel lvl, String message, String urlAfter) {
		IOUtilHtmlMessage.getInstance().retPageWithMessage(this, lvl, message, urlAfter);
	}
	

	public void retPageWithMessage(String message) {
		this.retPageWithMessage(null, message, null);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public boolean retSenchaJson(boolean valor) {
		return IOUtilSencha.getInstance().retSenchaJson(this, valor);
	}

	/**
	 * @revision 04-03-2019
	 * */
	public boolean retSenchaJson(Boolean valor) {
		return IOUtilSencha.getInstance().retSenchaJson(this, valor);
	}
	
	/**
	 * No tiene sentido
	 * 
	 * @deprecated
	 * @author Pancho
	 * @since 24-05-2018
	 */
	public boolean retSenchaJson(boolean valor, String msgSuccess, String msgError) {
		if (valor) {
			return retSenchaJson(msgSuccess, valor);
		} else {
			return retSenchaJson(msgError, valor);
		}
	}

	public <T> boolean retSenchaJson(Collection<? extends Vo> collection) {
		return IOUtilSencha.getInstance().retSenchaJson(this,collection);
	}
	
	public <T> boolean retSenchaJson(Collection<? extends Vo> collection, boolean encryptPrimaryAndForeignKeys) throws Exception {
		return IOUtilSencha.getInstance().retSenchaJson(this,collection, encryptPrimaryAndForeignKeys);
	}
	
	/**
	 * No está incluyendo las referencias
	 * @deprecated
	 * @author Pancho
	 * */
	public <T> boolean retSenchaJsonIncluyeRefs(Collection<? extends Vo> collection) {
		return IOUtilSencha.getInstance().retSenchaJsonIncluyeRefs(this,collection);
	}
	
	public <T> boolean retSenchaJson(T vo) {
		if(vo != null && !(vo instanceof Vo)) {
			throw new RuntimeException(vo.getClass()+ " No extiende de Vo ");
		}

		return IOUtilSencha.getInstance().retSenchaJson(this, (Vo)vo);
	}

	public boolean retSenchaJson(Enum<?> e) {
		IOUtilSencha.getInstance().retSenchaJson(this, e);
		
		
		
		return true;
	}
	
	public boolean retSenchaJson(ConsultaData data) {
		return retSenchaJson(data, true);
	}

	public boolean retSenchaJson(ConsultaData data, boolean estado) {
		return this.retSenchaJson(data, estado, null);
	}

	private boolean retSenchaJson(ConsultaData data, boolean estado, String message) {

		return IOUtilSencha.getInstance().retSenchaJson(this, data, null, estado, message);
	}

	public boolean retSenchaJson(ConsultaData data, ConsultaDataPageRenderer pageRenderer) {
		return retSenchaJson(data, pageRenderer, true);
	}
	
	public boolean retSenchaJson(ConsultaData data, ConsultaDataPageRenderer dataRenderer, boolean estado) {
		return IOUtilSencha.getInstance().retSenchaJson(this, data, dataRenderer, estado, null);
	}

	public boolean retSenchaJson(DataList list) {
		return IOUtilSencha.getInstance().retSenchaJson(this, list, true);
	}
	
	public boolean retSenchaJson(DataList list, boolean estado) {
		return IOUtilSencha.getInstance().retSenchaJson(this, list, estado);
	}

	public boolean retSenchaJson(double valor) {
		return IOUtilSencha.getInstance().retSenchaJson(this, String.valueOf(valor));
	}

	/**
	 * Se repreca por que choca el método {@link #retSenchaJson(Collection)}
	 * @revision 25-05-2018 Pancho
	 * @deprecated
	 * */
	public boolean retSenchaJson(Map<?,?> row) {
		return retSenchaJson(row, true);
	}
	
	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public boolean retSenchaJson(Map<?,?> row, boolean valor) {
		return IOUtilSencha.getInstance().retSenchaJson(this, row, valor);
	}
	
	/**
	 * Retorna un mensaje en el parámetro message
	 */
	public boolean retSenchaJson(String mensaje) {
 
		return IOUtilSencha.getInstance().retSenchaJson(this, mensaje, true);
	}

	/**
	 * El <b>valor</b> es el parámetro <b>success</b> en la respuesta
	 * @revision 25-05-2018 Pancho
	 */
	public boolean retSenchaJson(String mensaje, boolean valor) {
		return IOUtilSencha.getInstance().retSenchaJson(this, mensaje, valor);
	}

	/**
	 * ConsultaData[] el json no lo permite, no tiene regla válida
	 * @deprecated
	 * */
	public boolean retSenchaJson(String mensaje, ConsultaData[] adicional, boolean valor) {
		return IOUtilSencha.getInstance().retSenchaJson  (this, mensaje, adicional, valor);
	}

	/**
	 * @revision 25-05-2018 Pancho
	 * */
	public void retSenchaJson(Throwable e) {
		
		IOUtilSencha.getInstance().retSenchaJson(this, e);
	}
	
	/**
	 * Retorna true o false según corresponda, se tomará como false solo la
	 * ejecución incorrecta de la query, si en vez de Insert la consulta tiene
	 * valores select o updates solo entregará false cuando la sentencia no
	 * pueda ser ejecutada.
	 */
	public boolean retSenchaJsonFromInsert(String jndi, String sql) {
		return retSenchaJsonFromInsert(jndi, sql, null);
	}

	/**
	 * Retorna true o false según corresponda, se tomará como false solo la
	 * ejecución incorrecta de la query, si en vez de Insert la consulta tiene
	 * valores select o updates solo entregará false cuando la sentencia no
	 * pueda ser ejecutada.
	 */

	public boolean retSenchaJsonFromInsert(String jndi, String sql, Object[] params) {
		return IOUtilSencha.getInstance().retSenchaJsonFromInsert(this, jndi, sql, params);
	}

	public boolean retSenchaJsonFromSelect(String jndi, String sql) {
		return retSenchaJsonFromSelect(jndi, sql, null);
	}

	public boolean retSenchaJsonFromSelect(String jndi, String sql, Object[] params) {
		return IOUtilSencha.getInstance().retSenchaJsonFromSelect(this, jndi, sql, params);
	}

	public boolean retSenchaJsonPaginadoFromSelect(String jndi, String sql, String... pks) {
		return IOUtilSencha.getInstance().retSenchaJsonPaginadoFromSelect(this, jndi, sql, null, pks);
		
	}
	
	public boolean retSenchaJsonPaginadoFromSelect(String jndi, String sql, Object[] params, String... pks) {
		return IOUtilSencha.getInstance().retSenchaJsonPaginadoFromSelect(this, jndi, sql, params, pks);
		
	}
	
	/**
	 * Retorna true o false según corresponda, se tomará como false: <br/>
	 * <br/>
	 * 
	 * a) La ejecución incorrecta de la query,<br/>
	 * b) 0 registros modificados.<br/>
	 * 
	 */
	public boolean retSenchaJsonFromUpdate(String jndi, String sql) {
		return retSenchaJsonFromUpdate(jndi, sql, null);
	}

	/**
	 * Retorna true o false según corresponda, se tomará como false: <br/>
	 * <br/>
	 * 
	 * a) La ejecución incorrecta de la query,<br/>
	 * b) 0 registros modificados.<br/>
	 * 
	 */
	public boolean retSenchaJsonFromUpdate(String jndi, String sql, Object[] params) {
		return IOUtilSencha.getInstance().retSenchaJsonFromUpdate(this, jndi, sql, params);
	}

	public void retSenchaTree(IUnidadGenerica unidad) {
		IOUtilSenchaTree.getIntance().retSenchaTree(this, unidad);
	}
	
	public void retSenchaTree(TreeGenerico<?> tree) {
		if(tree != null) {
			IOUtilSenchaTree.getIntance().retSenchaTree(this, tree.getRoot(), tree.getStyle());	
		}
		else {
			retSenchaJson(false);
		}
	}

	public void retSenchaTree(IUnidadGenerica unidad, UnidadGenericaStyleDef style) {
		IOUtilSenchaTree.getIntance().retSenchaTree(this, unidad, style);
	}

	/**
	 * Genera una orgánica según las caracteristicas del usuario logeado<br/>
	 * <ul>
	 * <li>Si es jefatura; se mostrarán todas las unidades descendientes</li>
	 * <li>Si no lo es; solo se mostrará la unidad</li>
	 * </ul>
	 */

	public void retSenchaTree_miEstructura(UnidadGenericaStyleDef u) {
		IOUtilSenchaTree.getIntance().retSenchaTree_miEstructura(this, u);
	}

	/**
	 * Genera una orgánica de la compañía completa
	 * @revision 25-05-2018 Pancho
	 */
	public void retSenchaTreeAllCompanies() {
		IOUtilSenchaTree.getIntance().retSenchaTreeAllCompanies(this);
	}

	/**
	 * Genera una orgánica de la compañía completa
	 * @revision 25-05-2018 Pancho
	 */
	public void retSenchaTreeAllCompanies(IIOSenchaJsonTreeNodeListener nd) {
		IOUtilSenchaTree.getIntance().retSenchaTreeAllCompanies(this, nd);
	}

	/**
	 * Genera una orgánica de la compañía completa
	 * @revision 25-05-2018 Pancho
	 */
	public void retSenchaTreeAllCompanies(UnidadGenericaStyleDef style) {
		IOUtilSenchaTree.getIntance().retSenchaTreeAllCompanies(this, style);
	}

	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de
	 * los nodos
	 * @revision 25-05-2018 Pancho	
	 */
	public void retSenchaTreeFromUnidad(Object[] unidades) {
		IOUtilSenchaTree.getIntance().retSenchaTreeFromUnidad(this, unidades);
	}

	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de
	 * los nodos
	 * @revision 25-05-2018 Pancho	
	 */
	public void retSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad) {
		IOUtilSenchaTree.getIntance().retSenchaTreeFromUnidad(this, unidades, defUnidad);
	}

	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de
	 * los nodos
	 * @revision 25-05-2018 Pancho	
	 */

	public void retSenchaTreeFromUnidad(String unidad) {
		IOUtilSenchaTree.getIntance().retSenchaTreeFromUnidad(this, unidad);
	}

	/**
	 * Genera una orgánica según los la unidad que se entregue y el estilos de
	 * los nodos
	 * @revision 25-05-2018 Pancho	
	 */
	public void retSenchaTreeFromUnidad(String unidad, UnidadGenericaStyleDef style) {
		IOUtilSenchaTree.getIntance().retSenchaTreeFromUnidad(this, unidad, style);
	}



	/**
	 * Retorna una bolsa de gatos
	 * 
	 * @throws SQLException
	 * @revision 25-05-2018 Pancho	
	 */

	public void returnBolsaDeGatosByRut(int rut, int idUpload) throws SQLException {
		IOUtilBolsaDeGatos.getIntance().getBolsaDeGatosByRut(this, rut, idUpload);
	}

	/**
	 * Retorna un xml o html en una página dentro de tag pre, el parametro xml
	 * no debe estar con Escape
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 * @revision 25-05-2018 Pancho
	 */
	public boolean retXml(HttpFlow xml) {
		return IOUtilXml.getIntance().retXml(this, xml);
	}



	public void retSenchaJsonFromVo(Class<? extends Vo> clase, List<Where> wheres) {
		IOUtilSencha.getInstance().retSenchaJsonFromVo(this, clase, wheres);
	}

 	public boolean retTemplate(String templatePath) {
		SimpleHash modelRoot = new SimpleHash();
		return myHttpServlet.retTemplate(this, templatePath, modelRoot);
	}

 	public boolean retTemplate(String templatePath, SimpleHash modelRoot) {
		return myHttpServlet.retTemplate(this,  templatePath, modelRoot);
	}

 	public boolean retTemplate(String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) {
 		return myHttpServlet.retTemplate(this, templatePath, modelRoot, startFromTemplatePath);
		
	}
 	
 	public boolean retTemplate(IPageResource resource, SimpleHash modelRoot) {
 		FreemakerTool.pages.putHash(modelRoot, resource.getClass());
 		return myHttpServlet.retTemplate(this, resource.getPath(), modelRoot, resource.isFromTemplatePath());
 	}
 	
 	public boolean retTemplate(Class<? extends IPageResource> resource, SimpleHash modelRoot) {
 		 
 		IPageResource page = Weak.getInstance(resource);
 		return retTemplate(page, modelRoot);
 	}
 	public boolean retTemplate(Class<? extends IPageResource> resource) {
 		SimpleHash modelRoot = FreemakerTool.pages.putHash(resource);
 		IPageResource page = Weak.getInstance(resource);
 		return retTemplate(page, modelRoot);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getParamEnum(Class<T> T, String name, Object defaultValue) {
		return (T) EnumTool.getEnumByName((Class)T, getParamString(name, null), defaultValue);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getParamEnumByNameIgnoreCase(Class<T> T, String name, Object defaultValue) {
		return (T) EnumTool.getEnumByNameIngoreCase((Class)T, getParamString(name, null), defaultValue);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getParamEnumByToString(Class<T> T, String name, Object defaultValue) {
		return (T) EnumTool.getEnumByToString((Class)T, getParamString(name, null), defaultValue);
	}


	public void ret(EnumDataEscape escape, ConsultaData data) {
		switch (escape) {
		case SENCHA:
			retSenchaJson(data);
			break;
		case JSON:
			retJson(data);
			break;
		case EXCEL:
			retExcel(data, "Archivo excel");
			break;
		case XML:
			XStream x = new XStream();
			retTexto( x.toXML(data) );
			break;
		default:
			break;
		}
		
	}

	public void addExtraParam(String param, Integer value) {
		if(param != null) {
			addExtraParam(param, String.valueOf(value));	
		}
		
	}
	
	public void addExtraParam(String param, Boolean value) {
		if(param != null) {
			addExtraParam(param, String.valueOf(value));	
		}
		
	}


	public void ret(EnumDataEscape escape, Throwable e) {
		switch (escape) {
		case SENCHA:
			retSenchaJson(e);
			break;
		case JSON:
			throw new NotImplementedException();
		case EXCEL:
			throw new NotImplementedException();
		case XML:
			throw new NotImplementedException();
		default:
			break;
		}
		
	}

	public boolean redirectLogged(EModulos modulo, String montajeToRedirect) {
		return IOUtilRedirectLogged.getInstance().redirecLogged(this, modulo, montajeToRedirect);
	}



	public void retMensajeGenerico(Throwable e) {
		IOUtilHtmlMessage.getInstance().retMensajeGenerico(this, e);
		
	}

	public void retMensajeGenerico(String titulo, String mensaje) {
		IOUtilHtmlMessage.getInstance().retMensajeGenerico(this, titulo, mensaje);
		
	}
	
	/***
	 * Retorna un nuevo objeto que olo imprime valores de forma interna, no lo retorna al FRONT, para obtener el retorno se debe llamar a getInternResponse
	 * 
	 * @author Pancho
	 * @since 27-02-2019
	 * @see #getInternResponse()
	 */
	public IOClaseWebSimulator getSimulator(EModulos moduloSimulator) {
		
		IOClaseWebSimulator io = new IOClaseWebSimulator(this, this.cons, null, req, resp);
		//io.in = in.clone();
		io.user = this.user;
		io.modulo = moduloSimulator;
		io.cons = this.cons;
		io.cons.setContext(moduloSimulator);
		
		
		
		return io;
	}
 


	public boolean addExtraParam(Map<String, String> params) {
		boolean ok = false;
		if(params != null) {
			for(Entry<String, String> entry : params.entrySet()) {
				addExtraParam(entry.getKey(), entry.getValue());
			}
			
			ok = true;
		}
		
		return ok;
	}

	public TransactionConnection getTransactionConnection() {
		return cons;
	}

	public boolean isTextResponded() {
		boolean isTextSended = in.getResponseText() != null;
		boolean isComitted = getResp().isCommitted();
		
		return isTextSended || isComitted;
	}

	public EModulos getModulo() {
		return this.modulo;
	}

	/**
	
	 * Tiene que venir en el formato de una Lista javascript [1,2,3,4]
	 * @author Pancho
	 * @since 07-06-2019
	 * */
	public List<String> getParamList2(String nomParam)  {
		List<String> retorno = IOUtilParam.getInstance().getParamList2(this, nomParam);
		 
		return retorno;
	}

	/**
	 * El parámetro debe venir en el formato: 1,2,3,4,5
	 * */
	public <T>List<T> getListParam(String nomParam, Class<T> claseTo) {
		return IOUtilParam.getInstance().getListParam(this, nomParam, claseTo);
		
	}
	
	/**
	 * Funciona cuando el dato vienen varios valores con el mismo nombreparametro=1,parametro=2,parametro=3,parametro=4
	 * @author Pancho
	 * @since 18-06-2019
	 * */
	public <T>List<T> getListParamStringAsEnumByToStringIngoreCase(String nomParam, Class<? extends Enum<?>> clase) {
		return IOUtilParam.getInstance().getListParamStringAsEnumByToStringIngoreCase(this, nomParam, clase);
	}
	
	/**
	 * Obtiene una lista de valor que viene encryptada  <br/>
	 * El parámetro debe venir en el formato: 1,2,3,4,5
	 * 
	 * @author Pancho
	 * @since 07-06-2019
	 * */
	public <T>List<T> getListParamEncrypted(String nomParam, Class<T> claseTo) {
		return IOUtilParam.getInstance().getListParamEncrypted(this, nomParam, claseTo);
	}	

	/**
	 * Indica previo al retorno de HTTPServletResponse los campos indicados en el parámetro deben ser encryptados<br/>
	 * A la fecha, IOClaseWeb reconoce tanto en el retorno de ConsultaData como en el retorno de IOTree
	 * */
	public void onReturnEncryptColumns(List<String> columns) throws Exception {
		IOUtilSencha.getInstance().onReturnEncryptColumns(this, columns);
		
	}
	
	/**
	 * Indica previo al retorno de HTTPServletResponse los campos indicados en el parámetro deben ser encryptados<br/>
	 * A la fecha, IOClaseWeb reconoce tanto en el retorno de ConsultaData como en el retorno de IOTree
	 * */
	public void onReturnEncryptColumns(Class<? extends Vo> voClass) throws Exception {
		IOUtilSencha.getInstance().onReturnEncryptColumns(this, voClass);
		
	}
	
//	public void onReadDencryptColumns(List<String> columns) throws Exception {
//		IOUtilSencha.getInstance().onReadDencryptColumns(this, columns);
//		
//	}

	/**
	 * Si consultaData tiene algunos valores encryptados, estos serán reconocidos automáticamente ya que dentro del map de cada lista <br/>
	 * vienen  indicados los campos encriptados <br/>
	 * Para que ocurra esto la encriptación debe hacerse con el frameWork de IOClaseWweb
	 * 
	 * @author Pancho
	 * @since 05.06-2019
	 * */
	public ConsultaData getParamConsultaDataEncrypted(String paramName) {
		return IOUtilParam.getInstance().getParamConsultaDataEncrypted(this, paramName);
	}

	public IIOBootstrap bootstrap() {
		if(this.bootstrap == null) {
			this.bootstrap = new IOBootstrap(this);
		}
		
		return this.bootstrap;
	}

	public void onReturnEncryptColumn(String column) throws Exception {
		IOUtilSencha.getInstance().onReturnEncryptColumn(this, column);
	}



	
	
}
