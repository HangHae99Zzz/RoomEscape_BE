package com.project.roomescape.service;

import com.project.roomescape.model.Room;
import com.project.roomescape.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> findRoomByStringId(final String sid) {
        return roomRepository.findByTeamName(sid);
    }


    public Map<String, WebSocketSession> getClients(final Room room) {
        return Optional.ofNullable(room)
                .map(r -> Collections.unmodifiableMap(r.getClients()))
                .orElse(Collections.emptyMap());
    }

    public WebSocketSession addClient(final Room room, final String name, final WebSocketSession session) {
        return room.getClients().put(name, session);
    }

    public WebSocketSession removeClientByName(final Room room, final String name) {
        return room.getClients().remove(name);
    }
}
