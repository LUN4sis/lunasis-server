package com.github.lunasis.domain.survey.service;

import com.github.lunasis.domain.survey.dto.request.SurveyRequest;
import com.github.lunasis.domain.survey.dto.request.SurveyReviewRequest;
import com.github.lunasis.domain.survey.dto.response.SurveyResponse;
import com.github.lunasis.domain.survey.dto.response.SurveyReviewResponse;
import com.github.lunasis.domain.survey.entity.ProductReview;
import com.github.lunasis.domain.survey.entity.ProductReview.SurveyType;
import com.github.lunasis.domain.survey.entity.Survey;
import com.github.lunasis.domain.survey.repository.SurveyRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final UserService userService;
    private final SurveyRepository surveyRepository;

    @Transactional
    public void saveSurvey(UUID userId, SurveyRequest surveyRequest) {

        User user = userService.getUserById(userId);

        Survey survey = surveyRepository.findByUserId(userId)
                .orElseGet(() -> Survey.builder()
                        .user(user)
                        .build());

        survey.updateMessage(surveyRequest.messageToCreator());

        if (surveyRequest.tamponReviews() != null && !surveyRequest.tamponReviews().isEmpty()) {
            updateReviewsByType(survey, SurveyType.SURVEY_TAMPON, surveyRequest.tamponReviews());
        }

        if (surveyRequest.sanitaryReviews() != null && !surveyRequest.sanitaryReviews().isEmpty()) {
            updateReviewsByType(survey, SurveyType.SURVEY_SANITARY, surveyRequest.sanitaryReviews());
        }

        surveyRepository.save(survey);
    }

    private void updateReviewsByType(Survey survey, SurveyType surveyType, List<SurveyReviewRequest> newReviews) {

        survey.getProductReviews().removeIf(review -> review.getSurveyType() == surveyType);

        newReviews.forEach(reviewRequest -> {
            ProductReview productReview = ProductReview.builder()
                    .survey(survey)
                    .surveyType(reviewRequest.surveyType())
                    .productName(reviewRequest.productName())
                    .reviewText(reviewRequest.reviewText())
                    .rating(reviewRequest.rating())
                    .ranking(reviewRequest.ranking())
                    .build();

            survey.getProductReviews().add(productReview);
        });
    }

    public SurveyResponse getSurvey(UUID userId) {

        Survey survey = surveyRepository.findByUserId(userId).orElse(null);

        if (survey == null) {
            return new SurveyResponse(null, null, null);
        }

        List<SurveyReviewResponse> tamponReviews = new ArrayList<>();
        List<SurveyReviewResponse> sanitaryReviews = new ArrayList<>();

        survey.getProductReviews().forEach((review) -> {
            SurveyReviewResponse reviewResponse = new SurveyReviewResponse(
                    review.getSurveyType(),
                    review.getProductName(),
                    review.getRating(),
                    review.getRanking(),
                    review.getReviewText()
            );

            if (review.getSurveyType() == SurveyType.SURVEY_TAMPON) {
                tamponReviews.add(reviewResponse);
            } else if (review.getSurveyType() == SurveyType.SURVEY_SANITARY) {
                sanitaryReviews.add(reviewResponse);
            }
        });

        return new SurveyResponse(
                tamponReviews.isEmpty() ? null : tamponReviews,
                sanitaryReviews.isEmpty() ? null : sanitaryReviews,
                survey.getMessage()
        );

    }
}
