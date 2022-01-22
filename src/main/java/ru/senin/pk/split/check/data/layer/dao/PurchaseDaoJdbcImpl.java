package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.PurchaseEntity;
import ru.senin.pk.split.check.data.layer.entities.UserEntity;

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

        Long payerId = jdbcTemplate.queryForObject("select user_id from PURCHASES_PAYERS where purchase_id=?", Long.class, purchaseId);
        purchaseEntity.setPayerId(payerId);

        List<Long> consumersIds = jdbcTemplate.queryForList("select user_id from PURCHASES_CONSUMERS where purchase_id=?", Long.class, purchaseId);
        purchaseEntity.setConsumerIds(consumersIds);

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

    @Override
    public List<PurchaseEntity> getPurchasesByCheckId(Long checkId) {
        List<Long> purchaseIds = getCheckPurchaseIdsByCheckIdJdbc(checkId);
        return getPurchasesByIds(purchaseIds);
    }

    private static final String GET_CHECK_USERS_SQL = "select purchase_id from CHECKS_PURCHASES where check_id=?;";

    private List<Long> getCheckPurchaseIdsByCheckIdJdbc(Long checkId) {
        return jdbcTemplate.queryForList(GET_CHECK_USERS_SQL, Long.class, checkId);
    }
}
