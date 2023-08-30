package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Data
@Entity
@NoArgsConstructor
@Table(name = "scheduler")
public class SquedulerDAO implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;
    @Column(name = "name")
    private String name;



    //
  // @Enumerated(EnumType.STRING)
    @Column(name = "scan_mode")
    private String scanMode;
    @Column(name = "status")
    private boolean status;
    /*@Column(name = "checkPoint")
    private String scheckPoint;
    @Column(name = "s")
    private String s;*/
    @Column(name = "starts_time")
    private String startsTime;
    @Column(name = "end_time")
    private String endTime;
    @Column(name = "cron_expression")
    private String cronExpression;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "connector_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ConnectorDAO connectorDAO;


    @Column(name = "job_id")
    private String jobId;




}
