package com.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.myapp.domain.Customer.class.getName());
            createCache(cm, com.myapp.domain.Customer.class.getName() + ".contracts");
            createCache(cm, com.myapp.domain.Contract.class.getName());
            createCache(cm, com.myapp.domain.Contract.class.getName() + ".scopes");
            createCache(cm, com.myapp.domain.Service.class.getName());
            createCache(cm, com.myapp.domain.Service.class.getName() + ".scopes");
            createCache(cm, com.myapp.domain.Scope.class.getName());
            createCache(cm, com.myapp.domain.Peer.class.getName());
            createCache(cm, com.myapp.domain.Peer.class.getName() + ".meters");
            createCache(cm, com.myapp.domain.MeterImport.class.getName());
            createCache(cm, com.myapp.domain.Meter.class.getName());
            createCache(cm, com.myapp.domain.Meter.class.getName() + ".ownerships");
            createCache(cm, com.myapp.domain.Provider.class.getName());
            createCache(cm, com.myapp.domain.Provider.class.getName() + ".meters");
            createCache(cm, com.myapp.domain.PropertyType.class.getName());
            createCache(cm, com.myapp.domain.PropertyType.class.getName() + ".ownershipProperties");
            createCache(cm, com.myapp.domain.OwnershipProperty.class.getName());
            createCache(cm, com.myapp.domain.Namespace.class.getName());
            createCache(cm, com.myapp.domain.Namespace.class.getName() + ".meters");
            createCache(cm, com.myapp.domain.Ownership.class.getName());
            createCache(cm, com.myapp.domain.Ownership.class.getName() + ".ownershipProperties");
            createCache(cm, com.myapp.domain.Ownership.class.getName() + ".meters");
            createCache(cm, com.myapp.domain.Ownership.class.getName() + ".classifications");
            createCache(cm, com.myapp.domain.WasteTrackingParameters.class.getName());
            createCache(cm, com.myapp.domain.Address.class.getName());
            createCache(cm, com.myapp.domain.Country.class.getName());
            createCache(cm, com.myapp.domain.Owner.class.getName());
            createCache(cm, com.myapp.domain.Owner.class.getName() + ".peers");
            createCache(cm, com.myapp.domain.Owner.class.getName() + ".ownerships");
            createCache(cm, com.myapp.domain.OwnershipClassification.class.getName());
            createCache(cm, com.myapp.domain.OwnershipClassification.class.getName() + ".ownerships");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
