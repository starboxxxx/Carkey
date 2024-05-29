import matplotlib.pyplot as plt
import os

plt.rcParams['font.size'] = 12
plt.rcParams['font.weight'] = 'bold'

def plot_pie_chart(dir_path, scratch, crushed, highest_num):
    highest_picture_version = float(highest_num)

    total = scratch + crushed
    values = [scratch, crushed]
    labels = ['Scratch', 'Crushed']

    colors = ['skyblue', 'grey'] if scratch > crushed else ['grey', 'skyblue']

    # 원형 그래프
    plt.figure(figsize=(6, 6))
    patches, texts, autotexts = plt.pie(values, labels=labels, colors=colors,
                                        autopct='%1.1f%%',
                                        startangle=90,
                                        wedgeprops={'edgecolor': 'black', 'linewidth': 2})

    for autotext in autotexts:
        autotext.set_color('white')
        autotext.set_fontsize(14)

    plt.axis('equal')

    # 이미지 파일 이름 설정
    circle_image_name = f'circle_image_v{highest_picture_version:.1f}.jpg'
    image_path = os.path.join(f'{dir_path}/' + circle_image_name)

    # 그래프 이미지로 저장
    plt.savefig(image_path)
    plt.close()

    return f'{circle_image_name}'