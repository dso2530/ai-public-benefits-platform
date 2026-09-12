package com.govtech.rag.domain.model;

import java.util.List;

public record Answer(

                String answer,

                List<RetrievedChunk> sources,

                long processingTimeMs

) {
}