package com.gomito.Gomitobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
//@RequiredArgsConstructor
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "boards")
    @JsonIgnore
    private Set<GUser> users;

}
