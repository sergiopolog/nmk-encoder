# nmk-decoder
NMK Decoder

Utility for decoding the GFX ROMs (backgrounds and sprites) of NMK games using the NMK-214 + NMK-215 based GFX encryption. Based on the related code of MAME driver nmk16.cpp

Games using this are:
* Macross
* Gunnail
* Bombjack Twin
* Saboten Bombers
* Nouryoku Koujou Iinkai

Usage:
```
java -jar nmk-decoder.jar sprite|background encrypted_rom_file [output_file]
```

It creates a new file with the decoded content.

Requires Java 1.8 or newer.


TO-DO: it works fine with background (tested decoding background rom of bjtwin set and it produces a identical result with background roms of unencrypted background roms of bjtwinp), but it doesn't work well with sprites, more investigation to be done...
