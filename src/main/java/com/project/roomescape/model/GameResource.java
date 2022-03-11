package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class GameResource {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String type;    // userP1, userP2, userP3, userP4, gameRunFile 일단 5개

    @Column(nullable = false)
    private String url;     // S3에 저장되있는 파일의 url주소

    public GameResource(String type, String url) {
        this.type = type;
        this.url = url;
    }


//    public GameResource(GameResourceRequestDto gameResourceRequestDto) {
//        this.type = gameResourceRequestDto.getType();
//        this.url = gameResourceRequestDto.getUrl();
//    }
}
