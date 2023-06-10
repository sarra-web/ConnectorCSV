package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ConnectorService;
import io.vavr.control.Either;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ConnectorRestHandler {

    private static ConnectorRestHandler instance = null;

    public static ConnectorRestHandler instance(
            final ConnectorService connectorService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorRestHandler(
                            connectorService,
                            errorHeader,
                            messageSource
                    );
        return instance;
    }

    private final ConnectorService connectorService;
    private final String errorHeader;
    private final MessageSource messageSource;

    private ConnectorRestHandler(
            final ConnectorService connectorService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        this.connectorService = connectorService;
        this.errorHeader = errorHeader;
        this.messageSource = messageSource;
    }

    public ResponseEntity<ConnectorCSVDTO> create(
            final ConnectorCSVDTO connectorCSVDTO,
            final String languageCode
    ) {
        return
                ConnectorRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorService,
                        connectorCSVDTO,
                        ConnectorService::create,
                        languageCode,
                        this.errorHeader,
                        this.messageSource
                );
    }

    public ResponseEntity<ConnectorCSVDTO> update(
            final ConnectorCSVDTO connectorCSVDTO,
            final String languageCode
    ) {
        return
                ConnectorRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorService,
                        connectorCSVDTO,
                        ConnectorService::update,
                        languageCode,
                        this.errorHeader,
                        this.messageSource
                );
    }

    public ResponseEntity<ConnectorCSVDTO> delete(
            final String id,
            final String languageCode
    ) {
        return
                this.connectorService
                        .delete(id)
                        .mapLeft(serviceError ->
                                ConnectorRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }
    public ResponseEntity<Collection<ConnectorCSVDTO>> findAll(final String languageCode) {
        return this.connectorService
                .findAll()
                .mapLeft(serviceError ->
                        ConnectorRestHandler.<Collection<ConnectorCSVDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorRestHandler::toOkResponseForManyDTO)
                .fold(Function.identity(),
                        Function.identity());
    }
    public ResponseEntity<ConnectorCSVDTO> findOneByName(final String name, String languageCode) {
        return
                this.connectorService
                        .findOneByName(name)
                        .mapLeft(serviceError ->
                                ConnectorRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<ConnectorCSVDTO> findOneById(final String id, String languageCode) {
        return
                this.connectorService
                        .findOneById(id)
                        .mapLeft(serviceError ->
                                ConnectorRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<Collection<ConnectorCSVDTO>> findManyByNameContainsIgnoreCase(final String name, final String languageCode) {
        return this.connectorService
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(serviceError ->
                        ConnectorRestHandler.<Collection<ConnectorCSVDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorRestHandler::toOkResponseForManyDTO)
                .fold(Function.identity(),
                        Function.identity());
    }

    private static ResponseEntity<Collection<ConnectorCSVDTO>> toOkResponseForManyDTO(
            final Collection<ConnectorCSV> connectorCSVS
    ) {
        return
                ResponseEntity.ok(

                        connectorCSVS.stream().map(
                                ConnectorCSVDTO::new
                        ).toList()

                );


    }

    private static ResponseEntity<ConnectorCSVDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
            final ConnectorService connectorService,
            final ConnectorCSVDTO connectorCSVDTO,
            final BiFunction<ConnectorService, ConnectorCSV, Either<ConnectorService.Error, ConnectorCSV>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                ConnectorRestHandler.<ConnectorCSVDTO>configurationDTOToConfiguration(
                                connectorCSVDTO,
                                languageCode,
                                errorHeader,
                                messageSource
                        )
                        .flatMap(
                                ConnectorRestHandler.executeOnService(
                                        connectorService,
                                        serviceOperation,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                )
                        )
                        .map(ConnectorRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }

    private static Function<ConnectorCSV, Either<ResponseEntity<ConnectorCSVDTO>, ConnectorCSV>> executeOnService(
            final ConnectorService connectorService,
            final BiFunction<ConnectorService, ConnectorCSV, Either<ConnectorService.Error, ConnectorCSV>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                configuration ->
                        serviceOperation.apply(
                                        connectorService,
                                        configuration
                                )
                                .mapLeft(serviceError ->
                                        ConnectorRestHandler.serviceErrorToRestResponse(
                                                serviceError,
                                                languageCode,
                                                errorHeader,
                                                messageSource
                                        )
                                );
    }

    private static <RESPONSE> Either<ResponseEntity<RESPONSE>, ConnectorCSV> configurationDTOToConfiguration(
            final ConnectorCSVDTO connectorCSVDTO,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                connectorCSVDTO.toConfiguration()
                        .mapLeft(configurationErrors ->
                                ConnectorRestHandler.configurationErrorsToRestResponse(
                                        configurationErrors,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                )
                        );
    }

    private static ResponseEntity<ConnectorCSVDTO> toOkResponse(
            final ConnectorCSV connectorCSV
    ) {
        return
                ResponseEntity.ok(
                        new ConnectorCSVDTO(
                                connectorCSV
                        )
                );
    }

    private static <RESPONSE> ResponseEntity<RESPONSE> serviceErrorToRestResponse(
            final ConnectorService.Error serviceError,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                serviceError instanceof ConnectorService.Error.IO
                        ?
                        ResponseEntity
                                .internalServerError()
                                .build()
                        :
                        ResponseEntity
                                .badRequest()
                                .header(
                                        errorHeader,
                                        ConnectorRestHandler.i18nMessageOrCode(
                                                serviceError.message(),
                                                languageCode,
                                                messageSource
                                        )
                                )
                                .build();
    }

    private static <RESPONSE> ResponseEntity<RESPONSE> configurationErrorsToRestResponse(
            final Collection<ConnectorCSV.Error> configurationErrors,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                ResponseEntity
                        .badRequest()
                        .header(
                                errorHeader,
                                configurationErrorsToRestHeaders(
                                        configurationErrors,
                                        languageCode,
                                        messageSource
                                )
                        )
                        .build();
    }

    private static String[] configurationErrorsToRestHeaders(
            final Collection<ConnectorCSV.Error> configurationErrors,
            final String languageCode,
            final MessageSource messageSource
    ) {
        return
                configurationErrors.stream()
                        .map(error ->
                                ConnectorRestHandler.configurationErrorToRestHeader(
                                        error,
                                        languageCode,
                                        messageSource
                                )
                        )
                        .toArray(String[]::new);
    }

    private static String configurationErrorToRestHeader(
            final ConnectorCSV.Error configurationError,
            final String languageCode,
            final MessageSource messageSource
    ) {
        return
                ConnectorRestHandler.i18nMessageOrCode(
                        configurationError.message(),
                        languageCode,
                        messageSource
                );
    }

    private static String i18nMessageOrCode(
            final String code,
            final String languageCode,
            final MessageSource messageSource
    ) {
        try {
            return
                    messageSource.getMessage(
                            code,
                            null,
                            Objects.nonNull(languageCode)
                                    ? new Locale(languageCode)
                                    : Locale.getDefault()
                    );
        } catch (Exception exception) {
            return code;
        }
    }
}
