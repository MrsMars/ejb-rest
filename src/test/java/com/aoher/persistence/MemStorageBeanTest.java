package com.aoher.persistence;

import com.aoher.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class MemStorageBeanTest {

    private MemStorageBean memStorageBean;
    private Transaction transaction1;

    private static final int TRANSACTION_ID_11 = 11;
    private static final int TRANSACTION_ID_12 = 12;

    @Before
    public void setUp() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        Context context = new InitialContext(props);
        memStorageBean = (MemStorageBean) context.lookup("MemStorageBeanLocalBean");
        assertNotNull(memStorageBean);

        transaction1 = new Transaction(10000, "cars");
        Transaction transaction2 = new Transaction(15000, "shopping", TRANSACTION_ID_11);

        memStorageBean.store(TRANSACTION_ID_11, transaction1);
        memStorageBean.store(TRANSACTION_ID_12, transaction2);
    }

    @Test
    public void testNormalStoreTransaction() {
        Transaction transaction = new Transaction(10000, "food");
        memStorageBean.store(13, transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStoreNullTransaction() {
        memStorageBean.store(13, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStoreZeroTransactionId() {
        Transaction transaction = new Transaction(10000, "food");
        memStorageBean.store(0, transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStoreNegativeTransactionId() {
        Transaction transaction = new Transaction(10000, "food");
        memStorageBean.store(-10, transaction);
    }

    @Test
    public void testGetAllOfTransactionsBasedOnParentId() {
        List<Transaction> response = memStorageBean.getAllTransactionsWithParentId(TRANSACTION_ID_11);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllTransactionsBasedOnZeroParentId() {
        memStorageBean.getAllTransactionsWithParentId(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllTransactionsBasedOnNegativeParentId() {
        memStorageBean.getAllTransactionsWithParentId(-10);
    }

    @Test
    public void testGetListOfTransactionIdsByType() {
        List<Integer> response = memStorageBean.getTransactionByType(transaction1.getType());
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetListOfTransactionIdsByNullType() {
        memStorageBean.getTransactionByType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetListOfTransactionIdsByEmptyType() {
        memStorageBean.getTransactionByType("");
    }

    @Test
    public void testGetListOfTransactionIdsByNonExistingType() {
        List<Integer> list = memStorageBean.getTransactionByType("market");
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testGetTransactionsBasedOnId() {
        Transaction response = memStorageBean.getTransactionById(TRANSACTION_ID_11);
        assertNotNull(response);
        assertEquals(transaction1.getAmount(), response.getAmount(), 0.0);
        assertEquals(transaction1.getType(), response.getType());
        assertEquals(transaction1.getParentId(), response.getParentId(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTransactionsBasedOnZeroId() {
        memStorageBean.getTransactionById(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTransactionsBasedOnNegativeId() {
        memStorageBean.getTransactionById(-10);
    }
}