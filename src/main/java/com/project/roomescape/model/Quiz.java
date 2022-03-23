package com.project.roomescape.model;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Quiz extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(nullable = false)
    private String type;

    @Column(nullable = true)
    private String question;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private String hint;

    @Column(nullable = true)
    private String chance;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private Pass pass;

    Quiz(Builder builder) {
        this.room = builder.room;
        this.type = builder.type;
        this.question = builder.question;
        this.content = builder.content;
        this.hint = builder.hint;
        this.chance = builder.chance;
        this.answer = builder.answer;
        this.pass = builder.pass;
    }

//    static을 통해 상위 클래스를 생성하지 않고도 외부에서 바로 Quiz.Builder로 접근이 가능하다.
    public static class Builder {
        private Room room;
        private String type;
        private String question;
        private String content;
        private String hint;
        private String chance;
        private String answer;
        private Pass pass;

        // 필수적인 필드 : room, type, question, content, answer
        public Builder(Room room, String type, String question, String content, String answer,
                       Pass pass) {
            this.room = room;
            this.type = type;
            this.question = question;
            this.content = content;
            this.answer = answer;
            this.pass = pass;
        }
        public Builder hint(String clue) {
            this.hint = clue;
            return this;
        }
        public Builder chance(String hint) {
            this.chance = hint;
            return this;
        }

        public Quiz build() {
            return new Quiz(this);
        }
    }

    public void finishedQuiz() {
        this.pass = Pass.SUCCESS;
    }





}
