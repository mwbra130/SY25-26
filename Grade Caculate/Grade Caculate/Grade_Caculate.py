from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# ====== FILL THESE IN PRIVATELY ======
USERNAME = "mwbra130"
PASSWORD = "596130"
# =====================================

driver = webdriver.Chrome()
driver.get("https://ps.dvusd.org/public/home.html")

# --- LOGIN ---
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "fieldAccount"))
)
driver.find_element(By.ID, "fieldAccount").send_keys(USERNAME)
driver.find_element(By.ID, "fieldPassword").send_keys(PASSWORD)
driver.find_element(By.ID, "btn-enter-sign-in").click()

time.sleep(4)

# --- CLICK YOUR GRADE LINK (put your class FRN here) ---
driver.find_element(
    By.CSS_SELECTOR,
    'a[href*="scores.html?frn=00438290531"]'
).click()

# Wait for Angular table to appear
time.sleep(4)

# ---------- SCRAPE EVERYTHING ----------
assignments = []
categories = []
scores = []

rows = driver.find_elements(By.CSS_SELECTOR, "tr.ng-scope")

for row in rows:

    # assignment name
    try:
        name = row.find_element(By.CSS_SELECTOR, "td.assignmentcol .ng-binding").text.strip()
    except:
        name = None

    # category (Coursework or Assessment)
    try:
        category = row.find_element(By.CSS_SELECTOR, "span.psonly").text.strip()
    except:
        category = None

    # score (example: "20/20")
    try:
        score = row.find_element(By.CSS_SELECTOR, "td.score span").text.strip()
    except:
        score = None

    # only add rows with real assignment names
    if name and score and category:
        assignments.append(name)
        categories.append(category)
        scores.append(score)

# ---------- SORT INTO REQUESTED LISTS ----------

coursework_grades = []
assessment_grades = []

for name, cat, score in zip(assignments, categories, scores):
    if cat.lower() == "coursework":
        coursework_grades.append(score)
    elif cat.lower() == "assessment":
        assessment_grades.append(score)

# ---------- OUTPUT RESULTS ----------
print("\nCOURSEWORK GRADES:")
print(coursework_grades)

print("\nASSESSMENT GRADES:")
print(assessment_grades)

time.sleep(3)
driver.quit()
