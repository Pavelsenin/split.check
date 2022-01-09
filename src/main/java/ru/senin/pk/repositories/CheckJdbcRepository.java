package ru.senin.pk.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.senin.pk.entities.CheckEntity;
import ru.senin.pk.entities.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CheckJdbcRepository implements CheckRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CheckEntity> getChecksByUserId(String userId) {
        return null;
    }

    @Override
    public CheckEntity saveCheck(CheckEntity checkEntity) {
        return null;
    }

    private CheckEntity mapRowToCheckEntity(ResultSet rs, int rowNum) throws SQLException {
        return new CheckEntity(
                1L,
                ""
        );
    }
}
