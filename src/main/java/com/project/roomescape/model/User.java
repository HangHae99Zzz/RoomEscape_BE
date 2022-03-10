package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private boolean loading;

    // 연관관계 편의 메소드
    public void setRoom(Room room) {
        this.room = room;
        room.getUserList().add(this);
    }

    public void setUser(String nickName, String img, String userId) {
        this.nickName = nickName;
        this.img = img;
        this.userId = userId;
        this.loading = false;
    }

    // 생성 메소드
    public static User addUser(Room room, String nickName, String img, String userId) {
        User user = new User();
        user.setRoom(room);
        user.setUser(nickName, img, userId);
        return user;
    }



}
