package android.example.todoapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1; // Constant for start activity
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add button functionality
        FloatingActionButton buttonAddNote = findViewById(R.id.bottom_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class); // Creates a new intent of add edit open
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        // All displayed as items in a RecyclerView & stored in SQL lite, Room is wrapper around SQL lite


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Creating a instance of Note Adapter Class

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //This sets up/ implements the Recycler viewer ,
        // the linear manager makes sure they all come after one another.


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) { //This returns the live data
                adapter.setNotes(notes);
            }
        });



        // This allows items to be deleted with a swipe

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete((adapter.getNoteAt(viewHolder.getAdapterPosition()))); // To get the note adapter postion
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        // Section for updating note

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListerner() { // listen method that was created in note adapter
            @Override
            public void onitemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class); // add and edit the notes
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId()); // These are the items we want to update
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,EDIT_NOTE_REQUEST); // Room needs to dsigtustish form the different taks
            }
        });

    }


    // W

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Intent that was created when adding a new note
        super.onActivityResult(requestCode, resultCode, data);

        // This code manages the different requests,

        // Add request

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) { //request code that is passed

                String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
                Note note = new Note(title, description, priority);
                noteViewModel.insert(note);

            Toast.makeText(this, "Todo Saved", Toast.LENGTH_SHORT).show();

            //Edit note request

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            //stuff for update
            int id =data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id==-1){ // something went wrong
                Toast.makeText(this, "Todo Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title,description,priority);
            note.setId(id); // Without this room cant idenify this entry
            noteViewModel.update(note);

            Toast.makeText(this, "Todo Updated", Toast.LENGTH_SHORT).show();

        }  else {
            Toast.makeText(this, "Todo not Saved", Toast.LENGTH_SHORT).show();

        }
    }

    @Override // This creates the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllnotes(); // no passing needed
                Toast.makeText(this, "All todos deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

