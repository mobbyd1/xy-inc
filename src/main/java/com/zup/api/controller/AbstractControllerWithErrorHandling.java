package com.zup.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by ruhandosreis on 18/09/17.
 */
public class AbstractControllerWithErrorHandling {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException( Exception ex ) {
        return new ResponseEntity( "Ocorreu um erro inesperado", HttpStatus.INTERNAL_SERVER_ERROR );
    }

}
