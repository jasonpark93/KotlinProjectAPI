package service.core.count

import io.lettuce.core.RedisClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import service.model.meta.ReplaceKey
import service.service.core.count.RedisCountRepository
import kotlin.test.assertEquals

@SpringBootTest
class RedisCountRepositoryTest {
    @Autowired
    lateinit var redisCountRepository: RedisCountRepository

    @AfterEach
    fun afterEach() {
        val client = RedisClient.create("redis://localhost")
        client.connect().sync().flushall()
    }

    @Test
    fun testAddCount() {
        val data = ReplaceKey(title = "test_title")
        redisCountRepository.addCount(data).block()

        val count = redisCountRepository.getCount().block()
        assertEquals("[test_title=1]", count)
    }

    @Test
    fun `test getCount`() {
        redisCountRepository.addCount(ReplaceKey(title = "title1")).block()
        redisCountRepository.addCount(ReplaceKey(title = "title2")).block()
        redisCountRepository.addCount(ReplaceKey(title = "title2")).block()
        redisCountRepository.addCount(ReplaceKey(title = "title3")).block()
        redisCountRepository.addCount(ReplaceKey(title = "title3")).block()
        redisCountRepository.addCount(ReplaceKey(title = "title3")).block()

        val count = redisCountRepository.getCount().block()
        assertEquals("[title3=3, title2=2, title1=1]", count)
    }
}
