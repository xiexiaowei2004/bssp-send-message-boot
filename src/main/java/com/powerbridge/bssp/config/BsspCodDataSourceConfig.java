package com.powerbridge.bssp.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import com.powerbridge.bssp.config.model.BsspCodDBModel;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;
import java.sql.SQLException;
import static com.powerbridge.bssp.Constant.BSSP_COD_MAPPER_SCAN;

/**
 * Created by Administrator on 2017/6/24 0024.
 */
@Configuration
@MapperScan(basePackages = BSSP_COD_MAPPER_SCAN,sqlSessionTemplateRef = "bsspCodSqlSessionTemplate")
public class BsspCodDataSourceConfig extends BeanDataSourceConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource bsspCodDataSource(BsspCodDBModel bsspCodDBModel) throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(environment.getProperty("spring.datasource.bssp-cod.url"));
        mysqlXaDataSource.setPassword(environment.getProperty("spring.datasource.bssp-cod.password"));
        mysqlXaDataSource.setUser(environment.getProperty("spring.datasource.bssp-cod.username"));
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("bsspCodDataSource");
        setBaseDataSourceConfig(xaDataSource);
        return xaDataSource;
    }

    /**
     * 事务管理
     * @return
     */
    /*@Bean
    public DataSourceTransactionManager bsspCodTransactionManager(@Qualifier("bsspCodDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }*/

    @Bean
    public MybatisSqlSessionFactoryBean bsspCodSqlSessionFactoryBean(@Qualifier("bsspCodDataSource") DataSource dataSource) {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = getMybatisSqlSessionFactoryBean(dataSource);
        return mybatisSqlSessionFactoryBean;

    }

    @Bean
    public SqlSessionTemplate bsspCodSqlSessionTemplate(@Qualifier("bsspCodSqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
