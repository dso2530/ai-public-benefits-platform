package com.govtech.profile.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CafInformation(

        String beneficiaryNumber,
        String departmentCode,

        Boolean receivesFamilyBenefits,
        Boolean receivesHousingBenefits,
        Boolean receivesRsa,
        Boolean receivesAah,
        Boolean receivesPrimeActivite,

        BigDecimal monthlyAmount,
        LocalDate rightsStartDate,
        LocalDate rightsEndDate

) implements Mergeable<CafInformation> {

    @Override
    public CafInformation merge(CafInformation incoming) {

        if (incoming == null) {
            return this;
        }

        return new CafInformation(
                merge(beneficiaryNumber, incoming.beneficiaryNumber()),
                merge(departmentCode, incoming.departmentCode()),
                merge(receivesFamilyBenefits, incoming.receivesFamilyBenefits()),
                merge(receivesHousingBenefits, incoming.receivesHousingBenefits()),
                merge(receivesRsa, incoming.receivesRsa()),
                merge(receivesAah, incoming.receivesAah()),
                merge(receivesPrimeActivite, incoming.receivesPrimeActivite()),
                merge(monthlyAmount, incoming.monthlyAmount()),
                merge(rightsStartDate, incoming.rightsStartDate()),
                merge(rightsEndDate, incoming.rightsEndDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}