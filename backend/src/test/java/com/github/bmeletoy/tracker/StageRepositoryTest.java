package com.github.bmeletoy.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
public class StageRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StageRepository stageRepository;

    @Test
    void testSaveAndFindStage(){
        Project project = new Project();

        Stage stage = new Stage();

        stage.setName("Planning phase");
        stage.setProject(project);
        
        Project saved = entityManager.persistAndFlush(project);
        Stage savedStage = entityManager.persistAndFlush(stage);

        Optional<Stage> tempStage = stageRepository.findById(savedStage.getId());
        Stage stageRetrieved = tempStage.get();

        assertEquals(savedStage.getName(), stageRetrieved.getName());
        assertEquals(saved.getId(), stageRetrieved.getProject().getId());

    }
}
