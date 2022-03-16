package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Room extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roomId")
    @Id
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String createdUser;

    @OneToMany(mappedBy = "room")
    private List<User> userList = new ArrayList<>();

    @Column(nullable = false)
    private Long loadingCount;

    @Column
    private Long startAt;

    public Room(String teamName, String createdUser) {
        this.teamName = teamName;
        this.createdUser = createdUser;
        this.loadingCount = 0L;
        this.startAt = null;
    }

    public  String changeOwner() {

        this.createdUser = this.userList.get(0).getUserId();
        return this.createdUser;
    }


}
