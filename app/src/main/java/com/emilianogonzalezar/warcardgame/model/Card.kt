package com.emilianogonzalezar.warcardgame.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.IntRange

// Card
// A card with a number, color, and value. Value states the strength for the game (which card wins).
// This Parcelable implementation is needed because I need to use Card's CREATOR in the Deck class.
data class Card(@IntRange(from = 1, to = 13) val number: Int,
                val color: CardColor,
                @IntRange(from = 1, to = 13) val value: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        CardColor.values()[parcel.readInt()],
        parcel.readInt()
    )

    fun beats(otherCard: Card?): Boolean {
        return otherCard == null || value > otherCard.value
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeInt(color.ordinal)
        parcel.writeInt(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}