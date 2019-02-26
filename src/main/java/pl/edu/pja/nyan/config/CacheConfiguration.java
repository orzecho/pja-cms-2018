package pl.edu.pja.nyan.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(pl.edu.pja.nyan.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Lesson.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Lesson.class.getName() + ".lessonFiles", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Lesson.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Word.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Word.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Tag.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Tag.class.getName() + ".lessons", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Tag.class.getName() + ".words", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.LessonFile.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Tag.class.getName() + ".fillingGapsTestItems", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.FillingGapsTestItem.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.FillingGapsTestItem.class.getName() + ".gapItems", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.FillingGapsTestItem.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.GapItem.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Word.class.getName() + ".exams", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Exam.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Exam.class.getName() + ".results", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.Exam.class.getName() + ".words", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.ExamResult.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.ExamResult.class.getName() + ".writtenAnswers", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.ExamResult.class.getName() + ".trueFalseAnswers", jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.WrittenAnswer.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.TrueFalseAnswer.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.ProposedWord.class.getName(), jcacheConfiguration);
            cm.createCache(pl.edu.pja.nyan.domain.ProposedWord.class.getName() + ".tags", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
