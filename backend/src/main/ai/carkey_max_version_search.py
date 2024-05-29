import os
import re

def find_highest_version_file(version_dir_path):
    max_version = 0.0
    highest_version_file = None
    pattern = re.compile(r'v(\d+\.\d+)')

    # 디렉토리가 존재하는지 확인
    if not os.path.exists(version_dir_path):
        print(f"Directory '{version_dir_path}' does not exist.")
        return None

    # 디렉토리 내의 모든 파일을 순회하며 가장 높은 버전을 찾음
    for filename in os.listdir(version_dir_path):
        match = pattern.search(filename)
        if match:
            version = float(match.group(1))
            if version > max_version:
                max_version = version
                highest_version_file = filename

    if highest_version_file:
        return f'{max_version}'
    else:
        return '0.0'

