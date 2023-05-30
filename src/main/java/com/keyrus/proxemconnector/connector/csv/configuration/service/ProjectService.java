package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ProjectRepository;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;

public final class ProjectService {


    private static ProjectService instance = null;

        public static ProjectService instance(
                final ProjectRepository projectRepository
        ) {
            if (Objects.isNull(instance))
                instance =
                        new ProjectService(
                                projectRepository
                        );
            return instance;
        }

        private final ProjectRepository projectRepository;

        private ProjectService(
                final ProjectRepository projectRepository
        ) {
            this.projectRepository = projectRepository;
        }

        public Either<ProjectService.Error, Project> create(
                final Project project
        ) {
            return
                    this.projectRepository
                            .create(
                                    project
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<ProjectService.Error, Project> update(
                final Project project
        ) {
            return
                    this.projectRepository
                            .update(
                                    project
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<ProjectService.Error, Project> delete(
                final String id
        ) {
            return
                    this.projectRepository
                            .delete(
                                    id
                            )
                            .mapLeft(ProjectService::repositoryErrorToServiceError);
        }

        public Either<ProjectService.Error, Collection<Project>> findAll() {
            return this.projectRepository
                    .findAll()
                    .mapLeft(ProjectService::repositoryErrorToServiceError);

        }

        private static ProjectService.Error repositoryErrorToServiceError(
                final ProjectRepository.Error repositoryError
        ) {
            if (repositoryError instanceof ProjectRepository.Error.IO io)
                return new ProjectService.Error.IO(io.message());
            if (repositoryError instanceof ProjectRepository.Error.AlreadyExist)
                return new ProjectService.Error.AlreadyExist();
            if (repositoryError instanceof ProjectRepository.Error.NotFound)
                return new ProjectService.Error.NotFound();
            throw new IllegalStateException("repository error not mapped to service error");
        }

        public sealed interface Error {

            default String message() {
                return this.getClass().getCanonicalName();
            }

            record IO(
                    String message
            ) implements ProjectService.Error {
            }

            record AlreadyExist() implements ProjectService.Error {
            }

            record NotFound() implements ProjectService.Error {
            }
        }
    }



