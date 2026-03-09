package com.github.lunasis.domain.survey.controller;

import com.github.lunasis.domain.survey.dto.request.SurveyRequest;
import com.github.lunasis.domain.survey.dto.response.SurveyResponse;
import com.github.lunasis.domain.survey.service.SurveyService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
@PreAuthorize("isAuthenticated()")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ApiResponse<Void> createSurvey(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid SurveyRequest surveyRequest) {

        surveyService.saveSurvey(user.getId(), surveyRequest);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<SurveyResponse> getSurvey(@AuthenticationPrincipal User user) {

        SurveyResponse surveyResponse = surveyService.getSurvey(user.getId());
        return ApiResponse.ok(surveyResponse);
    }
}
