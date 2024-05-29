import json
from carkey_image_save import find_next_available_filename
from carkey_price_predict_rnn import ModelPredictor
from carkey_image_analyze import ImageProcessor
from carkey_error_documentation import save_errors_to_docx
from carkey_img_check import check_file_extension
from PIL import Image
import re
import os
import sys
import warnings
from carkey_trans_image_extension import convert_image_to_jpg
from carkey_max_version_search import find_highest_version_file
from carkey_graph_create_bar import plot_r2_accuracy
from carkey_graph_create_gaus import draw_normal_distribution_graph_with_gradient
from carkey_graph_create_circle import plot_pie_chart

def images_and_predict_costs(input_path, target_version, r2_score):
    warnings.filterwarnings("ignore")
    r2Score = float(r2_score)
    errors = []
    result = convert_image_to_jpg(f"{input_path}")
    input_path = result
    natural_image = result
    # 파일 확장자 검사, 확장자 존재시 ok
    extension_error = check_file_extension(input_path)
    if extension_error:
        errors.append(f"Picture extension error : {input_path}")
        print("error")
        save_errors_to_docx(errors)

    else :
        input_path = f"/home/t24120/image/ai/image/{input_path}"

        # target 버전을 원하는 경우에 적용
        if target_version is not None:
            version = target_version

            # img_analyze model 사용
            img_processor = ImageProcessor()
            image_map, pixel_areas = img_processor.process_image(input_path)

            # scratch_area_percent 및 crushed_area_percent 검사
            scratch_area_percent = (pixel_areas["scratch"] / (256 * 256)) * 100
            crushed_area_percent = (pixel_areas["crushed"] / (256 * 256)) * 100


            # scratch percent, crushed percent가 둘다 300 이하면 종료
            scratch_area = pixel_areas["scratch"]
            crushed_area = pixel_areas["crushed"]

            if scratch_area <= 500 and crushed_area <= 500:
                errors.append(f"Picture Pixel error : {input_path}")
                result = {
                    "error":f"picture pixel error {natural_image}"
                }
                print(json.dumps(result))
                save_errors_to_docx(errors)

            else:
                base_path = "/home/t24120/image/ai"
                types = ["original", "scratch", "crushed"]
                saved_img_paths = {}

                for image_type in types:
                    folder_path = f"{base_path}/{image_type}"
                    file_pattern = re.compile(f"{image_type}_img(\\d+)\\.jpg")
                    next_number = find_next_available_filename(folder_path, file_pattern)
                    img_path = os.path.join(folder_path, f"{image_type}_img{next_number}.jpg")
                    img_name = f"{image_type}_img{next_number}.jpg"

                    image = Image.fromarray(image_map[image_type])
                    image.save(img_path)

                    saved_img_paths[f"{image_type}Img"] = img_name

                model_path = f"/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model/model_RNN_{version}.pt"
                scaler_X_path = f"/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model/scaler_X_{version}.pkl"
                scaler_Y_path = f"/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model/scaler_Y_{version}.pkl"

                # rnn모델 입력 후 가격 출력
                predictor = ModelPredictor(model_path, scaler_X_path, scaler_Y_path)
                predicted_costs = predictor.predict(scratch_area_percent, crushed_area_percent)

                # 판금수리 가격이 0인데 crushed 퍼센트가 존재하는 경우, 판금수리 가격 0으로 교체
                if crushed_area_percent == 0 and predicted_costs[0] > 0:
                    predicted_costs[0] = 0

                predicted_costs = [round(cost) for cost in predicted_costs]

                total_price = predicted_costs[0] + predicted_costs[1]

                # price_analyze 이미지 3개
                highest_picture_version = float(find_highest_version_file('/home/t24120/image/ai/price_analyze')) + 0.1
                r2Image = plot_r2_accuracy('/home/t24120/image/ai/price_analyze', r2Score, highest_picture_version)
                pixelPercentImage = plot_pie_chart('/home/t24120/image/ai/price_analyze', scratch_area, crushed_area, highest_picture_version)
                priceImage = draw_normal_distribution_graph_with_gradient('/home/t24120/image/ai/price_analyze', total_price, highest_picture_version)

                result = {
                    "totalPrice": total_price,
                    "naturalImg": natural_image,
                    "r2Image": r2Image,
                    "pixelPerformanceImage": pixelPercentImage,
                    "priceImage": priceImage,
                    **saved_img_paths
                }
                print(json.dumps(result))

                if errors:
                    save_errors_to_docx(errors)
        else:
            errors.append("error: Model version error")
            save_errors_to_docx(errors)


images_and_predict_costs(sys.argv[1], sys.argv[2], sys.argv[3])