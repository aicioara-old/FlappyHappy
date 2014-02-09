import sys
import autopy
import time
import os

def jump():
	autopy.mouse.click()


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

def getPipePosition(birdPos, prev):
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

	# if the bird is under the pipe then return the 
	# bounds for the previous pair
	if prev != None and screen.get_color(birdPos[0], prev[0][1] - 2) != 7390926:
		return prev

	#get to the top of the pipe
	while screen.get_color(x, y) == 7390926:
		x += 1
		if not autopy.screen.point_visible(x, y) :
			return None

	# if the bird has not yet crossed the bounds of the previous top pipe then uodate only x
	if prev != None and prev[0][0] + 60 > birdPos[0]:
		newTop = (x, prev[0][1])
		newBottom = (x, prev[1][1])
		return (newTop, newBottom)


	#get to the bottom of the pipe
	while screen.get_color(x, y) != 7390926:
		y += 1
		if not autopy.screen.point_visible(x, y) :
			return None

	holeTop = (x, y)

	#get bottom of the hole
	while screen.get_color(x, y) == 7390926 or screen.get_color(x, y)%256 <= 20:
		y += 1
		if not autopy.screen.point_visible(x, y):
			return None

	holeBottom = (x, y)

	return (holeTop, holeBottom)

	
def main():
	loadBirds()
	tts         = float(sys.argv[1])
	distBuff    = int(sys.argv[2])
	updTime     = int(sys.argv[3])
	thresh      = int(sys.argv[4])
	errorMargin = int(sys.argv[5])
	ttj         = int(sys.argv[6])
	jumpDist    = int(sys.argv[7])
	x = 0
	pipePos = None
	while 1:
		birdPos = getBirdPosition()
		
		if not birdPos:
			continue

		pipePos = getPipePosition(birdPos, pipePos)
		if not pipePos:
			continue

		pipeTop = pipePos[0]
		pipeBottom = pipePos[1]

		# jump if the bird is below the line 
		# of the bottom pipe and if a jump 
		# will not cause it to collide with the
		# line of the top pipe
		if pipeBottom[1] <= birdPos[1] + distBuff and pipeTop[1] < birdPos[1] - jumpDist :
			jump()
		x += 1


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

