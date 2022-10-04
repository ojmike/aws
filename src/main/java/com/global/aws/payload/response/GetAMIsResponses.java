package com.global.aws.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAMIsResponses {

    private List<GetAMIsResponse> getAMIsResponses;
}
