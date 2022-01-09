package ru.senin.pk.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.senin.pk.entities.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJdbcRepository implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity getUser(String id) {
        return jdbcTemplate.queryForObject(
                "select id, name from USERS where id=?",
                this::mapRowToUserEntity, id
        );
    }

    private UserEntity mapRowToUserEntity(ResultSet rs, int rowNum) throws SQLException {
        return new UserEntity(
                1L,
                ""
        );
    }
}
