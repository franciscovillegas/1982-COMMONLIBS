  /*  				
   *ER/Studio 7.0 SQL Code Generation
	* Company :      pancho			
	* Project :      eje_participa.DM1
	* Author :       pancho 		
	*								
	* Date Created : Friday, October 20, 2017 16:08:07
	* Target DBMS : Microsoft SQL Server 2005 		 
	*/												 
	/* 												 
	* TABLE: eje_gp_ficha_participantes_participantes_discretos
	*/ 														
 
		IF OBJECT_ID('eje_gp_ficha_participantes_participantes_discretos') IS  NULL 
		BEGIN 
			CREATE TABLE eje_gp_ficha_participantes_participantes_discretos( 
			id_corr        int               IDENTITY(1,1), 
			id_ficha       numeric(18, 0)    NOT NULL, 
			rut            int               NULL, 
			nombre         varchar(50)       NULL, 
			ape_paterno    varchar(50)       NULL, 
			ape_materno    varchar(50)       NULL, 
			CONSTRAINT PK_eje_gp_ficha_participantes_participantes_discretos_30 PRIMARY KEY NONCLUSTERED (id_corr) 
			) 
 
			ALTER TABLE eje_gp_ficha_participantes_participantes_discretos ADD CONSTRAINT Refeje_gp_ficha32 
			FOREIGN KEY (id_ficha) 
			REFERENCES eje_gp_ficha(id_ficha) 
		END 