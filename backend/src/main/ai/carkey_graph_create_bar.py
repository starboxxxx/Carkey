import matplotlib.pyplot as plt
import os

def plot_r2_accuracy(dir_path, r2_score, highest_num):
    highest_picture_version = float(highest_num)

    accuracy = r2_score * 100

    plt.figure(figsize=(10, 2))

    plt.barh(['R2 Accuracy'], [100], color='lightgray', edgecolor='black', linewidth=2)

    bars = plt.barh(['R2 Accuracy'], [accuracy], color='skyblue', edgecolor='black', linewidth=1)

    plt.xlim(0, 100)

    for bar in bars:
        plt.text(bar.get_width() - 5, bar.get_y() + bar.get_height()/2,
                 f'{accuracy:.2f}%', fontsize=13, fontweight='bold',
                 va='center', ha='right', color='white')

    plt.gca().spines['top'].set_visible(False)
    plt.gca().spines['right'].set_visible(False)
    plt.gca().spines['left'].set_visible(False)
    plt.gca().get_yaxis().set_visible(False)

    plt.xticks(fontsize=10)
    plt.tight_layout()

    # 이미지 파일 이름 설정
    bar_image_name = f'bar_image_v{highest_picture_version:.1f}.jpg'
    image_path = os.path.join(f'{dir_path}/' + bar_image_name)

    # 그래프 이미지로 저장
    plt.savefig(image_path)
    plt.close()

    return f'{bar_image_name}'