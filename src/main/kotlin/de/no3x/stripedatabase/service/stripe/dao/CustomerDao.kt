package de.no3x.stripedatabase.service.stripe.dao

import com.stripe.model.Customer
import com.stripe.model.CustomerCollection
import com.stripe.param.CustomerListParams
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CustomerDao {

    val logger = LoggerFactory.getLogger(this::class.java)

    private var customerList: CustomerCollection? = null

    @PostConstruct
    fun init() {
        customerList = Customer.list(CustomerListParams.builder().build())
        logger.debug("Loaded customerList ({}): ", customerList!!.data.size)
        logger.debug("Loaded customers: {}", customerList!!.data.map { it.name to it.id })
    }

    @Cacheable("CustomerList")
    fun customers(): List<Customer> =
        customerList!!.data
}