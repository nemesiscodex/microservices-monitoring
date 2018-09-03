package io.nemesiscodex.repository;

import io.nemesiscodex.domain.JobResult;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobResultRepository extends JpaRepository<JobResult, Long> {
    public JobResult findByJobKey(String jobKey);
}
