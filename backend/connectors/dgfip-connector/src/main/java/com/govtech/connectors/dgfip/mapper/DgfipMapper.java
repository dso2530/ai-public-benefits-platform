package com.govtech.connectors.dgfip.mapper;

import org.mapstruct.Mapper;

import com.govtech.connectors.dgfip.dto.DgfipIncomeResponse;
import com.govtech.connectors.dgfip.dto.DgfipTaxNoticeResponse;
import com.govtech.shared.model.Income;
import com.govtech.shared.model.TaxNotice;

@Mapper(componentModel = "spring")
public interface DgfipMapper {

    Income toIncome(DgfipIncomeResponse response);

    TaxNotice toTaxNotice(DgfipTaxNoticeResponse response);
}
