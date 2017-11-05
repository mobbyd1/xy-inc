package com.zup.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.zup.api.dto.CreateModelDTO;
import com.zup.util.AttributeType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.elemMatch;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by ruhandosreis on 04/11/17.
 */
@Component
public class ModelDAOImpl implements ModelDAO {

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.user}")
    private String user;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.database}")
    private String database;

    private MongoDatabase mongoDatabase;

    private static String ATTRIBUTE_ID = "id";

    @PostConstruct
    public void init() {
        final String url = String.format( "mongodb://%s:%s@%s:%s/%s", user, password, host, port, database );

        final MongoClientURI connectionString = new MongoClientURI(url);

        final MongoClient mongoClient = new MongoClient(connectionString);
        mongoDatabase = mongoClient.getDatabase(this.database);
    }


    @Override
    public void create(CreateModelDTO modelDTO) {
        final String modelName = modelDTO.getModelName();
        mongoDatabase.createCollection( modelName );

        final Map<String, Object> document = new HashMap<>();

        final Map<String, AttributeType> attributes = modelDTO.getAttributes();
        final Set<Map.Entry<String, AttributeType>> entries = attributes.entrySet();

        final Map<String, String> attributesTypes = new HashMap<>();

        for( final Map.Entry<String, AttributeType> entry : entries ) {
            final String key = entry.getKey();
            final AttributeType value = entry.getValue();

            attributesTypes.put( key, value.toString() );
        }

        document.put( "modelName", modelName);
        document.put( "settings", attributesTypes);
        document.put( "data", new ArrayList<>() );

        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        collection.insertOne( new Document( document ) );

    }

    @Override
    public void add(String modelName, Map<String, Object> modelDTO) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        if (!existsCollection(collection)) return;

        final ObjectId objectId = ObjectId.get();
        final String id = objectId.toString();

        modelDTO.put("id", id);

        final Document document = new Document(modelDTO);
        collection.updateOne( eq("modelName", modelName), Updates.addToSet( "data", document ) );
    }

    @Override
    public void update(String modelName, String id, Map<String, Object> modelDTO) {
        delete( modelName, id );

        modelDTO.put("id", id);

        final Document document = new Document(modelDTO);

        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        collection.updateOne( eq("modelName", modelName), Updates.addToSet( "data", document ) );
    }

    @Override
    public Optional<List<Object>> get(String modelName) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        if (!existsCollection(collection)) return Optional.empty();

        final Document modelDocument = new Document();
        modelDocument.put("modelName", modelName);

        final Document first = collection.find(modelDocument).first();
        final List<Object> data =  ( List ) first.get("data");

        if( data == null ) {
            return Optional.empty();
        }

        return Optional.of( data );
    }

    @Override
    public Optional<List<Object>> getById(String modelName, String id) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        if (!existsCollection(collection)) return Optional.empty();

        final Bson projection = Projections.fields(
                excludeId()
                , eq("modelName", modelName)
                , elemMatch("data", eq("id", id)));

        final FindIterable<Document> iterable = collection.find().projection(projection);

        final Document first = iterable.first();

        final List<Object> data = ( List ) first.get("data");
        if( data == null ) {
            return Optional.empty();
        }

        return Optional.of( data );
    }

    @Override
    public void delete(String modelName, String id) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        if (!existsCollection(collection)) return;

        final Optional<List<Object>> optional = getById(modelName, id);

        if( optional.isPresent() ) {
            final Map element = (Map) optional.get().get(0);
            collection.updateOne(eq("modelName", modelName), Updates.pull("data", element));
        }
    }

    @Override
    public Optional<Map<String, String>> getSettings(String modelName) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(modelName);
        if (!existsCollection(collection)) return Optional.empty();

        final Document modelDocument = new Document();
        modelDocument.put("modelName", modelName);

        final Document first = collection.find(modelDocument).first();
        final Map<String, String> settings = (Map) first.get("settings");

        return Optional.of( settings );
    }

    private boolean existsCollection(MongoCollection<Document> collection) {
        return collection.count() != 0;
    }
}
