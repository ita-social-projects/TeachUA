package com.softserve.teachua.dto.location;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressProfile {

    private  Long id;
    private  String addressText;
    private  String realCity;

}
