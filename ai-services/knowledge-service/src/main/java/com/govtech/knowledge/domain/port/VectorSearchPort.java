package com.govtech.knowledge.domain.port;


import java.util.List;

import com.govtech.knowledge.domain.model.RetrievedChunk;


public interface VectorSearchPort {


    List<RetrievedChunk> search(

            float[] embedding,

            String territoryCode,

            int limit

    );

}