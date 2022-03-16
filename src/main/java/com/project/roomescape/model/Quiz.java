package com.project.roomescape.model;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Quiz extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String hint;

    @Column(nullable = true)
    private String chance;

    @Column(nullable = true)
    private String imgUrl;

    @Column(nullable = false)
    private String answer;

    Quiz(Builder builder) {
        this.room = builder.room;
        this.type = builder.type;
        this.question = builder.question;
        this.content = builder.content;
        this.hint = builder.hint;
        this.chance = builder.chance;
        this.answer = builder.answer;
    }

//    static을 통해 상위 클래스를 생성하지 않고도 외부에서 바로 Quiz.Builder로 접근이 가능하다.
    public static class Builder {
        private Room room;
        private String type;
        private String question;
        private String content;
        private String hint;
        private String chance;
        private String imgUrl;
        private String answer;

        // 필수적인 필드 : room, type, question, content, answer
        public Builder(Room room, String type, String question, String content, String answer) {
            this.room = room;
            this.type = type;
            this.question = question;
            this.content = content;
            this.answer = answer;
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





}
