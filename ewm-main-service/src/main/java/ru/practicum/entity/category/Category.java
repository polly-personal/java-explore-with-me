package ru.practicum.entity.category;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "categories")
@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
}
