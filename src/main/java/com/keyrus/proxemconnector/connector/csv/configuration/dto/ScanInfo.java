package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanInfo {
    private String startTime;
    private String finalTime;
    private int numberDoc;

  private List<ProxemDto> proxemDtos;
}
