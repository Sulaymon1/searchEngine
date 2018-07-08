//package com.skyforce.config;
//
//import lombok.SneakyThrows;
//import org.apache.commons.dbcp.BasicDataSource;
//import org.postgresql.copy.CopyManager;
//import org.postgresql.core.BaseConnection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.init.DataSourceInitializer;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import javax.sql.DataSource;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.concurrent.Executor;
//
///**
// * Created by Sulaymon on 11.03.2018.
// */
//@ComponentScan(basePackages = "com.skyforce.config")
//@EnableTransactionManagement
//@Configuration
//@PropertySource(value = "classpath:application.properties")
//public class Appconfig {
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driver;
//
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(@Qualifier("datasource") DataSource dataSource){
//        return new JdbcTemplate(dataSource);
//    }
//
//    @Bean
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("datasource") DataSource dataSource){
//        return new NamedParameterJdbcTemplate(dataSource);
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(@Qualifier("datasource") DataSource dataSource){
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "datasource")
//    public DataSource dataSource(){
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//
//    @Bean
//    @SneakyThrows
//    public CopyManager copyManager(){
//           return new CopyManager((BaseConnection) DriverManager
//                    .getConnection(url, username, password));
//    }
//
//    @Bean(name = "dataParserProcess")
//    public Executor asyncExecutor(){
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(2);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("dataParserService-");
//        executor.initialize();
//        return executor;
//    }
//
//}
