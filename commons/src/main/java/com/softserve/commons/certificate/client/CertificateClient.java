package com.softserve.commons.certificate.client;

import com.softserve.commons.certificate.dto.CertificateUserResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "certificate",
        url = "http://${APIGW_NETWORK}:${APIGW_CERTIFICATE_PORT}",
        path = "api/v1/certificate")
public interface CertificateClient {
    @GetMapping("/all")
    List<CertificateUserResponse> getListOfCertificatesByUserEmail(@RequestParam("email") String email);
}
