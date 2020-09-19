package com.tincho5588.reddittopsreader.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tincho5588.reddittopsreader.data.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE)
    fun insert(posts: List<Post>)

    @Query("SELECT * FROM Post WHERE NOT dismissed ORDER BY ups DESC LIMIT 50 ")
    fun load(): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE seen ORDER BY ups DESC")
    fun loadSeenPosts(): List<Post>

    @Query("UPDATE Post SET dismissed = 1 WHERE id = :id")
    fun dismiss(id: String)

    @Query("UPDATE Post SET seen = 1 WHERE id = :id")
    fun markAsSeen(id: String)
}