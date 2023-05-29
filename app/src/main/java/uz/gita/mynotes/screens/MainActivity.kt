package uz.gita.mynotes.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import uz.gita.mynotes.R
import uz.gita.mynotes.adapter.NotesListAdapter
import uz.gita.mynotes.data.NotesDatabase
import uz.gita.mynotes.data.entities.NoteEntity

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddNote: ImageView
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var inputSearch: EditText
    private val notesListAdapter = NotesListAdapter()
    private val noteDao = NotesDatabase.getInstance().getNoteDao()
    private val notesList = noteDao.getAllNotes()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()
        clickEvents()
        recyclerViewSet()
        inputSearch.addTextChangedListener {
            searchedText(it.toString())
        }
    }


    private fun initViews(){
        btnAddNote = findViewById(R.id.imageAddNoteMain)
        recyclerViewNotes = findViewById(R.id.notesList)
        inputSearch = findViewById(R.id.inputSearch)
    }

    private fun clickEvents(){
        btnAddNote.setOnClickListener{
            val intent = Intent(this@MainActivity, CreateNoteScreen::class.java)
            startActivity(intent)
        }

        notesListAdapter.setOnNoteClickedListener { noteEntity, index ->
            val intent = Intent(this@MainActivity, CreateNoteScreen::class.java)
            intent.putExtra("isViewOrUpdate",true)
            intent.putExtra("NOTE",noteEntity)
            startActivity(intent)
        }


    }
    private fun recyclerViewSet(){
        notesListAdapter.submitList(notesList)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerViewNotes.adapter = notesListAdapter

    }

    private fun searchedText(text: String){
        val tempList = ArrayList<NoteEntity>()
        val dataList = noteDao.getAllNotes()
        val size = noteDao.getAllNotes().size
        for (i in 0 until  size){
            if(dataList[i].noteText.lowercase().contains(text.lowercase()) ||
                dataList[i].subTitle.lowercase().contains(text.lowercase()) ||
                 dataList[i].title.lowercase().contains(text.lowercase())){
                tempList.add(dataList[i])
            }
        }
        notesListAdapter.submitList(tempList)
    }



    override fun onResume() {
        super.onResume()
        notesListAdapter.submitList(noteDao.getAllNotes())
    }
}