package com.semantyca.admin.api;

import com.toonext.EnvConst;
import com.toonext.core.api.User;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserMapper {

    public List<User> getAllUsers(){
        Properties props = new Properties();
        props.setProperty("user", EnvConst.DB_USER);
        props.setProperty("password", EnvConst.DB_PWD);
        String dbURL = "jdbc:postgresql://" + EnvConst.DATABASE_HOST + ":" + EnvConst.CONN_PORT + "/" + EnvConst.DATABASE_NAME;
        Jdbi jdbi = Jdbi.create(dbURL, props).installPlugin(new PostgresPlugin());
        try (Handle handle = jdbi.open()) {
        /*List<User> users = handle.createQuery("SELECT id, login FROM _users ORDER BY id ASC")
                .map((rs, ctx) -> new User(rs.getInt("id"), rs.getString("login")))
                .list();*/
            return new ArrayList<>();
        }
    }
}
