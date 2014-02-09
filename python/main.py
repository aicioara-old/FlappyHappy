import autopy
import time
import os

def jump():
	autopy.key.tap(' ', 0)

def loadBirds():
	
	global birds
	global feather

	birds = []
	for f in os.listdir(os.getcwd() + "/data/birds"):
		f = "data/birds/" +  f
		birds.append(autopy.bitmap.Bitmap.open(f))

	feather = autopy.bitmap.Bitmap.open("data/feather.png")


def getBirdPosition():
	screen = autopy.bitmap.capture_screen()
	position = screen.find_bitmap(feather, 0.02)
	return position
	
	
def main():
	loadBirds()
	while 1:
		print getBirdPosition()
		time.sleep(1)


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

