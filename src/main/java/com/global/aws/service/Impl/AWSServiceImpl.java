package com.global.aws.service.Impl;

import com.global.aws.payload.request.GetAMIsRequest;
import com.global.aws.payload.response.ImageResponse;
import com.global.aws.payload.response.UserInfo;
import com.global.aws.service.AWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AWSServiceImpl implements AWSService {

    private final AWSResource awsResource;

    @Override
    public List<List<ImageResponse>> getAMIs(GetAMIsRequest regions) {

        return regions.getRegions().parallelStream()
                .map( region -> {
                    Ec2Client ec2 = Ec2Client.builder()
                            .region(Region.of(region))
                            .build();
                    return awsResource.describeEC2Instances(ec2,region);
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<UserInfo> getClientReport() {
        return awsResource.getClientReport();
    }
}
