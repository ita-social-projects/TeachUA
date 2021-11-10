package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.AboutUsItem;
import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.service.AboutUsItemService;
import com.softserve.teachua.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AboutUsItemServiceImpl implements AboutUsItemService {
    private static final String ABOUT_US_ITEM_ALREADY_EXIST = "AboutUsItem with number: %s already exist";
    private static final String ABOUT_US_ITEM_NOT_FOUND_BY_ID = "AboutUsItem was not found by id: %s";
    private static final String ABOUT_US_ITEM_NULL_FIELD_ERROR = "AboutUsItem cannot exist without \"%s\"";
    private static final String VIDEO_PARAM = "watch?v=";
    private static final String EMBED_VIDEO_URL = "https://www.youtube.com/embed/";
    private static final Long STEP = 20L;

    private final AboutUsItemRepository aboutUsItemRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;

    @Autowired
    public AboutUsItemServiceImpl(
            AboutUsItemRepository aboutUsItemRepository,
            ArchiveService archiveService,
            DtoConverter dtoConverter
    ) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
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
        List<AboutUsItemResponse> aboutUsItemResponses = aboutUsItemRepository.findAllByOrderByNumberAsc()
                .stream()
                .map(item -> (AboutUsItemResponse)dtoConverter.convertToDto(item, AboutUsItemResponse.class))
                .collect(Collectors.toList());
        return aboutUsItemResponses;
    }

    @Override
    public AboutUsItemResponse addAboutUsItem(AboutUsItemProfile aboutUsItemProfile) {
        List<AboutUsItem> list = getListOfAboutUsItems();
        long number = 0;
        if(!list.isEmpty()){
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
        if(aboutUsItemProfile.getNumber() == null){
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
        return dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class);
    }

    @Override
    public void validateVideoUrl(AboutUsItemProfile aboutUsItemProfile) {
        String video_url = aboutUsItemProfile.getVideo();
        if(video_url != null) {
            int position = video_url.indexOf(VIDEO_PARAM);
            if(position != -1) {
                video_url = video_url.substring(position + VIDEO_PARAM.length());
                int end = video_url.indexOf('&');
                if(end != -1){
                    video_url = video_url.substring(0, end);
                }
                video_url = EMBED_VIDEO_URL + video_url;
                aboutUsItemProfile.setVideo(video_url);
                log.debug(video_url);
            }
        }
    }

    @Override
    public void changeOrder(Long id, Long position) {
        List<AboutUsItem> items = getListOfAboutUsItems();
        AboutUsItem item = getAboutUsItemById(id);
        if(items.size() > 1) {
            if (position == items.size() + 1) {
                item.setNumber(items.get((int) (position - 2)).getNumber() + STEP);
            } else if (position == 1) {
                Long num = STEP;
                for (AboutUsItem fItem : items) {
                    AboutUsItemProfile profile = dtoConverter.convertToDto(fItem, AboutUsItemProfile.class);
                    profile.setNumber(num);
                    num += STEP;
                    updateAboutUsItem(fItem.getId(), profile);
                }
                item.setNumber(1L);
            } else {
                Long up = items.get((int) (position - 1)).getNumber();
                Long down = items.get(position.intValue()).getNumber();
                if (down - up > 1) {
                    Long median = (down + up) / 2;
                    item.setNumber(median);
                } else {
                    Long num = 1L;
                    for (AboutUsItem fItem : items) {
                        AboutUsItemProfile profile = dtoConverter.convertToDto(fItem, AboutUsItemProfile.class);
                        profile.setNumber(num);
                        num += STEP;
                        updateAboutUsItem(fItem.getId(), profile);
                    }
                    up = items.get((int) (position - 1)).getNumber();
                    down = items.get(position.intValue()).getNumber();
                    Long median = (down + up) / 2;
                    item.setNumber(median);
                }
            }
            updateAboutUsItem(id, dtoConverter.convertToDto(item, AboutUsItemProfile.class));
        }
    }
}
