CREATE SEQUENCE IF NOT EXISTS %s_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE IF NOT EXISTS "public"."%s" (
    "id" integer DEFAULT nextval('%s_id_seq') NOT NULL,
    "hash" integer NOT NULL,
    "parenthash" integer NOT NULL,
    "patches" character varying NOT NULL,
    "p1_tp" integer NOT NULL,
    "p1_bm" integer NOT NULL,
    "p1_bs" integer NOT NULL,
    "p1_sp" integer NOT NULL,
    "p1_at" integer NOT NULL,
    "p1_bd" character varying NOT NULL,
    "p2_tp" integer NOT NULL,
    "p2_bm" integer NOT NULL,
    "p2_bs" integer NOT NULL,
    "p2_sp" integer NOT NULL,
    "p2_at" integer NOT NULL,
    "p2_bd" character varying NOT NULL,
    "p1_tn" boolean NOT NULL,
    "score" double precision NOT NULL,
    CONSTRAINT "%s_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "%s_checksum" UNIQUE ("patches", "p1_tp", "p1_bm", "p1_bs", "p1_sp", "p1_at", "p1_bd", "p2_tp", "p2_bm", "p2_bs", "p2_sp", "p2_at", "p2_bd", "p1_tn")
) WITH (oids = false);
