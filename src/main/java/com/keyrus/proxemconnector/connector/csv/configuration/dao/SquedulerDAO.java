package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "scheduler")
public class SquedulerDAO implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;

    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "connector_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ConnectorDAO connectorDAO;
}
