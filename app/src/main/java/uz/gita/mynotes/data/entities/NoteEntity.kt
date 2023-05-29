package uz.gita.mynotes.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dataTime: String,
    val subTitle: String = "",
    val noteText: String = "",
    val imagePath: String = "",
    val color: String = "",
    val webLink: String = ""
) : Serializable
