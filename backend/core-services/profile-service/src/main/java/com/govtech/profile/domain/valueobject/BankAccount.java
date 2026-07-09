package com.govtech.profile.domain.valueobject;

public record BankAccount(
        String iban,
        String bic)
        implements Mergeable<BankAccount> {

    @Override
    public BankAccount merge(BankAccount incoming) {

        if (incoming == null) {
            return this;
        }

        return new BankAccount(
                merge(iban, incoming.iban()),
                merge(bic, incoming.bic()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}