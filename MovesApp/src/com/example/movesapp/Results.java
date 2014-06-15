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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Results extends Activity {
	private String jsonPreguntas;
	private TextView texto;
	private ListView lista;
	private ArrayList<String> datos = new ArrayList<String>();
	private ArrayAdapter<String> adaptador;
	private List<Resultados> r = new ArrayList<Resultados>();
	private static int correctos;
	@Override
	public void onBackPressed(){
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		texto = (TextView) findViewById(R.id.Resultados);
		lista = (ListView) findViewById(R.id.listView1);
		
		jsonPreguntas = getIntent().getStringExtra("JSON");
		Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonPreguntas).getAsJsonArray();
        for(JsonElement obj : jArray )
        {
            Resultados cse = gson.fromJson( obj , Resultados.class);
            r.add(cse);
        }
        
        int pregunta = 1;
        
        for(Resultados ind : r){
        	String elemento = "\nPregunta "+pregunta+"\n" +
        			"Correcta: "+ind.getVerdadera()+"\n" +
        			"Contestada: "+ind.getContestado()+"\n";
        	datos.add(elemento);
        	
        	Boolean esCorrecto = false;
        	
        	if(ind.getContestado().equals(ind.getVerdadera())) {
        		correctos++;
        		esCorrecto = true;
        	}
        	pregunta++;
        }
		texto.setText("Has contestado correctamente "+correctos+" de 10");
		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datos);
		lista.setAdapter(adaptador);
	}
}
