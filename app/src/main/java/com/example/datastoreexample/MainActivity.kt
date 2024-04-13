package com.example.datastoreexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Extensión para obtener una instancia de DataStore asociada al contexto de la aplicación.
val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES_NAME")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura la vista del activity desde el layout XML.
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz de usuario en el layout.
        val btnSave = findViewById<Button>(R.id.btnSave)
        val etName = findViewById<EditText>(R.id.etName)
        val cbVIP = findViewById<CheckBox>(R.id.cbVIP)

        // Configuración del evento click para el botón de guardar.
        btnSave.setOnClickListener {
            // Lanzar una coroutine en el scope del lifecycle del Activity en el dispatcher de IO.
            lifecycleScope.launch(Dispatchers.IO) {
                // Guardar los valores introducidos en el DataStore.
                saveValues(etName.text.toString(), cbVIP.isChecked)
            }
            // Iniciar una nueva activity una vez que se ha hecho clic en el botón.
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }

    // Función suspendida para guardar los valores de preferencias del usuario.
    private suspend fun saveValues(name: String, checked: Boolean) {
        dataStore.edit { preferences ->
            // Asignar un nuevo valor a la clave 'name'.
            preferences[stringPreferencesKey("name")] = name
            // Asignar un nuevo valor a la clave 'vip'.
            preferences[booleanPreferencesKey("vip")] = checked
        }
    }
}
