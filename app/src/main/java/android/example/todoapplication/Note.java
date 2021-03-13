package android.example.todoapplication;
import androidx.room.Entity;
import androidx.room.PrimaryKey; // SQL annotations


// Step one was to create a table in which I will store my note object items in
@Entity(tableName = "note_table") // Name of note table ( this is used in notedao)
//useful as it hides all the SQL code and creates the table
//entity class
public class Note { //Model

    @PrimaryKey(autoGenerate = true)

    //Constructors of the different qualities each note needs
    private int id;

    private String title;

    private String description;

    private int priority;


    // Constructor
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    // This is a single note in which displays the 3 main atributes

    public void setId(int id) {
        this.id = id;
    }
    //this is the only value we dont have in the constructor


    public int getId() {
        return id;
    }

    // Get methods used once the user has inputted these values, and these are used to fill in the constructors
    // and therefore build a new note with these attributes

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}

