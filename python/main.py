import autopy
import time
import os

def jump():
	autopy.key.tap(' ', 0)

def loadBirds():
	
	global birds
	global feather
	global pipe_bottom
	global screen

	# birds = []
	# for f in os.listdir(os.getcwd() + "/data/birds"):
	# 	f = "data/birds/" +  f
	# 	birds.append(autopy.bitmap.Bitmap.open(f))

	feather = autopy.bitmap.Bitmap.open("data/feather.png")
	pipe_bottom = autopy.bitmap.Bitmap.open("data/pipe_bottom.png")


def getBirdPosition():
	global screen

	screen = autopy.bitmap.capture_screen()
	position = screen.find_bitmap(feather, 0.02)
	return position

def getPipePosition(birdPos):
	global screen 

	x = birdPos[0]
	y = birdPos[1]

	#get to the sky 
	# 7390926 is #70c6ce or blue
	while screen.get_color(x, y) != 7390926 :
		y -=1
		if not autopy.screen.point_visible(x, y) :
			return None

	#get to the top of the screen
	while screen.get_color(x, y) == 7390926:
		y -=1
		if not autopy.screen.point_visible(x, y) :
			return None
	y+=1

	#get to the top of the pipe
	while screen.get_color(x, y) == 7390926:
		x += 1
		if not autopy.screen.point_visible(x, y) :
			return None

	#get to the bottom of the pipe
	while screen.get_color(x, y) != 7390926:
		y += 1
		if not autopy.screen.point_visible(x, y) :
			return None

	holeTop = (x, y)

	#get bottom of the hole
	while screen.get_color(x, y) == 7390926:
		y += 1
		if not autopy.screen.point_visible(x, y) :
			return None

	holeBottom = (x, y)

	return (holeTop, holeBottom)

	
def main():
	loadBirds()

	x = 0

	while 1:
		birdPos = getBirdPosition()

		if not birdPos:
			continue

		pipePos = getPipePosition(birdPos)
		if not pipePos:
			continue
		

		pipeTop = pipePos[0];
		pipeBottom = pipePos[1];

		if birdPos[1] < pipeBottom[1]:
			print "JUMP" + str(x)

		x += 1

		# print birdPos
		# print pipeTop
		# print pipeBottom

		time.sleep(0.5)


def screenshot():
	x = 1
	while 1:
		# autopy.bitmap.capture_screen().save('screengrab.png')
		name = "screenshot" + str(x) + ".png"
		print(name)
		x+=1

		autopy.bitmap.capture_screen().save(name)

		time.sleep(0.1)

def test():
	feather = autopy.bitmap.Bitmap.open('data/feather.png')
	for bird in birds:
		position = bird.find_bitmap(feather, 0.01)
		if position:
			print bird


# loadBirds()


main()

