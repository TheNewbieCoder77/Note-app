package uz.gita.mynotes.adapter


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import uz.gita.mynotes.R
import uz.gita.mynotes.data.entities.NoteEntity
import java.util.Timer
import java.util.TimerTask

class NotesListAdapter : ListAdapter<NoteEntity, NotesListAdapter.NotesViewHolder>(NoteDiffUtil) {

    private var onNoteClickedListener: ((NoteEntity, Int) -> Unit)? = null


    fun setOnNoteClickedListener(f: (NoteEntity, Int) -> Unit) {
        onNoteClickedListener = f
    }

    object NoteDiffUtil : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return  oldItem.title == newItem.title &&
                    oldItem.subTitle == newItem.subTitle &&
                    oldItem.noteText == newItem.noteText &&
                    oldItem.imagePath == newItem.imagePath &&
                    oldItem.webLink == newItem.webLink &&
                    oldItem.dataTime == newItem.dataTime &&
                    oldItem.color == newItem.color
        }

    }

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        private val txtSubtitle: TextView = view.findViewById(R.id.txtSubtitle)
        private val txtDateTime: TextView = view.findViewById(R.id.txtDateTime)
        private val layoutNote: LinearLayout = view.findViewById(R.id.layoutNote)
        private val imgNote: RoundedImageView = view.findViewById(R.id.imgNote)

        init {
            layoutNote.setOnClickListener {
                onNoteClickedListener?.invoke(getItem(absoluteAdapterPosition) as NoteEntity, absoluteAdapterPosition)
            }
        }

        fun bind() {
            val data = getItem(absoluteAdapterPosition)
            txtTitle.text = data.title
            if (data.subTitle.trim().isEmpty()) {
                txtSubtitle.visibility = View.GONE
            } else {
                txtSubtitle.text = data.subTitle
            }

            txtDateTime.text = data.dataTime
            val gradientDrawable = layoutNote.background as GradientDrawable
            if (data.color.isEmpty()) {
                gradientDrawable.setColor(Color.parseColor("#333333"))
            } else {
                gradientDrawable.setColor(Color.parseColor(data.color))
            }

            if (data.imagePath.isEmpty()) {
                imgNote.visibility = View.GONE
            } else {
                imgNote.setImageBitmap(BitmapFactory.decodeFile(data.imagePath))
                imgNote.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) = holder.bind()


}