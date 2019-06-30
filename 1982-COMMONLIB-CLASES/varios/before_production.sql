
update EJE_GES_TRABAJADOR
set unidad = a.unid_id
from tmp_asociacion a inner join eje_ges_trabajador t on a.dni = t.rut




update eje_generico_mapping set path_vigente = 1 where id = 327
update eje_generico_mapping set path_vigente = 1 where id = 188 
update eje_ges_empresa set empre_rut = '20498049975', descrip = 'Servex' , empre_dir= 'Campiña Paisajista A1A'

agregar campo afp    en eje_ges_certif_histo_liquidacion_cabecera (afp    varchar(12))
agregar campo isapre en eje_ges_certif_histo_liquidacion_cabecera (isapre varchar(12))


 insert into eje_generico_mapping (path_original, path_mapeado, path_vigente) values ('/templates/certificados/haberes.htm'	  , '/templates/portal/certificados/haberes.htm'	, 1 )
 insert into eje_generico_mapping (path_original, path_mapeado, path_vigente) values ('/templates/certificados/descuentos.htm', '/templates/portal/certificados/descuentos.htm'	, 1 )
 
 
 agregar campoa tot_aportaciones en eje_ges_certif_histo_liquidacion_cabecera (tot_aportaciones decimal(18,0))
 
 insert into eje_generico_mapping	(path_original, path_mapeado, path_vigente) values 	('/images/firmas/firma1.png','/images/firmas/firma1.png',1)