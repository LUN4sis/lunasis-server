package com.github.lunasis.domain.survey.repository;

import com.github.lunasis.domain.survey.entity.Survey;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
