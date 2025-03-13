
package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.inject.Singleton;

public class PersistenceModule extends AbstractModule {

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("defaultPersistenceUnit");
    }

    @Provides
    public EntityManager provideEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
}