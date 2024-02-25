package dev.vaibhavsingh.processor;

public interface QueryProcessor {
    /**
     * Processes the query
     * @param query the query to be processed
     * @return true if the query is processed successfully, false otherwise
     */
    boolean process(String query);
}
