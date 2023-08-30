package com.keyrus.proxemconnector.connector.csv.configuration.model;

import java.util.Collection;

public abstract class Connector {
    protected final String id;
    protected final String name;
    protected final Collection<Field> fields;
    protected final String projectName;
    protected final String userName;

    protected Connector(
            final String id,
            final String name,
            final Collection<Field> fields
            , final String projectName
            , final String userName
    ) {
        this.id = id;
        this.name = name;
        this.fields = fields;
        this.projectName = projectName;
        this.userName = userName;
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

    public String userName() {
        return this.userName;
    }

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


    public  interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        final class IdMalformed implements Error {
            protected IdMalformed() {
            }
        }

        final class NameMalformed implements Error {
            public NameMalformed() {
            }
        }

        final class HeaderMalformed implements Error {
            public HeaderMalformed() {
            }
        }

        final class IdHeaderMissing implements Error {
            public IdHeaderMissing() {
            }
        }

        final class HeadersSequenceMalformed implements Error {
            public HeadersSequenceMalformed() {
            }
        }

        interface Header extends Error {

            final class IdMalformed implements Header {
                public IdMalformed() {
                }
            }

            final class ReferenceConnectorMalformed implements Header {
                public ReferenceConnectorMalformed() {
                }
            }

            final class NameMalformed implements Header {
                public NameMalformed() {
                }
            }

            final class PositionMalformed implements Header {
                public PositionMalformed() {
                }
            }

            final class MetaMalformed implements Header {
                public MetaMalformed() {
                }
            }

            final class NullOrEmptyRestrictionCombination implements Header {
                public NullOrEmptyRestrictionCombination() {
                }
            }
        }
    }

}


















