package com.softserve.teachua.dto.database_transfer;

import com.softserve.teachua.dto.database_transfer.model.CategoryExcel;
import com.softserve.teachua.dto.database_transfer.model.CenterExcel;
import com.softserve.teachua.dto.database_transfer.model.ClubExcel;
import com.softserve.teachua.dto.database_transfer.model.DistrictExcel;
import com.softserve.teachua.dto.database_transfer.model.LocationExcel;
import com.softserve.teachua.dto.database_transfer.model.StationExcel;
import com.softserve.teachua.model.ExcelCenterEntity;
import com.softserve.teachua.model.ExcelClubEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelParsingData {
    private final List<ClubExcel> clubs = new ArrayList<>();
    private final List<CenterExcel> centers = new ArrayList<>();
    private final List<CategoryExcel> categories = new ArrayList<>();
    private final List<StationExcel> stations = new ArrayList<>();
    private final List<DistrictExcel> districts = new ArrayList<>();
    private final List<LocationExcel> locations = new ArrayList<>();

    private final List<ExcelCenterEntity> excelCenters = new ArrayList<>();
    private final List<ExcelClubEntity> excelClubs = new ArrayList<>();
}
