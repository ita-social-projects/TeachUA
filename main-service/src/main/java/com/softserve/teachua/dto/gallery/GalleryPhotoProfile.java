package com.softserve.teachua.dto.gallery;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class GalleryPhotoProfile implements Convertible {
    private String urlGallery;
}
