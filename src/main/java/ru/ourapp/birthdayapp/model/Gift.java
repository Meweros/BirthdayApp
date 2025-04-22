package ru.ourapp.birthdayapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gifts")
@Data
@NoArgsConstructor
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String link;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] screenshot;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Boolean isGifted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
} 