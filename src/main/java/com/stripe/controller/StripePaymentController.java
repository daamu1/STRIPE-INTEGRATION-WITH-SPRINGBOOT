package com.stripe.controller;

import com.stripe.DTO.CustomerRequest;
import com.stripe.DTO.SubscriptionRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.CustomerData;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.repository.CustomerRepository;
import com.stripe.utils.StripeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripePaymentController {
    @Value("${stripe.apikey}")
   private String stripeKey;
    @Autowired
    StripeUtil stripeUtil;
    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/createCustomer")
    public void createCustomer(@RequestBody CustomerRequest customerRequest) throws StripeException {
        Stripe.apiKey = stripeKey;
        Map<String, Object> params = new HashMap<>();
        params.put("name", customerRequest.getName());
        params.put("email", customerRequest.getEmail());
        params.put("phone", customerRequest.getPhone());
        Customer customer = Customer.create(params);
    }


    @GetMapping("/getAllCustomer")
    public List<CustomerData> getAllCustomer() throws StripeException {
        Stripe.apiKey = stripeKey;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 5);

        CustomerCollection customers = Customer.list(params);
        List<CustomerData> allCustomer = new ArrayList<CustomerData>();
        for (int i = 0; i < customers.getData().size(); i++) {
            CustomerData customerData = new CustomerData();
            customerData.setCustomerId(customers.getData().get(i).getId());
            customerData.setName(customers.getData().get(i).getName());
            customerData.setEmail(customers.getData().get(i).getEmail());
            allCustomer.add(customerData);
        }
        return allCustomer;
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable("id") String id) throws StripeException {
        Stripe.apiKey = stripeKey;

        Customer customer = Customer.retrieve(id);

        Customer deletedCustomer = customer.delete();
        return "successfully deleted";
    }

    @GetMapping("/getCustomer/{id}")
    public CustomerData getCustomer(@PathVariable("id") String id) throws StripeException {

        return stripeUtil.getCustomer(id);
    }

    @PostMapping("/create-subscription")
    public Subscription createSubscription(@RequestBody SubscriptionRequest subscriptionRequest) throws StripeException {
        Stripe.apiKey = stripeKey;

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(subscriptionRequest.getCustomerId())
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(subscriptionRequest.getPriceId())
                        .build())
                .build();

        return Subscription.create(params);
    }
}


