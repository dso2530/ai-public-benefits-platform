CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE knowledge_document
(
    id BIGSERIAL PRIMARY KEY,

    document_id BIGINT NOT NULL,

    source VARCHAR(100) NOT NULL,

    territory_code VARCHAR(20),

    document_type VARCHAR(100) NOT NULL,

    title VARCHAR(500),

    checksum VARCHAR(64),

    indexed_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE knowledge_chunk
(
    id BIGSERIAL PRIMARY KEY,

    knowledge_document_id BIGINT NOT NULL
        REFERENCES knowledge_document(id)
        ON DELETE CASCADE,

    chunk_number INTEGER NOT NULL,

    content TEXT NOT NULL,

    embedding VECTOR(768) NOT NULL,

    metadata JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_knowledge_document_document
    ON knowledge_document(document_id);

CREATE INDEX idx_knowledge_document_source
    ON knowledge_document(source);

CREATE INDEX idx_knowledge_document_territory
    ON knowledge_document(territory_code);

CREATE UNIQUE INDEX uk_knowledge_document_checksum
    ON knowledge_document(checksum);

CREATE INDEX idx_knowledge_chunk_document
    ON knowledge_chunk(knowledge_document_id);

CREATE INDEX idx_knowledge_chunk_embedding
    ON knowledge_chunk
    USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 100);