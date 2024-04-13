package com.example.datastoreexample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        val backgroundView = findViewById<View>(R.id.viewBackground)
        val tvName = findViewById<TextView>(R.id.tvName)

        lifecycleScope.launch(Dispatchers.IO) {
            getUserProfile().collect{
                withContext(Dispatchers.Main){
                    tvName.text = it.name
                    if(it.vip){
                        backgroundView.setBackgroundResource(R.color.gold)
                    }
                }
            }
        }
    }

    private fun getUserProfile() = dataStore.data.map { preferences ->
        UserProfile(
            name = preferences[stringPreferencesKey("name")].orEmpty(),
            vip = preferences[booleanPreferencesKey("vip")] ?: false
        )
    }
}