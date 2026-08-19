package com.github.bmeletoy.tracker;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id){
        Optional<Project> result = projectRepository.findById(id);
        if (result.isPresent()){
            Project ans = result.get();
            return ResponseEntity.ok(ans);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProjects(@RequestBody Project project){
       Project ans =  projectRepository.save(project); 
       URI location = URI.create("/projects/" + ans.getId());
       return ResponseEntity.created(location).body(ans);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project){
        Optional<Project> tempUpdate = projectRepository.findById(id);
        if(tempUpdate.isPresent()){
            Project toUpdate = tempUpdate.get();
            toUpdate.setTitle(project.getTitle());
            toUpdate.setDomain(project.getDomain());
            toUpdate.setMedium(project.getMedium());
            toUpdate.setPurposeGoal(project.getPurposeGoal());
            toUpdate.setCheckInFrequency(project.getCheckInFrequency());
            toUpdate.setStartDate(project.getStartDate());
            toUpdate.setDueDate(project.getDueDate());

            Project saved = projectRepository.save(toUpdate);

            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Optional<Project> tempDelete = projectRepository.findById(id);
        if(tempDelete.isPresent()){
            Project toDelete = tempDelete.get(); 
            projectRepository.delete(toDelete);
            return ResponseEntity.noContent().build();
        } 
        return ResponseEntity.notFound().build();
    }
    
}
