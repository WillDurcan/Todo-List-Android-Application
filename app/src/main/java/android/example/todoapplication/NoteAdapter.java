package android.example.todoapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    // Note Adpater extends recyler view it holds , Noteview obejects into the recyler
    // which is done by Setnotes

    private List<Note> notes = new ArrayList<>();
    private OnItemClickListerner listerner;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote =notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }
    //this gets all the text into the corrosponding section

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
    // this is where it updates the notes onto the recyler
    // above are 3 methods we need to override


        // This gets the postion of the note outside of this class
    public Note getNoteAt(int position){
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            // Section for accessing existing note
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listerner != null && position != RecyclerView.NO_POSITION) { // Ensure no crashes
                        listerner.onitemClick(notes.get(position)); // passed notes array list and gets the postion of clicked item
                    }
                }
            });


        }
    }

    // Create a interface in order to click and update a existing note

    public interface OnItemClickListerner{
        void onitemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListerner listener){ // The interface I've created
        this.listerner=listener;
    }
}
