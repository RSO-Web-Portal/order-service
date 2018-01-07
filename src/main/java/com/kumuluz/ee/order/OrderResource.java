/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
*/
package com.kumuluz.ee.order;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Optional;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;


@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("orders")
@Log(LogParams.METRICS)
public class OrderResource {


    @Inject
    @DiscoverService(value = "item-service", version = "1.0.x", environment = "dev")
    private Optional<WebTarget> target;



    @GET
    public Response getAllOrders() {
        List<Order> categories = Database.getOrders();
        return Response.ok(categories).build();
    }


    @GET
    @Path("{id}")
    public Response getOrder(@PathParam("id") String id) {

        Order order = Database.getOrder(id);
        return order != null
                ? Response.ok(order).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/items")
    public Response getOrderItems(@PathParam("id") String id) {

        Order order = Database.getOrder(id);

        if (order == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }


        if (!target.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WebTarget itemService;



        Response response;
        Item[] items = new Item[order.getItemIds().length];
        try {

            for (int i = 0; i < order.getItemIds().length; i++) {
                itemService = target.get().path("v1/items/" + order.getItemIds()[i]);
                response = itemService.request().get();
                Item it = (response.readEntity(Item.class));
                items[i] = it;
            }

            return Response.ok(items).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(408).build();
        }

    }

    @GET
    @Path("{id}/active")
    public Response isActive(@PathParam("id") String id) {
        //TODO active - check catalogue if order active
        return Response.notModified().build();
    }

    @POST
    public Response addOrder(Order order) {
        Database.addOrder(order);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteOrder(@PathParam("id") String id) {
        Database.deleteOrder(id);
        return Response.ok().build();
    }
}
