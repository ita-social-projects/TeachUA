package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificateType.CertificateTypeProcessingResponse;
import com.softserve.teachua.dto.certificateType.CertificateTypeProfile;
import com.softserve.teachua.exception.EntityIsUsedException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.repository.CertificateTypeRepository;
import com.softserve.teachua.service.CertificateTypeService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateTypeServiceImpl implements CertificateTypeService {
    private static final String TYPE_NOT_FOUND_BY_ID = "Certificate type not found by id: %s";
    private static final String NAME_ALREADY_EXISTS_MESSAGE = "Тип із такою назвою уже існує!";
    private static final String CODE_NUMBER_ALREADY_EXISTS_MESSAGE = "Тип із таким кодовим номером уже існує!";
    private final DtoConverter dtoConverter;
    private final CertificateTypeRepository certificateTypeRepository;
    private final CertificateTemplateRepository certificateTemplateRepository;

    @Autowired
    public CertificateTypeServiceImpl(DtoConverter dtoConverter, CertificateTypeRepository certificateTypeRepository,
                                      CertificateTemplateRepository certificateTemplateRepository) {
        this.dtoConverter = dtoConverter;
        this.certificateTypeRepository = certificateTypeRepository;
        this.certificateTemplateRepository = certificateTemplateRepository;
    }

    @Override
    public CertificateType getCertificateTypeById(Integer id) {
        return certificateTypeRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(TYPE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateType getCertificateTypeByCodeNumber(Integer id) {
        return certificateTypeRepository.findCertificateTypeByCodeNumber(id);
    }

    @Override
    public List<CertificateType> getListOfCertificateTypes() {
        return certificateTypeRepository.findAll(Sort.by(Order.by("id")));
    }

    @Override
    public CertificateType addCertificateType(CertificateType certificateType) {
        return certificateTypeRepository.save(certificateType);
    }

    @Override
    public CertificateTypeProcessingResponse addCertificateType(CertificateTypeProfile certificateTypeProfile) {
        List<String[]> messagesList = new ArrayList<>();

        if (certificateTypeRepository.existsByNameIgnoreCase(certificateTypeProfile.getName())) {
            messagesList.add(new String[]{NAME_ALREADY_EXISTS_MESSAGE, "1"});
        }
        if (certificateTypeRepository.existsByCodeNumber(certificateTypeProfile.getCodeNumber())) {
            messagesList.add(new String[]{CODE_NUMBER_ALREADY_EXISTS_MESSAGE, "3"});
            return CertificateTypeProcessingResponse.builder().messages(messagesList).build();
        }

        return CertificateTypeProcessingResponse.builder()
                .messages(messagesList)
                .certificateType(certificateTypeRepository.save(
                        dtoConverter.convertToEntity(certificateTypeProfile, new CertificateType())))
                .build();
    }

    @Override
    public CertificateTypeProcessingResponse updateCertificateType(Integer id,
                                                                   CertificateTypeProfile updatedCertificateType) {
        List<String[]> messagesList = new ArrayList<>();
        CertificateType certificateType = getCertificateTypeById(id);

        if (!certificateType.getName().equals(updatedCertificateType.getName())
                && certificateTypeRepository.existsByNameIgnoreCase(updatedCertificateType.getName())) {
            messagesList.add(new String[]{NAME_ALREADY_EXISTS_MESSAGE, "1"});
        }
        if (!certificateType.getCodeNumber().equals(updatedCertificateType.getCodeNumber())
                && certificateTypeRepository.existsByCodeNumber(updatedCertificateType.getCodeNumber())) {
            messagesList.add(new String[]{CODE_NUMBER_ALREADY_EXISTS_MESSAGE, "3"});
            return CertificateTypeProcessingResponse.builder().messages(messagesList).build();
        }
        BeanUtils.copyProperties(updatedCertificateType, certificateType);

        return CertificateTypeProcessingResponse.builder().messages(messagesList)
                .certificateType(certificateTypeRepository.save(certificateType)).build();
    }

    @Override
    public void deleteCertificateType(Integer id) {
        if (certificateTemplateRepository.existsCertificateTemplateByCertificateType(getCertificateTypeById(id))) {
            throw new EntityIsUsedException();
        }
        certificateTypeRepository.deleteById(id);
    }
}
