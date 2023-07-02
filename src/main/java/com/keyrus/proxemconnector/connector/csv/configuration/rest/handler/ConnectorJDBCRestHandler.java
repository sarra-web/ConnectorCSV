package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorJDBCDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService;
import io.vavr.control.Either;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConnectorJDBCRestHandler {
    private static ConnectorJDBCRestHandler instance = null;

        public static ConnectorJDBCRestHandler instance(
                final ConnectorJDBCService connectorJDBCService,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            if (Objects.isNull(instance))
                instance =
                        new ConnectorJDBCRestHandler(
                                connectorJDBCService,
                                errorHeader,
                                messageSource
                        );
            return instance;
        }

        private final ConnectorJDBCService connectorJDBCService;
        private final String errorHeader;
        private final MessageSource messageSource;

        private ConnectorJDBCRestHandler(
                final ConnectorJDBCService connectorJDBCService,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            this.connectorJDBCService = connectorJDBCService;
            this.errorHeader = errorHeader;
            this.messageSource = messageSource;
        }

        public ResponseEntity<ConnectorJDBCDTO> create(
                final ConnectorJDBCDTO connectorJDBCDTO,
                final String languageCode
        ) {
            return
                    ConnectorJDBCRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                            this.connectorJDBCService,
                            connectorJDBCDTO,
                            ConnectorJDBCService::create,
                            languageCode,
                            this.errorHeader,
                            this.messageSource
                    );
        }

        public ResponseEntity<ConnectorJDBCDTO> create2(String idProject,
                                                       final ConnectorJDBCDTO connectorJDBCDTO,
                                                       final String languageCode
        ) {
            return
                    ConnectorJDBCRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceCreateOperation(
                            this.connectorJDBCService,
                            connectorJDBCDTO
                            ,idProject,
                            ConnectorJDBCService::create2,
                            languageCode,
                            this.errorHeader,
                            this.messageSource
                    );
        }



        public ResponseEntity<ConnectorJDBCDTO> update(
                final ConnectorJDBCDTO connectorJDBCDTO,
                final String languageCode
        ) {
            return
                    ConnectorJDBCRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                            this.connectorJDBCService,
                            connectorJDBCDTO,
                            ConnectorJDBCService::update,
                            languageCode,
                            this.errorHeader,
                            this.messageSource
                    );
        }

        public ResponseEntity<ConnectorJDBCDTO> delete(
                final String id,
                final String languageCode
        ) {
            return
                    this.connectorJDBCService
                            .delete(id)
                            .mapLeft(serviceError ->
                                    ConnectorJDBCRestHandler.<ConnectorJDBCDTO>serviceErrorToRestResponse(
                                            serviceError,
                                            languageCode,
                                            this.errorHeader,
                                            this.messageSource
                                    )
                            )
                            .map(ConnectorJDBCRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );
        }
        public ResponseEntity<Collection<ConnectorJDBCDTO>> findAll(final String languageCode) {
            return this.connectorJDBCService
                    .findAll()
                    .mapLeft(serviceError ->
                            ConnectorJDBCRestHandler.<Collection<ConnectorJDBCDTO>>serviceErrorToRestResponse(
                                    serviceError,
                                    languageCode,
                                    this.errorHeader,
                                    this.messageSource
                            )
                    )
                    .map(ConnectorJDBCRestHandler::toOkResponseForManyDTO)
                    .fold(Function.identity(),
                            Function.identity());
        }
        public ResponseEntity<ConnectorJDBCDTO> findOneByName(final String name, String languageCode) {
            return
                    this.connectorJDBCService
                            .findOneByName(name)
                            .mapLeft(serviceError ->
                                    ConnectorJDBCRestHandler.<ConnectorJDBCDTO>serviceErrorToRestResponse(
                                            serviceError,
                                            languageCode,
                                            this.errorHeader,
                                            this.messageSource
                                    )
                            )
                            .map(ConnectorJDBCRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );

        }
        public ResponseEntity<ConnectorJDBCDTO> findOneById(final String id, String languageCode) {
            return
                    this.connectorJDBCService
                            .findOneById(id)
                            .mapLeft(serviceError ->
                                    ConnectorJDBCRestHandler.<ConnectorJDBCDTO>serviceErrorToRestResponse(
                                            serviceError,
                                            languageCode,
                                            this.errorHeader,
                                            this.messageSource
                                    )
                            )
                            .map(ConnectorJDBCRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );

        }
        public ResponseEntity<Collection<ConnectorJDBCDTO>> findManyByNameContainsIgnoreCase(final String name, final String languageCode) {
            return this.connectorJDBCService
                    .findManyByNameContainsIgnoreCase(name)
                    .mapLeft(serviceError ->
                            ConnectorJDBCRestHandler.<Collection<ConnectorJDBCDTO>>serviceErrorToRestResponse(
                                    serviceError,
                                    languageCode,
                                    this.errorHeader,
                                    this.messageSource
                            )
                    )
                    .map(ConnectorJDBCRestHandler::toOkResponseForManyDTO)
                    .fold(Function.identity(),
                            Function.identity());
        }

        private static ResponseEntity<Collection<ConnectorJDBCDTO>> toOkResponseForManyDTO(
                final Collection<ConnectorJDBC> connectorJDBCS
        ) {
            return
                    ResponseEntity.ok(

                            connectorJDBCS.stream().map(
                                    ConnectorJDBCDTO::new
                            ).toList()

                    );


        }

        private static ResponseEntity<ConnectorJDBCDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                final ConnectorJDBCService connectorJDBCService,
                final ConnectorJDBCDTO connectorJDBCDTO,
                final BiFunction<ConnectorJDBCService, ConnectorJDBC, Either<ConnectorJDBCService.Error, ConnectorJDBC>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    ConnectorJDBCRestHandler.<ConnectorJDBCDTO>configurationDTOToConfiguration(
                                    connectorJDBCDTO,
                                    languageCode,
                                    errorHeader,
                                    messageSource
                            )
                            .flatMap(ConnectorJDBCRestHandler.executeOnService(
                                            connectorJDBCService,
                                            serviceOperation,
                                            languageCode,
                                            errorHeader,
                                            messageSource
                                    )
                            )
                            .map(ConnectorJDBCRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );
        }
        private static ResponseEntity<ConnectorJDBCDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceCreateOperation(
                final ConnectorJDBCService connectorJDBCService,
                final ConnectorJDBCDTO connectorJDBCDTO
                ,final String idProject,
                final com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.TriFunction<ConnectorJDBCService, ConnectorJDBC,String, Either<ConnectorJDBCService.Error, ConnectorJDBC>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    ConnectorJDBCRestHandler.<ConnectorJDBCDTO>configurationDTOToConfiguration(
                                    connectorJDBCDTO,
                                    languageCode,
                                    errorHeader,
                                    messageSource
                            )
                            .flatMap(ConnectorJDBCRestHandler.executeOnCreateService(
                                            connectorJDBCService,
                                            serviceOperation,
                                            languageCode,
                                            errorHeader,
                                            messageSource
                                            ,idProject
                                    )
                            )
                            .map(ConnectorJDBCRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );
        }

        private static Function<ConnectorJDBC, Either<ResponseEntity<ConnectorJDBCDTO>, ConnectorJDBC>> executeOnService(
                final ConnectorJDBCService connectorJDBCService,
                final BiFunction<ConnectorJDBCService, ConnectorJDBC, Either<ConnectorJDBCService.Error, ConnectorJDBC>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    configuration ->
                            serviceOperation.apply(
                                            connectorJDBCService,
                                            configuration
                                    )
                                    .mapLeft(serviceError ->
                                            ConnectorJDBCRestHandler.serviceErrorToRestResponse(
                                                    serviceError,
                                                    languageCode,
                                                    errorHeader,
                                                    messageSource
                                            )
                                    );
        }
        private static Function<ConnectorJDBC, Either<ResponseEntity<ConnectorJDBCDTO>, ConnectorJDBC>> executeOnCreateService(
                final ConnectorJDBCService connectorJDBCService,
                final com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.TriFunction<ConnectorJDBCService, ConnectorJDBC,String, Either<ConnectorJDBCService.Error, ConnectorJDBC>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
                ,final String idProject
        ) {
            return
                    configuration->
                            serviceOperation.apply(
                                            connectorJDBCService
                                            ,configuration,idProject
                                    )
                                    .mapLeft(serviceError ->
                                            ConnectorJDBCRestHandler.serviceErrorToRestResponse(
                                                    serviceError,
                                                    languageCode,
                                                    errorHeader,
                                                    messageSource
                                            )
                                    );
        }

        private static <RESPONSE> Either<ResponseEntity<RESPONSE>, ConnectorJDBC> configurationDTOToConfiguration(
                final ConnectorJDBCDTO connectorJDBCDTO,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    connectorJDBCDTO.toConfiguration()
                            .mapLeft(configurationErrors ->
                                    ConnectorJDBCRestHandler.configurationErrorsToRestResponse(
                                            configurationErrors,
                                            languageCode,
                                            errorHeader,
                                            messageSource
                                    )
                            );
        }

        private static ResponseEntity<ConnectorJDBCDTO> toOkResponse(
                final ConnectorJDBC connectorJDBC
        ) {
            return
                    ResponseEntity.ok(
                            new ConnectorJDBCDTO(
                                    connectorJDBC
                            )
                    );
        }

        private static <RESPONSE> ResponseEntity<RESPONSE> serviceErrorToRestResponse(
                final ConnectorJDBCService.Error serviceError,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    serviceError instanceof ConnectorJDBCService.Error.IO
                            ?
                            ResponseEntity
                                    .internalServerError()
                                    .build()
                            :
                            ResponseEntity
                                    .badRequest()
                                    .header(
                                            errorHeader,
                                    ConnectorJDBCRestHandler.i18nMessageOrCode(
                                                    serviceError.message(),
                                                    languageCode,
                                                    messageSource
                                            )
                                    )
                                    .build();
        }

        private static <RESPONSE> ResponseEntity<RESPONSE> configurationErrorsToRestResponse(
                final Collection<ConnectorJDBC.Error> configurationErrors,
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
                final Collection<ConnectorJDBC.Error> configurationErrors,
                final String languageCode,
                final MessageSource messageSource
        ) {
            return
                    configurationErrors.stream()
                            .map(error ->
                                    ConnectorJDBCRestHandler.configurationErrorToRestHeader(
                                            error,
                                            languageCode,
                                            messageSource
                                    )
                            )
                            .toArray(String[]::new);
        }

        private static String configurationErrorToRestHeader(
                final ConnectorJDBC.Error configurationError,
                final String languageCode,
                final MessageSource messageSource
        ) {
            return
                    ConnectorJDBCRestHandler.i18nMessageOrCode(
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



