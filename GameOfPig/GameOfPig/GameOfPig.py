import random

# Initialize variables
turn = 1
total = 0
round_score = 0
winning_score = 100  # Define the winning score
answer = ""

print("Welcome to the Game of Pig!")
print("First player to reach", winning_score, "wins!")

# Game loop
while total < winning_score:
    print("\nTurn:", turn)
    print("Your current score is:", total)
    print("This round you have:", round_score)
    answer = input("Would you like to roll or bank? ").lower()

    while answer == "roll":
        # Roll two dice
        die1 = random.randint(1, 6)
        die2 = random.randint(1, 6)
        print(f"You rolled a {die1} and a {die2}")

        if die1 == 1 and die2 == 1:  # Snake eyes
            total = 0
            round_score = 0
            print("Snake eyes! Your total score is reset to 0.")
            break
        elif die1 == 1 or die2 == 1:  # A single 1
            round_score = 0
            print("You rolled a 1! You get 0 for this round.")
            break
        else:
            round_score += die1 + die2
            print("This round you have:", round_score)

        answer = input("Would you like to roll or bank? ").lower()

    if answer == "bank":
        total += round_score
        round_score = 0
        print("You banked your points. Your total score is now:", total)

    if total >= winning_score:
        print("\nCongratulations! You reached", total, "and won the game!")
        break

    turn += 1