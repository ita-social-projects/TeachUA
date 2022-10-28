package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.service.CertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController implements Api {

    private final CertificateService certificateService;

    public DownloadController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }


    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> download() {
        CertificateTransfer certificateTransfer = certificateService.getOneUnsentCertificate();
        byte[] bytes = certificateService.getPdfOutput(certificateTransfer);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "certificate.pdf";
        headers.setContentDispositionFormData("attachment", filename);
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
