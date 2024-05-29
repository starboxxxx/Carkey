import json
import pandas as pd
import numpy as np
import torch
import torch.nn as nn
import torch.optim as optim
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import RobustScaler
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import r2_score
import torch.utils.data
import re
import os
import joblib
import matplotlib.pyplot as plt

class ComplexRNNModel_train(nn.Module):
    def __init__(self, epochs, model_dir):
        super(ComplexRNNModel_train, self).__init__()
        self.epochs = epochs
        self.model_dir = model_dir
        self.data_prepared = False

        # 모델 정의
        self.rnn = nn.RNN(input_size=2, hidden_size=50, num_layers=4, batch_first=True, bidirectional=True)
        self.dropout1 = nn.Dropout(0.4)
        self.fc1 = nn.Linear(50 * 2, 100)
        self.relu = nn.ReLU()
        self.dropout2 = nn.Dropout(0.4)
        self.fc2 = nn.Linear(100, 2)

        # 손실 함수 및 최적화 방법
        self.criterion = nn.SmoothL1Loss()
        self.optimizer = optim.SGD(self.parameters(), lr=0.001, momentum=0.9)

        if not self.data_prepared:
            self.prepare_data()
            self.data_prepared = True

        # 데이터 불러오기 및 전처리
        self.model_version = self.load_model()
        self.prepare_data()
        self.train_model()
        self.evaluate_model()

        # 모델 버전별 저장
        self.model_version += 0.1
        self.save_model()

    def forward(self, x):
        x, _ = self.rnn(x)
        x = self.dropout1(x)
        x = self.fc1(x[:, -1, :])
        x = self.relu(x)
        x = self.dropout2(x)
        x = self.fc2(x)
        return x

    def prepare_data(self):
        df = pd.read_excel('/home/t24120/carkey_v1.0/backend/src/main/ai/price_predict.xlsx')
        df['crushed_price'] = df['crushed_price'].astype(str).str.replace(',', '').astype(np.float32)
        df['scratch_price'] = df['scratch_price'].astype(str).str.replace(',', '').astype(np.float32)

        X = df[['scratch_area_percent', 'crushed_area_percent']].values.astype(np.float32)
        Y = df[['crushed_price', 'scratch_price']].values.astype(np.float32)

        # 스케일러 경로 설정 (모델 저장 경로와 동일한 위치에 저장)
        self.scaler_X_path = os.path.join(self.model_dir, 'scaler_X.pkl')
        self.scaler_Y_path = os.path.join(self.model_dir, 'scaler_Y.pkl')

        # 스케일러 학습 및 저장
        scaler_X = RobustScaler()
        scaler_Y = RobustScaler()

        X_scaled = scaler_X.fit_transform(X)
        Y_scaled = scaler_Y.fit_transform(Y)

        joblib.dump(scaler_X, self.scaler_X_path)
        joblib.dump(scaler_Y, self.scaler_Y_path)

        X_train, X_temp, Y_train, Y_temp = train_test_split(X_scaled, Y_scaled, test_size=0.2, random_state=15)

        # X_temp와 Y_temp를 나눠 test 데이터 정의
        X_val, X_test, Y_val, Y_test = train_test_split(X_temp, Y_temp, test_size=0.5, random_state=15)

        self.X_test, self.Y_test = X_test, Y_test
        self.train_dataset = torch.utils.data.TensorDataset(torch.tensor(X_train).unsqueeze(1), torch.tensor(Y_train))
        self.train_loader = torch.utils.data.DataLoader(dataset=self.train_dataset, batch_size=16, shuffle=True)

        # 검증 데이터셋
        self.val_dataset = torch.utils.data.TensorDataset(torch.tensor(X_val).unsqueeze(1), torch.tensor(Y_val))
        self.val_loader = torch.utils.data.DataLoader(dataset=self.val_dataset, batch_size=16, shuffle=False)

        self.scaler_X = scaler_X
        self.scaler_Y = scaler_Y

    def train_model(self):
        val_loss_values = []
        for epoch in range(self.epochs):
            self.train()
            total_loss = 0
            for inputs, targets in self.train_loader:
                self.optimizer.zero_grad()
                outputs = self(inputs.float())
                loss = self.criterion(outputs, targets)
                loss.backward()
                self.optimizer.step()
                total_loss += loss.item()

            # 검증 단계 시작
            self.eval()
            total_val_loss = 0
            with torch.no_grad():
                for val_inputs, val_targets in self.val_loader:
                    val_outputs = self(val_inputs.float())
                    val_loss = self.criterion(val_outputs, val_targets)
                    total_val_loss += val_loss.item()
            avg_val_loss = total_val_loss / len(self.val_loader)
            val_loss_values.append(avg_val_loss)

            # 검증 손실 값 변화 그래프 그리기
            plt.figure(figsize=(10, 5))
            plt.plot(val_loss_values, label='Validation Loss')
            plt.title('Validation Loss Decrease')
            plt.xlabel('Epoch')
            plt.ylabel('Validation Loss Value')
            plt.legend()
            plt.grid(True)

            val_image_version = self.model_version + 0.1

            # 이미지 파일 이름 설정
            self.val_image_name = f'loss_image_RNN_v{val_image_version:.1f}.jpg'
            image_path = os.path.join('/home/t24120/image/ai/loss_image', self.val_image_name)

            # 그래프 이미지로 저장
            plt.savefig(image_path)
            plt.close()



    def evaluate_model(self):
        self.eval()
        # 테스트 데이터셋 준비
        test_dataset = torch.utils.data.TensorDataset(torch.tensor(self.X_test).unsqueeze(1), torch.tensor(self.Y_test))
        test_loader = torch.utils.data.DataLoader(dataset=test_dataset, batch_size=16, shuffle=False)

        absolute_errors = []  # 평균 절대 오차를 저장할 리스트
        outputs_list = []  # 모델 출력을 저장할 리스트
        targets_list = []  # 실제 타겟 값을 저장할 리스트

        with torch.no_grad():  # 기울기 계산을 비활성화
            for inputs, targets in test_loader:
                outputs = self(inputs.float())
                outputs_list.append(outputs.numpy())
                targets_list.append(targets.numpy())

        # 리스트들을 NumPy 배열로 변환
        outputs_np = np.concatenate(outputs_list, axis=0)
        targets_np = np.concatenate(targets_list, axis=0)

        # R^2 Score 계산
        r2 = r2_score(targets_np, outputs_np)

        # 시각화
        plt.figure(figsize=(10, 5))

        # 실제 값 vs 예측 값 선 그래프
        plt.subplot(1, 2, 1)
        plt.plot(targets_np, label='Actual')
        plt.plot(outputs_np, label='Predicted')
        plt.title('Actual vs Predicted')
        plt.xlabel('Samples')
        plt.ylabel('Value')
        plt.legend()

        # 오차 분포 히스토그램
        plt.subplot(1, 2, 2)
        errors = targets_np - outputs_np
        plt.hist(errors, bins=50)
        plt.title('Prediction Error Distribution')
        plt.xlabel('Error')
        plt.ylabel('Frequency')
        plt.tight_layout()

        model_performance_image_version = self.model_version + 0.1

        # 이미지 파일 이름 설정
        performance_image_name = f'model_performance_image_RNN_v{model_performance_image_version:.1f}.jpg'
        image_path = os.path.join('/home/t24120/image/ai/model_performance', performance_image_name)

        # 그래프 이미지로 저장
        plt.savefig(image_path)
        plt.close()

        result = {
            "lossImageName" : self.val_image_name,
            "modelPerformanceImageName": performance_image_name,
            "r2Score": f'{r2}',
            "rnnVersion": f'v{model_performance_image_version:.1f}'
        }

        print(json.dumps(result))

    def load_model(self):
        max_version = 0
        pattern = re.compile(r'model_RNN_v(\d+\.\d+)\.pt')

        if not os.path.exists(self.model_dir):
            os.makedirs(self.model_dir)
        else:
            for filename in os.listdir(self.model_dir):
                match = pattern.match(filename)
                if match:
                    version = float(match.group(1))
                    if version > max_version:
                        max_version = version
                        self.load_state_dict(torch.load(os.path.join(self.model_dir, filename)))

        return max_version

    def save_model(self):
        model_name = f'model_RNN_v{self.model_version:.1f}.pt'
        model_path = os.path.join(self.model_dir, model_name)
        torch.save(self.state_dict(), model_path)

        # 스케일러를 새로운 버전으로 저장
        scaler_X_versioned_path = os.path.join(self.model_dir, f'scaler_X_v{self.model_version:.1f}.pkl')
        scaler_Y_versioned_path = os.path.join(self.model_dir, f'scaler_Y_v{self.model_version:.1f}.pkl')

        # 최근 버전의 스케일러 파일을 새 경로에 복사
        os.rename(self.scaler_X_path, scaler_X_versioned_path)
        os.rename(self.scaler_Y_path, scaler_Y_versioned_path)
