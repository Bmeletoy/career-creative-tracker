package com.github.bmeletoy.tracker.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.github.bmeletoy.tracker.Project;
import com.github.bmeletoy.tracker.Stage;

public class ProjectStatusTracker {
    public static boolean isDone(List<Stage> stages){
        if(stages.size() == 0){
            return false;
        }

       List<Stage> required = stages.stream().filter(stage -> !stage.isOptional())
       .collect(Collectors.toList());

       if(required.isEmpty()){
        return false;
       }

       return required.stream().allMatch(stage -> stage.getStatus()
        .equalsIgnoreCase("done"));
    }

    public static boolean isOverdue(Project project, List<Stage> stages){
        if(project.getDueDate() == null){
            return false;
        }

        if(isDone(stages)){
            return false;
        }

        return project.getDueDate().isBefore(LocalDate.now());
    }
}
