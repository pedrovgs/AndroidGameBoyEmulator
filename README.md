Android Game Boy Emulator [![Build Status](https://travis-ci.org/pedrovgs/AndroidGameBoyEmulator.svg?branch=master)](https://travis-ci.org/pedrovgs/AndroidGameBoyEmulator) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Game%20Boy%20Emulator-brightgreen.svg?style=flat)](http://android-arsenal.com/details/3/2623)
=========================

Android Game Boy Emulator written in Java. This project is under development.

![Game Boy Gif][5]

Build the project
-----------------

From the IDE:

* Import the project in Android Studio or IntelliJ.
* Run the ``app`` module.

From the command line:

* To build the project including tests execution and the checkstyle: ``./gradlew checkstyle build``.
* To build the project and deploy it in your device or emulator: ``./gradlew installDebug`

Already implemented tasks:
--------------------------

* CPU - Z80 Intel processor version.
* MMU - Memory Management Unit.
* CPU ISA - Instruction set used by the Z80 processor.
* Game Loader. Loads the content of a ROM into the MMU.
* LCD - A 160 x 144 pixels LCD.
* GPU - Part of the LCD implementation.
* BIOS execution. The current project is already able to execute the Game Boy BIOS routine.

Pending tasks:
--------------

* Keypad.
* Interruptions.
* GPU sprites.
* Timer.
* Memory Banking.
* Sound.
* Android UI.
* Android Room selector.

Do you want to contribute?
--------------------------

Please, do it. This project is still under development and I have a few issues waiting for your help :) Send me an email if you need some information or help with any issue.

Libraries used in this project
------------------------------

* [JUnit] [1]
* [Mockito] [2]
* [ButterKnife] [3]
* [Guava] [4]

Developed By
------------

* Pedro Vicente Gómez Sánchez - <pedrovicente.gomez@gmail.com>

<a href="https://twitter.com/pedro_g_s">
  <img alt="Follow me on Twitter" src="http://imageshack.us/a/img812/3923/smallth.png" />
</a>
<a href="https://es.linkedin.com/in/pedrovgs">
  <img alt="Add me to Linkedin" src="http://imageshack.us/a/img41/7877/smallld.png" />
</a>

License
-------

    Copyright 2014 Pedro Vicente Gómez Sánchez

    Licensed under the GNU General Public License, Version 3 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.gnu.org/licenses/gpl-3.0.en.html

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/junit-team/junit
[2]: https://github.com/mockito/mockito
[3]: https://github.com/JakeWharton/butterknife
[4]: https://github.com/google/guava
[5]: http://raw.github.com/pedrovgs/AndroidGameBoyEmulator/master/art/GameBoy.gif