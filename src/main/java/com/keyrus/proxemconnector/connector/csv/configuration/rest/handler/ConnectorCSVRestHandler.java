package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import io.vavr.control.Either;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ConnectorCSVRestHandler {

    private static ConnectorCSVRestHandler instance = null;

    public static ConnectorCSVRestHandler instance(
            final ConnectorCSVService connectorCSVService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorCSVRestHandler(
                            connectorCSVService,
                            errorHeader,
                            messageSource
                    );
        return instance;
    }

    private final ConnectorCSVService connectorCSVService;
    private final String errorHeader;
    private final MessageSource messageSource;

    private ConnectorCSVRestHandler(
            final ConnectorCSVService connectorCSVService,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        this.connectorCSVService = connectorCSVService;
        this.errorHeader = errorHeader;
        this.messageSource = messageSource;
    }

    public ResponseEntity<ConnectorCSVDTO> create(
            final ConnectorCSVDTO connectorCSVDTO,
            final String languageCode
    ) {
        return
                ConnectorCSVRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorCSVService,
                        connectorCSVDTO,
                        ConnectorCSVService::create,
                        languageCode,
                        this.errorHeader,
                        this.messageSource
                );
    }

    public ResponseEntity<ConnectorCSVDTO> create2(String idProject,
            final ConnectorCSVDTO connectorCSVDTO,
            final String languageCode
    ) {
        return
                ConnectorCSVRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceCreateOperation(
                        this.connectorCSVService,
                        connectorCSVDTO
                        ,idProject,
                        ConnectorCSVService::create2,
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
                ConnectorCSVRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                        this.connectorCSVService,
                        connectorCSVDTO,
                        ConnectorCSVService::update,
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
                this.connectorCSVService
                        .delete(id)
                        .mapLeft(serviceError ->
                                ConnectorCSVRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorCSVRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }
    public ResponseEntity<Collection<ConnectorCSVDTO>> findAll(final String languageCode) {
        return this.connectorCSVService
                .findAll()
                .mapLeft(serviceError ->
                        ConnectorCSVRestHandler.<Collection<ConnectorCSVDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorCSVRestHandler::toOkResponseForManyDTO)
                .fold(Function.identity(),
                        Function.identity());
    }
    public ResponseEntity<ConnectorCSVDTO> findOneByName(final String name, String languageCode) {
        return
                this.connectorCSVService
                        .findOneByName(name)
                        .mapLeft(serviceError ->
                                ConnectorCSVRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorCSVRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<ConnectorCSVDTO> findOneById(final String id, String languageCode) {
        return
                this.connectorCSVService
                        .findOneById(id)
                        .mapLeft(serviceError ->
                                ConnectorCSVRestHandler.<ConnectorCSVDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ConnectorCSVRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }
    public ResponseEntity<Collection<ConnectorCSVDTO>> findManyByNameContainsIgnoreCase(final String name, final String languageCode) {
        return this.connectorCSVService
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(serviceError ->
                        ConnectorCSVRestHandler.<Collection<ConnectorCSVDTO>>serviceErrorToRestResponse(
                                serviceError,
                                languageCode,
                                this.errorHeader,
                                this.messageSource
                        )
                )
                .map(ConnectorCSVRestHandler::toOkResponseForManyDTO)
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
            final ConnectorCSVService connectorCSVService,
            final ConnectorCSVDTO connectorCSVDTO,
            final BiFunction<ConnectorCSVService, ConnectorCSV, Either<ConnectorCSVService.Error, ConnectorCSV>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                ConnectorCSVRestHandler.<ConnectorCSVDTO>configurationDTOToConfiguration(
                                connectorCSVDTO,
                                languageCode,
                                errorHeader,
                                messageSource
                        )
                        .flatMap(
                                ConnectorCSVRestHandler.executeOnService(
                                        connectorCSVService,
                                        serviceOperation,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                )
                        )
                        .map(ConnectorCSVRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }
    private static ResponseEntity<ConnectorCSVDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceCreateOperation(
            final ConnectorCSVService connectorCSVService,
            final ConnectorCSVDTO connectorCSVDTO
            ,final String idProject,
            final  TriFunction<ConnectorCSVService, ConnectorCSV,String, Either<ConnectorCSVService.Error, ConnectorCSV>>  serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                ConnectorCSVRestHandler.<ConnectorCSVDTO>configurationDTOToConfiguration(
                                connectorCSVDTO,
                                languageCode,
                                errorHeader,
                                messageSource
                        )
                        .flatMap(
                                ConnectorCSVRestHandler.executeOnCreateService(
                                        connectorCSVService,
                                        serviceOperation,
                                        languageCode,
                                        errorHeader,
                                        messageSource
                                        ,idProject
                                )
                        )
                        .map(ConnectorCSVRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );
    }

    private static Function<ConnectorCSV, Either<ResponseEntity<ConnectorCSVDTO>, ConnectorCSV>> executeOnService(
            final ConnectorCSVService connectorCSVService,
            final BiFunction<ConnectorCSVService, ConnectorCSV, Either<ConnectorCSVService.Error, ConnectorCSV>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                configuration ->
                        serviceOperation.apply(
                                        connectorCSVService,
                                        configuration
                                )
                                .mapLeft(serviceError ->
                                        ConnectorCSVRestHandler.serviceErrorToRestResponse(
                                                serviceError,
                                                languageCode,
                                                errorHeader,
                                                messageSource
                                        )
                                );
    }
    private static Function<ConnectorCSV, Either<ResponseEntity<ConnectorCSVDTO>, ConnectorCSV>> executeOnCreateService(
            final ConnectorCSVService connectorCSVService,
            final TriFunction<ConnectorCSVService, ConnectorCSV,String, Either<ConnectorCSVService.Error, ConnectorCSV>> serviceOperation,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
            ,final String idProject
    ) {
        return
                configuration->
                        serviceOperation.apply(
                                        connectorCSVService
                                        ,configuration,idProject
                                )
                                .mapLeft(serviceError ->
                                        ConnectorCSVRestHandler.serviceErrorToRestResponse(
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
                                ConnectorCSVRestHandler.configurationErrorsToRestResponse(
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
            final ConnectorCSVService.Error serviceError,
            final String languageCode,
            final String errorHeader,
            final MessageSource messageSource
    ) {
        return
                serviceError instanceof ConnectorCSVService.Error.IO
                        ?
                        ResponseEntity
                                .internalServerError()
                                .build()
                        :
                        ResponseEntity
                                .badRequest()
                                .header(
                                        errorHeader,
                                        ConnectorCSVRestHandler.i18nMessageOrCode(
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
                                ConnectorCSVRestHandler.configurationErrorToRestHeader(
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
                ConnectorCSVRestHandler.i18nMessageOrCode(
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
@FunctionalInterface
 interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);

    default <K> TriFunction<T, U, V, K> andThen(Function<? super R, ? extends K> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
