def flag():
    for i in range(3):
        print("*********========================")
    for i in range(4):
        print("=================================")

print("Hello")
name = input("What is your name? ")
age = int(input("How old are you? "))
if age >= 18:
    print(name + " you can vote")
    flag()
else:
    print("You can't vote.")