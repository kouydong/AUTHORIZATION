package co.kr.apti.authorization.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {

//    @Primary
    @Bean(name = "oracle")
    @ConfigurationProperties(prefix = "spring.oracle-datasource-hjin")
    public DataSource oracleDataSource() {

        return DataSourceBuilder.create().type(HikariDataSource.class).build();

    }

}
