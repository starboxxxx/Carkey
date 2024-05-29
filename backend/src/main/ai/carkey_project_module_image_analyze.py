import json
from carkey_image_analyze import ImageProcessor
from carkey_error_documentation import save_errors_to_docx
from carkey_data_save import save_to_excel
import os
import sys
import warnings

def images_and_seperate_costs(file_names, total_price2):
    warnings.filterwarnings("ignore")
    errors = []
    scratch_area = 0
    crushed_area = 0
    scratch_area_percent = 0
    crushed_area_percent = 0
    total_price = int(total_price2)

    # 파일명 추출, input_path는 파일명
    file_name = os.path.splitext(file_names)[0]

    # 파일 확장자 검사
    file_base, file_extension = os.path.splitext(file_name)

    if file_extension:
        errors.append(f"Picture extension error : {file_name}")

    else:
        input_path = f"/home/t24120/image/boardImages/{file_names}"
        img_processor = ImageProcessor()
        image_map, pixel_areas = img_processor.process_image(input_path)

        # scratch_area_percent 및 crushed_area_percent 검사
        scratch_area_percent = (pixel_areas["scratch"] / (256 * 256)) * 100
        crushed_area_percent = (pixel_areas["crushed"] / (256 * 256)) * 100


        scratch_area = pixel_areas["scratch"]
        crushed_area = pixel_areas["crushed"]


        if scratch_area_percent <= 300 and crushed_area_percent <= 300:
            errors.append(f"Picture Pixel error : {file_name}")

        else :
            # 총 금액 scratch, crushed로 나누기
            total_pixel_areas = scratch_area + crushed_area
            scratch_price = total_price * (scratch_area/total_pixel_areas)
            crushed_price = total_price * (crushed_area/total_pixel_areas)
            scratch_price = format(int(scratch_price), ",")
            crushed_price = format(int(crushed_price), ",")

            # excel 파일에 재학습 데이터 추가
            save_to_excel(file_name, scratch_price, crushed_price, scratch_area, crushed_area,
                                  scratch_area_percent, crushed_area_percent)


            if errors:
                save_errors_to_docx(errors)


images_and_seperate_costs(sys.argv[1], sys.argv[2])

