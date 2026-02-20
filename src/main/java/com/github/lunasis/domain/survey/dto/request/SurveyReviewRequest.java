package com.github.lunasis.domain.survey.dto.request;

import com.github.lunasis.domain.survey.entity.ProductReview.ProductName;
import com.github.lunasis.domain.survey.entity.ProductReview.SurveyType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SurveyReviewRequest(
        @NotNull SurveyType surveyType,
        @NotNull ProductName productName,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotNull @Min(1) @Max(5) Integer ranking,
        @Nullable String reviewText
) {
}
