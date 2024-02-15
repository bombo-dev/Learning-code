package com.example.stock.stock.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLockRepository implements LockRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcLockRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void getLock(String key, Long lockTime) {
        String sql = "SELECT GET_LOCK(:key, :lockTime)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("key", key)
                .addValue("lockTime", lockTime);

        namedParameterJdbcTemplate.queryForObject(sql, param, Void.class);
    }

    @Override
    public void releaseLock(String key) {
        String sql = "SELECT RELEASE_LOCK(:key)";
        SqlParameterSource param = new MapSqlParameterSource("key", key);

        namedParameterJdbcTemplate.queryForObject(sql, param, Void.class);
    }
}
