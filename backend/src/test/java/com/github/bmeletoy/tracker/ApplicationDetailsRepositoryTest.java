package com.github.bmeletoy.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
public class ApplicationDetailsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationDetailsRepository applicationDetailsRepository;
    
    private Project project;

    @BeforeEach
    void setUp(){
        project = new Project();
        entityManager.persistAndFlush(project);
    }

    @Test
    void testSaveAndFindApplicationDetails(){
        ApplicationDetails applicationDetails = new ApplicationDetails();

        applicationDetails.setCompanyName("Acme");
        applicationDetails.setSkillsRequired("python, java");
        applicationDetails.setProject(project);

      
        ApplicationDetails savedDetails = entityManager.persistAndFlush(applicationDetails);

        Optional<ApplicationDetails> tempDetails = applicationDetailsRepository.findById(savedDetails.getId());
        ApplicationDetails retrievedDetails = tempDetails.get();
        
        assertEquals(savedDetails.getCompanyName(), retrievedDetails.getCompanyName());
        assertEquals(savedDetails.getSkillsRequired(), retrievedDetails.getSkillsRequired());
        assertEquals(project.getId(), retrievedDetails.getProject().getId());
    }

    @Test
    void testFindByProjectId (){
        ApplicationDetails applicationDetails = new ApplicationDetails();

        applicationDetails.setCompanyName("Acme");
        applicationDetails.setSkillsRequired("python, java");
        applicationDetails.setProject(project);

        
        ApplicationDetails savedDetails = entityManager.persistAndFlush(applicationDetails);

        Optional<ApplicationDetails> tempDetails = applicationDetailsRepository.findByProjectId(project.getId());
        ApplicationDetails retrievedDetails = tempDetails.get();
        
        assertEquals(savedDetails.getCompanyName(), retrievedDetails.getCompanyName());
        assertEquals(savedDetails.getSkillsRequired(), retrievedDetails.getSkillsRequired());
        assertEquals(project.getId(), retrievedDetails.getProject().getId());

    }

    @Test
    void testEmptyApplicationDetails(){
        Optional<ApplicationDetails> tempDetails = applicationDetailsRepository.findByProjectId(project.getId());
        assertTrue(tempDetails.isEmpty());
    }
    
}
