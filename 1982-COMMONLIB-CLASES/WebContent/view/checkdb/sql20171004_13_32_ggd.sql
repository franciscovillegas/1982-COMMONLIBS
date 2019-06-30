if not exists (select 1 from eje_ggd_estados_evaluacion where id = 13)
insert into eje_ggd_estados_evaluacion (id, status) values (13, 'Plan finalizado')