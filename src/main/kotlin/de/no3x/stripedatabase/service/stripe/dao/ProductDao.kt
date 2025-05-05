package de.no3x.stripedatabase.stripe.dao

import com.stripe.model.Product
import com.stripe.model.ProductCollection
import com.stripe.param.ProductListParams
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ProductDao {

    val logger = LoggerFactory.getLogger(this::class.java)

    private var productList: ProductCollection? = null

    @PostConstruct
    fun init() {
        productList = Product.list(ProductListParams.builder().build())
        logger.debug("Loaded productList ({}): ", productList!!.data.size)
        logger.debug("Loaded products: {}", productList!!.data.map { it.name to it.id })
    }

    @Cacheable("ProductList")
    fun products(): List<Product> =
        productList!!.data
}