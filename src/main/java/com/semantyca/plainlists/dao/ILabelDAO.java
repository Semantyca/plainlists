package com.semantyca.plainlists.dao;


import com.toonext.core.dao.IDAO;
import com.toonext.core.jdbi.BindJson;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ILabelDAO extends IDAO {

    @SqlUpdate(
            "CREATE TABLE public.pl__labels" +
            "(" +
            "  color character varying(64)," +
            "  category character varying(128)," +
            "  hidden boolean," +
                    IDAO.MANDATORY_REF_DDL_PIECE +
            ");")
    void createTable();

    @SqlUpdate("INSERT INTO public.pl__labels(color, category, hidden, loc_name, identifier, last_mod_date, last_mod_user, reg_date, title, author)VALUES (?,?,?,?::jsonb,?,?,?,?,?,?);")
    @GetGeneratedKeys("id")
    UUID insert(String color, String category, boolean isHidden, @BindJson("loc_name") String locName, String identifier, ZonedDateTime lastModifiedDate, long lastModifier,
                ZonedDateTime regDate, String title, long author);

}
