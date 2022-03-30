package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.Clue;
import com.project.roomescape.model.Pass;
import com.project.roomescape.model.Quiz;
import com.project.roomescape.model.Room;
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


@Service
@RequiredArgsConstructor
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
            if (quizType.equals("Aa")) quizResponseDto = createQuizAa(room, quizType);
            if (quizType.equals("Ab")) quizResponseDto = createQuizAb(room, quizType);
            if (quizType.equals("Ba")) quizResponseDto = createQuizBa(room, quizType);
            if (quizType.equals("Bb")) quizResponseDto = createQuizBb(room, quizType);
            if (quizType.equals("Ca")) quizResponseDto = createQuizCa(room, quizType);
        }
        return quizResponseDto;
    }

    @Transactional
    public QuizResponseDto createQuizAa(Room room, String quizType) {
        Random random = new Random();
        String question = "지금 몇시지?";
        final int CLOCKTIME = 12;

        // 1~12 중 랜덤
        int a = random.nextInt(CLOCKTIME) + 1;
        // 97(a) ~ 108(l) 중 랜덤
        char b = (char) (random.nextInt(CLOCKTIME) + 97);
        boolean q = random.nextBoolean();
        String direction = (q) ? "앞으로" : "거꾸로";

        StringBuilder sb = new StringBuilder();
        sb.append("어제 ").append(a).append("시에 잔거 같다. 시간이 ").append(direction)
                .append(" 돌고 있어. ").append(b).append("라고 써있는 건 뭐지? 지금 몇시지?");

        String content = sb.toString();

        String hint = null;
        String chance = "시계를 돌려볼까?";

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        if (ans < 0) ans += CLOCKTIME;
        if (ans > CLOCKTIME) ans -= CLOCKTIME;
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
    public QuizResponseDto createQuizAb(Room room, String quizType) {
        final String QUESTION = "바이러스에 걸린 컴퓨터를 구할 숫자는?";
        final String CHANCE = "홀짝";
        final String HINT = null;
        final int MAX_COUNT = 5;
        final int HALF_SIZE = 15;
        final int TOTAL_SIZE = 30;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String content;
        String answer;
        int temp;
        List<String> arr = new ArrayList<>();
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

//첫번째 시작이 짝수이냐 홀수이냐를 결정하기 위한 랜덤값.
        int standard1 = random.nextInt(2);
        int standard2;

//        첫번쨰 숫자가 짝수인 경우
        if(standard1 % 2 == 0) {
            for (int i = 0; i < HALF_SIZE; i++) {
                createEven(random, MAX_COUNT, even, count, arr);
                createOdd(random, MAX_COUNT, odd, count, arr);
            }
        } else {
            for (int i = 0; i < HALF_SIZE; i++) {
                createOdd(random, MAX_COUNT, odd, count, arr);
                createEven(random, MAX_COUNT, even, count, arr);
            }
        }

//        ?가 들어갈 위치 선정.
        standard1 = random.nextInt(TOTAL_SIZE);
        if(standard1 % 2 == 0) {
            standard2 = random.nextInt(HALF_SIZE) * 2 + 1;
        } else {
            standard2 = random.nextInt(HALF_SIZE) * 2;
        }

//      standard1이 항상 standard2보다 작게끔 만든다.
        if(standard1 > standard2) {
            temp = standard1;
            standard1 = standard2;
            standard2 = temp;
        }
        sb.append(arr.get(standard1)).append(", ").append(arr.get(standard2));

//        정답.
        answer = sb.toString();

//        정답이 들어가는 부분 숫자에서 ?로 바꾼다.
        arr.set(standard1, "?1");
        arr.set(standard2, "?2");
//      list형식을 하나의 String으로 바꿔준다.
        content = arr.toString();

        Pass pass = Pass.FAIL;

        //        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, content, answer, pass)
                .chance(CHANCE)
                .build();
        quizRepository.save(quiz);

        return new QuizResponseDto(QUESTION, content, HINT, CHANCE, answer, pass);

    }

    @Transactional
    public QuizResponseDto createQuizBa(Room room, String quizType) {
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
    public QuizResponseDto createQuizBb(Room room, String quizType){

        final String QUESTION = "이제 꿈에서 깨어날 시간입니다.";

        final String CONTENT = "";

        final String CHANCE = "3글자";

        final String HINT = "그림에 적혀있는 물건 속에 비밀번호가 들어있습니다.";

        final String ANSWER = "7799";

        Pass pass = Pass.FAIL;
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, CONTENT, ANSWER, pass)
                .hint(HINT)
                .chance(CHANCE)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(QUESTION, CONTENT, HINT, CHANCE, ANSWER, pass);
    }


    //  ㄱㄴㄷㅁ 퀴즈
    @Transactional
    public QuizResponseDto createQuizCa(Room room, String quizType){
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
        String e = "?";

        StringBuilder sb = new StringBuilder();
        sb.append(a).append(b).append(c).append(d).append(e);

        String question = sb.toString();


        String content = null;
        String hint = null;
        String chance = "낫 놓고...";

        questionList.set(0, "G");
        questionList.set(1, "C");
        questionList.set(2, "F");
        questionList.set(3, "E");

        StringBuilder sb2 = new StringBuilder();
        sb2.append(questionList.get(num1)).append(questionList.get(num2)).append(questionList.get(num3)).append(questionList.get(num4));

        String answer = sb2.toString();

        Pass pass = Pass.FAIL;
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }


    @Transactional
    public void endQuiz(Long roomId, String quizType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));
        Quiz quiz;

        Optional<Quiz> tempQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(tempQuiz.isPresent()) {
            quiz = tempQuiz.get();
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }
        quiz.endQuiz();
        quizRepository.save(quiz);
    }

    public void createEven(Random random, int MAX_COUNT, List<Integer> even, int[] count, List<String> arr) {
        int temp = random.nextInt(even.size());
//                슷자에 해당되는 카운트를 하나 올려준다.
        count[even.get(temp)]++;
//                문제를 만든다.
        arr.add(String.valueOf(even.get(temp)));
        if(count[even.get(temp)] == MAX_COUNT) {
            even.remove(temp);
        }
    }

    public void createOdd(Random random, int MAX_COUNT, List<Integer> odd, int[] count, List<String> arr) {
        int temp = random.nextInt(odd.size());
        count[odd.get(temp)]++;
        arr.add(String.valueOf(odd.get(temp)));
        if(count[odd.get(temp)] == MAX_COUNT) {
            odd.remove(temp);
        }

    }

}
