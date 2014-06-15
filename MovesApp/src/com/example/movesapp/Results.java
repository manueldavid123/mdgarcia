package com.example.movesapp;

import java.util.ArrayList;
import java.util.List;

import com.example.movesclass.Places;
import com.example.movesclass.Resultados;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Results extends Activity {
	private String jsonPreguntas;
	private TextView texto;
	private List<Resultados> r = new ArrayList<Resultados>();
	private static int correctos;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		texto = (TextView) findViewById(R.id.Resultados);
		
		jsonPreguntas = getIntent().getStringExtra("JSON");
		Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonPreguntas).getAsJsonArray();
        for(JsonElement obj : jArray )
        {
            Resultados cse = gson.fromJson( obj , Resultados.class);
            r.add(cse);
        }
        
        for(Resultados ind : r){
        	Boolean esCorrecto = false;
        	if(ind.getContestado().equals(ind.getVerdadera())) {
        		correctos++;
        		esCorrecto = true;
        	}
        	
        }
		texto.setText("Has contestado correctamente "+correctos+" de 10");
	}
}
