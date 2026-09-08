package com.example.apodpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject
import root_component.RootContent
import root_component.integration.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val rootComponentFactory: RootComponent.Factory by inject()
        val component = rootComponentFactory(
            defaultComponentContext()
        )
        setContent {
            RootContent(component = component)
        }
    }
}
