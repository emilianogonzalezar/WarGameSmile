package com.emilianogonzalezar.warcardgame.model

import java.util.*

// WarDeck
// This states the existing game cards.
class WarDeck {
    companion object {
        val deck = Deck(
            LinkedList(listOf(
            Card(1, CardColor.SPADES, 13),
            Card(2, CardColor.SPADES, 1),
            Card(3, CardColor.SPADES, 2),
            Card(4, CardColor.SPADES, 3),
            Card(5, CardColor.SPADES, 4),
            Card(6, CardColor.SPADES, 5),
            Card(7, CardColor.SPADES, 6),
            Card(8, CardColor.SPADES, 7),
            Card(9, CardColor.SPADES, 8),
            Card(10, CardColor.SPADES, 9),
            Card(11, CardColor.SPADES, 10),
            Card(12, CardColor.SPADES, 11),
            Card(13, CardColor.SPADES, 12),
            Card(1, CardColor.HEARTS, 13),
            Card(2, CardColor.HEARTS, 1),
            Card(3, CardColor.HEARTS, 2),
            Card(4, CardColor.HEARTS, 3),
            Card(5, CardColor.HEARTS, 4),
            Card(6, CardColor.HEARTS, 5),
            Card(7, CardColor.HEARTS, 6),
            Card(8, CardColor.HEARTS, 7),
            Card(9, CardColor.HEARTS, 8),
            Card(10, CardColor.HEARTS, 9),
            Card(11, CardColor.HEARTS, 10),
            Card(12, CardColor.HEARTS, 11),
            Card(13, CardColor.HEARTS, 12),
            Card(1, CardColor.DIAMONDS, 13),
            Card(2, CardColor.DIAMONDS, 1),
            Card(3, CardColor.DIAMONDS, 2),
            Card(4, CardColor.DIAMONDS, 3),
            Card(5, CardColor.DIAMONDS, 4),
            Card(6, CardColor.DIAMONDS, 5),
            Card(7, CardColor.DIAMONDS, 6),
            Card(8, CardColor.DIAMONDS, 7),
            Card(9, CardColor.DIAMONDS, 8),
            Card(10, CardColor.DIAMONDS, 9),
            Card(11, CardColor.DIAMONDS, 10),
            Card(12, CardColor.DIAMONDS, 11),
            Card(13, CardColor.DIAMONDS, 12),
            Card(1, CardColor.CLUBS, 13),
            Card(2, CardColor.CLUBS, 1),
            Card(3, CardColor.CLUBS, 2),
            Card(4, CardColor.CLUBS, 3),
            Card(5, CardColor.CLUBS, 4),
            Card(6, CardColor.CLUBS, 5),
            Card(7, CardColor.CLUBS, 6),
            Card(8, CardColor.CLUBS, 7),
            Card(9, CardColor.CLUBS, 8),
            Card(10, CardColor.CLUBS, 9),
            Card(11, CardColor.CLUBS, 10),
            Card(12, CardColor.CLUBS, 11),
            Card(13, CardColor.CLUBS, 12),
        )))
    }
}