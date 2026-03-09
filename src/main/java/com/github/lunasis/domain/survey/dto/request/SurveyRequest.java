package com.github.lunasis.domain.survey.dto.request;

import io.micrometer.common.lang.Nullable;
import java.util.List;

public record SurveyRequest(
        @Nullable List<SurveyReviewRequest> tamponReviews,
        @Nullable List<SurveyReviewRequest> sanitaryReviews,
        @Nullable String messageToCreator
) {
}
