package com.softserve.teachua.dto.about_us_item;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AboutUsItemResponse implements Convertible {

    private Long id;

    private String text;

    private String picture;

    private String video;

    private Long type;

}
