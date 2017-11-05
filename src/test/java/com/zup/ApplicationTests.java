package com.zup;

import com.google.gson.Gson;
import com.zup.dao.ModelDAO;
import com.zup.dlo.ModelDLO;
import com.zup.exception.ModelDLOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ruhandosreis on 05/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @MockBean
    ModelDAO modelDAO;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void init() {
        Mockito.doNothing().when( modelDAO ).init();
        Mockito.doNothing().when( modelDAO ).create( Mockito.any() );
        Mockito.doNothing().when( modelDAO ).delete( Mockito.any(), Mockito.any() );
        Mockito.doNothing().when( modelDAO ).update( Mockito.any(), Mockito.any(), Mockito.any() );
    }

    @Test
    public void testCreateModel() throws Exception {

        final Map<String, Object> map = new HashMap<>();
        map.put("modelName", "produtos");

        final Map<Object, Object> attributes = new HashMap<>();
        attributes.put("preco", "DECIMAL");
        attributes.put("nome", "STRING");

        map.put("attributes", attributes);

        final Gson gson = new Gson();
        final String json = gson.toJson(map);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/model/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateModelError() throws Exception {
        final Map<String, Object> map = new HashMap<>();
        map.put("modelName", "produtos");

        final Gson gson = new Gson();
        final String json = gson.toJson(map);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/model/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testUpdate() throws Exception {
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("preco", 123d);
        attributes.put("nome", "teste");

        final Gson gson= new Gson();
        final String json = gson.toJson(attributes);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        final ArgumentCaptor<String> modelArgument = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> idArgument = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Map> elementUpdateArgument = ArgumentCaptor.forClass(Map.class);

        verify( modelDAO ).update( modelArgument.capture(), idArgument.capture(), elementUpdateArgument.capture() );

        final String model = modelArgument.getValue();
        final String id = idArgument.getValue();
        final Map element = elementUpdateArgument.getValue();

        Assert.assertEquals( "produtos", model );
        Assert.assertEquals( "1", id );

        final Object preco = element.get("preco");
        Assert.assertEquals( 123d, preco );

        final Object nome = element.get("nome");
        Assert.assertEquals("teste", nome);
    }

    @Test
    public void testUpdateError() throws Exception {
        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("preco", 123d);
        attributes.put("nome", "teste");

        final Gson gson= new Gson();
        final String json = gson.toJson(attributes);

        Mockito.doThrow( new RuntimeException() ).when( modelDAO ).update( any(), any(), any() );

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void testAdd() throws Exception {
        final Map<String, Object> attributes = new HashMap<>();

        attributes.put("preco", 123d);
        attributes.put("nome", "teste");
        attributes.put("validade", "2017-01-01");

        final Map<String, String> settings = new HashMap<>();
        settings.put("preco", "DECIMAL");
        settings.put("nome", "STRING");
        settings.put("validade", "DATE");

        final Gson gson= new Gson();
        final String json = gson.toJson(attributes);

        when( modelDAO.getSettings(any()) ).thenReturn( Optional.of( settings ) );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        final ArgumentCaptor<String> modelArgument = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Map> elementUpdateArgument = ArgumentCaptor.forClass(Map.class);

        verify( modelDAO ).add( modelArgument.capture(), elementUpdateArgument.capture() );

        final String model = modelArgument.getValue();
        Assert.assertEquals( "produtos", model );

        final Map element = elementUpdateArgument.getValue();

        final Object preco = element.get("preco");
        Assert.assertEquals( 123d, preco );

        final Object nome = element.get("nome");
        Assert.assertEquals("teste", nome );

        final Object validade = element.get("validade");
        Assert.assertEquals("2017-01-01", validade);
    }

    @Test
    public void testAddError() throws Exception {
        final Map<String, Object> attributes = new HashMap<>();

        attributes.put("preco", 123d);
        attributes.put("nome", "teste");
        attributes.put("validade", "2017-01-01");

        final Map<String, String> settings = new HashMap<>();
        settings.put("preco", "DECIMAL");
        settings.put("nome", "STRING");
        settings.put("validade", "DATE");

        final Gson gson= new Gson();
        final String json = gson.toJson(attributes);

        when( modelDAO.getSettings(any()) ).thenReturn( Optional.of( settings ) );
        Mockito.doThrow( new RuntimeException() ).when( modelDAO ).add( any(), any() );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testGet() throws Exception {
        final Map<String, String> settings = new HashMap<>();
        settings.put("preco", "DECIMAL");
        settings.put("nome", "STRING");

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("preco", 123d);
        attributes.put("nome", "teste");

        final List<Object> list = Arrays.asList(attributes);

        when( modelDAO.getSettings(any() ) ).thenReturn( Optional.of( settings ) );
        when( modelDAO.get( any() ) ).thenReturn( Optional.of( list ) );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settings.preco", is("DECIMAL")))
                .andExpect(jsonPath("$.settings.nome", is("STRING")))
                .andExpect(jsonPath("$.data[0].preco", is(123d)))
                .andExpect(jsonPath("$.data[0].nome", is("teste")));
    }

    @Test
    public void testGetById() throws Exception {
        final Map<String, String> settings = new HashMap<>();
        settings.put("preco", "DECIMAL");
        settings.put("nome", "STRING");

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("preco", 123d);
        attributes.put("nome", "teste");

        final List<Object> list = Arrays.asList(attributes);

        when( modelDAO.getSettings(any() ) ).thenReturn( Optional.of( settings ) );
        when( modelDAO.getById( any(), any() ) ).thenReturn( Optional.of( list ) );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settings.preco", is("DECIMAL")))
                .andExpect(jsonPath("$.settings.nome", is("STRING")))
                .andExpect(jsonPath("$.data[0].preco", is(123d)))
                .andExpect(jsonPath("$.data[0].nome", is("teste")));
    }
}
