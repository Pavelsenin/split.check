package ru.senin.pk.split.check.data.layer.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.senin.pk.split.check.data.layer.entities.CheckEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CheckDaoJdbcImpl implements CheckDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<CheckEntity> getCheck(Long id) {
        // TODO test join vs several queries performance
        SqlRowSet checksRowSet = jdbcTemplate.queryForRowSet("select id, name, date from CHECKS where id=?", id);
        if (!checksRowSet.next()) {
            return Optional.empty();
        }
        CheckEntity checkEntity = new CheckEntity();
        checkEntity.setId(checksRowSet.getLong("id"));
        checkEntity.setName(checksRowSet.getString("name"));
        checkEntity.setDate(checksRowSet.getDate("date"));

        List<Long> usersIds = jdbcTemplate.queryForList("select user_id from USERS_CHECKS where check_id=?", Long.class, id);
        checkEntity.setUserIds(usersIds);

        List<Long> purchasesIds = jdbcTemplate.queryForList("select purchase_id from CHECKS_PURCHASES where check_id=?", Long.class, id);
        checkEntity.setPurchaseIds(purchasesIds);

        return Optional.of(checkEntity);
    }

    @Override
    public List<CheckEntity> getChecks(List<Long> ids) {
        //TODO optimize db interaction
        return CollectionUtils.emptyIfNull(ids).stream()
                .map(this::getCheck)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
