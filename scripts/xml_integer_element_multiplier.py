#!/usr/bin/python3
"""
1. Get's xml file name as an argument
2. Searching for an attribute 'xml_elemet' (constant currently) and parsing it's (integer) value
3. multiply parsed integer with constant 'multiplier'
"""
import sys
import re
import os

multiplier = 1.44

#
#open file
#
if len(sys.argv) != 2:
    print("You have to specify path to the file as the parameter\n")
    exit()
print("working with file: {0}\n".format(sys.argv[1]))
srcFileName = os.path.basename(sys.argv[1])
srcFilePath = os.path.dirname(sys.argv[1])
if srcFilePath != '':
    os.chdir(srcFilePath)
srcFile = open(srcFileName, "r")

#
#tmp file
#
tmpFileName = "~{0}".format(srcFileName)
tmpFile = open(tmpFileName, "w")

#xml_elemet = "<attack_radius" 
xml_elemet = "<view_radius"

for line in srcFile:
    newLine = line
    if xml_elemet in newLine:
        matched = re.search(r"\b\d+\b", newLine)
        if matched is None:
            raise ValueError("can't find integer value in the line with give element")
        val = int(newLine[matched.start():matched.end()])
        val = int(float(val) * multiplier)
        newLine = "{0}{1}{2}".format(newLine[0:matched.start()], val, newLine[matched.end():])
    tmpFile.write(newLine)

if not srcFile.closed:
    srcFile.close()

if not tmpFile.closed:
    tmpFile.close()

os.remove(srcFileName)
os.rename(tmpFileName, srcFileName)
