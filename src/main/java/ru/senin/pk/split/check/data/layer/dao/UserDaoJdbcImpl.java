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
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDaoJdbcImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserEntity> getUserById(Long userId) {
        // TODO test join vs several queries performance
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet("select id, name from USERS where id=?", userId);
        if (!usersRowSet.next()) {
            return Optional.empty();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(usersRowSet.getLong("id"));
        userEntity.setName(usersRowSet.getString("name"));

        List<Long> checkIds = jdbcTemplate.queryForList("select check_id from USERS_CHECKS where user_id=?", Long.class, userId);
        userEntity.setChecksIds(checkIds);

        return Optional.of(userEntity);
    }

    @Override
    public List<UserEntity> getUsersByIds(List<Long> userIds) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(userIds).stream()
                .map(this::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> getUsersByCheckId(Long checkId) {
        List<Long> userIds = getCheckUserIdsByCheckIdJdbc(checkId);
        return getUsersByIds(userIds);
    }

    @Override
    public UserEntity getPayerByPurchaseId(Long purchaseId) {
        Long userId = getPurchasePayerIdByPurchaseIdJdbc(purchaseId);
        return getUserById(userId).get();
    }

    @Override
    public List<UserEntity> getConsumersByPurchaseId(Long purchaseId) {
        List<Long> userIds = getPurchaseConsumersIdByPurchaseIdJdbc(purchaseId);
        return getUsersByIds(userIds);
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

    private static final String GET_CHECK_USERS_SQL = "select user_id from USERS_CHECKS where check_id=?;";

    private List<Long> getCheckUserIdsByCheckIdJdbc(Long checkId) {
        return jdbcTemplate.queryForList(GET_CHECK_USERS_SQL, Long.class, checkId);
    }

    private static final String GET_PURCHASE_PAYER_SQL = "select user_id from PURCHASES_PAYERS where purchase_id=?;";

    private Long getPurchasePayerIdByPurchaseIdJdbc(Long purchaseId) {
        return jdbcTemplate.queryForObject(GET_PURCHASE_PAYER_SQL, Long.class, purchaseId);
    }

    private static final String GET_PURCHASE_CONSUMERS_SQL = "select user_id from PURCHASES_CONSUMERS where purchase_id=?;";

    private List<Long> getPurchaseConsumersIdByPurchaseIdJdbc(Long purchaseId) {
        return jdbcTemplate.queryForList(GET_PURCHASE_CONSUMERS_SQL, Long.class, purchaseId);
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
