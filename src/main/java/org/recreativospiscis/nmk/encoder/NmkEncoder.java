package org.recreativospiscis.nmk.encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * NMK ROM ENCODER
 */
public class NmkEncoder {

	enum MODE {
		encode, decode;
	}

	enum LAYER {
		sprite, background;
	}

	// @formatter:off
	private final static byte[][] DATA_SHIFT_BG = {
			{ 0x3, 0x0, 0x7, 0x2, 0x5, 0x1, 0x4, 0x6 },
			{ 0x1, 0x2, 0x6, 0x5, 0x4, 0x0, 0x3, 0x7 },
			{ 0x7, 0x6, 0x5, 0x4, 0x3, 0x2, 0x1, 0x0 },
			{ 0x7, 0x6, 0x5, 0x0, 0x1, 0x4, 0x3, 0x2 },
			{ 0x2, 0x0, 0x1, 0x4, 0x3, 0x5, 0x7, 0x6 },
			{ 0x5, 0x3, 0x7, 0x0, 0x4, 0x6, 0x2, 0x1 },
			{ 0x2, 0x7, 0x0, 0x6, 0x5, 0x3, 0x1, 0x4 },
			{ 0x3, 0x4, 0x7, 0x6, 0x2, 0x0, 0x5, 0x1 }
	};

	private final static byte[][] DATA_SHIFT_SPRITE = {
			{ 0x9, 0x3, 0x4, 0x5, 0x7, 0x1, 0xb, 0x8, 0x0, 0xd, 0x2, 0xc, 0xe, 0x6, 0xf, 0xa },
			{ 0x1, 0x3, 0xc, 0x4, 0x0, 0xf, 0xb, 0xa, 0x8, 0x5, 0xe, 0x6, 0xd, 0x2, 0x7, 0x9 },
			{ 0xf, 0xe, 0xd, 0xc, 0xb, 0xa, 0x9, 0x8, 0x7, 0x6, 0x5, 0x4, 0x3, 0x2, 0x1, 0x0 },
			{ 0xf, 0xe, 0xc, 0x6, 0xa, 0xb, 0x7, 0x8, 0x9, 0x2, 0x3, 0x4, 0x5, 0xd, 0x1, 0x0 },

			{ 0x1, 0x6, 0x2, 0x5, 0xf, 0x7, 0xb, 0x9, 0xa, 0x3, 0xd, 0xe, 0xc, 0x4, 0x0, 0x8 },
			{ 0x7, 0x5, 0xd, 0xe, 0xb, 0xa, 0x0, 0x1, 0x9, 0x6, 0xc, 0x2, 0x3, 0x4, 0x8, 0xf },
			{ 0x0, 0x5, 0x6, 0x3, 0x9, 0xb, 0xa, 0x7, 0x1, 0xd, 0x2, 0xe, 0x4, 0xc, 0x8, 0xf },
			{ 0x9, 0xc, 0x4, 0x2, 0xf, 0x0, 0xb, 0x8, 0xa, 0xd, 0x3, 0x6, 0x5, 0xe, 0x1, 0x7 }
	};
	// @formatter:on

	public static void main(String[] args) throws IOException {
		MODE mode = null;
		LAYER layer = null;
		try {
			mode = args != null && args.length > 0 ? MODE.valueOf(args[0]) : null;
			layer = args != null && args.length > 1 ? LAYER.valueOf(args[1]) : null;
		} catch (IllegalArgumentException e) {
			// do nothing
		}
		if ((args.length == 3 || args.length == 4) && mode != null && layer != null) {
			String outputFilename = args.length == 4 ? args[3] : mode.toString() + "_" + args[2];
			byte[] inputRom = readFile(args[2]);
			byte[] outputRom = null;
			switch (layer) {
			case sprite:
				outputRom = processSpriteData(mode, inputRom);
				break;
			case background:
				outputRom = processBackgroundData(mode, inputRom);
				break;
			default:
				break;
			}
			writeFile(outputFilename, outputRom);
			System.out.println("Successfully " + mode.toString() + " rom into file: " + outputFilename);
		} else {
			System.err.println(
					"Invalid number of arguments. Usage: nmk-encoder.jar encode|decode sprite|background input_filename [output_filename]");
			return;
		}
	}

