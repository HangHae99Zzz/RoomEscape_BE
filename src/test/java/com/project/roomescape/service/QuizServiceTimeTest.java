package com.project.roomescape.service;

import com.project.roomescape.mockobject.MockClueRepository;
import com.project.roomescape.model.Clue;
import com.project.roomescape.model.Pass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTimeTest {

    @Mock
    MockClueRepository mockClueRepository;

    @Test
    @DisplayName("퀴즈 Aa생성 시간 테스트")
    void getQuizAa() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

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
        String imgUrl = null;

        // 시침이 앞으로 돌면 a + b, 뒤로 돌면 a - b
        int ans = (q) ? a + (b - 96) : a - (b - 96);
        if (ans < 0) ans += 12;
        if (ans > 12) ans -= 12;
        String answer = String.valueOf(ans);
        Pass pass = Pass.FAIL;

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);

        //퀴즈 생성은 시간만 측정하는 것으로 생각하자.
        assertNotNull(answer);
    }

    @Test
    @DisplayName("퀴즈 Ab생성 시간 테스트")
    void getQuizAb() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

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
        String imgUrl = null;
        Pass pass = Pass.FAIL;

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);

        //퀴즈 생성은 시간만 측정하는 것으로 생각하자.
        assertNotNull(answer);
    }

    @Test
    @DisplayName("퀴즈 Ba생성 시간 테스트 & 정답확인")
    void getQuizBa() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        // mock 객체 stubbing을 통해 진짜 메서드 불러오기.
        when(mockClueRepository.findAllByRoomId()).thenCallRealMethod();




        List<Clue> clueList = mockClueRepository.findAllByRoomId();

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
        String imgUrl = null;

        String answer = "";
        // [5, 0, 1, 6]
        int[] arr = Stream.of(String.valueOf(clueABC).split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < arr.length; i++) {
            answer += content.charAt(arr[i]);
        }
        Pass pass = Pass.FAIL;

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);

        assertEquals(answer, "acke");
    }

    @Test
    @DisplayName("퀴즈 Bb생성 시간 테스트")
    void getQuizBb() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();

        String question = "비밀번호가 숨겨진 장소는 어디일까?";

        String content = "";

        String chance = "배고프다...";

        String hint = "벽을 보세요";

        String answer = "7799";
        Pass pass = Pass.FAIL;

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);
    }

    @Test
    @DisplayName("퀴즈 Ca생성 시간 테스트")
    void getQuizCa() {
        //코드 실행 전에 시간 받아오기
        long beforeTime = System.currentTimeMillis();
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
        String question = a+b+c+d+"?";
        // content
        String content = null;
        // clue
        String hint = null;
        // hint
        String chance = "낫 놓고...";

        questionList.set(0, "G");
        questionList.set(1, "C");
        questionList.set(2, "F");
        questionList.set(3, "E");
        // answer
        String answer = questionList.get(num1)+questionList.get(num2)+questionList.get(num3)+questionList.get(num4);
        Pass pass = Pass.FAIL;

        // 코드 실행 후에 시간 받아오기
        long afterTime = System.currentTimeMillis();
        //두 시간에 차 계산
        double secDiffTime = (double)(afterTime - beforeTime)/ 1000;
        System.out.println("실행시간(m) : " + secDiffTime);

        assertNotNull(answer);
    }
}
