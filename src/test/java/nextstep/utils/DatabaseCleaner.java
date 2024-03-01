package nextstep.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class DatabaseCleaner implements InitializingBean {

    @Autowired
    private EntityManager em;
    private List<String> tables = new ArrayList<>();

    public void cleanUp() {
        for (String table : tables) {
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
                em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
                em.createNativeQuery("ALTER TABLE " + table + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
            }
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            em.flush();
        }

    @Override
    public void afterPropertiesSet() throws Exception {
        tables = em.getMetamodel().getEntities()
                .stream().map(entityType -> entityType.getJavaType().getAnnotation(Table.class).name())
                .collect(Collectors.toList());
    }
}
