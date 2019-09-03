package com.semantyca.admin.security;

import com.toonext.core.dao.ITokenDAO;
import com.toonext.domain.user.Token;
import com.toonext.log.Lg;
import io.dropwizard.auth.AuthFilter;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import java.security.Principal;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class CustomAuthFilter<P extends Principal> extends AuthFilter<CustomCredentials, P> {
  protected  Jdbi dbi;

  protected CustomAuthFilter(Jdbi jdbi){
      super();
      this.dbi = jdbi;
  }


  public void filter(ContainerRequestContext requestContext) {

      CustomCredentials credentials = getCredentials(requestContext);
      if (!this.authenticate(requestContext, credentials, "custom")) {
        throw new WebApplicationException(this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
      }
  }


  private CustomCredentials getCredentials(ContainerRequestContext requestContext) {
    CustomCredentials credentials = new CustomCredentials();
    ITokenDAO dao = dbi.onDemand(ITokenDAO.class);
    try {
      String tokenString = requestContext.getHeaderString("Authorization");
      Token token = dao.findToken(tokenString.substring(6));
      if (token != null) {
        credentials.setToken(token.getToken());
        credentials.setUserId(token.getUserId());
      }
    } catch (Exception e) {
      Lg.exception(e);
      credentials.setToken("0");
      credentials.setUserId(Long.valueOf(0));
    }


    return credentials;
  }

  public static class Builder<P extends Principal> extends AuthFilterBuilder<CustomCredentials, P, CustomAuthFilter<P>> {
    private static Jdbi dbi;

    public Builder(Jdbi dbi) {
      this.dbi = dbi;
    }

    protected CustomAuthFilter<P> newInstance() {
      return new CustomAuthFilter(dbi);
    }
  }
}
