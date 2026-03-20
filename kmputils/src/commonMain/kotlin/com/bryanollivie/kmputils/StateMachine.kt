package com.bryanollivie.kmputils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateMachine<S, E>(
    initialState: S,
    private val transitions: Map<Pair<S, E>, TransitionResult<S>>
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _history = mutableListOf(initialState)
    val history: List<S> get() = _history.toList()

    fun send(event: E): Boolean {
        val current = _state.value
        val result = transitions[current to event] ?: return false
        result.sideEffect?.invoke(current, result.target)
        _state.value = result.target
        _history.add(result.target)
        return true
    }

    fun canSend(event: E): Boolean =
        transitions.containsKey(_state.value to event)

    fun reset(state: S) {
        _state.value = state
        _history.clear()
        _history.add(state)
    }

    data class TransitionResult<S>(
        val target: S,
        val sideEffect: ((from: S, to: S) -> Unit)? = null
    )

    class Builder<S, E> {
        private val transitions = mutableMapOf<Pair<S, E>, TransitionResult<S>>()

        fun transition(
            from: S,
            event: E,
            to: S,
            sideEffect: ((from: S, to: S) -> Unit)? = null
        ): Builder<S, E> {
            transitions[from to event] = TransitionResult(to, sideEffect)
            return this
        }

        fun build(initialState: S): StateMachine<S, E> =
            StateMachine(initialState, transitions.toMap())
    }
}

fun <S, E> stateMachine(
    initialState: S,
    builder: StateMachine.Builder<S, E>.() -> Unit
): StateMachine<S, E> =
    StateMachine.Builder<S, E>().apply(builder).build(initialState)
