import os
import re
import json
import sys
def find_all_versions():
    version_dir_path = '/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model'
    versions = []
    pattern = re.compile(r'v(\d+\.\d+)')

    # 디렉토리가 존재하는지 확인
    if not os.path.exists(version_dir_path):
        print(json.dumps({'error': f"Directory '{version_dir_path}' does not exist."}))

    # 디렉토리 내의 모든 파일을 순회하며 모든 버전을 찾음
    for filename in os.listdir(version_dir_path):
        match = pattern.search(filename)
        if match:
            version = match.group(1)
            if version not in versions:
                versions.append(version)

    if versions:
        print(json.dumps({'versions': versions}))
    else:
        print(json.dumps({'error': 'No versions found.'}))

find_all_versions()
