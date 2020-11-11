package com.gomito.Gomitobackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String cardName;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GList list;
    private Integer cardIndex;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "card_label")
    @JsonIgnore
    private List<GLabel> labels;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cards")
    @JsonIgnore
    private List<GUser> users;

}
