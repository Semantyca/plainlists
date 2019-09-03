package com.semantyca.admin.resources;


import com.toonext.core.jdbi.ILanguageDAO;
import com.toonext.dto.Outcome;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/languages")
@Produces(MediaType.APPLICATION_JSON)
public class LanguageResource {
    ILanguageDAO dao;

    public LanguageResource(Jdbi jdbi) {
        dao = jdbi.onDemand(ILanguageDAO.class);
    }

    @GET
    public Outcome getAll() {
        Outcome outcome = new Outcome();
        return outcome.addPayload(dao.findAllActivated());
    }
}