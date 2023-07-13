package com.keyrus.proxemconnector.connector.csv.configuration.repository.project;


import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ProjectRepository {

    Either<ProjectRepository.Error, Project> create(final Project project);

    Either<ProjectRepository.Error, Project> update(final Project project);

    Either<ProjectRepository.Error, Project> delete(final String id);
    Either<ProjectRepository.Error, Collection<Project>>  findAll();
    Page<ProjectDAO> findAll(Pageable p);
    Either<ProjectRepository.Error, Project> findOneByName(String name);
    Page<ProjectDAO> findByNameContaining(String name, Pageable page);

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
