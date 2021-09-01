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
* You need to take care of the word swapping that could be applied to the original encrypted roms (ROM_LOAD16_WORD_SWAP used in MAME for load the ROM). In that case, you need to byte-deinterleave the original rom and byte-interleave again in reverse order, previous to decode it using this tool.
* Tested with Bombjack Twin and Nouryoku Koujou Iinkai games, as both games have dumped romsets of both versions: "encrypted" and "unencrypted".
