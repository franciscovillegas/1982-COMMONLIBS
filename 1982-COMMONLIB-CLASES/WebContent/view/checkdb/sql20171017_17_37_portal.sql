IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'op10' AND OBJECT_ID = OBJECT_ID(N'eje_generico_perfiles_webmatico'))
ALTER TABLE eje_generico_perfiles_webmatico add op10 bit