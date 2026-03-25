seeds = [1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2, 15]
winners = ['Purdue', 'FDU', 'FAU', 'Memphis', 'Duke', 'Oral Roberts', 'UVA', 'Furman', 'Kentucky', 'Pitt', 'Kansas', 'Howard', 'Texas', 'Penn St', 'UCLA', 'UNC Asheville']

count = 0
for seed in range(len(seeds)):
    if seeds[seed] > 10:
        print(f"Cinderella Alert! [{winners[seed]}]")
        count += 1
print(f"Total Cinderella Stories: {count}")