package com.example.fetchrewardscodingproj.Helpers;

/** Helper class used to wrap exceptions thrown by another thread
 *
 * <p> Allows main thread the ability to retrieve exceptions thrown by the API Client
 * */
public class ResultMightThrow<T> {

    private final T value;
    private final Exception exception;

    public ResultMightThrow(final T setResult) {
        value = setResult;
        exception = null;
    }
    public ResultMightThrow(final Exception setException) {
        value = null;
        exception = setException;
    }
    public T getValue() {
        if (exception != null) {
            throw new RuntimeException(exception);
        } else {
            return value;
        }
    }
}
