package com.bsfdv.backend.presentation.core

open class PresentationException(message: String) : RuntimeException(message)

class InvalidInputValue(inputName: String, inputValue: String) :
    PresentationException("Value '$inputValue' for $inputName is invalid, please re-enter a valid value.")

class EmptyRequiredInput(inputName: String) : PresentationException("'$inputName' is required, please enter a value.")