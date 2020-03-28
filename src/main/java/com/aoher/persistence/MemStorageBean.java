package com.aoher.persistence;

import com.aoher.model.Transaction;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aoher.util.Exceptions.*;

@Singleton
public class MemStorageBean {

    private Map<Integer, Transaction> transactions = new LinkedHashMap<>();

    /**
     * Method to store transaction into a Map
     *
     * @param id          id of transaction
     * @param transaction transaction data
     */
    public void store(int id, Transaction transaction) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }
        if (transaction == null) {
            throw new IllegalArgumentException(TRANSACTION_CANNOT_BE_NULL);
        }
        transactions.put(id, transaction);
    }

    /**
     * Method to retrieve transaction by id
     *
     * @param id id of transaction
     * @return transaction data, if found. Null otherwise
     */
    public Transaction getTransactionById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }
        return transactions.get(id);
    }

    /**
     * A method to retrieve all transaction associated by Parent id.
     * @param id id to be searched
     * @return List of Transactions, empty list otherwise
     */
    public List<Transaction> getAllTransactionsWithParentId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }

        return transactions.keySet().stream()
                .map(transactionId -> transactions.get(transactionId))
                .filter(transaction -> transaction.getParentId() == id)
                .collect(Collectors.toList());
    }

    /**
     * A method to retrieve all transaction ids by type
     * @param type type to be searched
     * @return List of transaction ids, empty otherwise
     */
    public List<Integer> getTransactionByType(String type) {
        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException(TYPE_CANNOT_BE_EMPTY);
        }
        List<Integer> listOfTransactions = new ArrayList<>();

        transactions.keySet().forEach(transactionId -> {
            Transaction transaction = transactions.get(transactionId);
            if (transaction.getType().equals(type.trim())) {
                listOfTransactions.add(transactionId);
            }
        });
        return listOfTransactions;
    }
}
