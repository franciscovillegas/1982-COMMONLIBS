IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_generico_perfiles_webmatico' AND COLUMN_NAME = 'grupo_familiar')
alter table eje_generico_perfiles_webmatico add grupo_familiar bit

IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_generico_perfiles_webmatico' AND COLUMN_NAME = 'estudios')
alter table eje_generico_perfiles_webmatico add estudios bit