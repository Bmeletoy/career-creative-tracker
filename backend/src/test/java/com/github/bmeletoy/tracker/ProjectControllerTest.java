package com.github.bmeletoy.tracker;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.containsString;


@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectRepository projectRepository;

    private Project project, project2;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setTitle("Acme job");
        project.setId(1L);

        project2 = new Project();
        project2.setTitle("fusionSpan");
        project2.setId(2L);
    }

    @Test
    void testGetProjectById_Found() throws Exception{

        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Acme job"));
    }

    @Test
    void testGetAllProjects() throws Exception{
        Mockito.when(projectRepository.findAll())
        .thenReturn(List.of(project, project2));

        mockMvc.perform(get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Acme job"))
        .andExpect(jsonPath("$[1].title").value("fusionSpan"));
    }

    @Test
    void testGetProjectNotFound() throws Exception{
        Mockito.when(projectRepository.findById(2L))
        .thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/2"))
        .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProject() throws Exception{
        Project temp = new Project();
        temp.setTitle("Acme job");

       Mockito.when(projectRepository.save(Mockito.any(Project.class)))
       .thenReturn(project);

       String json = objectMapper.writeValueAsString(temp);

       mockMvc.perform(post("/projects").contentType(MediaType.APPLICATION_JSON)
               .content(json)).andExpect(status().isCreated())
               .andExpect(jsonPath("$.title").value("Acme job"))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(header().string("Location", "/projects/1"));
    }

    @Test
    void testUpdateProject() throws Exception{
        Project toUpdate = new Project();
        toUpdate.setTitle("Not Acme job");

        String json = objectMapper.writeValueAsString(toUpdate);

        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(projectRepository.save(Mockito.any(Project.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/projects/1").contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Not Acme job"))
        .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateProjectNotFound() throws Exception{
        Project toUpdate = new Project();
        toUpdate.setTitle("Not Acme job");

        String json = objectMapper.writeValueAsString(toUpdate);

        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.empty());

        mockMvc.perform(put("/projects/3")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Project Not Found: 3")));
    }

    @Test 
    void testDeleteProject() throws Exception{
        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        mockMvc.perform(delete("/projects/1"))
        .andExpect(status().isNoContent());

        Mockito.verify(projectRepository).delete(project);
    }

    @Test
    void testDeleteProjectNotFound() throws Exception{
        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.empty());

        mockMvc.perform(delete("/projects/3"))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Project Not Found: 3")));
    }
    
}
