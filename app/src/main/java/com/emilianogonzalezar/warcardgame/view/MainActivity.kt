package com.emilianogonzalezar.warcardgame.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.SavedStateViewModelFactory
import com.emilianogonzalezar.warcardgame.R
import com.emilianogonzalezar.warcardgame.databinding.ActivityMainBinding
import com.emilianogonzalezar.warcardgame.model.Card
import com.emilianogonzalezar.warcardgame.model.CardColor
import com.emilianogonzalezar.warcardgame.model.WarGame
import com.emilianogonzalezar.warcardgame.model.WarGameState
import com.emilianogonzalezar.warcardgame.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }

    private var cpuCardYPos: Float = 0.0f
    private var playerCardYPos: Float = 0.0f
    private var gameIsFinished = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainViewModel.warGame.observe(this, { warGameState ->
            onGameStateChanged(warGameState)
        })

        binding.drawCardButton.setOnClickListener {
            if (gameIsFinished) {
                onStartPressed()
            } else {
                onDrawClicked()
            }
        }

        cpuCardYPos = binding.cpuCard.y
        playerCardYPos = binding.playerCard.y

        if (savedInstanceState == null) {
            onStartPressed()
        }
    }

    private fun onStartPressed() {
        gameIsFinished = false

        // Right now, the game visual representation only supports 2 players
        mainViewModel.onStartPressed(2)
    }

    private fun onDrawClicked() {
        binding.drawCardButton.isEnabled = false
        animateCardsWinner()?.doOnEnd {
            mainViewModel.onContinueClicked()
        }
    }

    private fun animateCardsWinner(): ObjectAnimator? {
        val currentWinner = mainViewModel.getCurrentWinner()
        val directionCPU = if (currentWinner == 1) 1 else -1
        val directionPlayer = if (currentWinner == 0) -1 else 1
        val offset = resources.displayMetrics.heightPixels.toFloat()
        ObjectAnimator.ofFloat(binding.cpuCard, "translationY", directionCPU*offset).apply {
            duration = 1000
            start()
        }

        // I'm returning the ObjectAnimator so I can use it's "end" listener
        // This works for both the ObjectAnimators here because they have the same duration.
        // If at any time the duration varies between them, I will need to change this
        return ObjectAnimator.ofFloat(binding.playerCard, "translationY", directionPlayer*offset).apply {
            duration = 1000
            start()
        }
    }

    private fun animateCardsDraw(): ObjectAnimator? {
        ObjectAnimator.ofFloat(binding.cpuCard, "translationY", cpuCardYPos).apply {
            duration = 1000
            start()
        }

        // I'm returning the ObjectAnimator so I can use it's "end" listener
        // This works for both the ObjectAnimators here because they have the same duration.
        // If at any time the duration varies between them, I will need to change this
        return ObjectAnimator.ofFloat(binding.playerCard, "translationY", playerCardYPos).apply {
            duration = 1000
            start()
        }
    }

    // Puts the cards out of the view, ready to be drawn
    private fun resetCardsPosition() {
        val percent = TypedValue()
        resources.getValue(R.dimen.card_height_percent, percent, true)
        binding.cpuCard.y = -resources.displayMetrics.heightPixels.toFloat() * percent.float
        binding.playerCard.y = resources.displayMetrics.heightPixels.toFloat()
    }

    // Here is all the view update logic.
    private fun onGameStateChanged(warGameState: WarGame) {
        gameIsFinished = warGameState.state == WarGameState.FINISHED
        if (warGameState.playerCards.size == 0) return

        resetCardsPosition()

        if (WarGameState.PLAYING == warGameState.state) {
            animateCardsDraw()?.doOnEnd {
                binding.drawCardButton.isEnabled = true
            }

            binding.drawCardButton.text = getString(R.string.continue_button_text)
            binding.gameResult.visibility = View.GONE

        } else {
            binding.drawCardButton.isEnabled = true
            binding.drawCardButton.text = getString(R.string.play_again)

            val gameStateText =
                if (mainViewModel.getCurrentWinner() == 1) {
                    getString(R.string.player_wins)
                } else {
                    getString(R.string.player_lost)
                }

            binding.gameResult.text = gameStateText
            binding.gameResult.visibility = View.VISIBLE
        }

        val cpuCards = warGameState.playerCards[0].cards
        val playerCards = warGameState.playerCards[1].cards
        binding.cpuScore.text = cpuCards.size.toString()
        binding.playerScore.text = playerCards.size.toString()
        cpuCards.peek()?.let { binding.cpuCard.setImageResource(mapCardToDrawableId(it)) }
        playerCards.peek()?.let { binding.playerCard.setImageResource(mapCardToDrawableId(it)) }
    }

    // This function maps the visual representation of a Card I could put this in the Card data class,
    // but I kept this here so the visual representation is completely detached from the underlying game rules
    private fun mapCardToDrawableId(card: Card): Int {
        val cardName: String =
            when (card.number) {
                1 -> "ace"
                11 -> "jack"
                12 -> "queen"
                13 -> "king"
                else -> "_" + card.number
            }

        val cardColor: String =
            when (card.color) {
                CardColor.SPADES -> "spades"
                CardColor.HEARTS -> "hearts"
                CardColor.DIAMONDS -> "diamonds"
                CardColor.CLUBS -> "clubs"
            }

        val name = String.format("%s_of_%s", cardName, cardColor)
        return resources.getIdentifier(name, "drawable", packageName)
    }
}