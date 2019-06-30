
GO
/****** Object:  Table [dbo].[eje_gp_noticias]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_noticias]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_noticias](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[fecha_create] [datetime] NULL,
	[rut_foto] [int] NULL,
	[id_file] [int] NULL,
	[nombre] [varchar](100) NULL,
	[texto] [varchar](2000) NULL,
	[eliminado] [bit] NULL,
 CONSTRAINT [PK_eje_gp_noticias_29] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha](
	[id_ficha] [numeric](18, 0) NOT NULL,
	[nombre] [varchar](100) NULL,
	[razon] [varchar](1000) NULL,
	[fecha_inicio] [smalldatetime] NULL,
	[fecha_fin] [smalldatetime] NULL,
	[id_clasificacion] [numeric](18, 0) NULL,
	[id_logo] [int] NULL,
	[image_name] [varchar](50) NULL,
	[descripcion_sucursales] [varchar](1000) NULL,
	[descripcion_general] [varchar](1000) NULL,
	[fecha_owner] [smalldatetime] NULL,
	[rut_owner] [int] NULL,
	[who_owner] [varchar](150) NULL,
	[cargo_who_owner] [varchar](150) NULL,
	[unidad_who_owner] [varchar](150) NULL,
	[status_ficha] [numeric](18, 0) NULL,
	[fecha_publica] [smalldatetime] NULL,
	[who_gerencia] [varchar](500) NULL,
	[who_publica] [varchar](150) NULL,
	[id_cargo_who_publica] [varchar](150) NULL,
	[cargo_who_publica] [varchar](150) NULL,
	[id_unidad_who_publica] [varchar](150) NULL,
	[unidad_who_publica] [varchar](150) NULL,
	[id_proyecto] [numeric](18, 0) NULL,
	[fecha_inscripcioninicio] [smalldatetime] NULL,
	[fecha_inscripcionfin] [smalldatetime] NULL,
	[fecha_publicacion] [smalldatetime] NULL,
 CONSTRAINT [PK_eje_gp_ficha_1] PRIMARY KEY NONCLUSTERED 
(
	[id_ficha] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_entregables_documentos]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_entregables_documentos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_entregables_documentos](
	[id_documentos] [varchar](50) NOT NULL,
	[id_serial] [numeric](18, 0) NOT NULL,
	[fecha_register] [smalldatetime] NULL,
	[descripcion] [varchar](50) NULL,
	[ruta_id] [varchar](50) NULL,
	[who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
	[fecha_publicacion] [smalldatetime] NULL,
 CONSTRAINT [PK_eje_gp_ficha_entregables_documentos_3] PRIMARY KEY NONCLUSTERED 
(
	[id_documentos] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes_estado]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_estado]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes_estado](
	[id_estado] [int] NOT NULL,
	[descripcion] [varchar](100) NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_estado_19] PRIMARY KEY NONCLUSTERED 
(
	[id_estado] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_postulacion_estadosolicitud]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion_estadosolicitud]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_postulacion_estadosolicitud](
	[id_estado] [int] NOT NULL,
	[descripcion] [varchar](50) NULL,
 CONSTRAINT [PK_eje_gp_ficha_postulacion_estadosolicitud_22] PRIMARY KEY NONCLUSTERED 
(
	[id_estado] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_novedades]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_novedades]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_novedades](
	[id_novedad] [numeric](18, 0) NOT NULL,
	[tipo] [numeric](18, 0) NULL,
	[titulo] [varchar](200) NULL,
	[contenido] [varchar](200) NULL,
	[fecha_publicacion] [smalldatetime] NULL,
	[fecha_fin_publicacion] [smalldatetime] NULL,
	[who_publica] [varchar](50) NULL,
	[nom_who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[status_publicacion] [numeric](18, 0) NULL,
	[rut_imagen] [varchar](200) NULL,
 CONSTRAINT [PK_eje_gp_novedades_5] PRIMARY KEY NONCLUSTERED 
(
	[id_novedad] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_perfiles]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_perfiles]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_perfiles](
	[id_perfil] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](50) NULL,
	[ape_paterno] [varchar](50) NULL,
	[ape_materno] [varchar](50) NULL,
	[is_creator_ficha] [bit] NULL,
	[is_admin] [bit] NULL,
	[fec_create] [datetime] NULL,
 CONSTRAINT [PK_eje_gp_perfiles_27] PRIMARY KEY NONCLUSTERED 
(
	[id_perfil] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_proyecto_documentos_adicionales]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_documentos_adicionales]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_proyecto_documentos_adicionales](
	[id_documentacion_adicional] [varchar](50) NOT NULL,
	[id_serial] [numeric](18, 0) NOT NULL,
	[fecha_register] [smalldatetime] NULL,
	[descripcion] [varchar](50) NULL,
	[ruta_id] [varchar](50) NULL,
	[who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
	[fecha_publicacion] [smalldatetime] NULL,
 CONSTRAINT [PK_eje_gp_proyecto_documentos_adicionales_9] PRIMARY KEY NONCLUSTERED 
(
	[id_documentacion_adicional] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_proyecto_participaciones_entregables_felicitaciones]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_felicitaciones]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_felicitaciones](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_proyecto] [varchar](50) NOT NULL,
	[id_entregable] [varchar](50) NOT NULL,
	[aquien_felicita] [numeric](18, 0) NULL,
	[who_felicita] [numeric](18, 0) NULL,
	[nom_who_felicita] [numeric](18, 0) NOT NULL,
	[id_cargo_who_felicita] [varchar](50) NULL,
	[cargo_who_felicita] [varchar](50) NULL,
	[id_unidad_who_felicita] [varchar](50) NULL,
	[unidad_who_felicita] [varchar](50) NULL,
	[fecha_register] [smalldatetime] NULL,
	[observacion_felicitacion] [varchar](200) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
 CONSTRAINT [PK12] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_user_rol]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_user_rol]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_user_rol](
	[id_rol] [int] IDENTITY(1,1) NOT NULL,
	[who_rol] [numeric](18, 0) NOT NULL,
	[nom_who_postula] [numeric](18, 0) NOT NULL,
	[id_cargo_who_rol] [varchar](50) NULL,
	[cargo_who_rol] [varchar](50) NULL,
	[id_unidad_who_rol] [varchar](50) NULL,
	[unidad_who_rol] [varchar](50) NULL,
	[rol] [varchar](50) NULL,
	[estado_who_rol] [varchar](50) NULL,
	[who_asigna] [varchar](50) NULL,
	[fecha_register] [smalldatetime] NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
 CONSTRAINT [PK13] PRIMARY KEY NONCLUSTERED 
(
	[id_rol] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_proyecto]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_proyecto](
	[id_proyecto] [numeric](18, 0) NOT NULL,
	[id_ficha] [numeric](18, 0) NOT NULL,
	[nom_proyecto] [varchar](200) NULL,
	[objetivo_proyecto] [varchar](200) NULL,
	[aquien_proyecto] [varchar](200) NULL,
	[fecha_inicio] [smalldatetime] NULL,
	[fecha_fin] [smalldatetime] NULL,
	[fecha_publicacion] [smalldatetime] NULL,
	[fecha_recordatorio] [smalldatetime] NULL,
	[plazo_inscripcioninicio] [smalldatetime] NULL,
	[plazo_inscripcionfin] [smalldatetime] NULL,
	[id_prioridad] [numeric](18, 0) NULL,
	[fecha_owner] [smalldatetime] NULL,
	[who_owner] [varchar](50) NULL,
	[cargo_who_owner] [varchar](50) NULL,
	[unidad_who_owner] [varchar](50) NULL,
	[status_publicacion] [numeric](18, 0) NULL,
	[fecha_publica] [smalldatetime] NULL,
	[who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[id_documentacion_adicional] [numeric](18, 0) NULL,
	[rut_imagen_cabecera] [varchar](200) NULL,
	[id_status] [numeric](18, 0) NULL,
	[alcance_cantidad_entregables] [numeric](18, 0) NULL,
	[reales_cantidad_entregables] [numeric](18, 0) NULL,
	[porcentaje_avance] [numeric](18, 0) NULL,
 CONSTRAINT [PK_eje_gp_proyecto_7] PRIMARY KEY NONCLUSTERED 
(
	[id_proyecto] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_postulacion]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_postulacion](
	[id_postula] [int] IDENTITY(1,1) NOT NULL,
	[id_ficha] [numeric](18, 0) NOT NULL,
	[postula_rut] [int] NOT NULL,
	[postula_nombres] [varchar](150) NOT NULL,
	[postula_id_cargo] [varchar](150) NULL,
	[postula_cargo] [varchar](150) NULL,
	[postula_unid_id] [varchar](100) NULL,
	[postula_unid_desc] [varchar](500) NULL,
	[postula_fecha_register] [smalldatetime] NULL,
	[postula_descripcion] [varchar](2000) NULL,
	[confirma_rut] [int] NULL,
	[confirma_nombres] [varchar](150) NULL,
	[confirma_apellidos] [varchar](150) NULL,
	[id_estado_postulacion] [int] NOT NULL,
	[is_the_boss] [bit] NULL,
 CONSTRAINT [PK_eje_gp_ficha_postulacion] PRIMARY KEY NONCLUSTERED 
(
	[id_postula] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_foro]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_foro]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_foro](
	[id_ficha] [numeric](18, 0) NOT NULL,
	[id_foro] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_eje_gp_foro_23] PRIMARY KEY NONCLUSTERED 
(
	[id_foro] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_gp_ficha_files]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_files]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_files](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_ficha] [numeric](18, 0) NOT NULL,
	[file_name] [varchar](200) NULL,
	[file_id] [int] NULL,
	[descripcion] [varchar](2000) NULL,
	[weight] [int] NULL,
	[fecha_upload] [datetime] NULL,
	[is_deleted] [bit] NULL,
	[rut_upload] [int] NULL,
 CONSTRAINT [PK_eje_gp_ficha_files_28] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes](
	[id_ficha] [numeric](18, 0) NOT NULL,
	[id_serial] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[fecha_register] [smalldatetime] NULL,
	[tipo_participacion] [varchar](25) NULL,
	[id_cargo] [numeric](18, 0) NULL,
	[id_familia] [int] NULL,
	[descripcion_cargo] [varchar](50) NULL,
	[who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[medida_antiguedad] [int] NULL,
	[antiguedad] [numeric](18, 0) NULL,
	[cantidad] [numeric](18, 0) NULL,
	[rol] [varchar](50) NULL,
	[instancia_participacion] [varchar](50) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
	[id_estado] [int] NOT NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_4] PRIMARY KEY NONCLUSTERED 
(
	[id_serial] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_entregables]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_entregables]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_entregables](
	[id_ficha] [numeric](18, 0) NOT NULL,
	[id_entregable] [int] NOT NULL,
	[nombre] [varchar](200) NULL,
	[fecha] [smalldatetime] NULL,
	[contenido] [varchar](1000) NULL,
	[descripcion] [varchar](1000) NULL,
	[tipo] [varchar](50) NULL,
	[formato] [varchar](5) NULL,
	[id_documentos] [int] NULL,
	[fecha_entrega] [smalldatetime] NULL,
	[fecha_recuerdo] [smalldatetime] NOT NULL,
	[who_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
	[fecha_publicacion] [smalldatetime] NULL,
	[id_estado] [int] NULL,
 CONSTRAINT [PK_eje_gp_ficha_entregables_2] PRIMARY KEY NONCLUSTERED 
(
	[id_entregable] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes_participantes_discretos]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_participantes_discretos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes_participantes_discretos](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_ficha] [numeric](18, 0) NOT NULL,
	[rut] [int] NULL,
	[nombre] [varchar](50) NULL,
	[ape_paterno] [varchar](50) NULL,
	[ape_materno] [varchar](50) NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_participantes_discretos_30] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_entregables_files]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_entregables_files]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_entregables_files](
	[id_correlativo] [int] IDENTITY(1,1) NOT NULL,
	[id_file] [int] NULL,
	[id_entregable] [int] NOT NULL,
 CONSTRAINT [PK_eje_gp_entregables_files_16] PRIMARY KEY NONCLUSTERED 
(
	[id_correlativo] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes_unidades]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_unidades]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes_unidades](
	[id_serial] [numeric](18, 0) NOT NULL,
	[id_corr] [int] NOT NULL,
	[id_unidad] [varchar](100) NULL,
	[unidad] [varchar](100) NULL,
	[q] [int] NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_unidades] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes_cargos]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_cargos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes_cargos](
	[id_serial] [numeric](18, 0) NOT NULL,
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_cargo] [int] NULL,
	[cargo] [varchar](100) NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_cargos_15] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_ficha_participantes_centro_costos]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_centro_costos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_ficha_participantes_centro_costos](
	[id_serial] [numeric](18, 0) NOT NULL,
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_centro_costo] [int] NULL,
	[centro_costo] [varchar](100) NULL,
 CONSTRAINT [PK_eje_gp_ficha_participantes_centro_costos_1] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_foro_tarjeta_presentacion]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_tarjeta_presentacion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_foro_tarjeta_presentacion](
	[id_tarjeta] [int] IDENTITY(1,1) NOT NULL,
	[id_postula] [int] NOT NULL,
	[cant_respuestas] [int] NULL,
	[cant_hilos_creados] [int] NULL,
 CONSTRAINT [PK_eje_gp_foro_tarjeta_presentacion_25] PRIMARY KEY NONCLUSTERED 
(
	[id_tarjeta] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_gp_foro_hilos]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_hilos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_foro_hilos](
	[id_hilo] [int] IDENTITY(1,1) NOT NULL,
	[id_foro] [int] NOT NULL,
	[hilo_nombre] [varchar](255) NULL,
	[hilo_fecha_create] [datetime] NULL,
	[id_tarjeta_responsable] [int] NOT NULL,
	[is_closed] [bit] NULL,
	[hilo_fecha_last_response] [datetime] NULL,
 CONSTRAINT [PK_eje_gp_foro_hilos_24] PRIMARY KEY NONCLUSTERED 
(
	[id_hilo] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_foro_respuesta]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_respuesta]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_foro_respuesta](
	[id_respuesta] [int] IDENTITY(1,1) NOT NULL,
	[id_hilo] [int] NOT NULL,
	[id_tarjeta_respuesta] [int] NOT NULL,
	[fecha] [datetime] NULL,
	[comentario] [text] NULL,
	[is_edited] [bit] NULL,
	[is_first] [bit] NULL,
 CONSTRAINT [PK_eje_gp_foro_respuesta_26] PRIMARY KEY NONCLUSTERED 
(
	[id_respuesta] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_gp_orden_novedades]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_orden_novedades]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_orden_novedades](
	[ubicacion] [numeric](18, 0) NOT NULL,
	[id_novedad] [numeric](18, 0) NOT NULL,
 CONSTRAINT [PK_eje_gp_orden_novedades_6] PRIMARY KEY NONCLUSTERED 
(
	[id_novedad] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_gp_proyecto_participaciones_entregables]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_proyecto_participaciones_entregables](
	[id_proyecto] [numeric](18, 0) NOT NULL,
	[id_entregable] [varchar](50) NOT NULL,
	[who_entrega] [numeric](18, 0) NULL,
	[nom_who_entrega] [numeric](18, 0) NOT NULL,
	[id_cargo_who_entrega] [varchar](50) NULL,
	[cargo_who_entrega] [varchar](50) NULL,
	[id_unidad_who_entrega] [varchar](50) NULL,
	[unidad_who_entrega] [varchar](50) NULL,
	[fecha_register] [smalldatetime] NULL,
	[id_documentos_entrega] [numeric](18, 0) NULL,
	[id_ranking] [numeric](18, 0) NULL,
	[id_destaca_jp] [numeric](18, 0) NULL,
	[fecha_destaca_jp] [smalldatetime] NULL,
	[who_destaca_jp] [varchar](50) NULL,
	[nom_destaca_jp] [varchar](50) NULL,
	[id_cargo_who_destaca_jp] [varchar](50) NULL,
	[cargo_who_destaca_jp] [varchar](50) NULL,
	[id_unidad_who_destaca_jp] [varchar](50) NULL,
	[unidad_who_destaca_jp] [varchar](50) NULL,
	[id_publica] [numeric](18, 0) NULL,
	[fecha_publica] [smalldatetime] NULL,
	[who_publica] [varchar](50) NULL,
	[nom_publica] [varchar](50) NULL,
	[id_cargo_who_publica] [varchar](50) NULL,
	[cargo_who_publica] [varchar](50) NULL,
	[id_unidad_who_publica] [varchar](50) NULL,
	[unidad_who_publica] [varchar](50) NULL,
	[id_documentos_publica] [numeric](18, 0) NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
 CONSTRAINT [PK_eje_gp_proyecto_participaciones_entregables_10] PRIMARY KEY NONCLUSTERED 
(
	[id_entregable] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_gp_proyecto_participaciones_entregables_detalles]    Script Date: 03/20/2018 09:48:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_detalles]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_detalles](
	[id_proyecto] [numeric](18, 0) NOT NULL,
	[id_documentos_entrega] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[id_documentos_entrega_serie] [numeric](18, 0) NULL,
	[descripcion_documento] [varchar](50) NULL,
	[ruta_documento] [varchar](50) NULL,
	[fecha_register] [smalldatetime] NULL,
	[id_destaca_jp] [numeric](18, 0) NULL,
	[fecha_destaca_jp] [smalldatetime] NULL,
	[who_destaca_jp] [varchar](50) NULL,
	[nom_destaca_jp] [varchar](50) NULL,
	[id_cargo_who_destaca_jp] [varchar](50) NULL,
	[cargo_who_destaca_jp] [varchar](50) NULL,
	[id_unidad_who_destaca_jp] [varchar](50) NULL,
	[unidad_who_destaca_jp] [varchar](50) NULL,
	[id_publica] [numeric](18, 0) NULL,
	[fecha_publica] [smalldatetime] NULL,
	[id_estado_visualiza] [numeric](18, 0) NULL,
	[id_entregable] [varchar](50) NOT NULL,
 CONSTRAINT [PK_eje_gp_proyecto_participaciones_entregables_detalles_11] PRIMARY KEY NONCLUSTERED 
(
	[id_documentos_entrega] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_entregables17]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_entregables17]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_entregables_files]'))
ALTER TABLE [dbo].[eje_gp_entregables_files]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_entregables17] FOREIGN KEY([id_entregable])
REFERENCES [dbo].[eje_gp_ficha_entregables] ([id_entregable])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_entregables17]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_entregables_files]'))
ALTER TABLE [dbo].[eje_gp_entregables_files] CHECK CONSTRAINT [Refeje_gp_ficha_entregables17]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha6]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha6]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_entregables]'))
ALTER TABLE [dbo].[eje_gp_ficha_entregables]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha6] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha6]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_entregables]'))
ALTER TABLE [dbo].[eje_gp_ficha_entregables] CHECK CONSTRAINT [Refeje_gp_ficha6]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha30]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha30]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_files]'))
ALTER TABLE [dbo].[eje_gp_ficha_files]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha30] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha30]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_files]'))
ALTER TABLE [dbo].[eje_gp_ficha_files] CHECK CONSTRAINT [Refeje_gp_ficha30]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_participantes_estado20]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes_estado20]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_participantes_estado20] FOREIGN KEY([id_estado])
REFERENCES [dbo].[eje_gp_ficha_participantes_estado] ([id_estado])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes_estado20]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes] CHECK CONSTRAINT [Refeje_gp_ficha_participantes_estado20]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha8]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha8]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha8] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha8]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes] CHECK CONSTRAINT [Refeje_gp_ficha8]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_participantes16]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes16]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_cargos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_cargos]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_participantes16] FOREIGN KEY([id_serial])
REFERENCES [dbo].[eje_gp_ficha_participantes] ([id_serial])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes16]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_cargos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_cargos] CHECK CONSTRAINT [Refeje_gp_ficha_participantes16]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_participantes18]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes18]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_centro_costos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_centro_costos]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_participantes18] FOREIGN KEY([id_serial])
REFERENCES [dbo].[eje_gp_ficha_participantes] ([id_serial])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes18]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_centro_costos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_centro_costos] CHECK CONSTRAINT [Refeje_gp_ficha_participantes18]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha32]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha32]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_participantes_discretos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_participantes_discretos]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha32] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha32]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_participantes_discretos]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_participantes_discretos] CHECK CONSTRAINT [Refeje_gp_ficha32]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_participantes19]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes19]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_unidades]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_unidades]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_participantes19] FOREIGN KEY([id_serial])
REFERENCES [dbo].[eje_gp_ficha_participantes] ([id_serial])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_participantes19]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_participantes_unidades]'))
ALTER TABLE [dbo].[eje_gp_ficha_participantes_unidades] CHECK CONSTRAINT [Refeje_gp_ficha_participantes19]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_postulacion_estadosolicitud23]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_postulacion_estadosolicitud23]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion]'))
ALTER TABLE [dbo].[eje_gp_ficha_postulacion]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_postulacion_estadosolicitud23] FOREIGN KEY([id_estado_postulacion])
REFERENCES [dbo].[eje_gp_ficha_postulacion_estadosolicitud] ([id_estado])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_postulacion_estadosolicitud23]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion]'))
ALTER TABLE [dbo].[eje_gp_ficha_postulacion] CHECK CONSTRAINT [Refeje_gp_ficha_postulacion_estadosolicitud23]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha21]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha21]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion]'))
ALTER TABLE [dbo].[eje_gp_ficha_postulacion]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha21] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha21]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_ficha_postulacion]'))
ALTER TABLE [dbo].[eje_gp_ficha_postulacion] CHECK CONSTRAINT [Refeje_gp_ficha21]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha24]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha24]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro]'))
ALTER TABLE [dbo].[eje_gp_foro]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha24] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha24]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro]'))
ALTER TABLE [dbo].[eje_gp_foro] CHECK CONSTRAINT [Refeje_gp_ficha24]
GO
/****** Object:  ForeignKey [Refeje_gp_foro_tarjeta_presentacion27]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_tarjeta_presentacion27]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_hilos]'))
ALTER TABLE [dbo].[eje_gp_foro_hilos]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_foro_tarjeta_presentacion27] FOREIGN KEY([id_tarjeta_responsable])
REFERENCES [dbo].[eje_gp_foro_tarjeta_presentacion] ([id_tarjeta])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_tarjeta_presentacion27]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_hilos]'))
ALTER TABLE [dbo].[eje_gp_foro_hilos] CHECK CONSTRAINT [Refeje_gp_foro_tarjeta_presentacion27]
GO
/****** Object:  ForeignKey [Refeje_gp_foro25]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro25]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_hilos]'))
ALTER TABLE [dbo].[eje_gp_foro_hilos]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_foro25] FOREIGN KEY([id_foro])
REFERENCES [dbo].[eje_gp_foro] ([id_foro])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro25]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_hilos]'))
ALTER TABLE [dbo].[eje_gp_foro_hilos] CHECK CONSTRAINT [Refeje_gp_foro25]
GO
/****** Object:  ForeignKey [Refeje_gp_foro_hilos28]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_hilos28]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_respuesta]'))
ALTER TABLE [dbo].[eje_gp_foro_respuesta]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_foro_hilos28] FOREIGN KEY([id_hilo])
REFERENCES [dbo].[eje_gp_foro_hilos] ([id_hilo])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_hilos28]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_respuesta]'))
ALTER TABLE [dbo].[eje_gp_foro_respuesta] CHECK CONSTRAINT [Refeje_gp_foro_hilos28]
GO
/****** Object:  ForeignKey [Refeje_gp_foro_tarjeta_presentacion29]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_tarjeta_presentacion29]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_respuesta]'))
ALTER TABLE [dbo].[eje_gp_foro_respuesta]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_foro_tarjeta_presentacion29] FOREIGN KEY([id_tarjeta_respuesta])
REFERENCES [dbo].[eje_gp_foro_tarjeta_presentacion] ([id_tarjeta])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_foro_tarjeta_presentacion29]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_respuesta]'))
ALTER TABLE [dbo].[eje_gp_foro_respuesta] CHECK CONSTRAINT [Refeje_gp_foro_tarjeta_presentacion29]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha_postulacion26]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_postulacion26]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_tarjeta_presentacion]'))
ALTER TABLE [dbo].[eje_gp_foro_tarjeta_presentacion]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha_postulacion26] FOREIGN KEY([id_postula])
REFERENCES [dbo].[eje_gp_ficha_postulacion] ([id_postula])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha_postulacion26]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_foro_tarjeta_presentacion]'))
ALTER TABLE [dbo].[eje_gp_foro_tarjeta_presentacion] CHECK CONSTRAINT [Refeje_gp_ficha_postulacion26]
GO
/****** Object:  ForeignKey [Refeje_gp_novedades3]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_novedades3]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_orden_novedades]'))
ALTER TABLE [dbo].[eje_gp_orden_novedades]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_novedades3] FOREIGN KEY([id_novedad])
REFERENCES [dbo].[eje_gp_novedades] ([id_novedad])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_novedades3]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_orden_novedades]'))
ALTER TABLE [dbo].[eje_gp_orden_novedades] CHECK CONSTRAINT [Refeje_gp_novedades3]
GO
/****** Object:  ForeignKey [Refeje_gp_ficha5]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha5]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto]'))
ALTER TABLE [dbo].[eje_gp_proyecto]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_ficha5] FOREIGN KEY([id_ficha])
REFERENCES [dbo].[eje_gp_ficha] ([id_ficha])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_ficha5]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto]'))
ALTER TABLE [dbo].[eje_gp_proyecto] CHECK CONSTRAINT [Refeje_gp_ficha5]
GO
/****** Object:  ForeignKey [Refeje_gp_proyecto7]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto7]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_proyecto7] FOREIGN KEY([id_proyecto])
REFERENCES [dbo].[eje_gp_proyecto] ([id_proyecto])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto7]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables] CHECK CONSTRAINT [Refeje_gp_proyecto7]
GO
/****** Object:  ForeignKey [Refeje_gp_proyecto_participaciones_entregables12]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto_participaciones_entregables12]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_detalles]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_detalles]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_proyecto_participaciones_entregables12] FOREIGN KEY([id_entregable])
REFERENCES [dbo].[eje_gp_proyecto_participaciones_entregables] ([id_entregable])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto_participaciones_entregables12]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_detalles]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_detalles] CHECK CONSTRAINT [Refeje_gp_proyecto_participaciones_entregables12]
GO
/****** Object:  ForeignKey [Refeje_gp_proyecto13]    Script Date: 03/20/2018 09:48:03 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto13]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_detalles]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_detalles]  WITH CHECK ADD  CONSTRAINT [Refeje_gp_proyecto13] FOREIGN KEY([id_proyecto])
REFERENCES [dbo].[eje_gp_proyecto] ([id_proyecto])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_gp_proyecto13]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_gp_proyecto_participaciones_entregables_detalles]'))
ALTER TABLE [dbo].[eje_gp_proyecto_participaciones_entregables_detalles] CHECK CONSTRAINT [Refeje_gp_proyecto13]
GO
