package io.nemesiscodex.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.nemesiscodex.domain.JobResult;
import io.nemesiscodex.service.JobResultService;
import io.nemesiscodex.web.rest.errors.BadRequestAlertException;
import io.nemesiscodex.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JobResult.
 */
@RestController
@RequestMapping("/api")
public class JobResultResource {

    private final Logger log = LoggerFactory.getLogger(JobResultResource.class);

    private final JobResultService jobResultService;

    public JobResultResource(JobResultService jobResultService) {
        this.jobResultService = jobResultService;
    }

    /**
     * GET  /job-results/:id : get the "id" jobResult.
     *
     * @param jobKey the id of the jobResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobResult, or with status 404 (Not Found)
     */
    @GetMapping("/job-results/{jobKey}")
    @Timed
    public ResponseEntity<JobResult> getJobResult(@PathVariable String jobKey) {
        log.debug("REST request to get JobResult : {}", jobKey);
        JobResult jobResult = jobResultService.findOne(jobKey);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobResult));
    }

}
