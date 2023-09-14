package com.stripe.controller;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;

@RestController
@RequestMapping("/api/stripe")
@Slf4j
public class WebhookContrller {

    String stripeKey = "sk_test_51NpoAcSBGRunvJYBjVwLmtV9BamzVAQW6oAgFAU0MDebFd4oKQB160aZVFQzQHrwQ1HbDNDtdTRRRTMlwwatEJll00UUuEW4BX";
    // This is your Stripe CLI webhook secret for testing your endpoint locally.
    String endpointSecret = "whsec_3NCMVUudg7spZNLWlL0NJuVQtf3092hi";
    @Autowired
    private HttpServletResponse httpServletResponse;

    @PostMapping("/webhook")
    public void webhookMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String sigHeader = request.getHeader("Stripe-Signature");
        BufferedReader reader = request.getReader();
        log.info("payload ____________>" + request.getReader().readLine());

        Event event = null;

        try {
            event = Webhook.constructEvent(request.getReader().readLine(), sigHeader, endpointSecret);
            log.info("------------->" + event);
            // Deserialize the nested object inside the event
//                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//                StripeObject stripeObject = null;
//                if (dataObjectDeserializer.getObject().isPresent()) {
//                    stripeObject = dataObjectDeserializer.getObject().get();
//                }

        } catch (Exception e) {
            // Invalid payload
            httpServletResponse.setStatus(400);
        }

//        post("/webhook", (request, response) -> {
//            String payload = request.body();
//            String sigHeader = request.headers("Stripe-Signature");
//            Event event = null;
//
//            try {
//                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
//            } catch (Exception e) {
//                // Invalid payload
//                response.status(400);
//                return "";
//            }
//
//            // Deserialize the nested object inside the event
//            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//            StripeObject stripeObject = null;
//            if (dataObjectDeserializer.getObject().isPresent()) {
//                stripeObject = dataObjectDeserializer.getObject().get();
//            }
//            // Handle the event
//            switch (event.getType()) {
//                case "customer.created" -> {
//                    // Then define and call a function to handle the event customer.created
//                }
//                case "customer.deleted" -> {
//                    // Then define and call a function to handle the event customer.deleted
//                }
//                case "customer.updated" -> {
//                    // Then define and call a function to handle the event customer.updated
//                }
//                case "customer.discount.created" -> {
//                    // Then define and call a function to handle the event customer.discount.created
//                }
//                case "customer.discount.deleted" -> {
//                    // Then define and call a function to handle the event customer.discount.deleted
//                }
//                case "customer.discount.updated" -> {
//                    // Then define and call a function to handle the event customer.discount.updated
//                }
//                case "customer.source.created" -> {
//                    // Then define and call a function to handle the event customer.source.created
//                }
//                case "customer.source.deleted" -> {
//                    // Then define and call a function to handle the event customer.source.deleted
//                }
//                case "customer.source.expiring" -> {
//                    // Then define and call a function to handle the event customer.source.expiring
//                }
//                case "customer.source.updated" -> {
//                    // Then define and call a function to handle the event customer.source.updated
//                }
//                case "customer.subscription.created" -> {
//                    // Then define and call a function to handle the event customer.subscription.created
//                }
//                case "customer.subscription.deleted" -> {
//                    // Then define and call a function to handle the event customer.subscription.deleted
//                }
//                case "customer.subscription.paused" -> {
//                    // Then define and call a function to handle the event customer.subscription.paused
//                }
//                case "customer.subscription.pending_update_applied" -> {
//                    // Then define and call a function to handle the event customer.subscription.pending_update_applied
//                }
//                case "customer.subscription.pending_update_expired" -> {
//                    // Then define and call a function to handle the event customer.subscription.pending_update_expired
//                }
//                case "customer.subscription.resumed" -> {
//                    // Then define and call a function to handle the event customer.subscription.resumed
//                }
//                case "customer.subscription.trial_will_end" -> {
//                    // Then define and call a function to handle the event customer.subscription.trial_will_end
//                }
//                case "customer.subscription.updated" -> {
//                    // Then define and call a function to handle the event customer.subscription.updated
//                }
//                case "customer.tax_id.created" -> {
//                    // Then define and call a function to handle the event customer.tax_id.created
//                }
//                case "customer.tax_id.deleted" -> {
//                    // Then define and call a function to handle the event customer.tax_id.deleted
//                }
//                case "customer.tax_id.updated" -> {
//                    // Then define and call a function to handle the event customer.tax_id.updated
//                }
//
//                // ... handle other event types
//                default -> System.out.println("Unhandled event type: " + event.getType());
//            }
//
//            response.status(200);
//            return "";
//        });
    }

}
