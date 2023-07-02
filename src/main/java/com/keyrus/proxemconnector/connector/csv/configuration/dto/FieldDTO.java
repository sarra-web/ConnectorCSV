package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;


public record FieldDTO(
        String id,
        String name,
        int position,
        FieldType fieldType,
        String meta,
        boolean included,
        boolean partOfDocumentIdentity


) {
    public FieldDTO(
            final Field field
    ) {
        this(
                field.id(),
                field.name(),
                field.position()
                ,field.field_type(),
                field.meta(),
                field.isIncluded(),
                field.partOfDocumentIdentity()
        );
    }

    public Either<Collection<Field.Error>, Field> toHeader(
            final String referenceConnector
    ) {
        return
                Field.of(
                        FieldDTO.idNonNullOrRandomId(this.id),
                        referenceConnector,
                        this.name,
                        this.position,
                        this.meta,
                        this.fieldType,
                        this.partOfDocumentIdentity,this.included
                );
    }

    private static String idNonNullOrRandomId(
            final String id
    ) {
        return
                Objects.nonNull(id)
                        ? id
                        : UUID.randomUUID().toString();
    }
}
