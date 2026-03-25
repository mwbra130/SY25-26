F1 = ["F1", "VW Off-Road-Bug", 185, (104, 142), 6000, 9, 1880, 4]
C1 = ["C1", "Subaru Impreza WRC", 220, (221,300), 5500, 5.4, 1994, 4]
A4 = ["A4", "Suzuki Ignis", 180, (153, 206), 7250, 8, 1597, 4]
E2 = ["E2", "Ford Escort WRC", 220, (220,229), 6250, 5.6, 1993, 4]
B3 = ["B3", "Toyota Corolla WRC", 210, (210,299), 5700,5.4, 1972, 4]
B1 = ["B1", "Seat Cordoba WRC", 230, (221,300), 6000, 5, 1998, 4]
C2 = ["C2", "Opel Astra GSi", 235, (235,320), 6200, 5.6, 2962, 6]
C4 = ["C4", "Citroen Saxo Kit-Car", 168, (161,220), 7000, 7.5, 1600, 4]
H3 = ["H3", "Honda Integra Typ R", 235, (145, 198), 6500, 5.5, 1800, 4]
F2 = ["F2", "Mitsubishi Galant",  180, (216,294), 5800, 6.3, 3395, 4]

cars = [F1, C1, A4, E2, B3, B1, C2, C4, H3, F2]

def print_car(c):
    width = 30
    print("-" * width)
    print(f"|{c[0]}  {c[1]}")
    print(f"|Speed: {c[2]}   0-60: {c[5]}|")
    print(f"|HP: {c[3]}      CCs: {c[6]}|")
    print(f"|RPM: {c[4]}     Cyl: {c[7]}|")
    print("-" * width)

i = 1

for car in cars:
    print(i,car[1])
    i += 1
selected_car = int(input("Select your car (1-10): "))
print(print_car(cars[selected_car -1]))