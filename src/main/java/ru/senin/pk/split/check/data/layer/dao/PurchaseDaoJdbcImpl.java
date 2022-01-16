package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PurchaseDaoJdbcImpl implements PurchaseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchaseDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PurchaseEntity> getPurchase(Long id) {
        // TODO test join vs several queries performance
        SqlRowSet purchaseRowSet = jdbcTemplate.queryForRowSet("select id, name, cost from PURCHASES where id=?", id);
        if (!purchaseRowSet.next()) {
            return Optional.empty();
        }
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseRowSet.getLong("id"));
        purchaseEntity.setName(purchaseRowSet.getString("name"));
        purchaseEntity.setCost(purchaseRowSet.getBigDecimal("cost"));

        Long payerId = jdbcTemplate.queryForObject("select user_id from PURCHASES_PAYERS where purchase_id=?", Long.class, id);
        purchaseEntity.setPayerId(payerId);

        List<Long> consumersIds = jdbcTemplate.queryForList("select user_id from PURCHASES_CONSUMERS where purchase_id=?", Long.class, id);
        purchaseEntity.setConsumerIds(consumersIds);

        return Optional.of(purchaseEntity);
    }

    @Override
    public List<PurchaseEntity> getPurchases(List<Long> ids) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(ids).stream()
                .map(this::getPurchase)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
