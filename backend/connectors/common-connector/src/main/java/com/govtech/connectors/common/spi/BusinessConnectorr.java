package com.govtech.connectors.common.spi;

import com.govtech.connectors.common.model.ConnectorResponse;

public interface BusinessConnectorr<I, O>
                extends Connector {

        /**
         * Exécute une requête métier.
         */
        ConnectorResponse<O> execute(I request);

}