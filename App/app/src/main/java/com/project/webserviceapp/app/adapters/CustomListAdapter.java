package com.project.webserviceapp.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.webserviceapp.R;
import com.project.webserviceapp.app.activities.MainActivity;
import com.project.webserviceapp.app.models.Personne;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Personne> implements View.OnClickListener{

    private ArrayList<Personne> dataSet;
    private Context mainContext;
    private MainActivity current;

    private TextView personneNom;
    private TextView personnePrenomAndEmail;
    private ImageButton buttonEditPersonne;
    private ImageButton buttonDeletePersonne;

    public CustomListAdapter(ArrayList<Personne> data, Context mainContext, MainActivity current) {
        super(mainContext, R.layout.list_item_layout, data);
        this.dataSet = data;
        this.mainContext = mainContext;
        this.current = current;
    }

    @Override
    public void onClick(View v) { }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Personne personne = getItem(position);

        View result;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
        personneNom = (TextView) convertView.findViewById(R.id.personneNomListItem);
        personnePrenomAndEmail = (TextView) convertView.findViewById(R.id.personnePrenomAndEmailListItem);
        buttonEditPersonne = (ImageButton) convertView.findViewById(R.id.buttonEditPersonne);
        buttonDeletePersonne = (ImageButton) convertView.findViewById(R.id.buttonDeletePersonne);
        
        result = convertView;

        Animation animation = AnimationUtils.loadAnimation(mainContext, R.anim.slide_animation);
        result.startAnimation(animation);

        if (personne.getNom().length() <= 24)
            personneNom.setText(personne.getNom());
        else
            personneNom.setText(personne.getNom().substring(0, 22) + "...");

        personnePrenomAndEmail.setText(personne.getPrenom() + "\n\n" +personne.getEmail());
        buttonEditPersonne.setTag(position);
        buttonDeletePersonne.setTag(position);

        buttonEditPersonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();
                Object object = getItem(position);
                Personne personne = (Personne)object;
                current.setCurrentView(R.layout.update_personne_layout);
                current.setContentView(current.getCurrentView());
                current.buildUpdateView(personne);
            }
        });
        buttonDeletePersonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();
                Object object = getItem(position);
                Personne personne = (Personne)object;
                current.showDeleteDialog(personne);
            }
        });

        return convertView;
    }
}