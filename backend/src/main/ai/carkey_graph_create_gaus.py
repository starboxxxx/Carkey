import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import norm
import seaborn as sns
import os

def draw_normal_distribution_graph_with_gradient(dir_path, total_amount, highest_num):
    highest_picture_version = float(highest_num)

    sns.set(style="whitegrid")

    range_amount = 50000
    mean = total_amount
    std = range_amount / 3

    x = np.linspace(mean - 3 * std, mean + 3 * std, 1000)
    y = norm.pdf(x, mean, std)

    plt.figure(figsize=(10, 6))

    plt.fill_between(x, 0, y, color = "skyblue", alpha = 0.24)

    # Normal Min에서 Normal Max까지 더 진한 색상 적용
    x_highlight = np.linspace(mean - std, mean + std, 1000)
    y_highlight = norm.pdf(x_highlight, mean, std)
    plt.fill_between(x_highlight, 0, y_highlight, color="skyblue", alpha= 0.24)

    plt.plot(x, y, color="dodgerblue", linestyle='-', linewidth=2.5)

    # 수직선과 텍스트 주석 추가
    plt.axvline(x=mean - std, color='grey', linestyle='--', linewidth=2)
    plt.text(mean - std, np.max(y) * 1.07, 'Normal Min', horizontalalignment='center', color='black', fontsize=12)
    plt.axvline(x=mean, color='dimgray', linestyle='-', linewidth=2)
    plt.text(mean, np.max(y) * 1.07, 'Normal', horizontalalignment='center', color='black', fontsize=12)
    plt.axvline(x=mean + std, color='grey', linestyle='--', linewidth=2)
    plt.text(mean + std, np.max(y) * 1.07, 'Normal Max', horizontalalignment='center', color='black', fontsize=12)

    plt.xticks(ticks=np.linspace(mean - 3 * std, mean + 3 * std, 7),
               labels=[f'₩{int(i):,}' for i in np.linspace(mean - 3 * std, mean + 3 * std, 7)], fontsize=10)
    plt.yticks([])

    plt.gca().spines['top'].set_visible(False)
    plt.gca().spines['right'].set_visible(False)

    # 이미지 파일 이름 설정
    gaus_image_name = f'gaus_image_v{highest_picture_version:.1f}.jpg'
    image_path = os.path.join(f'{dir_path}/' + gaus_image_name)

    # 그래프 이미지로 저장
    plt.savefig(image_path)
    plt.close()

    return f'{gaus_image_name}'
