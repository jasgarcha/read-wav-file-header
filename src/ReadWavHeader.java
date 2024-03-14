import java.io.File;
import java.util.Scanner;

import wav.WavHeader;

/**
 * ReadWavHeader
 * Read the header of a WAV file. 
 * @author Jasminder Garcha
 */
public class ReadWavHeader {
	/**
	 * Read the header of a WAV file. Parses and extracts the chunk size, subchunk 1 size, audio format, number of channels, sample rate, byte rate, block align, bits per sample, and subchunk 2 size.
	 * @param args The absolute path to a WAV file. A path with white spaces must be enclosed in double quotes. If no argument is provided, the program runs as an input of WAV header as a hexadecimal string (copied and pasted from a hex editor).
	 */
	public static void main(String[] args) {
		if(args.length > 0) {
			for(int i = 0; i < args.length; i++) {
				File wavFile = new File(args[0]);
				WavHeader wavFileHeader;
				try {
					wavFileHeader = new WavHeader(wavFile);
					System.out.println(wavFileHeader);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		else {
			Scanner input = new Scanner(System.in);
			System.out.println("Enter the WAV file header hexadecimal string: ");
			String wavHeaderHexString = input.nextLine();
			WavHeader wavHeader;
			try {
				wavHeader = new WavHeader(wavHeaderHexString);
				System.out.println(wavHeader);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				input.close();
			}
			input.close();
		}
	}
}