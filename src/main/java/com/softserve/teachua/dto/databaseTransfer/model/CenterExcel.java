package com.softserve.teachua.dto.databaseTransfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import com.softserve.teachua.utils.validations.Phone;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CenterExcel implements Convertible {
    private Long centerExternalId;

    @NotBlank
    @Size(min = 5, max = 100, message = "Довжина назви має бути від 5 до 100 символів")
    private String name;

    @CheckRussian
    @NotEmpty
    private String description;

    @Phone
    @NotBlank
    private String phone;
    // site field can include social media too
    private String site;

    // private String city;
    // private String address;
    // private Double longitude;
    // private Double latitude;
    // private String district;
    // private String station;
}
