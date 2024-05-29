from PIL import Image
import os

def convert_image_to_jpg(file_name):
    # 이미지 열기
    base_path = "/home/t24120/image/ai/image"
    input_path = os.path.join(base_path, f"{file_name}")
    image = Image.open(input_path)

    # RGB 모드로 변환
    if image.mode in ("RGBA", "P", "LA"):
        image = image.convert("RGB")

    # 파일 이름에서 확장자 제거하고 .jpg 확장자 추가
    output_file_name = os.path.splitext(file_name)[0] + ".jpg"
    output_path = os.path.join(base_path, output_file_name)

    # 기존 다른 확장자 이미지는 제거
    os.remove(input_path)

    # .jpg 형식으로 저장
    image.save(output_path, "JPEG")

    return output_file_name
