package com.example.donorbox.presentation.screens.contactUs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.donorbox.R
import com.example.donorbox.data.model.ContactUs
import com.example.donorbox.presentation.theme.NewBlue
import com.example.donorbox.presentation.theme.NewGray
import com.example.donorbox.presentation.theme.NewWhite
import com.example.donorbox.presentation.theme.TitleTypography
import com.example.donorbox.presentation.util.SharedScreen
import com.example.donorbox.presentation.util.TextFieldInput

@Composable
fun ContactUsPage(
    modifier: Modifier,
    contactUsUiState: ContactUsUiState,
    onActionContactUs: (ContactUsAction) -> Unit,
    textPage: String
) {
    SharedScreen {
        Box(modifier = modifier) {
            if (contactUsUiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(200.dp), color = NewGray)
                }
            } else {
                Column(
                    modifier = modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        text = textPage,
                        style = TitleTypography.copy(color = NewWhite),
                    )
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(0.6f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            TextFieldInput(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(R.string.name),
                                value = contactUsUiState.name,
                                imageVector = Icons.Filled.Person,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified,
                                    imeAction = ImeAction.Done
                                ),
                                onValueChange = { newName ->
                                    onActionContactUs(ContactUsAction.UpdateName(newName))
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldInput(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(R.string.title),
                                value = contactUsUiState.title,
                                imageVector = Icons.Filled.Title,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified,
                                    imeAction = ImeAction.Done
                                ),
                                onValueChange = { newTitlte ->
                                    onActionContactUs(ContactUsAction.UpdateTitle(newTitlte))
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextFieldInput(
                                singleLine = false,
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(R.string.message),
                                value = contactUsUiState.message,
                                imageVector = Icons.Filled.Description,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified,
                                    imeAction = ImeAction.Done
                                ),
                                onValueChange = { newMessage ->
                                    onActionContactUs(ContactUsAction.UpdateMessage(newMessage))
                                }
                            )

                            Spacer(modifier = Modifier.height(40.dp))

                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Button(
                                    modifier = Modifier,
                                    onClick = {
                                        onActionContactUs(
                                            ContactUsAction.ContactUs(
                                                contactUs = ContactUs(
                                                    name = contactUsUiState.name,
                                                    title = contactUsUiState.title,
                                                    message = contactUsUiState.message
                                                )
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NewBlue,
                                        contentColor = NewWhite
                                    )
                                ) {
                                    Text(
                                        text = stringResource(R.string.contact_us),
                                        style = TitleTypography.copy(color = NewWhite)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}