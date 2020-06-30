package com.example.pruba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pruba.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Persona> listPerson =new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nomP, appP, correoP, passwordP;
    ListView listV_personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP=findViewById(R.id.nombrePersona);
        appP= findViewById(R.id.apellidoPersona);
        correoP= findViewById(R.id.correoPersona);
        passwordP= findViewById(R.id.passwordPersona);

        listV_personas= findViewById(R.id.lv_datosPersonas);

        inicializarFirebase();

        listarDAtos();

    }

    private void listarDAtos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre= nomP.getText().toString();
        String apellido= appP.getText().toString();
        String correo= correoP.getText().toString();
        String password = passwordP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if(nombre.equals("")){
                    validacion();

                }else {
                    Persona p= new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setCorreo(correo);
                    p.setPassword(password);

                    databaseReference.child("Persona").child(p.getUid()).setValue(p);

                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete:{
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nomP.setText("");
        appP.setText("");
        correoP.setText("");
        passwordP.setText("");
    }

    private void validacion() {
        String nombre = nomP.getText().toString();
        String apellido= appP.getText().toString();
        String correo= correoP.getText().toString();
        String password = passwordP.getText().toString();
        if(nombre.equals("")){
            nomP.setError("Required");

        }else if(apellido.equals("")){
            appP.setError("Required");

        }else if(correo.equals("")){
            correoP.setError("Required");

        }else if(password.equals("")){
            passwordP.setError("Required");
        }
    }
}