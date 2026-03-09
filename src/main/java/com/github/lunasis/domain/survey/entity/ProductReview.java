package com.github.lunasis.domain.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    @Enumerated(EnumType.STRING)
    private ProductName productName;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double rating;

    @Min(1)
    @Max(5)
    private Integer ranking;


    public enum SurveyType {
        SURVEY_TAMPON,
        SURVEY_SANITARY,
    }

    public enum ProductName {
        ORGANIC_BORN,
        TEMPO_NATURAL,
        LUSONG,
        PLAYTEX,
        TAMPAX,
        INERTIA,
        HYYE,
        IMO,
        DANCING_WHALE,
        SOFY
    }
}
