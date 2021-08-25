# nmk-decoder
NMK Decoder

Utility for decoding the GFX ROMs (backgrounds and sprites) of NMK games using the NMK-214 + NMK-215 based GFX encryption.

Games using this are:
* Macross
* Gunnail
* Bombjack Twin
* Saboten Bombers
* Nouryoku Koujou Iinkai

Usage:
```
java -jar nmk-decoder sprite|background encrypted_rom_file [output_file]
```

It creates a new file with the decoded content.
