package com.example.flickrsearch.state

/**
 * Sealed interface representing the various states of the input field.
 *
 * This interface is used to track and represent the different states the search input field can be in.
 */
sealed interface InputFieldState {

    /**
     * Represents the idle state when the input field is not focused or used.
     */
    data object Idle : InputFieldState

    /**
     * Represents the state when the input field has some user input.
     */
    data object HasInput : InputFieldState

    /**
     * Represents the state when the input field is focused but contains no input text.
     */
    data object FocusedNoInput : InputFieldState
}