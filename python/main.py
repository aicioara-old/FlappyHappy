import autopy
import time
import os

def jump():
	autopy.key.tap(' ', 0)

def loadBirds():
	
	global birds

	birds = []

	for f in os.listdir(os.getcwd() + "/data/birds"):
		f = "data/birds/" +  f
		birds.append(autopy.bitmap.Bitmap.open(f))


def getBirdPosition():
	screen = autopy.bitmap.capture_screen()

	for bird in birds:
		position = screen.find_bitmap(bird)
		if position:
			return position


def loop():
	x = 1



	while 1:
		# autopy.bitmap.capture_screen().save('screengrab.png')
		name = "screenshot" + str(x) + ".png"
		print(name)
		x+=1

		autopy.bitmap.capture_screen().save(name)

		time.sleep(0.1)
	
def loop2():
	while 1:
		print getBirdPosition()
		# time.sleep(1)

def test():
	bird11 = autopy.bitmap.Bitmap.open('data/bird11.png')
	bird12 = autopy.bitmap.Bitmap.open('data/bird12.png')
	bird13 = autopy.bitmap.Bitmap.open('data/bird13.png')
	bird14 = autopy.bitmap.Bitmap.open('data/bird14.png')
	bird15 = autopy.bitmap.Bitmap.open('data/bird15.png')
	bird16 = autopy.bitmap.Bitmap.open('data/bird16.png')
	bird21 = autopy.bitmap.Bitmap.open('data/bird21.png')
	bird22 = autopy.bitmap.Bitmap.open('data/bird22.png')
	bird23 = autopy.bitmap.Bitmap.open('data/bird23.png')
	bird4 = autopy.bitmap.Bitmap.open('data/bird4.png')
	screen2 = autopy.bitmap.Bitmap.open('screenshot8.png')

	print screen2.find_bitmap(bird12)


# loadBirds()


loadBirds()
loop2()