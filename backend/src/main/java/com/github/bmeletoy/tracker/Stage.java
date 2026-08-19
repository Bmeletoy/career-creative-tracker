package com.github.bmeletoy.tracker;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Project project;
    private String name;
    private Integer orderIndex;
    private String status;
    private LocalDate completedAt;
    private String evidenceUrl;
    private String notes;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDate getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }
    public String getEvidenceUrl() {
        return evidenceUrl;
    }
    public void setEvidenceUrl(String evidenceUrl) {
        this.evidenceUrl = evidenceUrl;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    
}
