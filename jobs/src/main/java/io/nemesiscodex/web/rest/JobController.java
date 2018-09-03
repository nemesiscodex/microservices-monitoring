package io.nemesiscodex.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.nemesiscodex.domain.JobResult;
import io.nemesiscodex.service.JobResultService;
import io.nemesiscodex.web.rest.vm.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/jobs")
public class JobController {

    final
    ExecutorService executor;

    final
    JobResultService jobResultService;

    @Autowired
    public JobController(ExecutorService executor, JobResultService jobResultService) {
        this.executor = executor;
        this.jobResultService = jobResultService;
    }

    @PostMapping(value = "", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Timed
    public Result creatJob(@RequestParam(value = "shouldFail", required = false) Boolean shouldFail, @RequestParam("message") String message) {
        Runnable job;

        job = () -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                // ignore
            }
            if (shouldFail) {
                throw new MyFailException("Fail to process.");
            }

        };

        Result result = new Result("Job scheduled.", true, UUID.randomUUID().toString());

        Instant creationDate = Instant.now();
        JobResult jobResult = new JobResult();
        jobResult.setCreationDate(creationDate);

        jobResult.setJobKey(result.getJobKey());
        jobResult.setResult("Queued");
        final JobResult jobResultHandle = jobResultService.save(jobResult);

        Runnable handle = () -> {

            try {
                job.run();
                jobResultHandle.setResult("Finished");
            } catch (Exception ex) {
                jobResultHandle.setResult("Error: " + ex.getMessage());
            } finally {
                jobResultService.save(jobResultHandle);
            }

        };

        executor.submit(handle);

        return result;

    }

}
