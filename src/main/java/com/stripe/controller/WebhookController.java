package com.stripe.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String payload = IOUtils.toString(httpServletRequest.getReader());
            String sigHeader = httpServletRequest.getHeader("Stripe-Signature");
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            String eventType = event.getType();

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            }
            switch (eventType) {
                case "customer.created" -> {
                    Customer customer = (Customer) stripeObject;
                    CustomerData customerData = new CustomerData();
                    customerData.setCustomerId(customer.getId());
                    customerData.setEmail(customer.getEmail());
                    customerData.setCreated(customer.getCreated());
                    customerData.setPhone(customer.getPhone());
                    customerData.setName(customer.getName());
                    customerRepository.save(customerData);
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
            return ResponseEntity.ok("Signature verification failed");
        } catch (Exception e) {
            httpServletResponse.setStatus(400);
            return ResponseEntity.ok("Error processing webhook");
        }

        httpServletResponse.setStatus(200);
        return ResponseEntity.ok("Webhook handled successfully");
    }

    private void handleCustomerCreated(Customer customer) {
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("CustomerId", customer.getId());
        customerDetails.put("Email", customer.getEmail());
        customerDetails.put("Name", customer.getName());
        ResponseEntity.ok("Customer created: " + customerDetails);
    }

    private void handleCustomerUpdated(Customer customer) {
        // Retrieve customer details
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("CustomerId", customer.getId());
        customerDetails.put("Email", customer.getEmail());
        customerDetails.put("Name", customer.getName());
        ResponseEntity.ok("Customer updated: " + customer.getId());
    }

    private void handleCustomerDeleted() {
        ResponseEntity.ok("Customer deleted");
    }
}
