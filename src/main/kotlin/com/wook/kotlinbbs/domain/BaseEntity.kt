package com.wook.kotlinbbs.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected val id: Long? = null,

    @CreatedDate
    protected var createAt: LocalDateTime? = null,

    @LastModifiedDate
    protected var updateAt: LocalDateTime? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false

        if (id != other.id) return false
        if (createAt != other.createAt) return false
        if (updateAt != other.updateAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + createAt.hashCode()
        result = 31 * result + updateAt.hashCode()
        return result
    }
}
