package com.stripe.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "_subscription_data")
@Builder
public class SubscriptionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long subscriptionId;
    String productId;
    @OneToOne
    @JoinColumn(name = "customerId")
    CustomerData customerData;
}
