package com.project.webserviceapp.app.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.webserviceapp.R;
import com.project.webserviceapp.app.adapters.CustomListAdapter;
import com.project.webserviceapp.app.controllers.PersonnesController;
import com.project.webserviceapp.app.models.Personne;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private int currentView;
    private Context mainContext = this;
    private ModalActivity modalActivity;

    public int getCurrentView() {
        return currentView;
    }

    public void setCurrentView(int currentView) {
        this.currentView = currentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        currentView = R.layout.activity_main;
        setContentView(currentView);

        try {
            buildMainView();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
            System.exit(0012);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
            System.exit(0014);
        }
    }

    public void buildMainView() throws NoSuchMethodException {
        callGetAll();

        final BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        final FloatingActionButton floatingActionButton = findViewById(R.id.button);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);

        final ImageButton imageButton = (ImageButton) findViewById(R.id.searchButton);

        final View search = (View) findViewById(R.id.search);

        final EditText editText = (EditText) findViewById(R.id.searchEmail);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.containerSearch);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    buildMainView();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                    System.exit(0014);
                }
            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Method method = null;
                try {
                    method = MainActivity.class.getDeclaredMethod("showDeleteAllDialog");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                }
                modalActivity = new ModalActivity(MainActivity.this, MainActivity.this, method);
                modalActivity.show(getSupportFragmentManager(), modalActivity.getTag());
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = R.layout.register_personne_layout;
                setContentView(currentView);
                buildRegisterView();
            }
        });

        coordinatorLayout.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coordinatorLayout.getVisibility() == View.INVISIBLE) {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
                else {
                    coordinatorLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().trim().equals("")){
                    Method method = null;
                    try {
                        method = MainActivity.class.getDeclaredMethod("setPersonneResult");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                        System.exit(0012);
                    }

                    PersonnesController.getPersonne(editText.getText().toString().trim(), mainContext, MainActivity.this, method);
                }
            }
        });
    }

    public void setPersonneResult() {
        if(PersonnesController.getPersonne().get(0) == null)
            Toast.makeText(mainContext, "Personne not found", Toast.LENGTH_SHORT).show();
        else {
            ListView listView = findViewById(R.id.listView);

            listView.setAdapter(null);

            CustomListAdapter listViewAdapter = new CustomListAdapter(PersonnesController.getPersonne(), mainContext, this);

            listView.setAdapter(listViewAdapter);

            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.containerSearch);
            coordinatorLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void callGetAll() throws NoSuchMethodException {
        Method method = MainActivity.class.getDeclaredMethod("setContentOnListView");
        PersonnesController.getAllPersonnesRetrofit(mainContext, this, method);
    }

    public void setContentOnListView() {

        if(PersonnesController.getPersonneList().get(0) == null) {
            Toast.makeText(mainContext, "Personne not found", Toast.LENGTH_SHORT).show();
        }
        else {
            ListView listView = findViewById(R.id.listView);

            listView.setAdapter(null);

            CustomListAdapter listViewAdapter = new CustomListAdapter(PersonnesController.getPersonneList(), mainContext, this);

            listView.setAdapter(listViewAdapter);
        }
    }

    public void buildRegisterView() {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.insert_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final EditText prenom = (EditText) findViewById(R.id.personnePrenom);
            final EditText nom = (EditText) findViewById(R.id.personneNom);
            final EditText email = (EditText) findViewById(R.id.personneEmail);
            final EditText naissance = (EditText) findViewById(R.id.personneNaissance);

            if (nom.getText().toString().trim().length() == 0 || prenom.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0) {
                Toast.makeText(mainContext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            PersonnesController.insertPersonne(mainContext, prenom.getText().toString().trim(), nom.getText().toString().trim(), email.getText().toString().trim(), new Date(naissance.getText().toString().trim()));

            prenom.setText("");
            nom.setText("");
            email.setText("");
            naissance.setText("");

            }
        });
    }

    public void buildUpdateView(Personne personne) {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.update_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText prenom = (EditText) findViewById(R.id.personnePrenomUpdate);
                final EditText nom = (EditText) findViewById(R.id.personneNomUpdate);
                final EditText email = (EditText) findViewById(R.id.personneEmailUpdate);
                final EditText naissance = (EditText) findViewById(R.id.personneNaissanceUpdate);

                if (nom.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0) {
                    Toast.makeText(mainContext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                PersonnesController.updatePersonne(mainContext, prenom.getText().toString().trim(), nom.getText().toString().trim(), email.getText().toString().trim(), new Date(naissance.getText().toString().trim()));

                currentView = R.layout.activity_main;
                setContentView(currentView);

                try {
                    buildMainView();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                    System.exit(0014);
                }
            }
        });

        DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");

        final EditText prenom = (EditText) findViewById(R.id.personnePrenomUpdate);
        prenom.setText(personne.getPrenom());

        final EditText nom = (EditText) findViewById(R.id.personneNomUpdate);
        nom.setText(personne.getNom());

        final EditText email = (EditText) findViewById(R.id.personneEmailUpdate);
        email.setText(personne.getEmail());

        final EditText naissance = (EditText) findViewById(R.id.personneNaissanceUpdate);
        naissance.setText(formatter.format(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (currentView == R.layout.register_personne_layout || currentView == R.layout.update_personne_layout) {
            currentView = R.layout.activity_main;
            setContentView(currentView);

            try {
                buildMainView();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                System.exit(0012);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                System.exit(0014);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    public void showDeleteDialog(final Personne personne) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage("Are you sure you want to delete this personne? This action can not be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersonnesController.deletePersonne(mainContext, personne.getEmail());
                        try {
                            buildMainView();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                            System.exit(0012);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                            System.exit(0014);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .show();
    }

    public void showDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage("Are you sure you want to delete all personnes? This action can not be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modalActivity.dismiss();
                        PersonnesController.deleteAllPersonnes(mainContext);
                        try {
                            buildMainView();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                            System.exit(0012);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                            System.exit(0014);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modalActivity.dismiss();
                    }
                })
                .show();
    }
}