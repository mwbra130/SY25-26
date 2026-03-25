import pygame
import random
import os

# Initialize Pygame
pygame.init()

# Screen Shake Variables
shake_duration = 0
shake_magnitude = 0

# Constants
WIDTH, HEIGHT = 800, 600
RED = (255, 0, 0)

screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

# Player
player_size = 50
player_pos = [WIDTH // 2, HEIGHT - 50]
player_image = pygame.image.load(os.path.join('Sprite.png')).convert_alpha()
player_image = pygame.transform.scale(player_image, (player_size, player_size))

# Enemy
enemy_size = 50
enemy_pos = [random.randint(0, WIDTH - enemy_size), 0]
enemy_speed = 10

score = 0
running = True
game_over = False

# Screen shake function
def start_shake(duration, magnitude):
    global shake_duration, shake_magnitude
    shake_duration = duration
    shake_magnitude = magnitude

while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    # Movement (disabled after game over)
    if not game_over:
        keys = pygame.key.get_pressed()
        if keys[pygame.K_LEFT]:
            player_pos[0] -= 5
        if keys[pygame.K_RIGHT]:
            player_pos[0] += 5

    # Keep player inside screen
    player_pos[0] = max(0, min(WIDTH - player_size, player_pos[0]))

    # Enemy movement
    if not game_over:
        enemy_pos[1] += enemy_speed

    # Reset enemy
    if enemy_pos[1] > HEIGHT:
        enemy_pos[1] = 0
        enemy_pos[0] = random.randint(0, WIDTH - enemy_size)
        if not game_over:
            score += 1
            print(f"Score: {score}")

    # Collision detection
    if (enemy_pos[0] < player_pos[0] + player_size and
        enemy_pos[0] + enemy_size > player_pos[0] and
        enemy_pos[1] < player_pos[1] + player_size and
        enemy_pos[1] + enemy_size > player_pos[1]):

        if not game_over:
            print("Game Over!")
            start_shake(40, 10)
            game_over = True

    # ---------------- SCREEN SHAKE ----------------
    offset_x = 0
    offset_y = 0

    if shake_duration > 0:
        offset_x = random.randint(-shake_magnitude, shake_magnitude)
        offset_y = random.randint(-shake_magnitude, shake_magnitude)
        shake_duration -= 1

    # Drawing
    screen.fill((0, 0, 0))

    # Enemy
    pygame.draw.rect(
        screen,
        RED,
        (enemy_pos[0] + offset_x, enemy_pos[1] + offset_y, enemy_size, enemy_size)
    )

    # Player (sprite)
    screen.blit(
        player_image,
        (player_pos[0] + offset_x, player_pos[1] + offset_y)
    )

    pygame.display.update()
    clock.tick(30)

    # Exit after shake completes
    if game_over and shake_duration <= 0:
        pygame.time.delay(500)  # small pause before closing
        running = False

pygame.quit()