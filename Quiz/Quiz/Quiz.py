score = 0
q1 = "What was the first Tri-Fold Phone?"
O1_1 = "a) Samsung G Fold"
O1_2 = "b) Iphone Tri Fold"
O1_3 = "c) Xiaomi Tri Fold"
O1_4 = "d) Huawei Mate XT"
a1 = "d"

q2 = "What is the name of Samsung's latest fold phone series?"
O2_1 = "a) Samsung G Fold"
O2_2 = "b) Galaxy Z Fold 7"
O2_3 = "c) Galaxy Z Fold 6 "
O2_4 = "d) Fold 7"
a2 = "b"

q3 = "What type of display technology does the iPhone 16 Pro use?"
O3_1 = "a) AMOLED"
O3_2 = "b) Micro LED"
O3_3 = "c) LED"
O3_4 = "d) OLED"
a3 = "d"

q4 = "What is the thinnest phone?"
O4_1 = "a) iphone 17 air"
O4_2 = "b) S25 Edge"
O4_3 = "c) Oppo Find N5"
O4_4 = "d) Oppo R5"
a4 = "a"

q5 = "Which is the thinest Samsung Phone?"
O5_1 = "a) Samsung G Fold"
O5_2 = "b) S25 Edge"
O5_3 = "c) Huawei Mate XT"
O5_4 = "d) S8"
a5 = "b"

q6 = "What is the current amount of ram in the Galaxy S25 Series?"
O6_1 = "a) 16GB"
O6_2 = "b) 12GB"
O6_3 = "c) 8GB"
O6_4 = "d) 6GB"
a6 = "b"

q7 = "What is Apple's newest chip found in the iPhone 17?"
O7_1 = "a) A19"
O7_2 = "b) A19 Pro"
O7_3 = "c) A20 Pro"
O7_4 = "d) A17"
a7 = "a"

q8 = "Which material is used for the frame on the Samsung Galaxy S25 Ultra"
O8_1 = "a) Aluminum"
O8_2 = "b) Plastic"
O8_3 = "c) Titanium"
O8_4 = "d) Carbon Fiber"
a8 = "c"

q9 = "What port replaced the Lightning port on the iPhone 15 series"
O9_1 = "a) USB-A"
O9_2 = "b) USB-D"
O9_3 = "c) HDMI"
O9_4 = "d) USB-C"
a9 = "d"

q10 = "What is the newest Samsung S Series?"
O10_1 = "a) S25"
O10_2 = "b) S26"
O10_3 = "c) S27"
O10_4 = "d) S20"
a10 = "a"

print(q1)
print(O1_1)
print(O1_2)
print(O1_3)
print(O1_4)
user_answer = input("Enter your answer:")
if user_answer == a1:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q2)
print(O2_1)
print(O2_2)
print(O2_3)
print(O2_4)
user_answer = input("Enter your answer:")
if user_answer == a2:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q3)
print(O3_1)
print(O3_2)
print(O3_3)
print(O3_4)
user_answer = input("Enter your answer:")
if user_answer == a3:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q4)
print(O4_1)
print(O4_2)
print(O4_3)
print(O4_4)
user_answer = input("Enter your answer:")
if user_answer == a4:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q5)
print(O5_1)
print(O5_2)
print(O5_3)
print(O5_4)
user_answer = input("Enter your answer:")
if user_answer == a5:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q6)
print(O6_1)
print(O6_2)
print(O6_3)
print(O6_4)
user_answer = input("Enter your answer:")
if user_answer == a6:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")    
print(q7)
print(O7_1)
print(O7_2)
print(O7_3)
print(O7_4)
user_answer = input("Enter your answer:")
if user_answer == a7:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q8)
print(O8_1)
print(O8_2)
print(O8_3)
print(O8_4)
user_answer = input("Enter your answer:")
if user_answer == a8:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")
print(q9)
print(O9_1)
print(O9_2)
print(O9_3)
print(O9_4)
user_answer = input("Enter your answer:")
if user_answer == a9:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")   
print(q10)
print(O10_1)
print(O10_2)
print(O10_3)
print(O10_4)
user_answer = input("Enter your answer:")
if user_answer == a10:
    print("Correct!")
    score = score + 2
else:
    print("incorrect")   
print("You finished the quiz! Your score is " + str(score) + " out of 10.")