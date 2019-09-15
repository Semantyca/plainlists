package com.semantyca.plainlists.dao;

import com.semantyca.plainlists.api.Label;
import com.toonext.IPlainListsObject;
import com.toonext.core.dao.IDAO;
import com.toonext.core.jdbi.BindJson;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface ILabelDAO extends IPlainListsObject, IDAO {
    @SqlQuery("SELECT * FROM " + LABELS_TABLE_FULL_NAME + " as s WHERE s.identifier = :identifier;")
    @RegisterColumnMapper(LabelMapper.class)
    Label findByIdentifier(@Bind("identifier") String identifier);

    @SqlQuery("SELECT * FROM " + LABELS_TABLE_FULL_NAME + " as s  LIMIT :limit OFFSET :offset;")
    @RegisterColumnMapper(LabelMapper.class)
    List<Label> findAll(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("SELECT count(s.id) FROM " + LABELS_TABLE_FULL_NAME + " as s")
    long getCountOfAll();

    @SqlUpdate(
            "CREATE TABLE " + LABELS_TABLE_FULL_NAME +
            "(" +
            "  color character varying(64)," +
            "  category character varying(128)," +
            "  hidden boolean," +
                    IDAO.MANDATORY_REF_DDL_PIECE +
            ");")
    void createTable();


    @SqlUpdate("INSERT INTO " + LABELS_TABLE_FULL_NAME + " (source, name, path, last_mod_date, last_mod_user, reg_date, title, author)" +
            " VALUES (:getSourceId, :getName, :getPath, :getLastModifiedDate, :getLastModifier, :getRegDate, :getTitle, :getAuthor)")
    @GetGeneratedKeys("id")
    UUID insert(Label label);

    @SqlUpdate("INSERT INTO " + LABELS_TABLE_FULL_NAME + "(color, category, hidden, loc_name, identifier, last_mod_date, last_mod_user, reg_date, title, author)VALUES (?,?,?,?::jsonb,?,?,?,?,?,?);")
    @GetGeneratedKeys("id")
    UUID insert(String color, String category, boolean isHidden, @BindJson("loc_name") String locName, String identifier, ZonedDateTime lastModifiedDate, long lastModifier,
                ZonedDateTime regDate, String title, long author);




}
