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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserChecksDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_USERS_CHECKS_SQL = "insert into USERS_CHECKS (check_id, user_id) values (?, ?);";

    @Override
    public void linkChecksToUser(List<Long> checkIds, Long userId) {
        jdbcTemplate.batchUpdate(INSERT_USERS_CHECKS_SQL, new BatchPreparedStatementSetter() {
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

    private static final String DELETE_USERS_CHECKS_SQL = "delete from USERS_CHECKS where user_id=?;";

    @Override
    public void unlinkChecksFromUser(Long userId) {
        jdbcTemplate.update(DELETE_USERS_CHECKS_SQL, ps -> ps.setLong(1, userId));
    }
}
