CREATE TABLE IF NOT EXISTS connector (
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
CREATE TABLE IF NOT EXISTS project (
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    proxem_token VARCHAR(255) NOT NULL CHECK(proxem_token <> ''),
    PRIMARY KEY (id)
);
