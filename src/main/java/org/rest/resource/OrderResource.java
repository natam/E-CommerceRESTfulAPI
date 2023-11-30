package org.rest.resource;

import org.rest.dao.OrderDao;
import org.rest.entity.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")
public class OrderResource {
    OrderDao orderDao = new OrderDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getOrders(@QueryParam("customer") String customer,
                                 @QueryParam("offset") int offset,
                                 @QueryParam("limit") int limit){
        if(!customer.isEmpty()){
            return orderDao.getOrders(offset,limit).stream().filter(order -> order.getCustomer().equals(customer)).toList();
        }
        return orderDao.getOrders(offset,limit);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getProductById(@PathParam("id") int id){
        return orderDao.getOrder(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addOrder(Order order) {
        orderDao.addOrder(order);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOrder(@PathParam("id") int id, Order order) {
        order.setId(id);
        orderDao.updateOrder(order);
    }

    @DELETE
    @Path("/{id}")
    public void deleteOrder(@PathParam("id") int id) {
        orderDao.deleteOrder(id);
    }
}
