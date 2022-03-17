package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class GameResource {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String type;

    // S3에 저장되있는 파일의 url주소
    @Column(nullable = false)
    private String url;

    public GameResource(String type, String url) {
        this.type = type;
        this.url = url;
    }

}
