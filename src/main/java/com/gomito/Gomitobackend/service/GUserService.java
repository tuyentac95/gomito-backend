package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GUserService {
    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private GUserRepository gUserRepository;

    public boolean checkMemberOfBoard(GUser currentUser, Long id) {
        GBoard board = gBoardService.findById(id);
        Set<GUser> users = board.getUsers();
        for (GUser user: users) {
            if (user.getUserId().equals(currentUser.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public GUser findUserByName(String username) {
        return gUserRepository.findByUsername(username).orElse(null);
    }

    public GUser findUserByEmail(String email) {
        return gUserRepository.findByEmail(email).orElse(null);
    }
}
