package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.utils.SerializationUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class PurchasesConsumersDaoJdbcImpl implements PurchasesConsumersDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchasesConsumersDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_USER_IDS_SQL = "select user_id from PURCHASES_CONSUMERS where purchase_id=?;";

    @Override
    public List<Long> getConsumerUsers(Long purchaseId) {
        return jdbcTemplate.queryForList(SELECT_USER_IDS_SQL, Long.class, purchaseId);
    }

    private static final String DELETE_BY_PURCHASE_ID_SQL = "delete from PURCHASES_CONSUMERS where purchase_id=?;";

    private static final String INSERT_SQL = "insert into PURCHASES_CONSUMERS (purchase_id, user_id) values (?, ?);";

    @Override
    public void setConsumerUsers(List<Long> userIds, Long purchaseId) {
        List<Long> x1 = jdbcTemplate.queryForList(SELECT_USER_IDS_SQL, Long.class, purchaseId);
        jdbcTemplate.update(DELETE_BY_PURCHASE_ID_SQL, ps -> ps.setLong(1, purchaseId));
        List<Long> x2 = jdbcTemplate.queryForList(SELECT_USER_IDS_SQL, Long.class, purchaseId);
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, purchaseId);
                ps.setLong(2, userIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return userIds.size();
            }
        });
        List<Long> x3 = jdbcTemplate.queryForList(SELECT_USER_IDS_SQL, Long.class, purchaseId);
        System.out.println("");
    }
}
