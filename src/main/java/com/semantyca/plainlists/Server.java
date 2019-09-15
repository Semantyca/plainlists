package com.semantyca.plainlists;

import com.semantyca.plainlists.resources.LabelResource;
import com.semantyca.plainlists.task.PopulateTestData;
import com.toonext.ServerStarter;
import io.dropwizard.setup.Environment;

public class Server extends ServerStarter<com.semantyca.plainlists.MainConfiguration> {

    public static void main(String[] args) throws Exception {
        new Server().run(args);
    }

    @Override
    public String getName() {
        return "plainlists";
    }

    public void run(com.semantyca.plainlists.MainConfiguration config, Environment environment) {
        super.run(config, environment);
        environment.admin().addTask(new PopulateTestData(jdbi));
        environment.jersey().register(new LabelResource(jdbi, elasticClient));
    }
}
