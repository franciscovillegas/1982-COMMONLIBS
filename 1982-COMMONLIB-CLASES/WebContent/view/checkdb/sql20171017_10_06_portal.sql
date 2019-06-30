IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'secuencia' AND OBJECT_ID = OBJECT_ID(N'eje_ges_grupo_familiar')) 
ALTER TABLE eje_ges_grupo_familiar add secuencia int