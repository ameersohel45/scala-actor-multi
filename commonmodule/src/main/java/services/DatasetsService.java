

package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.*;
import models.Datasets;
import repositories.DatasetsRepository;
import utils.ResponseBuilder;
import utils.Validator;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatasetsService {

    @PersistenceContext
    private EntityManager entityManager;

    private final DatasetsRepository dataRepository;
    private final ObjectMapper objectMapper;

    @Inject
    public DatasetsService(DatasetsRepository dataRepository, ObjectMapper objectMapper, EntityManager entityManager) {
        this.dataRepository = dataRepository;
        this.objectMapper = objectMapper;
        this.entityManager = entityManager;
    }

    public Map<String, Object> getAllDatasets() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Datasets> datasets = entityManager.createQuery("SELECT d FROM Datasets d", Datasets.class).getResultList();
            return ResponseBuilder.buildResponseOnget("Success", 201, "Data retrieved successfully", datasets);

        } catch (PersistenceException e) {
            response.put("status", 503);
            response.put("message", "Database is currently unavailable. Please try again later.");
        }
        return response;
    }

    public Map<String, Object> getDatasetById(String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Datasets dataset = entityManager.find(Datasets.class, id);
            if (dataset != null) {
                return ResponseBuilder.buildResponseOngetById("Success", 200, "Dataset retrieved successfully", dataset);
            } else {
                return ResponseBuilder.buildResponse(id, "Failure", 404, "Dataset not found for ID: " + id);
            }
        } catch (PersistenceException e) {
            return ResponseBuilder.buildResponse(id, "Failure", 404, "Database is currently unavailable. Please try again later.");
        }
    }

    @Transactional
    public Map<String, Object> createDataset(String json) {
        Map<String, Object> response = new HashMap<>();
        Datasets dataset = null;

        try {

                dataset = objectMapper.readValue(json, Datasets.class);


            Optional<String> validationError = Validator.validate(dataset);
            if (validationError.isPresent()) {
                return ResponseBuilder.buildResponse(dataset.getId(), "Failure", 400, validationError.get());
            }

            if (entityManager.find(Datasets.class, dataset.getId()) != null) {
                return ResponseBuilder.buildResponse(dataset.getId(), "Failure", 409, "Dataset with same Id already exists");
            }
            dataset.setUpdatedBy("admin");
            dataset.setCreatedAt(LocalDateTime.now());
            dataset.setUpdatedAt(LocalDateTime.now());
            //entityManager.persist(dataset);
            dataRepository.save(dataset);

        } catch (InvalidFormatException e) {
            return ResponseBuilder.buildResponse("id", "Failure", 400, "Status should be Live, Draft, or RETIRED");
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(dataset.getId(), "Failure", 400, "Check Your Request Body");

        }
        String id = dataset.getId();
        return ResponseBuilder.buildResponse(id, "Success", 201, "Data added Successfully");
    }

    @Transactional
    public Map<String, Object> updateDataset( String json) {
        Map<String, Object> response = new HashMap<>();
        Datasets updates = null;
        try {
            
             updates = objectMapper.readValue(json, Datasets.class);
            Datasets existingDataset = entityManager.find(Datasets.class, updates.getId());
            if (existingDataset == null) {
                return ResponseBuilder.buildResponse(updates.getId(), "Failure", 404, "Dataset not found for ID: " + updates.getId());
            }
            
            Optional<String> validationError = Validator.validateForUpdate(updates);
            if (validationError.isPresent()) {
                return ResponseBuilder.buildResponse(updates.getId(), "Failure", 404, validationError.get());
            }
            if (updates.getDataSchema() != null) existingDataset.setDataSchema(updates.getDataSchema());
            if (updates.getRouteConfig() != null) existingDataset.setRouteConfig(updates.getRouteConfig());
            if (updates.getStatus() != null) existingDataset.setStatus(updates.getStatus());
            if (updates.getUpdatedBy() != null) existingDataset.setUpdatedBy(updates.getUpdatedBy());

            existingDataset.setUpdatedAt(LocalDateTime.now());
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(existingDataset);
            entityManager.getTransaction().commit();
            return ResponseBuilder.buildResponse(updates.getId(), "Success", 200, "Dataset updated successfully");
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(updates.getId(), "Failure", 400, "Check Request Body");
        }
    }

    @Transactional
    public Map<String, Object> deleteDataset(String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Datasets dataset = entityManager.find(Datasets.class, id);
            if (dataset != null) {
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                entityManager.remove(dataset);
                // entityManager.persist(dataset);
                entityManager.getTransaction().commit();
                return ResponseBuilder.buildResponse(id, "Success", 200, "Dataset deleted successfully");

            } else {
                return ResponseBuilder.buildResponse(id, "Failure", 404, "Dataset with ID " + id + " not found");

            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(id, "Failure", 404, "Error deleting record");

        }
    }
}
