package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.Type;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "field")
public final class FieldDAO implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private String id;
    @Column(name = "reference_connector", nullable = false, unique = false, insertable = true, updatable = true)
    private String referenceConnector;
    @Column(name = "name", nullable = false, unique = false, insertable = true, updatable = true)
    private String name;
    @Column(name = "position", nullable = false, unique = false, insertable = true, updatable = true)
    private int position;
    @Column(name = "meta", nullable = false, unique = false, insertable = true, updatable = true)
    private String meta;
    @Column(name = "part_of_document_identity", nullable = false, unique = false, insertable = true, updatable = true)
    private boolean partOfDocumentIdentity;
    @Column(name = "can_be_null_or_empty", nullable = false, unique = false, insertable = true, updatable = true)
    private boolean canBeNullOrEmpty;
    @Column(name = "type", nullable = false, unique = false, insertable = true, updatable = true)
    private Type type;


    public FieldDAO() {
    }

    public FieldDAO(
            String id,
            String referenceConnector,
            String name,
            int position,
            String meta,
            boolean partOfDocumentIdentity,
            boolean canBeNullOrEmpty
    ) {
        this.id = id;
        this.referenceConnector = referenceConnector;
        this.name = name;
        this.position = position;
        this.meta = meta;
        this.partOfDocumentIdentity = partOfDocumentIdentity;
        this.canBeNullOrEmpty = canBeNullOrEmpty;
    }

    public FieldDAO(
            final Field field
    ) {
        this(
                field.id(),
                field.referenceConnector(),
                field.name(),
                field.position(),
                field.meta(),
                field.partOfDocumentIdentity(),
                field.canBeNullOrEmpty()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceConnector() {
        return referenceConnector;
    }

    public void setReferenceConnector(String referenceConnector) {
        this.referenceConnector = referenceConnector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean isPartOfDocumentIdentity() {
        return partOfDocumentIdentity;
    }

    public void setPartOfDocumentIdentity(boolean partOfDocumentIdentity) {
        this.partOfDocumentIdentity = partOfDocumentIdentity;
    }

    public boolean isCanBeNullOrEmpty() {
        return canBeNullOrEmpty;
    }

    public void setCanBeNullOrEmpty(boolean canBeNullOrEmpty) {
        this.canBeNullOrEmpty = canBeNullOrEmpty;
    }

    public final Either<Collection<Field.Error>, Field> toHeader() {
        return
                Field.of(
                        this.id,
                        this.referenceConnector,
                        this.name,
                        this.position,
                        this.meta,
                        this.partOfDocumentIdentity,
                        this.canBeNullOrEmpty,this.type
                );
    }
}