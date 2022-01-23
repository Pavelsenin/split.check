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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ChecksPurchasesDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_CHECKS_PURCHASES_SQL = "insert into CHECKS_PURCHASES (check_id, purchase_id) values (?, ?);";

    @Override
    public void linkPurchasesToCheck(List<Long> purchaseIds, Long checkId) {
        jdbcTemplate.batchUpdate(INSERT_CHECKS_PURCHASES_SQL, new BatchPreparedStatementSetter() {
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

    private static final String DELETE_ALL_CHECKS_PURCHASES_SQL = "delete from CHECKS_PURCHASES where purchase_id=?;";

    @Override
    public void unlinkPurchasesFromCheck(Long checkId) {
        jdbcTemplate.update(DELETE_ALL_CHECKS_PURCHASES_SQL, ps -> ps.setLong(1, checkId));
    }
}
