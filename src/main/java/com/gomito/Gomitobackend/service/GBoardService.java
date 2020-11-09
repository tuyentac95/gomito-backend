package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GBoardService {

    @Autowired
    private GBoardRepository gBoardRepository;

    @Autowired
    private GUserRepository gUserRepository;

    @Autowired
    private AuthService authService;

    public List<GBoard> findAllBoardByUserId(Long id){
        GUser user = gUserRepository.findById(id).orElse(null);
        Collection<GUser> users = new HashSet<>();
        users.add(user);
        return gBoardRepository.findAllByUsersIn(users);
    }

    public GBoard save(GBoard gBoard){
        GUser currentUser = authService.getCurrentUser();
        GBoard board = gBoardRepository.save(gBoard);
        Set<GBoard> boards = currentUser.getBoards();
        boards.add(board);
        currentUser.setBoards(boards);
        gUserRepository.save(currentUser);
        return board;
    }

    public GBoard findById(Long boardId) {
        return gBoardRepository.findById(boardId)
                .orElseThrow(() -> new SpringGomitoException("KO tim thay Board: " + boardId));
    }
}
