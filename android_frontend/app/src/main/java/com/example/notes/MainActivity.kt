package com.example.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.R

/**
 * PUBLIC_INTERFACE
 * MainActivity is the single-activity entry point hosting the NavHostFragment.
 * It sets up navigation for:
 * - NotesListFragment (list and search notes)
 * - NoteEditorFragment (create/edit a note)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme early
        setTheme(R.style.Theme_Notes) // fallback if alias exists; otherwise use Theme.Notes
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHost.navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
