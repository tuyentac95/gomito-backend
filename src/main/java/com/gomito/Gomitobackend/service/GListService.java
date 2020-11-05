package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GListService {

    @Autowired
    private GListRepository gListRepository;

    @Autowired
    private GBoardRepository gBoardRepository;

    public List<GList> findAllListByBoardId(Long id) {
        GBoard board = gBoardRepository.findById(id)
                .orElseThrow(() -> new SpringGomitoException("Ko tim thay board voi id la " + id));
        return gListRepository.findAllByBoard(board);
    }
}
