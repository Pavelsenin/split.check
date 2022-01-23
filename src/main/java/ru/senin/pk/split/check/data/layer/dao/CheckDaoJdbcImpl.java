package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_CHECKS_SQL = "select id, name, date from CHECKS where id=?;";

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
        checkEntity.setUserIds(getUserIdsByCheckIdJdbc(checkId));
        checkEntity.setPurchaseIds(getPurchasesIdsByCheckIdJdbc(checkId));
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
    public List<CheckEntity> getChecksByUserId(Long userId) {
        List<Long> checkIds = getUserCheckIdsByUserIdJdbc(userId);
        return getChecksByIds(checkIds);
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

    private static final String CREATE_CHECK_SQL = "insert into CHECKS (name, date) values (?, ?);";

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

    private static final String GET_USER_IDS_SQL = "select user_id from USERS_CHECKS where check_id=?;";

    private List<Long> getUserIdsByCheckIdJdbc(Long checkId) {
        return jdbcTemplate.queryForList(GET_USER_IDS_SQL, Long.class, checkId);
    }

    private static final String GET_PURCHASES_IDS_SQL = "select purchase_id from CHECKS_PURCHASES where check_id=?;";

    private List<Long> getPurchasesIdsByCheckIdJdbc(Long checkId) {
        return jdbcTemplate.queryForList(GET_PURCHASES_IDS_SQL, Long.class, checkId);
    }

    private static final String UPDATE_CHECKS_SQL = "update CHECKS set name=?, date=? where id=?;";

    private void updateChecksJdbc(CheckEntity entity) {
        jdbcTemplate.update(UPDATE_CHECKS_SQL, ps -> {
            ps.setString(1, entity.getName());
            ps.setDate(2, new Date(entity.getDate().getTime()));
            ps.setLong(3, entity.getId());
        });
    }

    private static final String GET_USER_CHECKS_SQL = "select check_id from USERS_CHECKS where user_id=?;";

    private List<Long> getUserCheckIdsByUserIdJdbc(Long userId) {
        return jdbcTemplate.queryForList(GET_USER_CHECKS_SQL, Long.class, userId);
    }
}
