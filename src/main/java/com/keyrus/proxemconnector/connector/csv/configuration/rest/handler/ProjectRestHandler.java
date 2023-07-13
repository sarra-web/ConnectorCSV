package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ProjectService;
import io.vavr.control.Either;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ProjectRestHandler {

        private static ProjectRestHandler instance = null;

        public static ProjectRestHandler instance(
                final ProjectService projectService,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            if (Objects.isNull(instance))
                instance =
                        new ProjectRestHandler(
                                projectService,
                                errorHeader,
                                messageSource
                        );
            return instance;
        }

        private final ProjectService projectService;
        private final String errorHeader;
        private final MessageSource messageSource;

        private ProjectRestHandler(
                final ProjectService projectService,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            this.projectService = projectService;
            this.errorHeader = errorHeader;
            this.messageSource = messageSource;
        }

        public ResponseEntity<ProjectDTO> create(
                final ProjectDTO projectDTO,
                final String languageCode
        ) {
            return
                    ProjectRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                            this.projectService,
                            projectDTO,
                            ProjectService::create,
                            languageCode,
                            this.errorHeader,
                            this.messageSource
                    );
        }

        public ResponseEntity<ProjectDTO> update(
                final ProjectDTO projectDTO,
                final String languageCode
        ) {
            return
                    ProjectRestHandler.convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                            this.projectService,
                            projectDTO,
                            ProjectService::update,
                            languageCode,
                            this.errorHeader,
                            this.messageSource
                    );
        }

        public ResponseEntity<ProjectDTO> delete(
                final String id,
                final String languageCode
        ) {
            return
                    this.projectService
                            .delete(id)
                            .mapLeft(serviceError ->
                                    ProjectRestHandler.<ProjectDTO>serviceErrorToRestResponse(
                                            serviceError,
                                            languageCode,
                                            this.errorHeader,
                                            this.messageSource
                                    )
                            )
                            .map(ProjectRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );
        }
        public ResponseEntity<Collection<ProjectDTO>> findAll(final String languageCode) {
            return this.projectService
                    .findAll()
                    .mapLeft(serviceError ->
                            ProjectRestHandler.<Collection<ProjectDTO>>serviceErrorToRestResponse(
                                    serviceError,
                                    languageCode,
                                    this.errorHeader,
                                    this.messageSource
                            )
                    )
                    .map(ProjectRestHandler::toOkResponseForManyDTO)
                    .fold(Function.identity(),
                            Function.identity());
        }



    public ResponseEntity<ProjectDTO> findOneByName(final String name, String languageCode) {
        return
                this.projectService
                        .findOneByName(name)
                        .mapLeft(serviceError ->
                                ProjectRestHandler.<ProjectDTO>serviceErrorToRestResponse(
                                        serviceError,
                                        languageCode,
                                        this.errorHeader,
                                        this.messageSource
                                )
                        )
                        .map(ProjectRestHandler::toOkResponse)
                        .fold(
                                Function.identity(),
                                Function.identity()
                        );

    }


        private static ResponseEntity<Collection<ProjectDTO>> toOkResponseForManyDTO(
                final Collection<Project> projects
        ) {
            return
                    ResponseEntity.ok(

                            projects.stream().map(
                                    ProjectDTO::new
                            ).toList()

                    );


        }

        private static ResponseEntity<ProjectDTO> convertConfigurationDTOToConfigurationThenApplyOnServiceOperation(
                final ProjectService projectService,
                final ProjectDTO projectDTO,
                final BiFunction<ProjectService, Project, Either<ProjectService.Error, Project>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    ProjectRestHandler.<ProjectDTO>configurationDTOToConfiguration(
                                    projectDTO,
                                    languageCode,
                                    errorHeader,
                                    messageSource
                            )
                            .flatMap(
                                    ProjectRestHandler.executeOnService(
                                            projectService,
                                            serviceOperation,
                                            languageCode,
                                            errorHeader,
                                            messageSource
                                    )
                            )
                            .map(ProjectRestHandler::toOkResponse)
                            .fold(
                                    Function.identity(),
                                    Function.identity()
                            );
        }

        private static Function<Project, Either<ResponseEntity<ProjectDTO>, Project>> executeOnService(
                final ProjectService projectService,
                final BiFunction<ProjectService, Project, Either<ProjectService.Error, Project>> serviceOperation,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    project ->
                            serviceOperation.apply(
                                            projectService,
                                            project
                                    )
                                    .mapLeft(serviceError ->
                                            ProjectRestHandler.serviceErrorToRestResponse(
                                                    serviceError,
                                                    languageCode,
                                                    errorHeader,
                                                    messageSource
                                            )
                                    );
        }

        private static <RESPONSE> Either<ResponseEntity<RESPONSE>, Project> configurationDTOToConfiguration(
                final ProjectDTO projectDTO,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    projectDTO.toProject()
                            .mapLeft(configurationErrors ->
                                    ProjectRestHandler.configurationErrorsToRestResponse(
                                            configurationErrors,
                                            languageCode,
                                            errorHeader,
                                            messageSource
                                    )
                            );
        }

        private static ResponseEntity<ProjectDTO> toOkResponse(
                final Project project
        ) {
            return
                    ResponseEntity.ok(
                            new ProjectDTO(
                                    project
                            )
                    );
        }

        private static <RESPONSE> ResponseEntity<RESPONSE> serviceErrorToRestResponse(
                final ProjectService.Error serviceError,
                final String languageCode,
                final String errorHeader,
                final MessageSource messageSource
        ) {
            return
                    serviceError instanceof ProjectService.Error.IO
                            ?
                            ResponseEntity
                                    .internalServerError()
                                    .build()
                            :
                            ResponseEntity
                                    .badRequest()
                                    .header(
                                            errorHeader,
                                            ProjectRestHandler.i18nMessageOrCode(
                                                    serviceError.message(),
                                                    languageCode,
                                                    messageSource
                                            )
                                    )
                                    .build();
        }

        private static <RESPONSE> ResponseEntity<RESPONSE> configurationErrorsToRestResponse(
                final Collection<Project.Error> configurationErrors,
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
                final Collection<Project.Error> configurationErrors,
                final String languageCode,
                final MessageSource messageSource
        ) {
            return
                    configurationErrors.stream()
                            .map(error ->
                                    ProjectRestHandler.configurationErrorToRestHeader(
                                            error,
                                            languageCode,
                                            messageSource
                                    )
                            )
                            .toArray(String[]::new);
        }

        private static String configurationErrorToRestHeader(
                final Project.Error configurationError,
                final String languageCode,
                final MessageSource messageSource
        ) {
            return
                    ProjectRestHandler.i18nMessageOrCode(
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


