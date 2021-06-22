package com.meetingplanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Meetingplanner.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Double pourcentageCapacite;

    private Integer delaiCreneau;

    public Double getPourcentageCapacite() {
        return pourcentageCapacite;
    }

    public void setPourcentageCapacite(Double pourcentageCapacite) {
        this.pourcentageCapacite = pourcentageCapacite;
    }

    public Integer getDelaiCreneau() {
        return delaiCreneau;
    }

    public void setDelaiCreneau(Integer delaiCreneau) {
        this.delaiCreneau = delaiCreneau;
    }
}
