package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.UserAuthEntity;
import ru.senin.pk.split.check.errors.DataBaseException;

@Component
public class UserAuthDaoImpl implements UserAuthDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserAuthDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_USERS_AUTH_BY_USERNAME_SQL = "SELECT username, password, user_id FROM users_auth WHERE username=?;";

    @Override
    public UserAuthEntity getUserAuthByUsername(String username) {
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet(GET_USERS_AUTH_BY_USERNAME_SQL, username);
        if (!usersRowSet.next()) {
            return null;
        }
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername(usersRowSet.getString("username"));
        userAuthEntity.setPassword(usersRowSet.getString("password"));
        userAuthEntity.setUserId(usersRowSet.getLong("user_id"));
        return userAuthEntity;
    }

    @Override
    public void saveUserAuth(UserAuthEntity entity) {
        mergeUserAuthJdbc(entity);
    }

    private static final String MERGE_USER_AUTH_SQL_ =
            "MERGE INTO users_auth source " +
            "USING (SELECT * FROM (VALUES(?, ?, ?)) AS t(username, password, user_id)) target " +
            "ON (source.username = target.username) " +
            "WHEN MATCHED THEN " +
            "UPDATE SET password = target.password " +
            "WHEN NOT MATCHED THEN " +
            "INSERT (username, password, user_id) " +
            "VALUES (target.username, target.password, target.user_id);";

    private void mergeUserAuthJdbc(UserAuthEntity entity) {
        int res = jdbcTemplate.update(MERGE_USER_AUTH_SQL_, ps -> {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setLong(3, entity.getUserId());
        });
        if (res == 0) {
            throw new DataBaseException("Create user auth error. entity: " + entity);
        }
    }
}
