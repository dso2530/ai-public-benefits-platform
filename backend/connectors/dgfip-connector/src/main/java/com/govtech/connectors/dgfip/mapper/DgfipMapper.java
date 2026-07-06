package com.govtech.connectors.dgfip.mapper;

import com.govtech.connectors.dgfip.dto.DgfipIncomeResponse;
import com.govtech.connectors.dgfip.dto.DgfipTaxNoticeResponse;
import com.govtech.shared.model.Income;
import com.govtech.shared.model.TaxNotice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DgfipMapper {

  Income toIncome(DgfipIncomeResponse response);

  TaxNotice toTaxNotice(DgfipTaxNoticeResponse response);
}
