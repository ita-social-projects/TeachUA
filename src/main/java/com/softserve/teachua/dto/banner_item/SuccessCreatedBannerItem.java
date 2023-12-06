package com.softserve.teachua.dto.banner_item;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedBannerItem implements Convertible {
    private Long id;
    private String title;
    private String subtitle;
    private String link;
    private String picture;
    private Integer sequenceNumber;
}
