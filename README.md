# ISO 9564-3 Tech test

## Focus
Conversion of text into 8 byte array using ISO 9564-3

UI work was from Wizard generation with a few extra controls.

MVVM was used to keep things seperated.

Koin was used over Dagger for speed of setup. DI was added to make injection easy for classes that required constructor injection (which makes teasting easier)

I assumed that the value would want to be packed into a byte array. I could have packed into a `Long`, but then I would have to think about MSB/LSB problems.

I added the random generator as a injected class to allow replacement for testing.

Unit test where written for the Pin generator

## What I didn't do

* More positive tests. They are very time consuming.
* I didn't use unsigned bytes as these are still experimental in Kotlin.
* I didn't remove the last digit of the PAN. This would not have been difficult.
* I did not test the integrity of the PAN. I don't know how to do this
* This are fast calculations to not threading was required.
* I was unsure when I PAN would be too long. So I didn't test this
* I didn't write tests for the ViewModel because it does so little in this case.
* The UI was not considered important to again to tests.

## Thoughts

Very easy to get wrong. More difficult than I thought it would be because Kotlin is not as good at bit manipulation like C/C++

Having said that you can construct really concise code with Kotlin, which is nice.