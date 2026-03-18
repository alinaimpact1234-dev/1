package com.impact.lessons.entity;

import com.impact.lessons.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_status")
@Getter
@Setter
public class UserStatus {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

}
