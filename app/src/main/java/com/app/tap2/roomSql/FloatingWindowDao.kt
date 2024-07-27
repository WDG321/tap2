package com.app.tap2.roomSql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FloatingWindowDao {
    // @Insert用于定义插入方法
    // @Insert 方法的每个参数都必须是一个带有 @Entity 注解的 Room 数据实体类实例，或是数据实体类实例的集合，
    // 而且每个参数都指向一个数据库。调用 @Insert 方法时，Room 会将每个传递的实体实例插入到相应的数据库表中。
    // 如果 @Insert 方法接收到单个参数，则可返回一个 long 值，这是插入项的新 rowId。
    // 如果参数是数组或集合，则可改为返回由 long 值组成的数组或集合，并且每个值都作为其中一个插入项的 rowId
    // 参数 onConflict 用于告知 Room 在发生冲突时应该执行的操作。OnConflictStrategy.IGNORE 策略会忽略新添加的值。
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // 使用 suspend 关键字标记函数，使其在单独的线程上运行
    // 插入一条数据
    suspend fun insertFloatingWindow(floatingWindowEntity: FloatingWindowEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // 插入多条数据
    suspend fun insertFloatingWindows(floatingWindowEntitys: MutableList<FloatingWindowEntity>):Array<Long>

    // 更新一条数据
    @Update
    suspend fun updateFloatingWindow(floatingWindowEntity: FloatingWindowEntity): Int

    // 更新多条数据
    @Update
    suspend fun updateFloatingWindows(floatingWindowEntitys: MutableList<FloatingWindowEntity>): Int

    // 删除一条数据
    @Delete
    suspend fun deleteFloatingWindow(floatingWindowEntity: FloatingWindowEntity): Int

    // 根据id查询一条数据
    @Query("SELECT * from floatingWindow WHERE id = :id")
    fun getFloatingWindow(id: Int): Flow<FloatingWindowEntity>

    // 查询所有数据
    @Query("SELECT * from floatingWindow")
    // 使用Flow(数据流)接收结果,可以使用数据流从数据库接收实时更新
            /* 如果将返回值更改为集合会导致以下问题：
                 阻塞主线程：返回值更改为集合，查询操作将会立即执行并返回整个结果集合。会导致在主线程上执行数据库查询操作，从而阻塞主线程，影响应用的响应性。
                 实时更新失效：使用Flow作为返回值可以实现实时更新，即当数据库中的数据发生变化时，Flow会自动发出新的数据。如果将返回值更改为集合，将无法实现实时更新，因为集合只会包含查询时的静态数据。
                 性能问题：将查询结果一次性加载到集合中可能会导致内存占用过高，尤其是当数据量较大时。而使用Flow可以在需要时异步获取数据，避免一次性加载大量数据。*/
    fun getAllFloatingWindow(): Flow<List<FloatingWindowEntity>>
}