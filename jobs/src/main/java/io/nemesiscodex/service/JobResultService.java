package io.nemesiscodex.service;

import io.nemesiscodex.domain.JobResult;
import java.util.List;

/**
 * Service Interface for managing JobResult.
 */
public interface JobResultService {

    /**
     * Save a jobResult.
     *
     * @param jobResult the entity to save
     * @return the persisted entity
     */
    JobResult save(JobResult jobResult);

    /**
     * Get all the jobResults.
     *
     * @return the list of entities
     */
    List<JobResult> findAll();

    /**
     * Get the "id" jobResult.
     *
     * @param jobKey the key of the job
     * @return the entity
     */
    JobResult findOne(String jobKey);

    /**
     * Delete the "id" jobResult.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
