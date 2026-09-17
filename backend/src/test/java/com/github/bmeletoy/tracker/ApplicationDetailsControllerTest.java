package com.github.bmeletoy.tracker;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class ApplicationDetailsControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApplicationDetailsRepository appDetailsRepo;

    @MockitoBean
    private ProjectRepository projectRepository;

    private Project project, proj2, proj3;

    private ApplicationDetails appDet1, appDet2, appDet3;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setTitle("Acme job");
        project.setId(1L);

        appDet1 = new ApplicationDetails();
        appDet1.setProject(project);
        appDet1.setSource("LinkedIn.com");

        proj2 = new Project();
        proj2.setTitle("Brivo Job");
        proj2.setId(2L);

        appDet2 = new ApplicationDetails();
        appDet2.setProject(proj2);
        appDet2.setSource("JobRight.ai");

        proj3 = new Project();
        proj3.setTitle("Government Job");
        proj3.setId(3L);

        appDet3 = new ApplicationDetails();
        appDet3.setSource("Handshake.com");
        appDet3.setCompanyName("fusionSpan");
        appDet3.setResumeVariant("MasterSwe.docx");
        

    }

    @Test
    void testGetApplicationDetailsFound() throws Exception {
        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(appDetailsRepo.findByProjectId(1L)).
        thenReturn(Optional.of(appDet1));

        mockMvc.perform(get("/projects/1/application-details"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.source").value("LinkedIn.com"));
    }

    @Test
    void testGetApplicationDetailsNoDetails() throws Exception{
        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.of(proj3));

        Mockito.when(appDetailsRepo.findByProjectId(3L)).
        thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/3/application-details"))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Application Details not found")));
    }

    @Test
    void testGetApplicationDetailsNoProject() throws Exception{
         Mockito.when(projectRepository.findById(4L))
        .thenReturn(Optional.empty());

        Mockito.when(appDetailsRepo.findByProjectId(4L)).
        thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/4/application-details"))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Project: 4 not found")));
    }

    @Test
    void testCreateApplicationDetails() throws Exception{
        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.of(proj3));

        Mockito.when(appDetailsRepo.findByProjectId(3L))
        .thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(appDet3);

        Mockito.when(appDetailsRepo.save(Mockito.any(ApplicationDetails.class)))
        .thenReturn(appDet3);

        mockMvc.perform(post("/projects/3/application-details")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.source").value("Handshake.com"))
        .andExpect(header().string("location", "/projects/3/application-details"));      
    }

    @Test
    void testCreateApplicationDetailsNoProject() throws Exception{
        Mockito.when(projectRepository.findById(4L))
        .thenReturn(Optional.empty());

        Mockito.when(appDetailsRepo.findByProjectId(4L))
        .thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(appDet3);

        mockMvc.perform(post("/projects/4/application-details")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Project Not Found: 4"));
    }

    @Test
    void testCreateApplicationDetailsDuplicate() throws Exception{
        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(appDetailsRepo.findByProjectId(1L))
        .thenReturn(Optional.of(appDet1));

        String json = objectMapper.writeValueAsString(appDet3);

        mockMvc.perform(post("/projects/1/application-details")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Project: 1 already has an existing Application-Detail"));
    }

    @Test
    void testUpdateApplicationDetails() throws Exception{
    
        String json = objectMapper.writeValueAsString(appDet3);

        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(appDetailsRepo.findByProjectId(1L))
        .thenReturn(Optional.of(appDet1));

        Mockito.when(appDetailsRepo.save(Mockito.any(ApplicationDetails.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/projects/1/application-details").contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isOk())
        .andExpect(jsonPath("$.source").value("Handshake.com"))
        .andExpect(jsonPath("$.companyName").value("fusionSpan"))
        .andExpect(jsonPath("$.resumeVariant").value("MasterSwe.docx"));
    }

    @Test
    void testUpdateNoDetails() throws Exception{
        String json = objectMapper.writeValueAsString(appDet3);

        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.of(proj3));

        Mockito.when(appDetailsRepo.findByProjectId(3L))
        .thenReturn(Optional.empty());

        mockMvc.perform(put("/projects/3/application-details")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Application Details not found for project:  3")));
    }
    
    @Test
    void testUpdateNoProject() throws Exception{
        String json = objectMapper.writeValueAsString(appDet3);

        Mockito.when(projectRepository.findById(4L))
        .thenReturn(Optional.empty());

        Mockito.when(appDetailsRepo.findByProjectId(4L))
        .thenReturn(Optional.empty());

        mockMvc.perform(put("/projects/4/application-details")
        .contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Project: 4 not found")));
    }

    @Test
    void testDeleteFound() throws Exception{
        Mockito.when(projectRepository.findById(1L))
        .thenReturn(Optional.of(project));

        Mockito.when(appDetailsRepo.findByProjectId(1L)).
        thenReturn(Optional.of(appDet1));

        mockMvc.perform(delete("/projects/1/application-details"))
        .andExpect(status().isNoContent());

        Mockito.verify(appDetailsRepo).delete(appDet1);
    }

    @Test
    void testDeleteNoProject() throws Exception{
        Mockito.when(projectRepository.findById(4L))
        .thenReturn(Optional.empty());

        Mockito.when(appDetailsRepo.findByProjectId(4L)).
        thenReturn(Optional.empty());

        mockMvc.perform(delete("/projects/4/application-details"))
        .andExpect(status().isNotFound())
       .andExpect(content().string(containsString("Project: 4 not found")));
    }

    @Test
    void testDeleteNoDetails() throws Exception{
        Mockito.when(projectRepository.findById(3L))
        .thenReturn(Optional.of(proj3));

        Mockito.when(appDetailsRepo.findByProjectId(3L)).
        thenReturn(Optional.empty());

        mockMvc.perform(delete("/projects/3/application-details"))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Application Details not found for project:  3")));
    }
}
