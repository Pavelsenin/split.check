package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserChecksDaoJdbcImpl implements UserChecksDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserChecksDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_USER_IDS_SQL = "SELECT user_id FROM users_checks WHERE check_id=?;";

    @Override
    public List<Long> getUsers(Long checkId) {
        return jdbcTemplate.queryForList(GET_USER_IDS_SQL, Long.class, checkId);
    }

    private static final String GET_USER_CHECKS_SQL = "SELECT check_id FROM users_checks WHERE user_id=?;";

    @Override
    public List<Long> getChecks(Long userId) {
        return jdbcTemplate.queryForList(GET_USER_CHECKS_SQL, Long.class, userId);
    }

    private static final String DELETE_USERS_BY_CHECK_SQL = "delete FROM users_checks WHERE user_id=?;";

    private static final String INSERT_SQL = "INSERT INTO users_checks (check_id, user_id) VALUES (?, ?);";

    @Override
    public void setChecks(List<Long> checkIds, Long userId) {
        jdbcTemplate.update(DELETE_USERS_BY_CHECK_SQL, ps -> ps.setLong(1, userId));
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, checkIds.get(i));
                ps.setLong(2, userId);
            }

            @Override
            public int getBatchSize() {
                return checkIds.size();
            }
        });
    }

    private static final String DELETE_CHECKS_BY_USER_SQL = "DELETE FROM users_checks WHERE check_id=?;";

    @Override
    public void setUsers(List<Long> userIds, Long checkId) {
        jdbcTemplate.update(DELETE_CHECKS_BY_USER_SQL, ps -> ps.setLong(1, checkId));

        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, checkId);
                ps.setLong(2, userIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return userIds.size();
            }
        });
    }
}
