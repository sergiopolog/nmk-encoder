# nmk-encoder
NMK Encoder

Utility for encoding/decoding the GFX ROMs (backgrounds and sprites) of NMK games using the NMK-214 + NMK-215 based GFX encryption. Based on the related code of MAME driver [nmk16.cpp](https://github.com/mamedev/mame/blob/2ec9b81c46d256d2c157620c61837828bc5cda8a/src/mame/drivers/nmk16.cpp#L5233)

Games using this are:
* Macross
* Gunnail
* Bombjack Twin
* Saboten Bombers
* Nouryoku Koujou Iinkai

Usage:
```
java -jar nmk-encoder.jar encode|decode sprite|background encrypted_rom_file [output_file]
```

It creates a new file with the encoded/decoded content.

Requires Java 1.8 or newer.

NOTES:
* Sprite data rom is always byteswapped when loading in MAME. However, this tool takes care of it and outputs directly the decrypted data prepared to be fed into the sprite engine in the pcb, so there is no need to previous tweaking (deinterleave and interleave swapped) on the encrypted rom. Use directly the official dumped roms from MAME.
* Tested with Bombjack Twin and Nouryoku Koujou Iinkai games, as both games have dumped romsets of both versions: "encrypted" and "unencrypted".
