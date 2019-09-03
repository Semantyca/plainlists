package com.semantyca.plainlists;

import com.toonext.ServerStarter;

public class Server extends ServerStarter<com.semantyca.admin.MainConfiguration> {

    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public String getName() {
        return "plainlists";
    }

}
