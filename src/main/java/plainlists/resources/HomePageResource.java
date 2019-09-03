package com.semantyca.admin.resources;

import com.toonext.core.api.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HomePageResource {

    @GET
    public String handleGetRequest(@Auth User tokenPrincipal) {
        return "Hello! We'll be contacting you at: " + tokenPrincipal.getName();
    }
}