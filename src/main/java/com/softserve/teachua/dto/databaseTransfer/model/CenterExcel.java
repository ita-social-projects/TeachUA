package com.softserve.teachua.dto.databaseTransfer.model;

import com.softserve.teachua.dto.club.validation.Phone;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CenterExcel implements Convertible {

    private Long centerExternalId;

    @NotNull @NotEmpty
    @Size(
            min = 5,
            max = 100,
            message = "Довжина назви має бути від 5 до 100 символів")
    @Pattern(
            regexp = "^[А-Яа-яіІєЄїЇґҐ\\'a-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$" ,
            message = "Це поле може містити тільки українські та англійські літери, цифри та спеціальні символи’")
    private String name;

    @NotNull @NotEmpty
    private String description;

    @Valid
    @Phone
    @NotNull @NotEmpty
    private String phone;
    // site field can include social media too
    private String site;

//    private String city;
//    private String address;
//    private Double longitude;
//    private Double latitude;
//    private String district;
//    private String station;

}
