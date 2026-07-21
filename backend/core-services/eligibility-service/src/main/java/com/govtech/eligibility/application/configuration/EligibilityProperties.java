package com.govtech.eligibility.application.configuration;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "eligibility")
public class EligibilityProperties {

    private Rsa rsa = new Rsa();
    private PrimeActivite primeActivite = new PrimeActivite();
    private Apl apl = new Apl();
    private Css css = new Css();
    private Ars ars = new Ars();

    @Getter
    @Setter
    public static class Rsa {

        private BigDecimal maxIncome;
        private String estimatedAmount;
        private String estimatedAmountLabel;

    }

    @Getter
    @Setter
    public static class PrimeActivite {

        private BigDecimal incomeCeiling;
        private String estimatedAmount;
        private String estimatedAmountLabel;

    }

    @Getter
    @Setter
    public static class Apl {

        private BigDecimal incomeCeiling;
        private String estimatedAmount;
        private String estimatedAmountLabel;
    }

    @Getter
    @Setter
    public static class Css {

        private BigDecimal incomeCeiling;
        private String estimatedAmount;
        private String estimatedAmountLabel;
    }

    @Getter
    @Setter
    public static class Ars {

        private BigDecimal incomeCeiling;
        private String estimatedAmount;
        private String estimatedAmountLabel;
    }

}