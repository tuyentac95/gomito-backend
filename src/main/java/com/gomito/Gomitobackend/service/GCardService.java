package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.repository.GCardRepository;
import com.gomito.Gomitobackend.repository.GListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCardService {

    @Autowired
    private GCardRepository gCardRepository;

    @Autowired
    private GListRepository gListRepository;

    public List<GCard> findAllCardByListId(Long id) {
        GList list = gListRepository.findById(id).orElseThrow(() -> new SpringGomitoException("Ko tim thay list voi id la " + id));
        return gCardRepository.findAllByList(list);
    }
}
