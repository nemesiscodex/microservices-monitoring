package io.nemesiscodex.web.rest;

import io.nemesiscodex.JobsApp;

import io.nemesiscodex.domain.JobResult;
import io.nemesiscodex.repository.JobResultRepository;
import io.nemesiscodex.service.JobResultService;
import io.nemesiscodex.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.nemesiscodex.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobResultResource REST controller.
 *
 * @see JobResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobsApp.class)
public class JobResultResourceIntTest {

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_JOB_KEY = "AAAAAAAAAA";
    private static final String UPDATED_JOB_KEY = "BBBBBBBBBB";

    @Autowired
    private JobResultRepository jobResultRepository;

    @Autowired
    private JobResultService jobResultService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobResultMockMvc;

    private JobResult jobResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResultResource jobResultResource = new JobResultResource(jobResultService);
        this.restJobResultMockMvc = MockMvcBuilders.standaloneSetup(jobResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobResult createEntity(EntityManager em) {
        JobResult jobResult = new JobResult()
            .result(DEFAULT_RESULT)
            .creationDate(DEFAULT_CREATION_DATE)
            .jobKey(DEFAULT_JOB_KEY);
        return jobResult;
    }

    @Before
    public void initTest() {
        jobResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobResult() throws Exception {
        int databaseSizeBeforeCreate = jobResultRepository.findAll().size();

        // Create the JobResult
        restJobResultMockMvc.perform(post("/api/job-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobResult)))
            .andExpect(status().isCreated());

        // Validate the JobResult in the database
        List<JobResult> jobResultList = jobResultRepository.findAll();
        assertThat(jobResultList).hasSize(databaseSizeBeforeCreate + 1);
        JobResult testJobResult = jobResultList.get(jobResultList.size() - 1);
        assertThat(testJobResult.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testJobResult.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testJobResult.getJobKey()).isEqualTo(DEFAULT_JOB_KEY);
    }

    @Test
    @Transactional
    public void createJobResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobResultRepository.findAll().size();

        // Create the JobResult with an existing ID
        jobResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobResultMockMvc.perform(post("/api/job-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobResult)))
            .andExpect(status().isBadRequest());

        // Validate the JobResult in the database
        List<JobResult> jobResultList = jobResultRepository.findAll();
        assertThat(jobResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobResults() throws Exception {
        // Initialize the database
        jobResultRepository.saveAndFlush(jobResult);

        // Get all the jobResultList
        restJobResultMockMvc.perform(get("/api/job-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobKey").value(hasItem(DEFAULT_JOB_KEY.toString())));
    }

    @Test
    @Transactional
    public void getJobResult() throws Exception {
        // Initialize the database
        jobResultRepository.saveAndFlush(jobResult);

        // Get the jobResult
        restJobResultMockMvc.perform(get("/api/job-results/{id}", jobResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobResult.getId().intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.jobKey").value(DEFAULT_JOB_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobResult() throws Exception {
        // Get the jobResult
        restJobResultMockMvc.perform(get("/api/job-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobResult() throws Exception {
        // Initialize the database
        jobResultService.save(jobResult);

        int databaseSizeBeforeUpdate = jobResultRepository.findAll().size();

        // Update the jobResult
        JobResult updatedJobResult = jobResultRepository.findOne(jobResult.getId());
        // Disconnect from session so that the updates on updatedJobResult are not directly saved in db
        em.detach(updatedJobResult);
        updatedJobResult
            .result(UPDATED_RESULT)
            .creationDate(UPDATED_CREATION_DATE)
            .jobKey(UPDATED_JOB_KEY);

        restJobResultMockMvc.perform(put("/api/job-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobResult)))
            .andExpect(status().isOk());

        // Validate the JobResult in the database
        List<JobResult> jobResultList = jobResultRepository.findAll();
        assertThat(jobResultList).hasSize(databaseSizeBeforeUpdate);
        JobResult testJobResult = jobResultList.get(jobResultList.size() - 1);
        assertThat(testJobResult.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testJobResult.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testJobResult.getJobKey()).isEqualTo(UPDATED_JOB_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingJobResult() throws Exception {
        int databaseSizeBeforeUpdate = jobResultRepository.findAll().size();

        // Create the JobResult

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobResultMockMvc.perform(put("/api/job-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobResult)))
            .andExpect(status().isCreated());

        // Validate the JobResult in the database
        List<JobResult> jobResultList = jobResultRepository.findAll();
        assertThat(jobResultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobResult() throws Exception {
        // Initialize the database
        jobResultService.save(jobResult);

        int databaseSizeBeforeDelete = jobResultRepository.findAll().size();

        // Get the jobResult
        restJobResultMockMvc.perform(delete("/api/job-results/{id}", jobResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobResult> jobResultList = jobResultRepository.findAll();
        assertThat(jobResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobResult.class);
        JobResult jobResult1 = new JobResult();
        jobResult1.setId(1L);
        JobResult jobResult2 = new JobResult();
        jobResult2.setId(jobResult1.getId());
        assertThat(jobResult1).isEqualTo(jobResult2);
        jobResult2.setId(2L);
        assertThat(jobResult1).isNotEqualTo(jobResult2);
        jobResult1.setId(null);
        assertThat(jobResult1).isNotEqualTo(jobResult2);
    }
}
