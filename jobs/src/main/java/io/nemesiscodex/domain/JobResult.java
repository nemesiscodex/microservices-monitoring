package io.nemesiscodex.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A JobResult.
 */
@Entity
@Table(name = "job_result")
public class JobResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "result")
    private String result;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "job_key")
    private String jobKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public JobResult result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public JobResult creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getJobKey() {
        return jobKey;
    }

    public JobResult jobKey(String jobKey) {
        this.jobKey = jobKey;
        return this;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobResult jobResult = (JobResult) o;
        if (jobResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobResult{" +
            "id=" + getId() +
            ", result='" + getResult() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", jobKey='" + getJobKey() + "'" +
            "}";
    }
}
