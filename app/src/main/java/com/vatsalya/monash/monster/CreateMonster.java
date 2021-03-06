package com.vatsalya.monash.monster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vatsalya.monash.monster.models.Monster;
import com.vatsalya.monash.monster.utils.DatabaseHelper;

public class CreateMonster extends AppCompatActivity {

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText speciesEditText;
    private EditText attackPowerEditText;
    private EditText healthEditText;

    private Button updateButton;
    private Button saveButton;

    private TextView descriptionTextView;
    private TextView feedbackTextView;

    private Monster monster;
    private String callerName;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_monster);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        speciesEditText = (EditText) findViewById(R.id.speciesEditText);
        attackPowerEditText = (EditText) findViewById(R.id.attackPowerEditText);
        healthEditText = (EditText) findViewById(R.id.healthEditText);

        updateButton = (Button) findViewById(R.id.updateButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        feedbackTextView = (TextView) findViewById(R.id.feedbackTextView);

        Intent intent = getIntent();
        monster = intent.getParcelableExtra(getString(R.string.monster_parcel_id));
        callerName = intent.getStringExtra(getString(R.string.sender_class_name));

        if( callerName != null && callerName.equals( "SearchMonsters")) // If caller is listview
        {
            updateButton.setVisibility(View.INVISIBLE); //hide update button
        }
        if(callerName != null && callerName.equals("ViewMonster")) // If caller is View Monster
        {
            saveButton.setVisibility(View.INVISIBLE); //hide create button
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(monster != null) {
                updateMonster();
            } else {
                createMonster();
            }
            if(callerName != null && callerName.equals("ViewMonster")) {
                finish();
            }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMonster();
                if(callerName != null && callerName.equals("SearchMonsters")) {
                    finish();
                }
            }
        });

        // If monster passed into intet fill in edit text
        if (monster != null) {
            nameEditText.setText(monster.getName());
            ageEditText.setText(Integer.toString(monster.getAge()));
            speciesEditText.setText(monster.getSpecies());
            attackPowerEditText.setText(Integer.toString(monster.getAttackPower()));
            healthEditText.setText(Integer.toString(monster.getHealth()));
        }
    }

    /**
     * Creates a monster based on field values
     */
    private void createMonster() {
        monster = new Monster(nameEditText.getText().toString(),
                Integer.parseInt(ageEditText.getText().toString()),
                speciesEditText.getText().toString(),
                Integer.parseInt(attackPowerEditText.getText().toString()),
                Integer.parseInt(healthEditText.getText().toString())
        );
        long id = databaseHelper.addMonster(monster);
        if (id != -1) {
            monster.setId(id);
            descriptionTextView.setText(monster.toString());
            feedbackTextView.setText(getString(R.string.feedback_monster_created));
        } else {
            feedbackTextView.setText(getString(R.string.feedback_monster_failed));
        }

    }
    
    /**
     * Update the monster based on field values
     */
    private void updateMonster() {
        monster.setName(nameEditText.getText().toString());
        monster.setAge(Integer.parseInt(ageEditText.getText().toString()));
        monster.setSpecies(speciesEditText.getText().toString());
        monster.setAttackPower(Integer.parseInt(attackPowerEditText.getText().toString()));
        monster.setHealth(Integer.parseInt(healthEditText.getText().toString()));
        databaseHelper.updateMonster(monster);
        descriptionTextView.setText("");
        feedbackTextView.setText(getString(R.string.feedback_monster_updated));
    }
}
