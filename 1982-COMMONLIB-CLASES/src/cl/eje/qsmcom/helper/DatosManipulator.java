package cl.eje.qsmcom.helper;

import intranet.com.eje.qsmcom.estructuras.Periodo;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import portal.com.eje.tools.excel.xlsx.Excel2010;
import portal.com.eje.tools.excel.xlsx.IExcel;
import portal.com.eje.tools.excel.xlsx.IXlsx2Row;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.eje.qsmcom.tool.TipoRegistro;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.fileupload.FileService;

public class DatosManipulator implements IDatosManipulator {
	private final int MAX_REG_BY_TABLE = 800000; //Valor óptimo del máximo de cada tabla.
	private final int PAGENUMBER = 0;
	private final int PAGENUMBER_DETALLE = 1;
	
	public boolean addRegistros(ServletContext context,int idPlantilla, int periodo, int idSubida) {
		/* OBTENER VALORES*/
		boolean ok = false;
		ManagerQSM q = ManagerQSM.getInstance();
		int idProceso = q.initProceso(idSubida);
		String tableName = q.getOrNewTableName(idPlantilla, this.MAX_REG_BY_TABLE);
		
		ConsultaData dataPlantilla = q.getPlantilla(idPlantilla);
		int cantColumnas = 0;
		int cantColumnasDetalle = 0;
		
		if(dataPlantilla.next()) {
			cantColumnas = dataPlantilla.getInt("cant_columnas");
			cantColumnasDetalle = dataPlantilla.getInt("cant_columnas_detalle");
		}
		/* TERMINO DE OBTENCION DE VALORES */
		int filas = -1;
		if(tableName != null) { 	
			ConsultaData data = q.getSubida(idSubida);
			
			if(data.next()) {
				int idFile = data.getInt("id_file");
				FileService fService = new FileService(context);
				File f = fService.getFile(idFile);
				
				IExcel excel = new Excel2010(true);
				
				try {
					RowManipulator rowManipulator = new RowManipulator(tableName, cantColumnas, idSubida);
					excel.getWorkBookDinamicamente(f,PAGENUMBER,cantColumnas,rowManipulator, new ExcelFieldTransformator());
					rowManipulator.flush();
					filas = rowManipulator.getFilas();
					ok = filas > 0;
					
					ConsultaData dataPlanilla = ManagerQSM.getInstance().getPlantilla(idPlantilla);
					if(dataPlanilla.next()) {
						boolean conDetalle = dataPlanilla.getBoolean("con_detalle");
						
						if(conDetalle) {
							RowManipulator rowManipulatorDetalle = new RowManipulator(tableName.concat("_detalle"), cantColumnasDetalle, idSubida);
							excel.getWorkBookDinamicamente(f,PAGENUMBER_DETALLE,cantColumnasDetalle,rowManipulatorDetalle);
							rowManipulatorDetalle.flush();
							filas = rowManipulator.getFilas();
							ok = filas > 0;
							
							ManagerQSM.getInstance().addRegistrosTablaRegistro(tableName.concat("_detalle"),  filas);
						}
					}

					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ManagerQSM.getInstance().addRegistrosTablaRegistro(tableName,  filas);
			}
		}
		
		ManagerQSM.getInstance().updateTrackeoDato(idSubida, "cant_regs", filas);
		ManagerQSM.getInstance().addRelationSubidaRut(tableName,idSubida);
		ManagerQSM.getInstance().addTrackeoRegistro(tableName,  idSubida);
		ManagerQSM.getInstance().endProceso(idProceso,0,0);
		return ok;
	}

	public boolean delRegistros(int idPlantilla, int periodo) {
		// TODO Auto-generated method stub
		return false;
	}

	public ConsultaData getRegistosByIdReq(int idReq, TipoRegistro t) {
		int periodo = ManagerQSM.getInstance().getPeriodoValidoParaUnTicket(idReq);
		int rut = ManagerQSM.getInstance().getRutResponsable_IdReq(idReq);
		
		int idSubida = ManagerQSM.getInstance().getLastSubidaPorRut(periodo, rut);
		
		ConsultaData data = ManagerQSM.getInstance().getTablaIdRegistroFromIdSubida(idSubida);
		int idTabla = -1;
		if(data.next()) {
			idTabla = data.getInt("id_tabla_registro");
		}
		
		return ManagerQSM.getInstance().getRegistros(idTabla, idSubida, rut, t);
	}

	public ConsultaData getLastRegistrosByRut_PeriodoActual(int rut, TipoRegistro t) {
		Periodo periodo = ManagerQSM.getInstance().getPeriodoValido(TipoCarga.calculo);		
		return getLastRegistrosByRut_Periodo(periodo.getPeriodo(), rut,t);
	}

	
	public ConsultaData getLastRegistrosByRut_Periodo(int periodo, int rut, TipoRegistro t) {
		int idSubida = ManagerQSM.getInstance().getLastSubida_PeriodoRut(periodo, rut);
		
		ConsultaData data = ManagerQSM.getInstance().getTablaIdRegistroFromIdSubida(idSubida);
		int idTabla = -1;
		if(data.next()) {
			idTabla = data.getInt("id_tabla_registro");
		}
		
		return ManagerQSM.getInstance().getRegistros(idTabla, idSubida, rut, t);
	}
	
	public ConsultaData getLastRegistrosByRut_Periodo(int periodo, int rut, TipoRegistro t, int idSubida) {

		ConsultaData data = ManagerQSM.getInstance().getTablaIdRegistroFromIdSubida(idSubida);
		int idTabla = -1;
		if(data.next()) {
			idTabla = data.getInt("id_tabla_registro");
		}
		
		return ManagerQSM.getInstance().getRegistros(idTabla, idSubida, rut, t);
	}
	
	public ConsultaData getLastRegistrosByRut(TipoCarga c,int rut, TipoRegistro t) {
	
		ConsultaData dataSubida = ManagerQSM.getInstance().getLastSubidaTipoCarga(c);
		
		if(dataSubida.next()) {
			int idSubida = dataSubida.getInt("id_subida");
			
			ConsultaData data = ManagerQSM.getInstance().getTablaIdRegistroFromIdSubida(idSubida);
			int idTabla = -1;
			if(data.next()) {
				idTabla = data.getInt("id_tabla_registro");
			}
			
			return ManagerQSM.getInstance().getRegistros(idTabla, idSubida, rut,t);
		}
		
		return null;
	}




}

class RowManipulator implements IXlsx2Row {
	private int cantFilas;
	private Cronometro cro;
	private ConsultaData dataExcel;
	private boolean firstRow;
	private String pal1;
	private String tableName;
	private int cantColumnasEsperadas;
	private int idSubida;
	private int fila;
	
