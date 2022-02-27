package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.FriendsRequestEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FriendsRequestsDaoImpl implements FriendsRequestsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsRequestsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_FRIENDS_REQUESTS_BY_USER_ID_SQL =
            "SELECT source_user_id, target_user_id, status " +
            "FROM friends_requests " +
            "WHERE ? in (source_user_id, target_user_id);";

    @Override
    public List<FriendsRequestEntity> getFriendsRequests(Long userId) {
        SqlRowSet friendsRequestRowSet = jdbcTemplate.queryForRowSet(GET_FRIENDS_REQUESTS_BY_USER_ID_SQL, userId);
        List<FriendsRequestEntity> friendRequestEntities = new ArrayList<>();
        while (friendsRequestRowSet.next()) {
            friendRequestEntities.add(new FriendsRequestEntity(
                    friendsRequestRowSet.getLong("source_user_id"),
                    friendsRequestRowSet.getLong("target_user_id"),
                    FriendsRequestEntity.Status.fromCode(friendsRequestRowSet.getString("status"))
            ));
        }
        return friendRequestEntities;
    }

    private static final String DELETE_FRIENDS_REQUESTS_SQL =
            "DELETE FROM friends_requests " +
            "WHERE ? in (source_user_id, target_user_id);";

    private static final String INSERT_FRIENDS_REQUESTS_SQL =
            "INSERT INTO friends_requests (source_user_id, target_user_id, status) " +
            "VALUES (?, ?, ?);";

    @Override
    public void saveFriendsRequests(Long userId, List<FriendsRequestEntity> friendsRequestEntities) {
        jdbcTemplate.update(DELETE_FRIENDS_REQUESTS_SQL, ps -> ps.setLong(1, userId));
        jdbcTemplate.batchUpdate(INSERT_FRIENDS_REQUESTS_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, friendsRequestEntities.get(i).getSourceUserId());
                ps.setLong(2, friendsRequestEntities.get(i).getTargetUserId());
                ps.setString(3, friendsRequestEntities.get(i).getStatus().getCode());
            }

            @Override
            public int getBatchSize() {
                return friendsRequestEntities.size();
            }
        });
    }
}
