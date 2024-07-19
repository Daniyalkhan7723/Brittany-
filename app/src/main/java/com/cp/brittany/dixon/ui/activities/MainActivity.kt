package com.cp.brittany.dixon.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cp.brittany.dixon.navigation.navGraphs.rootGraph.RootNavigationGraph
import com.cp.brittany.dixon.ui.theme.BrittanyDixonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenWithStatusBarWhiteIcon()
        setContent {
            BrittanyDixonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    ShowScreen()
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ShowScreen() {
    val navHostController = rememberNavController()
    RootNavigationGraph(navHostController = navHostController)
}

