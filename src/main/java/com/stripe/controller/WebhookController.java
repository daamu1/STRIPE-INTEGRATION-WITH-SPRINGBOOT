package com.stripe.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class WebhookController {

    @Value("${stripe.webhook-secret}")
     String webhookSecret;

    @PostMapping("/webhook")
    public String handleWebhook(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String payload = IOUtils.toString(httpServletRequest.getReader());
            String sigHeader=httpServletRequest.getHeader("Stripe-Signature");
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            String eventType = event.getType();

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            }
            switch (eventType) {
                case "customer.created" -> {
                    Customer createdCustomer = (Customer) stripeObject;
                    assert createdCustomer != null;
                     handleCustomerCreated(createdCustomer);
                }
                case "customer.updated" -> {
                    Customer updatedCustomer = (Customer) stripeObject;
                    assert updatedCustomer != null;
                     handleCustomerUpdated(updatedCustomer);
                }
                case "customer.deleted" -> {
                     handleCustomerDeleted();
                }
                default -> {
                     ResponseEntity.ok("Unhandled event type: " + eventType);
                }
            }
        } catch (SignatureVerificationException e) {
            httpServletResponse.setStatus(400);
            return "";
        } catch (Exception e) {
            httpServletResponse.setStatus(400);
            return "";
        }

        httpServletResponse.setStatus(200);

        return  "Webhook handled successfully";
    }

    private ResponseEntity<String> handleCustomerCreated(Customer customer) {
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("CustomerId", customer.getId());
        customerDetails.put("Email", customer.getEmail());
        customerDetails.put("Name", customer.getName());
           return ResponseEntity.ok("Customer created: " + customerDetails);
    }

    private ResponseEntity<String> handleCustomerUpdated(Customer customer) {
        // Retrieve customer details
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("CustomerId", customer.getId());
        customerDetails.put("Email", customer.getEmail());
        customerDetails.put("Name", customer.getName());
        return ResponseEntity.ok("Customer updated: " + customer.getId());
    }

    private ResponseEntity<String> handleCustomerDeleted() {
                return ResponseEntity.ok("Customer deleted");
    }
}
