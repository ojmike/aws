package com.global.aws.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageResponse {

    private String accountId;

    private String region;

    private String imageId;

    private String imageType;

    private String creationDate;

    private String STATUS;
}
