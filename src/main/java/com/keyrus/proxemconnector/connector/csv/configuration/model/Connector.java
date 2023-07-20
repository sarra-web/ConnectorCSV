package com.keyrus.proxemconnector.connector.csv.configuration.model;

import java.util.Collection;

public abstract class Connector {
    protected final String id;
    protected final String name;
    protected final Collection<Field> fields;
    protected final String projectName;
   // protected final Long userId;

    protected Connector(
            final String id,
            final String name,
            final Collection<Field> fields
            ,final String projectName
           // Long userId
    ) {
        this.id = id;
        this.name = name;
        this.fields = fields;
         this.projectName = projectName;
        //this.userId = userId;
    }


    public String name() {
        return this.name;
    }

    public String id() {
        return this.id;
    }
    public Collection<Field> fields() {
        return this.fields;
    }
    public String projectName() {
        return this.projectName;
    }
   /* public Long userId() {
        return this.userId;
    }*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        if (!id.equals(connector.id)) return false;
        if (!name.equals(connector.name)) return false;
        if (!fields.equals(connector.fields)) return false;
        return projectName.equals(connector.projectName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + fields.hashCode();
        result = 31 * result + projectName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Connector{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fields=" + fields +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}