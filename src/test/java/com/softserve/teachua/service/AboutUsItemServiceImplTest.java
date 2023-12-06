package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.AboutUsItem;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.archivable.AboutUsItemArch;
import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.service.impl.AboutUsItemServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AboutUsItemServiceImplTest {

    private static final Long CORRECT_ITEM_ID = 1L;
    private static final Long WRONG_ITEM_ID = 2L;
    private static final String ITEM_TEXT = "text";
    private static final Long ITEM_TYPE = 1L;
    private static final String VIDEO_ID = "ABC123";
    private static final String DEFAULT_YOUTUBE_LINK = String
            .format("https://www.youtube.com/watch?v=%s&ab_channel=MyChannel", VIDEO_ID);
    private static final String EXTENDED_YOUTUBE_LINK = String.format(
            "https://www.youtube.com/watch?v=%s&list=List&start_radio=1&rv=rvrvrv&ab_channel=MyChannel", VIDEO_ID);
    private static final String WRONG_YOUTUBE_LINK = "https://www.youtube.com/ab_channel=MyChannel";
    private static final String VALIDATED_URL = String.format("https://www.youtube.com/embed/%s", VIDEO_ID);
    // order
    private static final Long FIRST_POSITION = 0L;
    @Mock
    private AboutUsItemRepository aboutUsItemRepository;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private DtoConverter dtoConverter;
    @InjectMocks
    private AboutUsItemServiceImpl aboutUsItemService;
    private AboutUsItem aboutUsItem;
    private AboutUsItemResponse aboutUsItemResponse;
    private AboutUsItemArch aboutUsItemArch;
    private AboutUsItemProfile aboutUsItemProfile;

    @BeforeEach
    void setUpMocks() {
        aboutUsItem = AboutUsItem.builder().id(CORRECT_ITEM_ID).text(ITEM_TEXT).type(ITEM_TYPE).number(FIRST_POSITION)
                .build();

        aboutUsItemResponse = AboutUsItemResponse.builder().id(CORRECT_ITEM_ID).text(ITEM_TEXT).type(ITEM_TYPE).build();

        aboutUsItemProfile = AboutUsItemProfile.builder().text(ITEM_TEXT).type(ITEM_TYPE).number(FIRST_POSITION)
                .build();

        aboutUsItemArch = AboutUsItemArch.builder().text(ITEM_TEXT).type(ITEM_TYPE).number(FIRST_POSITION).build();
    }

    @Test
    void getAboutUsItemByCorrectIdShouldReturnAboutUsItem() {
        when(aboutUsItemRepository.findById(CORRECT_ITEM_ID)).thenReturn(Optional.of(aboutUsItem));
        assertThat(aboutUsItemService.getAboutUsItemById(CORRECT_ITEM_ID)).isEqualTo(aboutUsItem);
    }

    @Test
    void getAboutUsItemByWrongIdThrowNotExistException() {
        assertThatThrownBy(() -> aboutUsItemService.getAboutUsItemById(WRONG_ITEM_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getAboutUsItemResponseByCorrectIdShouldReturnAboutUsItem() {
        when(aboutUsItemRepository.findById(CORRECT_ITEM_ID)).thenReturn(Optional.of(aboutUsItem));
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class)).thenReturn(aboutUsItemResponse);
        assertThat(aboutUsItemService.getAboutUsItemResponseById(CORRECT_ITEM_ID)).isEqualTo(aboutUsItemResponse);
    }

    @Test
    void getAboutUsItemResponseByWrongIdThrowNotExistException() {
        assertThatThrownBy(() -> aboutUsItemService.getAboutUsItemResponseById(WRONG_ITEM_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void getListOfAboutUsItemResponsesShouldReturnListOfAboutUsItemResponses() {
        when(aboutUsItemRepository.findAllByOrderByNumberAsc()).thenReturn(Collections.singletonList(aboutUsItem));
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class)).thenReturn(aboutUsItemResponse);
        assertThat(aboutUsItemService.getListOfAboutUsItemResponses()).isNotEmpty()
                .isEqualTo(Collections.singletonList(aboutUsItemResponse));
    }

    @Test
    void getListOfAboutUsItemResponsesShouldReturnEmptyList() {
        when(aboutUsItemRepository.findAllByOrderByNumberAsc()).thenReturn(List.of());
        assertThat(aboutUsItemService.getListOfAboutUsItemResponses()).isEmpty();
    }

    @Test
    void addAboutUsItemShouldReturnAboutUsItemResponse() {
        when(aboutUsItemRepository.findAllByOrderByNumberAsc()).thenReturn(Collections.singletonList(aboutUsItem));
        when(dtoConverter.convertToEntity(aboutUsItemProfile, new AboutUsItem())).thenReturn(aboutUsItem);
        when(aboutUsItemRepository.save(aboutUsItem)).thenReturn(aboutUsItem);
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class)).thenReturn(aboutUsItemResponse);
        assertThat(aboutUsItemService.addAboutUsItem(aboutUsItemProfile)).isEqualTo(aboutUsItemResponse);
    }

    @Test
    void updateAboutUsItemShouldReturnAboutUsItemResponse() {
        when(aboutUsItemRepository.findById(CORRECT_ITEM_ID)).thenReturn(Optional.of(aboutUsItem));
        when(aboutUsItemRepository.save(aboutUsItem)).thenReturn(aboutUsItem);
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class)).thenReturn(aboutUsItemResponse);
        assertThat(aboutUsItemService.updateAboutUsItem(CORRECT_ITEM_ID, aboutUsItemProfile))
                .isEqualTo(aboutUsItemResponse);
    }

    @Test
    void deleteAboutUsItemByIdShouldReturnAboutUsItemResponse() {
        when(aboutUsItemRepository.findById(CORRECT_ITEM_ID)).thenReturn(Optional.of(aboutUsItem));
        doNothing().when(aboutUsItemRepository).deleteById(CORRECT_ITEM_ID);
        doNothing().when(aboutUsItemRepository).flush();
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemArch.class)).thenReturn(aboutUsItemArch);
        when(archiveService.saveModel(aboutUsItemArch)).thenReturn(Archive.builder().build());
        when(dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class)).thenReturn(aboutUsItemResponse);
        assertThat(aboutUsItemService.deleteAboutUsItemById(CORRECT_ITEM_ID)).isEqualTo(aboutUsItemResponse);
    }

    @Test
    void validateVideoWithNullUrlShouldReturnNull() {
        aboutUsItemProfile.setVideo(null);
        assertThat(aboutUsItemService.validateVideoUrl(aboutUsItemProfile)).isNull();
    }

    @Test
    void validateVideoWithDefaultUrlShouldReturnValidatedUrl() {
        aboutUsItemProfile.setVideo(DEFAULT_YOUTUBE_LINK);
        assertThat(aboutUsItemService.validateVideoUrl(aboutUsItemProfile)).isEqualTo(VALIDATED_URL);
    }

    @Test
    void validateVideoWithExtendedUrlShouldReturnValidatedUrl() {
        aboutUsItemProfile.setVideo(EXTENDED_YOUTUBE_LINK);
        assertThat(aboutUsItemService.validateVideoUrl(aboutUsItemProfile)).isEqualTo(VALIDATED_URL);
    }

    @Test
    void validateVideoWithWrongUrlShouldThrowIllegalArgumentException() {
        aboutUsItemProfile.setVideo(WRONG_YOUTUBE_LINK);
        assertThatThrownBy(() -> aboutUsItemService.validateVideoUrl(aboutUsItemProfile))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
