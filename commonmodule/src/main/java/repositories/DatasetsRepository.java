package repositories;

import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.Datasets;


import java.util.List;
import java.util.Optional;

public class DatasetsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public DatasetsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Datasets dataset) {
        if(!entityManager.getTransaction().isActive()){
            entityManager.getTransaction().begin();
        }
        entityManager.persist(dataset);
        entityManager.getTransaction().commit();
    }

    public Optional<Datasets> findById(String id) {
        Datasets dataset = entityManager.find(Datasets.class, id);
        return Optional.ofNullable(dataset);
    }

    public List<Datasets> findAll() {
        TypedQuery<Datasets> query = entityManager.createQuery("SELECT d FROM Datasets d", Datasets.class);
        return query.getResultList();
    }
}
