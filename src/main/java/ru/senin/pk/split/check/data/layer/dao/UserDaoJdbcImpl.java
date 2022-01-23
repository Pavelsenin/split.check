package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserDaoJdbcImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity getUserById(Long userId) {
        // TODO test join vs several queries performance
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet("select id, name from USERS where id=?", userId);
        if (!usersRowSet.next()) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(usersRowSet.getLong("id"));
        userEntity.setName(usersRowSet.getString("name"));

        return userEntity;
    }

    @Override
    public List<UserEntity> getUsersByIds(List<Long> userIds) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(userIds).stream()
                .map(this::getUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveUser(UserEntity entity) {
        if (Objects.isNull(entity.getId())) {
            createUserJdbc(entity);
        } else {
            updateUserJdbc(entity);
        }
    }

    private static final String CREATE_USER_SQL = "insert into USERS (name) values (?);";

    private void createUserJdbc(UserEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    private static final String UPDATE_USER_SQL = "update USERS set name=? where id=?;";

    private void updateUserJdbc(UserEntity entity) {
        jdbcTemplate.update(UPDATE_USER_SQL, ps -> {
            ps.setString(1, entity.getName());
            ps.setLong(2, entity.getId());
        });
    }
}
