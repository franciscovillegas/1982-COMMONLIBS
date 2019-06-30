if (not exists(select * from eje_ges_aplicacion where app_id = 'participa_adm'))
insert into eje_ges_aplicacion (app_id, app_desc, orden) values ('participa_adm', 'Administrador de Participa', 25)