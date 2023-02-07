package com.softserve.teachua.service;

import static com.softserve.teachua.TestConstants.FILE_PATH_PDF;
import static com.softserve.teachua.TestUtils.getCertificateTemplate;
import static com.softserve.teachua.TestUtils.getCertificateType;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate_template.CertificateTemplatePreview;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProcessingResponse;
import com.softserve.teachua.dto.certificate_template.CertificateTemplateProfile;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import static com.softserve.teachua.service.CertificateService.LAST_JRXML_TEMPLATE_ID;
import com.softserve.teachua.service.impl.CertificateTemplateServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CertificateTemplateServiceTest {
    private static final ModelMapper mapper = new ModelMapper();
    @Spy
    @InjectMocks
    private CertificateTemplateServiceImpl certificateTemplateService;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private CertificateTemplateRepository certificateTemplateRepository;
    @Mock
    private CertificateTypeService certificateTypeService;
    @Mock
    private DtoConverter dtoConverter;

    private CertificateTemplate certificateTemplate;
    private CertificateType certificateType;
    private CertificateTemplatePreview certificateTemplatePreview;
    private CertificateTemplateProfile certificateTemplateProfile;

    private static Stream<Arguments> templateSource() {
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
        certificateTemplate = getCertificateTemplate();
        certificateTemplate.setFilePath(FILE_PATH_PDF);
        certificateType = getCertificateType();

        certificateTemplatePreview = mapper.map(certificateTemplate, CertificateTemplatePreview.class);

        certificateTemplateProfile = new CertificateTemplateProfile();
        BeanUtils.copyProperties(certificateTemplate, certificateTemplateProfile);
        certificateTemplateProfile.setCertificateType(certificateType.getCodeNumber());
    }

    @Test
    void getTemplateById() {
        when(certificateTemplateRepository.findById(certificateTemplate.getId())).thenReturn(
                Optional.ofNullable(certificateTemplate));

        assertThat(certificateTemplateService.getTemplateById(certificateTemplate.getId())).isNotNull()
                .isEqualTo(certificateTemplate);
    }

    @ParameterizedTest
    @MethodSource("templateSource")
    void getTemplateProfileById(boolean isUsed) {
        when(certificateTemplateRepository.findById(certificateTemplate.getId())).thenReturn(
                Optional.ofNullable(certificateTemplate));
        when(certificateRepository.existsByTemplateId(certificateTemplate.getId())).thenReturn(isUsed);

        certificateTemplatePreview.setUsed(isUsed);

        assertThat(certificateTemplateService.getTemplateProfileById(certificateTemplate.getId())).isNotNull()
                .isEqualTo(certificateTemplatePreview);
    }

    @Test
    void getTemplateByType() {
        when(certificateTemplateRepository.findFirstByCertificateTypeId(
                certificateTemplate.getCertificateType().getId())).thenReturn(Optional.ofNullable(certificateTemplate));

        assertThat(certificateTemplateService.getTemplateByType(
                certificateTemplate.getCertificateType().getId())).isNotNull().isEqualTo(certificateTemplate);
    }

    @Test
    void addTemplate() {
        when(certificateTemplateRepository.save(certificateTemplate)).thenReturn(certificateTemplate);

        assertThat(certificateTemplateService.addTemplate(certificateTemplate)).isNotNull()
                .isEqualTo(certificateTemplate);
    }

    @ParameterizedTest
    @MethodSource("templateSource")
    void addTemplateUsingProfile(boolean isNameExists, boolean isFilePathExists, int messagesCount,
                                 boolean isCertificateTemplateNull) {
        when(certificateTemplateRepository.existsByNameIgnoreCase(certificateTemplate.getName())).thenReturn(
                isNameExists);
        when(certificateTemplateRepository.existsByFilePath(certificateTemplate.getFilePath())).thenReturn(
                isFilePathExists);
        if (!isCertificateTemplateNull) {
            when(dtoConverter.convertToEntity(certificateTemplateProfile, new CertificateTemplate())).thenReturn(
                    certificateTemplate);
            when(certificateTypeService.getCertificateTypeByCodeNumber(
                    certificateTemplateProfile.getCertificateType())).thenReturn(certificateType);
            when(certificateTemplateRepository.save(certificateTemplate)).thenReturn(certificateTemplate);
        }

        CertificateTemplateProcessingResponse response =
                certificateTemplateService.addTemplate(certificateTemplateProfile);

        assertThat(response.getMessages()).hasSizeGreaterThanOrEqualTo(messagesCount);
        assertThat(response.getTemplate()).isEqualTo(isCertificateTemplateNull ? null : certificateTemplate);
    }

    @Test
    void getAllTemplates() {
        when(certificateTemplateRepository.findByIdGreaterThanOrderByIdDesc(LAST_JRXML_TEMPLATE_ID)).thenReturn(
                Collections.singletonList(certificateTemplate));
        when(dtoConverter.convertToDto(certificateTemplate, CertificateTemplatePreview.class)).thenReturn(
                certificateTemplatePreview);

        assertThat(certificateTemplateService.getAllTemplates()).isNotEmpty()
                .isEqualTo(Collections.singletonList(certificateTemplatePreview));
    }

    @Test
    void getTemplateByFilePath() {
        when(certificateTemplateRepository.getCertificateTemplateByFilePath(
                certificateTemplate.getFilePath())).thenReturn(certificateTemplate);

        assertThat(certificateTemplateService.getTemplateByFilePath(certificateTemplate.getFilePath())).isNotNull()
                .isEqualTo(certificateTemplate);
    }

    @ParameterizedTest
    @MethodSource("templateSource")
    void updateTemplate(boolean isNameExists, boolean isFilePathExists, int messagesCount,
                        boolean isCertificateTemplateNull) {
        certificateTemplateProfile.setName("Name");
        certificateTemplateProfile.setFilePath("path.pdf");
        CertificateTemplate targetTemplate = new CertificateTemplate();
        BeanUtils.copyProperties(certificateTemplateProfile, targetTemplate);

        when(certificateTemplateRepository.findById(certificateTemplate.getId())).thenReturn(
                Optional.ofNullable(certificateTemplate));
        when(certificateTemplateRepository.existsByNameIgnoreCase(certificateTemplateProfile.getName())).thenReturn(
                isNameExists);
        when(certificateTemplateRepository.existsByFilePath(certificateTemplateProfile.getFilePath())).thenReturn(
                isFilePathExists);
        if (!isCertificateTemplateNull) {
            when(certificateTypeService.getCertificateTypeByCodeNumber(
                    certificateTemplateProfile.getCertificateType())).thenReturn(certificateType);
            when(certificateTemplateRepository.save(certificateTemplate)).thenReturn(certificateTemplate);
        }

        CertificateTemplateProcessingResponse response =
                certificateTemplateService.updateTemplate(certificateTemplateProfile.getId(),
                        certificateTemplateProfile);
        assertThat(response.getMessages()).hasSizeGreaterThanOrEqualTo(messagesCount);
        assertThat(response.getTemplate())
                .isEqualTo(isCertificateTemplateNull ? null : certificateTemplate);
    }

    @Test
    void test(@TempDir() Path tempDirectory) throws IOException {
        String fileName = "test.txt";
        Files.createFile(tempDirectory.resolve(fileName));
        ReflectionTestUtils.setField(certificateTemplateService, "certificateTemplateUrl", tempDirectory.toString());
        when(certificateRepository.existsByTemplateId(anyInt())).thenReturn(false);
        when(certificateTemplateRepository.findById(anyInt())).thenReturn(
                Optional.of(CertificateTemplate.builder().filePath(fileName).build()));

        boolean actual = certificateTemplateService.deleteTemplateById(anyInt());
        assertThat(actual).isTrue();
    }
}
