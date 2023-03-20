package service.model.meta

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("replace_key")
class ReplaceKey(
    @Id
    var id: Long = 0L,
    var title: String,
    var count: Long = 1
)
