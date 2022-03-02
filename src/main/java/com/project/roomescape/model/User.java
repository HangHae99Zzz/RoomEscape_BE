package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    // 연관관계 편의 메소드
    public void setRoom(Room room) {
        this.room = room;
        room.getUserList().add(this);
    }

    public void setUser(String nickName, String img) {
        this.nickName = nickName;
        this.img = img;
    }

    // 생성 메소드
    public static User addUser(Room room, String nickName, String img) {
        User user = new User();
        user.setRoom(room);
        user.setUser(nickName, img);
        return user;
    }


    // Table과 매핑하지 않는다. 임시 저장 값
//    @Transient
//    List<String> nickNameList;
//    public static void registerNickNameList(List<String> nickNameList) {
//        User user = new User();
//        user.setNickNameList(nickNameList);
//    }
}
