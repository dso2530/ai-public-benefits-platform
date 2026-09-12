-- V2__create_knowledge_embedding.sql

CREATE TABLE knowledge_embedding
(
    id BIGSERIAL PRIMARY KEY,

    chunk_id BIGINT NOT NULL,

    embedding vector(768) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_knowledge_embedding_chunk
        FOREIGN KEY (chunk_id)
        REFERENCES knowledge_chunk(id)
        ON DELETE CASCADE
);


CREATE INDEX idx_knowledge_embedding_chunk
    ON knowledge_embedding(chunk_id);