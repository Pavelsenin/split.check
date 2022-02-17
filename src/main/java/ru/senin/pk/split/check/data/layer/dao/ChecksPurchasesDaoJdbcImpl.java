package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class ChecksPurchasesDaoJdbcImpl implements ChecksPurchasesDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChecksPurchasesDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_PURCHASES_IDS_SQL = "SELECT purchase_id FROM checks_purchases WHERE check_id=?;";

    @Override
    public List<Long> getPurchases(Long checkId) {
        return jdbcTemplate.queryForList(GET_PURCHASES_IDS_SQL, Long.class, checkId);
    }

    private static final String DELETE_PURCHASE_IDS_BY_CHECK_ID_SQL = "DELETE FROM checks_purchases WHERE check_id=?";

    private static final String INSERT_SQL = "INSERT INTO checks_purchases (check_id, purchase_id) VALUES (?, ?)";

    @Override
    public void setPurchases(List<Long> purchaseIds, Long checkId) {
        jdbcTemplate.update(DELETE_PURCHASE_IDS_BY_CHECK_ID_SQL, ps -> ps.setLong(1, checkId));
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, checkId);
                ps.setLong(2, purchaseIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return purchaseIds.size();
            }
        });
    }
}
