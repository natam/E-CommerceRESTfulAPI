package org.rest.resource;

import org.rest.dao.OrderDao;
import org.rest.dao.ProductDao;
import org.rest.entity.Product;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
public class ProductResource {
    ProductDao productDao = new ProductDao();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts(@QueryParam("category") String category,
                                     @QueryParam("offset") int offset,
                                     @QueryParam("limit") int limit){
        System.out.println(category);
        if(!category.isEmpty()){
            return productDao.getAllProducts(offset,limit).stream().filter(product -> product.getCategory().equals(category)).toList();
        }
        return productDao.getAllProducts(offset,limit);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProductById(@PathParam("id") int id){
        return productDao.getProduct(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addProduct(Product product) {
        productDao.addProduct(product);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateProduct(@PathParam("id") int id, Product product) {
        product.setId(id);
        productDao.updateProduct(product);
    }

    @DELETE
    @Path("/{id}")
    public void deleteProduct(@PathParam("id") int id) {
        productDao.deleteProduct(id);
    }
}
