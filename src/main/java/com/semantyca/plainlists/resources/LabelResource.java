package com.semantyca.plainlists.resources;

import com.semantyca.plainlists.api.Label;
import com.semantyca.plainlists.controller.LabelController;
import com.semantyca.plainlists.dao.ILabelDAO;
import com.toonext.UserSession;
import com.toonext.domain.user.AnonymousUser;
import com.toonext.domain.user.IUser;
import com.toonext.domain.user.SuperUser;
import com.toonext.dto.Outcome;
import io.dropwizard.auth.Auth;
import org.elasticsearch.client.RestHighLevelClient;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;

@Path("/labels")
@Produces(MediaType.APPLICATION_JSON)
public class LabelResource {
    private ILabelDAO dao;
    private LabelController controller;

    public LabelResource(Jdbi jdbi, RestHighLevelClient elasticClient) {
        dao = jdbi.onDemand(ILabelDAO.class);
        controller = new LabelController(dao, elasticClient);
    }


    @GET
    @Path("/{name}")
    public Response find(@Auth IUser user, @PathParam("name") String name, @QueryParam("page") String page, @QueryParam("pagesize") String pageSize) {
        Outcome outcome = new Outcome();
        if (name.equalsIgnoreCase("all")){
            outcome.addPayload(controller.getAll(page, pageSize, AnonymousUser.ID));
        }else {
            outcome.addPayload(dao.findByIdentifier(name));
        }
        outcome.addPayload(new UserSession(user));
        return Response.ok().entity(outcome).build();
    }


    @GET
    @Path("/search")
    public Response search(@Auth IUser user, @QueryParam("keyword") String keyword, @QueryParam("page") String page, @QueryParam("pagesize") String pageSize) {
        Outcome outcome = new Outcome();
        outcome.addPayload(controller.search(page, pageSize, keyword));
        outcome.addPayload(new UserSession(user));
        return Response.ok().entity(outcome).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Outcome post(Label composition) {
        Outcome outcome = new Outcome();
        ZonedDateTime now = ZonedDateTime.now();
        composition.setRegDate(now);
        composition.setLastModifiedDate(now);
        composition.setAuthor(SuperUser.ID);
        composition.setLastModifier(SuperUser.ID);
        return outcome.addPayload(dao.insert(composition));
    }
}