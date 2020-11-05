package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return gBoardRepository.findAllByUser(user);
    }

    public void save(GBoard gBoard){
        GUser currentUser = authService.getCurrentUser();
        gBoard.setUser(currentUser);
        gBoardRepository.save(gBoard);
    }

    public GBoard findById(Long boardId) {
        return gBoardRepository.findById(boardId)
                .orElseThrow(() -> new SpringGomitoException("KO tim thay Board: " + boardId));
    }
}
