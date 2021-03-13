package android.example.todoapplication;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class}, version =1) //The list of database views included in the database. Each class turns into a view in the database.
public abstract class NoteDatabase extends RoomDatabase {

    private static  NoteDatabase instance; // singleton
    public abstract NoteDao noteDao(); // I've used this method to access the DAO


    //Note database extends room, you input the note entitys

    public static synchronized NoteDatabase getInstance(Context context){
        /*
         - synchronized means that this stops multi threads creating two instances of the database when trying to access the db at the same time.
         - Context is a way to pass background information to our app to the new component
         Which in our case is the resources , and it could be used to launch actives.
        */
        if (instance==null){ // So I only want to indicate the db if one doesn't already exist
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .addCallback(roomCallback) // When the instance is first created it will execute the method that populates the first 3 notes
                    .fallbackToDestructiveMigration() // When the verion number exceeds 1 it needs to migrate to the schema, this will delete the current and create another
                    .build(); // creates and initialise db
        }
        return instance; // it returns the already excisting instance
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db); //Overrides this class so that the db can be populated
            new PopulateDBAsyncTask(instance).execute(); // Executes the task with the specified parameters.
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void,  Void>
    {
        private NoteDao noteDao;
        private PopulateDBAsyncTask(NoteDatabase db){
            noteDao=db.noteDao();
        }
        // Does these task in the background. Then update UI thread.

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Desription1",1));
            noteDao.insert(new Note("Title 2", "Desription2",2));
            noteDao.insert(new Note("Title 3", "Desription3",3));
            return null;
        }
    }

}

