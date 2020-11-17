package com.gomito.Gomitobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long attachmentId;

    private String attachmentName;

    @Column(columnDefinition = "longtext")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GCard card;
}
