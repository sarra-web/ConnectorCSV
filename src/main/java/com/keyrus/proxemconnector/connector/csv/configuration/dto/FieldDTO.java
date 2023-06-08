package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;


public record FieldDTO(
        @JsonProperty("id")
        String id,
        @JsonProperty("name")
        String name,
        @JsonProperty("position")
        int position,
        @JsonProperty("field_type")
        String field_type,
        @JsonProperty("meta")
        String meta,
        @JsonProperty("partOfDocumentIdentity")
        boolean partOfDocumentIdentity,
        @JsonProperty("canBeNullOrEmpty")
        boolean canBeNullOrEmpty

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
                field.canBeNullOrEmpty()
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
