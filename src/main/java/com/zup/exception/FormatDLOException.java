package com.zup.exception;

/**
 * Created by ruhandosreis on 05/11/17.
 */
public class FormatDLOException extends Exception {

    public FormatDLOException() {
        super();
    }

    public FormatDLOException( final String message ) {
        super( message );
    }

    public FormatDLOException( final Throwable throwable ) {
        super( throwable );
    }

    public FormatDLOException( final String message, final Throwable throwable ) {
        super( message, throwable );
    }
}
