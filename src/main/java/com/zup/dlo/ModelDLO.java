package com.zup.dlo;

import com.zup.api.dto.CreateModelDTO;
import com.zup.exception.ModelDLOException;

import java.util.List;
import java.util.Map;

/**
 * Created by ruhandosreis on 04/11/17.
 */
public interface ModelDLO {

    public void create( final CreateModelDTO modelDTO ) throws ModelDLOException;

    public void add ( final String modelName, final Map<String, Object> modelDTO ) throws ModelDLOException;

    public void update ( final String modelName, final String id, final Map<String, Object> modelDTO ) throws ModelDLOException;

    public Map get ( final String modelName ) throws ModelDLOException;

    public Map getById ( final String modelName, final String id ) throws ModelDLOException;

    public void delete( final String modelName, final String id ) throws ModelDLOException;


}
