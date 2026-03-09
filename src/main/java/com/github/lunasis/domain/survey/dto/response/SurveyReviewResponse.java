package com.github.lunasis.domain.survey.dto.response;

import com.github.lunasis.domain.survey.entity.ProductReview;

public record SurveyReviewResponse(
        ProductReview.SurveyType surveyType,
        ProductReview.ProductName productName,
        Double rating,
        Integer ranking,
        String reviewText
) {
}
