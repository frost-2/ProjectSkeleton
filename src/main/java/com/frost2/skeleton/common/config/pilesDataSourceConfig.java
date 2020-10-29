package com.frost2.skeleton.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author 陈伟平
 * @date 2019-09-10 2:50:00
 */
@Configuration
@MapperScan(basePackages = pilesDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "pilesSqlSessionFactory")
public class pilesDataSourceConfig {

    // 精确到 slave 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.frost2.skeleton.mapper.piles";
    static final String MAPPER_LOCATION = "classpath:mapper/piles/*.xml";

    @Value("${piles.datasource.url}")
    private String url;

    @Value("${piles.datasource.username}")
    private String user;

    @Value("${piles.datasource.password}")
    private String password;

    @Value("${piles.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "pilesDataSource")
    public DataSource pilesDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "pilesTransactionManager")
    public DataSourceTransactionManager pilesTransactionManager() {
        return new DataSourceTransactionManager(pilesDataSource());
    }

    @Bean(name = "pilesSqlSessionFactory")
    public SqlSessionFactory pilesSqlSessionFactory(@Qualifier("pilesDataSource") DataSource pilesDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(pilesDataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(pilesDataSourceConfig.MAPPER_LOCATION));
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true); //多数据源设置驼峰命名规则
        return sessionFactory.getObject();
    }
}
