package com.govtech.knowledge.infrastructure.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.govtech.knowledge.domain.model.RetrievedChunk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PgVectorRepository {

    private final JdbcTemplate jdbc;

    private final ObjectMapper objectMapper;

    @Value("${knowledge.search.min-score:0.65}")
    private double minScore;

    public List<RetrievedChunk> search(
            float[] queryEmbedding,
            String territoryCode,
            int limit) {

        return jdbc.query(
                """
                        SELECT
                            kd.document_id,
                            kd.territory_code,
                            kc.chunk_number,
                            kc.content,
                            kc.metadata,
                            1 - (ke.embedding <=> ?::vector) AS score

                        FROM knowledge_embedding ke

                        JOIN knowledge_chunk kc
                            ON kc.id = ke.chunk_id

                        JOIN knowledge_document kd
                            ON kd.id = kc.knowledge_document_id

                        WHERE (
                            ? IS NULL
                            OR kd.territory_code = ?
                        )

                        AND 1 - (ke.embedding <=> ?::vector) >= ?

                        ORDER BY
                            ke.embedding <=> ?::vector

                        LIMIT ?
                        """,

                (rs, rowNum) -> {

                    Map<String, String> metadata = readMetadata(rs.getString("metadata"));

                    String documentTerritory = rs.getString("territory_code");

                    if (documentTerritory != null) {
                        metadata.put(
                                "territoryCode",
                                documentTerritory);
                    }

                    return new RetrievedChunk(
                            rs.getLong("document_id"),
                            rs.getInt("chunk_number"),
                            rs.getString("content"),
                            rs.getDouble("score"),
                            metadata);
                },

                vectorToSql(queryEmbedding),

                territoryCode,
                territoryCode,

                vectorToSql(queryEmbedding),

                0.65,

                vectorToSql(queryEmbedding),

                limit);
    }

    private Map<String, String> readMetadata(String json) {

        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }

        try {

            Map<String, String> metadata = objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, String>>() {
                    });

            return new HashMap<>(metadata);

        } catch (Exception e) {

            log.warn(
                    "Unable to parse metadata json={}",
                    json,
                    e);

            return new HashMap<>();
        }
    }

    public void saveEmbedding(
            Long chunkId,
            float[] vector) {

        jdbc.update(
                """
                        INSERT INTO knowledge_embedding(
                            chunk_id,
                            embedding
                        )
                        VALUES (?, ?::vector)
                        """,
                chunkId,
                vectorToSql(vector));
    }

    private String vectorToSql(float[] vector) {

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < vector.length; i++) {

            if (i > 0) {
                builder.append(",");
            }

            builder.append(vector[i]);
        }

        builder.append("]");

        return builder.toString();
    }
}