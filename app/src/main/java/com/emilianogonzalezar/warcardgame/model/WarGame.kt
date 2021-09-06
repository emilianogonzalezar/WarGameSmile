package com.emilianogonzalezar.warcardgame.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

// WarGame
// Contains the state of the game and the player cards
@Parcelize
data class WarGame(@NotNull val state: WarGameState = WarGameState.FINISHED,
                   @NotNull val playerCards: List<Deck> = listOf()) : Parcelable