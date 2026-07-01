package com.govtech.connectors.dgfip.client;

import com.govtech.connectors.dgfip.TestApplication;
import com.govtech.connectors.dgfip.dto.DgfipIncomeResponse;
import com.govtech.connectors.dgfip.dto.DgfipTaxNoticeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
class DgfipClientIT {

    @Autowired
    private DgfipClient client;

    @Test
    void shouldRetrieveIncome() {

        DgfipIncomeResponse response = client.getIncome("123456789");

        assertThat(response).isNotNull();
        assertThat(response.fiscalYear()).isEqualTo("2025");
        assertThat(response.annualIncome()).isPositive();
    }

    @Test
    void shouldRetrieveLatestTaxNotice() {

        DgfipTaxNoticeResponse response = client.getLatestTaxNotice("123456789");

        assertThat(response).isNotNull();
        assertThat(response.referenceNumber())
                .startsWith("AV-");
    }

}