package com.softserve.teachua.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubDto {
    private Long id;
    private String clubName;
    private String address;
    private String generalDescriptionOfClub;
    private String logoUrl;
    private Long childrenCenterId;
}
