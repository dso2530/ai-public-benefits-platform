package com.govtech.profile.domain.valueobject;

public record HousingInformation(
        String occupancyStatus,
        String housingType,
        Integer householdSize,
        Double housingArea,
        Double monthlyRent,
        Double monthlyCharges,
        Boolean aplRecipient)
        implements Mergeable<HousingInformation> {

    @Override
    public HousingInformation merge(HousingInformation incoming) {

        if (incoming == null) {
            return this;
        }

        return new HousingInformation(
                merge(occupancyStatus, incoming.occupancyStatus()),
                merge(housingType, incoming.housingType()),
                merge(householdSize, incoming.householdSize()),
                merge(housingArea, incoming.housingArea()),
                merge(monthlyRent, incoming.monthlyRent()),
                merge(monthlyCharges, incoming.monthlyCharges()),
                merge(aplRecipient, incoming.aplRecipient()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}