package com.example.donorbox.presentation.screens.mydonations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donorbox.presentation.theme.BrightBlue

@Composable
fun MyDonationPage(isLoading: Boolean, list: List<String>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(200.dp))
        }else{
            MyDonationList(list)
        }
    }
}

@Composable
fun MyDonationList(list: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        items(list) { item ->
            MyDonationItem(item)
        }
    }
}

@Composable
fun MyDonationItem(donation: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.5.dp),
        colors = CardDefaults.cardColors(containerColor = BrightBlue)
    ) {
        Text(
            text = donation,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White, fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}