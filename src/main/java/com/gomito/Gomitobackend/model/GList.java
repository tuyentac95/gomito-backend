package com.gomito.Gomitobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JsonIgnore
    private GBoard board;

    private Integer listIndex;
}