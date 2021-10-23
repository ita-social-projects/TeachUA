package com.softserve.teachua.dto.about_us_page;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AboutUsPageResponse implements Convertible {

    private Long id;

    private String topic;

    private String title;

    private String subtitle;

    private String picture;

}
