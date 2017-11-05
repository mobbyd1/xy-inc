package com.zup.dao;

import com.zup.api.dto.CreateModelDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by ruhandosreis on 04/11/17.
 */
public interface ModelDAO {

    void init();

    void create( final CreateModelDTO modelDTO );

    void add ( final String modelName, final Map<String, Object> modelDTO );

    void update ( final String modelName, final String id, final Map<String, Object> modelDTO );

    Optional<List<Object>> get (final String modelName );

    Optional<List<Object>> getById (final String modelName, final String id );

    void delete( final String modelName, final String id );

    Optional<Map<String, String>> getSettings( final String modelName );
}
