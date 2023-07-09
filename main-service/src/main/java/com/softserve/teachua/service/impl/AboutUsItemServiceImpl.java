package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.model.AboutUsItem;
import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.service.AboutUsItemService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AboutUsItemServiceImpl implements AboutUsItemService {
    private static final String ABOUT_US_ITEM_NOT_FOUND_BY_ID = "AboutUsItem was not found by id: %s";
    private static final String WRONG_LINK = "Youtube link should contain 'watch?v='";
    private static final String VIDEO_PARAM = "watch?v=";
    private static final String EMBED_VIDEO_URL = "https://www.youtube.com/embed/";
    private static final Long STEP = 20L;

    private final AboutUsItemRepository aboutUsItemRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final ArchiveMQMessageProducer<AboutUsItem> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    public AboutUsItemServiceImpl(AboutUsItemRepository aboutUsItemRepository, DtoConverter dtoConverter,
                                  ObjectMapper objectMapper,
                                  ArchiveMQMessageProducer<AboutUsItem> archiveMQMessageProducer,
                                  ArchiveClient archiveClient) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.archiveClient = archiveClient;
    }

    @Override
    public AboutUsItem getAboutUsItemById(Long id) {
        return aboutUsItemRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(ABOUT_US_ITEM_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public AboutUsItemResponse getAboutUsItemResponseById(Long id) {
        return dtoConverter.convertToDto(getAboutUsItemById(id), AboutUsItemResponse.class);
    }

    @Override
    public List<AboutUsItem> getListOfAboutUsItems() {
        return aboutUsItemRepository.findAllByOrderByNumberAsc();
    }

    @Override
    public List<AboutUsItemResponse> getListOfAboutUsItemResponses() {
        return aboutUsItemRepository.findAllByOrderByNumberAsc().stream()
                .map(item -> (AboutUsItemResponse) dtoConverter.convertToDto(item, AboutUsItemResponse.class))
                .toList();
    }

    @Override
    public AboutUsItemResponse addAboutUsItem(AboutUsItemProfile aboutUsItemProfile) {
        List<AboutUsItem> list = getListOfAboutUsItems();
        long number = 0;
        if (!list.isEmpty()) {
            number = list.get(list.size() - 1).getNumber() + STEP;
        }
        aboutUsItemProfile.setNumber(number);
        validateVideoUrl(aboutUsItemProfile);
        AboutUsItem aboutUsItem = dtoConverter.convertToEntity(aboutUsItemProfile, new AboutUsItem());
        return dtoConverter.convertToDto(aboutUsItemRepository.save(aboutUsItem), AboutUsItemResponse.class);
    }

    @Override
    public AboutUsItemResponse updateAboutUsItem(Long id, AboutUsItemProfile aboutUsItemProfile) {
        AboutUsItem aboutUsItem = getAboutUsItemById(id);
        if (aboutUsItemProfile.getNumber() == null) {
            aboutUsItemProfile.setNumber(aboutUsItem.getNumber());
        }
        validateVideoUrl(aboutUsItemProfile);
        log.debug(aboutUsItemProfile.toString());
        BeanUtils.copyProperties(aboutUsItemProfile, aboutUsItem);
        return dtoConverter.convertToDto(aboutUsItemRepository.save(aboutUsItem), AboutUsItemResponse.class);
    }

    @Override
    public AboutUsItemResponse deleteAboutUsItemById(Long id) {
        AboutUsItem aboutUsItem = getAboutUsItemById(id);
        aboutUsItemRepository.deleteById(id);
        aboutUsItemRepository.flush();
        archiveModel(aboutUsItem);
        return dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class);
    }

    @Override
    public String validateVideoUrl(AboutUsItemProfile aboutUsItemProfile) {
        String videoUrl = aboutUsItemProfile.getVideo();
        if (videoUrl != null) {
            int position = videoUrl.indexOf(VIDEO_PARAM);
            if (position == -1) {
                throw new IllegalArgumentException(WRONG_LINK);
            }
            videoUrl = videoUrl.substring(position + VIDEO_PARAM.length());
            int end = videoUrl.indexOf('&');
            if (end != -1) {
                videoUrl = videoUrl.substring(0, end);
            }
            videoUrl = EMBED_VIDEO_URL + videoUrl;
            aboutUsItemProfile.setVideo(videoUrl);
            log.debug(videoUrl);
        }
        return videoUrl;
    }

    @Override
    public void changeOrder(Long id, Long position) {
        List<AboutUsItem> items = getListOfAboutUsItems();
        if (items.size() <= 1) {
            return;
        }
        AboutUsItem item = getAboutUsItemById(id);

        if (position == items.size() + 1) {
            item.setNumber(items.get((int) (position - 2)).getNumber() + STEP);
        } else if (position == 1) {
            long num = STEP;
            for (AboutUsItem element : items) {
                AboutUsItemProfile profile = dtoConverter.convertToDto(element, AboutUsItemProfile.class);
                profile.setNumber(num);
                num += STEP;
                updateAboutUsItem(element.getId(), profile);
            }
            item.setNumber(1L);
        } else {
            Long up = items.get((int) (position - 1)).getNumber();
            Long down = items.get(position.intValue()).getNumber();
            if (down - up > 1) {
                Long median = (down + up) / 2;
                item.setNumber(median);
            } else {
                long num = 1L;
                for (AboutUsItem element : items) {
                    AboutUsItemProfile profile = dtoConverter.convertToDto(element, AboutUsItemProfile.class);
                    profile.setNumber(num);
                    num += STEP;
                    updateAboutUsItem(element.getId(), profile);
                }
                up = items.get((int) (position - 1)).getNumber();
                down = items.get(position.intValue()).getNumber();
                Long median = (down + up) / 2;
                item.setNumber(median);
            }
        }
        updateAboutUsItem(id, dtoConverter.convertToDto(item, AboutUsItemProfile.class));
    }

    private void archiveModel(AboutUsItem aboutUsItem) {
        archiveMQMessageProducer.publish(aboutUsItem);
    }

    @Override
    public void restoreModel(Long id) {
        var aboutUsItem = objectMapper.convertValue(
                archiveClient.restoreModel(AboutUsItem.class.getName(), id),
                AboutUsItem.class);

        aboutUsItemRepository.save(aboutUsItem);
    }
}
