package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.*;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.repository.QuizRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.responseDto.QuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static com.project.roomescape.exception.ErrorCode.QUIZ_NOT_FOUND;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class QuizService {

    private final RoomRepository roomRepository;
    private final QuizRepository quizRepository;
    private final ClueRepository clueRepository;

    // Quiz 조회하기
    public QuizResponseDto getQuiz(Long roomId, String quizType) {
        QuizResponseDto quizResponseDto = new QuizResponseDto();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

        Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
        if(temporary.isPresent()) {
            Quiz quiz = temporary.get();
            quizResponseDto = new QuizResponseDto(
                    quiz.getQuestion(), quiz.getContent(), quiz.getHint(),
                    quiz.getChance(), quiz.getAnswer(), quiz.getPass());
        }
        else {
            if (quizType.equals("Aa")) quizResponseDto = getQuizAa(room, quizType);
            if (quizType.equals("Ab")) quizResponseDto = getQuizAb(room, quizType);
            if (quizType.equals("Ba")) quizResponseDto = getQuizBa(room, quizType);
            if (quizType.equals("Bb")) quizResponseDto = getQuizBb(room, quizType);
            if (quizType.equals("Ca")) quizResponseDto = getQuizCa(room, quizType);
        }
        return quizResponseDto;
    }

    @Transactional
    public QuizResponseDto getQuizAa(Room room, String quizType) {
        Random random = new Random();
        String question = "지금 몇시지?";

        // 1~12 중 랜덤
        int a = random.nextInt(12) + 1;
        // 97(a) ~ 108(l) 중 랜덤
        char b = (char) (random.nextInt(12) + 97);
        boolean q = random.nextBoolean();
        String direction = (q) ? "앞으로" : "거꾸로";
        String content = "어제 " + a + "시에 잔거 같다. 시간이 " + direction + " 돌고 있어. "
                + b + "라고 써있는 건 뭐지? 지금 몇시지?";

        String hint = null;
        String chance = "시계를 돌려볼까?";

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        if (ans < 0) ans += 12;
        if (ans > 12) ans -= 12;
        String answer = String.valueOf(ans);
        Pass pass = Pass.FAIL;
//        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }

    @Transactional
    public QuizResponseDto getQuizAb(Room room, String quizType) {
        Random random = new Random();
        String content;
        String answer;
        List<String> arr = new ArrayList<>();
        int temp;
//        짝수들
        List<Integer> even = new ArrayList<Integer>() {{
            add(0);
            add(2);
            add(4);
        }};
//      홀수들
        List<Integer> odd = new ArrayList<Integer>() {{
            add(1);
            add(3);
            add(5);
        }};

        int[] count = {0, 0, 0, 0, 0, 0};

        String question = "바이러스에 걸린 컴퓨터를 구할 숫자는?";
//첫번째 시작이 짝수이냐 홀수이냐를 결정하기 위한 랜덤값.
        int standard1 = random.nextInt(2);
        int standard2;

//        첫번쨰 숫자가 짝수인 경우
        if(standard1 % 2 == 0) {
            for (int i = 0; i < 15; i++) {
                temp = random.nextInt(even.size());
//                슷자에 해당되는 카운트를 하나 올려준다.
                count[even.get(temp)]++;
//                문제를 만든다.
                arr.add(String.valueOf(even.get(temp)));
                if(count[even.get(temp)] == 5) {
                    even.remove(temp);
                }

                temp = random.nextInt(odd.size());
                count[odd.get(temp)]++;
                arr.add(String.valueOf(odd.get(temp)));
                if(count[odd.get(temp)] == 5) {
                    odd.remove(temp);
                }
            }

        } else {
            for (int i = 0; i < 15; i++) {
                temp = random.nextInt(odd.size());
                count[odd.get(temp)]++;
                arr.add(String.valueOf(odd.get(temp)));
                if(count[odd.get(temp)] == 5) {
                    odd.remove(temp);
                }

                temp = random.nextInt(even.size());
                count[even.get(temp)]++;
                arr.add(String.valueOf(even.get(temp)));
                if(count[even.get(temp)] == 5) {
                    even.remove(temp);
                }
            }
        }

//        ?가 들어갈 위치 선정.
        standard1 = random.nextInt(30);
        if(standard1 % 2 == 0) {
            standard2 = random.nextInt(15) * 2 + 1;
        } else {
            standard2 = random.nextInt(15) * 2;
        }

//      standard1이 항상 standard2보다 작게끔 만든다.
        if(standard1 > standard2) {
            temp = standard1;
            standard1 = standard2;
            standard2 = temp;
        }

//        정답.
        answer = arr.get(standard1) + ", " + arr.get(standard2);

//        정답이 들어가는 부분 숫자에서 ?로 바꾼다.
        arr.set(standard1, "?");
        arr.set(standard2, "?");
//      list형식을 하나의 String으로 바꿔준다.
        content = arr.toString();

        String hint = null;
        String chance = "홀짝";
        Pass pass = Pass.FAIL;

        //        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);

        return new QuizResponseDto(question, content, hint, chance, answer, pass);

    }

    @Transactional
    public QuizResponseDto getQuizBa(Room room, String quizType) {
        List<Clue> clueList = clueRepository.findAllByRoomId(room.getId());

        Long clueA = 0L;
        Long clueB = 0L;
        String clueC = "";

        for (Clue clue : clueList) {
            if (clue.getType().equals("Ba1")) clueA = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba2")) clueB = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba3")) clueC = clue.getContent();
        }
        // 5016
        Long clueABC = (clueC.equals("+")) ? clueA + clueB : Math.abs(clueA - clueB);

        String question = "비밀번호";
        String content = "HackerRoom";

        String hint = "포스터들을 눈여겨 보세요";
        String chance = "r = 5";

        String answer = "";
        // [5, 0, 1, 6]
        int[] arr = Stream.of(String.valueOf(clueABC).split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < arr.length; i++) {
            answer += content.charAt(arr[i]);
        }
        Pass pass = Pass.FAIL;
        //        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .hint(hint)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }

    @Transactional
    public QuizResponseDto getQuizBb(Room room, String quizType){


        String question = "이제 꿈에서 깨어날 시간입니다.";

        String content = "";

        String chance = "3글자";

        String hint = "그림에 적혀있는 물건 속에 비밀번호가 들어있습니다.";

        String answer = "7799";
        Pass pass = Pass.FAIL;
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .hint(hint)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }


    //  ㄱㄴㄷㅁ 퀴즈
    @Transactional
    public QuizResponseDto getQuizCa(Room room, String quizType){
        Random random = new Random();

        ArrayList<String> questionList = new ArrayList<String>();
        questionList.add("ㄱ");
        questionList.add("ㄴ");
        questionList.add("ㄷ");
        questionList.add("ㅁ");
        // questionList에서 랜덤으로 문제 가져오기
        int num1 = random.nextInt(questionList.size());
        int num2 = random.nextInt(questionList.size());
        int num3 = random.nextInt(questionList.size());
        int num4 = random.nextInt(questionList.size());
        String a = questionList.get(num1);
        String b = questionList.get(num2);
        String c = questionList.get(num3);
        String d = questionList.get(num4);
        String question = a+b+c+d+"?";
        String content = null;
        String hint = null;
        String chance = "test중 낫 놓고...";

        questionList.set(0, "G");
        questionList.set(1, "C");
        questionList.set(2, "F");
        questionList.set(3, "E");
        // answer
        String answer = questionList.get(num1)+questionList.get(num2)+questionList.get(num3)+questionList.get(num4);
        Pass pass = Pass.FAIL;
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }


    @Transactional
    public void finishedQuiz(Long roomId, String quizType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));
        Quiz quiz;

        Optional<Quiz> tempQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(tempQuiz.isPresent()) {
            quiz = tempQuiz.get();
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }
        quiz.finishedQuiz();
        quizRepository.save(quiz);
    }

}
