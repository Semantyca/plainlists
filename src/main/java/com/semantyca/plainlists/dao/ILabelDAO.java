package com.semantyca.plainlists.dao;


import com.semantyca.plainlists.ModuleConst;
import com.semantyca.plainlists.api.Label;
import com.toonext.core.dao.IDAO;
import com.toonext.core.jdbi.BindJson;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ILabelDAO extends IDAO {
    String TABLE_SHORT_NAME = ModuleConst.CODE + "__labels";
    String TABLE_FULL_NAME = SCHEMA + "."  + TABLE_SHORT_NAME;

    @SqlQuery("SELECT * FROM " + TABLE_FULL_NAME + " as s WHERE s.identifier = :identifier;")
    @RegisterColumnMapper(LabelMapper.class)
    Label findByIdentifier(@Bind("identifier") String identifier);

    @SqlUpdate(
            "CREATE TABLE " + TABLE_FULL_NAME +
            "(" +
            "  color character varying(64)," +
            "  category character varying(128)," +
            "  hidden boolean," +
                    IDAO.MANDATORY_REF_DDL_PIECE +
            ");")
    void createTable();

    @SqlUpdate("INSERT INTO " + TABLE_FULL_NAME + "(color, category, hidden, loc_name, identifier, last_mod_date, last_mod_user, reg_date, title, author)VALUES (?,?,?,?::jsonb,?,?,?,?,?,?);")
    @GetGeneratedKeys("id")
    UUID insert(String color, String category, boolean isHidden, @BindJson("loc_name") String locName, String identifier, ZonedDateTime lastModifiedDate, long lastModifier,
                ZonedDateTime regDate, String title, long author);

}
