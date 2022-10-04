package com.global.aws.service;

import com.global.aws.payload.request.GetAMIsRequest;
import com.global.aws.payload.response.ImageResponse;
import com.global.aws.payload.response.UserInfo;

import java.util.List;

public interface AWSService {

    List<List<ImageResponse>>  getAMIs(GetAMIsRequest regions);

   List<UserInfo> getClientReport();
}
