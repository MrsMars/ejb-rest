package com.aoher.service.integrationtests;

import com.aoher.model.Transaction;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.core.MediaType;

import static com.aoher.util.Exceptions.TRANSACTION_NOT_FOUND;
import static com.jayway.restassured.RestAssured.expect;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

public class TransactionHandlerIT {

    private static final String STATUS_OK = javax.ws.rs.core.Response.Status.OK.name();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8989;
        RestAssured.basePath = "/SimpleRESTService";
    }

    @Test
    public void testStoreTransaction() {
        Response response = expect()
                .statusCode(200)
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(1000, "cars"))
                .when()
                .put("/rest/transactionservice/transaction/10");
        assertEquals(getStatusResponse(STATUS_OK), response.getBody().asString().trim());
    }

    @Test
    public void testStoreParentIdTransaction() {
        Response response = expect()
                .statusCode(200)
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(15000, "shopping", 10))
                .when()
                .put("/rest/transactionservice/transaction/11");
        assertEquals(STATUS_OK, response.getBody().asString().trim());
    }

    @Test
    public void testGetTransaction() {
        Transaction transaction = new Transaction(10000, "cars");
        Response response = expect()
                .statusCode(200)
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(transaction)
                .when()
                .put("/rest/transactionservice/transaction/10");
        assertEquals(getStatusResponse(STATUS_OK), response.getBody().asString().trim());

        response = expect()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/rest/transactionservice/transaction/10");
        assertEquals("{\"amount\":" + transaction.getAmount() +
                        ",\"type\":\"" + transaction.getType() +
                        "\",\"parent_id\":" + transaction.getParentId() + "}",
                response.getBody().asString().trim());
    }

    @Test
    public void testGetNonExistingTransaction() {
        Response response = expect()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/rest/transactionservice/transaction/199");
        assertEquals(getStatusResponse(TRANSACTION_NOT_FOUND), response.getBody().asString().trim());
    }

    @Test
    public void testGetTransactionsSum() throws JSONException {
        Response response = expect().statusCode(200).given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(10000, "cars"))
                .when().put("/rest/transactionservice/transaction/10");
        assertEquals(getStatusResponse(STATUS_OK), response.getBody().asString().trim());

        response = expect().statusCode(200).given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(15000, "shopping", 10))
                .when().put("/rest/transactionservice/transaction/11");
        assertEquals(getStatusResponse(STATUS_OK), response.getBody().asString().trim());

        response = expect().statusCode(200).contentType(MediaType.APPLICATION_JSON)
                .when().get("/rest/transactionservice/sum/10");
        JSONAssert.assertEquals("{\"sum\":25000", response.asString(), false);
    }

    @Test
    public void testGetTransactionsByType() throws JSONException {
        Response response = expect().statusCode(200).given().contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(10000, "cars")).when().put("/rest/transactionservice/transaction/10");
        assertEquals(getStatusResponse(STATUS_OK), response.getBody().asString().trim());

        response = expect().statusCode(200).given().contentType(MediaType.APPLICATION_JSON)
                .body(new Transaction(15000, "shopping", 10)).when().put("/rest/transactionservice/transaction/11");
        assertEquals(response.getBody().asString().trim(), "{\"status\":\"OK\"}");

        response = expect().statusCode(200).contentType(MediaType.APPLICATION_JSON).when()
                .get("/rest/transactionservice/types/cars");
        JSONAssert.assertEquals("[10]", response.asString(), false);
    }

    private String getStatusResponse(String status) {
        return format("{\"status\":\"%s\"}", status);
    }
}
