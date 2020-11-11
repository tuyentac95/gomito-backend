package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.model.JoinGroupToken;
import com.gomito.Gomitobackend.model.NotificationEmail;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.JoinGroupTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GBoardService {

    @Autowired
    private GBoardRepository gBoardRepository;

    @Autowired
    private GUserRepository gUserRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private MailService mailService;

    @Autowired
    private JoinGroupTokenRepository joinGroupTokenRepository;

    public List<GBoard> findAllBoardByUserId(Long id){
        GUser user = gUserRepository.findById(id).orElse(null);
        return user != null ? user.getBoards() : null;
    }

    @Transactional
    public GBoard save(GBoard gBoard){
        GUser currentUser = authService.getCurrentUser();
        GBoard board = gBoardRepository.save(gBoard);
        List<GBoard> boards = currentUser.getBoards();
        boards.add(board);
        currentUser.setBoards(boards);
        gUserRepository.save(currentUser);
        return board;
    }

    public GBoard findById(Long boardId) {
        return gBoardRepository.findById(boardId).orElse(null);
    }

    @Transactional
    public boolean addMember(GUser member, Long boardId) {
        GBoard board = gBoardRepository.findById(boardId).orElse(null);
        if (board != null) {
            String token = UUID.randomUUID().toString();
            JoinGroupToken joinGroupToken = new JoinGroupToken();
            joinGroupToken.setToken(token);
            joinGroupToken.setMember(member);
            joinGroupToken.setBoard(board);
            joinGroupTokenRepository.save(joinGroupToken);

            GUser currentUser = authService.getCurrentUser();
            mailService.setMail(new NotificationEmail(
                    currentUser.getUsername() + " added you to the board " + board.getBoardName(),
                    member.getEmail(),
                    "Please click on the below url to join the board:\n"
                            + "http://localhost:8080/api/users/join/" + token
            ));

            return true;
        } else return false;
    }
}
