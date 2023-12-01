package ru.volovnik.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.volovnik.configuration.JacksonConfiguration;
import ru.volovnik.documents.controller.dto.DocumentDto;
import ru.volovnik.documents.controller.dto.IdDto;
import ru.volovnik.documents.controller.dto.IdsDto;
import ru.volovnik.documents.service.DocumentServiceImpl;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class DocumentControllerTest {

    private static final String BASE_PATH = "/documents";

    private final ObjectMapper mapper = new JacksonConfiguration().objectMapper();

    private MockMvc mockMvc;

    @MockBean
    private DocumentServiceImpl service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testSuccessWhenSave() throws Exception {
        var organization = randomAlphabetic(100);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        when(service.save(documentDto)).thenReturn(documentDto);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());
    }

    @Test
    public void testErrorWhenSave() throws Exception {
        var organization = randomAlphabetic(1000);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        when(service.save(documentDto)).thenThrow(new IllegalStateException("Organization length is too long"));
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is4xxClientError());
    }

    @Test
    public void testSuccessGet() throws Exception {
        var organization = randomAlphabetic(100);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        when(service.findAll()).thenReturn(List.of(documentDto));
        mockMvc
                .perform(getAction(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(documentDto))));
    }

    @Test
    public void testSuccessWhenSend() throws Exception {
        var organization = randomAlphabetic(100);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        IdDto idDto = new IdDto();
        idDto.setId(5L);

        when(service.send(idDto.getId())).thenReturn(documentDto);
        mockMvc
                .perform(postAction(BASE_PATH + "/send", idDto))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(documentDto)));
    }

    @Test
    public void testSuccessWhenDelete() throws Exception {
        var organization = randomAlphabetic(100);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        when(service.save(documentDto)).thenReturn(documentDto);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());
        mockMvc.perform(delete(BASE_PATH + "/5")).andExpect(status().isOk());
    }

    @Test
    public void testSuccessWhenDeleteAll() throws Exception {
        var organization = randomAlphabetic(100);
        var description = randomAlphabetic(100);
        var patient = randomAlphabetic(20);
        var type = randomAlphabetic(100);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(1L);
        documentDto.setOrganization(organization);
        documentDto.setDescription(description);
        documentDto.setPatient(patient);
        documentDto.setType(type);

        IdsDto idsDto = new IdsDto();
        idsDto.setIds(Set.of(1L));

        when(service.save(documentDto)).thenReturn(documentDto);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());
        mockMvc.perform(deleteAction(BASE_PATH, idsDto)).andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder postAction(String uri, Object dto) throws JsonProcessingException {
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder deleteAction(String uri, Object dto) throws JsonProcessingException {
        return delete(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder getAction(String uri) {
        return get(uri)
                .contentType(APPLICATION_JSON);
    }
}
