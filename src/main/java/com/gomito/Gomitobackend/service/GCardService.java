package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.repository.GCardRepository;
import com.gomito.Gomitobackend.repository.GListRepository;
import com.gomito.Gomitobackend.repository.GUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCardService {

    @Autowired
    private GCardRepository gCardRepository;

    @Autowired
    private GListRepository gListRepository;

    @Autowired
    private GUserRepository gUserRepository;

    public List<GCard> findAllCardByListId(Long id) {
        GList list = gListRepository.findById(id)
                .orElseThrow(() -> new SpringGomitoException("Ko tim thay list voi id la " + id));
        return gCardRepository.findAllByList(list);
    }

    public GCard save(GCard gcard) {
        return gCardRepository.save(gcard);
    }

//    public Integer findMaxIndex(Long listId) {
//        GList list = gListRepository.findById(listId)
//                .orElseThrow(() -> new SpringGomitoException("Ko tim thay list voi id la " + listId));
//        GCard cardMaxIndex = gCardRepository.findFirstByListOrderByCardIndexDesc(list)
//                .orElse(null);
//        if(cardMaxIndex != null) {
//            return cardMaxIndex.getCardIndex();
//        }
//        return -1;
//    }

    public GCard findById(Long cardId){
        return gCardRepository.findById(cardId)
                .orElseThrow(null);
    }

    public List<GCard> findALlByListIdAndOrderByCardIndex(Long id){
        GList list = gListRepository.findById(id)
                .orElseThrow(()-> new SpringGomitoException("Ko tim thay list với id là: " + id));
        return gCardRepository.findAllByListOrderByCardIndex(list);
    }

    public Integer findMaxIndex(Long listId){
        GList gList = gListRepository.findById(listId)
                .orElseThrow(() -> new SpringGomitoException("Không tìm thấy list"));
        List<GCard> cards = gCardRepository.findAllByList(gList);
        if(cards.size() >0){
            Integer maxIndex = cards.get(0).getCardIndex();
            for (int i =0; i < cards.size();i++){
                if (maxIndex < cards.get(i).getCardIndex()){
                    maxIndex = cards.get(i).getCardIndex();
                }
            }
            return maxIndex;
        } else {
            return -1;
        }
    }


    public GCard findCardById(Long id) {
        return gCardRepository.findById(id)
                .orElse(null);
    }

    public List<GCard> searchByName(String name) {
        return gCardRepository.findCardByNamedParams(name);
    }

    public boolean addMember(GUser newMember, Long cardId) {
        GCard card = gCardRepository.findById(cardId).orElse(null);
        if (card != null) {
            if (checkMemberOfCard(newMember, card)) return false;
            System.out.println("check add member to card");
            List<GCard> cards = newMember.getCards();
            cards.add(card);
            newMember.setCards(cards);
            gUserRepository.save(newMember);
            return true;
        }
        return false;
    }

    private boolean checkMemberOfCard(GUser newMember, GCard card) {
        List<GUser> users = card.getUsers();
        for (GUser user : users) {
            if (user.getUserId().equals(newMember.getUserId())) return true;
        }
        return false;
    }
}
