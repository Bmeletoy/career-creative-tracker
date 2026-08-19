package com.github.bmeletoy.tracker;


import java.net.URI;
import java.util.Optional;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectId}/stages")
public class StageController {
    private final StageRepository stageRepository;
    private final ProjectRepository projectRepository;

    public StageController(StageRepository stageReposoitory, ProjectRepository projectRepository){
        this.stageRepository = stageReposoitory;
        this.projectRepository = projectRepository;
    }


    @GetMapping("/{stageId}")
    public ResponseEntity<Stage> getStageForProject(@PathVariable Long projectId, @PathVariable Long stageId) {
        Optional<Stage> result = stageRepository.findById(stageId);
        if(result.isPresent() && result.get().getProject().getId().equals(projectId)) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Stage>> getStagesForProject(@PathVariable Long projectId) {
        Optional<Project> result = projectRepository.findById(projectId);
        if(result.isPresent()){
            return ResponseEntity.ok(stageRepository.findByProjectId(projectId));
        }
        return ResponseEntity.notFound().build();
        
    }

    @PostMapping
    public ResponseEntity<Stage> createStage(@PathVariable Long projectId, @RequestBody Stage stage) {
        Optional<Project> result = projectRepository.findById(projectId);
        if(result.isPresent()){
            Project ans = result.get();
            stage.setProject(ans);
            Stage saved = stageRepository.save(stage);
            URI location = URI.create("/projects/" + ans.getId() + "/stages/" + saved.getId());
            return ResponseEntity.created(location).body(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{stageId}")
    public ResponseEntity<Stage> updateStage(@PathVariable Long projectId,  @PathVariable Long stageId, @RequestBody Stage stage){
        Optional<Stage> result = stageRepository.findById(stageId);
        if(result.isPresent() && result.get().getProject().getId().equals(projectId)){
            Stage ans = result.get();
            ans.setEvidenceUrl(stage.getEvidenceUrl());
            ans.setName(stage.getName());
            ans.setCompletedAt(stage.getCompletedAt());
            ans.setNotes(stage.getNotes());
            ans.setStatus(stage.getStatus());
            ans.setOrderIndex(stage.getOrderIndex());
            ans = stageRepository.save(ans);
            return ResponseEntity.ok(ans);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{stageId}")
    public ResponseEntity<Void> deleteStage(@PathVariable Long projectId, @PathVariable Long stageId){
        Optional<Stage> result = stageRepository.findById(stageId);
        if(result.isPresent() && result.get().getProject().getId().equals(projectId)){
            // Stage toDelete = result.get();
            // stageRepository.delete(toDelete);
            stageRepository.deleteById(stageId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
