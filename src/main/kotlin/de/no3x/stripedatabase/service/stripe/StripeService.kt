package de.no3x.stripedatabase.stripe

import com.stripe.model.Customer
import com.stripe.model.Subscription
import com.stripe.param.SubscriptionListParams
import de.no3x.stripedatabase.service.stripe.dao.CustomerDao
import org.springframework.stereotype.Service

@Service
class StripeService(val customerDao: CustomerDao, val productService: ProductService) {

    fun findCustomerIdByEmail(email: String): String? {
        return findCustomerByEmail(email)?.id
    }

    fun findCustomerByEmail(email: String): Customer? {
        return customerDao.customers().first { it.email == email }
    }

    fun hasActiveSubscription(email: String, productName: String): Boolean {
        val customerId = findCustomerIdByEmail(email) ?: return false

        val subscriptionListParams = SubscriptionListParams.builder()
            .setCustomer(customerId)
            .setStatus(SubscriptionListParams.Status.ACTIVE)
            .build()

        val subscriptions = Subscription.list(subscriptionListParams)

        val productByName = productService.getProductByName(productName) ?: return false

        return subscriptions.data
            .flatMap { it.items.data }
            .any { it.price.product == productByName.id }
    }

}