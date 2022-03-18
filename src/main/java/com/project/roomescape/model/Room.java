package com.project.roomescape.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Room extends Timestamped implements Serializable {

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

    @Column
    private Long startAt;

    public Room(String teamName, String createdUser) {
        this.teamName = teamName;
        this.createdUser = createdUser;
        this.startAt = null;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

}
