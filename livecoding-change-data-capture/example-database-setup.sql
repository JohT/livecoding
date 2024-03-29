--
-- PostgreSQL Change Data Capture (CDC) Example - Admin Settings that are needed for change data capture using debezium:
-- The database needs to be restarted after that.  
--
ALTER SYSTEM SET wal_level = logical;
SELECT * from pg_settings WHERE name ='wal_level';

--
-- PostgreSQL Change Data Capture (CDC) Example - Table Setup
--
CREATE SCHEMA IF NOT EXISTS "change_data_capture_example";

CREATE TABLE IF NOT EXISTS "change_data_capture_example"."product" (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL, 
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(255) NOT NULL,
	PRIMARY KEY (ID)
);

--
-- PostgreSQL Change Data Capture (CDC) Example - Table Setting REPLICA IDENTITY
-- REPLICA IDENTITY FULL assures that the data change event always contains the "before" state
-- for UPDATEs and DELETEs on that specific table. 
-- References:
-- - https://www.postgresql.org/docs/current/sql-altertable.html#SQL-CREATETABLE-REPLICA-IDENTITY
-- - https://stackoverflow.com/questions/55249431/find-replica-identity-for-a-postgres-table
--
ALTER TABLE IF EXISTS "change_data_capture_example"."product" REPLICA IDENTITY FULL;
SELECT relname, relreplident FROM pg_class WHERE oid = 'change_data_capture_example.product'::regclass;

SELECT *
  FROM information_schema.tables
 WHERE table_type = 'BASE TABLE'
   AND table_schema = 'change_data_capture_example';

--
-- PostgreSQL Change Data Capture (CDC) Example - Contens
--
INSERT INTO "change_data_capture_example"."product"(NAME, DESCRIPTION) values ('Umbrella UM-10','Large 10m diameter sun umbrella');
UPDATE "change_data_capture_example"."product" SET DESCRIPTION = '10m diameter sun umbrella' WHERE NAME = 'Umbrella LX-3';
INSERT INTO "change_data_capture_example"."product"(NAME, DESCRIPTION) values ('Chair CH-1BK','Small, black wood chair');
SELECT * FROM "change_data_capture_example"."product";
DELETE FROM "change_data_capture_example"."product";