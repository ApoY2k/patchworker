CREATE SEQUENCE IF NOT EXISTS %s_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE IF NOT EXISTS "public"."%s" (
    "id" integer DEFAULT nextval('%s_id_seq') NOT NULL,
    "patches" character varying NOT NULL,
    "p1_tracker_position" integer NOT NULL,
    "p1_button_multiplier" integer NOT NULL,
    "p1_buttons" integer NOT NULL,
    "p1_special_patches" integer NOT NULL,
    "p1_actions_taken" integer NOT NULL,
    "p1_board" character varying NOT NULL,
    "p2_tracker_position" integer NOT NULL,
    "p2_button_multiplier" integer NOT NULL,
    "p2_buttons" integer NOT NULL,
    "p2_special_patches" integer NOT NULL,
    "p2_actions_taken" integer NOT NULL,
    "p2_board" character varying NOT NULL,
    "is_p1_turn" boolean NOT NULL,
    "score" double precision NOT NULL,
    CONSTRAINT "%s_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "%s_checksum" UNIQUE ("patches", "p1_tracker_position", "p1_button_multiplier", "p1_buttons", "p1_special_patches", "p1_actions_taken", "p1_board", "p2_tracker_position", "p2_button_multiplier", "p2_buttons", "p2_special_patches", "p2_actions_taken", "p2_board", "is_p1_turn", "score")
) WITH (oids = false);
