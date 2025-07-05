
package com.example.localloopapplication;


// Android core
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Google Play services
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

// Firebase
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Glide (optional, for displaying selected image in preview)
import com.bumptech.glide.Glide;

// Java utility
import java.io.IOException;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private EditText etName, etDescription, etCategory, etFee, etDateTime;
    private Button btnSave, selectImageButton;

    private DatabaseReference eventsRef;
    
    private Uri selectedImageUri;
    
    public static final int PICK_IMAGE = 1001;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event); // We'll build this layout next

        // Firebase reference to "events" table
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Link input fields
        etName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etEventDescription);
        etCategory = findViewById(R.id.etEventCategory);
        etFee = findViewById(R.id.etEventFee);
        etDateTime = findViewById(R.id.etEventDateTime);
        btnSave = findViewById(R.id.btnSaveEvent);
        selectImageButton = findViewById(R.id.addImagebtn);
        btnSave.setOnClickListener(v -> saveEvent());
        selectImageButton.setOnClickListener(v-> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });
    }
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            // save image 
            selectedImageUri = data.getData();
        }
        
    } 
    
    
    private void saveEvent() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String feeText = etFee.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        
        if (selectedImageUri!=null){
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("posts_images");
            String fileName = UUID.randomUUID().toString()+".jpg";
            storageRef.child(fileName).putFile(selectedImageUri)
                .addOnSuccessListener(taskSnaphsot -> {
                    // get download url of uploaded image 
                    storageRef.child(fileName).getDownloadUrl()
                        .addOnSuccessListener(downloadUri -> {
                            String imageUrl = downloadUri.toString();
                            // continue the logic
                            double fee = 0;
                            try {
                                if (!feeText.isEmpty()) fee = Double.parseDouble(feeText);
                            } catch (NumberFormatException e) {
                                etFee.setError("Invalid fee");
                                return;
                            }
                            saveEventToDatabase(name, description, category, fee, dateTime, imageUrl);
                            
                        });
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
            });
        } else {
            // no image 
            saveEventToDatabase(name, description, category, feeText, dateTime, null);
        }
        
    }
    
    private void saveEventToDatabase(String name, String description, String category, double fee, String dateTime, String imageUrl){
        
        /*
        ok so notice how the code below goes directly to check if name is empty and if proce is valid 
        well it's important to know that the saveEvent method is the one that initializes these variables which are the event's attributes
        and then it passes them as arguments to this method
        so the answer to, how does this method already have the values and is able to check for invalid arguments
        is that it has them because they are passed as an argument 
        we are allowed to pass invalid arguments to this method, for example, empty name or other stuff 
        and then this method handles it properly
        */
        if (name.isEmpty()) {
            etName.setError("Event name is required");
            return;
        }

        
        
        // Get current logged-in organizer's UID
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String eventId = UUID.randomUUID().toString();
        Event event = new Event(eventId, organizerId, name, description, category, fee, dateTime, imageUrl);

        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show());
    
    }
}
