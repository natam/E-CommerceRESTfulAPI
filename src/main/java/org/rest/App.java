package org.rest;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.rest.resource.OrderResource;
import org.rest.resource.ProductResource;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

public class App {
    public static void main(String[] args) {
        ResourceConfig resourceConfig = new ResourceConfig(new HashSet<>(Arrays.asList (OrderResource.class, ProductResource.class)));
        GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8001/"),resourceConfig);
    }
}
