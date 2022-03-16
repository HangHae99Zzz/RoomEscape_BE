package com.project.roomescape.service;

import com.project.roomescape.exception.CustomException;
import com.project.roomescape.model.Clue;
import com.project.roomescape.model.Quiz;
import com.project.roomescape.model.Room;
import com.project.roomescape.repository.ClueRepository;
import com.project.roomescape.repository.QuizRepository;
import com.project.roomescape.repository.RoomRepository;
import com.project.roomescape.responseDto.QuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Room room;

        Optional<Room> temp = roomRepository.findById(roomId);
        if(temp.isPresent()) {
            room = temp.get();
        } else {
            throw new CustomException(ROOM_NOT_FOUND);
        }

        if (quizType.equals("Aa")) {
            Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
            if(temporary.isPresent()) {
                Quiz quiz = temporary.get();
                quizResponseDto.setQuestion(quiz.getQuestion());
                quizResponseDto.setContent(quiz.getContent());
                quizResponseDto.setHint(quiz.getHint());
                quizResponseDto.setChance(quiz.getChance());
                quizResponseDto.setIngUrl(quiz.getImgUrl());
                quizResponseDto.setAnswer(quiz.getAnswer());
            }
            else{
                quizResponseDto = getQuizAa(room, quizType);
            }

        } else if (quizType.equals("Ab")) {
            Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
            if(temporary.isPresent()) {
                Quiz quiz = temporary.get();
                quizResponseDto.setQuestion(quiz.getQuestion());
                quizResponseDto.setContent(quiz.getContent());
                quizResponseDto.setHint(quiz.getHint());
                quizResponseDto.setChance(quiz.getChance());
                quizResponseDto.setIngUrl(quiz.getImgUrl());
                quizResponseDto.setAnswer(quiz.getAnswer());
            }
            else{
                quizResponseDto = getQuizAb(room, quizType);
            }

        } else if (quizType.equals("Ba")) {
            Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
            if (temporary.isPresent()) {
                Quiz quiz = temporary.get();
                quizResponseDto.setQuestion(quiz.getQuestion());
                quizResponseDto.setContent(quiz.getContent());
                quizResponseDto.setHint(quiz.getHint());
                quizResponseDto.setChance(quiz.getChance());
                quizResponseDto.setIngUrl(quiz.getImgUrl());
                quizResponseDto.setAnswer(quiz.getAnswer());
            }
            else {
                quizResponseDto = getQuizBa(room, quizType);
            }
        } else if (quizType.equals("Bb")) {
            Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
            if (temporary.isPresent()) {
                Quiz quiz = temporary.get();
                quizResponseDto.setQuestion(quiz.getQuestion());
                quizResponseDto.setContent(quiz.getContent());
                quizResponseDto.setHint(quiz.getHint());
                quizResponseDto.setChance(quiz.getChance());
                quizResponseDto.setIngUrl(quiz.getImgUrl());
                quizResponseDto.setAnswer(quiz.getAnswer());
            }
            else {
                quizResponseDto = getQuizBb(room, quizType);
            }
        } else if (quizType.equals("Ca")) {
            Optional<Quiz> temporary = quizRepository.findByRoomAndType(room, quizType);
            if(temporary.isPresent()) {
                Quiz quiz = temporary.get();
                quizResponseDto.setQuestion(quiz.getQuestion());
                quizResponseDto.setContent(quiz.getContent());
                quizResponseDto.setHint(quiz.getHint());
                quizResponseDto.setChance(quiz.getChance());
                quizResponseDto.setIngUrl(quiz.getImgUrl());
                quizResponseDto.setAnswer(quiz.getAnswer());
            }
            else{
                quizResponseDto = getQuizCa(room, quizType);
            }
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }
        return quizResponseDto;
    }


    private QuizResponseDto getQuizAa(Room room, String quizType) {
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

        String hint = null;
        String chance = "시계를 돌려볼까?";
        String imgUrl = null;

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        String answer = ans > 12 ? String.valueOf(ans - 12) : String.valueOf(Math.abs(ans));
//        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, imgUrl, answer);
    }



    private QuizResponseDto getQuizAb(Room room, String quizType) {
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

        if(standard1 % 2 == 0) {
            for (int i = 0; i < 15; i++) {
                temp = random.nextInt(even.size());
                count[even.get(temp)]++;
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

        standard1 = random.nextInt(30);
        if(standard1 % 2 == 0) {
            standard2 = random.nextInt(15) * 2 + 1;
        } else {
            standard2 = random.nextInt(15) * 2;
        }

        if(standard1 > standard2) {
            temp = standard1;
            standard1 = standard2;
            standard2 = temp;
        }

        answer = arr.get(standard1) + ", " + arr.get(standard2);

        arr.set(standard1, "?");
        arr.set(standard2, "?");

        content = arr.toString();

        String hint = null;
        String chance = "개수";
        String imgUrl = null;

        //        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer)
                .chance(chance)
                .build();
        quizRepository.save(quiz);

        return new QuizResponseDto(question, content, hint, chance, imgUrl, answer);

    }




    private QuizResponseDto getQuizBa(Room room, String quizType) {
        List<Clue> clueList = clueRepository.findAllByRoomId(room.getId());

        Long clueA = 0L;
        Long clueB = 0L;
        String clueC = "";

        for (Clue clue : clueList) {
            if (clue.getType().equals("Ba1")) clueA = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba2")) clueB = Long.valueOf(clue.getContent());
            if (clue.getType().equals("Ba3")) clueC = clue.getContent();
        }

        Long clueABC = (clueC.equals("+")) ? clueA + clueB : Math.abs(clueA - clueB);

        String question = "비밀번호";
        String content = "HackerRoom";

        String hint = "포스터들을 눈여겨 보세요";
        String chance = "r = 5";
        String imgUrl = null;

        String answer = "";
        int[] arr = Stream.of(String.valueOf(clueABC).split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < arr.length; i++) {
            answer += content.charAt(arr[i]);
        }
        //        퀴즈 저장.
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer)
                .chance(chance)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, imgUrl, answer);
    }




    private QuizResponseDto getQuizBb(Room room, String quizType){



        String question = "비밀번호가 숨겨진 장소는 어디일까?";

        String content = null;

        String chance = "배고프다...";

        String hint = "벽을 보세요";

        String imgUrl = null;


        String answer = "7799";
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer)
                .hint(hint)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, imgUrl, answer);
    }





    //  ㄱㄴㄷㅁ 퀴즈
    private QuizResponseDto getQuizCa(Room room, String quizType){
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
        //question
        String question = "한글의 위대함이 느껴진다.";
        // content
        String content = a+b+c+d+"?";
        // clue
        String hint = null;
        // hint
        String chance = "낫 놓고...";
        String imgUrl = null;

        questionList.set(0, "G");
        questionList.set(1, "C");
        questionList.set(2, "F");
        questionList.set(3, "E");
        // answer
        String answer = questionList.get(num1)+questionList.get(num2)+questionList.get(num3)+questionList.get(num4);
        // 퀴즈 저장
        Quiz quiz = new Quiz.Builder(room, quizType, question, content, answer)
                .hint(hint)
                .build();
        quizRepository.save(quiz);
        return new QuizResponseDto(question, content, hint, chance, imgUrl, answer);
    }






    public void finishedQuiz(Long roomId, String quizType) {
        Optional<Room> temp = roomRepository.findById(roomId);
        Room room;
        Quiz quiz;
        if(temp.isPresent()) {
            room = temp.get();
        } else {
            throw new CustomException(ROOM_NOT_FOUND);
        }
        Optional<Quiz> tempQuiz = quizRepository.findByRoomAndType(room, quizType);
        if(tempQuiz.isPresent()) {
            quiz = tempQuiz.get();
        } else {
            throw new CustomException(QUIZ_NOT_FOUND);
        }
        quiz.finishedQuiz();
        quizRepository.save(quiz);
    }

    // count +1
//    @Transactional
//    public void getCount(Long roomId) {
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(()-> new CustomException(ROOM_NOT_FOUND));
//        Long count = room.getCount();
//        room.setCount(count + 1);
//        roomRepository.save(room);
//    }

}
