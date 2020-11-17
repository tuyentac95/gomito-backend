package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GLabel;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GLabelService {
    @Autowired
    GLabelRepository gLabelRepository;

    @Autowired
    GBoardRepository gBoardRepository;

    @Autowired
    AuthService authService;

    public GLabel save(GLabel gLabel){return gLabelRepository.save(gLabel);}

    public GLabel findById(Long labelId){
        return gLabelRepository.findById(labelId)
                .orElseThrow(() -> new SpringGomitoException("Không tìm thấy label: " + labelId));
    }

    public List<GLabel> findAllLabelByBoardId(Long id){
        GBoard gBoard = gBoardRepository.findById(id).orElse(null);
        return gLabelRepository.findAllByBoard(gBoard);
    }
}
