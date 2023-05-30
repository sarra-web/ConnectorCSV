CREATE TABLE IF NOT EXISTS connector (
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    separator VARCHAR(255) NOT NULL CHECK(separator <> ''),
    encoding VARCHAR(255) NOT NULL CHECK(encoding <> ''),
    folder_to_scan VARCHAR(255) NOT NULL CHECK(folder_to_scan <> ''),
    archive_folder VARCHAR(255) NOT NULL CHECK(archive_folder <> ''),
    failed_records_folder VARCHAR(255) NOT NULL CHECK(failed_records_folder <> ''),
    contains_headers BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS field (
    id VARCHAR(255) NOT NULL CHECK(name <> ''),
    reference_connector VARCHAR(255) NOT NULL CHECK(name <> ''),
    position INTEGER NOT NULL CHECK(position > 0),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    meta VARCHAR(255) NOT NULL CHECK(name <> ''),
    part_of_document_identity BOOLEAN NOT NULL,
    can_be_null_or_empty BOOLEAN NOT NULL,
    field_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reference_connector FOREIGN KEY (reference_connector) REFERENCES connector(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS project (
    id VARCHAR(255) NOT NULL CHECK(id <> ''),
    name VARCHAR(255) NOT NULL CHECK(name <> ''),
    proxem_token VARCHAR(255) NOT NULL CHECK(name <> ''),
    PRIMARY KEY (id)
);