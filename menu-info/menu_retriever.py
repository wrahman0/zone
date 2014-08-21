from glob import glob
from os import rename, listdir
import urllib2
import json
import re 


def getMenuItems(lowerBound, upperBound):
	url_first = "https://api.uwaterloo.ca/v2/foodservices/products/"
	url_last = ".json?key=1775694fa7f453e7f7605169f8d6fab1"
	for x in range(lowerBound, upperBound):
		url_final = url_first + str(x) + url_last
		response = urllib2.urlopen(url_final)
		data = json.load(response)
		if len(data["data"].keys()) > 0:
			print data
			filename = "menu-json/" + data["data"]["product_name"].replace("/","") + ".txt"
			# Replacing regex
			print "Filename is: " + filename
			with open(filename, 'w') as outfile:
				json.dump(data, outfile)	


def removeInvalidNames():
	fnames = glob("rename-test/*")
	for name in fnames:
		if hasInvalidWord(name):
			print "INVALID NAME DETECTED: " + name


def unique(items):
    found = set([])
    keep = []

    for item in items:
        if item not in found:
            found.add(item)
            keep.append(item)

    return keep


#Returns keywords from an item name
def getKeyWords(itemName):
	#seperate words by spaces
	wordsToAnalyze = itemName.split()
	print wordsToAnalyze

	#Run it against this database
	invalid_words = ['is', 'the', 'with', 'w', 'to', 'morning', 'red', 'and', 'on', 'a', '(2013)', '(2014)', 'of']

	master = []
	for word in wordsToAnalyze:
		

		if word.isdigit():
			wordsToAnalyze.remove(word)

		if '/' in word:
			word = word[word.index('/')+1:]

		if '.' in word:
			word = word[:word.index('.')]

		if ',' in word:
			word = word[:word.index(',')]

		if ')' in word:
			word = word[word.index(')') + 1:]

		if '(' in word:
			word.replace('(','')

		if word != "" and len(word) > 3 and word not in invalid_words:
			master.append(word.lower())


	print "Returning: " + str(master)
	return master


def getTags():
	tags = []
	fnames = glob("menu-json/*")
	for name in fnames:
		tags += getKeyWords(name)
	tags = unique(tags)
	print tags
	text_file = open("tags-1.txt", "w")
	for tag in tags:
		text_file.write(tag + "\n")
	text_file.close()


getTags()


