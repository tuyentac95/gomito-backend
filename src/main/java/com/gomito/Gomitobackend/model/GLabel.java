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
public class GLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;
    private String labelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GBoard board;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "labels")
    @JsonIgnore
    private List<GCard> cards;
    private String color;
}
