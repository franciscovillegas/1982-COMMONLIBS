IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'is_closed' AND OBJECT_ID = OBJECT_ID(N'eje_gp_foro_hilos'))
ALTER TABLE eje_gp_foro_hilos add is_closed bit