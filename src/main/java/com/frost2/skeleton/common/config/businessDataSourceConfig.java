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
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = businessDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "businessSqlSessionFactory")
public class businessDataSourceConfig {

    // 精确到 slave 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.frost2.skeleton.mapper.business";
    static final String MAPPER_LOCATION = "classpath:mapper/business/*.xml";

    @Value("${business.datasource.url}")
    private String url;

    @Value("${business.datasource.username}")
    private String user;

    @Value("${business.datasource.password}")
    private String password;

    @Value("${business.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "businessDataSource")
    public DataSource businessDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "businessTransactionManager")
    public DataSourceTransactionManager businessTransactionManager() {
        return new DataSourceTransactionManager(businessDataSource());
    }

    @Bean(name = "businessSqlSessionFactory")
    public SqlSessionFactory businessSqlSessionFactory(@Qualifier("businessDataSource") DataSource businessDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(businessDataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(businessDataSourceConfig.MAPPER_LOCATION));
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true); //多数据源设置驼峰命名规则
        return sessionFactory.getObject();
    }
}
