from PIL import Image
import os
import shutil


def print_done(name):
    size1 = os.path.getsize(folder + '/' + name)
    size2 = os.path.getsize(folder2 + '/' + name)

    if size2 > size1:
        shutil.copy(folder + '/' + name, folder2 + '/' + name, follow_symlinks=True)
        size2 = size1

    if size2 == size1:
        print(name, ' NO CHANGE (0%)')
        return

    print(name, ': ',
          str(round(size1 / 1024, 3)) + ' KB -> ',
          str(round(size2 / 1024, 3)) + ' KB ',
          '(-', round(((size1 - size2) / size1) * 100), '%)',
          sep='')


folder = input('Enter platform name: ') + "_emoji"
folder2 = folder + '2'

directory = os.fsencode(folder)
if not os.path.exists(folder2):
    os.makedirs(folder2)

for file in os.listdir(directory):
    filename = os.fsdecode(file)
    if filename.endswith(".png"):
        try:
            picture = Image.open(folder + '/' + filename)
            # picture.thumbnail((66, 66), Image.ANTIALIAS)
            picture_8bit = picture.convert(mode='P', palette=Image.ADAPTIVE)
            picture_8bit.save(folder2 + '/' + filename)
        except:
            shutil.copy(folder + '/' + filename, folder2 + '/' + filename, follow_symlinks=True)

        print_done(filename)
