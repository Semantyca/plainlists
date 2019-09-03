package com.semantyca.plainlists.resources;


import com.codahale.metrics.annotation.Timed;
import com.toonext.UserSession;
import com.toonext.core.dao.ITokenDAO;
import com.toonext.core.jdbi.ILanguageDAO;
import com.toonext.core.jdbi.IUserDAO;
import com.toonext.domain.user.IUser;
import com.toonext.domain.user.Token;
import com.toonext.dto.Credentials;
import com.toonext.dto.Outcome;
import com.toonext.dto.OutcomeType;
import com.toonext.log.Lg;
import com.toonext.officeframe.api.Employee;
import com.toonext.officeframe.jdbi.EmployeeDAO;
import com.toonext.util.StringUtil;
import io.dropwizard.auth.Auth;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {
    private UserSession ses;
    private Jdbi jdbi;
    private Validator validator;


    public SessionResource(Jdbi jdbi, Validator validator) {
        this.jdbi = jdbi;
        this.validator = validator;
    }

    @GET
    @Timed
    @PermitAll
    public Outcome get(@Auth IUser user) {
        Outcome outcome = new Outcome();
        ses = getSession(user);
        try {
            outcome.setTitle(user.getUserName());
            //   outcome.addPayload("orgLogo", Environment.logo);
            //   String template = Environment.templates.getTemplate(MessagingType.SITE, "about", ses.getLang());
            //   outcome.addPayload("about",  TemplatesSet.getRenderedTemplate(template).render());
            //    outcome.addPayload("orgName", Environment.orgName);
            //    outcome.addPayload("orgWallpaper", Environment.wallpaper);
            //   outcome.addPayload("orgColor", Environment.color);
            //    outcome.addPayload("serverVersion", EnvConst.SERVER_VERSION);
            //    outcome.addPayload("build", Server.compilationTime);
            ILanguageDAO languageDAO = jdbi.onDemand(ILanguageDAO.class);
            outcome.addPayload("languages", languageDAO.findAllActivated());
            //    outcome.addPayload("uiThemes", new IModuleDAO(ses).findAllActivated().get(0).getAvailableThemes());
            //    outcome.addPayload("subscriptions", new SubscriptionDAO(ses).findAll());
        } catch (Exception e) {
            Lg.exception(e);
        }
        //   outcome.addPayload(ses);


        EmployeeDAO dao = new EmployeeDAO(ses);
        if (!user.isAnonymous() && dao != null) {
            Employee employee = dao.findByUserId(user.getId());
            if (employee == null) {
                Lg.warning("\"" + user.getLogin() + " " + user.getId() + "\" is not Employee");
            } else {
                //  outcome.addPayload("employee", employee);
            }
        }
        return outcome;

    }

    @Path("/log-in")
    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(@Context HttpServletRequest request, @Valid Credentials credentials) {
        Outcome outcome = new Outcome();
        IUserDAO userDAO = jdbi.onDemand(IUserDAO.class);
        IUser user = userDAO.findNameByLogin(credentials.getLogin());
        if (user != null) {
            if (user.getPwdHash().equals(StringUtil.encode(credentials.getPassword()))) {
                ITokenDAO tokenDAO = jdbi.onDemand(ITokenDAO.class);
                Token token = new Token();
                token.setUserId(user.getId());
                token.setToken(new RandomString().nextString());
                tokenDAO.insert(token.getUserId(), ZonedDateTime.now(), token.getToken(), null);
                outcome.addPayload("token", token.getToken());
                outcome.setTitle("authenticated");
            } else {
                outcome.setTitle("password is incorrect");
                outcome.setType(OutcomeType.AUTHENTICATION_FAILURE);
                outcome.setTitle("user not found");
                return Response.status(401).entity(outcome).build();
            }
        } else {
            outcome.setType(OutcomeType.AUTHENTICATION_FAILURE);
            outcome.setTitle("user not found");
            return Response.status(401).entity(outcome).build();
        }
        return Response.ok().entity(outcome).build();
    }


    public final UserSession getSession(IUser user) {
        if (ses == null) {
            return new UserSession(user);
        }
        return ses;
    }


}