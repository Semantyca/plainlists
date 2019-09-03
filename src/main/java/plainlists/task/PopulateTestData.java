package com.semantyca.admin.task;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.toonext.core.api.User;
import com.toonext.core.jdbi.ILanguageDAO;
import com.toonext.domain.user.IUser;
import io.dropwizard.servlets.tasks.Task;
import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PopulateTestData extends Task {
    private final Jdbi dbi;


    public PopulateTestData(Jdbi jdbi) {
        super("populate_test_data");
        this.dbi = jdbi;
    }


    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();

        ILanguageDAO dao = dbi.onDemand(ILanguageDAO.class);
        dao.createTable();


        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<IUser> users = new ArrayList<>();
        InputStream is = PopulateTestData.class.getResourceAsStream("/testData.yml");
        JsonNode rootNode = yamlMapper.readTree(is);
        JsonNode drNode = rootNode.path("users");
        Iterator<JsonNode> itr = drNode.elements();
        while (itr.hasNext()) {
            IUser user = yamlMapper.treeToValue(itr.next(), User.class);
            users.add(user);

       //     String jsonText = jsonMapper.writeValueAsString(user.getLocName());
         //   dao.insert(user.getCode(), user.isCyrillic(), user.isOn(), ZonedDateTime.now(), SuperUser.ID, jsonText, user.getName(),
          //          user.getStance(),ZonedDateTime.now(),"title", SuperUser.ID);
        }


    }
}
