package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateType.CertificateTypeProcessingResponse;
import com.softserve.teachua.dto.certificateType.CertificateTypeProfile;
import com.softserve.teachua.model.CertificateType;
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
