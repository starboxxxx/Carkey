import os
import joblib
import numpy as np
import torch
import torch.nn as nn

class ModelPredictor(nn.Module):
    def __init__(self, model_path, scaler_X_path, scaler_Y_path):
        super(ModelPredictor, self).__init__()
        self.model_path = model_path
        self.scaler_X_path = scaler_X_path
        self.scaler_Y_path = scaler_Y_path

        # 모델 구조 정의
        self.rnn = nn.RNN(input_size=2, hidden_size=50, num_layers=4, batch_first=True, bidirectional=True)
        self.dropout1 = nn.Dropout(0.4)
        self.fc1 = nn.Linear(50 * 2, 100)
        self.relu = nn.ReLU()
        self.dropout2 = nn.Dropout(0.4)
        self.fc2 = nn.Linear(100, 2)

        # 모델 및 스케일러 로드
        self.load_model()
        self.load_scalers()

    def forward(self, x):
        x, _ = self.rnn(x)
        x = self.dropout1(x)
        x = self.fc1(x[:, -1, :])
        x = self.relu(x)
        x = self.dropout2(x)
        x = self.fc2(x)
        return x

    def load_model(self):
        if os.path.exists(self.model_path):
            self.load_state_dict(torch.load(self.model_path))

    def load_scalers(self):
        if os.path.exists(self.scaler_X_path):
            self.scaler_X = joblib.load(self.scaler_X_path)
        else:
            self.scaler_X = None

        if os.path.exists(self.scaler_Y_path):
            self.scaler_Y = joblib.load(self.scaler_Y_path)
        else:
            self.scaler_Y = None

    def predict(self, scratch_area_percent, crushed_area_percent):
        X = np.array([[scratch_area_percent, crushed_area_percent]], dtype=np.float32)
        if self.scaler_X is not None:
            X_scaled = self.scaler_X.transform(X)
        else:
            X_scaled = X

        self.eval()
        with torch.no_grad():
            inputs = torch.tensor(X_scaled).unsqueeze(1)
            outputs = self(inputs.float())

        predicted_values = outputs.numpy()
        if self.scaler_Y is not None:
            predicted_values = self.scaler_Y.inverse_transform(predicted_values.reshape(1, -1))
            predicted_values = predicted_values.flatten()

        return predicted_values
