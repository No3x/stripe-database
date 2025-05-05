package de.no3x.stripedatabase.security.permission

import de.no3x.stripedatabase.stripe.StripeService
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class StripePermissionEvaluator(
    private val stripeService: StripeService
) : PermissionEvaluator {

    override fun hasPermission(
        auth: Authentication,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean {
        val email = auth.name
        val productId = permission as? String ?: return false
        return stripeService.hasActiveSubscription(email, productId)
    }

    override fun hasPermission(
        auth: Authentication,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ) = false
}