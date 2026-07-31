package com.govtech.connectors.infrastructure.datagouv;

import java.util.List;

public record DataGouvDatasetResponse(

                List<Dataset> data

) {

        public record Dataset(

                        String id,

                        String title,

                        String description,

                        String page,

                        List<Resource> resources

        ) {

                public record Resource(

                                String id,

                                String title,

                                String url,

                                String format,

                                Long filesize,

                                String mime

                ) {
                }

        }

}