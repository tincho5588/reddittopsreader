package com.tincho5588.reddittopsreader.data.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tincho5588.reddittopsreader.data.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE)
    fun insert(posts: List<Post>)

    @Query("SELECT * FROM Post WHERE NOT dismissed ORDER BY ups DESC")
    fun load(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM Post WHERE seen OR dismissed ORDER BY ups DESC")
    fun loadSeenAndDismissedPosts(): List<Post>

    @Query("UPDATE Post SET dismissed = 1 WHERE id = :id")
    fun dismiss(id: String)

    @Query("UPDATE Post SET seen = 1 WHERE id = :id")
    fun markAsSeen(id: String)
}