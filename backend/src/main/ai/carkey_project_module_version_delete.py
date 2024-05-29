import os
import glob
import json
import sys

def delete_files_by_version(delete_version):
    deleted_files = []  # 삭제된 파일 목록
    errors = []  # 에러 목록

    # 각 경로에 대해 삭제할 파일 탐색
    paths = ['/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model', '/home/t24120/image/ai/loss_image', '/home/t24120/image/ai/model_performance']
    extensions = ['*.py', '*.pkl', '*.pt', '*.jpg']  # 삭제 대상 파일 확장자

    files_to_delete = []
    for path in paths:
        for ext in extensions:
            # 경로와 확장자를 조합하여 삭제 대상 파일 탐색
            files_to_delete += glob.glob(os.path.join(path, f'*{delete_version}*{ext}'))

    # 찾은 파일들을 삭제
    for file_path in files_to_delete:
        try:
            os.remove(file_path)
            deleted_files.append(file_path)  # 삭제 성공 시, 리스트에 추가
        except Exception as e:
            error_message = f'파일 삭제 중 오류 발생: {file_path}, 오류: {e}'
            errors.append(error_message)  # 에러 발생 시, 에러 메시지를 리스트에 추가

    if errors:
        print("오류 메시지:", errors)
        # save_errors_to_docx(errors)  # 에러 문서화 함수 호출 주석 처리 (필요 시 주석 해제)

    print(json.dumps(deleted_files))  # 삭제된 파일의 목록 출력

delete_files_by_version(sys.argv[1])

