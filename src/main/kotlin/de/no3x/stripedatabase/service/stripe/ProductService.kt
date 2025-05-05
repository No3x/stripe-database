package de.no3x.stripedatabase.stripe

import com.stripe.model.Product
import de.no3x.stripedatabase.stripe.dao.ProductDao
import org.springframework.stereotype.Service

@Service
class ProductService(val productDao : ProductDao) {

   fun getProductByName(name: String): Product? {
       return productDao.products().firstOrNull { it.name == name }
   }

}