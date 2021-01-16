from os import listdir
import sys, os
from os.path import isfile, join
import pprint

path = os.path.abspath(__file__)
args = len(sys.argv)
if args > 1:
    path = sys.argv[1]
else:
    pathar = path.split("\\")
    path = ""
    pathar.remove(pathar[len(pathar)-1])
    for p in pathar:
        path += p + "\\"


# files = [f for  f in listdir(path) if isfile(join(path, f))]
# print(files)

files = []


def forceDir(direc:str) -> list:
    fs = []
    entries = listdir(direc)
    for en in entries:
        if isfile(join(direc, en)):
            fs.append(join(direc, en))
        else:
            fs += forceDir(join(direc, en))
    return fs

files = forceDir(path)

pp = pprint.PrettyPrinter(indent=4)
pp.pprint(files)
# print(files)

t_lines = []

for f in files:
    of = open(f)
    lines = of.readlines()
    t_lines += lines

#pp.pprint(t_lines)
s = ""
for t in t_lines:
    s += t

print("Number of files: " + str(len(files)))
print("Number of lines: " + str(len(t_lines)))
print("Number of chars: " + str(len(s)))
