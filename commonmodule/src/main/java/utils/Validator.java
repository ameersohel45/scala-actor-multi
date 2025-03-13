package utils;


import models.Datasets;

import java.util.Optional;

public class Validator {

    public static Optional<String> validate(Datasets dataset) {
        if (dataset.getId() == null || dataset.getId().isBlank() || dataset.getId().isEmpty()) {
            return Optional.of("ID cannot be null or empty");
        }

        if (dataset.getDataSchema() == null || dataset.getDataSchema().values().isEmpty() || dataset.getDataSchema().isEmpty()) {
            return Optional.of("Data schema cannot be null or empty");
        }if (dataset.getStatus() == null || dataset.getStatus().toString().isBlank()) {
            return Optional.of("Status required : Live, Draft, RETIRED");
        }
        if (dataset.getRouteConfig() == null || dataset.getRouteConfig().isEmpty() || dataset.getRouteConfig().values().isEmpty()) {
            return Optional.of("Route config cannot be null or empty");
        }
//        if (dataset.getUpdatedBy() == null ) {
//            return Optional.of("UpdatedBy cannot be null or empty");
//        }

        if (dataset.getCreatedBy() == null) {
            return Optional.of("CreatedBy field required in request body ");
        }

        if (dataset.getCreatedBy().matches(".*\\d.*") || dataset.getCreatedBy().isBlank()) {
            return Optional.of("CreatedBy : Give proper name  ");
        }

//        if (dataset.getUpdatedBy().matches(".*\\d.*") || dataset.getUpdatedBy().isBlank()) {
//            return Optional.of("UpdatedBy : Give proper name ");
//        }
        return Optional.empty();
    }

    private static boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Live") ||
                status.equalsIgnoreCase("Draft") ||
                status.equalsIgnoreCase("RETIRED");
    }

    public static Optional<String> validateForUpdate(Datasets updates) {
        if (updates.getDataSchema() == null || updates.getDataSchema().isEmpty()
                || updates.getDataSchema().values().isEmpty()) {
            return Optional.of("dataSchema is required.");
        }

        if (updates.getRouteConfig() == null || updates.getRouteConfig().isEmpty()
                || updates.getRouteConfig().values().isEmpty()) {
            return Optional.of("Route config key & values are needed.");
        }

        if (updates.getStatus() == null || updates.getStatus().toString().isBlank()) {
            return Optional.of("Status is required.");
        }


        if (updates.getUpdatedBy() == null) {
            return Optional.of("UpdatedBy field required in request body");
        }


        if (updates.getUpdatedBy().matches(".*\\d.*") || updates.getUpdatedBy().isBlank()) {
            return Optional.of("UpdatedBy : should be name ");
        }
        if (updates.getCreatedBy() != null) {
            return Optional.of("Created by cannot be changed so remove from request body ");
        }


        return Optional.empty();
    }
}
