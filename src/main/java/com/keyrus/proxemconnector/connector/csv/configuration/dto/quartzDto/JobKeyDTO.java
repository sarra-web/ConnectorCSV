package com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobKeyDTO {
    private String jobId;
    private String jobGroup;
}
