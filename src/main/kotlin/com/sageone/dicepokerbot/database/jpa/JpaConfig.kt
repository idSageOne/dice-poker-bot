package com.sageone.dicepokerbot.database.jpa

import com.sageone.dicepokerbot.BetaTester
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
import javax.persistence.EntityManagerFactory

@Configuration
@EnableJpaRepositories("com.sageone.dicepokerbot.database.jpa.repositories")
@PropertySource("classpath:application.yml")
class JpaConfig {

    @Autowired
    private val env: Environment? = null

    @Bean
    @ConfigurationProperties(prefix = "spring.postgres")
    fun hikariConfig() = HikariConfig()

    @Bean
    fun dataSource() = HikariDataSource(hikariConfig())

    @Bean
    @ConfigurationProperties(prefix = "beta-tester")
    fun betaTesterConfig() = BetaTester()

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()
        em.setPackagesToScan("com.sageone.dicepokerbot.database.jpa.entity")
        em.jpaVendorAdapter = HibernateJpaVendorAdapter()
        em.setJpaProperties(additionalProperties())
        return em
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory?): JpaTransactionManager? {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        return transactionManager
    }

    fun additionalProperties(): Properties {
        val hibernateProperties = Properties()
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env!!.getProperty("spring.jpa.hibernate.ddl-auto"))
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.database-platform"))
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"))
        return hibernateProperties
    }
}