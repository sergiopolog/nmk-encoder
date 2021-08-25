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

NOTES:
You need to take care of the word swapping that could be applied to the original encrypted roms (ROM_LOAD16_WORD_SWAP used in MAME for load the ROM). In that case, you need to byte-deinterleave the original rom and byte-interleave again in reverse order, previous to decode it using this tool.


TO-DO: tested and working with bjtwin (compared results with unencrypted set bjtiwnp). Pending to test it in Nouryoku Koujou Iinkai
