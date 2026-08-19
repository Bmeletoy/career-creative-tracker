package com.github.bmeletoy.tracker;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
public class Project{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String title;
    private String domain;
    private String medium;
    private String purposeGoal;
    private String checkInFrequency;
    private LocalDate startDate;
    private LocalDate dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDomain(){
        return domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPurposeGoal() {
        return purposeGoal;
    }

    public void setPurposeGoal(String purposeGoal) {
        this.purposeGoal = purposeGoal;
    }

    public String getCheckInFrequency() {
        return checkInFrequency;
    }

    public void setCheckInFrequency(String checkInFrequency) {
        this.checkInFrequency = checkInFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    
}
