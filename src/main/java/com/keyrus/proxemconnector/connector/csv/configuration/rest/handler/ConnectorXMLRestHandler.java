package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorXMLDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML;
import com.keyrus.proxemconnector.connector.csv.configuration.service.xml.ConnectorXMLService;
import io.vavr.control.Either;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ConnectorXMLRestHandler {

    private static ConnectorXMLRestHandler instance = null;

    public static ConnectorXMLRestHandler instance(
            final ConnectorXMLService connectorXMLService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorXMLRestHandler(
                            connectorXMLService,
                            errorHeader,
                            messageSource
                    );
        return instance;
    }

    private final ConnectorXMLService connectorXMLService;
    private final String errorHeader;
    private final MessageSource messageSource;

    private ConnectorXMLRestHandler(
            final ConnectorXMLService connectorXMLService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        this.connectorXMLService = connectorXMLService;
        this.errorHeader = errorHeader;
        this.messageSource = messageSource;
    }

    public ResponseEntity<ConnectorXMLDTO> create(
            final ConnectorXMLDTO connectorXMLDTO,
            final String languageCode
    ) {
        return
                ConnectorXMLRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorXMLService,
                        connectorXMLDTO,
                        ConnectorXMLService::create,
                        languageCode,
                        this.errorHeader,
                        this.messageSource
                );
    }





    public ResponseEntity<ConnectorXMLDTO> update(
            final ConnectorXMLDTO connectorXMLDTO,
            final String languageCode
    ) {
        return
                ConnectorXMLRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorXMLService,
                        connectorXMLDTO,
                        ConnectorXMLService::update,
                        languageCode,
                        this.errorHeader,
                        this.messageSource
                );
    }

    public ResponseEntity<ConnectorXMLDTO> delete(
            final String id,
            final String languageCode
    ) {
        return
                this.connectorXMLService
                        .delete(id)
                        .mapLeft(serviceError ->
                                ConnectorXMLRestHandler.<ConnectorXMLDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorXMLRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }
    public ResponseEntity<Collection<ConnectorXMLDTO>> findAll(final String languageCode) {
        return this.connectorXMLService
                .findAll()
                .mapLeft(serviceError ->
                        ConnectorXMLRestHandler.<Collection<ConnectorXMLDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorXMLRestHandler::toOkResponseForManyDTO)
                .fold(Function.identity(),
                        Function.identity());
    }
    public ResponseEntity<ConnectorXMLDTO> findOneByName(final String name, String languageCode) {
        return
                this.connectorXMLService
                        .findOneByName(name)
                        .mapLeft(serviceError ->
                                ConnectorXMLRestHandler.<ConnectorXMLDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorXMLRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<ConnectorXMLDTO> findOneById(final String id, String languageCode) {
        return
                this.connectorXMLService
                        .findOneById(id)
                        .mapLeft(serviceError ->
                                ConnectorXMLRestHandler.<ConnectorXMLDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorXMLRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<Collection<ConnectorXMLDTO>> findManyByNameContainsIgnoreCase(final String name, final String languageCode) {
        return this.connectorXMLService
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(serviceError ->
                        ConnectorXMLRestHandler.<Collection<ConnectorXMLDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorXMLRestHandler::toOkResponseForManyDTO)
                .fold(Function.identity(),
                        Function.identity());
    }

    private static ResponseEntity<Collection<ConnectorXMLDTO>> toOkResponseForManyDTO(
            final Collection<ConnectorXML> connectorXMLS
    ) {
        return
                ResponseEntity.ok(

                        connectorXMLS.stream().map(
                                ConnectorXMLDTO::new
                        ).toList()

                );


    }

    private static ResponseEntity<ConnectorXMLDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
            final ConnectorXMLService connectorXMLService,
            final ConnectorXMLDTO connectorXMLDTO,
            final BiFunction<ConnectorXMLService, ConnectorXML, Either<ConnectorXMLService.Error, ConnectorXML>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                ConnectorXMLRestHandler.<ConnectorXMLDTO>configurationDTOToConfiguration(
                                connectorXMLDTO,
                                languageCode,
                                errorHeader,
                                messageSource
                        )
                        .flatMap(
                                ConnectorXMLRestHandler.executeOnService(
                                        connectorXMLService,
                                        serviceOperation,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                )
                        )
                        .map(ConnectorXMLRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }


    private static Function<ConnectorXML, Either<ResponseEntity<ConnectorXMLDTO>, ConnectorXML>> executeOnService(
            final ConnectorXMLService connectorXMLService,
            final BiFunction<ConnectorXMLService, ConnectorXML, Either<ConnectorXMLService.Error, ConnectorXML>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                configuration ->
                        serviceOperation.apply(
                                        connectorXMLService,
                                        configuration
                                )
                                .mapLeft(serviceError ->
                                        ConnectorXMLRestHandler.serviceErrorToRestResponse(
                                                serviceError,
                                                languageCode,
                                                errorHeader,
                                                messageSource
                                        )
                                );
    }


    private static <RESPONSE> Either<ResponseEntity<RESPONSE>, ConnectorXML> configurationDTOToConfiguration(
            final ConnectorXMLDTO connectorXMLDTO,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                connectorXMLDTO.toConfiguration()
                        .mapLeft(configurationErrors ->
                                ConnectorXMLRestHandler.configurationErrorsToRestResponse(
                                        configurationErrors,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                )
                        );
    }

    private static ResponseEntity<ConnectorXMLDTO> toOkResponse(
            final ConnectorXML connectorXML
    ) {
        return
                ResponseEntity.ok(
                        new ConnectorXMLDTO(
                                connectorXML
                        )
                );
    }

    private static <RESPONSE> ResponseEntity<RESPONSE> serviceErrorToRestResponse(
            final ConnectorXMLService.Error serviceError,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                serviceError instanceof ConnectorXMLService.Error.IO
                        ?
                        ResponseEntity
                                .internalServerError()
                                .build()
                        :
                        ResponseEntity
                                .badRequest()
                                .header(
                                        errorHeader,
                                        ConnectorXMLRestHandler.i18nMessageOrCode(
                                                serviceError.message(),
                                                languageCode,
                                                messageSource
                                        )
                                )
                                .build();
    }

    private static <RESPONSE> ResponseEntity<RESPONSE> configurationErrorsToRestResponse(
            final Collection<Connector.Error> configurationErrors,
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
            final Collection<Connector.Error> configurationErrors,
            final String languageCode,
            final MessageSource messageSource
    ) {
        return
                configurationErrors.stream()
                        .map(error ->
                                ConnectorXMLRestHandler.configurationErrorToRestHeader(
                                        error,
                                        languageCode,
                                        messageSource
                                )
                        )
                        .toArray(String[]::new);
    }

    private static String configurationErrorToRestHeader(
            final Connector.Error configurationError,
            final String languageCode,
            final MessageSource messageSource
    ) {
        return
                ConnectorXMLRestHandler.i18nMessageOrCode(
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

