package de.no3x.stripedatabase.config

import com.stripe.Stripe
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class StripeInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    val logger = LoggerFactory.getLogger(this::class.java)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val env = applicationContext.environment
        val apiKey = env.getProperty("stripe.api.key")
            ?: error("Missing Stripe API key")

        // Set Stripe API key early in the Spring lifecycle
        Stripe.apiKey = apiKey
        logger.info("Stripe API key initialized in configuration")
    }
}
