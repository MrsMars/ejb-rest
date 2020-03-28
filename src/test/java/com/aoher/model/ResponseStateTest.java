package com.aoher.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseStateTest {

    @Test
    public void testNormalResponseState() {
        ResponseState responseState = new ResponseState();
        assertNotNull(responseState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStatus() {
        new ResponseState(null);
    }

    @Test
    public void testNormalStatus() {
        ResponseState responseState = new ResponseState("OK");
        assertNotNull(responseState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStatus() {
        new ResponseState("");
    }

    @Test
    public void testGetStatus() {
        String status = "OK";
        ResponseState responseState = new ResponseState(status);
        assertNotNull(responseState);
        assertEquals(status, responseState.getStatus());
    }
}