package com.emilianogonzalezar.warcardgame.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

// Deck
// Deck Parcelable had to be implemented because Queue was not recognized by the @Parcelize annotation
// I used a Queue because of the decks usage nature for the War Game:
// draw a card from the top, and place the card at the bottom when you win
data class Deck(val cards: Queue<Card>) : Parcelable {
    constructor(parcel: Parcel) : this(LinkedList(parcel.createTypedArrayList(Card.CREATOR)!!.toList()))

    fun putTopAtBottom() {
        if (cards.size > 1) {
            cards.add(cards.peek())
            cards.poll()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(cards.toList())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Deck> {
        override fun createFromParcel(parcel: Parcel): Deck {
            return Deck(parcel)
        }

        override fun newArray(size: Int): Array<Deck?> {
            return arrayOfNulls(size)
        }
    }
}