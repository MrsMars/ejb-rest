package com.aoher.api;

import com.aoher.exception.TransactionNotFoundException;
import com.aoher.model.CalculationData;
import com.aoher.model.ResponseState;
import com.aoher.model.Transaction;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TransactionService {

    /**
     * Method to store transaction
     * @param id to be stored
     * @param transaction object containing information about transaction
     * @return ResponseState "OK" if stored
     */
    ResponseState storeTransaction(int id, Transaction transaction);

    /**
     * Method to retrieve transaction by id
     * @param id needs to be searched
     * @return Transaction object is found
     * @throws TransactionNotFoundException if transaction not found
     */
    Transaction getTransactionById(int id) throws TransactionNotFoundException;

    /**
     * Method to get the sum of transactions by parent id
     * @param id needs to be searched
     * @return CalculationData object if found which stores the calculation
     * @throws TransactionNotFoundException if transaction not found
     */
    CalculationData getTransactionSumByParentId(int id) throws TransactionNotFoundException;

    /**
     * Method to retrieve all transaction ids by types
     * @param type needs to be searched
     * @return List<Integer> list of transaction ids is found
     * @throws TransactionNotFoundException if transaction not found
     */
    List<Integer> getTransactionsByType(String type) throws TransactionNotFoundException;
}
