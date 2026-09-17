package com.github.bmeletoy.tracker;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.bmeletoy.tracker.exception.ApplicationDetailsNotFoundException;
import com.github.bmeletoy.tracker.exception.DuplicateApplicationDetailsException;
import com.github.bmeletoy.tracker.exception.ProjectNotFoundException;

@RestController
@RequestMapping("/projects/{projectId}/application-details")
public class ApplicationDetailsController {
    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final ProjectRepository projectRepository;

    public ApplicationDetailsController(ApplicationDetailsRepository applicationDetailsRepository, ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
        this.applicationDetailsRepository = applicationDetailsRepository;
    }

    @GetMapping
    public ResponseEntity<ApplicationDetails> getApplicationDetails(@PathVariable Long projectId){
        Optional<Project> ans = projectRepository.findById(projectId);
        Optional<ApplicationDetails> result = applicationDetailsRepository.findByProjectId(projectId);
        if(result.isPresent() && ans.isPresent()){
           return ResponseEntity.ok(result.get());
        } else if(ans.isEmpty()){
           throw new ProjectNotFoundException("Project: " +  projectId + " not found");
        } else { 
              throw new ApplicationDetailsNotFoundException("Application Details not found for project:  " + projectId);
        }
    }

    @PostMapping 
    public ResponseEntity<ApplicationDetails> createApplicationDetails(@PathVariable Long projectId, @RequestBody ApplicationDetails applicationDetails){
        Optional<Project> result = projectRepository.findById(projectId);
        if(result.isPresent() && applicationDetailsRepository.findByProjectId(projectId).isEmpty()){
            Project ans = result.get();
            applicationDetails.setProject(ans);
            ApplicationDetails saved = applicationDetailsRepository.save(applicationDetails);
            URI location = URI.create("/projects/" + ans.getId() + "/application-details");
            return ResponseEntity.created(location).body(saved);
        }
         if(result.isEmpty()){
            throw new ProjectNotFoundException("Project Not Found: " + projectId);
         } else {
            throw new DuplicateApplicationDetailsException("Project: " + projectId + " already has an existing Application-Detail");
         } 
       
    }

    @PutMapping
    public ResponseEntity<ApplicationDetails> updateApplicationDetails(@PathVariable Long projectId, @RequestBody ApplicationDetails applicationDetails){
        Optional<ApplicationDetails> res = applicationDetailsRepository.findByProjectId(projectId);
        Optional<Project> proj = projectRepository.findById(projectId);
        if(res.isPresent() && proj.isPresent()){
            ApplicationDetails ans = res.get();
            ans.setCompanyName(applicationDetails.getCompanyName());
            ans.setResumeVariant(applicationDetails.getResumeVariant());
            ans.setSkillsRequired(applicationDetails.getSkillsRequired());
            ans.setSource(applicationDetails.getSource());
            ans = applicationDetailsRepository.save(ans);
            return ResponseEntity.ok(ans);
        }
        if(proj.isEmpty()){
           throw new ProjectNotFoundException("Project: " +  projectId + " not found");
        } else { 
              throw new ApplicationDetailsNotFoundException("Application Details not found for project:  " + projectId);
        }
        
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteApplicationDetails(@PathVariable Long projectId){
        Optional<ApplicationDetails> res = applicationDetailsRepository.findByProjectId(projectId);
        Optional<Project> proj = projectRepository.findById(projectId);
        if(res.isPresent() && proj.isPresent()){
            ApplicationDetails ans = res.get();
            applicationDetailsRepository.delete(ans);
            return ResponseEntity.noContent().build();
        }
        if(proj.isEmpty()){
           throw new ProjectNotFoundException("Project: " +  projectId + " not found");
        } else { 
              throw new ApplicationDetailsNotFoundException("Application Details not found for project:  " + projectId);
        }
    }
}
