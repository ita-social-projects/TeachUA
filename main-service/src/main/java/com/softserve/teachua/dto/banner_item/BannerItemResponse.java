package com.softserve.teachua.dto.banner_item;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BannerItemResponse implements Convertible {
    private Long id;
    private String title;
    private String subtitle;
    private String link;
    private String picture;
    private Integer sequenceNumber;
}
