package com.github.lunasis.domain.user.module;

import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RandomNicknameGenerator {

    private final UserRepository userRepository;

    private final String[] ADJECTIVES = {
            "행복한", "똑똑한", "즐거운", "강한", "빠른", "재치있는", "충성스러운", "멋진", "훌륭한", "아름다운",
            "기쁜", "사랑스러운", "환상적인", "놀라운", "매력적인", "긍정적인", "빛나는", "희망찬", "용감한", "따뜻한",
            "신나는", "친절한", "든든한", "감동적인", "뛰어난", "성실한", "창의적인", "자랑스러운", "유쾌한", "당당한"
    };

    private final String[] REGIONS = {
            "서울", "부산", "제주", "인천", "광주", "대전", "대구", "울산", "수원", "춘천",
            "전주", "여수", "강릉", "경주", "포항", "목포", "순천", "청주", "천안", "평택",
            "도쿄", "뉴욕", "파리", "런던", "베를린", "시드니", "방콕", "싱가포르", "바르셀로나", "로마"
    };

    private final String[] ANIMALS = {
            "사자", "호랑이", "독수리", "상어", "판다", "여우", "늑대", "용", "곰", "매",
            "강아지", "고양이", "토끼", "햄스터", "앵무새", "거북이", "고슴도치", "물고기", "말", "돌고래",
            "펭귄", "코알라", "기린", "수달", "코끼리", "치타", "표범", "하이에나", "미어캣", "플라밍고"
    };

    private final Random random = new Random();

    public String generate() {
        String nickname;
        do {
            String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
            String region = REGIONS[random.nextInt(REGIONS.length)];
            String animal = ANIMALS[random.nextInt(ANIMALS.length)];

            nickname = adjective + region + animal;
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }
}
