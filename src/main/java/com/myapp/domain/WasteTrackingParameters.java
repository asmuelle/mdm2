package com.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WasteTrackingParameters.
 */
@Entity
@Table(name = "waste_tracking_parameters")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WasteTrackingParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "waste_issue_creation_threshold")
    private Float wasteIssueCreationThreshold;

    @Column(name = "max_waste_issue_creation_rate")
    private Integer maxWasteIssueCreationRate;

    @Column(name = "max_active_waste_issues")
    private Integer maxActiveWasteIssues;

    @Column(name = "auto_create_waste_issues")
    private Boolean autoCreateWasteIssues;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WasteTrackingParameters id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public WasteTrackingParameters name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getWasteIssueCreationThreshold() {
        return this.wasteIssueCreationThreshold;
    }

    public WasteTrackingParameters wasteIssueCreationThreshold(Float wasteIssueCreationThreshold) {
        this.setWasteIssueCreationThreshold(wasteIssueCreationThreshold);
        return this;
    }

    public void setWasteIssueCreationThreshold(Float wasteIssueCreationThreshold) {
        this.wasteIssueCreationThreshold = wasteIssueCreationThreshold;
    }

    public Integer getMaxWasteIssueCreationRate() {
        return this.maxWasteIssueCreationRate;
    }

    public WasteTrackingParameters maxWasteIssueCreationRate(Integer maxWasteIssueCreationRate) {
        this.setMaxWasteIssueCreationRate(maxWasteIssueCreationRate);
        return this;
    }

    public void setMaxWasteIssueCreationRate(Integer maxWasteIssueCreationRate) {
        this.maxWasteIssueCreationRate = maxWasteIssueCreationRate;
    }

    public Integer getMaxActiveWasteIssues() {
        return this.maxActiveWasteIssues;
    }

    public WasteTrackingParameters maxActiveWasteIssues(Integer maxActiveWasteIssues) {
        this.setMaxActiveWasteIssues(maxActiveWasteIssues);
        return this;
    }

    public void setMaxActiveWasteIssues(Integer maxActiveWasteIssues) {
        this.maxActiveWasteIssues = maxActiveWasteIssues;
    }

    public Boolean getAutoCreateWasteIssues() {
        return this.autoCreateWasteIssues;
    }

    public WasteTrackingParameters autoCreateWasteIssues(Boolean autoCreateWasteIssues) {
        this.setAutoCreateWasteIssues(autoCreateWasteIssues);
        return this;
    }

    public void setAutoCreateWasteIssues(Boolean autoCreateWasteIssues) {
        this.autoCreateWasteIssues = autoCreateWasteIssues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WasteTrackingParameters)) {
            return false;
        }
        return id != null && id.equals(((WasteTrackingParameters) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WasteTrackingParameters{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", wasteIssueCreationThreshold=" + getWasteIssueCreationThreshold() +
            ", maxWasteIssueCreationRate=" + getMaxWasteIssueCreationRate() +
            ", maxActiveWasteIssues=" + getMaxActiveWasteIssues() +
            ", autoCreateWasteIssues='" + getAutoCreateWasteIssues() + "'" +
            "}";
    }
}
