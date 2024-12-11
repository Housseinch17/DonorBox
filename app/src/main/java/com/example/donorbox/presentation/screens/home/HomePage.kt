package com.example.donorbox.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse
import com.example.donorbox.presentation.theme.BrightBlue
import com.example.donorbox.presentation.util.DonorBoxImage
import com.example.donorbox.presentation.util.ShimmerEffect

@Composable
fun HomePage(
    modifier: Modifier,
    response: ReceiversResponse,
    onReceiverClick: (Receiver) -> Unit
) {
    Box(
        modifier = modifier
            .background(BrightBlue)
            .padding(vertical = 10.dp, horizontal = 10.dp),
        contentAlignment = if (response is ReceiversResponse.Success) {
            Alignment.TopStart
        } else {
            Alignment.Center
        }
    ) {
            when (response) {
                is ReceiversResponse.Error -> {
                    Text(
                        text = response.message,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                    )
                }

                ReceiversResponse.IsLoading -> {
                    ShimmerReceiverList()
                }

                is ReceiversResponse.Success -> {
                    ReceiverList(receiverList = response.receivers) {
                        onReceiverClick(it)
                    }
                }
        }
    }
}

@Composable
fun ShimmerReceiverList() {
    val imageTopPadding: Dp = 40.dp
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(imageTopPadding + 20.dp),
        contentPadding = PaddingValues(top = imageTopPadding, bottom = 20.dp)
    ) {
        items(6) { // Display a fixed number of shimmer items
            Spacer(modifier = Modifier.height(imageTopPadding))
            ShimmerReceiverComponent(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ShimmerReceiverComponent(modifier: Modifier) {
    ConstraintLayout(modifier = modifier.padding(8.dp)) {
        val (card, image) = createRefs()

        // Shimmer Card Placeholder
        ShimmerEffect(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .constrainAs(card) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
                .height(150.dp)
        )

        // Shimmer Image Placeholder
        ShimmerEffect(
            modifier = Modifier
                .padding(10.dp)
                .width(100.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .constrainAs(image) {
                    top.linkTo(card.top)
                    bottom.linkTo(card.top)
                    start.linkTo(card.start)
                    end.linkTo(card.end)
                }
        )
    }
}

@Composable
fun ReceiverList(
    receiverList: List<Receiver>,
    onReceiverClick: (Receiver) -> Unit
) {
    val imageTopPadding: Dp = 40.dp
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(imageTopPadding + 20.dp),
        contentPadding = PaddingValues(top = imageTopPadding, bottom = 20.dp)
    ) {
        items(receiverList) { receiver ->
            Spacer(modifier = Modifier.height(imageTopPadding))
            ReceiverComponent(modifier = Modifier.fillMaxWidth(), receiver = receiver) {
                onReceiverClick(receiver)
            }
        }
    }
}

@Composable
fun ReceiverComponent(modifier: Modifier, receiver: Receiver, onReceiverClick: (Receiver) -> Unit) {
    ConstraintLayout {
        val (card, image) = createRefs()
        ReceiverInfo(
            modifier = modifier.constrainAs(card) {
                top.linkTo(parent.top)
            }, receiver = receiver,
            onReceiverClick = onReceiverClick
        )
        DonorBoxImage(
            modifier = Modifier
                .padding(10.dp)
                .width(100.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .constrainAs(image) {
                    top.linkTo(card.top)
                    bottom.linkTo(card.top)
                    start.linkTo(card.start)
                    end.linkTo(card.end)
                },
            imageUrl = receiver.picUrl
        )
    }
}

@Composable
fun ReceiverInfo(modifier: Modifier, receiver: Receiver, onReceiverClick: (Receiver) -> Unit) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                onReceiverClick(receiver)
            },
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp, bottom = 10.dp,
                    start = 8.dp, end = 8.dp
                ),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            ReceiverText(
                text = "Name: ",
                description = receiver.name,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = BrightBlue,
                    fontSize = 14.sp,
                )
            )
            ReceiverText(
                text = "Address: ",
                description = receiver.address,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = BrightBlue,
                    fontSize = 14.sp,
                )
            )
            ReceiverText(
                text = "Omt: ",
                description = receiver.omt,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = BrightBlue,
                    fontSize = 12.sp
                )
            )
            ReceiverText(
                text = "Whish: ",
                description = receiver.whish,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = BrightBlue,
                    fontSize = 12.sp
                )
            )
            ReceiverText(
                text = "Bank iban: ",
                description = receiver.bank,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = BrightBlue,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun ReceiverText(
    text: String, description: String, textStyle: TextStyle,
) {
    val horizontalScrollState = rememberScrollState()
    Row(
        Modifier.horizontalScroll(state = horizontalScrollState),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier,
            text = text,
            maxLines = 1,
            style = textStyle.copy(lineHeight = 18.sp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            modifier = Modifier,
            text = description,
            maxLines = 1,
            style = textStyle.copy(
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }

}