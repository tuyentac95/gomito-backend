package com.gomito.Gomitobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String boardName;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
//    private GUser user;

    @ManyToMany(mappedBy = "boards")
    @JsonIgnore
    private Set<GUser> users;

}
