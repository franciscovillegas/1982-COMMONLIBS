IF (exists(select character_maximum_length from INFORMATION_SCHEMA.COLUMNS IC where TABLE_NAME = 'eje_ges_user_app' and COLUMN_NAME = 'app_id' and character_maximum_length = 10))
ALTER TABLE eje_ges_user_app ALTER COLUMN app_id VARCHAR (20) NOT NULL