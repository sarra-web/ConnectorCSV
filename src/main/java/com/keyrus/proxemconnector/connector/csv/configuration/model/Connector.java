package com.keyrus.proxemconnector.connector.csv.configuration.model;

import java.util.Collection;

public abstract class Connector {
    protected final String id;
    protected final String name;
    protected final Collection<Field> fields;
    //protected final Project project;

    protected Connector(
            final String id,
            final String name,
            final Collection<Field> fields
          //  , Project project
    ) {
        this.id = id;
        this.name = name;
        this.fields = fields;
       // this.project = project;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        if (!id.equals(connector.id)) return false;
        if (!name.equals(connector.name)) return false;
        return fields.equals(connector.fields);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return
                """
                        Configuration[
                            id=%s,
                            name=%s,                     
                            fields=%s
                        ]
                        """
                        .formatted(
                                this.id,
                                this.name,
                                this.fields
                        );
    }

}
