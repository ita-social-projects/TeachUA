package com.softserve.teachua.dto.gallery;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class GalleryPhotoProfile implements Convertible {
    private String urlGallery;
}
