package com.govtech.rag.domain.port;


import java.util.List;

import com.govtech.rag.domain.model.RetrievedChunk;


public interface KnowledgeSearchPort {


    List<RetrievedChunk> search(

            float[] embedding,

            String territoryCode,

            int limit);

}