	private static byte[] readFile(String fileName) throws IOException {
		if (!Files.exists(Paths.get(fileName))) {
			throw new IOException("Input file not exist: " + fileName);
		}
		return Files.readAllBytes(Paths.get(fileName));
	}

	private static void writeFile(String fileName, byte[] content) throws IOException {
		if (Files.exists(Paths.get(fileName))) {
			throw new IOException("Output file still exist, do not overwrite it: " + fileName);
		}
		Files.write(Paths.get(fileName), content);
	}

	public static byte[] processBackgroundData(MODE mode, byte[] rom) {
		byte[] outputRom = new byte[rom.length];
		for (int addr = 0; addr < rom.length; addr++) {
			switch (mode) {
			case encode:
				outputRom[addr] = nmkEncodeByte(rom[addr], DATA_SHIFT_BG[nmkAddressMap_bg0(addr)]);
				break;
			case decode:
				outputRom[addr] = nmkDecodeByte(rom[addr], DATA_SHIFT_BG[nmkAddressMap_bg0(addr)]);
				break;
			default:
				break;
			}
		}
		return outputRom;
	}

	public static byte[] processSpriteData(MODE mode, byte[] rom) {
		byte[] outputRom = new byte[rom.length];
		for (int addr = 0; addr < rom.length; addr += 2) {
			int tmp = 0;
			// here the original encrypted data is always byteswapped after encoding, so we need to
			// deal with it differently when encoding or decoding:
			switch (mode) {
			case encode:
				tmp = nmkEncodeWord((Byte.toUnsignedInt(rom[addr + 1]) * 256) + (Byte.toUnsignedInt(rom[addr])),
						DATA_SHIFT_SPRITE[nmkAddressMap_sprites(addr)]);
				outputRom[addr] = (byte) (tmp >> 8);
				outputRom[addr + 1] = (byte) (tmp & 0xff);
				break;
			case decode:
				tmp = nmkDecodeWord((Byte.toUnsignedInt(rom[addr]) * 256) + (Byte.toUnsignedInt(rom[addr + 1])),
						DATA_SHIFT_SPRITE[nmkAddressMap_sprites(addr)]);
				outputRom[addr + 1] = (byte) (tmp >> 8);
				outputRom[addr] = (byte) (tmp & 0xff);
				break;
			default:
				break;
			}
		}
		return outputRom;
	}

	private static int nmkAddressMap_bg0(int addr) {
		return ((addr & 0x00004) >> 2) | ((addr & 0x00800) >> 10) | ((addr & 0x40000) >> 16);
	}

	private static int nmkAddressMap_sprites(int addr) {
		return ((addr & 0x00010) >> 4) | ((addr & 0x20000) >> 16) | ((addr & 0x100000) >> 18);
	}

	private static byte nmkDecodeByte(byte src, byte[] bitp) {
		byte ret = 0;
		for (int i = 0; i < 8; i++) {
			ret |= (((src >> bitp[i]) & 1) << (7 - i));
		}
		return ret;
	}

	private static byte nmkEncodeByte(byte src, byte[] bitp) {
		byte ret = 0;
		for (int i = 0; i < 8; i++) {
			ret |= (((src >> (7 - i)) & 1) << bitp[i]);
		}
		return ret;
	}

	private static int nmkDecodeWord(int src, byte[] bitp) {
		int ret = 0;
		for (int i = 0; i < 16; i++) {
			ret |= (((src >> Byte.toUnsignedInt(bitp[i])) & 1) << (15 - i));
		}
		return ret;
	}

	private static int nmkEncodeWord(int src, byte[] bitp) {
		int ret = 0;
		for (int i = 0; i < 16; i++) {
			ret |= (((src >> (15 - i)) & 1) << Byte.toUnsignedInt(bitp[i]));
		}
		return ret;
	}
}