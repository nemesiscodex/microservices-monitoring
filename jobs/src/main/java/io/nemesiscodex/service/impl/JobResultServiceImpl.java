package io.nemesiscodex.service.impl;

import io.nemesiscodex.service.JobResultService;
import io.nemesiscodex.domain.JobResult;
import io.nemesiscodex.repository.JobResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing JobResult.
 */
@Service
@Transactional
public class JobResultServiceImpl implements JobResultService {

    private final Logger log = LoggerFactory.getLogger(JobResultServiceImpl.class);

    private final JobResultRepository jobResultRepository;

    public JobResultServiceImpl(JobResultRepository jobResultRepository) {
        this.jobResultRepository = jobResultRepository;
    }

    /**
     * Save a jobResult.
     *
     * @param jobResult the entity to save
     * @return the persisted entity
     */
    @Override
    public JobResult save(JobResult jobResult) {
        log.debug("Request to save JobResult : {}", jobResult);
        return jobResultRepository.save(jobResult);
    }

    /**
     * Get all the jobResults.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<JobResult> findAll() {
        log.debug("Request to get all JobResults");
        return jobResultRepository.findAll();
    }

    /**
     * Get one jobResult by id.
     *
     * @param jobKey the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobResult findOne(String jobKey) {
        log.debug("Request to get JobResult : {}", jobKey);
        return jobResultRepository.findByJobKey(jobKey);
    }

    /**
     * Delete the jobResult by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobResult : {}", id);
        jobResultRepository.delete(id);
    }
}
