package com.aoher.service;

import com.aoher.api.TransactionService;
import com.aoher.exception.TransactionNotFoundException;
import com.aoher.model.CalculationData;
import com.aoher.model.ResponseState;
import com.aoher.model.Transaction;
import com.aoher.persistence.MemStorageBean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.aoher.util.Exceptions.ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO;
import static com.aoher.util.Exceptions.TYPE_CANNOT_BE_EMPTY;

@LocalBean
@Stateless
public class TransactionServiceBean implements TransactionService {

    @EJB
    private MemStorageBean ms;

    @Override
    public ResponseState storeTransaction(int id, Transaction transaction) {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }
        if (transaction == null) {
            throw new IllegalArgumentException(TYPE_CANNOT_BE_EMPTY);
        }
        ms.store(id, transaction);
        return new ResponseState(Response.Status.OK.name());
    }

    @Override
    public Transaction getTransactionById(int id) throws TransactionNotFoundException {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }
        Transaction transaction = ms.getTransactionById(id);
        if (transaction == null) {
            throw new TransactionNotFoundException();
        }
        return transaction;
    }

    @Override
    public CalculationData getTransactionSumByParentId(int id) throws TransactionNotFoundException {
        if (id <= 0) {
            throw new IllegalArgumentException(ID_CANNOT_BE_LESS_OR_EQUALS_TO_ZERO);
        }
        CalculationData data = new CalculationData();
        Transaction mainTransaction = getTransactionById(id);
        if (mainTransaction == null) {
            throw new TransactionNotFoundException();
        }

        data.add(mainTransaction.getAmount());
        List<Transaction> listOfTransactions = ms.getAllTransactionsWithParentId(id);
        if (listOfTransactions != null && !listOfTransactions.isEmpty()) {
            listOfTransactions.stream().map(Transaction::getAmount).forEach(data::add);
        }
        return data;
    }

    @Override
    public List<Integer> getTransactionsByType(String type) throws TransactionNotFoundException {
        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException(TYPE_CANNOT_BE_EMPTY);
        }
        List<Integer> listOfTransactions = ms.getTransactionByType(type.trim());
        if (listOfTransactions == null || listOfTransactions.isEmpty()) {
            throw new TransactionNotFoundException();
        }
        return listOfTransactions;
    }
}
