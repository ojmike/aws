package com.global.aws.service.Impl;

import com.global.aws.payload.response.ImageResponse;
import com.global.aws.payload.response.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeImagesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.iam.IamClient;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class AWSResource {
    public List<ImageResponse> describeEC2Instances(Ec2Client ec2, String region){

        try {
        DescribeImagesResponse response = ec2.describeImages();

                return response.images().parallelStream()
                        .map( image ->  ImageResponse.builder()
                                .accountId(image.ownerId())
                                .region(region)
                                .imageId(image.imageId())
                                .imageType(image.imageTypeAsString())
                                .creationDate(image.creationDate())
                                .STATUS(image.publicLaunchPermissions() ? "CRITICAL" : "OK")
                                .build())
                        .collect(Collectors.toList());


        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
            return null;
        }

    }

    public List<UserInfo> getClientReport() {

        List<UserInfo> responses = new ArrayList<>();
        Region awsRegion = Region.AWS_GLOBAL;

        try (IamClient iamClient = IamClient.builder().region(awsRegion).build()) {

            iamClient.generateCredentialReport();

            String csvReport = iamClient.getCredentialReport().content().asUtf8String();

            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();

            CSVParser csvParser = new CSVParser(new StringReader(csvReport), csvFormat);

            csvParser.getRecords().forEach(csvRecord -> {

                UserInfo userInfo = new UserInfo();

                userInfo.setAccountId(csvRecord.get("arn").substring(13,25));

                userInfo.setIamUser(csvRecord.get("user"));

                userInfo.setAccessKey1LastRotated(formatStringToLocalDate(csvRecord.get("access_key_1_last_rotated")));

                userInfo.setAccessKey2LastRotated(formatStringToLocalDate(csvRecord.get("access_key_2_last_rotated")));

                int count = 0;
                if(Boolean.parseBoolean(csvRecord.get("access_key_1_active"))){
                    if(LocalDate.now().minusDays(30).isAfter(userInfo.getAccessKey1LastRotated())){
                        count++;
                        userInfo.setAccessKey1DayCount(DAYS.between(userInfo.getAccessKey1LastRotated(),LocalDate.now()));
                    }
                }

                if(Boolean.parseBoolean(csvRecord.get("access_key_2_active"))){
                    if(LocalDate.now().minusDays(30).isAfter(userInfo.getAccessKey2LastRotated())){
                        count++;
                        userInfo.setAccessKey2DayCount(DAYS.between(userInfo.getAccessKey2LastRotated(),LocalDate.now()));
                    }
                }

                userInfo.setSTATUS(count > 0 ? "CRITICAL" : "OK");

                responses.add(userInfo);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responses;
    }


    private LocalDate formatStringToLocalDate(String localDate){

        if(localDate.length() > 3) localDate =  localDate.substring(0,10);
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale( Locale.US );
            return LocalDate.parse(localDate, formatter);
        }catch (Exception e){
            return null;
        }
    }

}
