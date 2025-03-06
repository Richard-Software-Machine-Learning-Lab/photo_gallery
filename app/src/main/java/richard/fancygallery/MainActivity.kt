package richard.fancygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import richard.fancygallery.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // standard bottom navigation
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHost
        binding.bottomNav.setupWithNavController(navHost.navController)

        // to avoid the bottom navigation from taking control of all navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navHost.navController)
            true
        }
        setContentView(binding.root)
    }
}