package com.aoher.service;

import com.aoher.api.TransactionService;
import com.aoher.exception.TransactionNotFoundException;
import com.aoher.model.CalculationData;
import com.aoher.model.ResponseState;
import com.aoher.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private Transaction transaction1;

    private static final int TRANSACTION_ID_11 = 11;
    private static final int TRANSACTION_ID_12 = 12;

    @Before
    public void setUp() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        Context context = new InitialContext(props);
        transactionService = (TransactionService) context.lookup("TransactionServiceBeanLocalBean");
        assertNotNull(transactionService);

        transaction1 = new Transaction(10000, "cars");
        Transaction transaction2 = new Transaction(15000, "shopping", TRANSACTION_ID_11);

        transactionService.storeTransaction(TRANSACTION_ID_11, transaction1);
        transactionService.storeTransaction(TRANSACTION_ID_12, transaction2);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testGetNonExistingTransaction() throws TransactionNotFoundException {
        transactionService.getTransactionById(100);
    }

    @Test
    public void testGetNegativeTransactionId() {
        try {
            transactionService.getTransactionById(-10);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testGetZeroTransactionId() {
        try {
            transactionService.getTransactionById(0);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testStoreTransaction() {
        Transaction transaction = new Transaction(1000, "food");
        ResponseState response = transactionService.storeTransaction(10, transaction);
        assertNotNull(response);
        assertEquals(Response.Status.OK.name(), response.getStatus());
    }

    @Test
    public void testStoreNullTransaction() {
        ResponseState response = null;
        try {
            response = transactionService.storeTransaction(13, null);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testStoreZeroTransactionId() {
        ResponseState response = null;
        try {
            response = transactionService.storeTransaction(0, new Transaction(1000, "food"));
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testStoreZeroTransactionIdAndNullTransaction() {
        ResponseState response = null;
        try {
            response = transactionService.storeTransaction(0, null);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testGetSumOfTransactionsBasedOnParentId() {
        CalculationData response = null;
        try {
            response = transactionService.getTransactionSumByParentId(11);
            assertNotNull(response);
            assertEquals(25000D, response.getSum(), 0.0);
        } catch (TransactionNotFoundException e) {
            assertNull(response);
        }
    }

    @Test
    public void testGetSumOfTransactionsBasedOnZeroParentId() {
        CalculationData response = null;
        try {
            response = transactionService.getTransactionSumByParentId(0);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testGetSumOfTransactionsBasedOnNegativeParentId() {
        CalculationData response = null;
        try {
            response = transactionService.getTransactionSumByParentId(-10);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);

    }

    @Test
    public void testGetListOfTransactionIdsByType() throws TransactionNotFoundException {
        List<Integer> response = transactionService.getTransactionsByType(transaction1.getType());
            assertNotNull(response);
            assertEquals(1, response.size());
    }

    @Test
    public void testGetListOfTransactionIdsByNullType() {
        List<Integer> response = null;
        try {
            response = transactionService.getTransactionsByType(null);

        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testGetListOfTransactionIdsByEmptyType() {
        List<Integer> response = null;
        try {
            response = transactionService.getTransactionsByType("");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testGetListOfTransactionIdsByNonExistingType() throws TransactionNotFoundException {
        transactionService.getTransactionsByType("market");
    }

    @Test
    public void testGetTransactionsBasedOnId() {
        Transaction response = null;
        try {
            response = transactionService.getTransactionById(TRANSACTION_ID_11);
            assertNotNull(response);
            assertEquals(transaction1.getAmount(), response.getAmount(), 0.0);
            assertEquals(transaction1.getType(), response.getType());
            assertEquals(transaction1.getParentId(), response.getParentId(), 0.0);
        } catch (TransactionNotFoundException e) {
            assertNull(response);
        }
    }

    @Test
    public void testGetTransactionsBasedOnZeroId() {
        Transaction response = null;
        try {
            response = transactionService.getTransactionById(0);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }

    @Test
    public void testGetTransactionsBasedOnNegativeId() {
        Transaction response = null;
        try {
            response = transactionService.getTransactionById(-10);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertNull(response);
    }
}