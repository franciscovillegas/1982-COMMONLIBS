package portal.com.eje.portal.liquidacion;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.liquidacion.enums.EjeGesCertifHistoLiquidacionCabeceraField;
import portal.com.eje.portal.liquidacion.error.FecTraspasoIncAndPeriodoNoCoincidenException;
import portal.com.eje.portal.liquidacion.error.NoTrabajadorHistoriaExcepcion;
import portal.com.eje.portal.liquidacion.error.PPMLiquidacionSinDatosException;
import portal.com.eje.portal.liquidacion.error.PPMPeriodoNoValidoException;
import portal.com.eje.portal.liquidacion.ifaces.ICtrGLiquidacionTool;
import portal.com.eje.portal.liquidacion.ifaces.ILiquidacionAccessPolicy;
import portal.com.eje.portal.pdf.HttpFlow;
import portal.com.eje.portal.plugins.ifaces.ITool;

public interface ILiquidacionTool extends ITool {

	public ConsultaData getPeriodos();
	
	public IPeriodoMensual getPeriodoActual();
	
	public HttpFlow getLiquidacion(final IOClaseWeb io, final int periodo, final int rut, final boolean canPrint) throws PPMLiquidacionSinDatosException, PPMPeriodoNoValidoException, SQLException, NoTrabajadorHistoriaExcepcion, FecTraspasoIncAndPeriodoNoCoincidenException, Exception;
	
	/**
	 * Obtiene las liquidaciones de todas las personas solicitadas para los periodos indicados<br/>
	 * Omite aquellas liquidaciones incorrectas
	 * 
	 * @author Pancho
	 * @throws FecTraspasoIncAndPeriodoNoCoincidenException 
	 * @throws NoTrabajadorHistoriaExcepcion 
	 * @throws SQLException 
	 * @throws PPMPeriodoNoValidoException 
	 * @throws PPMLiquidacionSinDatosException 
	 * @throws Exception 
	 * @since 26-06-2018
	 * 
	 * */
	
	public List<File> getLiquidacionesPdf(IOClaseWeb io, Collection<VoPeriodoMensual> periodos, ArrayFactory rutsToSend) throws IOException, PPMLiquidacionSinDatosException, PPMPeriodoNoValidoException, SQLException, NoTrabajadorHistoriaExcepcion, FecTraspasoIncAndPeriodoNoCoincidenException, Exception ;

	public ConsultaData getCabecera(int peri_id, ArrayFactory rutTrabajador, List<EjeGesCertifHistoLiquidacionCabeceraField> fields);
		
	public ICtrGLiquidacionTool getCtrG();
	
	public ILiquidacionAccessPolicy getAccessPolicy();
	
//	@Override
//	public List<Class<AZoneUtil>> getServices();
	

	
}
