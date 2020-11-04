package com.gomito.Gomitobackend.service;

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

    public List<GBoard> findAllBoardByUserId(Long id){
        GUser user = gUserRepository.findById(id).orElse(null);
        return gBoardRepository.findAllByUser(user);
    }

}
