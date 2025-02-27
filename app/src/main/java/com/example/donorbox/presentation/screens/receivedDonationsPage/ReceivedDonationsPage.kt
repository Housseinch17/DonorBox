package com.example.donorbox.presentation.screens.receivedDonationsPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donorbox.R
import com.example.donorbox.presentation.theme.BodyTypography
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.SharedScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReceivedDonationsPage(
    modifier: Modifier,
    receivedDonationsUiState: ReceivedDonationsUiState,
    onActionReceivedDonationsAction: (ReceivedDonationsAction) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = receivedDonationsUiState.isRefreshing,
        onRefresh = { onActionReceivedDonationsAction(ReceivedDonationsAction.LoadNewOrders) },
    )
    SharedScreen{
        Box(
            modifier = modifier
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center
        ) {
            if (receivedDonationsUiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(200.dp), color = NewGray)
            } else {
                if (receivedDonationsUiState.receivedDonationsList.isEmpty() && !receivedDonationsUiState.isRefreshing) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                modifier = Modifier,
                                text = stringResource(R.string.no_donations_yet),
                                style = TitleTypography.copy(
                                    fontSize = 30.sp,
                                )
                            )
                        }
                    }
                } else {
                    ReceivedDonationsList(receivedDonationsUiState.receivedDonationsList)
                }
                // Adding the PullRefreshIndicator
                PullRefreshIndicator(
                    refreshing = receivedDonationsUiState.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun ReceivedDonationsList(list: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(list) { item ->
            ReceivedDonationsItem(item)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ReceivedDonationsItem(receivedDonations: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.5.dp),
        colors = CardDefaults.cardColors(containerColor = NewBlue)
    ) {
        Text(
            maxLines = 2,
            text = receivedDonations,
            modifier = Modifier.padding(12.dp),
            style = BodyTypography.copy(color = NewWhite),
            overflow = TextOverflow.Ellipsis
        )
    }
}