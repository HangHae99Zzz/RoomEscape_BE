package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.responseDto.QuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;
import java.util.stream.Stream;

import static com.project.roomescape.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class QuizService {

    private final RoomRepository roomRepository;

    // Quiz 조회하기
    public QuizResponseDto getQuiz(Long roomId, String quizType) {

        QuizResponseDto quizResponseDto;
        if (quizType.equals("Aa")) {
            quizResponseDto = getQuizAa();
        } else if (quizType.equals("Ba")) {
            quizResponseDto = getQuizBa(roomId);
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }
        return quizResponseDto;
    }


    private QuizResponseDto getQuizAa() {
        Random random = new Random();

        String question = "지금 몇시지?";

        // 1~12 중 랜덤
        int a = random.nextInt(11) + 1;
        // 97(a) ~ 108(l) 중 랜덤
        char b = (char) (random.nextInt(11) + 97);
        boolean q = random.nextBoolean();
        String direction = (q) ? "앞으로" : "거꾸로";
        String content = "어제 " + a + "시에 잔거 같다. 시간이 " + direction + " 돌고 있어. "
                + b + "라고 써있는 건 뭐지? 지금 몇시지?";

        String clue = "";
        String hint = "시계를 돌려볼까?";

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        String answer = ans > 12 ? String.valueOf(ans - 12) : String.valueOf(Math.abs(ans));
        return new QuizResponseDto(question, content, clue, hint, answer);
    }

    private QuizResponseDto getQuizBa(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));

        // clue
        Long clueA = room.getClueA();
        Long clueB = room.getClueB();
        String clueC = room.getClueC();
        Long clueABC = (clueC.equals("+")) ? clueA + clueB : Math.abs(clueA - clueB);

        String question = "비밀번호";
        String content = "HackerRoom";

        String clue = "포스터들을 눈여겨 보세요";
        String hint = "r = 5";

        String answer = "";
        int[] arr =  Stream.of(String.valueOf(clueABC).split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < arr.length; i++) {
            answer += content.charAt(arr[i]);
        }
        return  new QuizResponseDto(question, content, clue, hint, answer);
    }

    // count +1
    @Transactional
    public void getCount(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
        Long count = room.getCount();
        room.setCount(count + 1);
        roomRepository.save(room);
    }

}
