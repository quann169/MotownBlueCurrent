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
package io.motown.ochp.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationAcceptedEvent;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.ochp.viewmodel.OchpEventHandler;
import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:ochp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OchpEventHandlerTest {

    private OchpEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository.deleteAll();
        chargingStationRepository.deleteAll();

        eventHandler = new OchpEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
        eventHandler.setTransactionRepository(transactionRepository);
    }

    @Test
    public void testTransaction() {
        chargingStationRepository.save(new ChargingStation(CHARGING_STATION_ID.getId()));
        Transaction transaction = new Transaction(chargingStationRepository.findByChargingStationId(CHARGING_STATION_ID.getId()), TRANSACTION_ID.getId());
        transactionRepository.save(transaction);
        Transaction transactionFromRepo = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transactionFromRepo);
        assertEquals(transaction, transactionFromRepo);
    }

    @Test
    public void testTransactionWithoutChargingStation() {
        Transaction transaction = new Transaction(TRANSACTION_ID.getId());
        transactionRepository.save(transaction);
        Transaction transactionFromRepo = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transactionFromRepo);
        assertEquals(transaction, transactionFromRepo);
    }

    @Test
    public void testHandleTransactionStartedEvent() {
        eventHandler.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, FIVE_MINUTES_AGO, CONFIGURATION_ITEMS));
    }

    @Test
    public void testHandleTransactionStoppedEvent() {
        eventHandler.handle(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, TWO_MINUTES_AGO));
    }

    @Test
    public void testHandleChargingStationAcceptedEvent() {
        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));
    }

    @Test
    public void testStartStopTransaction() {
        ChargingStation chargingStation = new ChargingStation(CHARGING_STATION_ID.getId());
        chargingStationRepository.save(chargingStation);

        eventHandler.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, FIVE_MINUTES_AGO, CONFIGURATION_ITEMS));
        Transaction transaction = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transaction);
        assertEquals(TRANSACTION_ID.getId(), transaction.getTransactionId());
        assertEquals(EVSE_ID.getId(), transaction.getEvseId());
        assertEquals(IDENTIFYING_TOKEN.getToken(), transaction.getIdentifyingToken());
        assertEquals(METER_START, transaction.getMeterStart());
        assertEquals(FIVE_MINUTES_AGO.getTime(), transaction.getTimeStart().getTime());
        assertTrue(transaction.getTimeStart().compareTo(FIVE_MINUTES_AGO) == 0);
        assertEquals(CONFIGURATION_ITEMS, transaction.getAttributes());
        assertEquals(TransactionStatus.STARTED, transaction.getStatus());
        assertEquals(chargingStation, transaction.getChargingStation());

        eventHandler.handle(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, TWO_MINUTES_AGO));
        Transaction transactionStopped = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transactionStopped);
        assertEquals(TRANSACTION_ID.getId(), transactionStopped.getTransactionId());
        assertEquals(EVSE_ID.getId(), transactionStopped.getEvseId());
        assertEquals(IDENTIFYING_TOKEN.getToken(), transactionStopped.getIdentifyingToken());
        assertEquals(METER_START, transactionStopped.getMeterStart());
        assertEquals(FIVE_MINUTES_AGO.getTime(), transactionStopped.getTimeStart().getTime());
        assertTrue(transactionStopped.getTimeStart().compareTo(FIVE_MINUTES_AGO) == 0);
        assertEquals(CONFIGURATION_ITEMS, transactionStopped.getAttributes());
        assertEquals(chargingStation, transactionStopped.getChargingStation());
        assertEquals(METER_STOP, transactionStopped.getMeterStop());
        assertEquals(TWO_MINUTES_AGO.getTime(), transactionStopped.getTimeStop().getTime());
        assertTrue(transactionStopped.getTimeStop().compareTo(TWO_MINUTES_AGO) == 0);
        assertEquals(TransactionStatus.STOPPED, transactionStopped.getStatus());
        assertEquals(transaction, transactionStopped);
    }

    @Test
    public void testStartStopTransactionWithoutChargingStation() {
        eventHandler.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, FIVE_MINUTES_AGO, CONFIGURATION_ITEMS));
        Transaction transaction = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transaction);
        assertEquals(TRANSACTION_ID.getId(), transaction.getTransactionId());
        assertEquals(EVSE_ID.getId(), transaction.getEvseId());
        assertEquals(IDENTIFYING_TOKEN.getToken(), transaction.getIdentifyingToken());
        assertEquals(METER_START, transaction.getMeterStart());
        assertEquals(FIVE_MINUTES_AGO.getTime(), transaction.getTimeStart().getTime());
        assertTrue(transaction.getTimeStart().compareTo(FIVE_MINUTES_AGO) == 0);
        assertEquals(CONFIGURATION_ITEMS, transaction.getAttributes());
        assertEquals(TransactionStatus.STARTED, transaction.getStatus());
        assertNull(transaction.getChargingStation());

        eventHandler.handle(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, TWO_MINUTES_AGO));
        Transaction transactionStopped = transactionRepository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transactionStopped);
        assertEquals(TRANSACTION_ID.getId(), transactionStopped.getTransactionId());
        assertEquals(EVSE_ID.getId(), transactionStopped.getEvseId());
        assertEquals(IDENTIFYING_TOKEN.getToken(), transactionStopped.getIdentifyingToken());
        assertEquals(METER_START, transactionStopped.getMeterStart());
        assertEquals(FIVE_MINUTES_AGO.getTime(), transactionStopped.getTimeStart().getTime());
        assertTrue(transactionStopped.getTimeStart().compareTo(FIVE_MINUTES_AGO) == 0);
        assertEquals(CONFIGURATION_ITEMS, transactionStopped.getAttributes());
        assertNull(transactionStopped.getChargingStation());
        assertEquals(METER_STOP, transactionStopped.getMeterStop());
        assertEquals(TWO_MINUTES_AGO.getTime(), transactionStopped.getTimeStop().getTime());
        assertTrue(transactionStopped.getTimeStop().compareTo(TWO_MINUTES_AGO) == 0);
        assertEquals(TransactionStatus.STOPPED, transactionStopped.getStatus());
        assertEquals(transaction, transactionStopped);
    }

    @Test(expected = PersistenceException.class)
    public void testDuplicateTransactionId() {
        Transaction transaction = new Transaction(TRANSACTION_ID.getId());
        transactionRepository.save(transaction);
        transaction = new Transaction(TRANSACTION_ID.getId());
        transactionRepository.save(transaction);
    }

    @Test(expected = PersistenceException.class)
    public void testDuplicateChargingStationId() {
        ChargingStation chargingStation = new ChargingStation(CHARGING_STATION_ID.getId());
        chargingStationRepository.save(chargingStation);
        chargingStation = new ChargingStation(CHARGING_STATION_ID.getId());
        chargingStationRepository.save(chargingStation);
    }

    @Test
    public void testFindTransactionsByStatus() {
        Transaction t1 = new Transaction("t1");
        t1.setStatus(TransactionStatus.STARTED);
        Transaction t2 = new Transaction("t2");
        t2.setStatus(TransactionStatus.STARTED);
        Transaction t3 = new Transaction("t3");
        t3.setStatus(TransactionStatus.STOPPED);
        Transaction t4 = new Transaction("t4");
        t4.setStatus(TransactionStatus.STOPPED);
        Transaction t5 = new Transaction("t5");
        t5.setStatus(TransactionStatus.STOPPED);

        List<Transaction> startedTransactions = Arrays.asList(t1, t2);
        List<Transaction> stoppedTransactions = Arrays.asList(t3, t4, t5);
        transactionRepository.save(startedTransactions);
        transactionRepository.save(stoppedTransactions);

        List<Transaction> startedTransactionsFromRepo = transactionRepository.findTransactionsByStatus(TransactionStatus.STARTED);
        List<Transaction> stoppedTransactionsFromRepo = transactionRepository.findTransactionsByStatus(TransactionStatus.STOPPED);

        assertEquals(startedTransactions.size(), startedTransactionsFromRepo.size());
        assertEquals(stoppedTransactions.size(), stoppedTransactionsFromRepo.size());
        assertArrayEquals(startedTransactions.toArray(), startedTransactionsFromRepo.toArray());
        assertArrayEquals(stoppedTransactions.toArray(), stoppedTransactionsFromRepo.toArray());
    }

    @Test
    public void testChargingStation() {
        ChargingStation chargingStation = new ChargingStation(CHARGING_STATION_ID.getId());
        chargingStationRepository.save(chargingStation);

        ChargingStation chargingStationFromRepo = chargingStationRepository.findByChargingStationId(CHARGING_STATION_ID.getId());
        assertNotNull(chargingStationFromRepo);
        assertEquals(chargingStation, chargingStationFromRepo);
    }

}