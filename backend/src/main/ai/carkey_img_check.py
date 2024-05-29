import os

def check_file_extension(file_path):
    _, file_extension = os.path.splitext(file_path)
    if file_extension.lower() != '.jpg':
        print("이미지 오류")
    return None