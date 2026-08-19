package com.github.bmeletoy.tracker;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class ApplicationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Project project;
    private String companyName;
    private String skillsRequired;
    private String resumeVariant;
    private String source;
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
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getSkillsRequired() {
        return skillsRequired;
    }
    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }
    public String getResumeVariant() {
        return resumeVariant;
    }
    public void setResumeVariant(String resumeVariant) {
        this.resumeVariant = resumeVariant;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    
}
