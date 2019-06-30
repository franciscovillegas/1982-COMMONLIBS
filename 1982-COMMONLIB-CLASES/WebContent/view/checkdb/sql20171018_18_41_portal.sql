IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'hilo_fecha_last_response' AND OBJECT_ID = OBJECT_ID(N'eje_gp_foro_hilos'))
ALTER TABLE eje_gp_foro_hilos add hilo_fecha_last_response datetime