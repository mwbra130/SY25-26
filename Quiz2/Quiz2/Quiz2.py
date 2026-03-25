'''
count = 0
letter = input("Enter a letter: ")
for l in "elephant":
    if l == letter:
        count += 1
print(count)
'''


#Get a number from 1 to 10 from person
#if the user guesses 3 in less than 5 guesses the win
#If they guess in more than 5 guesses tell them they lose

guesses = 0
while True:
    guess = input("Guess a number from 1 to 10: ")
    guesses += 1
    if guesses >= 5:
        print("You lose!")
        break
    elif guesses < 5 and guess == "3":
        print("You win")
        break