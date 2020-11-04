package com.gomito.Gomitobackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private GUser user;

    @OneToMany(fetch=FetchType.LAZY)
    private List<GList> lists;
}
