package service.core.count

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import service.model.meta.ReplaceKey
import service.service.core.count.InMemoryCountRepository

class InMemoryCountRepositoryTest {

    private val inMemoryCountRepository = InMemoryCountRepository()

    @Test
    fun testAddCount() {
        val existingData = ReplaceKey(title = "Chicken1")
        val result = inMemoryCountRepository.addCount(existingData).block()
        assertEquals(ReplaceKey(title = "Chicken1", count = 2).count, result.count)
    }

    @Test
    fun testGetCount() {
        val data1 = ReplaceKey(title = "Chicken1")
        val data2 = ReplaceKey(title = "Chicken2")
        val data3 = ReplaceKey(title = "Chicken3")
        inMemoryCountRepository.addCount(data1).block()
        inMemoryCountRepository.addCount(data2).block()
        inMemoryCountRepository.addCount(data2).block()
        inMemoryCountRepository.addCount(data2).block()
        inMemoryCountRepository.addCount(data3).block()
        inMemoryCountRepository.addCount(data3).block()

        val result = inMemoryCountRepository.getCount().block()

        assertEquals("{Chicken2=3, Chicken3=2, Chicken1=1}", result)
    }
}
