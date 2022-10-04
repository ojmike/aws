package com.global.aws.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAMIsResponse {

    private String accountId;

    private String region;

    private String imageId;

    private String imageType;

    private String creationDate;

    private String STATUS;

}
