package com.example.donorbox.presentation.screens.home

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.donorbox.R
import com.example.donorbox.data.model.Receiver
import com.example.donorbox.presentation.sealedInterfaces.ReceiversResponse
import com.example.donorbox.presentation.theme.BrightBlue
import com.example.donorbox.presentation.theme.DescriptionTypography
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.util.DonorBoxImage
import com.example.donorbox.presentation.util.PasswordTextField
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.ShimmerEffect
import com.example.donorbox.presentation.util.TrailingIcon
import com.example.donorbox.presentation.util.getPasswordVisualTransformation

@Composable
fun HomePage(
    modifier: Modifier,
    response: ReceiversResponse,
    onReceiverClick: (Receiver) -> Unit,
    modalBottomSheetReceiver: ModalBottomSheetReceiver, hideBottomSheetReceiver: () -> Unit,
    onCall: (String) -> Unit,
    onOpenApp: () -> Unit,
    onOpenWhishApp: () -> Unit,
    onOpenGoogleMap: (Double, Double) -> Unit,
    onSendButton: (receiverToken: String, receiverUsername: String) -> Unit,
    sendMoney: (moneyToDonate: String, password: String) -> Unit,
    showDialog: Boolean,
    hideDialog: () -> Unit,
    moneyToDonate: String,
    onMoneyUpdate: (String) -> Unit,
    isLoading: Boolean,
    showText: Boolean,
    newPasswordValue: String,
    showPassword: Boolean,
    newPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    SharedScreen(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
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
                    HomeSuccess(
                        receiverList = response.receivers,
                        onReceiverClick = {
                            onReceiverClick(it)
                        },
                        modalBottomSheetReceiver = modalBottomSheetReceiver,
                        hideBottomSheetReceiver = { hideBottomSheetReceiver() },
                        onCall = onCall,
                        onOpenApp = onOpenApp,
                        onOpenWhishApp = onOpenWhishApp,
                        onOpenGoogleMap = onOpenGoogleMap,
                        onSendButton = onSendButton,
                        sendMoney = sendMoney,
                        showDialog = showDialog,
                        hideDialog = hideDialog,
                        moneyToDonate = moneyToDonate,
                        onMoneyUpdate = onMoneyUpdate,
                        isLoading = isLoading,
                        showText = showText,
                        newPasswordValue = newPasswordValue,
                        showPassword = showPassword,
                        newPasswordValueChange = newPasswordValueChange,
                        imageVector = imageVector,
                        onIconClick = onIconClick,
                    )
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
                .height(200.dp)
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
fun HomeSuccess(
    receiverList: List<Receiver>,
    onReceiverClick: (Receiver) -> Unit,
    modalBottomSheetReceiver: ModalBottomSheetReceiver, hideBottomSheetReceiver: () -> Unit,
    onCall: (String) -> Unit,
    onOpenApp: () -> Unit,
    onOpenWhishApp: () -> Unit,
    onOpenGoogleMap: (Double, Double) -> Unit,
    onSendButton: (receiverToken: String, receiverUsername: String) -> Unit,
    sendMoney: (moneyToDonate: String, password: String) -> Unit,
    showDialog: Boolean,
    hideDialog: () -> Unit,
    moneyToDonate: String,
    onMoneyUpdate: (String) -> Unit,
    isLoading: Boolean,
    showText: Boolean, newPasswordValue: String,
    showPassword: Boolean,
    newPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        ShowDialog(
            showDialog = showDialog,
            confirmButton = { sendMoney(moneyToDonate, newPasswordValue) },
            onDismissButton = hideDialog,
            moneyToDonate = moneyToDonate,
            onMoneyUpdate = onMoneyUpdate,
            isLoading = isLoading,
            showText = showText,
            newPasswordValue = newPasswordValue,
            showPassword = showPassword,
            newPasswordValueChange = newPasswordValueChange,
            imageVector = imageVector,
            onIconClick = onIconClick,
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ReceiverList(receiverList, onReceiverClick)
            PartialBottomSheet(
                modalBottomSheetReceiver, hideBottomSheetReceiver,
                onCall = onCall,
                onOpenApp = onOpenApp,
                onOpenWhishApp = onOpenWhishApp,
                onOpenGoogleMap = onOpenGoogleMap,
                onSendButton = onSendButton
            )
        }
    }
}


//Using ModalBottomSheet is simpler sometimes
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(
    modalBottomSheetReceiver: ModalBottomSheetReceiver, hideBottomSheetReceiver: () -> Unit,
    onCall: (String) -> Unit,
    onOpenApp: () -> Unit,
    onOpenWhishApp: () -> Unit,
    onOpenGoogleMap: (Double, Double) -> Unit,
    onSendButton: (receiverToken: String, receiverUsername: String) -> Unit,
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //skipPartiallyExpanded here it means to expand alone or when i drag it up
    //its like not fill max height just take the height im giving to screen
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val verticalScroll = rememberScrollState()

    val receiver by derivedStateOf {
        modalBottomSheetReceiver.modalBottomSheetReceiver
    }

    if (modalBottomSheetReceiver.showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier,
            sheetState = sheetState,
            onDismissRequest = { hideBottomSheetReceiver() },
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 4.dp,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = NewBlue
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                NewBlue.copy(alpha = 0.7f),
                                NewBlue.copy(alpha = 0.5f),
                                NewBlue.copy(alpha = 0.3f),
                                NewWhite.copy(alpha = 0.3f),
                                NewWhite.copy(alpha = 0.5f),
                                NewWhite.copy(alpha = 0.7f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(verticalScroll),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DonorBoxImage(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        imageUrl = receiver.picUrl
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ModalBottomSheetItem(prefixText = "Name", text = receiver.name)
                    ModalBottomSheetItem(prefixText = "Bank", text = receiver.bank)
                    ModalBottomSheetItem(
                        prefixText = "Phone",
                        text = receiver.phoneNumber
                    ) {
                        ModalBottomSheetIconButton(
                            imageVector = Icons.Filled.Call,
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            onCall(receiver.phoneNumber)
                        }
                    }
                    ModalBottomSheetItem(prefixText = "Omt", text = receiver.omt) {
                        ModalBottomSheetIconButton(
                            imageVector = ImageVector.vectorResource(R.drawable.omt),
                            color = MaterialTheme.colorScheme.secondary, onClick = onOpenApp
                        )
                    }
                    ModalBottomSheetItem(prefixText = "Whish ", text = receiver.whish) {
                        ModalBottomSheetIconButton(
                            imageVector = ImageVector.vectorResource(R.drawable.whish),
                            color = MaterialTheme.colorScheme.tertiary, onClick = onOpenWhishApp
                        )
                    }
                    ModalBottomSheetItem(
                        prefixText = "Address",
                        text = receiver.address.location
                    ) {
                        ModalBottomSheetIconButton(
                            imageVector = ImageVector.vectorResource(R.drawable.googlemap),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            onClick = {
                                onOpenGoogleMap(
                                    receiver.address.latitude,
                                    receiver.address.longitude
                                )
                            }
                        )
                    }
                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            onSendButton(receiver.token, receiver.username)
                            keyboardController?.hide()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
                    ) {
                        Text(
                            text = "Send Money",
                            style = MaterialTheme.typography.titleMedium.copy(color = NewWhite)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ModalBottomSheetIconButton(
    imageVector: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .clip(CircleShape)
            .border(
                color = color,
                width = 1.dp,
                shape = CircleShape
            )
            .size(48.dp),
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            imageVector = imageVector, contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ModalBottomSheetItem(
    prefixText: String,
    text: String,
    content: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    Row(
        modifier = Modifier.height(50.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier
                .widthIn(80.dp)
                .horizontalScroll(rememberScrollState()),
            text = "$prefixText: ",
            style = DescriptionTypography.copy(color = Color.Black, fontWeight = FontWeight.Bold),
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 2.dp)
                .horizontalScroll(rememberScrollState())
                .clickable {
                    val clip = android.content.ClipData.newPlainText("Copied Text", text)
                    clipboardManager.setPrimaryClip(clip)
                    Toast
                        .makeText(context, "$prefixText copied", Toast.LENGTH_SHORT)
                        .show()
                },
            text = text,
            style = DescriptionTypography.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(10.dp))
        content?.let {
            it()
        }
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
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onReceiverClick(receiver)
            },
        colors = CardDefaults.cardColors(containerColor = NewGray),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp, bottom = 10.dp,
                    start = 8.dp, end = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ReceiverText(
                text = "Name: ",
                description = receiver.name,
                textStyle = DescriptionTypography
            )
            ReceiverText(
                text = "Phone: ",
                description = receiver.phoneNumber,
                textStyle = DescriptionTypography
            )
            ReceiverText(
                text = "Address: ",
                description = receiver.address.location,
                textStyle = DescriptionTypography
            )
            ReceiverText(
                text = "Omt: ",
                description = receiver.omt,
                DescriptionTypography.copy(fontSize = 14.sp)
            )
            ReceiverText(
                text = "Whish: ",
                description = receiver.whish,
                DescriptionTypography.copy(fontSize = 14.sp)
            )
            ReceiverText(
                text = "Bank iban: ",
                description = receiver.bank,
                DescriptionTypography.copy(fontSize = 14.sp)
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
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.widthIn(60.dp),
            text = text,
            maxLines = 1,
            style = textStyle.copy(lineHeight = 20.sp),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier.fillMaxWidth(1f),
            text = description,
            maxLines = 1,
            style = textStyle.copy(
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NewWhite
            ),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialog(
    showDialog: Boolean,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit,
    moneyToDonate: String,
    onMoneyUpdate: (String) -> Unit,
    isLoading: Boolean,
    showText: Boolean,
    newPasswordValue: String,
    showPassword: Boolean,
    newPasswordValueChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BrightBlue, RoundedCornerShape(20.dp)),
            containerColor = Color.Black.copy(alpha = 0.85f),
            onDismissRequest = {},
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    onClick = confirmButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrightBlue,
                        disabledContainerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.send_money), color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissButton,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = BrightBlue,
                        disabledContainerColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, BrightBlue)
                ) {
                    Text(
                        text = stringResource(R.string.dismiss),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Donate money \n",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = BrightBlue,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp),
                            color = Color.Red,
                            strokeWidth = 4.dp
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            value = moneyToDonate,
                            onValueChange = {
                                onMoneyUpdate(it)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = {
                                Text("Enter the amount")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Black.copy(alpha = 0.1f),
                                focusedIndicatorColor = BrightBlue,
                                unfocusedIndicatorColor = Color.Gray,
                                focusedTextColor = NewWhite,
                                unfocusedTextColor = NewWhite
                            )
                        )
                        if (showText) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.amount_empty),
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Light
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        PasswordTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.password), value = newPasswordValue,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = getPasswordVisualTransformation(showPassword),
                            onValueChange = newPasswordValueChange,
                            trailingIcon = {
                                TrailingIcon(imageVector = imageVector, onIconClick = onIconClick)
                            }
                        )
                    }
                }
            })
    }
}