package com.sayed.rahman.springsecurityclient.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
public class PasswordResetToken {
    private static final Integer VERIFICATION_TIME = 10 ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String token;

    private LocalDateTime expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(
            name = "FK_USER_PASSRESET_TOKEN"
    ))
    private User user;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.expirationTime = calculateVarficationTime(VERIFICATION_TIME);
        this.user = user;
    }

    public PasswordResetToken(String token) {
        this.token = token;
    }


    private LocalDateTime calculateVarficationTime(Integer verificationTime) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plusMinutes(VERIFICATION_TIME);
        return localDateTime ;
    }

    public User getUser() {
        return this.user;
    }

    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
    }
}
