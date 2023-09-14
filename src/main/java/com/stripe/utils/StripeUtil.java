package com.stripe.utils;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeUtil {

    @Value("${stripe.apikey}")
    String stripeKey;

    public CustomerData getCustomer(String id) throws StripeException {
        Stripe.apiKey = stripeKey;
        Customer customer = Customer.retrieve(id);
        return setCustomerData(customer);
    }

    public CustomerData setCustomerData(Customer customer) {
        CustomerData customerData = new CustomerData();
        customerData.setCustomerId(customer.getId());
        customerData.setName(customer.getName());
        customerData.setEmail(customer.getEmail());
        customerData.setCreated(customer.getCreated());
        customerData.setPhone(customer.getPhone());
        return customerData;
    }
}