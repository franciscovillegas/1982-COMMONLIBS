if not exists (select * from sysobjects where name='eje_ges_user_app_func' and xtype='U') 
CREATE TABLE eje_ges_user_app_func(app_id varchar(20),	descripcion varchar(100),	orden int,	is_administrable bit)