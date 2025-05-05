# Spring Security Stripe Database

This POC shows how to use stripe as single source of truth for users with spring security.
Usually you have a database with the users but this approach tries to avoid it.

## Setup
### Environment
Copy the `.env.example` file to `.env` and fill in the values.
```
STRIPE_API_KEY=sk_test_abc
```

### Stripe
Some data must be created in stripe before testing the application.
#### Customer
For testing a customer is created with an email and a meta field `password` with the password of the customer.
#### Product
Then a product `t1` is created.
#### Subscription
Then an active subscription with the t1 product is created for the customer.

# Usage
When running the application and accessing http://localhost:9000/login you should be able to log in with the customer email and the password.
The password is stored in plain text in the meta field of the customer and compared to the login password. Please do not use this in production.

The http://localhost:9000/premium endpoint is protected and only accessible for users with an active subscription to the t1 product.

The premium endpoint is configured with the `@PreAuthorize` annotation which takes the product as second parameter:
```
@PreAuthorize("hasPermission(null, 't1')")
```
I'm not sure if this is the best way to do it. Maybe it is better to not do it with permissions and just call a service directly.

# Caching
When the application is started the customers and the products are fetched from the stripe api and cached in memory.