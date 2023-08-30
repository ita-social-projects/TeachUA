package com.softserve.teachua.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayForDockerConfig {
    private final DataSource dataSource;
    @Value("${flyway.for.docker.enabled:false}")
    private boolean isEnabled;

    @EventListener(ContextRefreshedEvent.class)
    public void migrateFlywayAfterHibernate() {
        if (isEnabled) {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:/db/docker")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
        }
    }
}
