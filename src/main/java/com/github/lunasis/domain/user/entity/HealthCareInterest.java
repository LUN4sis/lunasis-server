package com.github.lunasis.domain.user.entity;

public enum HealthCareInterest {
    GYNECOLOGY_QNA,        // 여성 의학 궁금증 해결 → GynecologyInterest 선택으로 연결
    FIND_HOSPITAL,         // 나에게 맞는 여성병원 찾기 → HospitalPriority 선택으로 연결
    PRODUCT_REVIEW,        // 여성용품 후기 탐색 (단순 선택)
    HEALTH_REPORT          // 맞춤형 건강 보고서 (단순 선택)
}
