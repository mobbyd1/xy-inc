package com.zup.util;

import com.zup.exception.FormatDLOException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruhandosreis on 05/11/17.
 */
@Component
public class FormatDLO {

    public static String API_DATE_FORMAT = "yyyy-mm-dd";

    public Object format ( final Object value, final AttributeType attributeType ) throws FormatDLOException {

        switch( attributeType ) {

            case INTEGER:
                return formatInteger( value );

            case DECIMAL:
                return formatDecimal( value );

            case DATE:
                return formatDate( value );

            case STRING:
                return value.toString();

            default:
                final String msg = String.format("Não foi possível formatar o valor %s.", value);
                throw new FormatDLOException( msg );
        }
    }

    private Integer formatInteger ( final Object value ) throws FormatDLOException {

        try {
            final Integer integer = Integer.valueOf(value.toString());
            return integer;

        } catch ( final NumberFormatException e ) {
            final String msg = String.format("Não foi possível converter o valor %s para inteiro", value);
            throw new FormatDLOException( msg, e );
        }
    }

    private Double formatDecimal ( final Object value ) throws FormatDLOException {

        try {
            final Double aDouble = Double.valueOf(value.toString());
            return aDouble;

        } catch ( final NumberFormatException e ) {
            final String msg = String.format("Não foi possível converter o valor %s para decimal", value);
            throw new FormatDLOException( msg, e );
        }
    }

    private String formatDate ( final Object value ) throws FormatDLOException {
        try {

            final DateFormat simpleDateFormat = new SimpleDateFormat(API_DATE_FORMAT);

            simpleDateFormat.parse(value.toString());
            return value.toString();

        } catch ( ParseException e ) {
            final String msg = String.format("Não foi possível converter o valor %s para date", value);
            throw new FormatDLOException( msg, e );
        }
    }
}
