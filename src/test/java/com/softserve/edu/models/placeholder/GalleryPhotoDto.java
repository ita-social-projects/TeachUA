package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GalleryPhotoDto extends BaseDto {

    private Integer id;
    private String url;
    private ClubResponseDto[] club;

    public int getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ClubResponseDto[] getClub() {
        return this.club;
    }

    public void setClub(ClubResponseDto[] club) {
        this.club = club;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GalleryPhotoDto galleryPhotoDto = (GalleryPhotoDto) o;
        return new EqualsBuilder()
                .append(id, galleryPhotoDto.id)
                .append(url, galleryPhotoDto.url)
                .append(club, galleryPhotoDto.club)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(url)
                .append(club)
                .toHashCode();
    }
}
