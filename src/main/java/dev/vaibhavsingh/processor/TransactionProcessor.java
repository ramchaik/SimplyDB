package dev.vaibhavsingh.processor;

import java.util.LinkedList;
import java.util.Queue;

import static dev.vaibhavsingh.Main.processQuery;

public class TransactionProcessor implements QueryProcessor {
    public static Queue<String> buffer = new LinkedList<>();
    public static boolean inTransaction;

    private static void enableTransaction() {
        if (inTransaction) {
            throw new IllegalArgumentException("Transaction already in progress");
        }
        TransactionProcessor.inTransaction = true;
    }

    private static void disableTransaction() {
        if (!inTransaction) {
            throw new IllegalArgumentException("Transaction not in progress");
        }
        TransactionProcessor.inTransaction = false;
    }

    public static boolean isInTransaction() {
        return inTransaction;
    }

    public static boolean start() {
        if (inTransaction) {
            throw new IllegalArgumentException("Transaction already in progress");
        }
        buffer.clear();
        enableTransaction();
        return true;
    }

    public static boolean rollback() {
        if (!inTransaction) {
            throw new IllegalArgumentException("Transaction not in progress");
        }
        disableTransaction();
        buffer.clear();
        return true;
    }

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

    public static boolean addQueryToBuffer(String query) {
        buffer.add(query);
        return true;
    }

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

