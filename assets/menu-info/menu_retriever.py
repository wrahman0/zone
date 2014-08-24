from glob import glob
from os import rename, listdir
from pprint import pprint
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

def renameToId():
	fnames = glob("menu-json/*")
	
	for name in fnames:
		json_data_file = open(name)
		json_data = json.load(json_data_file)
		pprint(json_data)
		rename(name,"menu-json-text/" + str(json_data["data"]["product_id"])+ ".txt")
		json_data_file.close()


def removeSpecialChars(keyword):
	return ''.join(e for e in keyword if e.isalnum())

#Extracts tags from the given key and returns in an array
def getTagsUpdated(key):
	# key = removeSpecialChars(key)
	print "FILTERING: " +key
	tags = []
	json_file = open('tags.txt')
	for word in json_file.read().split():
		tags.append(word)

	tags_to_return = []

	#Check if indiv words are in the tags array
	for key_substr in key.split():
		if key_substr.lower() in tags:
			tags_to_return.append(key_substr.lower())

	print tags_to_return
	return tags_to_return
	

def addTagsToJson():
	fnames = glob("append-test/*")

	for name in fnames:
		json_data_file = open(name)
		json_data = json.load(json_data_file)
		pprint(json_data)
		print "Name: " + json_data['data']['product_name']
		json_data['data']['tags'] = getTagsUpdated(json_data['data']['product_name'])
		pprint(json_data)
		json_data_file.close()
		with open(name, 'w') as outfile:
				json.dump(json_data, outfile)


addTagsToJson()
# getTagsUpdated("Steamed Corn")





