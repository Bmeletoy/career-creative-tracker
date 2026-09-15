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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class StageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StageRepository stageRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    private Project project, randProj;

    private Stage stage1, stage2, stage3, stage4;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setTitle("Acme job");
        project.setId(1L);

        randProj = new Project();
        randProj.setTitle("Brivo job");
        randProj.setId(2L);

        stage3 = new Stage();
        stage3.setName("Accepted");
        stage3.setId(3L);
        stage3.setProject(randProj);
        
        stage1 = new Stage();
        stage1.setName("Getting Started");
        stage1.setProject(project);
        stage1.setId(1L);

        stage2 = new Stage();
        stage2.setName("Middle");
        stage2.setProject(project);
        stage2.setId(2L);

        stage4 = new Stage();
        stage4.setName("Pleased to offer");
        stage4.setId(4L);
    }

    @Test
    void testGetStageFound() throws Exception{
        Mockito.when(stageRepository.findById(1L)).
        thenReturn(Optional.of(stage1));

        mockMvc.perform(get("/projects/1/stages/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Getting Started"));
    }

    @Test
    void testGetStageNoStage() throws Exception{
        Mockito.when(stageRepository.findById(5L)).
        thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/1/stages/5"))
        .andExpect(status().isNotFound());
    }

    @Test
    void testGetStageIncorrectStage() throws Exception{
        Mockito.when(stageRepository.findById(3L)).
        thenReturn(Optional.of(stage3));

         mockMvc.perform(get("/projects/1/stages/3"))
        .andExpect(status().isNotFound());
    }

    @Test
    void testGetStagesForProject() throws Exception{
        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(stageRepository.findByProjectId(1L)).
        thenReturn(List.of(stage1,stage2));

        mockMvc.perform(get("/projects/1/stages"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Getting Started"))
        .andExpect(jsonPath("$[1].name").value("Middle"));
    }

    @Test
    void testCreateStage() throws Exception{
        Stage temp = new Stage();
        temp.setName("Pleased to offer");

        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(stageRepository.save(Mockito.any(Stage.class)))
        .thenReturn(stage4);

        String json = objectMapper.writeValueAsString(temp);

       mockMvc.perform(post("/projects/1/stages")
       .contentType(MediaType.APPLICATION_JSON)
       .content(json)).andExpect(status().isCreated())
       .andExpect(jsonPath("$.name").value("Pleased to offer"))
       .andExpect(header().string("Location", "/projects/1/stages/4"));
    }

    @Test
    void testUpdateStage() throws Exception{
        Stage toUpdate = new Stage();
        toUpdate.setName("Graced");

        String json = objectMapper.writeValueAsString(toUpdate);

        Mockito.when(stageRepository.findById(1L))
        .thenReturn(Optional.of(stage1));

        Mockito.when(stageRepository.save(Mockito.any(Stage.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/projects/1/stages/1").contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Graced"))
        .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateNoStage() throws Exception{
        String json = objectMapper.writeValueAsString(stage4);

        Mockito.when(stageRepository.findById(5L)).
        thenReturn(Optional.empty());

        mockMvc.perform(put("/projects/1/stages/5")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateIncorrectStage() throws Exception{
        String json = objectMapper.writeValueAsString(stage3);
        Mockito.when(stageRepository.findById(3L)).
        thenReturn(Optional.of(stage3));

        mockMvc.perform(put("/projects/1/stages/3")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteStage() throws Exception{
        Mockito.when(stageRepository.findById(1L))
        .thenReturn(Optional.of(stage1));

        mockMvc.perform(delete("/projects/1/stages/1"))
        .andExpect(status().isNoContent());

        Mockito.verify(stageRepository).deleteById(1L);
    }
}
