package org.recreativospiscis.nmk.encoder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

import org.recreativospiscis.nmk.encoder.NmkEncoder.MODE;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for NMK Encoder.
 */
public class NmkEncoderTest extends TestCase {

	private final static String BG_ROM_BJTWIN = "93087-4.bin";
	private final static String BG_ROM_NOURYOKU = "ic32.4";
	private final static String SPRITE_ROM_BJTWIN = "93087-5.bin";
	private final static String SPRITE_ROM_NOURYOKU = "ic100.5";

	private final static String BG_ROM_BJTWIN_UNENCRYPTED = "mergedBg_bjtwinp.bin";
	private final static String BG_ROM_NOURYOKU_UNENCRYPTED = "mergedBg_nouryokup.bin";
	// These are created by byteswapping the decrypted contents to simulate the
	// original single file data:
	private final static String SPRITE_ROM_BJTWIN_UNENCRYPTED = "mergedSprites_bjtwinp.bin";
	private final static String SPRITE_ROM_NOURYOKU_UNENCRYPTED = "mergedSprites_nouryokup.bin";

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public NmkEncoderTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(NmkEncoderTest.class);
	}

	public void testEncodeNmk_BG_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_BJTWIN_UNENCRYPTED);
		byte[] encryptedRom = this.getFileContents(BG_ROM_BJTWIN);

		byte[] encodedRom = NmkEncoder.processBackgroundData(MODE.encode, rom);

		assertTrue("File encoded is not equal to original encrypted.", Arrays.equals(encryptedRom, encodedRom));
	}

	public void testEncodeNmk_Sprite_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_BJTWIN_UNENCRYPTED);
		byte[] encryptedRom = this.getFileContents(SPRITE_ROM_BJTWIN);

		byte[] encodedRom = NmkEncoder.processSpriteData(MODE.encode, rom);

		assertTrue("File encoded is not equal to original encrypted.", Arrays.equals(encryptedRom, encodedRom));
	}

	public void testEncodeNmk_BG_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_NOURYOKU_UNENCRYPTED);
		byte[] encryptedRom = this.getFileContents(BG_ROM_NOURYOKU);

		byte[] encodedRom = NmkEncoder.processBackgroundData(MODE.encode, rom);

		assertTrue("File encoded is not equal to original encrypted.", Arrays.equals(encryptedRom, encodedRom));
	}

	public void testEncodeNmk_Sprite_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_NOURYOKU_UNENCRYPTED);
		byte[] encryptedRom = this.getFileContents(SPRITE_ROM_NOURYOKU);

		byte[] encodedRom = NmkEncoder.processSpriteData(MODE.encode, rom);

		assertTrue("File encoded is not equal to original encrypted.", Arrays.equals(encryptedRom, encodedRom));
	}

	public void testDecodeNmk_BG_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_BJTWIN);
		byte[] unencryptedRom = this.getFileContents(BG_ROM_BJTWIN_UNENCRYPTED);

		byte[] decodedRom = NmkEncoder.processBackgroundData(MODE.decode, rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testDecodeNmk_Sprite_bjtwin() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_BJTWIN);
		byte[] unencryptedRom = this.getFileContents(SPRITE_ROM_BJTWIN_UNENCRYPTED);

		byte[] decodedRom = NmkEncoder.processSpriteData(MODE.decode, rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testDecodeNmk_BG_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(BG_ROM_NOURYOKU);
		byte[] unencryptedRom = this.getFileContents(BG_ROM_NOURYOKU_UNENCRYPTED);

		byte[] decodedRom = NmkEncoder.processBackgroundData(MODE.decode, rom);

		assertTrue("File decoded is not equal to original unencrypted.", Arrays.equals(unencryptedRom, decodedRom));
	}

	public void testDecodeNmk_Sprite_nouryoku() throws IOException, URISyntaxException {
		byte[] rom = this.getFileContents(SPRITE_ROM_NOURYOKU);
		byte[] unencryptedRom = this.getFileContents(SPRITE_ROM_NOURYOKU_UNENCRYPTED);

		byte[] decodedRom = NmkEncoder.processSpriteData(MODE.decode, rom);

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
