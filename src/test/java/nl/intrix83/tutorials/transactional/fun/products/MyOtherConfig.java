//package nl.intrix83.tutorials.transactional.fun.products;
//
//import java.time.Duration;
//import java.util.Properties;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.web.client.RestTemplate;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//@ContextConfiguration(classes = MyOtherConfig.MyConfig.class)
//public class MyOtherConfig {
//
//    @MockBean
//    public RestTemplate restTemplate;
//
//    @Configuration
//    public static class MyConfig {
//
//        @Bean
//        public DataSource dataSource(final PostgreSQLContainer postgreSQLContainer) {
//            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//            // Datasource initialization
//            dataSourceBuilder.url(postgreSQLContainer.getJdbcUrl());
//            dataSourceBuilder.username(postgreSQLContainer.getUsername());
//            dataSourceBuilder.password(postgreSQLContainer.getPassword());
//            dataSourceBuilder.driverClassName(postgreSQLContainer.getDriverClassName());
//            // Additional parameters configuration omitted
//            return dataSourceBuilder.build();
//        }
//
//        @Bean
//        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//            //JpaVendorAdapteradapter can be autowired as well if it's configured in application properties.
//            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//            vendorAdapter.setGenerateDdl(false);
//
//            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//            factory.setJpaVendorAdapter(vendorAdapter);
//            //Add package to scan for entities.
//            factory.setPackagesToScan("nl.intrix83.tutorials.transactional");
//            factory.setDataSource(dataSource);
//            return factory;
//        }
//
//        @Bean
//        public PostgreSQLContainer postgreSQLContainer() {
//            final PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:10.4") //
//                    .withDatabaseName("sampledb")
//                    .withUsername("sampleuser")
//                    .withPassword("samplepwd")
//                    .withStartupTimeout(Duration.ofSeconds(600));
//            postgreSQLContainer.start();
//            return postgreSQLContainer;
//        }
//
//        //        @Bean
//        //        public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {
//        //            HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        //            transactionManager.setSessionFactory(sessionFactory.getObject());
//        //            return transactionManager;
//        //        }
//
////        @Bean
////        public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
////            LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
////            sessionFactory.setDataSource(dataSource);
////            sessionFactory.setPackagesToScan("nl.intrix83.tutorials.transactional");
////            sessionFactory.setHibernateProperties(hibernateProperties());
////
////            return sessionFactory;
////        }
//
//        @Bean
//        public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//            JpaTransactionManager txManager = new JpaTransactionManager();
//            txManager.setEntityManagerFactory(entityManagerFactory);
//            return txManager;
//        }
//
//        private Properties hibernateProperties() {
//            Properties hibernateProperties = new Properties();
//            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
//            hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//            return hibernateProperties;
//        }
//    }
//}
