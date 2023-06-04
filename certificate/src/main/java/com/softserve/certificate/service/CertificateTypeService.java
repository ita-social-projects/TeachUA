package com.softserve.certificate.service;

import com.softserve.certificate.dto.certificate_type.CertificateTypeProcessingResponse;
import com.softserve.certificate.dto.certificate_type.CertificateTypeProfile;
import com.softserve.certificate.model.CertificateType;
import java.util.List;

public interface CertificateTypeService {
    CertificateType getCertificateTypeById(Integer id);

    CertificateType getCertificateTypeByCodeNumber(Integer id);

    List<CertificateType> getListOfCertificateTypes();

    CertificateType addCertificateType(CertificateType certificateType);

    CertificateTypeProcessingResponse addCertificateType(CertificateTypeProfile certificateTypeProfile);

    CertificateTypeProcessingResponse updateCertificateType(Integer id, CertificateTypeProfile certificateTypeProfile);

    void deleteCertificateType(Integer id);
}
