package com.app.tap2.roomSql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.tap2.utils.convertLayoutParamsToWindowPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [FloatingWindowEntity::class], version = 1, exportSchema = false)
abstract class FloatingWindowRoomDatabase : RoomDatabase() {

    abstract fun floatingWindowDao(): FloatingWindowDao

    companion object {
        // @Volatile 注解。volatile 变量的值绝不会缓存，所有读写操作都将在主内存中完成。这有助于确保 INSTANCE 的值始终是最新的值，并且对所有执行线程都相同。也就是说，一个线程对 INSTANCE 所做的更改会立即对所有其他线程可见。
        @Volatile
        // 在伴生对象里创建数据库连接的变量,这样就不会多次创建数据库连接,因为开销极大
        private var INSTANCE: FloatingWindowRoomDatabase? = null

        // 获取数据库连接,确保在多线程环境下只有一个数据库实例被创建，并且在数据库实例已经存在的情况下直接返回现有实例，避免重复创建数据库实例。
        fun getDatabase(context: Context): FloatingWindowRoomDatabase {
            // 如果INSTANCE不为空，则直接返回INSTANCE，否则执行synchronized块
            // synchronized(this)用于确保在多线程环境下只有一个线程可以执行Room数据库的创建操作，避免多个线程同时创建数据库实例。这样可以保证数据库实例的唯一性，并避免潜在的并发问题
            return INSTANCE ?: synchronized(this) {
                // 创建Room数据库实例
                val instance = Room.databaseBuilder(
                    context.applicationContext, // 使用应用程序上下文
                    FloatingWindowRoomDatabase::class.java, // 数据库类
                    "floatingWindow_database" // 数据库名称
                )
                    // 添加数据库创建时调用的函数
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // 在数据库创建时执行初始化操作，例如插入初始数据
                            val dao = getDatabase(context).floatingWindowDao()
                            // 需要初始添加的悬浮窗的集合
                            val floatingWindows = mutableListOf<FloatingWindowEntity>()
                            // 在协程中调用挂起函数
                            CoroutineScope(Dispatchers.Main).launch {
                                floatingWindows.add(
                                    FloatingWindowEntity(
                                        // id为1代表开始点击的按钮,需要特殊处理
                                        id = 1,
                                        windowPositionX = 0,
                                        windowPositionY = 0,
                                        width = 300,
                                        height = 100,
                                        layoutParamsPositionX = 300,
                                        layoutParamsPositionY = 300
                                    )
                                )
                                val windowPosition = convertLayoutParamsToWindowPosition(
                                    context,
                                    0,
                                    0
                                )
                                floatingWindows.add(
                                    FloatingWindowEntity(
                                        // id传入0代表自动生成id
                                        id = 0,
                                        windowPositionX = windowPosition[0],
                                        windowPositionY = windowPosition[1],
                                        width = 100,
                                        height = 100,
                                        layoutParamsPositionX = 0,
                                        layoutParamsPositionY = 0
                                    )
                                )
                                /* for (i in 0 until 50) {
                                     loatingWindows.add(
                                         FloatingWindowEntity(
                                             id = 0,
                                             windowPositionX = 0,
                                             windowPositionY = 0,
                                             width = 100,
                                             height = 100,
                                             layoutParamsPositionX = i,
                                             layoutParamsPositionY = i
                                         )
                                     )
                                 }*/
                                // 添加进数据库
                                dao.insertFloatingWindows(floatingWindows)
                            }
                        }
                    }).fallbackToDestructiveMigration() // 数据库升级时采用破坏性迁移
                    .build() // 构建数据库实例
                INSTANCE = instance // 将新创建的数据库实例赋值给INSTANCE
                return instance // 返回数据库实例
            }
        }
    }
}

