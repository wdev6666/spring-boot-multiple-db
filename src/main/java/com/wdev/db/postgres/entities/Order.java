package com.wdev.db.postgres.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="orders")
@Setter
@Getter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String item;
    private long orderTotal;
}
