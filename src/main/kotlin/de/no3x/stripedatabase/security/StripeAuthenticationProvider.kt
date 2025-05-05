package de.no3x.stripedatabase.security

import de.no3x.stripedatabase.stripe.StripeService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class StripeAuthenticationProvider(
    private val stripeService: StripeService,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val email = authentication.name // Email is used as the username
        val password = authentication.credentials as? String ?: throw BadCredentialsException("No password provided")

        // Lookup customer in Stripe by email
        val customer = stripeService.findCustomerByEmail(email)
            ?: throw UsernameNotFoundException("No user found with email $email")

        // Check if the provided password matches the expected password
        if (!passwordEncoder.matches(password, customer.metadata["password"] ?: "")) {
            throw BadCredentialsException("Invalid password")
        }

        // If all checks pass, authenticate the user
        val userDetails: UserDetails = User.builder()
            .username(customer.email)
            .password(password) // You can set the password here as part of the authentication flow
            .roles("USER") // Assign roles as needed
            .build()

        return UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}