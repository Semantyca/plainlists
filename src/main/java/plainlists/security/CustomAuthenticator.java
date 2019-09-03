package com.semantyca.admin.security;

import com.toonext.UserSession;
import com.toonext.core.jdbi.IUserDAO;
import com.toonext.domain.user.AnonymousUser;
import com.toonext.domain.user.IUser;
import com.toonext.domain.user.ServerRole;
import com.toonext.util.SessionsTracker;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class CustomAuthenticator implements Authenticator<CustomCredentials, IUser> {
    private IUserDAO userDAO;

    public CustomAuthenticator(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<IUser> authenticate(CustomCredentials credentials) throws AuthenticationException {
        IUser authenticatedUser = null;
        if (credentials.getUserId() == 0) {
            authenticatedUser = new AnonymousUser();
        } else {
            IUser user = userDAO.findById(credentials.getUserId());
            if (user != null) {
                user.addRole(ServerRole.AUTHENTICATED);
                SessionsTracker.addSession(new UserSession(user));
                authenticatedUser = user;
            }
        }
        return Optional.ofNullable(authenticatedUser);
    }
}
