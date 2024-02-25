package dev.vaibhavsingh.processor;

import java.util.LinkedList;
import java.util.Queue;

import static dev.vaibhavsingh.Main.processQuery;

public class TransactionProcessor implements QueryProcessor {
    /**
     * The buffer to store the queries
     */
    public static Queue<String> buffer = new LinkedList<>();
    /**
     * The flag to check if the transaction is in progress
     */
    public static boolean inTransaction;

    /**
     * Enables the transaction
     */
    private static void enableTransaction() {
        if (inTransaction) {
            throw new IllegalArgumentException("Transaction already in progress");
        }
        TransactionProcessor.inTransaction = true;
    }

    /**
     * Disables the transaction
     */
    private static void disableTransaction() {
        if (!inTransaction) {
            throw new IllegalArgumentException("Transaction not in progress");
        }
        TransactionProcessor.inTransaction = false;
    }

    /**
     * Checks if the transaction is in progress
     * @return true if the transaction is in progress, false otherwise
     */
    public static boolean isInTransaction() {
        return inTransaction;
    }

    /**
     * Starts the transaction
     * @return true if the transaction is started, false otherwise
     */
    public static boolean start() {
        if (inTransaction) {
            throw new IllegalArgumentException("Transaction already in progress");
        }
        buffer.clear();
        enableTransaction();
        return true;
    }

    /**
     * Rolls back the transaction
     * @return true if the transaction is rolled back, false otherwise
     */
    public static boolean rollback() {
        if (!inTransaction) {
            throw new IllegalArgumentException("Transaction not in progress");
        }
        disableTransaction();
        buffer.clear();
        return true;
    }

    /**
     * Commits the transaction
     * @return true if the transaction is committed, false otherwise
     */
    public static boolean commit() {
        if (!inTransaction) {
            throw new IllegalArgumentException("Transaction not in progress");
        }
        disableTransaction();
        // loop over the buffer and process queries
        for (String command : buffer) {
            processQuery(command);
        }
        buffer.clear();
        return true;
    }

    /**
     * Adds the query to the buffer
     * @param query the query to be added to the buffer
     * @return true if the query is added to the buffer, false otherwise
     */
    public static boolean addQueryToBuffer(String query) {
        buffer.add(query);
        return true;
    }

    /**
     * Processes the query
     * @param query the query to be processed
     * @return true if the query is processed successfully, false otherwise
     */
    @Override
    public boolean process(String query) {
        // if query is start transaction
        if (query.equalsIgnoreCase("start transaction") || query.equalsIgnoreCase("begin transaction")) {
            System.out.println("Transaction started");
            return start();
        }
        // if query is commit
        if (query.equalsIgnoreCase("commit")) {
            System.out.println("Transaction committed");
            return commit();
        }
        // if query is rollback
        if (query.equalsIgnoreCase("rollback")) {
            System.out.println("Transaction rolled back");
            return rollback();
        }
        // if query is not a transaction query
        addQueryToBuffer(query);
        return true;
    }
}

