package com.semantyca.plainlists.task;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.semantyca.plainlists.api.Label;
import com.semantyca.plainlists.dao.ILabelDAO;
import com.toonext.core.task.CommonTask;
import com.toonext.domain.user.SuperUser;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.io.InputStream;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PopulateTestData extends CommonTask {
    private final Jdbi dbi;


    public PopulateTestData(Jdbi jdbi) {
        super("populate_test_data");
        this.dbi = jdbi;
    }


    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        ILabelDAO dao = dbi.onDemand(ILabelDAO.class);

        try {
            dao.createTable();
        } catch (
                UnableToExecuteStatementException e) {
            logDatabaseException(e, "42P07");
        }


        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InputStream is = PopulateTestData.class.getResourceAsStream("/testData.yml");
        JsonNode rootNode = yamlMapper.readTree(is);
        JsonNode drNode = rootNode.path("labels");
        Iterator<JsonNode> itr = drNode.elements();
        while (itr.hasNext()) {
            Label label = yamlMapper.treeToValue(itr.next(), Label.class);
            ZonedDateTime now = ZonedDateTime.now();
            label.setRegDate(now);
            label.setLastModifiedDate(now);
            label.setAuthor(SuperUser.ID);
            label.setLastModifier(SuperUser.ID);
            dao.insert(label.getColor(), label.getCategory(), label.isHidden(), jsonMapper.writeValueAsString(label.getLocName()), label.getIdentifier(), label.getLastModifiedDate(),
                    label.getLastModifier(), label.getRegDate(), label.getTitle(), label.getAuthor());
        }
    }
}

