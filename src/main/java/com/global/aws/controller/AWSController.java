package com.global.aws.controller;

import com.global.aws.payload.request.GetAMIsRequest;
import com.global.aws.payload.response.ImageResponse;
import com.global.aws.service.AWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AWSController {

    private final AWSService awsService;

    @PostMapping("/images")
    public List<List<ImageResponse>>  getImageDetails(@RequestBody GetAMIsRequest regions){
    return awsService.getAMIs(regions);
}

    @GetMapping("/user-info")
    public Object  getUserInfo(){
        return awsService.getClientReport();
    }
}
