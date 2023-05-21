package com.keyrus.proxemconnector.connector.csv.configuration.model;

import java.util.Collection;
import java.util.UUID;


public class Project {

    private UUID idProject;
    private  String projectName;

    private Collection<User> users;

    Collection<Connector> connectors;

    public Project(UUID i, String name) {
        this.idProject=i;this.projectName=name;
    }
}
