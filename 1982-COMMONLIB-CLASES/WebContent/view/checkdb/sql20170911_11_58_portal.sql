IF NOT EXISTS (SELECT * FROM eje_ges_user_app_func WHERE APP_ID =  'miestruc_likeboss')
INSERT INTO eje_ges_user_app_func (APP_ID, DESCRIPCION, ORDEN, IS_ADMINISTRABLE) VALUES ('miestruc_likeboss','Puede ver la estructura dependiente', 1,1)