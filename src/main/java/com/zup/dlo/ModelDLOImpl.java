package com.zup.dlo;

import com.zup.api.dto.CreateModelDTO;
import com.zup.dao.ModelDAO;
import com.zup.exception.ModelDLOException;
import com.zup.util.AttributeType;
import com.zup.util.FormatDLO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by ruhandosreis on 04/11/17.
 */
@Component
public class ModelDLOImpl implements ModelDLO {

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private FormatDLO formatDLO;

    @Override
    public void create(final CreateModelDTO modelDTO) throws ModelDLOException {
        if( modelDTO.getAttributes().size() == 0 ) {
            throw new ModelDLOException( "Lista de atributos vazia" );
        }

        try {
            modelDAO.create(modelDTO);
        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }

    @Override
    public void add(final String modelName, final Map<String, Object> modelDTO) throws ModelDLOException {

        try {
            final Optional<Map<String, String>> optionalSettings = modelDAO.getSettings(modelName);
            if( !optionalSettings.isPresent() ) {
                throw new ModelDLOException( "O modelo não foi encontrado" );
            }

            final Map<String, Object> attributes = new HashMap<>();
            final Set<Map.Entry<String, String>> entries = optionalSettings.get().entrySet();

            for( final Map.Entry<String, String> entry : entries ) {
                final String key = entry.getKey();

                final String value = entry.getValue();
                final AttributeType attributeType = AttributeType.valueOf(value);

                final Object attributeValue = modelDTO.get(key);

                final Object valueFormatted = formatDLO.format(attributeValue, attributeType);
                attributes.put( key, valueFormatted );
            }

            modelDAO.add(modelName, attributes);

        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }

    @Override
    public void update(final String modelName, final String id, final Map<String, Object> modelDTO) throws ModelDLOException {
        try {
            modelDAO.update(modelName, id, modelDTO);
        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }

    @Override
    public Map get(final String modelName) throws ModelDLOException  {

        try {
            final Optional<Map<String, String>> optionalSettings = modelDAO.getSettings(modelName);
            if( !optionalSettings.isPresent() ) {
                return new HashMap<>();
            }

            final Optional<List<Object>> optional = modelDAO.get(modelName);

            if( !optional.isPresent() ) {
                return new HashMap<>();
            }

            return buildGetResponse(optionalSettings.get(), optional.get());

        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }

    @Override
    public Map getById(final String modelName, final String id) throws ModelDLOException {

        try {

            final Optional<Map<String, String>> optionalSettings = modelDAO.getSettings(modelName);
            if( !optionalSettings.isPresent() ) {
                return new HashMap<>();
            }

            final Optional<List<Object>> optional = modelDAO.getById(modelName, id);

            if( !optional.isPresent() ) {
                return new HashMap<>();
            }

            return buildGetResponse(optionalSettings.get(), optional.get());

        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }

    private Map buildGetResponse( final Map<String, String> settings, final List<Object> elements ) {
        final Map<String, Object> map = new HashMap<>();
        map.put( "settings", settings );
        map.put( "data", elements );

        return map;
    }

    @Override
    public void delete(final String modelName, final String id) throws ModelDLOException {
        try {
            modelDAO.delete(modelName, id);
        } catch ( Throwable t ) {
            throw new ModelDLOException( t );
        }
    }
}
