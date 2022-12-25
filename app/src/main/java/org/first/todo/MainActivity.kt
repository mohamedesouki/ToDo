package org.first.todo

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.navHostfragment))
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.purple_700)))

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController= findNavController(R.id.navHostfragment)
        return navController.navigateUp()||super.onSupportNavigateUp()
    }
}