# Maréu

P4 OCR - mini Android App for training - goal was to create a meeting management app from scratch.

## Installation

Import the project in **Android Studio** using one of the links below :

```bash
git@github.com:Jerome-eagle-OCR/Mareu-P4-OCR.git
```

or

```bash
https://github.com/Jerome-eagle-OCR/Mareu-P4-OCR.git
```

## Run

In **Android Studio**, select "app" and hit "run" button or Maj+F10 on keyboard.

## Use

The app starts with a list of 8 dummy meetings that can be deleted.<br />
Click on the "+" button to schedule a new meeting.<br />
**Meeting room** can only be set when all date, time and duration are set, as the app checks the availability of the rooms for the given time slot.<br />
**Subject** is limited to 55 characters.<br />
**Email adresses** have to be valid (contain @ and . and not finsishing by one of these two characters).<br />
**Email adress** can be set by clicking OK on virtual keyboard or by typing space or semicolon at the end of it.<br />
**Schedule button** at the bottom is enabled automatically when all fields are filled.<br /><br />
**Note : Data persistency has not been managed, as asked.**

## Tests

Unit tests and UI tests have been done and are passed.<br />
Reports files can be found in repository root.
