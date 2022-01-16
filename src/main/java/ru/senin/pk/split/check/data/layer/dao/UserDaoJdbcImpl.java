package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Component
public class UserDaoJdbcImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserEntity> getUser(Long id) {
        // TODO test join vs several queries performance
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet("select id, name from USERS where id=?", id);
        if (!usersRowSet.next()) {
            return Optional.empty();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(usersRowSet.getLong("id"));
        userEntity.setName(usersRowSet.getString("name"));

        List<Long> checkIds = jdbcTemplate.queryForList("select check_id from USERS_CHECKS where user_id=?", Long.class, id);
        userEntity.setChecksIds(checkIds);

        return Optional.of(userEntity);
    }
}
