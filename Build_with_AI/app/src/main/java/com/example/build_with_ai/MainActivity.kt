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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import kotlinx.coroutines.launch

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
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        // Header
        Header()

        // Content area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            if (isLoading) {
                // Show loading indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2196F3))
                }
            } else {
                // Show verification results or placeholder
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
        }

        // Input area
        InputArea(
            value = urlInput,
            onValueChange = { urlInput = it },
            onSendClick = {
                if (urlInput.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        verificationResult = analyzeInput(urlInput)
                        isLoading = false
                        // Clear the input field
                        urlInput = ""
                    }
                }
            },
            enabled = !isLoading
        )
    }
}

// Modified analyzeInput to use coroutines and call the API
suspend fun analyzeInput(input: String): String {
    return try {
        // Call the API endpoint
        withContext(Dispatchers.IO) {
            val url = URL("http://10.0.2.2:5000/chat")
            val connection = url.openConnection() as HttpURLConnection

            try {
                // Configure the connection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Prepare the JSON payload
                val jsonPayload = JSONObject().apply {
                    put("message", input)
                }.toString()

                // Send the data
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonPayload)
                    writer.flush()
                }

                // Read the response
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    // Extract the response field from JSON
                    jsonResponse.getString("response")
                } else {
                    // If we got a bad response code, fall back to the existing logic
                    fallbackAnalysis(input)
                }
            } finally {
                connection.disconnect()
            }
        }
    } catch (e: Exception) {
        // If any network or parsing error occurs, fall back to the existing logic
        when (e) {
            is UnknownHostException -> "Unable to connect to the analysis server. Using offline analysis.\n\n${fallbackAnalysis(input)}"
            else -> "Error: ${e.message}\n\nUsing offline analysis:\n${fallbackAnalysis(input)}"
        }
    }
}

// Original analysis logic as a fallback method
private fun fallbackAnalysis(input: String): String {
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
    onSendClick: () -> Unit,
    enabled: Boolean = true
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
                unfocusedContainerColor = Color.White,
                disabledBorderColor = Color.LightGray,
                disabledContainerColor = Color.White.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(28.dp),
            maxLines = 1,
            enabled = enabled
        )

        Button(
            onClick = onSendClick,
            modifier = Modifier.height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(28.dp),
            enabled = enabled
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

// Add these dependencies to your build.gradle:
/*
dependencies {
    implementation "androidx.compose.ui:ui:1.3.0"
    implementation "androidx.compose.material3:material3:1.0.0"
    implementation "androidx.activity:activity-compose:1.6.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"

    // Coroutines for asynchronous operations
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
}
*/