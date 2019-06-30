package cl.eje.qsmcom;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.eje.qsmcom.managers.ManagerAcceso;
import cl.eje.qsmcom.managers.ManagerPortal;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;

public class Index extends AbsClaseWeb {
	
	public Index(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGet() throws Exception {
		int rut = Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1);
		ConsultaData data = ManagerAcceso.getInstance().getEjeGesUsuario(rut);
		ConsultaData dataTrabajador = ManagerTrabajador.getInstance().getTrabajador(rut);
		
		if(data == null || !data.next() ) {
			/*Si no esta en la eje_ges_trabajador ni en la eje_ges_usuario*/
			super.getIoClaseWeb().getResp().sendRedirect("EjeCore?claseweb=cl.eje.qsmcom.mantenedor.DatosPersonales");
		}
		else if( data.getInt("cant_ingresos") <= 1 && rut != 99999999) {
			/* Si es primera vez que ingresa */
			SimpleHash modelRoot = new SimpleHash();
			
			if(dataTrabajador.next()) {
				modelRoot.put("nombre", dataTrabajador.getString("nombres"));
			}

			super.getIoClaseWeb().retTemplate("main/bienvenida.html", modelRoot);
		}
		else {
			dataTrabajador.toStart();
			
			if(dataTrabajador.next()) {
				ConsultaData dataCorreoValido = ManagerTrabajador.getInstance().getConfirmacionCorreo(rut, dataTrabajador.getString("mail"));
				
				if(( dataCorreoValido == null || !dataCorreoValido.next() ) && rut != 99999999) {
					super.getIoClaseWeb().getResp().sendRedirect("EjeCore?claseweb=cl.eje.qsmcom.mantenedor.DatosPersonales&options=hf");
				}
				else {
					super.getIoClaseWeb().getResp().sendRedirect("Tool?htm=main/index.html");
				}
			}
			else {
				super.getIoClaseWeb().getResp().sendRedirect("Tool?htm=main/index.html");
			}
		}

		
	}

	
	
}
