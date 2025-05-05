package de.no3x.stripedatabase.config

import de.no3x.stripedatabase.security.StripeAuthenticationProvider
import de.no3x.stripedatabase.security.permission.StripePermissionEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val stripePermissionEvaluator: StripePermissionEvaluator
) {

    @Bean
    fun methodSecurityExpressionHandler(): DefaultMethodSecurityExpressionHandler {
        val handler = DefaultMethodSecurityExpressionHandler()
        handler.setPermissionEvaluator(stripePermissionEvaluator)
        return handler
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        http
            .csrf { it.disable() } // Optional, depending on your client setup
            .authorizeHttpRequests {
                it.requestMatchers("/login", "/public/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            }
            .logout { it.logoutUrl("/logout").logoutSuccessUrl("/login") }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }

        return http.build()
    }

    @Bean
    fun authenticationManager(
        stripeAuthenticationProvider: StripeAuthenticationProvider
    ): AuthenticationManager {
        return ProviderManager(stripeAuthenticationProvider)
    }

}