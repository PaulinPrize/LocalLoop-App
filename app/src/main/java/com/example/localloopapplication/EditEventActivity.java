package com.example.localloopapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EditEventActivity extends AppCompatActivity {

    private EditText editName, editDateTime, editDescription, editFee;
    private Button btnConfirm;

    private String eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialisation des composants
        editName = findViewById(R.id.editEventName);
        editDateTime = findViewById(R.id.editEventDateTime);
        editDescription = findViewById(R.id.editEventDescription);
        editFee = findViewById(R.id.editEventFee);
        btnConfirm = findViewById(R.id.btnConfirmEdit);

        // Récupération des données envoyées depuis l'adapter
        eventId = getIntent().getStringExtra("eventId");
        String name = getIntent().getStringExtra("eventName");
        String dateTime = getIntent().getStringExtra("eventDateTime");
        String description = getIntent().getStringExtra("eventDescription");
        double fee = getIntent().getDoubleExtra("eventFee", 0.0);

        // Remplir les champs avec les anciennes données
        editName.setText(name);
        editDateTime.setText(dateTime);
        editDescription.setText(description);
        editFee.setText(String.format("%.2f", fee));

        // Bouton de confirmation
        btnConfirm.setOnClickListener(v -> {
            new AlertDialog.Builder(EditEventActivity.this)
                    .setTitle("Confirmer la modification")
                    .setMessage("Voulez-vous vraiment enregistrer ces changements ?")
                    .setPositiveButton("Oui", (dialog, which) -> saveChanges())
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    // Fonction de sauvegarde
    private void saveChanges() {
        String newName = editName.getText().toString().trim();
        String newDateTime = editDateTime.getText().toString().trim();
        String newDescription = editDescription.getText().toString().trim();
        String newFeeString = editFee.getText().toString().trim();

        // Validation
        if (newName.isEmpty()) {
            editName.setError("Nom requis");
            editName.requestFocus();
            return;
        }

        double feeValue;
        try {
            feeValue = Double.parseDouble(newFeeString);
        } catch (NumberFormatException e) {
            editFee.setError("Frais invalide");
            editFee.requestFocus();
            return;
        }

        // ✅ Récupérer l'ID de l'organisateur connecté
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Créer l'objet mis à jour
        Event updatedEvent = new Event();
        updatedEvent.setId(eventId); // important pour la base
        updatedEvent.setName(newName);
        updatedEvent.setDateTime(newDateTime);
        updatedEvent.setDescription(newDescription);
        updatedEvent.setFee(feeValue);
        updatedEvent.setOrganizerId(organizerId); // ✅ ne surtout pas oublier !

        // Mettre à jour Firebase
        FirebaseDatabase.getInstance().getReference("events")
                .child(eventId)
                .setValue(updatedEvent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditEventActivity.this, "Événement mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // revenir en arrière
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditEventActivity.this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                });
    }
}
