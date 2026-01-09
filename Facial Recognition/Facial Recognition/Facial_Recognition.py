"""
real_time_face_recognition.py

Requirements:
 - Python 3.8+
 - pip install opencv-python face_recognition numpy

Folder layout:
 .
 ?? real_time_face_recognition.py
 ?? known_people/
     ?? alice.jpg
     ?? bob.png
     ?? charlie.jpeg

Each file in known_people/ should be a clear image of one person; the filename (minus extension)
will be used as the label (e.g. "alice.jpg" -> "alice").
"""

import os
import sys
import time
from typing import List, Tuple

import cv2
import numpy as np
import face_recognition

# CONFIG
KNOWN_FACES_DIR = "known_people"      # folder with one image per known person
TOLERANCE = 0.5                       # lower = stricter, typical 0.4-0.6
MODEL = "cnn"                         # "hog" (fast, CPU) or "cnn" (more accurate, GPU if available)
FRAME_THICKNESS = 2
FONT_THICKNESS = 2
FRAME_RESIZE_SCALE = 0.25             # scale down frame for faster processing (0.25 = 1/4 size)
SHOW_FPS = True

def load_known_faces(known_dir: str) -> Tuple[List[np.ndarray], List[str]]:
    """
    Load known faces from directory. Returns (encodings, names).
    Each image file should contain one face. If an image has multiple faces, only the first is used.
    """
    encodings = []
    names = []

    if not os.path.exists(known_dir):
        print(f"[!] Known faces directory '{known_dir}' not found.")
        return encodings, names

    for filename in os.listdir(known_dir):
        filepath = os.path.join(known_dir, filename)
        basename, ext = os.path.splitext(filename)
        if not ext.lower() in [".jpg", ".jpeg", ".png"]:
            continue

        print(f"[i] Loading {filepath} as {basename}")
        image = face_recognition.load_image_file(filepath)
        # Get face encodings (may return empty if no face found)
        face_encs = face_recognition.face_encodings(image)
        if len(face_encs) == 0:
            print(f"  [!] No face found in {filename}; skipping.")
            continue

        encodings.append(face_encs[0])
        names.append(basename)
    print(f"[i] Loaded {len(encodings)} known faces.")
    return encodings, names


def main():
    known_encodings, known_names = load_known_faces(KNOWN_FACES_DIR)

    if len(known_encodings) == 0:
        print("[!] No known faces loaded. Add face images to the 'known_people' folder and try again.")
        sys.exit(1)

    # Open webcam
    video = cv2.VideoCapture(0)
    if not video.isOpened():
        print("[!] Could not open webcam. Exiting.")
        sys.exit(1)

    prev_time = time.time()
    while True:
        ret, frame = video.read()
        if not ret:
            print("[!] Failed to read frame from webcam.")
            break

        # Resize frame for faster processing
        small_frame = cv2.resize(frame, (0, 0), fx=FRAME_RESIZE_SCALE, fy=FRAME_RESIZE_SCALE)

        # Convert BGR (OpenCV) to RGB (face_recognition)
        rgb_small_frame = small_frame[:, :, ::-1]

        # Find all faces and face encodings in the current frame
        face_locations = face_recognition.face_locations(rgb_small_frame, model=MODEL)
        face_encodings = face_recognition.face_encodings(rgb_small_frame, face_locations)

        names_in_frame = []

        for face_encoding in face_encodings:
            # Compare to known faces
            matches = face_recognition.compare_faces(known_encodings, face_encoding, tolerance=TOLERANCE)
            name = "Unknown"

            # Use the shortest distance to find the best match
            face_distances = face_recognition.face_distance(known_encodings, face_encoding)
            if len(face_distances) > 0:
                best_idx = np.argmin(face_distances)
                if matches[best_idx]:
                    name = known_names[best_idx]

            names_in_frame.append(name)

        # Draw boxes and labels
        for (top, right, bottom, left), name in zip(face_locations, names_in_frame):
            # Scale back up face locations since the frame we detected in was scaled to small_frame
            top = int(top / FRAME_RESIZE_SCALE)
            right = int(right / FRAME_RESIZE_SCALE)
            bottom = int(bottom / FRAME_RESIZE_SCALE)
            left = int(left / FRAME_RESIZE_SCALE)

            # Draw rectangle
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 255, 0), FRAME_THICKNESS)

            # Label background
            text = name
            text_size, _ = cv2.getTextSize(text, cv2.FONT_HERSHEY_SIMPLEX, 0.75, FONT_THICKNESS)
            text_w, text_h = text_size
            cv2.rectangle(frame, (left, bottom + 5), (left + text_w + 10, bottom + 5 + text_h + 10), (0, 255, 0), cv2.FILLED)
            cv2.putText(frame, text, (left + 5, bottom + text_h + 8), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 0, 0), FONT_THICKNESS)

        # FPS
        if SHOW_FPS:
            curr_time = time.time()
            fps = 1.0 / (curr_time - prev_time) if curr_time != prev_time else 0.0
            prev_time = curr_time
            cv2.putText(frame, f"FPS: {fps:.1f}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

        cv2.imshow("Face Recognition (q to quit)", frame)

        # Press 'q' to quit
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    video.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()

