(define (problem strips-sat-x-1)
(:domain satellite)
(:objects
	satellite0 - satellite
	instrument0 - instrument
	satellite1 - satellite
	instrument1 - instrument
	satellite2 - satellite
	instrument2 - instrument
	instrument3 - instrument
	instrument4 - instrument
	instrument5 - instrument
	instrument6 - instrument
	satellite3 - satellite
	instrument7 - instrument
	instrument8 - instrument
	instrument9 - instrument
	instrument10 - instrument
	instrument11 - instrument
	satellite4 - satellite
	instrument12 - instrument
	thermograph7 - mode
	image0 - mode
	spectrograph1 - mode
	infrared3 - mode
	image4 - mode
	thermograph5 - mode
	thermograph6 - mode
	spectrograph2 - mode
	Star1 - direction
	Star11 - direction
	Star5 - direction
	GroundStation4 - direction
	GroundStation8 - direction
	Star9 - direction
	GroundStation3 - direction
	Star12 - direction
	GroundStation2 - direction
	Star6 - direction
	Star13 - direction
	GroundStation14 - direction
	Star7 - direction
	GroundStation10 - direction
	Star0 - direction
	Planet15 - direction
	Star16 - direction
	Star17 - direction
	Phenomenon18 - direction
	Planet19 - direction
	Star20 - direction
	Planet21 - direction
	Phenomenon22 - direction
	Planet23 - direction
	Planet24 - direction
	Phenomenon25 - direction
	Planet26 - direction
	Planet27 - direction
	Star28 - direction
	Star29 - direction
	Star30 - direction
	Star31 - direction
	Star32 - direction
	Planet33 - direction
	Star34 - direction
	Star35 - direction
	Star36 - direction
	Phenomenon37 - direction
	Planet38 - direction
	Star39 - direction
)
(:init
	(supports instrument0 image0)
	(calibration_target instrument0 GroundStation2)
	(calibration_target instrument0 Star13)
	(calibration_target instrument0 GroundStation3)
	(on_board instrument0 satellite0)
	(power_avail satellite0)
	(pointing satellite0 Phenomenon37)
	(supports instrument1 thermograph7)
	(supports instrument1 infrared3)
	(calibration_target instrument1 GroundStation10)
	(calibration_target instrument1 Star6)
	(on_board instrument1 satellite1)
	(power_avail satellite1)
	(pointing satellite1 GroundStation14)
	(supports instrument2 thermograph7)
	(calibration_target instrument2 Star9)
	(calibration_target instrument2 GroundStation2)
	(calibration_target instrument2 Star1)
	(calibration_target instrument2 GroundStation4)
	(calibration_target instrument2 GroundStation10)
	(supports instrument3 thermograph7)
	(supports instrument3 spectrograph2)
	(supports instrument3 spectrograph1)
	(calibration_target instrument3 Star12)
	(supports instrument4 spectrograph1)
	(supports instrument4 spectrograph2)
	(supports instrument4 image4)
	(calibration_target instrument4 Star0)
	(calibration_target instrument4 GroundStation8)
	(calibration_target instrument4 Star7)
	(calibration_target instrument4 Star5)
	(supports instrument5 image4)
	(supports instrument5 spectrograph2)
	(calibration_target instrument5 Star7)
	(calibration_target instrument5 Star5)
	(calibration_target instrument5 Star6)
	(calibration_target instrument5 Star11)
	(supports instrument6 image0)
	(supports instrument6 infrared3)
	(calibration_target instrument6 GroundStation8)
	(calibration_target instrument6 Star6)
	(calibration_target instrument6 GroundStation4)
	(calibration_target instrument6 GroundStation10)
	(on_board instrument2 satellite2)
	(on_board instrument3 satellite2)
	(on_board instrument4 satellite2)
	(on_board instrument5 satellite2)
	(on_board instrument6 satellite2)
	(power_avail satellite2)
	(pointing satellite2 Planet21)
	(supports instrument7 spectrograph2)
	(calibration_target instrument7 Star6)
	(calibration_target instrument7 Star9)
	(supports instrument8 image0)
	(supports instrument8 infrared3)
	(calibration_target instrument8 GroundStation3)
	(supports instrument9 spectrograph1)
	(supports instrument9 infrared3)
	(calibration_target instrument9 Star12)
	(supports instrument10 infrared3)
	(calibration_target instrument10 GroundStation2)
	(calibration_target instrument10 GroundStation10)
	(calibration_target instrument10 Star12)
	(supports instrument11 thermograph6)
	(supports instrument11 thermograph5)
	(supports instrument11 image4)
	(calibration_target instrument11 Star7)
	(calibration_target instrument11 GroundStation14)
	(calibration_target instrument11 Star13)
	(calibration_target instrument11 Star6)
	(on_board instrument7 satellite3)
	(on_board instrument8 satellite3)
	(on_board instrument9 satellite3)
	(on_board instrument10 satellite3)
	(on_board instrument11 satellite3)
	(power_avail satellite3)
	(pointing satellite3 Star16)
	(supports instrument12 spectrograph2)
	(calibration_target instrument12 Star0)
	(calibration_target instrument12 GroundStation10)
	(on_board instrument12 satellite4)
	(power_avail satellite4)
	(pointing satellite4 Planet26)
)
(:goal (and
	(have_image Planet15 thermograph5)
	(have_image Planet15 image4)
	(have_image Star16 spectrograph1)
	(have_image Star17 spectrograph2)
	(have_image Phenomenon18 image0)
	(have_image Planet19 image4)
	(have_image Planet19 image0)
	(have_image Star20 spectrograph2)
	(have_image Planet21 image0)
	(have_image Planet21 thermograph6)
	(have_image Phenomenon22 spectrograph1)
	(have_image Planet23 image4)
	(have_image Planet23 thermograph5)
	(have_image Planet24 infrared3)
	(have_image Planet24 thermograph5)
	(have_image Phenomenon25 spectrograph2)
	(have_image Phenomenon25 thermograph6)
	(have_image Planet26 thermograph7)
	(have_image Planet26 image4)
	(have_image Star29 thermograph7)
	(have_image Star29 spectrograph1)
	(have_image Star30 spectrograph1)
	(have_image Star31 thermograph6)
	(have_image Star31 thermograph7)
	(have_image Star32 image4)
	(have_image Planet33 infrared3)
	(have_image Planet33 thermograph6)
	(have_image Star35 thermograph6)
	(have_image Star35 spectrograph2)
	(have_image Phenomenon37 spectrograph2)
	(have_image Planet38 infrared3)
	(have_image Star39 spectrograph2)
))
(:metric minimize (total-time))

)
