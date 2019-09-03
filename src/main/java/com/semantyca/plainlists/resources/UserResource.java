package com.semantyca.plainlists.resources;


import com.toonext.core.jdbi.IUserDAO;
import com.toonext.domain.user.IUser;
import com.toonext.domain.user.ServerRole;
import com.toonext.dto.Outcome;
import com.toonext.util.SessionsTracker;
import io.dropwizard.auth.Auth;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    IUserDAO dao;

    public UserResource(Jdbi jdbi) {
        dao = jdbi.onDemand(IUserDAO.class);
    }

    @GET
    @RolesAllowed(ServerRole.AUTHENTICATED)
    public Outcome getAll(@Auth IUser user) {
        Outcome outcome = new Outcome();
        outcome.addPayload(SessionsTracker.getSession(user.getId()));
        return outcome.addPayload(dao.findAll());
    }
}