package com.govtech.connectors.common.spi;

import java.util.List;

import com.govtech.connectors.common.model.DocumentPage;

public interface KnowledgeConnector
                extends Connector {

        /**
         * Explore une source documentaire.
         */
        List<DocumentPage> crawl();

}