package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.RestoreArchiveException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.repository.ArchiveRepository;
import com.softserve.teachua.service.impl.ArchiveServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArchiveServiceImplTest {

    private static final long CORRECT_ID = 1;

    private static final String ARCHIVE_DATA = "{\"field\":\"value\"}";

    private static final String RIGHT_ARCHIVE_CLASS_NAME = "java.lang.Object";

    private static final String WRONG_ARCHIVE_CLASS_NAME = "wrong class";

    private static final long WRONG_ID = 10;

    @Mock
    private ApplicationContext context;

    @InjectMocks
    private ArchiveServiceImpl archiveService;

    @Mock
    private ArchiveRepository archiveRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ObjectMapper objectMapper;

    private Archive archive;

    private List<Archive> singletonArchiveList;

    @Mock
    private Archivable archivable;

    @Mock
    private ArchiveMark<Object> archiveMark;

    @BeforeEach
    void setUp() {
        archive = Archive.builder()
            .id(CORRECT_ID)
            .data(ARCHIVE_DATA)
            .className(RIGHT_ARCHIVE_CLASS_NAME)
            .build();
        singletonArchiveList = Collections.singletonList(archive);

//        archivable = new Archivable() {
//
//            private String field = "value";
//
//            public String getField() {
//                return field;
//            }
//
//            @Override
//            public Class getServiceClass() {
//                return Object.class;
//            }
//        };

//        archiveMark = new ArchiveMark<Archivable>() {
//            @Override
//            public void archiveModel(Archivable archivable) {
//                return;
//            }
//
//            @Override
//            public void restoreModel(String archiveObject) throws JsonProcessingException {
//                return;
//            }
//        };
//        archiveMark = Mockito.mock(ArchiveMark.class);
    }

    @Test
    @DisplayName("Getting archive by wrong id throws NotExistException")
    void getArchiveObjectByWrongId() {
        assertThatThrownBy(() -> archiveService.getArchiveObjectById(WRONG_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    @DisplayName("Getting archive by correct id returns correct archive")
    void getArchiveObjectByCorrectId() {
        when(archiveRepository.findById(CORRECT_ID)).thenReturn(Optional.of(archive));
        assertThat(archiveService.getArchiveObjectById(CORRECT_ID)).isEqualTo(archive);
    }

    @Test
    @DisplayName("Finding archives by not existing Class name returns empty list")
    void findArchivesByWrongClassName() {
        assertThat(archiveService.findArchivesByClassName(WRONG_ARCHIVE_CLASS_NAME))
            .isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("Finding archives by Class name returns correct list")
    void findArchivesByCorrectClassName() {
        when(archiveRepository.findAllByClassName(RIGHT_ARCHIVE_CLASS_NAME)).thenReturn(singletonArchiveList);
        assertThat(archiveService.findArchivesByClassName(RIGHT_ARCHIVE_CLASS_NAME))
            .isEqualTo(singletonArchiveList);
    }

    @Test
    @DisplayName("Finding all archives returns correct list")
    void findAll() {
        when(archiveRepository.findAll()).thenReturn(singletonArchiveList);
        assertThat(archiveService.findAllArchives()).isEqualTo(singletonArchiveList);
    }

    @Test
    @DisplayName("Saving a model returns correct result")
    void saveCorrect() throws JsonProcessingException {
        archive.setId(null);
        when(archivable.getServiceClass()).thenReturn(Object.class);
        when(archiveRepository.save(archive)).thenReturn(archive);
        when(objectMapper.writeValueAsString(archivable)).thenReturn(ARCHIVE_DATA);
        assertThat(archiveService.saveModel(archivable)).isEqualTo(archive);
    }

    @Test
    @DisplayName("Saving not serializable model throws RestoreArchiveException")
    void saveWrong() throws JsonProcessingException {
        archive.setId(null);
        when(archivable.getServiceClass()).thenReturn(Object.class);
        when(objectMapper.writeValueAsString(archivable)).thenThrow(JsonProcessingException.class);
        assertThatThrownBy(() -> archiveService.saveModel(archivable))
            .isInstanceOf(RestoreArchiveException.class);
    }

    @Test
    @DisplayName("Restoring object by correct id returns correct archive")
    void restoreCorrectId() throws ClassNotFoundException, JsonProcessingException {
//        when(context.getBean(Class.forName(archive.getClassName()))).thenReturn((Object) archiveMark);
//        when(archiveMark.restoreModel(ARCHIVE_DATA)).thenCallRealMethod();
        doNothing().when(archiveMark).restoreModel(ARCHIVE_DATA);
        when(archiveRepository.findById(CORRECT_ID)).thenReturn(Optional.of(archive));
        assertThat(archiveService.restoreArchiveObject(CORRECT_ID)).isEqualTo(archive);
    }

    @Test
    void restoreWrongId() {

    }

}
