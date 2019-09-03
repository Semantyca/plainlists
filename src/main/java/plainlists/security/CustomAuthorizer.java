package com.semantyca.admin.security;


import com.toonext.domain.user.IUser;
import io.dropwizard.auth.Authorizer;

public class CustomAuthorizer implements Authorizer<IUser> {
    @Override
    public boolean authorize(IUser user, String role) {
        if (user.getRoles().contains(role)){
            return true;
        }
        return false;
    }
}
