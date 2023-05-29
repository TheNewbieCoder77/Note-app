package uz.gita.mynotes.screens


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import uz.gita.mynotes.R
import uz.gita.mynotes.data.NotesDatabase
import uz.gita.mynotes.data.daos.NoteDao
import uz.gita.mynotes.data.entities.NoteEntity
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteScreen : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var btnDone: ImageView
    private lateinit var title: EditText
    private lateinit var subtitle: EditText
    private lateinit var noteText: EditText
    private lateinit var dateTime: TextView
    private lateinit var notesDatabase: NotesDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var viewSubtitleIndicator: View
    private lateinit var layoutMiscellaneous: LinearLayout
    private lateinit var layoutAddImage: LinearLayout
    private lateinit var layoutAddURL: LinearLayout
    private lateinit var imgNote: ImageView
    private lateinit var textWebURL: TextView
    private lateinit var layoutWebURL: LinearLayout
    private lateinit var btnRemoveWebURL: ImageView
    private lateinit var btnRemoveImage: ImageView
    private lateinit var layoutDeleteNote: LinearLayout

    private lateinit var imgColor1: ImageView
    private lateinit var imgColor2: ImageView
    private lateinit var imgColor3: ImageView
    private lateinit var imgColor4: ImageView
    private lateinit var imgColor5: ImageView

    private var checkUnableNote = false
    private var selectedNoteColor: String = "#333333"
    private var selectedImagePath: String = ""
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<LinearLayout>
    private val REQUEST_CODE_STORAGE_PERMISSION = 1
    private val REQUEST_CODE_SELECT_IMAGE = 2
    private var dialogAddURL: AlertDialog? = null
    private var dialogDeleteNote: AlertDialog? = null
    private lateinit var noteForUpdate: NoteEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note_screen)

        initViews()
        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            noteForUpdate = intent.getSerializableExtra("NOTE") as NoteEntity
            setViewOrUpdateNote()
        }
        clickEvents()
        notesDatabase = NotesDatabase.getInstance()
        noteDao = notesDatabase.getNoteDao()
        dateTime.text = SimpleDateFormat("EEEE, dd yyyy MMMM HH:mm a", Locale.getDefault()).format(Date())
        bottomSheetBehaviour = BottomSheetBehavior.from(layoutMiscellaneous)
        initMiscellaneous()
        setSubtitleIndicator()


    }

    private fun setViewOrUpdateNote() {
        title.setText(noteForUpdate.title)
        subtitle.setText(noteForUpdate.subTitle)
        noteText.setText(noteForUpdate.noteText)
        dateTime.text = noteForUpdate.dataTime
        if (noteForUpdate.imagePath.isNotEmpty()) {
            imgNote.setImageBitmap(BitmapFactory.decodeFile(noteForUpdate.imagePath))
            imgNote.visibility = View.VISIBLE
            btnRemoveImage.visibility = View.VISIBLE
            selectedImagePath = noteForUpdate.imagePath
        }
        if (noteForUpdate.webLink.isNotEmpty()) {
            textWebURL.text = noteForUpdate.webLink
            layoutWebURL.visibility = View.VISIBLE
        }

    }

    private fun initViews() {
        btnBack = findViewById(R.id.imageBack)
        btnDone = findViewById(R.id.imageSave)
        title = findViewById(R.id.inputNoteTitle)
        subtitle = findViewById(R.id.inputNoteSubtitle)
        noteText = findViewById(R.id.inputNote)
        dateTime = findViewById(R.id.textDate)
        textWebURL = findViewById(R.id.txtWebURL)
        layoutWebURL = findViewById(R.id.layoutWebURL)
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator)

        layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous)

        imgColor1 = layoutMiscellaneous.findViewById(R.id.imgColor1)
        imgColor2 = layoutMiscellaneous.findViewById(R.id.imgColor2)
        imgColor3 = layoutMiscellaneous.findViewById(R.id.imgColor3)
        imgColor4 = layoutMiscellaneous.findViewById(R.id.imgColor4)
        imgColor5 = layoutMiscellaneous.findViewById(R.id.imgColor5)
        layoutAddImage = layoutMiscellaneous.findViewById(R.id.layoutAddImage)
        layoutAddURL = layoutMiscellaneous.findViewById(R.id.layoutAddUrl)
        layoutDeleteNote = layoutMiscellaneous.findViewById(R.id.layoutDeleteNote)
        imgNote = findViewById(R.id.imgNote)
        btnRemoveImage = findViewById(R.id.imgRemoveImage)
        btnRemoveWebURL = findViewById(R.id.imgRemoveWebURL)


    }

    private fun clickEvents() {
        btnBack.setOnClickListener {
            finish()
        }

        btnDone.setOnClickListener {
            saveNote()
            if (checkUnableNote) finish()
        }

        layoutMiscellaneous.findViewById<View>(R.id.viewColor1).setOnClickListener {
            selectedNoteColor = "#333333"
            imgColor1.setImageResource(R.drawable.ic_done)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            setSubtitleIndicator()
        }

        layoutMiscellaneous.findViewById<View>(R.id.viewColor2).setOnClickListener {
            selectedNoteColor = "#FDBE3B"
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(R.drawable.ic_done)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            setSubtitleIndicator()
        }

        layoutMiscellaneous.findViewById<View>(R.id.viewColor3).setOnClickListener {
            selectedNoteColor = "#FF4842"
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(R.drawable.ic_done)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            setSubtitleIndicator()
        }

        layoutMiscellaneous.findViewById<View>(R.id.viewColor4).setOnClickListener {
            selectedNoteColor = "#3A52Fc"
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(R.drawable.ic_done)
            imgColor5.setImageResource(0)
            setSubtitleIndicator()
        }

        layoutMiscellaneous.findViewById<View>(R.id.viewColor5).setOnClickListener {
            selectedNoteColor = "#000000"
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(R.drawable.ic_done)
            setSubtitleIndicator()
        }

        layoutAddImage.setOnClickListener {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@CreateNoteScreen,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                selectImage()
            }
        }

        layoutAddURL.setOnClickListener {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddURLDialog()
        }

        if (::noteForUpdate.isInitialized) {
            when (noteForUpdate.color) {
                "#FDBE3B" -> {
                    layoutMiscellaneous.findViewById<View>(R.id.viewColor2).performClick()
                }
                "#FF4842" -> {
                    layoutMiscellaneous.findViewById<View>(R.id.viewColor3).performClick()
                }
                "#3A52Fc" -> {
                    layoutMiscellaneous.findViewById<View>(R.id.viewColor4).performClick()
                }
                "#000000" -> {
                    layoutMiscellaneous.findViewById<View>(R.id.viewColor5).performClick()
                }
                else -> {
                    layoutMiscellaneous.findViewById<View>(R.id.viewColor1).performClick()
                }
            }
        }

        btnRemoveWebURL.setOnClickListener{
            textWebURL.text = null
            layoutWebURL.visibility = View.GONE
        }

        btnRemoveImage.setOnClickListener{
            imgNote.setImageBitmap(null)
            imgNote.visibility = View.GONE
            btnRemoveImage.visibility = View.GONE
            selectedImagePath = ""
        }

        if(::noteForUpdate.isInitialized){
            layoutDeleteNote.visibility = View.VISIBLE
            layoutDeleteNote.setOnClickListener{
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                showDeleteNoteDialog()
            }
        }


    }

    private fun saveNote() {
        if (title.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            return
        } else if (subtitle.text.toString().trim().isEmpty() && noteText.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        var tempVariable = ""
        if (layoutWebURL.visibility == View.VISIBLE) tempVariable = textWebURL.text.toString()

        val note: NoteEntity?
        if (::noteForUpdate.isInitialized) {
            note = NoteEntity(
                id = noteForUpdate.id,
                title = title.text.toString(),
                subTitle = subtitle.text.toString(),
                noteText = noteText.text.toString(),
                dataTime = dateTime.text.toString(),
                color = selectedNoteColor,
                imagePath = selectedImagePath,
                webLink = tempVariable
            )
        } else {
            note = NoteEntity(
                title = title.text.toString(),
                subTitle = subtitle.text.toString(),
                noteText = noteText.text.toString(),
                dataTime = dateTime.text.toString(),
                color = selectedNoteColor,
                imagePath = selectedImagePath,
                webLink = tempVariable
            )
        }

        noteDao.insertNote(note)
        checkUnableNote = true
    }


    private fun initMiscellaneous() {
        layoutMiscellaneous.findViewById<TextView>(R.id.txtMiscellaneous).setOnClickListener {
            if (bottomSheetBehaviour.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setSubtitleIndicator() {
        val gradientDrawable: GradientDrawable = viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    try {
                        val inputStream = contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility = View.VISIBLE
                        btnRemoveImage.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUri)

                    } catch (exception: Exception) {
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    @SuppressLint("Recycle")
    private fun getPathFromUri(contentUri: Uri): String {
        var filePath = ""
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path!!
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun showAddURLDialog() {
        if (dialogAddURL == null) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@CreateNoteScreen)
            val view: View = LayoutInflater.from(this).inflate(R.layout.layout_add_url, findViewById(R.id.layoutAddUrlContainer), false)
            builder.setView(view)
            dialogAddURL = builder.create()

            if (dialogAddURL!!.window != null) {
                dialogAddURL!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            val inputURL = view.findViewById<EditText>(R.id.inputURL)
            inputURL.requestFocus()

            val btnAdd = view.findViewById<TextView>(R.id.txtAdd)
            val btnCancel = view.findViewById<TextView>(R.id.txtCancel)

            btnAdd.setOnClickListener {
                if (inputURL.text.toString().trim().isEmpty()) {
                    Toast.makeText(this@CreateNoteScreen, "Enter URL!", Toast.LENGTH_SHORT).show()
                } else if (!Patterns.WEB_URL.matcher(inputURL.text.toString()).matches()) {
                    Toast.makeText(this@CreateNoteScreen, "Enter valid URL!", Toast.LENGTH_SHORT).show()
                } else {
                    textWebURL.text = inputURL.text.toString()
                    layoutWebURL.visibility = View.VISIBLE
                    dialogAddURL!!.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dialogAddURL!!.dismiss()
            }
        }
        dialogAddURL!!.show()
    }

    private fun showDeleteNoteDialog(){
        if(dialogDeleteNote == null){
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@CreateNoteScreen)
            val view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, findViewById(R.id.layoutDeleteNoteContainer),false)
            builder.setView(view)
            dialogDeleteNote = builder.create()
            if(dialogDeleteNote!!.window != null){
                dialogDeleteNote!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            val btnDelete = view.findViewById<TextView>(R.id.btnDeleteNote)
            val btnCancel = view.findViewById<TextView>(R.id.btnCancel)

            btnDelete.setOnClickListener{
                noteDao.deleteNote(noteForUpdate)
                dialogDeleteNote!!.dismiss()
                finish()
            }

            btnCancel.setOnClickListener {
                dialogDeleteNote!!.dismiss()
            }
        }

        dialogDeleteNote!!.show()
    }

}