/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.operatorapi.viewmodel.persistence.repositories;

import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ChargingStationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public ChargingStation createOrUpdate(ChargingStation chargingStation) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            ChargingStation persistedChargingStation = entityManager.merge(chargingStation);

            tx.commit();

            return persistedChargingStation;
        } catch (Exception e) {
            LOG.error("Exception while trying to persist chargingStation.", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public ChargingStation findOne(String id) {
        return getEntityManager().find(ChargingStation.class, id);
    }

    public List<ChargingStation> findAll(int offset, int limit) {
        return getEntityManager().createQuery("SELECT cs FROM io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation AS cs", ChargingStation.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long getTotalNumberOfChargingStations() {
        return getEntityManager().createQuery("SELECT COUNT(cs) FROM io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation cs", Long.class).getSingleResult();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}