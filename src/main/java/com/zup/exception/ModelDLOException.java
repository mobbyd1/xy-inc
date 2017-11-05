package com.zup.exception;

/**
 * Created by ruhandosreis on 04/11/17.
 */
public class ModelDLOException extends Exception {

    public ModelDLOException() {
        super();
    }

    public ModelDLOException( final String message ) {
        super( message );
    }

    public ModelDLOException( final Throwable throwable ) {
        super( throwable );
    }

    public ModelDLOException( final String message, final Throwable throwable ) {
        super( message, throwable );
    }
}
