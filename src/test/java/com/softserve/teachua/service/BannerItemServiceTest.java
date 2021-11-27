package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.BannerItemResponse;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BannerItemServiceTest {

    @Mock
    private BannerItemRepository bannerItemRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @InjectMocks
    private BannerItemServiceImpl bannerItemService;

    private BannerItem bannerItem;
    private BannerItemProfile bannerItemProfile;
    private BannerItemResponse bannerItemResponse;
    private SuccessCreatedBannerItem successCreatedBannerItem;

    private final Long EXISTING_ID = 1L;
    private final Long NOT_EXISTING_ID = 500L;

    private final String VALID_TITLE = "Valid Title/Валідний тайтл/123456";
    private final String VALID_SUBTITLE = "Valid Subtitle/Валідний Субтайтл/123456";
    private final String VALID_PICTURE_PATH = "/upload/folder/file.png";

    private final String NEW_VALID_TITLE = "New Valid Title/Валідний тайтл/123456";
    private final String NEW_VALID_PICTURE_PATH = "/upload/folder/newfile.png";

    private final Integer VALID_SEQUENCE_NUMBER = 1;

    @BeforeEach
    public void setUp() {
        bannerItem = BannerItem.builder()
                .id(EXISTING_ID)
                .title(VALID_TITLE)
                .subtitle(VALID_SUBTITLE)
                .picture(VALID_PICTURE_PATH)
                .sequenceNumber(VALID_SEQUENCE_NUMBER)
                .build();
        bannerItemProfile = BannerItemProfile.builder()
                .title(NEW_VALID_TITLE)
                .picture(NEW_VALID_PICTURE_PATH)
                .sequenceNumber(VALID_SEQUENCE_NUMBER)
                .build();
        bannerItemResponse = BannerItemResponse.builder()
                .title(VALID_TITLE)
                .picture(VALID_PICTURE_PATH)
                .sequenceNumber(VALID_SEQUENCE_NUMBER)
                .build();
        successCreatedBannerItem = SuccessCreatedBannerItem.builder()
                .title(NEW_VALID_TITLE)
                .picture(NEW_VALID_PICTURE_PATH)
                .sequenceNumber(VALID_SEQUENCE_NUMBER)
                .build();
    }

    @Test
    public void getBannerItemProfileByExistingIdShouldReturnBannerItemResponse() {
        when(bannerItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(bannerItem));
        when(dtoConverter.convertToDto(bannerItem, BannerItemResponse.class)).thenReturn(bannerItemResponse);

        BannerItemResponse actual = bannerItemService.getBannerItemProfileById(EXISTING_ID);
        assertEquals(bannerItemResponse, actual);
    }

    @Test
    public void getBannerItemProfileByNotExistingIdShouldThrowNotExistException() {
        when(bannerItemRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bannerItemService.getBannerItemProfileById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getBannerItemByExistingIdShouldReturnBannerItem() {
        when(bannerItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(bannerItem));

        BannerItem actual = bannerItemService.getBannerItemById(EXISTING_ID);
        assertEquals(bannerItem, actual);
    }

    @Test
    public void getBannerItemByNotExistingIdShouldThrowNotExistException() {
        when(bannerItemRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bannerItemService.getBannerItemById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getListOfBannerItemsShouldReturnListOfBannerItemResponses() {
        List<BannerItem> bannerItems = Arrays.asList(bannerItem);

        when(bannerItemRepository.findAllByOrderBySequenceNumberAsc()).thenReturn(bannerItems);
        when(dtoConverter.convertToDto(bannerItem, BannerItemResponse.class)).thenReturn(bannerItemResponse);

        List<BannerItemResponse> actual = bannerItemService.getListOfBannerItems();
        assertEquals(bannerItems.size(), actual.size());
        assertEquals(bannerItem.getTitle(), actual.get(0).getTitle());
        assertEquals(bannerItem.getPicture(), actual.get(0).getPicture());
        assertEquals(bannerItem.getSequenceNumber(), actual.get(0).getSequenceNumber());
    }

    @Test
    public void addBannerItemWithValidDataShouldReturnSuccessCreatedBannerItem() {
        BannerItem newBanner = BannerItem.builder().title(NEW_VALID_TITLE).picture(NEW_VALID_PICTURE_PATH)
                .sequenceNumber(VALID_SEQUENCE_NUMBER).build();

        when(dtoConverter.convertToEntity(bannerItemProfile, new BannerItem())).thenReturn(newBanner);
        when(bannerItemRepository.save(any())).thenReturn(newBanner);
        when(dtoConverter.convertToDto(newBanner, SuccessCreatedBannerItem.class)).thenReturn(SuccessCreatedBannerItem.builder()
                .title(NEW_VALID_TITLE).picture(NEW_VALID_PICTURE_PATH).sequenceNumber(VALID_SEQUENCE_NUMBER).build());

        SuccessCreatedBannerItem actual = bannerItemService.addBannerItem(bannerItemProfile);
        assertEquals(successCreatedBannerItem, actual);
    }

    @Test
    public void updateBannerItemWithExistingIdShouldReturnBannerItemResponse() {
        BannerItemResponse expected = BannerItemResponse.builder()
                .title(NEW_VALID_TITLE).picture(NEW_VALID_PICTURE_PATH).sequenceNumber(VALID_SEQUENCE_NUMBER).build();

        when(bannerItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(bannerItem));
        when(bannerItemRepository.save(any())).thenReturn(bannerItem);
        when(dtoConverter.convertToEntity(bannerItemProfile, bannerItem)).thenReturn(BannerItem.builder()
                .title(NEW_VALID_TITLE).picture(NEW_VALID_PICTURE_PATH).sequenceNumber(VALID_SEQUENCE_NUMBER).build());
        when(dtoConverter.convertToDto(bannerItem, BannerItemResponse.class)).thenReturn(BannerItemResponse.builder()
                .title(NEW_VALID_TITLE).picture(NEW_VALID_PICTURE_PATH).sequenceNumber(VALID_SEQUENCE_NUMBER).build());

        BannerItemResponse actual = bannerItemService.updateBannerItem(EXISTING_ID, bannerItemProfile);
        assertEquals(expected, actual);
    }

    @Test
    public void updateBannerItemWithNotExistingIdThrowNotExistException() {
        when(bannerItemRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bannerItemService.updateBannerItem(NOT_EXISTING_ID, bannerItemProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void deleteBannerItemWithExistingId() {
        when(bannerItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(bannerItem));
//        when(archiveService.saveModel(bannerItem)).thenReturn(bannerItem);
        doNothing().when(bannerItemRepository).deleteById(EXISTING_ID);
        doNothing().when(bannerItemRepository).flush();
        when(dtoConverter.convertToDto(bannerItem, BannerItemResponse.class)).thenReturn(bannerItemResponse);

        BannerItemResponse actual = bannerItemService.deleteBannerItemById(EXISTING_ID);
        assertEquals(bannerItemResponse, actual);
    }

    @Test
    public void deleteBannerItemWithNotExistingIdShouldThrowNotExistException() {
        when(bannerItemRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bannerItemService.deleteBannerItemById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }
}
