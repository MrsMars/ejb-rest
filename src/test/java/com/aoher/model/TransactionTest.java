package com.aoher.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void testNormalTransaction() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroTransactionParentId() {
        new Transaction(200, "test", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTransactionParentId() {
        new Transaction(200, "test", -9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeNegativeTransactionParentId() {
        new Transaction(200, null, -9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTypeNegativeTransactionParentId() {
        new Transaction(200, "", -9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeZeroTransactionParentId() {
        new Transaction(200, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTypeZeroTransactionParentId() {
        new Transaction(200, "", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTransactionType() {
        new Transaction(200, "", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTransactionType() {
        new Transaction(200, null, 10);
    }

    @Test
    public void testNegativeAmount() {
        Transaction transaction = new Transaction(-200, "test", 10);
        assertNotNull(transaction);
    }

    @Test
    public void testNormalTransactionWithValues() {
        double amount = 50;
        String type = "test";
        int parentId = 10;

        Transaction transaction = new Transaction(amount, type, parentId);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount(), 0.001);
        assertEquals(parentId, transaction.getParentId());
        assertEquals(type, transaction.getType());
    }
}