package com.tincho5588.reddittopsreader.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tincho5588.reddittopsreader.data.datasource.local.dao.PostDao
import com.tincho5588.reddittopsreader.data.datasource.local.entity.PostRoomEntity

@Database(entities = [PostRoomEntity::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}