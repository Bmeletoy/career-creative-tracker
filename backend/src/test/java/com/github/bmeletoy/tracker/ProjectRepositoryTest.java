package com.github.bmeletoy.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
public class ProjectRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testSaveAndFindProject(){
        Project project = new Project();
        project.setDueDate(null);
        project.setPurposeGoal("get job");
        project.setTitle("getting Acme job");
        Project saved = entityManager.persistAndFlush(project);
        
        Optional<Project> temp = projectRepository.findById(saved.getId());
        Project retrieved = temp.get();

        assertEquals(saved.getTitle(), retrieved.getTitle()); 
        assertEquals(saved.getPurposeGoal(), retrieved.getPurposeGoal());
        assertEquals(saved.getDueDate(), retrieved.getDueDate());
    }
}
