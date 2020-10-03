package com.tincho5588.reddittopsreader.data.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tincho5588.reddittopsreader.domain.model.post.Post

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(posts: List<Post>)

    @Query("SELECT * FROM Post WHERE NOT dismissed ORDER BY ups DESC LIMIT :amount")
    fun loadNotDismissed(amount: Int): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE id = :id")
    suspend fun loadPost(id: String): Post

    @Query("UPDATE Post SET dismissed = 1 WHERE id = :id")
    suspend fun dismiss(id: String)

    @Query("UPDATE Post SET seen = 1 WHERE id = :id")
    suspend fun markAsSeen(id: String)
}