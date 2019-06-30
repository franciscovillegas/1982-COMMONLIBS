GO
/****** Object:  Table [dbo].[eje_generico_bolsadegatos_type]    Script Date: 03/20/2018 16:15:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_type]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_generico_bolsadegatos_type](
	[id_type] [int] NOT NULL,
	[nombre] [varchar](50) NULL,
 CONSTRAINT [PKeje_generico_bolsadegatos_type] PRIMARY KEY NONCLUSTERED 
(
	[id_type] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_generico_bolsadegatos]    Script Date: 03/20/2018 16:15:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_generico_bolsadegatos](
	[id_upload] [int] IDENTITY(1,1) NOT NULL,
	[usuario_rut] [int] NULL,
	[usuario_nombre] [varchar](100) NULL,
	[process_time] [int] NULL,
	[fecha_upload] [datetime] NULL,
	[descripcion] [varchar](500) NULL,
	[id_file] [int] NULL,
	[cant_registros] [int] NULL,
	[nombre_tabla] [varchar](100) NULL,
	[id_type] [int] NOT NULL,
 CONSTRAINT [PKeje_generico_bolsagatos] PRIMARY KEY NONCLUSTERED 
(
	[id_upload] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[eje_generico_bolsadegatos_data]    Script Date: 03/20/2018 16:15:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_data]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_generico_bolsadegatos_data](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_upload] [int] NOT NULL,
 CONSTRAINT [PKeje_generico_bolsagatos_data] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[eje_generico_bolsadegatos_referencias]    Script Date: 03/20/2018 16:15:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_referencias]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[eje_generico_bolsadegatos_referencias](
	[id_corr] [int] IDENTITY(1,1) NOT NULL,
	[id_upload] [int] NOT NULL,
	[rut] [varchar](10) NULL,
 CONSTRAINT [PKeje_generico_bolsagatos_referencias] PRIMARY KEY NONCLUSTERED 
(
	[id_corr] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [Refeje_generico_bolsadegatos_type4]    Script Date: 03/20/2018 16:15:18 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos_type4]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos]  WITH CHECK ADD  CONSTRAINT [Refeje_generico_bolsadegatos_type4] FOREIGN KEY([id_type])
REFERENCES [dbo].[eje_generico_bolsadegatos_type] ([id_type])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos_type4]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos] CHECK CONSTRAINT [Refeje_generico_bolsadegatos_type4]
GO
/****** Object:  ForeignKey [Refeje_generico_bolsadegatos3]    Script Date: 03/20/2018 16:15:18 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos3]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_data]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos_data]  WITH CHECK ADD  CONSTRAINT [Refeje_generico_bolsadegatos3] FOREIGN KEY([id_upload])
REFERENCES [dbo].[eje_generico_bolsadegatos] ([id_upload])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos3]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_data]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos_data] CHECK CONSTRAINT [Refeje_generico_bolsadegatos3]
GO
/****** Object:  ForeignKey [Refeje_generico_bolsadegatos1]    Script Date: 03/20/2018 16:15:18 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos1]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_referencias]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos_referencias]  WITH CHECK ADD  CONSTRAINT [Refeje_generico_bolsadegatos1] FOREIGN KEY([id_upload])
REFERENCES [dbo].[eje_generico_bolsadegatos] ([id_upload])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[Refeje_generico_bolsadegatos1]') AND parent_object_id = OBJECT_ID(N'[dbo].[eje_generico_bolsadegatos_referencias]'))
ALTER TABLE [dbo].[eje_generico_bolsadegatos_referencias] CHECK CONSTRAINT [Refeje_generico_bolsadegatos1]
GO
