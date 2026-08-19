package com.github.bmeletoy.tracker;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationDetailsRepository extends JpaRepository<ApplicationDetails, Long> {
    Optional<ApplicationDetails> findByProjectId(Long projectId);
}
