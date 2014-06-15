package com.example.movesapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movesclass.Places;
import com.example.movesclass.Resultados;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Preguntas extends Activity {
	private TextView pregunta;
	private TextView tituloPreguntas;
	private RadioButton r1,r2,r3;
	private Button button;
	private String access_token;
	private String expires_in;
	private static String str_respuesta;
	private static Date fecha;
	private static Date inicio;
	private static Date fin;
	private static int contador = 1;
	private static List<Resultados> lista = new ArrayList<Resultados>();
	private ArrayList<Places> segments = new ArrayList<Places>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preguntas);
		final Context contexto = this;
		access_token = getIntent().getStringExtra("accessToken");
		expires_in = getIntent().getStringExtra("expires_in");
		
		pregunta = (TextView) findViewById(R.id.pregunta);
		tituloPreguntas = (TextView) findViewById(R.id.tituloPreguntas);
		r1 = (RadioButton) findViewById(R.id.radio0);
		r2 = (RadioButton) findViewById(R.id.radio1);
		r3 = (RadioButton) findViewById(R.id.radio2);
		button = (Button) findViewById(R.id.siguiente);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				contador++;
				if(contador > 10){
					String fin = new Gson().toJson(lista);
					Intent i = new Intent(contexto, Results.class);
					i.putExtra("JSON", fin);
					startActivity(i);
				}else{
				
					try {
						ejecutar(str_respuesta);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
            });
		
		
		
		
		HttpClient client = new DefaultHttpClient();
		int date = 201406;
        HttpGet request = new HttpGet("https://api.moves-app.com/api/1.1/user/places/daily/"+date+"?access_token="+access_token);
        HttpResponse respuesta,respuesta2;
		try {
			respuesta = client.execute(request);
	        str_respuesta = EntityUtils.toString(respuesta.getEntity());
	     //   Log.d("JSON",recorre.substring(2, 7));
	      //  str_respuesta = str_respuesta.substring(0, str_respuesta.length()-1)+","; 
	     //   String cadenaNueva2 = r.substring(1, str_respuesta2.length());
	      //  str_respuesta = "";
	     //   str_respuesta = cadenaNueva + cadenaNueva2;
	      //  Log.d("JSON",cadenaNueva);
	      //  Log.d("JSON2",cadenaNueva2);
		} catch (Exception e) {	
			Toast.makeText(this, "Ocurrio un error llamando a la API: " + e.toString() , Toast.LENGTH_LONG).show();
			//pregunta.setText(e.toString());
		}
		
		try {
			ejecutar(str_respuesta);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        

		
	}
	@SuppressLint("ShowToast")
	public void ejecutar(String respuesta) throws IOException{
		tituloPreguntas.setText("Pregunta "+ contador +" de 10");
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(respuesta).getAsJsonArray();

        

        for(JsonElement obj : jArray )
        {
            Places cse = gson.fromJson( obj , Places.class);
            segments.add(cse);
        }
        SimpleDateFormat ffecha =  new SimpleDateFormat("dd 'de' MMMM", new Locale("es_ES"));
		SimpleDateFormat fHora =  new SimpleDateFormat("HH:mm", new Locale("es_ES"));
		int aleat=0,aseg=0,af1=0,af2=0,asegf1=0,asegf2=0,salir=0;
		String verdadero = null,falso1 = null,falso2 = null;
		
		
		do{
			
			aleat = aleatorio(0,segments,0,0); 
			aseg = aleatorio(1,segments,aleat,0);
			try{
				verdadero = segments.get(aleat).getSegments().get(aseg).getPlace().getName();
			}catch(Exception e){
				Log.e("Verdadero" ,"Fallo en verdadero");
			}
			if(verdadero == null || verdadero.isEmpty()) {
				Log.e("nullVerdadero","nulo");
				verdadero = "";
			}
		}while( verdadero.length() < 2);
		
		
		do{
			af1 = aleatorio(2,segments,0,0);
			asegf1 = aleatorio(1,segments,af1,0);
			try{
			falso1=segments.get(af1).getSegments().get(asegf1).getPlace().getName();
			}catch(Exception e){
				Log.e("Falso1","nulo");
			}
			if(falso1 == null || falso1.isEmpty()){
				Log.e("nullFalso1","nulo"); 
				falso1 = "";
			}
		}while(falso1.length()<2 || falso1.equals(verdadero));
		
		do{
			af2 = aleatorio(2,segments,0,af1);	
			asegf2 = aleatorio(1,segments,af2,asegf1);	
			try{
			falso2 = segments.get(af2).getSegments().get(asegf2).getPlace().getName();
			}catch(Exception e){
				Log.e("Fallo","Fallo en falso2");
			}
			if(falso2 == null || falso2 == ""){
				Log.e("nullFalso2","nulo");
				falso2 = "";
			}
		}while(falso2.length()<2 || (falso2.equals(falso1) || falso2.equals(verdadero)));
		
		Log.e("salir","sali del bucle");

		
		try{
	
			fecha = new SimpleDateFormat("yyyyMMdd",Locale.FRANCE).parse( segments.get(aleat).getDate());
			inicio = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ",Locale.FRANCE).parse( segments.get(aleat).getSegments().get(aseg).getStartTime());
			fin = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ",Locale.FRANCE).parse( segments.get(aleat).getSegments().get(aseg).getEndTime());
		}catch(Exception e){
			Log.e("Fecha","Ocurrio un error con la fecha");
			pregunta.setText(e.toString());
		}
		
		Toast.makeText(this, ffecha.format(fecha) + " "+ fHora.format(inicio), Toast.LENGTH_LONG);
		pregunta.setText("¿Dónde estuvo el día "+ffecha.format(fecha)+" "+getDiaSemana(fecha)+" desde las "+fHora.format(inicio)+" hasta las "+fHora.format(fin)+"?");
		
		
		//validar primero verdadero
			
		int preguntas = (int)Math.round(Math.random()*3);
		int verdadera = 0;
		switch(preguntas){
		case 0:
			r1.setText(verdadero);
			r2.setText(falso1);
			r3.setText(falso2);
			verdadera = 1;
			break;
		case 1:
			r1.setText(falso1);
			r2.setText(verdadero);
			r3.setText(falso2);
			verdadera = 2;
			break;
		case 2:
			r1.setText(falso2);
			r2.setText(falso1);
			r3.setText(verdadero);
			verdadera = 3;
			break;
		case 3:
			r1.setText(verdadero);
			r2.setText(falso1);
			r3.setText(falso2);
			verdadera = 1;
			break;
		}

		if(r1.isChecked()){
			Resultados res = new Resultados(r1.getText().toString(),verdadero);
			lista.add(res);
		}
		if(r2.isChecked()){
			Resultados res = new Resultados(r2.getText().toString(),verdadero);
			lista.add(res);
		}
		if(r3.isChecked()){
			Resultados res = new Resultados(r3.getText().toString(),verdadero);
			lista.add(res);
		}
		
	}
	
	public int aleatorio(int tipo,ArrayList<Places> segmentos,int seleccionado, int anterior){
		int i = 0;
		switch(tipo){
		case 0: //aleatorio principal
		
				i = (int)Math.round(Math.random()*(segmentos.size()-1));
				if(i < 0) i = 0;
		
			break;
			
		case 1: //fechas de la pregunta
				if(segmentos.get(seleccionado).getSegments() != null){
					i = (int)Math.round(Math.random()*(segmentos.get(seleccionado).getSegments().size()-1));
					if(i < 0) i = 0;
			
				}
			break;
		
		case 2: //segmento aleatorio de falso 1
	
				i = (int)Math.round(Math.random()*(segmentos.size()-1));
				if(i < 0) i = 0;
		
			break;
			
		case 3: //fecha para pregunta falsa
			
				i = (int)Math.round(Math.random()*(segmentos.get(seleccionado).getSegments().size()-1));
				if(i < 0) i = 0;
			
			break;
		}
		
		return i;
	}
	
	public static String getDiaSemana(Date d){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		String dia = ""; 
		switch(cal.get(Calendar.DAY_OF_WEEK)){
		case 1:
			dia = "(Domingo)";
			break;
		case 2:
			dia = "(Lunes)";
			break;
		case 3:
			dia = "(Martes)";
			break;
		case 4:
			dia = "(Miercoles)";
			break;
		case 5:
			dia = "(Jueves)";
			break;
		case 6:
			dia = "(Viernes)";
			break;
		case 7:
			dia = "(Sabado)";
			break;
		
		}
		return dia;
	}
	
}
