package portal.com.eje.serhumano.user;

public interface IUsuario {

	public String getJndi();
	public String getEmpresa();
	public String getPlanta();
	public String getUnidad();
	public String getPassWord();
	public boolean esValido();
	public String getName();
	public Rut getRut();
	public String getRutId();
	
}
