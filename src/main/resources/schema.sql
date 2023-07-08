CREATE TABLE IF NOT EXISTS project (
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    proxem_token VARCHAR(255) NOT NULL CHECK(proxem_token <> ''),
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS connector (
    connector_type       VARCHAR(255)     NOT NULL,
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    separator VARCHAR(255),
    encoding VARCHAR(255) ,
    path VARCHAR(255) ,
    quoting_caracter VARCHAR(255),
    escaping_caracter VARCHAR(255) ,
    contains_headers BOOLEAN ,
    jdbc_url  VARCHAR(255),
    username   VARCHAR(255),
    password   VARCHAR(255),
    class_name  VARCHAR(255),
    table_name  VARCHAR(255) ,
    initial_query VARCHAR(255),
    checkpoint_column VARCHAR(255),
    incremental_variable	VARCHAR(255),
    incremental_query VARCHAR(255),
    mode VARCHAR(255),
     PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS field (
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    reference_connector VARCHAR(255) NOT NULL CHECK(reference_connector <> ''),
    position INTEGER NOT NULL CHECK(position > 0),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    meta VARCHAR(255) NOT NULL CHECK(meta <> ''),
    part_of_document_identity BOOLEAN NOT NULL,
    included BOOLEAN NOT NULL,
    field_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reference_connector FOREIGN KEY (reference_connector) REFERENCES connector(id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS scheduler (
    id INTEGER NOT NULL ,
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    scan_mode VARCHAR(255),
    starts_time VARCHAR(255),
    cron_expression VARCHAR(255),
    PRIMARY KEY (id),
    connector_id VARCHAR(255)  NOT NULL REFERENCES connector(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER NOT NULL ,
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    PRIMARY KEY (id)
    );
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER NOT NULL ,
        username VARCHAR(255) NOT NULL CHECK(username <> ''),
        email VARCHAR(255),
        password VARCHAR(255),
        PRIMARY KEY (id)
    );
        CREATE TABLE IF NOT EXISTS user_roles (
            user_id INTEGER NOT NULL  REFERENCES users(id) ,
            role_id INTEGER NOT NULL REFERENCES roles(id),

            PRIMARY KEY (user_id,role_id)
        );

/*
CREATE TABLE qrtz_job_details
  (
     sched_name        VARCHAR(120) NOT NULL,
     job_name          VARCHAR(200) NOT NULL,
     job_group         VARCHAR(200) NOT NULL,
     description       VARCHAR(250) NULL,
     job_class_name    VARCHAR(250) NOT NULL,
     is_durable        BOOL NOT NULL,
     is_nonconcurrent  BOOL NOT NULL,
     is_update_data    BOOL NOT NULL,
     requests_recovery BOOL NOT NULL,
     job_data          BYTEA NULL,
     PRIMARY KEY (sched_name, job_name, job_group)
  );

CREATE TABLE qrtz_triggers
  (
     sched_name     VARCHAR(120) NOT NULL,
     trigger_name   VARCHAR(200) NOT NULL,
     trigger_group  VARCHAR(200) NOT NULL,
     job_name       VARCHAR(200) NOT NULL,
     job_group      VARCHAR(200) NOT NULL,
     description    VARCHAR(250) NULL,
     next_fire_time BIGINT NULL,
     prev_fire_time BIGINT NULL,
     priority       INTEGER NULL,
     trigger_state  VARCHAR(16) NOT NULL,
     trigger_type   VARCHAR(8) NOT NULL,
     start_time     BIGINT NOT NULL,
     end_time       BIGINT NULL,
     calendar_name  VARCHAR(200) NULL,
     misfire_instr  SMALLINT NULL,
     job_data       BYTEA NULL,
     PRIMARY KEY (sched_name, trigger_name, trigger_group),
     FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details(
     sched_name, job_name, job_group)
  );

CREATE TABLE qrtz_simple_triggers
  (
     sched_name      VARCHAR(120) NOT NULL,
     trigger_name    VARCHAR(200) NOT NULL,
     trigger_group   VARCHAR(200) NOT NULL,
     repeat_count    BIGINT NOT NULL,
     repeat_interval BIGINT NOT NULL,
     times_triggered BIGINT NOT NULL,
     PRIMARY KEY (sched_name, trigger_name, trigger_group),
     FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
     qrtz_triggers(sched_name, trigger_name, trigger_group)
  );

CREATE TABLE qrtz_cron_triggers
  (
     sched_name      VARCHAR(120) NOT NULL,
     trigger_name    VARCHAR(200) NOT NULL,
     trigger_group   VARCHAR(200) NOT NULL,
     cron_expression VARCHAR(120) NOT NULL,
     time_zone_id    VARCHAR(80),
     PRIMARY KEY (sched_name, trigger_name, trigger_group),
     FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
     qrtz_triggers(sched_name, trigger_name, trigger_group)
  );

CREATE TABLE qrtz_simprop_triggers
  (
     sched_name    VARCHAR(120) NOT NULL,
     trigger_name  VARCHAR(200) NOT NULL,
     trigger_group VARCHAR(200) NOT NULL,
     str_prop_1    VARCHAR(512) NULL,
     str_prop_2    VARCHAR(512) NULL,
     str_prop_3    VARCHAR(512) NULL,
     int_prop_1    INT NULL,
     int_prop_2    INT NULL,
     long_prop_1   BIGINT NULL,
     long_prop_2   BIGINT NULL,
     dec_prop_1    NUMERIC(13, 4) NULL,
     dec_prop_2    NUMERIC(13, 4) NULL,
     bool_prop_1   BOOL NULL,
     bool_prop_2   BOOL NULL,
     PRIMARY KEY (sched_name, trigger_name, trigger_group),
     FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
     qrtz_triggers(sched_name, trigger_name, trigger_group)
  );

CREATE TABLE qrtz_blob_triggers
  (
     sched_name    VARCHAR(120) NOT NULL,
     trigger_name  VARCHAR(200) NOT NULL,
     trigger_group VARCHAR(200) NOT NULL,
     blob_data     BYTEA NULL,
     PRIMARY KEY (sched_name, trigger_name, trigger_group),
     FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES
     qrtz_triggers(sched_name, trigger_name, trigger_group)
  );

CREATE TABLE qrtz_calendars
  (
     sched_name    VARCHAR(120) NOT NULL,
     calendar_name VARCHAR(200) NOT NULL,
     calendar      BYTEA NOT NULL,
     PRIMARY KEY (sched_name, calendar_name)
  );

CREATE TABLE qrtz_paused_trigger_grps
  (
     sched_name    VARCHAR(120) NOT NULL,
     trigger_group VARCHAR(200) NOT NULL,
     PRIMARY KEY (sched_name, trigger_group)
  );

CREATE TABLE qrtz_fired_triggers
  (
     sched_name        VARCHAR(120) NOT NULL,
     entry_id          VARCHAR(95) NOT NULL,
     trigger_name      VARCHAR(200) NOT NULL,
     trigger_group     VARCHAR(200) NOT NULL,
     instance_name     VARCHAR(200) NOT NULL,
     fired_time        BIGINT NOT NULL,
     sched_time        BIGINT NOT NULL,
     priority          INTEGER NOT NULL,
     state             VARCHAR(16) NOT NULL,
     job_name          VARCHAR(200) NULL,
     job_group         VARCHAR(200) NULL,
     is_nonconcurrent  BOOL NULL,
     requests_recovery BOOL NULL,
     PRIMARY KEY (sched_name, entry_id)
  );

CREATE TABLE qrtz_scheduler_state
  (
     sched_name        VARCHAR(120) NOT NULL,
     instance_name     VARCHAR(200) NOT NULL,
     last_checkin_time BIGINT NOT NULL,
     checkin_interval  BIGINT NOT NULL,
     PRIMARY KEY (sched_name, instance_name)
  );

CREATE TABLE qrtz_locks
  (
     sched_name VARCHAR(120) NOT NULL,
     lock_name  VARCHAR(40) NOT NULL,
     PRIMARY KEY (sched_name, lock_name)
  );

CREATE INDEX idx_qrtz_j_req_recovery
  ON qrtz_job_details(sched_name, requests_recovery);

CREATE INDEX idx_qrtz_j_grp
  ON qrtz_job_details(sched_name, job_group);

CREATE INDEX idx_qrtz_t_j
  ON qrtz_triggers(sched_name, job_name, job_group);

CREATE INDEX idx_qrtz_t_jg
  ON qrtz_triggers(sched_name, job_group);

CREATE INDEX idx_qrtz_t_c
  ON qrtz_triggers(sched_name, calendar_name);

CREATE INDEX idx_qrtz_t_g
  ON qrtz_triggers(sched_name, trigger_group);

CREATE INDEX idx_qrtz_t_state
  ON qrtz_triggers(sched_name, trigger_state);

CREATE INDEX idx_qrtz_t_n_state
  ON qrtz_triggers(sched_name, trigger_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_n_g_state
  ON qrtz_triggers(sched_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_next_fire_time
  ON qrtz_triggers(sched_name, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st
  ON qrtz_triggers(sched_name, trigger_state, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_misfire
  ON qrtz_triggers(sched_name, misfire_instr, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st_misfire
  ON qrtz_triggers(sched_name, misfire_instr, next_fire_time, trigger_state);

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp
  ON qrtz_triggers(sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_ft_trig_inst_name
  ON qrtz_fired_triggers(sched_name, instance_name);

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry
  ON qrtz_fired_triggers(sched_name, instance_name, requests_recovery);

CREATE INDEX idx_qrtz_ft_j_g
  ON qrtz_fired_triggers(sched_name, job_name, job_group);

CREATE INDEX idx_qrtz_ft_jg
  ON qrtz_fired_triggers(sched_name, job_group);

CREATE INDEX idx_qrtz_ft_t_g
  ON qrtz_fired_triggers(sched_name, trigger_name, trigger_group);

CREATE INDEX idx_qrtz_ft_tg
  ON qrtz_fired_triggers(sched_name, trigger_group);
*/










/*
CREATE TABLE qrtz_blob_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    blob_data text,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_calendars (
    calendar_name character varying(80) NOT NULL,
    calendar text NOT NULL,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_cron_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    cron_expression character varying(80) NOT NULL,
    time_zone_id character varying(80),
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_fired_triggers (
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    instance_name character varying(80) NOT NULL,
    fired_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(80),
    job_group character varying(80),
    is_nonconcurrent boolean,
    is_update_data boolean,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL,
    sched_time bigint NOT NULL,
    requests_recovery boolean
);

CREATE TABLE qrtz_job_details (
    job_name character varying(128) NOT NULL,
    job_group character varying(80) NOT NULL,
    description character varying(120),
    job_class_name character varying(200) NOT NULL,
    is_durable boolean,
    is_nonconcurrent boolean,
    is_update_data boolean,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL,
    requests_recovery boolean,
    job_data bytea
);

CREATE TABLE qrtz_locks (
    lock_name character varying(40) NOT NULL,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

INSERT INTO QRTZ_LOCKS VALUES('trigger_access');
INSERT INTO QRTZ_LOCKS VALUES('job_access');
INSERT INTO QRTZ_LOCKS VALUES('calendar_access');
INSERT INTO QRTZ_LOCKS VALUES('state_access');
INSERT INTO QRTZ_LOCKS VALUES('misfire_access');

CREATE TABLE qrtz_paused_trigger_grps (
    trigger_group character varying(80) NOT NULL,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_scheduler_state (
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint,
    checkin_interval bigint,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_simple_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);

CREATE TABLE qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);

CREATE TABLE qrtz_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    job_name character varying(80) NOT NULL,
    job_group character varying(80) NOT NULL,
    description character varying(120),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(80),
    misfire_instr smallint,
    job_data bytea,
    sched_name character varying(120) DEFAULT 'TestScheduler'::character varying NOT NULL
);


ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);

ALTER TABLE ONLY qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);

ALTER TABLE ONLY qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);

ALTER TABLE ONLY qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);

ALTER TABLE ONLY qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);

CREATE INDEX fki_qrtz_simple_triggers_job_details_name_group ON qrtz_triggers USING btree (job_name, job_group);

CREATE INDEX fki_qrtz_simple_triggers_qrtz_triggers ON qrtz_simple_triggers USING btree (trigger_name, trigger_group);

CREATE INDEX idx_qrtz_ft_j_g ON qrtz_fired_triggers USING btree (sched_name, job_name, job_group);

CREATE INDEX idx_qrtz_ft_jg ON qrtz_fired_triggers USING btree (sched_name, job_group);

CREATE INDEX idx_qrtz_ft_t_g ON qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);

CREATE INDEX idx_qrtz_ft_tg ON qrtz_fired_triggers USING btree (sched_name, trigger_group);

CREATE INDEX idx_qrtz_ft_trig_inst_name ON qrtz_fired_triggers USING btree (sched_name, instance_name);

CREATE INDEX idx_qrtz_j_grp ON qrtz_job_details USING btree (sched_name, job_group);

CREATE INDEX idx_qrtz_t_c ON qrtz_triggers USING btree (sched_name, calendar_name);

CREATE INDEX idx_qrtz_t_g ON qrtz_triggers USING btree (sched_name, trigger_group);

CREATE INDEX idx_qrtz_t_j ON qrtz_triggers USING btree (sched_name, job_name, job_group);

CREATE INDEX idx_qrtz_t_jg ON qrtz_triggers USING btree (sched_name, job_group);

CREATE INDEX idx_qrtz_t_n_g_state ON qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_n_state ON qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_next_fire_time ON qrtz_triggers USING btree (sched_name, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st ON qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);

CREATE INDEX idx_qrtz_t_nft_st_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

CREATE INDEX idx_qrtz_t_state ON qrtz_triggers USING btree (sched_name, trigger_state);

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details(sched_name, job_name, job_group);
*/