package com.softserve.certificate.controller;

import com.softserve.certificate.constants.RoleData;
import com.softserve.certificate.controller.marker.Api;
import com.softserve.certificate.dto.certificate_type.CertificateTypeProcessingResponse;
import com.softserve.certificate.dto.certificate_type.CertificateTypeProfile;
import com.softserve.certificate.model.CertificateType;
import com.softserve.certificate.service.CertificateTypeService;
import com.softserve.certificate.utils.annotation.AllowedRoles;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for managing certificate types.
 */
@RestController
@Slf4j
public class CertificateTypeController implements Api {
    private final CertificateTypeService certificateTypeService;

    public CertificateTypeController(CertificateTypeService certificateTypeService) {
        this.certificateTypeService = certificateTypeService;
    }

    /**
     * This endpoint is used to get all certificate types.
     *
     * @return {@code List<CertificateType>}
     */
    @GetMapping("/certificate-types")
    @AllowedRoles(RoleData.ADMIN)
    public List<CertificateType> getAllCertificateTypes() {
        return certificateTypeService.getListOfCertificateTypes();
    }

    /**
     * The method saves certificate type to database.
     *
     * @param certificateTypeProfile {@code CertificateTypeProfile} read from form.
     * @return {@code CertificateTypeProcessingResponse}
     */
    @PostMapping("/certificate-type")
    public CertificateTypeProcessingResponse createCertificateType(@Valid @RequestBody
                                                                   CertificateTypeProfile certificateTypeProfile) {
        return certificateTypeService.addCertificateType(certificateTypeProfile);
    }

    /**
     * Use this endpoint to update certificate type. The controller returns {@code CertificateTypeProcessingResponse}.
     * This feature available only for admins.
     *
     * @param id              put template id here.
     * @param certificateTypeProfile put updated object here.
     * @return {@code CertificateTypeProcessingResponse} shows result of updating template.
     */
    @PutMapping("/certificate-type/{id}")
    public CertificateTypeProcessingResponse updateCertificateType(@PathVariable Integer id,
                                                                   @Valid @RequestBody
                                                                 CertificateTypeProfile certificateTypeProfile) {
        return certificateTypeService.updateCertificateType(id, certificateTypeProfile);
    }

    /**
     * Use this endpoint to delete certificate type.
     * This feature available only for admins.
     *
     * @param id put certificate type id here.
     */
    @DeleteMapping("/certificate-type/{id}")
    public void deleteCertificateType(@PathVariable Integer id) {
        certificateTypeService.deleteCertificateType(id);
    }
}
