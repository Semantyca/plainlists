package com.semantyca.plainlists.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.semantyca.plainlists.api.Label;
import com.toonext.localization.constants.LanguageCode;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LabelMapper implements ColumnMapper<Label> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Label map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        Label label = new Label();
        label.setId(rs.getObject("id", UUID.class));
        label.setLastModifiedDate(getDateTime(rs.getTimestamp("last_mod_date")));
        label.setLastModifier(rs.getLong("last_mod_user"));
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(Map.class, LanguageCode.class, String.class);
            PGobject po = rs.getObject("loc_name", PGobject.class);
            label.setLocName(mapper.readValue(po.getValue(), mapType));
        } catch (Exception e) {
            label.setLocName(new HashMap());
        }
        label.setIdentifier(rs.getString("identifier"));
        label.setRegDate(getDateTime(rs.getTimestamp("reg_date")));
        label.setTitle(rs.getString("title"));
        label.setAuthor(rs.getLong("author"));
        label.setColor(rs.getString("color"));
        label.setCategory(rs.getString("category"));
       return label;
    }

    @Override
    public Label map(ResultSet r, String columnLabel, StatementContext ctx) throws SQLException {
        return null;
    }

    private static ZonedDateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp.getTime()), ZoneOffset.UTC) : null;
    }

}
