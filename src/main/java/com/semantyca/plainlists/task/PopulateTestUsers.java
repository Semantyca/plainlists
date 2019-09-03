package com.semantyca.plainlists.task;

import com.toonext.core.api.User;
import com.toonext.core.jdbi.IUserDAO;
import com.toonext.domain.user.SuperUser;
import com.toonext.localization.constants.LanguageCode;
import io.dropwizard.servlets.tasks.Task;
import org.jdbi.v3.core.Jdbi;

import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;


public class PopulateTestUsers extends Task {
    private final Jdbi dbi;
    private final static String[] testUsers = {"test1", "test2", "test3", "test4", "test5", "test6", "test7"};
    private final static String DEFAULT_PASSWORD = "123";

    public PopulateTestUsers(Jdbi jdbi) {
        super("populate_test_users");
        this.dbi = jdbi;
    }


    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {

        IUserDAO dao = dbi.onDemand(IUserDAO.class);
        for (String userName: testUsers) {
            ZonedDateTime now = ZonedDateTime.now();
            User user = new User();
            user.setUserName(userName);
            user.setPwd(DEFAULT_PASSWORD);
            user.setDefaultLang(LanguageCode.ENG);
            user.setLogin(userName);
            user.setRegDate(now);
            user.setLastModifiedDate(now);
            user.setAuthor(SuperUser.ID);
            user.setLastModifier(SuperUser.ID);

            dao.insert(user);
        }

    }
}
