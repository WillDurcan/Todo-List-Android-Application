package android.example.todoapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Tells room its DOA
public interface NoteDao { //Data Access Object / Model
    /*
     Room generates the code for us so therefore its a interface or abstract classes as we don't provide method body
     Data access object it includes SQ lite functions
     Entity is a annotation in the ROOM , its used to represent one table in our database.
     We use getter methods for better encapsulation , also we use getters , and room generates constructors ,
     also we use setters to assign ID
    */

    // Here I create all the methods I want to happen to my note table

    @Insert // Room will automattically write the consequent implementation code
   void insert(Note note); // pass a note

    @Update
    void update(Note note);

    @Delete
    void delete(Note note); //If I wanted to pass more notes I could but I only want to remove one not at a time

    @Query("DELETE FROM note_table") // Table made in notes class
    void deleteAllNotes();

    // '*' = all collums
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
   LiveData<List<Note>> getALLNotes(); // Live data , creating a list of note objects
    // Thus, if any changes are made to the table they will automatically be updated. By declaring it to return live data.


}
