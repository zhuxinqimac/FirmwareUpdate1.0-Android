# FirmwareUpdate1.0-Android
This is a simple Android application that can flash firmware into a TI Board(tested on TM4C1294XL) wirelessly through Bluetooth using an Android smart phone (tested with Samsung Galaxy Note3).
<p class="lead">
            Before you install the app, you need to change app's source code(in <i>Main_Act.java</i>) to match your Bluetooth module's mac address on your TI board(I am sorry to say this is a really bad thing I made in my app, because I didn't use search function to connect to a Bluetooth module. I would be very grateful if you can use search function in your own app to connect a Bluetooth module.). Then generate your own version app.
        </p>
        <p class="lead">
            Besides, you also need to put your demo binary files into your device's internal storage root folder.
        </p>
        <p class="lead">
            Then you can connect, and begin to flash (press SW1 on board first and do not leave <i> Select Firmware to Flash </i> page until finished).
        </p>
        <p class="lead">
            When the board contains an official demo, you need to press SW1 until LED2 is turned off, then start your burning. When the board contains one of my customized demos, you need to press SW1 and the four LEDs will all turn on, then start your burning.
        </p>
        <img src="../images/firmwareupdate2.jpg" alt="firmpare update 2">
        <p class="lead">You can switch to <i>Talk to Device</i> page to interact with the board.</p>
        <p class="lead">My three customized demos have different functions.</p>
        <div class="well">
            <p class="lead">
                LEDfollow.bin : you can press B1, B2, B3, or B4 button to turn the four LEDs on board on or off.
            </p>
            <p class="lead">
                LEDbreath.bin : you can press up and down to add or reduce the breathing LED frequecy; you can also press left or right to change the fluid direction.
            </p>
            <p class="lead">
                CalculatorDEMO.bin : you can input an expression ended up with a '#' in the text box to let the board calculate, and it will return the result to the receive part of this page(for instance, you can input (1+2+3)*4-5/2#) and the board would return (1+2+3)*4-5/2=22 for you).
            </p>
        </div>
