SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;


CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = FALSE;

CREATE SEQUENCE IF NOT EXISTS auto_increment_users
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  CACHE 1;


ALTER TABLE auto_increment_users
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS "Users" (
  "User_id"           BIGINT DEFAULT nextval('auto_increment_users' :: REGCLASS) NOT NULL,
  "Login"             CHARACTER VARYING(18)                                      NOT NULL UNIQUE,
  "Password"          CHARACTER VARYING(30)                                      NOT NULL,
  "Role"              CHARACTER VARYING(18)                                      NOT NULL,
  "Registration_date" DATE                                                       NOT NULL,
  PRIMARY KEY ("User_id")
);


ALTER TABLE "Users"
  OWNER TO postgres;


CREATE SEQUENCE IF NOT EXISTS auto_increment_journal
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  CACHE 1;


ALTER TABLE auto_increment_journal
  OWNER TO postgres;


CREATE TABLE IF NOT EXISTS "Journal" (
  "Journal_id"  BIGINT DEFAULT nextval('auto_increment_journal' :: REGCLASS) NOT NULL,
  "Name"        CHARACTER VARYING(18)                                        NOT NULL UNIQUE,
  "Description" CHARACTER VARYING(80),
  "User_id"     BIGINT,
  PRIMARY KEY ("Journal_id"),
  FOREIGN KEY ("User_id") REFERENCES "Users" ("User_id") ON UPDATE CASCADE ON DELETE CASCADE
);


ALTER TABLE "Journal"
  OWNER TO postgres;


CREATE SEQUENCE IF NOT EXISTS auto_increment_tasks
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  CACHE 1;


ALTER TABLE auto_increment_tasks
  OWNER TO postgres;


CREATE TABLE IF NOT EXISTS "Tasks" (
  "Task_id"           BIGINT DEFAULT nextval('auto_increment_tasks' :: REGCLASS) NOT NULL,
  "Name"              CHARACTER VARYING(18)                                      NOT NULL UNIQUE,
  "Status"            CHARACTER VARYING(18)                                      NOT NULL,
  "Description"       CHARACTER VARYING(80),
  "Planned_date"      DATE                                                       NOT NULL,
  "Notification_date" DATE                                                       NOT NULL,
  "Upload_date"       DATE                                                       NOT NULL,
  "Change_date"       DATE                                                       NOT NULL,
  "Journal_id"        BIGINT                                                     NOT NULL,
  PRIMARY KEY ("Task_id"),
  FOREIGN KEY ("Journal_id") REFERENCES "Journal" ("Journal_id") ON UPDATE CASCADE ON DELETE CASCADE
);


ALTER TABLE "Tasks"
  OWNER TO postgres;


CREATE INDEX IF NOT EXISTS Tasks_User_id_fkey
  ON "Tasks" USING BTREE ("Journal_id");


CREATE INDEX IF NOT EXISTS Journal_User_id_fkey
  ON "Journal" USING BTREE ("User_id");
