package com.emilianogonzalezar.warcardgame.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.emilianogonzalezar.warcardgame.model.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(SavedStateHandle())
    }

    private fun getCardFromDeck(number: Int, color: CardColor): Card? {
        for (card in WarDeck.deck.cards) {
            if (card.number == number && card.color == color) {
                return card
            }
        }

        return null
    }

    private fun setUpForTie() {
        val playerDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(10, CardColor.DIAMONDS)
        )))
        val cpuDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(10, CardColor.CLUBS)
        )))

        setUpDecks(cpuDeck, playerDeck)
    }

    private fun setUpForCPUToWin() {
        val playerDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(10, CardColor.DIAMONDS)
        )))
        val cpuDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(13, CardColor.CLUBS)
        )))

        setUpDecks(cpuDeck, playerDeck)
    }

    private fun setUpForPlayerToWin() {
        val playerDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(1, CardColor.SPADES)
        )))
        val cpuDeck = Deck(LinkedList(arrayListOf(
            getCardFromDeck(2, CardColor.HEARTS)
        )))

        setUpDecks(cpuDeck, playerDeck)
    }

    private fun setUpDecks(cpuDeck: Deck, playerDeck: Deck) {
        val decks = ArrayList<Deck>()
        decks.add(cpuDeck)
        decks.add(playerDeck)
        mainViewModel.warGame.value = WarGame(WarGameState.PLAYING, decks)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onStartPressedWith2Players() {
        val playerAmount = 2
        mainViewModel.onStartPressed(playerAmount)
        mainViewModel.warGame.observeForever {
            assert(mainViewModel.warGame.value?.state == WarGameState.PLAYING)

            var allPlayersHaveTheSameAmountOfCards = true
            for (player in 0 until playerAmount) {
                allPlayersHaveTheSameAmountOfCards = allPlayersHaveTheSameAmountOfCards &&
                        mainViewModel.warGame.value?.playerCards != null &&
                        mainViewModel.warGame.value?.playerCards!![player].cards.size == WarDeck.deck.cards.size/playerAmount
            }
            assert(allPlayersHaveTheSameAmountOfCards)
        }
    }

    @Test
    fun getCurrentWinnerPlayer() {
        setUpForPlayerToWin()
        assert(mainViewModel.getCurrentWinner() == 1)
    }

    @Test
    fun getCurrentWinnerCPU() {
        setUpForCPUToWin()
        assert(mainViewModel.getCurrentWinner() == 0)
    }

    @Test
    fun getCurrentWinnerTie() {
        setUpForTie()
        assert(mainViewModel.getCurrentWinner() == -1)
    }

    @Test
    fun onContinueClickedGameGoesOn() {
        val playerAmount = 2
        mainViewModel.onStartPressed(playerAmount)
        mainViewModel.onContinueClicked()

        mainViewModel.warGame.observeForever {
            assert(mainViewModel.warGame.value?.state == WarGameState.PLAYING)
        }
    }

    @Test
    fun onContinueClickedGameFinishesPlayerWins() {
        setUpForPlayerToWin()
        mainViewModel.onContinueClicked()

        mainViewModel.warGame.observeForever {
            assert(mainViewModel.warGame.value?.state == WarGameState.FINISHED)
            assert(mainViewModel.getCurrentWinner() == 1)
        }
    }

    @Test
    fun onContinueClickedGameFinishesPlayerLoses() {
        setUpForCPUToWin()
        mainViewModel.onContinueClicked()
        mainViewModel.warGame.observeForever {
            assert(mainViewModel.warGame.value?.state == WarGameState.FINISHED)
            assert(mainViewModel.getCurrentWinner() == 0)
        }
    }
}