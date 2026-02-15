package com.github.lunasis.domain.survey.entity.request;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SurveyRequest(
        @NotNull List<SurveyReviewRequest> surveyReviewRequests,
        @Nullable String messageToCreator
) {
}
