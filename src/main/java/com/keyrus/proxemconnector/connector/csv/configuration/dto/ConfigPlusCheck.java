package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPlusCheck {
    ConnectorJDBCDTO connectorJDBCDTO;
    String check;
}
