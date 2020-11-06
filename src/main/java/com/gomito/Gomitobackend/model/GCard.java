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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GList list;

    private Integer cardIndex;
}
