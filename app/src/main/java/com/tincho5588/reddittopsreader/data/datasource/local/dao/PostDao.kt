package com.tincho5588.reddittopsreader.data.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tincho5588.reddittopsreader.data.datasource.local.entity.PostRoomEntity
import com.tincho5588.reddittopsreader.domain.model.post.Post

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(posts: List<PostRoomEntity>)

    @Query("SELECT * FROM PostRoomEntity WHERE NOT dismissed ORDER BY ups DESC LIMIT :amount")
    fun loadNotDismissed(amount: Int): LiveData<List<PostRoomEntity>>

    @Query("SELECT * FROM PostRoomEntity WHERE id = :id")
    suspend fun loadPost(id: String): PostRoomEntity

    @Query("UPDATE PostRoomEntity SET dismissed = 1 WHERE id = :id")
    suspend fun dismiss(id: String)

    @Query("UPDATE PostRoomEntity SET seen = 1 WHERE id = :id")
    suspend fun markAsSeen(id: String)
}