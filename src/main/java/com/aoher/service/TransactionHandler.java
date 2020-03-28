package com.aoher.service;

import com.aoher.api.TransactionService;
import com.aoher.exception.TransactionNotFoundException;
import com.aoher.model.CalculationData;
import com.aoher.model.ResponseState;
import com.aoher.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static com.aoher.util.Exceptions.TRANSACTION_NOT_FOUND;
import static com.aoher.util.Exceptions.TYPE_NOT_FOUND;

@Stateless
@Path("/transactionservice")
public class TransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionHandler.class);

    @EJB
    private TransactionService transactionService;

    /**
     * A method to store transaction based on id and transaction data. URI :
     * /rest/transactionservice/transaction/{id}{"amount":20000,"type":"cars",
     * "parent_id",10}
     * @param id id to be stored
     * @param transaction transaction to be stored
     * @return returns JSON with status data
     */
    @PUT
    @Path("/transaction/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTransaction(@PathParam("id") int id, Transaction transaction) {
        log.info("addTransaction called");
        ResponseState response = transactionService.storeTransaction(id, transaction);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    /**
     * A method to retrieve transaction based on id. URI :
     * /rest/transactionservice/transaction/{id}
     * @param id id to be searched
     * @return returns JSON with Transaction data, else response string with error
     */
    @GET
    @Path("/transaction/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionById(@PathParam("id") int id) {
        log.info("getTransactionById called");
        Transaction transaction;
        try {
            transaction = transactionService.getTransactionById(id);
            return Response.status(Response.Status.OK).entity(transaction).build();
        } catch (TransactionNotFoundException e) {
            log.warn(TransactionNotFoundException.getName(), e);
        }
        ResponseState response = new ResponseState(TRANSACTION_NOT_FOUND);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    /**
     * A method to retrieve transaction based on types. URI :
     * /rest/transactionservice/types/{type}
     * @param type type to be searched
     * @return returns JSON with List of Transaction ids, else response string with error
     */
    @GET
    @Path("/types/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionByType(@PathParam("type") String type) {
        log.info("getTransactionByType called");
        List<Integer> transactions;
        try {
            transactions = transactionService.getTransactionsByType(type);
            return Response.status(Response.Status.OK).entity(transactions).build();
        } catch (TransactionNotFoundException e) {
            log.warn(TransactionNotFoundException.getName(), e);
        }
        ResponseState response = new ResponseState(TYPE_NOT_FOUND);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    /**
     * A method to retrieve transactions sum based on parent Id. URI :
     * /rest/transactionservice/sum/{id}
     * @param id id to be searched
     * @return returns JSON with SUM, else response string with error
     */
    @GET
    @Path("/sum/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsSum(@PathParam("id") int id) {
        log.info("getTransactionsSum called");
        try {
            CalculationData sum = transactionService.getTransactionSumByParentId(id);
            return Response.status(Response.Status.OK).entity(sum).build();
        } catch (TransactionNotFoundException e) {
            log.warn(TransactionNotFoundException.getName(), e);
        }
        ResponseState response = new ResponseState(TRANSACTION_NOT_FOUND);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
