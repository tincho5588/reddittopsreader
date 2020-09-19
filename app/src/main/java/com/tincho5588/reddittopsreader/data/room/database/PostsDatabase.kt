package com.tincho5588.reddittopsreader.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.room.dao.PostDao

@Database(entities = [Post::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}