package com.stripe.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "_customer_record")
@Builder
public class CustomerData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String customerId;
    String name;
    String email;
    String phone;
    Long created;
    @OneToOne(mappedBy = "customerData")
    SubscriptionData subscription;
}