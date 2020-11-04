package com.gomito.Gomitobackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GList {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;
    private String listName;

    @ManyToOne(fetch = FetchType.LAZY)
    private GBoard board;

}