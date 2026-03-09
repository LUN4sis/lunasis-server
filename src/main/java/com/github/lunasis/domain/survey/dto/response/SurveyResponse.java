package com.github.lunasis.domain.survey.dto.response;

import java.util.List;

public record SurveyResponse(
        List<SurveyReviewResponse> tamponReviews,
        List<SurveyReviewResponse> sanitaryReviews,
        String messageToCreator
) {
}
