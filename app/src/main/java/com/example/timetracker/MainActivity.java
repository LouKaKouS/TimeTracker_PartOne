package com.example.timetracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivity activity;
    private View dialogView;
    private String typeActivitySelected;
    private NumberPicker startHourSelected, startMinuteSelected;
    private NumberPicker endHourSelected, endMinuteSelected;
    private String taskName;
    private String selectedStartTime;
    private String selectedEndTime;
    private int selectedDifficultyNumber;
    private String difficultyColor;
    private DatabaseHelper databaseHelper;
    private TaskAdapter taskAdapter;
    private List<TaskData> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity = this;

        TextView dateTextView = findViewById(R.id.date_indicator);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String date = dateFormat.format(calendar.getTime());

        dateTextView.setText(date);

        // Récupérer le CalendarView depuis le layout XML
        CalendarView calendarView = findViewById(R.id.calendarView);

        // Initialiser les variables
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        // Récupérer le bouton addButton
        Button addButton = findViewById(R.id.addButton);

        // Ajouter un écouteur de clic au bouton addButton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivityPopup();
            }
        });
    }

    private void newActivityPopup() {

        AlertDialog.Builder selectTypeActivityPopup = new AlertDialog.Builder(activity);
        selectTypeActivityPopup.setTitle("Nouvelle activitée : ");

        selectTypeActivityPopup.setPositiveButton("Professionnelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                typeActivitySelected = "professionnelle";
                newTaskPopup(typeActivitySelected);
            }
        });

        selectTypeActivityPopup.setNegativeButton("Personelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                typeActivitySelected = "personnelle";
                newTaskPopup(typeActivitySelected);
            }
        });
        selectTypeActivityPopup.show();
    }

    private String newTaskPopup(String typeActivitySelected) {
        AlertDialog.Builder addTaskPopup = new AlertDialog.Builder(activity);
        addTaskPopup.setTitle("Nouvelle tâche " + typeActivitySelected + " :");

        // Créer un EditText pour permettre à l'utilisateur d'entrer du texte
        final EditText input = new EditText(MainActivity.this);
        addTaskPopup.setView(input);

        // Définir les boutons "OK" et "Annuler" de la popup
        addTaskPopup.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Extraire le texte de l'EditText et le stocker dans une variable
                taskName = input.getText().toString();

                if (taskName.isEmpty()) {
                    // Afficher un message d'erreur si aucun nom n'est saisi
                    Toast.makeText(MainActivity.this, "Erreur : Aucun nom inscrit...", Toast.LENGTH_SHORT).show();
                    newTaskPopup(typeActivitySelected);
                } else {
                    setSrartTime();
                }
            }
        });

        addTaskPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Le code à exécuter lorsque l'utilisateur appuie sur le bouton "Annuler"
                newActivityPopup(); // Fermer la popup
            }
        });
        addTaskPopup.show();
        return taskName;
    }

    private String setSrartTime() {
        AlertDialog.Builder secondPopup = new AlertDialog.Builder(activity);
        secondPopup.setTitle("Heure du début : ");

        // Inflate le layout XML pour la deuxième popup
        dialogView = getLayoutInflater().inflate(R.layout.time_start, null);
        secondPopup.setView(dialogView);

        // Récupération des NumberPickers pour les heures et les minutes
        startHourSelected = dialogView.findViewById(R.id.hourStart);
        startMinuteSelected = dialogView.findViewById(R.id.minuteStart);

        // Définir les valeurs minimales et maximales pour les NumberPickers
        startHourSelected.setMinValue(0);
        startHourSelected.setMaxValue(23);
        startMinuteSelected.setMinValue(0);
        startMinuteSelected.setMaxValue(3); // Indice de la dernière valeur dans le tableau

        // Définir les valeurs acceptables pour les minutes (00, 15, 30, 45)
        String[] minuteValues = {"00", "15", "30", "45"};

        // Définir les valeurs pour le NumberPicker des minutes
        startMinuteSelected.setDisplayedValues(minuteValues);

        // Définir les boutons "OK" et "Annuler" de la deuxième popup
        secondPopup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérer les valeurs sélectionnées dans les NumberPickers
                int startHour = startHourSelected.getValue();
                int startMinute = startMinuteSelected.getValue();
                String[] selectedMinuteValues = {"00", "15", "30", "45"};
                String selectedMinute = selectedMinuteValues[startMinute];

                // Formater l'heure de début sélectionnée
                selectedStartTime = String.format("%02d:%02d", startHour, startMinute);

                // Appeler la méthode pour sélectionner l'heure de fin
                setEndTime(typeActivitySelected);
            }
        });

        secondPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retourner à la popup de saisie de la tâche
                newTaskPopup(typeActivitySelected);
            }
        });

        // Afficher la deuxième popup
        secondPopup.show();

        // Retourner l'heure de début sélectionnée
        return selectedStartTime;
    }

    private String setEndTime(String typeActivitySelected) {
        AlertDialog.Builder thirdPopup = new AlertDialog.Builder(activity);
        thirdPopup.setTitle("Heure de fin : ");

        // Inflate le layout XML pour la troisième popup
        dialogView = getLayoutInflater().inflate(R.layout.time_end, null);
        thirdPopup.setView(dialogView);

        // Récupération des NumberPickers pour les heures et les minutes
        endHourSelected = dialogView.findViewById(R.id.hourEnd);
        endMinuteSelected = dialogView.findViewById(R.id.minuteEnd);

        // Définir les valeurs minimales et maximales pour les NumberPickers
        endHourSelected.setMinValue(0);
        endHourSelected.setMaxValue(23);
        endMinuteSelected.setMinValue(0);
        endMinuteSelected.setMaxValue(3); // Indice de la dernière valeur dans le tableau

        // Définir les valeurs acceptables pour les minutes (00, 15, 30, 45)
        String[] minuteValues = {"00", "15", "30", "45"};

        // Définir les valeurs pour le NumberPicker des minutes
        endMinuteSelected.setDisplayedValues(minuteValues);

        // Définir les boutons "OK" et "Annuler" de la troisième popup
        thirdPopup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Récupérer les valeurs sélectionnées dans les NumberPickers
                int endHour = endHourSelected.getValue();
                int endMinute = endMinuteSelected.getValue();

                String[] selectedMinuteValues = {"00", "15", "30", "45"};
                String selectedMinute = selectedMinuteValues[endMinute];

                // Formater l'heure de fin sélectionnée
                selectedEndTime = String.format("%02d:%02d", endHour, endMinute);
                if(selectedEndTime.equals(selectedStartTime))
                {
                    Toast.makeText(MainActivity.this, "Erreur : Vous avez choisis la même heure", Toast.LENGTH_SHORT).show();
                    setEndTime(typeActivitySelected);
                }
                else
                {
                    if (typeActivitySelected.equals("personnelle")) {
                        finishTaskPopup(typeActivitySelected, taskName, selectedStartTime, selectedEndTime, difficultyColor);
                    } else {
                        difficultyTask();
                    }
                }

            }
        });

        thirdPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retourner à la popup de sélection de l'heure de début
                setSrartTime();
            }
        });

        // Afficher la troisième popup
        thirdPopup.show();

        // Retourner l'heure de fin sélectionnée
        return selectedEndTime;
    }


    private void difficultyTask() {
        AlertDialog.Builder fourthPopup = new AlertDialog.Builder(activity);
        fourthPopup.setTitle("Choisissez le niveau de difficulté : ");

        View dialogView = getLayoutInflater().inflate(R.layout.difficulty_task, null);
        fourthPopup.setView(dialogView);

        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        numberPicker.setWrapSelectorWheel(false);

        fourthPopup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDifficultyNumber = numberPicker.getValue();
                if (selectedDifficultyNumber == 1) {
                    difficultyColor = "#00FF27";
                } else if (selectedDifficultyNumber == 2) {
                    difficultyColor = "#AEFF00";
                } else if (selectedDifficultyNumber == 3) {
                    difficultyColor = "#F0FF00";
                } else if (selectedDifficultyNumber == 4) {
                    difficultyColor = "#FFA200";
                } else {
                    difficultyColor = "#FF0000";
                }
                finishTaskPopup(typeActivitySelected, taskName, selectedStartTime, selectedEndTime, difficultyColor);
            }
        });
        fourthPopup.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setEndTime(typeActivitySelected);
            }
        });
        fourthPopup.show();
    }

    private void finishTaskPopup(String typeActivitySelected, String taskName, String selectedStartTime, String selectedEndTime, String difficultyColor) {

        if (difficultyColor == null) {
            difficultyColor = "#00C1FF";
        }
        AlertDialog.Builder finishTaskPopup = new AlertDialog.Builder(activity);

        finishTaskPopup.setTitle("Confirmation ? ");
        finishTaskPopup.setMessage("Type : " + typeActivitySelected + "\nNom : " + taskName + "\nDate de début : " + selectedStartTime + "\nDate de fin : " + selectedEndTime + "\nCode couleur : " + difficultyColor);
        String finalDifficultyColor = difficultyColor;

        String finalDifficultyColor1 = difficultyColor;
        finishTaskPopup.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Créez un nouvel objet TaskData avec les valeurs récupérées
                TaskData taskData = new TaskData();
                taskData.setTypeActivitySelected(typeActivitySelected);
                taskData.setTaskName(taskName);
                taskData.setSelectedStartTime(selectedStartTime);
                taskData.setSelectedEndTime(selectedEndTime);
                taskData.setDifficultyColor(finalDifficultyColor1);
                taskList.add(taskData);
                taskAdapter.addTask(taskData);

                // Insérez l'objet TaskData dans la base de données
                long newRowId = databaseHelper.insertTask(taskData);


                if (newRowId != -1) {
                    Toast.makeText(MainActivity.this, "Données enregistrées avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Échec de l'enregistrement des données", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finishTaskPopup.setNeutralButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (typeActivitySelected == "personnelle") {
                    setEndTime(typeActivitySelected);
                } else {
                    difficultyTask();
                }
            }
        });
        finishTaskPopup.show();
    }
}