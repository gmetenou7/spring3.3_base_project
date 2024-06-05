DO $$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_database
      WHERE datname = 'spring_starter'
   ) THEN
      PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE spring_starter');
END IF;
END
$$;

\c spring_starter;

CREATE SEQUENCE avis_id_seq;
CREATE TABLE IF NOT EXISTS avis (
    id integer NOT NULL DEFAULT nextval('avis_id_seq'),
    message varchar(255) NOT NULL,
    statut varchar(255) NOT NULL,
    PRIMARY KEY (id)
);
ALTER SEQUENCE avis_id_seq OWNED BY avis.id;
ALTER TABLE IF EXISTS public.avis OWNER to postgres;