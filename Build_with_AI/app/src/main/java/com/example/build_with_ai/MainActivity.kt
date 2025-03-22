package com.example.build_with_ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        setContent {
            FakeNewsDetectorTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF293241)) // Header color
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp, bottom = 32.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        FakeNewsDetectorApp()
                    }
                }
            }
        }
    }
}

@Composable
fun FakeNewsDetectorApp() {
    var urlInput by remember { mutableStateOf("") }
    var verificationResult by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        // Header
        Header()

        // Content area (empty for now, will contain results)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            // This is where verification results would appear
            verificationResult?.let { result ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Analysis Result:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = result)
                }
            } ?: run {
                // Show placeholder when no result is available
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter a news URL or text to verify",
                        color = Color.Gray
                    )
                }
            }
        }

        // Input area
        InputArea(
            value = urlInput,
            onValueChange = { urlInput = it },
            onSendClick = {
            /* Handle verification logic here */
                if (urlInput.isNotBlank()) {
                    // Simple verification logic (you would replace this with actual verification)
                    verificationResult = analyzeInput(urlInput)
                    // Optionally clear the input field
                    urlInput = ""
                }}
        )
    }
}
fun analyzeInput(input: String): String {
    // This is just a placeholder. In a real app, you would call your verification API
    return if (input.contains("fake", ignoreCase = true)) {
        "This content appears to be POTENTIALLY FAKE. The text contains keywords that are often associated with misinformation."
    } else if (input.startsWith("http", ignoreCase = true)) {
        "Analyzing URL: $input\n\nOur AI analysis indicates this content is from a TRUSTED SOURCE with a confidence level of 87%."
    } else {
        "Analyzing text: \"$input\"\n\nOur analysis is INCONCLUSIVE. We need more context to determine reliability."
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF293241))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo part
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF1E2735)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FAKE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "NEWS",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Title part
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = "FAKE NEWS DETECTOR",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Your 24/7 AI assistant for detecting fake news.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun InputArea(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Type your message...") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp),
            maxLines = 1
        )

        Button(
            onClick = onSendClick,
            modifier = Modifier.height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Send")
        }
    }
}

// Theme setup
@Composable
fun FakeNewsDetectorTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF2196F3),
        secondary = Color(0xFF03DAC5),
        background = Color(0xFFF0F0F0)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Add this to your res/values/strings.xml
// <string name="app_name">Fake News Detector</string>

// For the Gradle setup, ensure you have:
// 1. The latest Compose dependencies
// 2. Kotlin version 1.7.0+
// 3. Compose compiler version matching your Kotlin version

/*
Example build.gradle additions:

dependencies {
    implementation "androidx.compose.ui:ui:1.3.0"
    implementation "androidx.compose.material3:material3:1.0.0"
    implementation "androidx.activity:activity-compose:1.6.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
}
*/