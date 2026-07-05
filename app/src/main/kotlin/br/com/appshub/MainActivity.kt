package br.com.appshub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.appshub.core.ui.theme.AppsHubTheme
import br.com.appshub.navigation.AppsHubApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppsHubTheme {
                AppsHubApp()
            }
        }
    }
}
