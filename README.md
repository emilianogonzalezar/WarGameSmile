# WarGameSmile

Welcome to Emiliano González's excercise for Smile!

<img src="https://imgur.com/etWABCx"/>

# Part C

The overall idea of the architecture can be the same between the Android native app and the React.
Let’s describe it by comparing with the architecture I have built for the app (MVVM):

## The View
The visual representation of the app. It is all of what happens with what is rendered: the card, the player’s score, the buttons, the events when pressing the buttons and the card animations.
For the card resources I would need PNGs.
The view would be developed with the HTML + SCSS. To update the view I would use React Hooks: the view is registered to the state change events and updates accordingly. When the player presses a button, the view notifies the Controller by calling the method “onContinueClicked”.
As the animations are pretty simple, I would do them with SCSS + javascript. With the same Hooks, and as I did in Android native, by the time the player presses the “Continue” button, I would make the animations of the cards going out of the view, call the Controller to notify that the user clicked “Continue”, and then when the Hook notifies the view, I would make the animation of the new cards going in.
As it’s a responsive web app, I would follow the concept “Mobile First” and start the development with the resolutions for Mobile: 360px, 768px. After that, I would take into account the resolutions for desktop: 1024px, 1280px, 1920px that are the most common resolutions.

## The ViewModel
The business logic (or the game logic). The way the deck is shuffled, the way the cards are dealt, the definition of a “winner”, and what happens with the player cards when the view notifies the player wants to continue.
Hooks allow React to work without classes. Nevertheless, I would develop a React component that stores the methods that transform the state. I can use the hooks in the components.


## The Model
The data classes used by the business logic. The definition of what are: Card, Deck, War Game Deck, and texts. For this, I would define really simple components for every one of these. Mainly, I need to define which cards are available to the players, and which of them beats what other cards.
For multi language support, in Android Native I used the native localization folders, but in the react app I would use i18n.

## Other considerations

**Routing**

I would have only one route, just like in Android native I have only one Activity. If there were differences between countries, the routing could resolve it by using queryparams.

**Scaffolding**

__app__
>__assets__
>
>>__fonts__
>>
>>__images__
>>
>__components__
>
>>__card__ (card data - number, color, value)
>>
>>__deck__ (deck data - list of cards)
>>
>>__warDeck__ (poker cards for playing War)
>>
>>__warGame__ (the game logic controller)
>>
>__hooks__
>
>__pages__ (the HTML view)
>
>__server__ (the router configuration)
>
>__styles__ (SCSS files)
