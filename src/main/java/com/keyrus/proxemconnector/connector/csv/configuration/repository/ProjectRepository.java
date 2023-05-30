package com.keyrus.proxemconnector.connector.csv.configuration.repository;


import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;

import java.util.Collection;

public interface ProjectRepository {

    Either<ProjectRepository.Error, Project> create(final Project Project);

    Either<ProjectRepository.Error, Project> update(final Project Project);

    Either<ProjectRepository.Error, Project> delete(final String id);
    Either<ProjectRepository.Error, Collection<Project>>  findAll();

    sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        record IO(
                String message
        ) implements ProjectRepository.Error {
        }

        record AlreadyExist() implements ProjectRepository.Error {
        }

        record NotFound() implements ProjectRepository.Error {
        }
    }
}
