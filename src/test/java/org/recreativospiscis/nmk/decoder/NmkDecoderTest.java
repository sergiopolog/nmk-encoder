package org.recreativospiscis.nmk.decoder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for NMK Decoder.
 */
public class NmkDecoderTest extends TestCase {

	private final static String BG_ROM_BJTWIN = "93087-4.bin";
	private final static String BG_ROM_NOURYOKU = "ic32.4";
	private final static String SPRITE_ROM_BJTWIN_WORD_SWAPPED = "93087-5_word_swap.bin";
	private final static String SPRITE_ROM_NOURYOKU_WORD_SWAPPED = "ic100_word_swap.5";

	private final static String BG_ROM_BJTWIN_UNENCRYPTED = "mergedBg_bjtwinp.bin";
	private final static String BG_ROM_NOURYOKU_UNENCRYPTED = "mergedBg_nouryokup.bin";
	private final static String SPRITE_ROM_BJTWIN_UNENCRYPTED = "interleavedSprites_bjtwinp.bin";
	private final static String SPRITE_ROM_NOURYOKU_UNENCRYPTED = "interleavedSprites_nouryokup.bin";

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public NmkDecoderTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(NmkDecoderTest.class);
	}

	public void testNmkDecoder_BG_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_BJTWIN);
		byte[] unencryptedRom = this.getFileContents(BG_ROM_BJTWIN_UNENCRYPTED);

		byte[] decodedRom = NmkDecoder.decodeBackgroundData(rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testNmkDecoder_Sprite_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_BJTWIN_WORD_SWAPPED);
		byte[] unencryptedRom = this.getFileContents(SPRITE_ROM_BJTWIN_UNENCRYPTED);

		byte[] decodedRom = NmkDecoder.decodeSpriteData(rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testNmkDecoder_BG_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_NOURYOKU);
		byte[] unencryptedRom = this.getFileContents(BG_ROM_NOURYOKU_UNENCRYPTED);

		byte[] decodedRom = NmkDecoder.decodeBackgroundData(rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testNmkDecoder_Sprite_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_NOURYOKU_WORD_SWAPPED);
		byte[] unencryptedRom = this.getFileContents(SPRITE_ROM_NOURYOKU_UNENCRYPTED);

		byte[] decodedRom = NmkDecoder.decodeSpriteData(rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	private byte[] getFileContents(String fileName) throws URISyntaxException, IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return Files.readAllBytes(new File(resource.toURI()).toPath());
		}
	}
}
