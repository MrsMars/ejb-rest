package com.aoher.exception;

/**
 * TransactionNotFoundException
 * @author Oher_AI
 * @changed Oher_AI 02.03.2020 - created.
 */
public class TransactionNotFoundException extends Exception {

    private static final String NAME = "TransactionNotFoundException";

    public static String getName() {
        return NAME;
    }
}
