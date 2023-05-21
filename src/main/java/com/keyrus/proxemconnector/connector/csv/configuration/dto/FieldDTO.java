package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.field_type;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public record FieldDTO(
        String id,
        String name,
        int position, field_type field_type,
        String meta,
        boolean partOfDocumentIdentity,
        boolean canBeNullOrEmpty,

         field_type Type
) {
    public FieldDTO(
            final Field field
    ) {
        this(
                field.id(),
                field.name(),
                field.position(),field.type(),
                field.meta(),
                field.partOfDocumentIdentity(),
                field.canBeNullOrEmpty(),field.type()
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
                        this.partOfDocumentIdentity,
                        this.canBeNullOrEmpty,this.field_type
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
