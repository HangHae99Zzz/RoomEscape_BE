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
import java.util.*;
import java.util.stream.Stream;

import static com.project.roomescape.exception.ErrorCode.QUIZ_NOT_FOUND;
import static com.project.roomescape.exception.ErrorCode.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final RoomRepository roomRepository;
    private final QuizRepository quizRepository;
    private final ClueRepository clueRepository;

    public QuizResponseDto getQuiz(Long roomId, String quizType) {
        QuizResponseDto quizResponseDto = new QuizResponseDto();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));

        Optional<Quiz> savedQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(savedQuiz.isPresent()) {
            Quiz quiz = savedQuiz.get();
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
        int timeInt = random.nextInt(CLOCKTIME) + 1;
        // 97(a) ~ 108(l) 중 랜덤
        char timeChar = (char) (random.nextInt(CLOCKTIME) + 97);
        boolean randomBoolean = random.nextBoolean();

        // randomBoolean에 따라 direction 결정
        String direction = (randomBoolean) ? "앞으로" : "거꾸로";

        StringBuilder sb = new StringBuilder();
        sb.append("어제 ").append(timeInt).append("시에 잔거 같다. 시간이 ").append(direction)
                .append(" 돌고 있어. ").append(timeChar).append("라고 써있는 거 같은데, 지금 몇시지?");
        String content = sb.toString();

        String hint = null;
        String chance = "시계를 돌려볼까?";

        // direction이 '앞으로'면 a + b, '거꾸로'면 a - b
        int answerInt = (randomBoolean) ? timeInt + (timeChar - 96) : timeInt - (timeChar - 96);
        if (answerInt <= 0) answerInt += CLOCKTIME;
        if (answerInt > CLOCKTIME) answerInt -= CLOCKTIME;
        String answer = String.valueOf(answerInt);

        Pass pass = Pass.FAIL;

        // 퀴즈 저장.
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
        List<String> numbers = new ArrayList<>();

        List<Integer> even = new ArrayList<Integer>() {{
            add(0);
            add(2);
            add(4);
        }};
        List<Integer> odd = new ArrayList<Integer>() {{
            add(1);
            add(3);
            add(5);
        }};

        int[] count = {0, 0, 0, 0, 0, 0};

        //첫번째 시작이 짝수이냐 홀수이냐를 결정하기 위한 랜덤값.
        int randomInt = random.nextInt(2);

        // 첫번쨰 숫자가 짝수인 경우
        if(randomInt % 2 == 0) {
            for (int i = 0; i < HALF_SIZE; i++) {
                createEven(random, MAX_COUNT, even, count, numbers);
                createOdd(random, MAX_COUNT, odd, count, numbers);
            }
        } else {
            for (int i = 0; i < HALF_SIZE; i++) {
                createOdd(random, MAX_COUNT, odd, count, numbers);
                createEven(random, MAX_COUNT, even, count, numbers);
            }
        }

        // ?가 들어갈 위치 선정.
        int question1 = random.nextInt(TOTAL_SIZE);
        int question2;
        if(question1 % 2 == 0) {
            question2 = random.nextInt(HALF_SIZE) * 2 + 1;
        } else {
            question2 = random.nextInt(HALF_SIZE) * 2;
        }

        int temp;
        if(question1 > question2) {
            temp = question1;
            question1 = question2;
            question2 = temp;
        }
        sb.append(numbers.get(question1)).append(", ").append(numbers.get(question2));
        answer = sb.toString();

        // 정답이 들어가는 부분 숫자에서 ?로 바꾼다.
        numbers.set(question1, "?1");
        numbers.set(question2, "?2");

        content = numbers.toString();
        Pass pass = Pass.FAIL;

        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, content, answer, pass)
                .chance(CHANCE)
                .build();
        quizRepository.save(quiz);

        return new QuizResponseDto(QUESTION, content, HINT, CHANCE, answer, pass);
    }

    @Transactional
    public QuizResponseDto createQuizBa(Room room, String quizType) {
        List<Clue> clueList = clueRepository.findAllByRoomId(room.getId());

        Long clueBa1 = 0L;
        Long clueBa2 = 0L;
        String clueBa3 = "";

        for (Clue clue : clueList) {
            if (clue.getType().equals("Ba1")) clueBa1 = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba2")) clueBa2 = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba3")) clueBa3 = clue.getContent();
        }

        Long clue = (clueBa3.equals("+")) ? clueBa1 + clueBa2 : Math.abs(clueBa1 - clueBa2);
        String question = "비밀번호";
        String content = "HackerRoom";
        String hint = "포스터들을 눈여겨 보세요";
        String chance = "H = 0, r = 5";
        String answer = "";

        int[] clueArr = Stream.of(String.valueOf(clue).split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < clueArr.length; i++) {
            answer += content.charAt(clueArr[i]);
        }
        Pass pass = Pass.FAIL;

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

        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, CONTENT, ANSWER, pass)
                .hint(HINT)
                .chance(CHANCE)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(QUESTION, CONTENT, HINT, CHANCE, ANSWER, pass);
    }

    @Transactional
    public QuizResponseDto createQuizCa(Room room, String quizType){
        final int SIZE = 4;

        Random random = new Random();
        int[] numbers = new int[SIZE];
        for(int i = 0; i < SIZE; i++) {
            numbers[i] = random.nextInt(SIZE);
        }

        String[] questionList = new String[SIZE];
        String[] randomList = new String[]{"ㄱ", "ㄴ", "ㄷ", "ㅁ"};
        for(int i = 0; i < SIZE; i++) {
            questionList[i] = randomList[numbers[i]];
        }
        String question = Arrays.toString(questionList) + "?";

        String content = null;
        String hint = null;
        String chance = "낫 놓고...";

        String[] answerList = new String[]{"G", "C", "F", "E"};
        StringBuilder answer = new StringBuilder();
        for(int i = 0; i < SIZE; i++) {
            answer.append(answerList[numbers[i]]);
        }

        Pass pass = Pass.FAIL;

        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer.toString(), pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer.toString(), pass);
    }

    @Transactional
    public void endQuiz(Long roomId, String quizType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));
        Quiz quiz;

        Optional<Quiz> savedQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(savedQuiz.isPresent()) {
            quiz = savedQuiz.get();
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }

        //찾아온 퀴즈에서 pass값을 SUCCESS로 바꾸고 다시 저장.
        quiz.endQuiz();
        quizRepository.save(quiz);
    }

    // createQuizAb에서 사용
    private void createEven(Random random, int MAX_COUNT, List<Integer> even, int[] count, List<String> numbers) {
        int randomNum = random.nextInt(even.size());
        // randomNum에 해당되는 카운트를 하나 올려준다.
        count[even.get(randomNum)]++;
        // 문제를 만든다.
        numbers.add(String.valueOf(even.get(randomNum)));
        if(count[even.get(randomNum)] == MAX_COUNT) {
            even.remove(randomNum);
        }
    }

    // createQuizAb에서 사용
    private void createOdd(Random random, int MAX_COUNT, List<Integer> odd, int[] count, List<String> numbers) {
        int randomNum = random.nextInt(odd.size());
        count[odd.get(randomNum)]++;
        numbers.add(String.valueOf(odd.get(randomNum)));
        if(count[odd.get(randomNum)] == MAX_COUNT) {
            odd.remove(randomNum);
        }
    }
}
