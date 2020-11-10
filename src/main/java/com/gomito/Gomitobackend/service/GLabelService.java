package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GLabel;
import com.gomito.Gomitobackend.repository.GLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GLabelService {
    @Autowired
    GLabelRepository gLabelRepository;

    @Autowired
    AuthService authService;

    public GLabel save(GLabel gLabel){return gLabelRepository.save(gLabel);}

    public GLabel findById(Long labelId){
        return gLabelRepository.findById(labelId)
                .orElseThrow(() -> new SpringGomitoException("Không tìm thấy label: " + labelId));
    }
}
