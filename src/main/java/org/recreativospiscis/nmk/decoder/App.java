package org.recreativospiscis.nmk.decoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * NMK ROM DECODER
 *
 */
public class App {

	// @formatter:off
	private final static byte[][] DECODE_DATA_BG = {
			{ 0x3, 0x0, 0x7, 0x2, 0x5, 0x1, 0x4, 0x6 },
			{ 0x1, 0x2, 0x6, 0x5, 0x4, 0x0, 0x3, 0x7 },
			{ 0x7, 0x6, 0x5, 0x4, 0x3, 0x2, 0x1, 0x0 },
			{ 0x7, 0x6, 0x5, 0x0, 0x1, 0x4, 0x3, 0x2 },
			{ 0x2, 0x0, 0x1, 0x4, 0x3, 0x5, 0x7, 0x6 },
			{ 0x5, 0x3, 0x7, 0x0, 0x4, 0x6, 0x2, 0x1 },
			{ 0x2, 0x7, 0x0, 0x6, 0x5, 0x3, 0x1, 0x4 },
			{ 0x3, 0x4, 0x7, 0x6, 0x2, 0x0, 0x5, 0x1 }
	};

	private final static byte[][] DECODE_DATA_SPRITE = {
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
		if ((args.length == 2 || args.length == 3)
				&& ("sprite".equals(args[0]) || "background".equals(args[0]) || "interleave".equals(args[0]))) {
			String outputFilename = args.length == 3 ? args[2] : "decoded_" + args[1];
			byte[] encodedRom = readFile(args[1]);
			byte[] decodedRom = null;
			switch (args[0]) {
			case "sprite":
				decodedRom = decodeSpriteData(encodedRom);
				break;
			case "background":
				decodedRom = decodeBackgroundData(encodedRom);
				break;
			default:
				break;
			}
			writeFile(outputFilename, decodedRom);
			System.out.println("Successfully decoded rom into file: " + outputFilename);
		} else {
			System.err.println(
					"Invalid number of arguments. Usage: nmk-decoder.jar sprite|background input_filename [output_filename]");
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

	private static byte[] decodeBackgroundData(byte[] rom) {
		byte[] decodedRom = new byte[rom.length];
		for (int addr = 0; addr < rom.length; addr++) {
			decodedRom[addr] = nmkDecodeByte(rom[addr], DECODE_DATA_BG[nmkAddressMap_bg0(addr)]);
		}
		return decodedRom;
	}

	private static byte[] decodeSpriteData(byte[] rom) {
		byte[] decodedRom = new byte[rom.length];
		for (int addr = 0; addr < rom.length; addr += 2) {
			int tmp = nmkDecodeWord((Byte.toUnsignedInt(rom[addr + 1]) * 256) + (Byte.toUnsignedInt(rom[addr])),
					DECODE_DATA_SPRITE[nmkAddressMap_sprites(addr)]);
			decodedRom[addr + 1] = (byte) (tmp >> 8);
			decodedRom[addr] = (byte) (tmp & 0xff);
		}
		return decodedRom;
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

	private static int nmkDecodeWord(int src, byte[] bitp) {
		int ret = 0;
		for (int i = 0; i < 16; i++) {
			ret |= (((src >> Byte.toUnsignedInt(bitp[i])) & 1) << (15 - i));
		}
		return ret;
	}
}