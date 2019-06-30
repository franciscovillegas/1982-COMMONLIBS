<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl= "http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:output method="html" indent="yes"/>
 
<xsl:template match="/">
   <html><body>
   <div style="overflow:auto; width:330px; height:348px; align:center;">
   <xsl:apply-templates/>
   </div>
   </body></html>
</xsl:template>
<xsl:template match="dimensiones">
<table width="300" border="0" cellpadding="0" cellspacing="1">
<tr><td height="27" class="TITULO_MODULOS"></td></tr>
</table>
<form name="frm" action="../servlet/GestionarDimension" method="post">
  <xsl:variable name='vempresa' select='@empresa' />
  <xsl:variable name='vunidad' select='@unidad' />
  <xsl:variable name='vestado' select='@estado' />
  <input type="hidden" name="unidad" value="{$vunidad}"/>
  <input type="hidden" name="empresa" value="{$vempresa}"/>
  <div style="position:absolute; left:90px; top:2px; width:238px; height:24px; z-index:4">
  <!-- 
  <a href="../servlet/DimensionesUnidad?unidad={$vunidad}&#38;empresa={$vempresa}"><img src="../servlet/Tool?images/Gestion/dimensiones/dimesiones_uni.gif" width="101" height="26" border="0"/></a>&#160;
   -->
  <a href="../servlet/GestionarDimension?unidad={$vunidad}&#38;empresa={$vempresa}"><img src="../servlet/Tool?images/Gestion/dimensiones/dimesiones_ges.gif" width="101" height="26" border="0"/></a>
  </div>
  <table border="0" width="85%" cellpadding="1" cellspacing="1">
    <tr>
    <td colspan="3" align="center"><strong><font size="-1"><i>ID.Unidad : <xsl:value-of select="@unidad"/></i></font></strong></td>
    </tr>
    <tr>
    <td colspan="3" align="center"><strong><font size="-1"><i>Descripcion.Unidad : <xsl:value-of select="@descripcion"/></i></font></strong></td>
    </tr>
    <xsl:for-each select="dimension">
      <tr>
        <xsl:variable name='idc2' select='id' />
        <xsl:variable name='ids' select='serial' />
        <td colspan="3" class="inputText">
        <div class="campo_texto">
        <xsl:if test="$vestado='Sdel'"><input type="checkbox" name="Ldimensiones" value="{$idc2}"/></xsl:if>
        <xsl:if test="$vestado='Sins'">
        <input type="hidden" name="Ldimensiones" value="{$idc2}"/>
        <input type="checkbox" name="Seleccion" value="{$ids}"/>
        </xsl:if>        
        <xsl:value-of select="nombre"/>
        </div>
        </td>
      </tr>
      <tr>
        <xsl:variable name='idc' select='id' />
        <xsl:if test="$vestado!='Sdel'">
        <td valign="top" colspan="3" style="padding-left:35px" class="campo_texto"><font size="-1">
          <xsl:choose>
            <xsl:when test="tipo='textarea'">
			  <textarea rows="4" cols="25" name="valor{$idc}" class="inputText" >
			    <xsl:for-each select="./valores/valor"><xsl:value-of select="@St"/></xsl:for-each>
			  </textarea>
			</xsl:when>
            <xsl:when test="tipo='text'">
              <xsl:for-each select="./valores/valor">
                <xsl:variable name='desc' select='@St' />
                <input size="25" maxlenght="50" type="text" name="valor{$idc}" value="{$desc}" class="inputText" />
              </xsl:for-each>
			</xsl:when>
            <xsl:when test="tipo='select'">
			  <select name="valor{$idc}" class="inputText" >
			    <xsl:for-each select="./valores/valor">
			      <xsl:variable name='varios' select='@Val' />
				  <xsl:choose>
                    <xsl:when test="@elegido='true'">
                    <option value="{$varios}" selected="true" ><xsl:value-of select="@St"/></option>
                    </xsl:when>
                    <xsl:otherwise>
                    <option value="{$varios}" ><xsl:value-of select="@St"/></option>
                    </xsl:otherwise>
                  </xsl:choose>
			    </xsl:for-each>
			  </select>
			</xsl:when>
            <xsl:when test="tipo='selectmult'">
			  <select name="valor{$idc}" size="5" multiple="true" class="inputText" >
			    <xsl:for-each select="./valores/valor">
			      <xsl:variable name='varios' select='@Val' />
				  <xsl:choose>
                    <xsl:when test="@elegido='true'">
                    <option value="{$varios}" selected="true" ><xsl:value-of select="@St"/></option>
                    </xsl:when>
                    <xsl:otherwise>
                    <option value="{$varios}" ><xsl:value-of select="@St"/></option>
                    </xsl:otherwise>
                  </xsl:choose>
			    </xsl:for-each>
			  </select>
			</xsl:when>
            <xsl:when test="tipo='opcion'">
			  <xsl:for-each select="./valores/valor">
			    <xsl:variable name='varios' select='@Val' />
			      <xsl:choose>
                    <xsl:when test="@elegido='true'">
                      <div class="campo_texto">
			    	  <input type="radio" name="valor{$idc}" value="{$varios}" checked="true"/> <xsl:value-of select="@St"/><br/>
			    	  </div>
			    	</xsl:when>
                    <xsl:otherwise>
                      <div class="campo_texto"> 
                      <input type="radio" name="valor{$idc}" value="{$varios}"/> <xsl:value-of select="@St"/><br/>
                      </div>
                    </xsl:otherwise>
			      </xsl:choose>	
			  </xsl:for-each>
			</xsl:when>
            <xsl:when test="tipo='check'">
			  <xsl:for-each select="./valores/valor">
			    <xsl:variable name='varios' select='@Val' />
			    <xsl:choose>
                  <xsl:when test="@elegido='true'">
                    <div class="campo_texto"> 
			        <input type="checkbox" name="valor{$idc}" checked="true" value="{$varios}"/> <xsl:value-of select="@St"/><br/>
			        </div>
			      </xsl:when>
			      <xsl:otherwise>
			        <div class="campo_texto">
                    <input type="checkbox" name="valor{$idc}" value="{$varios}"/> <xsl:value-of select="@St"/><br/>
                    </div>
                  </xsl:otherwise>
			    </xsl:choose>      
			  </xsl:for-each>
			</xsl:when>
		  </xsl:choose>
		</font>  
        </td>
        </xsl:if>
      </tr>
    </xsl:for-each>
    <tr>
      <td colspan="3" align="center">
      <xsl:choose >
	    <xsl:when test="$vestado='Ssel'"> 
          <input type="submit" value="Agregar" name="accion" class="inputText" />
          <input type="submit" value="Grabar" name="accion" class="inputText" />
          <input type="submit" value="Eliminar" name="accion" class="inputText" />
        </xsl:when>
   	    <xsl:when test="$vestado='Sins'"> 
          <input type="submit" value="Asociar" name="accion" class="inputText" />
        </xsl:when>
   	    <xsl:when test="$vestado='Sdel'"> 
          <input type="submit" value="Borrar" name="accion" class="inputText" />
        </xsl:when>
      </xsl:choose>            
      </td>
    </tr>
  </table>
</form>  
</xsl:template>
</xsl:stylesheet> 