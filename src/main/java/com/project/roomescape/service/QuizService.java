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

        //해당 room와 quiztype에 맞는 quiz를 찾아옵니다.
        Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
        //이미 생성된 퀴즈가 존재한다면 그 퀴즈를 ResponseDto에 담아줍니다.
        if(temporary.isPresent()) {
            Quiz quiz = temporary.get();
            quizResponseDto = new QuizResponseDto(
                    quiz.getQuestion(), quiz.getContent(), quiz.getHint(),
                    quiz.getChance(), quiz.getAnswer(), quiz.getPass());
        }

        //생성된 퀴즈가 없다면 각각 퀴즈타입에 맞는 생성 메서드를 수행합니다.
        else {
            if (quizType.equals("Aa")) quizResponseDto = createQuizAa(room, quizType);
            if (quizType.equals("Ab")) quizResponseDto = createQuizAb(room, quizType);
            if (quizType.equals("Ba")) quizResponseDto = createQuizBa(room, quizType);
            if (quizType.equals("Bb")) quizResponseDto = createQuizBb(room, quizType);
            if (quizType.equals("Ca")) quizResponseDto = createQuizCa(room, quizType);
        }
        return quizResponseDto;
    }

    // quizType Aa 생성하기
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

        // 랜덤으로 나온 q에 따라 direction 결정
        String direction = (q) ? "앞으로" : "거꾸로";

        // content
        StringBuilder sb = new StringBuilder();
        sb.append("어제 ").append(a).append("시에 잔거 같다. 시간이 ").append(direction)
                .append(" 돌고 있어. ").append(b).append("라고 써있는 거 같은데, 지금 몇시지?");
        String content = sb.toString();

        String hint = null;
        String chance = "시계를 돌려볼까?";

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        if (ans <= 0) ans += CLOCKTIME;
        if (ans > CLOCKTIME) ans -= CLOCKTIME;
        String answer = String.valueOf(ans);

        // 문제 생성했을 때는 FAIL을 기본값으로 지정
        Pass pass = Pass.FAIL;

        // 퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }

    // quizType Ab 생성하기
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
        // 짝수들
        List<Integer> even = new ArrayList<Integer>() {{
            add(0);
            add(2);
            add(4);
        }};
        // 홀수들
        List<Integer> odd = new ArrayList<Integer>() {{
            add(1);
            add(3);
            add(5);
        }};

        int[] count = {0, 0, 0, 0, 0, 0};

        //첫번째 시작이 짝수이냐 홀수이냐를 결정하기 위한 랜덤값.
        int standard1 = random.nextInt(2);
        int standard2;

        // 첫번쨰 숫자가 짝수인 경우
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


        // ?가 들어갈 위치 선정.
        standard1 = random.nextInt(TOTAL_SIZE);
        if(standard1 % 2 == 0) {
            standard2 = random.nextInt(HALF_SIZE) * 2 + 1;
        } else {
            standard2 = random.nextInt(HALF_SIZE) * 2;
        }

        // standard1이 항상 standard2보다 작게끔 만든다.
        if(standard1 > standard2) {
            temp = standard1;
            standard1 = standard2;
            standard2 = temp;
        }
        sb.append(arr.get(standard1)).append(", ").append(arr.get(standard2));


        // 정답.
        answer = sb.toString();

        // 정답이 들어가는 부분 숫자에서 ?로 바꾼다.
        arr.set(standard1, "?1");
        arr.set(standard2, "?2");
        // list형식을 하나의 String으로 바꿔준다.
        content = arr.toString();

        Pass pass = Pass.FAIL;


        // 퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, content, answer, pass)
                .chance(CHANCE)
                .build();
        quizRepository.save(quiz);

        return new QuizResponseDto(QUESTION, content, HINT, CHANCE, answer, pass);

    }

    // quizType Ba 생성하기
    @Transactional
    public QuizResponseDto createQuizBa(Room room, String quizType) {
        List<Clue> clueList = clueRepository.findAllByRoomId(room.getId());

        Long clueA = 0L;
        Long clueB = 0L;
        String clueC = "";

        // 해당 방의 clue를 찾아와서 변수로 지정
        for (Clue clue : clueList) {
            if (clue.getType().equals("Ba1")) clueA = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba2")) clueB = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba3")) clueC = clue.getContent();
        }

        // 정답을 계산하기 전에 우선 Clue들을 계산
        Long clueABC = (clueC.equals("+")) ? clueA + clueB : Math.abs(clueA - clueB);

        String question = "비밀번호";
        String content = "HackerRoom";

        String hint = "포스터들을 눈여겨 보세요";
        String chance = "H = 0, r = 5";

        String answer = "";

        // clueABC를 자리별로 끊어서 int[]로 만듦
        int[] arr = Stream.of(String.valueOf(clueABC).split("")).mapToInt(Integer::parseInt).toArray();

        // content의 해당 자리수를 찾아서 answer로 저장
        for (int i = 0; i < arr.length; i++) {
            answer += content.charAt(arr[i]);
        }

        // Quiz 생성시 pass는 FAIL을 기본값으로 지정
        Pass pass = Pass.FAIL;

        // 퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .hint(hint)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }

    // quizType Bb 생성하기
    @Transactional
    public QuizResponseDto createQuizBb(Room room, String quizType){
        // 마지막 최종 문제로서 5문제중 4문제를 맞춰야 이 문제를 풀 수 있다.
        final String QUESTION = "이제 꿈에서 깨어날 시간입니다.";
        // 비밀번호가 있는 장소가 글씨를 섞어놓은 그림으로 주어지기 때문에 content는 따로 없다.
        final String CONTENT = "";
        // 비밀번호가 있는 장소가 적힌 글씨는 총 3글자이다.
        final String CHANCE = "3글자";
        final String HINT = "그림에 적혀있는 물건 속에 비밀번호가 들어있습니다.";
        // 최종 그림에 적힌 장소에 가면 7799라는 비밀번호를 알 수 있다.
        final String ANSWER = "7799";
        // 게임종료해야 Pass.SUCCESS가 된다.
        Pass pass = Pass.FAIL;
        // 퀴즈 인스턴스에 다 담아서
        Quiz quiz = new Quiz.Builder(room, quizType, QUESTION, CONTENT, ANSWER, pass)
                .hint(HINT)
                .chance(CHANCE)
                .build();
        // Repository에 저장해준다
        quizRepository.save(quiz);
        // 반환할 ResponseDto에 해당하는 것들을 담아 반환해준다
        return new QuizResponseDto(QUESTION, CONTENT, HINT, CHANCE, ANSWER, pass);
    }


    // quizType Ca 생성하기
    @Transactional
    public QuizResponseDto createQuizCa(Room room, String quizType){
        // 랜덤으로 문제와 답을 생성할거라 Random 함수를 선언
        Random random = new Random();
        // 문제를 담을 arrayList를 선언
        ArrayList<String> questionList = new ArrayList<String>();
        // ㄱ,ㄴ,ㄷ,ㅁ를 순서대로 담아준다
        questionList.add("ㄱ");
        questionList.add("ㄴ");
        questionList.add("ㄷ");
        questionList.add("ㅁ");
        // questionList에서 랜덤으로 4문제 가져오기
        int num1 = random.nextInt(questionList.size());
        int num2 = random.nextInt(questionList.size());
        int num3 = random.nextInt(questionList.size());
        int num4 = random.nextInt(questionList.size());
        // 랜덤으로 생성된 번호에 해당하는 문제를 a,b,c,d에 차례로 넣어준다
        String a = questionList.get(num1);
        String b = questionList.get(num2);
        String c = questionList.get(num3);
        String d = questionList.get(num4);
        String e = "?";
        // +연산자 대신하여 StringBuilder 함수를 사용하여 문제를 구성해준다
        StringBuilder sb = new StringBuilder();
        sb.append(a).append(b).append(c).append(d).append(e);
        String question = sb.toString();

        // img 파일로 content를 대신한다
        String content = null;
        // 너무 어렵지 않은 문제라 따로 hint는 없다
        String hint = null;
        // 문제를 푸는 방향을 알려준다. 낫 놓고 기역자도 모른다는 속담을 가져옴
        String chance = "낫 놓고...";
        // arrayList가 순서대로 담긴다는 특성을 살려서 문제에 해당하는 정답으로 순서에 맞게 변경해준다("ㄱ"->"G", "ㄴ"->"C", ...)
        questionList.set(0, "G");
        questionList.set(1, "C");
        questionList.set(2, "F");
        questionList.set(3, "E");
        // +연산자를 대신하여 StringBuilder 함수를 사용하여 정답을 구성해준다
        StringBuilder sb2 = new StringBuilder();
        sb2.append(questionList.get(num1)).append(questionList.get(num2)).append(questionList.get(num3)).append(questionList.get(num4));
        String answer = sb2.toString();
        // 게임종료를 해야 pass가 SUCCESS로 바뀌기 때문에 FAIL의 값을 준다
        Pass pass = Pass.FAIL;
        // 퀴즈 인스턴스에 다 담아서
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer, pass)
                .chance(chance)
                .build();
        // Repository에 저장해준다
        quizRepository.save(quiz);
        // 반환할 ResponseDto에 해당하는 것들을 담아 반환해준다
        return new QuizResponseDto(question, content, hint, chance, answer, pass);
    }

    // Quiz 완료하기
    @Transactional
    public void endQuiz(Long roomId, String quizType) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ROOM_NOT_FOUND));
        Quiz quiz;


        //room과 quiztype에 해당되는 퀴즈를 찾아옵니다.
        Optional<Quiz> tempQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(tempQuiz.isPresent()) {
            quiz = tempQuiz.get();
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }

        //찾아온 퀴즈에서 pass값을 SUCCESS로 바꾸고 다시 저장합니다.
        quiz.endQuiz();
        quizRepository.save(quiz);
    }

    public void createEven(Random random, int MAX_COUNT, List<Integer> even, int[] count, List<String> arr) {
        int temp = random.nextInt(even.size());
        // 슷자에 해당되는 카운트를 하나 올려준다.
        count[even.get(temp)]++;
        // 문제를 만든다.
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
