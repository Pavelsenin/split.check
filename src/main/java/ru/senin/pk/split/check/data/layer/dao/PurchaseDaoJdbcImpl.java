package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PurchaseDaoJdbcImpl implements PurchaseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchaseDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PurchaseEntity> getPurchaseById(Long purchaseId) {
        // TODO test join vs several queries performance
        SqlRowSet purchaseRowSet = jdbcTemplate.queryForRowSet("select id, name, cost from PURCHASES where id=?", purchaseId);
        if (!purchaseRowSet.next()) {
            return Optional.empty();
        }
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseRowSet.getLong("id"));
        purchaseEntity.setName(purchaseRowSet.getString("name"));
        purchaseEntity.setCost(purchaseRowSet.getBigDecimal("cost"));

        return Optional.of(purchaseEntity);
    }

    @Override
    public List<PurchaseEntity> getPurchasesByIds(List<Long> purchaseIds) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(purchaseIds).stream()
                .map(this::getPurchaseById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void savePurchase(PurchaseEntity entity) {
        if (Objects.isNull(entity.getId())) {
            createPurchaseJdbc(entity);
        } else {
            updatePurchaseJdbc(entity);
        }
    }

    private static final String CREATE_PURCHASE_SQL = "insert into PURCHASES (name, cost) values (?, ?);";

    private void createPurchaseJdbc(PurchaseEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(CREATE_PURCHASE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setBigDecimal(2, entity.getCost());
            return ps;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    private static final String UPDATE_PURCHASE_SQL = "update PURCHASES set name=?, cost=? where id=?;";

    private void updatePurchaseJdbc(PurchaseEntity entity) {
        jdbcTemplate.update(UPDATE_PURCHASE_SQL, ps -> {
            ps.setString(1, entity.getName());
            ps.setBigDecimal(2, entity.getCost());
            ps.setLong(3, entity.getId());
        });
    }
}
