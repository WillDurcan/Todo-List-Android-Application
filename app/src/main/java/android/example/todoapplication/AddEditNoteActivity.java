package android.example.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    // Keys
    public static final String EXTRA_ID=
            "com.android.example.todoapplication.EXTRA_ID";

    public static final String EXTRA_TITLE=
            "com.android.example.todoapplication.EXTRA_TITLE";

    public static final String EXTRA_DESCRIPTION=
            "com.android.example.todoapplication.EXTRA_DESCRIPTION";

    public static final String EXTRA_PRIORITY=
            "com.android.example.todoapplication.EXTRA_PRIORITY";

    private EditText editTextTitle; //creating new instances
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note2);

        editTextTitle = findViewById(R.id.edit_text_title); // The Title on the note adding page
        editTextDescription = findViewById(R.id.edit_description); // The Des
        numberPickerPriority = findViewById(R.id.number_picker_priority); // priority

        numberPickerPriority.setMinValue(1); // min priority
        numberPickerPriority.setMaxValue(10); // max

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); //close icon

        Intent intent =getIntent();

        if (intent.hasExtra(EXTRA_ID)){ // this determines what title the page has
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(Intent.EXTRA_TITLE)); // we need the new inputted text
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {

            setTitle("Add Note");
        }
    }

    private  void saveNote(){
        String title = editTextTitle.getText().toString(); //Gets the new text from the user input
        String description = editTextDescription.getText().toString();
        int priority =numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()){ //It will return true if any of those cases are empty
            Toast.makeText(this, "Please insert Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }
        //Checks whether the fields have been filled in order to save

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title); // Get the new values for the inputed title
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);


        // If its update

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,data); // if the save is successful set the resultCode to RESULT_OK
            finish();

            //putting data into intents , this avoids having to make additional view model

    }

    // This gets the current  We want to save the note if its been saved

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu); // pass the menu I've just created, it tells the system to use the menu
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note: // I create only one case as one one item will be selected
                saveNote(); // Method
                return true;
            default:
            return super.onOptionsItemSelected(item);
            //save note clicked we call the new savenote method
        }
    }
}
