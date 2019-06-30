IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'q' AND OBJECT_ID = OBJECT_ID(N'eje_gp_ficha_participantes_unidades'))
ALTER TABLE eje_gp_ficha_participantes_unidades ADD q INT