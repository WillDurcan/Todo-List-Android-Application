package android.example.todoapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel { //subclass of view model, the application is passed in the constructor
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    // the view model is the gateway for the UI controller
    /* view model outlives the activitiy in order to solve rotational problem */
    //its a sub model of view model

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application); // constructor - creates a new instance of repository that takes the application
        allNotes = repository.getAllNotes();
    }

        public void insert(Note note){
        repository.insert(note);
        }
        public void update(Note note){
        repository.update(note);
        }
        public void delete(Note note){
        repository.delete(note);
        }
        public void deleteAllnotes(){
        repository.deleteAllNotes();
        }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes; // varible
    }
}
