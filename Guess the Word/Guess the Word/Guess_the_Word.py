import random

guesses = 10
words = ["algorithm", "bandwidth", "cryptography", "dashboard", "encryption","framework"]
secret_word = random.choice(words)
display_word = ["_"] * len(secret_word)
guessed_letters = []

def get_guess():
    guess = ""
    while True:
        guess = input("Guess: ")
        if len(guess) != 1:
            print("Your guess must have exactly one character!")
        elif not guess.islower():
            print("Your guess must be a lowercase letter!")
        elif guess in guessed_letters:
            print("You already guessed that letter!")
        else:
            return guess

remaining_guesses = guesses
while "_" in display_word and remaining_guesses > 0:
    print("Current word:", " ".join(display_word))
    print(f"Guesses left: {remaining_guesses}")
    guess = get_guess()
    guessed_letters.append(guess)
    if guess in secret_word:
        print("That letter is in the word!")
        for i, letter in enumerate(secret_word):
            if letter == guess:
                display_word[i] = guess
    else:
        print("That letter isn't in the word.")
        remaining_guesses = remaining_guesses - 1
    if "_" not in display_word:
        print("Congratulations! You guessed the word:", secret_word)
        break
    elif remaining_guesses == 0:
        print("Out of guesses! The word was:", secret_word)