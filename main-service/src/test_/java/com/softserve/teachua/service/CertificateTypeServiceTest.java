package com.softserve.teachua.service;

import static com.softserve.teachua.TestUtils.getCertificateType;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate_type.CertificateTypeProcessingResponse;
import com.softserve.teachua.dto.certificate_type.CertificateTypeProfile;
import com.softserve.teachua.exception.EntityIsUsedException;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.repository.CertificateTypeRepository;
import com.softserve.teachua.service.impl.CertificateTypeServiceImpl;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class CertificateTypeServiceTest {
    private static final ModelMapper mapper = new ModelMapper();
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private CertificateTypeRepository certificateTypeRepository;
    @Mock
    private CertificateTemplateRepository certificateTemplateRepository;
    @Spy
    @InjectMocks
    private CertificateTypeServiceImpl certificateTypeService;
    private CertificateType certificateType;
    private CertificateTypeProfile certificateTypeProfile;

    private static Stream<Arguments> certificateTypeSource() {
        // @formatter:off
        return Stream.of(
                Arguments.of(true, true, 2, true),
                Arguments.of(false, true, 1, true),
                Arguments.of(false, false, 0, false)
        );
        // @formatter:on
    }

    @BeforeEach
    void setUp() {
        certificateType = getCertificateType();

        certificateTypeProfile = mapper.map(certificateType, CertificateTypeProfile.class);
    }

    @Test
    void getCertificateTypeById() {
        when(certificateTypeRepository.findById(certificateType.getId())).thenReturn(Optional.of(certificateType));

        assertThat(certificateTypeService.getCertificateTypeById(certificateType.getId())).isEqualTo(certificateType);
    }

    @Test
    void getCertificateTypeByCodeNumber() {
        when(certificateTypeRepository.findCertificateTypeByCodeNumber(certificateType.getCodeNumber())).thenReturn(
                certificateType);

        assertThat(certificateTypeService.getCertificateTypeByCodeNumber(certificateType.getCodeNumber())).isEqualTo(
                certificateType);
    }

    @Test
    void getListOfCertificateTypes() {
        when(certificateTypeRepository.findAll(Sort.by(Sort.Order.by("id"))))
                .thenReturn(Collections.singletonList(certificateType));

        assertThat(certificateTypeService.getListOfCertificateTypes())
                .isNotEmpty()
                .isEqualTo(Collections.singletonList(certificateType));
    }

    @Test
    void addCertificateType() {
        when(certificateTypeRepository.save(certificateType)).thenReturn(certificateType);

        assertThat(certificateTypeService.addCertificateType(certificateType))
                .isEqualTo(certificateType);
    }

    @ParameterizedTest
    @MethodSource("certificateTypeSource")
    void addCertificateType(boolean isNameExists, boolean isCodeNumberExists, int messagesCount,
                            boolean isCertificateTypeNull) {
        when(certificateTypeRepository.existsByNameIgnoreCase(certificateType.getName())).thenReturn(isNameExists);
        when(certificateTypeRepository.existsByCodeNumber(certificateType.getCodeNumber())).thenReturn(
                isCodeNumberExists);
        if (!isCertificateTypeNull) {
            when(certificateTypeRepository.save(
                    dtoConverter.convertToEntity(certificateTypeProfile, new CertificateType()))).thenReturn(
                    certificateType);
        }

        CertificateTypeProcessingResponse response = certificateTypeService.addCertificateType(certificateTypeProfile);

        assertThat(response.getMessages()).hasSizeGreaterThanOrEqualTo(messagesCount);
        assertThat(response.getCertificateType()).isEqualTo(isCertificateTypeNull ? null : certificateType);
    }

    @ParameterizedTest
    @MethodSource("certificateTypeSource")
    void updateCertificateType(boolean isNameExists, boolean isCodeNumberExists, int messagesCount,
                               boolean isCertificateTypeNull) {
        certificateTypeProfile.setName("Name");
        certificateTypeProfile.setCodeNumber(10);
        CertificateType targetType = new CertificateType();
        BeanUtils.copyProperties(certificateTypeProfile, targetType);

        when(certificateTypeRepository.findById(certificateTypeProfile.getId())).thenReturn(
                Optional.of(certificateType));
        when(certificateTypeRepository.existsByNameIgnoreCase(certificateTypeProfile.getName())).thenReturn(
                isNameExists);
        when(certificateTypeRepository.existsByCodeNumber(certificateTypeProfile.getCodeNumber())).thenReturn(
                isCodeNumberExists);
        if (!isCertificateTypeNull) {
            when(certificateTypeRepository.save(certificateType)).thenReturn(certificateType);
        }

        CertificateTypeProcessingResponse response =
                certificateTypeService.updateCertificateType(certificateTypeProfile.getId(), certificateTypeProfile);

        assertThat(response.getMessages()).hasSizeGreaterThanOrEqualTo(messagesCount);
        assertThat(response.getCertificateType()).isEqualTo(isCertificateTypeNull ? null : targetType);
    }

    @Test
    void deleteCertificateType_IfExists() {
        when(certificateTypeRepository.findById(certificateTypeProfile.getId())).thenReturn(
                Optional.of(certificateType));
        when(certificateTemplateRepository.existsCertificateTemplateByCertificateType(certificateType)).thenReturn(
                true);
        Integer certificateTypeId = certificateType.getId();
        assertThrows(EntityIsUsedException.class,
                () -> certificateTypeService.deleteCertificateType(certificateTypeId));
    }

    @Test
    void deleteCertificateType_IfDoesNotExists() {
        when(certificateTypeRepository.findById(certificateTypeProfile.getId())).thenReturn(
                Optional.of(certificateType));
        when(certificateTemplateRepository.existsCertificateTemplateByCertificateType(certificateType)).thenReturn(
                false);

        assertDoesNotThrow(() -> certificateTypeService.deleteCertificateType(certificateType.getId()));
    }

}
