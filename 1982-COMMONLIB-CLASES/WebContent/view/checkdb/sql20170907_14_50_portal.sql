if not exists (select * from sysobjects where name='eje_gp_noticias' and xtype='U') 
CREATE TABLE eje_gp_noticias( id_corr int IDENTITY(1,1), 
								fecha_create datetime NULL, 
								rut_foto int NULL, 
								id_file int NULL, 
								nombre varchar(100) NULL, 
								texto varchar(2000) NULL, 
								eliminado bit NULL, 
								CONSTRAINT PK_eje_gp_noticias_29 PRIMARY KEY NONCLUSTERED (id_corr))