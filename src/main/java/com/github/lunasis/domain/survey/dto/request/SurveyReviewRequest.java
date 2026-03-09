package com.github.lunasis.domain.survey.dto.request;

import com.github.lunasis.domain.survey.entity.ProductReview.ProductName;
import com.github.lunasis.domain.survey.entity.ProductReview.SurveyType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SurveyReviewRequest(
        @NotNull SurveyType surveyType,
        @NotNull ProductName productName,
        @NotNull @DecimalMin("1.0") @DecimalMax("5.0") Double rating,
        @NotNull @Min(1) @Max(5) Integer ranking,
        @Nullable String reviewText
) {
}
