package com.mi.hundsun.oxchains.base.common.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * Created by Donfy on 2017/5/25.
 */
//@Configuration
public class MybatisConfig
        implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;

    //     创建事务管理器
    @Bean(name = "txManager")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
//    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setBasePackage("com.mi.hundsun.oxchains.base.core.master.mapper");
//        Properties p = new Properties();
//        p.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
//        configurer.setProperties(p);
//        return configurer;
//    }
}
