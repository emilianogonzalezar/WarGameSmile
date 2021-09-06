package com.emilianogonzalezar.warcardgame.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.emilianogonzalezar.warcardgame.model.Deck
import com.emilianogonzalezar.warcardgame.model.WarDeck
import com.emilianogonzalezar.warcardgame.model.WarGame
import com.emilianogonzalezar.warcardgame.model.WarGameState
import java.util.*

class MainViewModel(val state: SavedStateHandle): ViewModel() {
    private val STATE_KEY = "WAR_GAME"

    val warGame = state.getLiveData(STATE_KEY, WarGame())

    // Game can be easily extended for more than 2 players.
    // This viewModel should do fine with a custom War game mode that allows more than 2 players.
    fun onStartPressed(@IntRange(from = 2) players: Int) {
        // Shuffle the whole deck
        val shuffledDeck = IntArray(WarDeck.deck.cards.size){it}
        shuffledDeck.shuffle()

        // Give the same amount of cards for every player
        val decks = ArrayList<Deck>(players)
        for (i in 0 until players) {
            val cardsArr = WarDeck.deck.cards.toTypedArray()
            val playerCards = (i*shuffledDeck.size/players until ((i+1)*shuffledDeck.size/players))
                .map{cardsArr[shuffledDeck[it]]}

            decks.add(Deck(LinkedList(playerCards)))
        }

        // Post the state and notify
        warGame.postValue(WarGame(WarGameState.PLAYING, decks))
    }

    fun getCurrentWinner(): Int {
        val decks = warGame.value?.playerCards
        var winner = -1
        var isTie = false

        decks?.let {
            for (otherPlayer in decks.indices) {
                // The winner is the one that beats the other player's top card
                val winnerPlayerCard = if (winner > -1) decks[winner].cards.peek() else null
                val otherPlayerCard = decks[otherPlayer].cards.peek()
                if (otherPlayerCard?.beats(winnerPlayerCard) == true) {
                    winner = otherPlayer
                }

                // If any two players have the same card, it's a tie.
                isTie = isTie ||
                        (winnerPlayerCard != null && otherPlayerCard != null
                                && winnerPlayerCard.value == otherPlayerCard.value)
            }
        }

        return if (isTie) -1 else winner
    }

    fun onContinueClicked() {
        val decks = warGame.value?.playerCards
        var emptyDecksCount = 0

        decks?.let {
            val winner = getCurrentWinner()

            // If ther was a tie, all players must get the
            // top card and place it at the bottom of their deck
            if (winner < 0) {
                for (deck in decks) {
                    deck.putTopAtBottom()
                }
            } else {
                // If a player won this step, the he/she gets all of the other players top card
                for (otherPlayer in decks.indices) {
                    val otherPlayerCard = decks[otherPlayer].cards.poll()
                    otherPlayerCard?.let { decks[winner].cards.add(otherPlayerCard) }

                    if (decks[otherPlayer].cards.size == 0) {
                        emptyDecksCount++
                    }
                }
            }

            // Is there a game winner?
            val newGameState =
                if (emptyDecksCount == decks.size - 1)
                    WarGameState.FINISHED
                else
                    WarGameState.PLAYING

            // Post the state and notify
            warGame.postValue(WarGame(newGameState, decks))
        }
    }
}