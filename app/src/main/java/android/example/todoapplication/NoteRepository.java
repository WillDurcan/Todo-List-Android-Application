package android.example.todoapplication;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;

public class NoteRepository { // This is another abstraction layer between the different data layer and the rest of the App. Middleground between UI and data sources /db
    private NoteDao noteDao; // Abstract method form notedao
    private LiveData<List<Note>> allNotes; // Creating instances of /Data Access Object, in order to do these commands in the repository

    // If there were multiple data sources, the layer adds the option to differentiate from these data sources .
    // in out case we only get data from the DOA but in others we may get it from the interent.


    // This solves the rotation problem , as the activity recieves the same view instance
    // the repository gives the view model
    // view models contains the

    public NoteRepository (Application application){

        // Application is a subclass of content
        NoteDatabase database = NoteDatabase.getInstance(application); // note database gets the instance, this creates a instance of the app
        noteDao = database.noteDao();
                //abstract method in other class, we build it in the istance to call it
        allNotes=noteDao.getALLNotes(); // method without a body that I created in my DOA interface
    }

        // All Methods in Dao class

    // Room doesn't allow database operations to take place on the main thread or else it would create crashes & freezes so I use Async class
    // These methods call the Async methods I've created after. This acts as the abstraction layer


        public void insert(Note note){
            new InsertNoteAsyncTask(noteDao).execute(note); }
            // this is what we want to insert}

        public void update (Note note){
            new UpdateNoteAsyncTask(noteDao).execute(note);
        }

        public void delete(Note note) {
            new DeleteNoteAsyncTask(noteDao).execute(note);
        }

        public void deleteAllNotes()
        {
            new DeleteAllNoteAsyncTask(noteDao).execute(); //Nothing needs to be passed
            }

        public LiveData<List<Note>> getAllNotes(){
        return allNotes; // gets all note objects
        }
        /*
               it has to be static so that it doesnt have a reference to the repository itself


               syncTask class, which makes it easier to do operations on a
                background thread and publish the results on the UI/main thread,
                without having to manipulate threads and handlers ourselves.

                AsyncTask enables proper and easy use of the UI thread.
                This class allows you to perform background
                 operations and publish results on the UI thread without
                 having to manipulate threads and/or handlers.
             */


    // The 4 different DOA tasks syncTask class, which makes it easier to do operations on a
    //                background thread and publish the results on the UI/main thread,
    //                without having to manipulate threads and handlers ourselves.
    //
    //                AsyncTask enables proper and easy use of the UI thread.
    //                This class allows you to perform background
    //                 operations and publish results on the UI thread without
    //                 having to manipulate threads and/or handlers.

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
            private NoteDao noteDao; // Need this notedoa to do database operations
            private InsertNoteAsyncTask(NoteDao noteDao){ //passing noadoa over a constructor
            this.noteDao = noteDao;
            }

        @Override
            protected Void doInBackground(Note... notes) { // One of the 4 AsyncTasks
                noteDao.insert(notes[0]); // First index, similar to array
                return null;
            }
        }


    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }


    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }


    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private DeleteAllNoteAsyncTask (NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
    }



