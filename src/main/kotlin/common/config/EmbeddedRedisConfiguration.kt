package service.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import redis.embedded.RedisServer

@Configuration
class EmbeddedRedisConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun redisServer(): RedisServer {
        return RedisServer.builder()
            .port(6379)
            .build()
    }

    @Bean
    fun lettuceConnectionFactory(redisServer: RedisServer): LettuceConnectionFactory {
        val config = LettuceConnectionFactory()
        return config
    }

    @Bean
    fun reactiveRedisTemplate(lettuceConnectionFactory: LettuceConnectionFactory): ReactiveRedisTemplate<String, Int> {
        val serializer = StringRedisSerializer()
        val valueSerializer = GenericToStringSerializer<Int>(Int::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, Int>(serializer)
        val context = builder.value(valueSerializer).build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, context)
    }
}
