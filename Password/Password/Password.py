from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service
import time

# --- Setup WebDriver ---
service = Service("path/to/chromedriver")   # example: "C:/drivers/chromedriver.exe"
driver = webdriver.Chrome(service=service)

# --- Open Website ---
driver.get("https://ps.dvusd.org/public/home.html")
time.sleep(2)

# --- Locate Fields ---
username_input = driver.find_element(By.ID, "fieldAccount")     # Username box
password_input = driver.find_element(By.ID, "fieldPassword")    # Password box

# --- Enter Data ---
username_input.send_keys("YOUR_USERNAME")
password_input.send_keys("YOUR_PASSWORD")

# (Optional) Click Sign In
login_button = driver.find_element(By.ID, "btn-enter-sign-in")
login_button.click()

time.sleep(5)
driver.quit()
