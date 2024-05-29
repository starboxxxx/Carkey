import torch
import cv2
import numpy as np
import segmentation_models_pytorch as smp

class ImageProcessor:
    def __init__(self):
        self.models, self.device = self.load_models()
        self.labels = ['original', 'scratch', 'crushed']

    def load_models(self):
        labels = ['scratch', 'crushed']
        models = []
        n_classes = 2
        device = 'cuda' if torch.cuda.is_available() else 'cpu'
        model_labels = ['Scratch', 'Crushed']

        for label in model_labels:
            model_path = f'/home/t24120/carkey_v1.0/backend/src/main/ai/unet_trained_models/[{label}]Unet.pt'
            model = smp.Unet(encoder_name='resnet34', encoder_weights='imagenet', in_channels=3, classes=n_classes).to(device)
            model.load_state_dict(torch.load(model_path, map_location=torch.device(device)))
            model.eval()
            models.append(model)
        return models, device

    def convert_to_rgb(self, img_output):
        label_colors = {
            1: (255, 0, 0),  # scratch (빨간색)
            2: (255, 0, 0)  # crash (파란색)
        }

        img_rgb = np.zeros((img_output.shape[0], img_output.shape[1], 3), dtype=np.uint8)

        for label, color in label_colors.items():
            img_rgb[img_output == label] = color

        return img_rgb

    def overlay_image(self, original, overlay, alpha=0.5):
        original_height, original_width = original.shape[:2]

        # overlay 이미지를 원본 이미지와 동일한 크기로 리사이즈
        overlay_resized = cv2.resize(overlay, (original_width, original_height))

        # 두 이미지의 채널 수가 동일한지 확인하고 맞추기 위해 필요한 경우 변환 수행
        if original.shape[2] != overlay_resized.shape[2]:
            if original.shape[2] == 3 and overlay_resized.shape[2] == 1:
                overlay_resized = cv2.cvtColor(overlay_resized, cv2.COLOR_GRAY2BGR)
            elif original.shape[2] == 1 and overlay_resized.shape[2] == 3:
                original = cv2.cvtColor(original, cv2.COLOR_GRAY2BGR)

        # 가중치를 적용하여 두 이미지 합성
        return cv2.addWeighted(overlay_resized, alpha, original, 1 - alpha, 0)

    def process_image(self, img_path):
        img = cv2.imread(img_path)
        original_img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = cv2.resize(original_img, (256, 256))

        img_input = img / 255.
        img_input = img_input.transpose([2, 0, 1])
        img_input = torch.tensor(img_input).float().to(self.device)
        img_input = img_input.unsqueeze(0)

        image_map = {}
        pixel_areas = {}

        for i, model in enumerate(self.models):
            output = model(img_input)
            img_output = torch.argmax(output, dim=1).detach().cpu().numpy()
            img_output = np.squeeze(img_output)

            # 예측된 레이블을 RGB 색상으로 변환한 이미지
            img_rgb = self.convert_to_rgb(img_output)

            label = self.labels[i + 1]

            # 오버레이 이미지 생성
            overlay_img = self.overlay_image(original_img, img_rgb, alpha=0.5)

            image_map[label] = overlay_img
            pixel_areas[label] = np.sum(img_output)

        image_map['original'] = original_img

        return image_map, pixel_areas