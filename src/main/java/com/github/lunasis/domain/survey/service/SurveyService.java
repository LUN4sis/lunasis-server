package com.github.lunasis.domain.survey.service;

import com.github.lunasis.domain.survey.entity.ProductReview;
import com.github.lunasis.domain.survey.entity.Survey;
import com.github.lunasis.domain.survey.entity.request.SurveyRequest;
import com.github.lunasis.domain.survey.entity.request.SurveyReviewRequest;
import com.github.lunasis.domain.survey.repository.SurveyRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final UserService userService;
    private final SurveyRepository surveyRepository;

    public void saveSurvey(UUID userId, SurveyRequest surveyRequest) {

        User user = userService.getUserById(userId);

        Survey newSurvey = Survey.builder()
                .user(user)
                .message(surveyRequest.messageToCreator())
                .build();

        addReview(newSurvey, surveyRequest.surveyReviewRequests());
    }

    private void addReview(Survey survey, List<SurveyReviewRequest> reviews) {

        reviews.forEach(review -> {
            ProductReview productReview = ProductReview.builder()
                    .survey(survey)
                    .surveyType(review.surveyType())
                    .productName(review.productName())
                    .reviewText(review.reviewText())
                    .rating(review.rating())
                    .ranking(review.ranking())
                    .build();

            survey.getProductReviews().add(productReview);
        });

        surveyRepository.save(survey);
    }
}
