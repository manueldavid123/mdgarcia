package com.example.movesapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movesclass.Places;
import com.example.movesclass.Segments;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Preguntas extends Activity {
	private TextView pregunta;
	private RadioButton r1,r2,r3;
	private List<String> pyr;
	private String access_token;
	private String expires_in;
	private List<Segments> s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preguntas);
		
		access_token = getIntent().getStringExtra("accessToken");
		expires_in = getIntent().getStringExtra("expires_in");
		
		pregunta = (TextView) findViewById(R.id.pregunta);
		r1 = (RadioButton) findViewById(R.id.radio0);
		r2 = (RadioButton) findViewById(R.id.radio1);
		r3 = (RadioButton) findViewById(R.id.radio2);

		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://api.moves-app.com/api/1.1/user/places/daily/201404?access_token="+access_token);
        HttpResponse respuesta;
		try {
			respuesta = client.execute(request);
	        String str_respuesta = EntityUtils.toString(respuesta.getEntity());
	       
	        Gson gson = new Gson();
	        JsonParser parser = new JsonParser();
	        JsonArray jArray = parser.parse(str_respuesta).getAsJsonArray();

	        ArrayList<Places> segments = new ArrayList<Places>();

	        for(JsonElement obj : jArray )
	        {
	            Places cse = gson.fromJson( obj , Places.class);
	            segments.add(cse);
	        }
			int aleat = (int) Math.round(Math.random()*segments.size());
			int af1 = (int) Math.round(Math.random()*segments.size());
			int af2 = (int) Math.round(Math.random()*segments.size());
			SimpleDateFormat ffecha =  new SimpleDateFormat("dd 'de' MMMM", new Locale("es_ES"));
			SimpleDateFormat fHora =  new SimpleDateFormat("HH:mm", new Locale("es_ES"));
			
			Date fecha = new SimpleDateFormat("yyyyMMdd",Locale.FRANCE).parse( segments.get(aleat).getDate());
			Date inicio = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ",Locale.FRANCE).parse( segments.get(aleat).getSegments().get(0).getStartTime());
			Date fin = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ",Locale.FRANCE).parse( segments.get(aleat).getSegments().get(0).getEndTime());
			
			pregunta.setText("¿Qué estuvo haciendo el día "+ffecha.format(fecha)+" desde las "+fHora.format(inicio)+" hasta las "+fHora.format(fin)+"?");
			
			r1.setText(segments.get(aleat).getSegments().get(0).getPlace().getName());
			r2.setText(segments.get(af1).getSegments().get(0).getPlace().getName());
			r3.setText(segments.get(af2).getSegments().get(0).getPlace().getName());
		
		} catch (Exception e) {	
			Toast.makeText(this, "Ocurrio un error: " + e.toString() , Toast.LENGTH_LONG).show();
			pregunta.setText(e.toString());
		}

        

		
	}

}
