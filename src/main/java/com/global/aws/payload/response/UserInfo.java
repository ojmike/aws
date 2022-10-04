package com.global.aws.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    private String iamUser;

    private String accountId;

    private String userCreationTime;

    private LocalDate accessKey1LastRotated;

    private LocalDate accessKey2LastRotated;

    private String STATUS;

    private Long accessKey1DayCount;

    private Long accessKey2DayCount;

}