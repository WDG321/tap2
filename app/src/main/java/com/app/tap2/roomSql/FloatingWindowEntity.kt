package com.app.tap2.roomSql

import androidx.room.Entity
import androidx.room.PrimaryKey

// 定义room操作数据库的实体类
@Entity(tableName = "floatingWindow")
class FloatingWindowEntity(
    // @PrimaryKey表示主键, autoGenerate = true表示自动生成
    @PrimaryKey(autoGenerate = true) val id: Int,
    // 默认列明为属性名,可以使用@ColumnInfo(name = "user_name")来定义列名
    var windowPositionX: Int,
    var windowPositionY: Int,
    var width: Int,
    var height: Int,
    var layoutParamsPositionX: Int,
    var layoutParamsPositionY: Int

) {

}