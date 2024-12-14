package com.example.flickrsearch.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flickrsearch.R
import com.example.flickrsearch.state.InputFieldState

/**
 * A composable function that displays a searchable input field with configurable behavior.
 *
 * @param inputFieldState The current state of the search field, represented by a [InputFieldState] object
 * @param searchQuery The current search query text displayed in the input field.
 * @param onSearchInputChanged lambda invoked when the text in the search field changes.
 * @param onClearInputClicked lambda invoked when the clear ("X") button in the input field is clicked.
 * @param onSearchInputClicked lambda invoked when the search input field itself is clicked.
 *
 * This component can be used to provide a dynamic, interactive search field with custom behavior
 * for input handling and clearing actions.
 */
@Composable
fun SearchableInputField(
    inputFieldState: InputFieldState = InputFieldState.Idle,
    searchQuery: String = "",
    onSearchInputChanged: (String) -> Unit,
    onClearInputClicked: () -> Unit,
    onSearchInputClicked: () -> Unit,
) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchInputChanged(it) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        trailingIcon = {
            if (inputFieldState is InputFieldState.HasInput) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.clear_input_field_content_description),
                    modifier = Modifier.clickable {
                        onClearInputClicked()
                    }
                )
            }
        },
        leadingIcon = {
            if (inputFieldState is InputFieldState.Idle) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_content_description),
                )
            }
        },
        colors = when (inputFieldState) {
            InputFieldState.Idle -> searchFieldColorsStateIdle()
            InputFieldState.FocusedNoInput,
            InputFieldState.HasInput -> searchFieldColorsStateActive()
        },
        placeholder = {
            Text(
                text = stringResource(R.string.input_field_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
            LaunchedEffect(key1 = interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        onSearchInputClicked.invoke()
                    }
                }
            }
        },
    )
}

@Composable
private fun searchFieldColorsStateIdle() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = Color.Transparent,
)

@Composable
private fun searchFieldColorsStateActive() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

@Preview
@Composable
fun SearchableInputFieldPreview() {
    SearchableInputField(
        inputFieldState = InputFieldState.Idle,
        onSearchInputChanged = {},
        onClearInputClicked = {},
        onSearchInputClicked = {},
    )
}