package service.core.count

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import service.model.meta.ReplaceKey
import service.model.meta.ReplaceKeyRepository
import service.service.core.count.H2CountRepository

class H2CountRepositoryTest {

    private lateinit var countRepository: H2CountRepository
    private lateinit var replaceKeyRepository: ReplaceKeyRepository

    @BeforeEach
    fun setUp() {
        replaceKeyRepository = mock(ReplaceKeyRepository::class.java)
        countRepository = H2CountRepository(replaceKeyRepository)
    }

    @Test
    fun testAddCount_newData() {
        val data = ReplaceKey(title = "Chicken", count = 1)

        `when`(replaceKeyRepository.findByTitle(data.title)).thenReturn(Mono.empty())
        `when`(replaceKeyRepository.save(data)).thenReturn(Mono.just(data))

        val result = countRepository.addCount(data).block()
        assertEquals(data, result)
    }

    @Test
    fun testGetCount() {
        val data = listOf(
            ReplaceKey(title = "Chicken1", count = 1),
            ReplaceKey(title = "Chicken2", count = 3),
            ReplaceKey(title = "Chicken3", count = 2)
        )
        `when`(replaceKeyRepository.findAll()).thenReturn(Flux.fromIterable(data))

        val result = countRepository.getCount().block()

        assertEquals("[Chicken2=3, Chicken3=2, Chicken1=1]", result)
    }
}
