package com.zup.api.controller;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.zup.api.dto.CreateModelDTO;
import com.zup.exception.ModelDLOException;
import com.zup.util.AttributeType;
import com.zup.dlo.ModelDLO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by ruhandosreis on 02/11/17.
 */
@Controller
@RequestMapping("/api")
public class ApiController extends AbstractControllerWithErrorHandling {

    @Autowired
    private ModelDLO modelDLO;

    @RequestMapping(value = "/{model}/{id}", method = RequestMethod.GET)
    public ResponseEntity getById( @PathVariable final String model, @PathVariable final String id ) throws ModelDLOException {
        final Map elements = modelDLO.getById(model, id);

        final ResponseEntity responseEntity = new ResponseEntity(elements, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/{model}", method = RequestMethod.GET)
    public ResponseEntity get( @PathVariable final String model ) throws ModelDLOException {
        final ResponseEntity responseEntity = new ResponseEntity( modelDLO.get(model), HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/{model}", method = RequestMethod.POST)
    public ResponseEntity add(
            @PathVariable final String model
            , @RequestBody final Map<String, Object> modelDTO ) throws ModelDLOException {

        modelDLO.add( model, modelDTO );

        final ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/{model}/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(
            @PathVariable final String model
            , @PathVariable final String id
            , @RequestBody final Map<String, Object> modelDTO) throws ModelDLOException {

        modelDLO.update( model, id, modelDTO );

        final ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "{model}/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete( @PathVariable final String model, @PathVariable final String id ) throws ModelDLOException {
        modelDLO.delete( model, id );

        final ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/format/types", method = RequestMethod.GET)
    public ResponseEntity getFormatTypes() {
        final ResponseEntity responseEntity = new ResponseEntity(AttributeType.values(), HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/model/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody final CreateModelDTO modelDTO) throws ModelDLOException{
        modelDLO.create( modelDTO );

        final ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }
}
