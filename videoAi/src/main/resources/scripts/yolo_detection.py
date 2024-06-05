import sys
from ultralytics import YOLO

def detect_objects(image_path):
    model = YOLO('yolov8x.pt')
    results = model(image_path)
    objects = []
    for result in results:
        for box in result.boxes:
            objects.append(box.cls)
    return objects

if __name__ == "__main__":
    image_path = sys.argv[1]
    detected_objects = detect_objects(image_path)
    print(detected_objects)
