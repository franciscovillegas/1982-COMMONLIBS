package organica.com.eje.ges.gestion.dimension;

import java.sql.Connection;

import organica.tools.Validar;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;

public class Dimension_Manager
{

    public Dimension_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }
    
    
    public Consulta XmlGetDimensiones(String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select distinct i.dimension_id,d.dimension_desc,d.dimension_tipo from (eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id) inner join eje_ges_dimension d on i.dimension_id=d.dimension_id where u.unidad_id='" + unidad + "'"))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta XmlNotDimensiones(String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select distinct dimension_id,dimension_desc,dimension_tipo from eje_ges_dimension where dimension_id not in (select distinct i2.dimension_id from (eje_ges_ditem_unidad u2 inner join eje_ges_dimension_item i2 on u2.ditem_id=i2.ditem_id) inner join eje_ges_dimension d2 on i2.dimension_id=d2.dimension_id where u2.unidad_id='" + unidad + "')"))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta XmlGetValoresDimensiones(String unidad, String dimension)
    {
        consul2 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select cast(i.ditem_id as varchar) as id,i.ditem_desc as valor,'true' as t from eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id where u.unidad_id='" + unidad + "' and i.dimension_id=" + dimension + " union select cast(ditem_id as varchar) as id,ditem_desc as valor,'false' as t from eje_ges_dimension_item where dimension_id=" + dimension + " and ditem_id not in (select i1.ditem_id from eje_ges_ditem_unidad u1 inner join eje_ges_dimension_item i1 on u1.ditem_id=i1.ditem_id where u1.unidad_id='" + unidad + "' and i1.dimension_id=" + dimension + " ) order by ditem_desc"))));
        consul2.exec(sql);
        return consul2;
    }

    public Consulta XmlGetValoresDimensiones2(String unidad, String dimension)
    {
        consul2 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select cast(i.ditem_id as varchar) as id,i.ditem_desc as valor,'true' as t from eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id where u.unidad_id='" + unidad + "' and i.dimension_id=" + dimension ))));
        consul2.exec(sql);
        return consul2;
    }

    
    public Consulta XmlNotValoresDimensiones(String dimension)
    {
        consul2 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select cast(ditem_id as varchar) as id,ditem_desc as valor,'false' as t from eje_ges_dimension_item where dimension_id=" + dimension))));
        consul2.exec(sql);
        return consul2;
    }

    public Consulta XmlNotValoresDimensiones2(String dimension)
    {
        consul2 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select top 1 cast(ditem_id as varchar) as id,ditem_desc as valor,'false' as t from eje_ges_dimension_item where dimension_id=" + dimension))));
        consul2.exec(sql);
        return consul2;
    }
    
    public String XmlGetDescUnidad(String unidad)
    {
        consul3 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select unid_desc from eje_ges_unidades where unid_id='" + unidad + "'"))));
        consul3.exec(sql); consul3.next();
        return consul3.getString("unid_desc");
    }    

    public Consulta XmlGetDimensionesCardinalidad(String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select distinct i.dimension_id,i.ditem_id,d.dimension_cardinalidad,d.dimension_tipo from (eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id) inner join eje_ges_dimension d on i.dimension_id=d.dimension_id where u.unidad_id='" + unidad + "'"))));
        consul.exec(sql);
        return consul;
    }

    public void XmlCleanDimensiones(String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("delete from eje_ges_ditem_unidad where unidad_id='" + unidad + "' and ditem_id in (SELECT u.ditem_id FROM (eje_ges_ditem_unidad as u inner join eje_ges_dimension_item as i on u.ditem_id=i.ditem_id) inner join eje_ges_dimension as d on i.dimension_id=d.dimension_id where u.unidad_id='" + unidad + "' and d.dimension_tipo not in ('textarea','text'))"))));
        consul.delete(sql);
    }

    public void XmlCleanDimensiones(String unidad,String dimension)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("delete from eje_ges_ditem_unidad where unidad_id='" + unidad + "' and ditem_id in (SELECT ditem_id FROM eje_ges_dimension_item where dimension_id=" + dimension + ")"))));
        consul.delete(sql);
    }

    public void XmlGetInsertUnidadDimension(String unidad,String valor)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ISNULL(cast(max(ditemuni_id) as varchar),0) as item FROM eje_ges_ditem_unidad"))));
        consul.exec(sql); consul.next();
        String id = consul.getString("item");
        int nid=Integer.parseInt(id.trim()); nid+=1;

        sql = String.valueOf(String.valueOf((new StringBuilder("insert into eje_ges_ditem_unidad(ditemuni_id,unidad_id,ditem_id) values(" + nid + ",'" + unidad + "'," + valor + ")"))));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA INSERCION VALOR DIMENSION" + sql); }
    }
    
    public void XmlGetInsertUnidadDimension(String unidad,String dimension,String valor)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cast(max(ditem_id) as varchar) as item FROM eje_ges_dimension_item"))));
        consul.exec(sql); consul.next();
        String id = consul.getString("item");
        int nid=Integer.parseInt(id.trim()); nid+=1;

        sql = String.valueOf(String.valueOf((new StringBuilder("insert into eje_ges_dimension_item(ditem_id,ditem_desc,dimension_id) values(" + nid + ",'" + valor + "'," + dimension + ")"))));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA INSERCION VALOR DIMENSION" + sql); }
        XmlGetInsertUnidadDimension(unidad,String.valueOf(nid));
    }

    public void XmlGetUpdateUnidadDimension(String item, String valor)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("update eje_ges_dimension_item set ditem_desc='" + valor + "' where ditem_id=" + item ))));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA UPDATE DIMENSION" + sql); }
    }
    
    public String XmlGetCardinalidadDimension(String dimension)
    {
        consul3 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select dimension_cardinalidad from eje_ges_dimension where dimension_id=" + dimension))));
        consul3.exec(sql); consul3.next();
        return consul3.getString("dimension_cardinalidad");
    }    

    public String XmlGetTipoDimension(String dimension)
    {
        consul3 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select dimension_tipo from eje_ges_dimension where dimension_id=" + dimension))));
        consul3.exec(sql); consul3.next();
        return consul3.getString("dimension_tipo");
    }    

    
    //****************************************************************************
    
    
    public Consulta GetDimensiones()
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_dimension order by dimension_desc"))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDimensionesUnidad(String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select i.dimension_id,d.dimension_desc from (eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id) inner join eje_ges_dimension d on i.dimension_id=d.dimension_id where u.unidad_id='" + unidad + "'"))));
        consul.exec(sql);
        return consul;
    }

    public SimpleList GetListDimensiones(String dimension)
    {
        consul = GetDimensiones();
        SimpleList DimensionList = new SimpleList();
        SimpleHash DimensionHash;
        valida = new Validar();
        String mensaje="";
        for(; consul.next(); DimensionList.add(DimensionHash))
        {
            DimensionHash = new SimpleHash();
            DimensionHash.put("id", valida.validarDato(consul.getString("dimension_id")));
            DimensionHash.put("desc", valida.validarDato(consul.getString("dimension_desc")));
            if(!dimension.equals("0") && valida.validarDato(consul.getString("dimension_id")).equals(dimension)) mensaje= "selected";
            else mensaje= "";
            DimensionHash.put("select", mensaje);
        }
        return DimensionList;
    }

    public String GetValorDimension(String unidad,String dimension)
    {
        consul2 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select i.ditem_id from eje_ges_ditem_unidad u inner join eje_ges_dimension_item i on u.ditem_id=i.ditem_id where u.unidad_id='" + unidad + "' and i.dimension_id=" + dimension ))));
        consul2.exec(sql);consul2.next();
        return valida.validarDato(consul2.getString("ditem_id"));
    }
    
    public SimpleList GetListDimensionesUnidad(String unidad)
    {
        consul = GetDimensionesUnidad(unidad);
        SimpleList DimensionList = new SimpleList();
        SimpleHash DimensionHash;
        valida = new Validar();
        for(; consul.next(); DimensionList.add(DimensionHash))
        {
            DimensionHash = new SimpleHash();
            String dim = valida.validarDato(consul.getString("dimension_id"));
            DimensionHash.put("id", dim);
            DimensionHash.put("desc", valida.validarDato(consul.getString("dimension_desc")));
            DimensionHash.put("dimsel", GetValorDimension(unidad,dim));
            DimensionHash.put("valores",GetListDimensionesValores(dim));
        }
        return DimensionList;
    }

    
    public Consulta GetDimensionesValores(String dimension)
    {
        consul4 = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select cast(ditem_id as varchar) as id,ditem_desc as valor from eje_ges_dimension_item where dimension_id=" + dimension + " order by ditem_desc"))));
        consul4.exec(sql);
        return consul4;
    }

    public SimpleList GetListDimensionesValores(String dimension)
    {
    	consul3 = GetDimensionesValores(dimension);
        SimpleList ValDimensionList = new SimpleList();
        SimpleHash ValDimensionHash;
        for(; consul3.next(); ValDimensionList.add(ValDimensionHash))
        {
            ValDimensionHash = new SimpleHash();
            ValDimensionHash.put("id", valida.validarDato(consul3.getString("id")));
            ValDimensionHash.put("desc", valida.validarDato(consul3.getString("Valor")));
        }
        return ValDimensionList;
    }

    public void GetInsertDimensiones(String dimension,String tipo, String cardinalidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ISNULL(cast(max(dimension_id) as varchar),0) as dimension FROM eje_ges_dimension"))));
        consul.exec(sql); consul.next();
        String id = consul.getString("dimension");
        int nid=Integer.parseInt(id.trim()); nid+=1;
        sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_dimension(dimension_id,dimension_desc,dimension_tipo,dimension_cardinalidad) VALUES(" + nid + ",'" + dimension + "','" + tipo + "','" + cardinalidad + "')"))));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA INSERCION DIMENSION" + sql); }
    }

    public void GetInsertValoresDimensiones(String dimension, String valor)
    {
        consul = new Consulta(con);
        
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ISNULL(cast(max(ditem_id) as varchar),0) as item FROM eje_ges_dimension_item"))));
        consul.exec(sql); consul.next();
        String id = consul.getString("item");
        int nid=Integer.parseInt(id.trim()); nid+=1;
        int nid2=Integer.parseInt(dimension.trim());

        sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_dimension_item(ditem_id,ditem_desc,dimension_id) VALUES(" + nid + ",'" + valor + "'," + nid2 + ")"))));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA INSERCION VALOR DIMENSION" + sql); }
    }

    public void GetUpdateDimensiones(int id,String valor)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_dimension SET dimension_desc='").append(valor).append("' WHERE (dimension_id = ")).append(id).append(")")));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA UPDATE DIMENSION" + sql); }
    }
    
    public void GetUpdateValoresDimensiones(String id,String valor)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_dimension_item SET ditem_desc='").append(valor).append("' WHERE (ditem_id = ")).append(id).append(")")));
        if(!consul.insert(sql))
        { System.out.println("PROBLEMA UPDATE VALOR DIMENSION" + sql); }
    }

    public void GetDeleteDimensiones(int id)
    {
        consul = new Consulta(con);
        GetDeleteItemDimensiones(id);
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_dimension WHERE (dimension_id = ")).append(id).append(")")));
        consul.delete(sql);
    }
    
    public void GetDeleteItemDimensiones(int id)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select ditem_id from eje_ges_dimension_item where (dimension_id = ")).append(id).append(")")));
        consul.exec(sql);
        for(;consul.next();)
        {
        	GetDeleteUnidadItem(consul.getString("ditem_id"));
        }
        sql = String.valueOf(String.valueOf((new StringBuilder("delete from eje_ges_dimension_item where (dimension_id = ")).append(id).append(")")));
        consul.delete(sql);
    }    
    
    public void GetDeleteUnidadItem(String id)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("delete from eje_ges_ditem_unidad where (ditem_id= ")).append(id).append(")")));
        consul.delete(sql);
    }

    public void GetDeleteValoresDimensiones(int id)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_dimension_item WHERE (ditem_id = ")).append(id).append(")")));
        consul.delete(sql);
    }

    public Consulta GetValuesDimensiones()
    {
        //consul = new Consulta(con);
        //String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT numero,nombre,fecha_nacim,rut_carga,dv_carga,es_carga,es_carga_salud,num_matrim,actividad,fecha_inicio,fecha_termino,sexo,rtrim(parentesco) as parentesco,(DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = ")).append(rut).append(")")));
        //consul.exec(sql);
        return consul;
    }

    
    public String getError()
    {
        return mensajeError;
    }

    private Connection con;
    private Consulta consul, consul2, consul3, consul4;
    private String mensajeError;
    private Validar valida;    
}