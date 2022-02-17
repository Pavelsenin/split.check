package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CheckDaoJdbcImpl implements CheckDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_CHECKS_SQL = "SELECT id, name, date FROM checks WHERE id=?;";

    @Override
    public Optional<CheckEntity> getCheckById(Long checkId) {
        // TODO test join vs several queries performance
        SqlRowSet checksRowSet = jdbcTemplate.queryForRowSet(GET_CHECKS_SQL, checkId);
        if (!checksRowSet.next()) {
            return Optional.empty();
        }
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setId(checksRowSet.getLong("id"));
        checkEntity.setName(checksRowSet.getString("name"));
        checkEntity.setDate(checksRowSet.getDate("date"));
        return Optional.of(checkEntity);
    }

    @Override
    public List<CheckEntity> getChecksByIds(List<Long> checkIds) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(checkIds).stream()
                .map(this::getCheckById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveCheck(CheckEntity entity) {
        if (Objects.isNull(entity.getId())) {
            createChecksJdbc(entity);
        } else {
            updateChecksJdbc(entity);
        }
    }

    private static final String CREATE_CHECK_SQL = "INSERT INTO checks (name, date) VALUES (?, ?);";

    private void createChecksJdbc(CheckEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(CREATE_CHECK_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setDate(2, new Date(entity.getDate().getTime()));
            return ps;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    private static final String UPDATE_CHECKS_SQL = "UPDATE checks SET name=?, date=? WHERE id=?;";

    private void updateChecksJdbc(CheckEntity entity) {
        jdbcTemplate.update(UPDATE_CHECKS_SQL, ps -> {
            ps.setString(1, entity.getName());
            ps.setDate(2, new Date(entity.getDate().getTime()));
            ps.setLong(3, entity.getId());
        });
    }
}
