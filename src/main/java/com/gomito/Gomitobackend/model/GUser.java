package com.gomito.Gomitobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class GUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    private String username;

    @Email
    @NotEmpty(message = "Email is required")
    private String email;

    @NotBlank
    private String password;

    private Instant created;

    private boolean enabled;

    private String avatarUrl;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_board",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "board_id", referencedColumnName = "boardId"))
    @JsonIgnore
    private List<GBoard> boards;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_card",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "card_id", referencedColumnName = "cardId"))
    private List<GCard> cards;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_notification",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
    inverseJoinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "notificationId"))
    @JsonIgnore
    private List<Notification> notifications;


}
