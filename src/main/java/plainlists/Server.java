package com.semantyca.admin;

import com.toonext.ServerStarter;

public class Server extends ServerStarter<MainConfiguration> {

    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public String getName() {
        return "administrator";
    }

}
