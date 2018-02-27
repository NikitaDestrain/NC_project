SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;


CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;


CREATE SEQUENCE auto_increment_journal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auto_increment_journal OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE "Journal" (
    "Journal_id" bigint DEFAULT nextval('auto_increment_journal'::regclass) NOT NULL,
    "Name" character varying(18) NOT NULL,
    "Description" character varying(80),
    "User_id" bigint
);


ALTER TABLE "Journal" OWNER TO postgres;


CREATE SEQUENCE auto_increment_tasks
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auto_increment_tasks OWNER TO postgres;


CREATE TABLE "Tasks" (
    "Task_id" bigint DEFAULT nextval('auto_increment_tasks'::regclass) NOT NULL,
    "Name" character varying(18) NOT NULL,
    "Description" character varying(80),
    "Planned_date" date NOT NULL,
    "Notification_date" date NOT NULL,
    "Upload_date" date NOT NULL,
    "Change_date" date NOT NULL,
    "Journal_id" bigint NOT NULL
);


ALTER TABLE "Tasks" OWNER TO postgres;


CREATE SEQUENCE auto_increment_users
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auto_increment_users OWNER TO postgres;


CREATE TABLE "Users" (
    "User_id" bigint DEFAULT nextval('auto_increment_users'::regclass) NOT NULL,
    "Login" character varying(18) NOT NULL,
    "Password" character varying(18) NOT NULL,
    "Role" character varying(18) NOT NULL,
    "Registration_date" date NOT NULL
);


ALTER TABLE "Users" OWNER TO postgres;


ALTER TABLE ONLY "Journal"
    ADD CONSTRAINT "Journal_pkey" PRIMARY KEY ("Journal_id");


ALTER TABLE ONLY "Tasks"
    ADD CONSTRAINT "Tasks_pkey" PRIMARY KEY ("Task_id");


ALTER TABLE ONLY "Users"
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY ("User_id");


CREATE INDEX fki_journal_id_fk ON "Tasks" USING btree ("Journal_id");


CREATE INDEX fki_user_id_fk ON "Journal" USING btree ("User_id");


ALTER TABLE ONLY "Tasks"
    ADD CONSTRAINT journal_id_fk FOREIGN KEY ("Journal_id") REFERENCES "Journal"("Journal_id");


ALTER TABLE ONLY "Journal"
    ADD CONSTRAINT user_id_fk FOREIGN KEY ("User_id") REFERENCES "Users"("User_id") ON UPDATE CASCADE ON DELETE CASCADE;