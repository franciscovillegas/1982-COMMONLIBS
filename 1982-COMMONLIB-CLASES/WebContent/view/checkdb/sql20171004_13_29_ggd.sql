if not exists (select 1 from eje_ggd_estados_evaluacion where id = 12) 
insert into eje_ggd_estados_evaluacion (id, status) values (12, 'Plan diseñado')