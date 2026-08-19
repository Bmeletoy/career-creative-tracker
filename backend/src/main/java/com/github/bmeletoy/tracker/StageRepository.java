package com.github.bmeletoy.tracker;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByProjectId(Long projectId);

}