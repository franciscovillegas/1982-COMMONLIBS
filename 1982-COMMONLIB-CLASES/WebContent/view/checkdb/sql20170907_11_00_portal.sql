IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_ges_tracking' AND COLUMN_NAME = 'context') 
alter table eje_ges_tracking add  context varchar(100)