IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_ggd_evaluacion_competencias_resumen_competencias' AND COLUMN_NAME = 'nota_competencia_from_m') 
alter table eje_ggd_evaluacion_competencias_resumen_competencias add nota_competencia_from_m float