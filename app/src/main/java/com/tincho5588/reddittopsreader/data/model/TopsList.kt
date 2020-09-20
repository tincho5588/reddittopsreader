package com.tincho5588.reddittopsreader.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

data class TopsList(
    val kind: String,
    val data: Data
)

data class Data(
    val modhash: String,
    val dist: Int,
    val children: List<Children>,
    val after: String,
    val before: String
)

data class Children(
    val kind: String,
    val data: Post
)

@Entity
data class Post(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnail: String,
    val author: String,
    val num_comments: Int,
    val created_utc: Long,
    val ups: Int,
    val subreddit_name_prefixed: String,
    val url: String,
    val name: String,
    var seen: Boolean = false,
    var dismissed: Boolean = false
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Post> = object : Parcelable.Creator<Post> {
            override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)

            override fun createFromParcel(source: Parcel): Post = Post(source)
        }
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readInt(),
        source.readLong(),
        source.readInt(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readInt() == 1,
        source.readInt() == 1
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(id)
            dest.writeString(title)
            dest.writeString(thumbnail)
            dest.writeInt(num_comments)
            dest.writeLong(created_utc)
            dest.writeInt(ups)
            dest.writeString(subreddit_name_prefixed)
            dest.writeString(url)
            dest.writeString(name)
            dest.writeInt(if (seen) 1 else 0)
            dest.writeInt(if (dismissed) 1 else 0)
        }
    }
}