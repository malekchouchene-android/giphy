package com.malek.giffy.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.malek.giffy.ui.home.GIFPreview

class GIFDetailsActivity : ComponentActivity() {
    companion object {
        const val GIF_TITLE_KEY = "gif_title_key"
        const val GIF_URL_KEY = "gif_url_key"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gifTitle = intent.extras?.getString(GIF_TITLE_KEY, "").orEmpty()
        val gifImageUrl = intent.extras?.getString(GIF_URL_KEY, "").orEmpty()
        setContent {
            AppTheme {
                Scaffold(topBar = {
                    CenterAlignedTopAppBar(title = {
                        Text(
                            text = gifTitle,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)

                        )
                    }, navigationIcon = {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clickable {
                                    finish()
                                }, contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                Modifier
                            )
                        }

                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        actions = {
                            Box(
                                Modifier
                                    .size(40.dp)
                                    .clickable {
                                        val shareIntent = Intent(Intent.ACTION_SEND)
                                        shareIntent.type = "text/plain"
                                        shareIntent.putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            "Sharing URL"
                                        )
                                        shareIntent.putExtra(
                                            Intent.EXTRA_TEXT, gifImageUrl
                                        )
                                        startActivity(
                                            Intent.createChooser(
                                                shareIntent,
                                                "Share URL"
                                            )
                                        )
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Partager GIF"
                                )
                            }
                        })
                }) {
                    GIFPreview(
                        previewUrl = gifImageUrl, modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentScale = ContentScale.Fit
                    )

                }
            }
        }

    }
}