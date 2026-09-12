CREATE INDEX idx_documents_source
    ON documents(source);


CREATE INDEX idx_documents_origin
    ON documents(origin);


CREATE INDEX idx_documents_connector_name
    ON documents(connector_name);


CREATE INDEX idx_documents_territory_code
    ON documents(territory_code);