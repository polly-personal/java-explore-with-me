package ru.practicum.entity.user;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "users")
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String email;

    private String name;
}
