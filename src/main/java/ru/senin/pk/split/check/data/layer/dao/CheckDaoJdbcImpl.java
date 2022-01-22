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
    private static final String GET_USER_IDS_SQL = "select user_id from USERS_CHECKS where check_id=?;";
    private static final String GET_PURCHASES_IDS_SQL = "select purchase_id from CHECKS_PURCHASES where check_id=?;";

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

        List<Long> usersIds = jdbcTemplate.queryForList(GET_USER_IDS_SQL, Long.class, checkId);
        checkEntity.setUserIds(usersIds);

        List<Long> purchasesIds = jdbcTemplate.queryForList(GET_PURCHASES_IDS_SQL, Long.class, checkId);
        checkEntity.setPurchaseIds(purchasesIds);

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
    public void saveCheck(CheckEntity entity) {
        if (Objects.isNull(entity.getId())) {
            createCheck(entity);
        } else {
            updateCheck(entity);
        }
    }

    @Transactional
    void createCheck(CheckEntity entity) {
        // TODO what if create not succeeded
        createChecksJdbc(entity);
        insertUsersChecksJdbc(entity);
    }

    @Transactional
    void updateCheck(CheckEntity entity) {
        updateChecksJdbc(entity);
        deleteUsersChecksJdbc(entity);
        insertUsersChecksJdbc(entity);
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

    private static final String DELETE_USERS_CHECKS_SQL = "delete from USERS_CHECKS where check_id=?;";

    private void deleteUsersChecksJdbc(CheckEntity entity) {
        jdbcTemplate.update(DELETE_USERS_CHECKS_SQL, ps -> ps.setLong(1, entity.getId()));
    }

    private static final String INSERT_USERS_CHECKS_SQL = "insert into USERS_CHECKS (user_id, check_id) values (?, ?);";

    private void insertUsersChecksJdbc(CheckEntity entity) {
        jdbcTemplate.batchUpdate(INSERT_USERS_CHECKS_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, entity.getUserIds().get(i));
                ps.setLong(2, entity.getId());
            }

            @Override
            public int getBatchSize() {
                return entity.getUserIds().size();
            }
        });
    }
}
