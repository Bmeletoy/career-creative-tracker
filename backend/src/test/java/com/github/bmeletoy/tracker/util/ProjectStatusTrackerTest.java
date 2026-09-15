package com.github.bmeletoy.tracker.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.bmeletoy.tracker.Project;
import com.github.bmeletoy.tracker.Stage;

public class ProjectStatusTrackerTest {

    @Test
    void testEmptyIsDone(){
        List<Stage> stages = new ArrayList<>();

        assertFalse(ProjectStatusTracker.isDone(stages));
    }

    @Test
    void testAllOptionalIsDone(){
        List<Stage> stages = new ArrayList<>();
        Stage optional1 = new Stage();
        optional1.setOptional(true);
        Stage optional2 = new Stage();
        optional2.setOptional(true);

        stages.add(optional2);
        stages.add(optional1);

        assertFalse(ProjectStatusTracker.isDone(stages));
    }

    @Test
    void testAllRequiredAndDoneIsDone(){
        Stage required = new Stage();
        required.setOptional(false);
        required.setStatus("done");
        Stage required2 = new Stage();
        required2.setOptional(false);
        required2.setStatus("done");

        List<Stage> stages = List.of(required, required2);

        assertTrue(ProjectStatusTracker.isDone(stages));
    }

    @Test
    void testAllButOneNotDoneIsDone(){
        Stage required = new Stage();
        required.setOptional(false);
        required.setStatus("done");
        Stage required2 = new Stage();
        required2.setOptional(false);
        required2.setStatus("done");
        Stage required3 = new Stage();
        required3.setOptional(false);
        required3.setStatus("in progress");

        List<Stage> stages = List.of(required, required2, required3);

        assertFalse(ProjectStatusTracker.isDone(stages));

    }

    @Test
    void testNullIsOverdue(){
        Project project = new Project();
        project.setDueDate(null);
        List<Stage> test = new ArrayList<>();

        assertFalse(ProjectStatusTracker.isOverdue(project, test));

    }

    @Test
    void testPastDueDoneisOverdue(){
        Project project = new Project();
        project.setDueDate(LocalDate.now().minusDays(5));
         Stage required = new Stage();
        required.setOptional(false);
        required.setStatus("done");
        Stage required2 = new Stage();
        required2.setOptional(false);
        required2.setStatus("done");
        Stage required3 = new Stage();
        required3.setOptional(true);
        required3.setStatus("in progress");

        List<Stage> stages = List.of(required, required2, required3);

        assertFalse(ProjectStatusTracker.isOverdue(project, stages));
        
    }
    
    @Test
    void testPastDueNotDoneisOverdue(){
        Project project = new Project();
        project.setDueDate(LocalDate.now().minusDays(5));
         Stage required = new Stage();
        required.setOptional(false);
        required.setStatus("done");
        Stage required2 = new Stage();
        required2.setOptional(false);
        required2.setStatus("done");
        Stage required3 = new Stage();
        required3.setOptional(false);
        required3.setStatus("in progress");

        List<Stage> stages = List.of(required, required2, required3);

        assertTrue(ProjectStatusTracker.isOverdue(project, stages));
        
    }

    @Test
    void testFutureDueNotDueisOverdue(){
        Project project = new Project();
        project.setDueDate(LocalDate.now().plusDays(5));
         Stage required = new Stage();
        required.setOptional(false);
        required.setStatus("done");
        Stage required2 = new Stage();
        required2.setOptional(false);
        required2.setStatus("done");
        Stage required3 = new Stage();
        required3.setOptional(false);
        required3.setStatus("in progress");

        List<Stage> stages = List.of(required, required2, required3);

        assertFalse(ProjectStatusTracker.isOverdue(project, stages));
        
    }  
}
