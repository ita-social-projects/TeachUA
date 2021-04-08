package com.softserve.teachua.dto.databaseTransfer;

import com.softserve.teachua.dto.databaseTransfer.model.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelParsingData {
    private final List<ClubExcel> clubs = new ArrayList<>();
    private final List<CenterExcel> centers = new ArrayList<>();
    private final List<CategoryExcel> categories = new ArrayList<>();
    private final List<StationExcel> stations = new ArrayList<>();
    private final List<DistrictExcel> districts = new ArrayList<>();
}
