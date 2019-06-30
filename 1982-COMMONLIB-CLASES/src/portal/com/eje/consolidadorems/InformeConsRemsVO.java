package portal.com.eje.consolidadorems;


public class InformeConsRemsVO
{

    public InformeConsRemsVO()
    {
    }

    public void setPeriodo(String periodo)
    {
        this.periodo = periodo;
    }

    public void setRut_usuario(String rut_usuario)
    {
        this.rut_usuario = rut_usuario;
    }

    public void setCodempresa(String codempresa)
    {
        this.codempresa = codempresa;
    }

    public void setNumPers(Integer numPers)
    {
        this.numPers = numPers;
    }

    public void setTot_haberes_consolid(Integer tot_haberes_consolid)
    {
        this.tot_haberes_consolid = tot_haberes_consolid;
    }

    public void setTot_liquido_consolid(Integer tot_liquido_consolid)
    {
        this.tot_liquido_consolid = tot_liquido_consolid;
    }

    public String getPeriodo()
    {
        return periodo;
    }

    public String getRut_usuario()
    {
        return rut_usuario;
    }

    public String getCodempresa()
    {
        return codempresa;
    }

    public Integer getNumPers()
    {
        return numPers;
    }

    public Integer getTot_haberes_consolid()
    {
        return tot_haberes_consolid;
    }

    public Integer getTot_liquido_consolid()
    {
        return tot_liquido_consolid;
    }

    private String periodo;
    private String rut_usuario;
    private String codempresa;
    private Integer numPers;
    private Integer tot_haberes_consolid;
    private Integer tot_liquido_consolid;
}