	public RowManipulator(String tableName, int cantColumnasEsperadas, int idSubida) {
		this.tableName = tableName;
		this.cantFilas = 0;
		this.cro = new Cronometro();
		this.cro.Start();
		this.firstRow = true;
		this.pal1 = "abcdefghijklmnopqrstuvwxyz";
		this.idSubida = idSubida;
		this.cantColumnasEsperadas = cantColumnasEsperadas;
		this.fila = 0;
	}
	
	public int getFilas() {
		return cantFilas;
	}
	
	public void row(DataFields row) {

			
			if(firstRow) {
				firstRow = false;
				iniciaData( row.size() );
			}
			
			cantFilas++;
			dataExcel.add(row);
			
			if(cantFilas % 500 == 0) {
				flush();
				iniciaData( row.size() );
			}	
	}

	public void flush() {
		System.out.println(String.valueOf(cantFilas).concat(" - ".intern()).concat(String.valueOf(cro.GetMilliseconds())).concat("ms".intern()));
		cro.Start();
		
		

		while(dataExcel != null && dataExcel.next()) {
			StringBuffer buf = new StringBuffer();
			
			
			try {
				
				fila++;
				StringBuffer insert = new StringBuffer();	
				StringBuffer values = new StringBuffer();
				insert.append("INSERT INTO [").append(tableName).append("]");
				insert.append("(fila,id_subida,");
				
				values.append(" (?,?,");
				
				Object[] params = new Object[cantColumnasEsperadas + 2];
				params[0] = fila;
				params[1] = idSubida;
				
				int pos = 0;
				for(int i = 0; i < pal1.length() ;i++) {
					String p1 = pal1.substring(i, i+1);
					if(pos >= cantColumnasEsperadas) {
						break;
					}
					
					insert.append("[").append(p1).append("],");
					values.append("?,");
					pos++;
					params[pos+1] =  dataExcel.getForcedString(pos-1);
				}
				
				for(int i = 0; i < pal1.length() ;i++) {
					String p1 = pal1.substring(i, i+1);
					if(pos >= cantColumnasEsperadas) {
						break;
					}
					
					for(int j = 0; j < pal1.length() ;j++) {
						String p2 = pal1.substring(j, j+1);
						
						if(pos >= cantColumnasEsperadas) {
							break;
						}
						
						insert.append("[").append(p1).append(p2).append("],");
						values.append("?,");
						pos++;
						params[pos+1] =  dataExcel.getForcedString(pos-1);
					}
				}
				
				
				buf.append(insert.toString().substring(0, insert.toString().lastIndexOf(",")).concat(")"));
				buf.append(" VALUES ");
				buf.append(values.toString().substring(0, values.toString().lastIndexOf(",")).concat(")"));


				ConsultaTool.getInstance().insert("mac", buf.toString(),params);
			} catch (SQLException e) {
				e.printStackTrace();
				
			} catch (NoSuchFieldError e) {
				e.printStackTrace();
			}
		}
	}
	
	private void iniciaData(int totalColumnas) {
		List<String> lista = new ArrayList<String>();
		for(int i=1; i <= totalColumnas; i++) {
			lista.add(String.valueOf(i));
		}
		dataExcel = new ConsultaData(lista);
	}

}
