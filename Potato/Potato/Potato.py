
#The Potato Grinder

weight = float(input("Enter the weight of the potato in grams: "))
if weight < 100:
    grade = "small"
elif 100 <= weight <= 200:
    grade = "medium"
else:
    grade = "large"
print(f"This is a {grade} potato.")

#The Blemish Counter
blemish_counts = []
for i in range(5):
    count = int(input(f"Enter the number of blemishes on potato {i+1}: "))
    blemish_counts.append(count)
total_blemishes = sum(blemish_counts)
total = sum(blemish_counts)
average = total / len(blemish_counts)
print(f"Total blemishes: {total_blemishes}")
print(f"Average blemishes per potato: {average}")

#Quality Control Filter
all_potatos = [0,2,5,1,0,8,3,0]
perfect_potatos = []
for p in all_potatos:
    if p == 0:
        perfect_potatos.append(p)
num_total = len(all_potatos)
num_perfect = len(perfect_potatos)
percentage = (num_perfect / num_total) * 100
print(f"Batch Quality: {percentage}% perfect")
print(f"Perfect Potatos found: {num_perfect}")