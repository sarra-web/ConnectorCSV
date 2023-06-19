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
    separator VARCHAR(255) NOT NULL CHECK(separator <> ''),
    encoding VARCHAR(255) NOT NULL CHECK(encoding <> ''),
    path VARCHAR(255) NOT NULL CHECK(path <> ''),
    quoting_caracter VARCHAR(255) NOT NULL CHECK(quoting_caracter <> ''),
    escaping_caracter VARCHAR(255) NOT NULL CHECK(escaping_caracter <> ''),
    contains_headers BOOLEAN NOT NULL,
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
    scan_type VARCHAR(255),
    starts_time VARCHAR(255),
    execution_time VARCHAR(255),
    PRIMARY KEY (id),
    connector_id VARCHAR(255)  NOT NULL REFERENCES connector(id)
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

