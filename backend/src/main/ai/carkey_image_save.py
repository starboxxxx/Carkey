import os
import re


def find_next_available_filename(folder_path, file_pattern):
    max_number = 0
    for filename in os.listdir(folder_path):
        match = re.match(file_pattern, filename)
        if match:
            current_number = int(match.group(1))
            max_number = max(max_number, current_number)
    return max_number + 1
