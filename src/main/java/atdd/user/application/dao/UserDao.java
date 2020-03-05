package atdd.user.application.dao;

import atdd.exception.ErrorType;
import atdd.exception.SubwayException;
import atdd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("USER")
                .usingGeneratedKeyColumns("ID");
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", user.getName());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("PASSWORD", user.getPassword());

        Long userId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return findById(userId);
    }

    public User findByEmail(String email) {
        String sql = "select id, email, password, name  " +
                "from USER \n" +
                "WHERE email = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{email});
        return mapUser(result);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from USER where id = ?", id);
    }

    private User findById(Long id) {
        String sql = "select id, email, password, name  " +
                "from USER \n" +
                "WHERE id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapUser(result);
    }

    private User mapUser(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new SubwayException(ErrorType.INTERNAL_SERVER_ERROR);
        }

        return new User((Long) result.get(0).get("ID"),
                (String) result.get(0).get("EMAIL"),
                (String) result.get(0).get("PASSWORD"),
                (String) result.get(0).get("NAME"));
    }
}
