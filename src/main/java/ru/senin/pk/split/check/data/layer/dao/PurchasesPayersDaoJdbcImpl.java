package ru.senin.pk.split.check.data.layer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PurchasesPayersDaoJdbcImpl implements PurchasesPayersDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchasesPayersDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_USER_ID_BY_PURCHASE_ID_SQL = "select user_id from PURCHASES_PAYERS where purchase_id=?";

    @Override
    public Long getPayerUser(Long purchaseId) {
        return jdbcTemplate.queryForObject(SELECT_USER_ID_BY_PURCHASE_ID_SQL, Long.class, purchaseId);
    }

    private static final String DELETE_BY_PURCHASE_ID_SQL = "delete from PURCHASES_PAYERS where purchase_id=?";

    private static final String INSERT_SQL = "insert into PURCHASES_PAYERS (purchase_id, user_id) values (?, ?)";

    @Override
    public void setPayerUser(Long userId, Long purchaseId) {
        jdbcTemplate.update(DELETE_BY_PURCHASE_ID_SQL, ps -> ps.setLong(1, purchaseId));
        jdbcTemplate.update(INSERT_SQL, ps ->  {
            ps.setLong(1, purchaseId);
            ps.setLong(2, userId);
        });
    }
}
