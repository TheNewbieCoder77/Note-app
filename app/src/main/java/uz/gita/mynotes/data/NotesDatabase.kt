package uz.gita.mynotes.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.mynotes.app.App
import uz.gita.mynotes.data.daos.NoteDao
import uz.gita.mynotes.data.entities.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NotesDatabase : RoomDatabase(){
    abstract fun getNoteDao(): NoteDao

    companion object{
        private lateinit var instance: NotesDatabase

        fun getInstance(): NotesDatabase{
            if(!::instance.isInitialized){
                instance = Room.databaseBuilder(App.context,NotesDatabase::class.java,"Notes")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}