package wav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * WavHeader
 * The canonical WAV file header format.
 * @author Jasminder Garcha
 *
 */
public class WavHeader {
	/**
	 * The WAV file header hexadecimal string.
	 */
	public String wavHeaderHexString;
	
	/**
	 * Chunk Id. Contains the letters "RIFF" in ASCII form (0x52494646 big-endian form). Offset 0 bytes. Size 4 bytes.
	 * "RIFF" chunk descriptor with chunk size and format.
	 */
	public static final int CHUNK_ID = 0x52494646;
	
	/**
	 * Chunk Id hexadecimal string.
	 */
	public static final String CHUNK_ID_HEXSTRING = "0x"+Integer.toHexString(CHUNK_ID);			
	
	/**
	 * Chunk size. Offset 4 bytes. Size 4 bytes. Little endian.
	 */
	public int chunkSize;
	
	/**
	 * Chunk size hexadecimal string.
	 */
	public String chunkSizeHexString;
	
	/**
	 * Format. Contains the letters "WAVE" (0x57415645 big-endian form).  Offset 8 bytes. Size 4 bytes.
	 */
	public static final int FORMAT = 0x57415645;
	
	/**
	 * Format hexadecimal string.
	 */
	public static final String FORMAT_HEXSTRING = "0x"+Integer.toHexString(FORMAT);
	
	/**
	 * Subchunk 1 Id. Contains the letters "fmt" (0x666D7420 big-endian form). Offset 12 bytes. Size 4 bytes.
	 * The "fmt" subchunk describes the format of the sound information in the data subchunk.
	 */
	public static final int SUBCHUNK1_ID = 0x666D7420;	

	/**
	 * Subchunk 1 Id hexadecimal string.
	 */
	public static final String SUBCHUNK1_ID_HEXSTRING = "0x"+Integer.toHexString(SUBCHUNK1_ID);
	
	/**
	 * Subchunk 1 size. Offset 16 bytes. Size 4 bytes. Little endian.
	 */
	public int subchunk1Size;
	
	/**
	 * Subchunk 1 size hexadecimal string.
	 */
	public String subchunk1SizeHexString;
	
	/**
	 * Audio format. 1 is PCM. Offset 20 bytes. Size 2 bytes. Little endian.
	 */
	public int audioFormat;
	
	/**
	 * Audio format hexadecimal string. 
	 */
	public String audioFormatHexString;
	
	/**
	 * Number of channels. 1 is mono, 2 is stereo. Offset 22 bytes. Size 2 bytes. Little endian.
	 */
	public int numberOfChannels;
	
	/**
	 * Number of channels hexadecimal string. 
	 */
	public String numberOfChannelsHexString;
	
	/**
	 * Sample rate. Offset 24 bytes. Size 4 bytes. Little endian.
	 */
	public int sampleRate;
	
	/**
	 * Sample rate hexadecimal string.
	 */
	public String sampleRateHexString;
	
	/**
	 * Byte rate. = SampleRate * NumChannels * BitsPerSample/8. Offset 28 bytes. Size 4 bytes. Little endian.
	 */
	public int byteRate;
	
	/**
	 * Byte rate hexadecimal string.
	 */
	public String byteRateHexString;
	
	/**
	 * Block align. = NumChannels * BitsPerSample/8. Offset 32 bytes. Size 2 bytes. Little endian.
	 */
	public int blockAlign;
	
	/**
	 * Block align hexadecimal string.
	 */
	public String blockAlignHexString;
	
	/**
	 * Bits per sample. Offset 34 bytes. Size 2 bytes.
	 */
	public int bitsPerSample; 
	
	/**
	 * Bits per sample hexadecimal string.
	 */
	public String bitsPerSampleHexString;
	
	/**
	 * The "data" subchunk indicates the size of the sound information and contains the raw sound data.
	 * Subchunk 2 Id. Contains the letters "data" (0x64617461 big-endian form). Offset 36 bytes. Size 4 bytes.
	 */
	public static final int SUBCHUNK2_ID = 0x64617461;
	
	/**
	 * Subchunk 2 Id hexadecimal string.
	 */
	public static final String SUBCHUNK2_ID_HEXSTRING = "0x"+Integer.toHexString(SUBCHUNK2_ID);			
	
	/**
	 * Subchunk 2 size. = NumSamples * NumChannels * BitsPerSample/8. Offset 40 bytes. Size 4 bytes.
	 */
	public int subchunk2Size;
	
	/**
	 * Subchunk 2 size hexadecimal string.
	 */
	public String subchunk2SizeHexString;	
	
	/**
	 * Parses the WAV file header.
	 * @param wavHeader WAV file header hexadecimal string.
	 * @throws Exception The header does not match the canonical WAV file format.
	 */
	public WavHeader(String wavHeader) throws Exception {
		wavHeaderHexString = wavHeader.toUpperCase().trim();
		String header;
		if(!wavHeaderHexString.contains(" ")) { //No spaces.
			header = wavHeaderHexString;
		}			
		else {
			header = "";
			String[] bytes = wavHeaderHexString.split(" ");
            for (String aByte : bytes)
				header += aByte;
		}
		if(header.length() < 89)
			throw new Exception("The WAV header size does not match the canonical WAV file format.");
		if(!header.substring(0, 8).equals(CHUNK_ID_HEXSTRING.substring(2).toUpperCase()) || !header.substring(16, 24).equals(FORMAT_HEXSTRING.substring(2).toUpperCase()))
			throw new Exception("The \"RIFF\" chunk descriptor does not match the canonical WAV file format.");
		if(!header.substring(24, 32).equals(SUBCHUNK1_ID_HEXSTRING.substring(2).toUpperCase()))
			throw new Exception("The \"fmt\" subchunk does not match the canonical WAV file format.");
		if(!header.substring(72, 80).equals(SUBCHUNK2_ID_HEXSTRING.substring(2)))
			throw new Exception("The \"data\" subchunk (subchunk2Id) does not match the canonical WAV file format.");
		/*
		 * Parse the WAV header. Convert the hexadecimal strings to little endian and convert to base 10.
		 */
		chunkSizeHexString = header.substring(8, 16);
		chunkSize = base16ToBase10(reverseBytes(chunkSizeHexString));
		subchunk1SizeHexString = header.substring(32, 40);
		subchunk1Size = base16ToBase10(reverseBytes(subchunk1SizeHexString));
		audioFormatHexString = header.substring(40, 44);
		audioFormat = base16ToBase10(reverseBytes(audioFormatHexString));
		numberOfChannelsHexString = header.substring(44, 48);
		numberOfChannels = base16ToBase10(reverseBytes(numberOfChannelsHexString));
		sampleRateHexString = header.substring(48, 56);
		sampleRate = base16ToBase10(reverseBytes(sampleRateHexString));
		byteRateHexString = header.substring(56, 64);
		byteRate = base16ToBase10(reverseBytes(byteRateHexString));
		blockAlignHexString = header.substring(64, 68);
		blockAlign = base16ToBase10(reverseBytes(blockAlignHexString));
		bitsPerSampleHexString = header.substring(68, 72);
		bitsPerSample = base16ToBase10(reverseBytes(bitsPerSampleHexString));
		subchunk2SizeHexString = header.substring(80, 88);
		subchunk2Size = base16ToBase10(reverseBytes(subchunk2SizeHexString));
	}
	
	/**
	 * Parses the WAV file header
	 * @param wavFile WAV file header hexadecimal string.
	 * @throws Exception The header does not match the canonical WAV file format
	 */
	public WavHeader(File wavFile) throws Exception {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(wavFile);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}		
		byte[] headerBytes;
		String header = "";
		try {
			headerBytes = fileInputStream.readNBytes(128);
            for (byte headerByte : headerBytes) {
                header += String.format("%02X", headerByte & 0xFF);
            }
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		wavHeaderHexString = header;
		if(header.length() < 89)
			throw new Exception("The WAV header size does not match the canonical WAV file format.");
		if(!header.substring(0, 8).equals(CHUNK_ID_HEXSTRING.substring(2).toUpperCase()) || !header.substring(16, 24).equals(FORMAT_HEXSTRING.substring(2).toUpperCase()))
			throw new Exception("The \"RIFF\" chunk descriptor does not match the canonical WAV file format.");
		if(!header.substring(24, 32).equals(SUBCHUNK1_ID_HEXSTRING.substring(2).toUpperCase()))
			throw new Exception("The \"fmt\" subchunk does not match the canonical WAV file format.");
		if(!header.substring(72, 80).equals(SUBCHUNK2_ID_HEXSTRING.substring(2)))
			throw new Exception("The \"data\" subchunk (subchunk2Id) does not match the canonical WAV file format.");
		/*
		 * Parse the WAV header. Convert the hexadecimal strings to little endian and convert to base 10.
		 */
		chunkSizeHexString = header.substring(8, 16);
		chunkSize = base16ToBase10(reverseBytes(chunkSizeHexString));
		subchunk1SizeHexString = header.substring(32, 40);
		subchunk1Size = base16ToBase10(reverseBytes(subchunk1SizeHexString));
		audioFormatHexString = header.substring(40, 44);
		audioFormat = base16ToBase10(reverseBytes(audioFormatHexString));
		numberOfChannelsHexString = header.substring(44, 48);
		numberOfChannels = base16ToBase10(reverseBytes(numberOfChannelsHexString));
		sampleRateHexString = header.substring(48, 56);
		sampleRate = base16ToBase10(reverseBytes(sampleRateHexString));
		byteRateHexString = header.substring(56, 64);
		byteRate = base16ToBase10(reverseBytes(byteRateHexString));
		blockAlignHexString = header.substring(64, 68);
		blockAlign = base16ToBase10(reverseBytes(blockAlignHexString));
		bitsPerSampleHexString = header.substring(68, 72);
		bitsPerSample = base16ToBase10(reverseBytes(bitsPerSampleHexString));
		subchunk2SizeHexString = header.substring(80, 88);
		subchunk2Size = base16ToBase10(reverseBytes(subchunk2SizeHexString));		
	}
	
	/**
	 * Get the header of the wav file as a hexadecimal string.
	 * @return Wav file header hexadecimal string.
	 */
	public String header() {
		return wavHeaderHexString;
	}
	
	/**
	 * Get the chunk size of the WAV file.
	 * @return Chunk size.
	 */
	public int chunkSize() {
		return chunkSize;
	}

	/**
	 * Get the chunk size hexadecimal string of the WAV file.
	 * @return Chunk size.
	 */
	public String chunkSizeHexString() {
		return chunkSizeHexString;
	}
	
	/**
	 * Get the subchunk 1 size of the WAV file.
	 * @return Subchunk 1 size.
	 */
	public int subchunk1Size() {
		return subchunk1Size;
	}

	/**
	 * Get the subchunk 1 size hexadecimal string of the WAV file.
	 * @return Subchunk 1 size.
	 */
	public String subchunk1SizeHexString() {
		return subchunk1SizeHexString;
	}
	
	/**
	 * Get the audio format of the WAV file.
	 * @return Audio format.
	 */
	public int audioFormat() {
		return audioFormat;
	}

	/**
	 * Get the audio format hexadecimal string of the WAV file.
	 * @return Audio format.
	 */
	public String audioFormatHexString() {
		return audioFormatHexString;
	}
	
	/**
	 * Get the number of channels of the WAV file.
	 * @return Number of channels.
	 */
	public int numberOfChannels() {
		return numberOfChannels;
	}

	/**
	 * Get the sample rate of the WAV file.
	 * @return Sample rate.
	 */
	public int sampleRate() {
		return sampleRate;
	}

	/**
	 * Get the sample rate hexadecimal string of the WAV file.
	 * @return Sample rate.
	 */
	public String sampleRateHexString() {
		return sampleRateHexString;
	}
	
	/**
	 * Get the byte rate of the WAV file.
	 * @return Byte rate.
	 */
	public int byteRate() {
		return byteRate;
	}

	/**
	 * Get the byte rate hexadecimal string of the WAV file.
	 * @return Byte rate.
	 */
	public String byteRateHexString() {
		return byteRateHexString;
	}
	
	/**
	 * Get the block align of the WAV file.
	 * @return Block align.
	 */
	public int blockAlign() {
		return blockAlign;
	}

	/**
	 * Get the block align hexadecimal string of the WAV file.
	 * @return Block align.
	 */
	public String blockAlignHexString() {
		return blockAlignHexString;
	}

	/**
	 * Get the bits per sample of the WAV file.
	 * @return Bits per sample.
	 */
	public int bitsPerSample() {
		return bitsPerSample;
	}

	/**
	 * Get the bits per sample hexadecimal string of the WAV file.
	 * @return Bits per sample.
	 */
	public String bitsPerSampleHexString() {
		return bitsPerSampleHexString;
	}

	/**
	 * Get the subchunk 2 size of the WAV file.
	 * @return Subchunk 2 size.
	 */
	public int subchunk2Size() {
		return subchunk2Size;
	}
	
	/**
	 * Get the subchunk 2 size hexadecimal string of the WAV file.
	 * @return Subchunk 2 size.
	 */
	public String subchunk2SizeHexString() {
		return subchunk2SizeHexString;
	}

	/**
	 * Get the number of channels hexadecimal string of the WAV file.
	 * @return Number of channels.
	 */
	public String numberOfChannelsHexString() {
		return numberOfChannelsHexString;
	}

	/**
	 * String representation of WAV header fields.
	 */
	public String toString() {
		return  	
			"Chunk Descriptor Id: \"RIFF\".\n" +	
			"Format: \"WAVE\".\n" + 
			"Chunk size is little endian 0x"+chunkSizeHexString()+"\n" +
			"Chunk Size: "+chunkSize()+"\n" +
			"\"fmt\" Subchunk:\n" +
			"Subchunk 1 Size is little endian 0x"+subchunk1SizeHexString()+"\n" +
			"Subchunk 1 Size: "+subchunk1Size()+"\n" +
			"Audio Format is little endian 0x"+audioFormatHexString()+"\n" +
			"Audio Format: "+audioFormat()+"\n" +
			"Number Of Channels is little endian 0x"+numberOfChannelsHexString()+"\n" +
			"Number Of Channels: "+numberOfChannels()+"\n" +
			"Sample Rate is little endian 0x"+sampleRateHexString()+"\n" +
			"Sample Rate: "+sampleRate()+"\n"+
			"Byte Rate is little endian 0x"+byteRateHexString()+"\n" +
			"Byte Rate: "+byteRate()+"\n" +
			"Block Align is little endian 0x"+blockAlignHexString()+"\n" +
			"Block Align: "+blockAlign()+"\n" +
			"Bits Per Sample is little endian 0x"+bitsPerSampleHexString()+"\n" +
			"Bits Per Sample: "+bitsPerSample()+"\n" +
			"Subchunk 2 Size is little endian 0x"+subchunk2SizeHexString()+"\n" +
			"Subchunk 2 Size: "+subchunk2Size();
	}
	
	/**
	 * Converts hexadecimal (base 16) to decimal (base 10).
	 * @param hexString Hexadecimal string.
	 * @return The decimal value of the hexadecimal string.
	 */
	public int base16ToBase10(String hexString) {
		hexString.toUpperCase();
		int decimalValue = 0;
		for (int i = 0; i < hexString.length(); i++)	
			decimalValue += Integer.parseInt(String.valueOf(hexString.charAt(hexString.length()-i-1)), 16)*Math.pow(16, i);
		return decimalValue;
	}
	
	/**
	 * Reverses a string in chunks of two characters, bytes in a hexadecimal string. Converts a hexadecimal string from big endian to little endian. 
	 * @param hexString Hexadecimal string.
	 * @return String reversed in chunks of two characters, bytes in a hexadecimal string. Hexadecimal string in little endian. 
	 */
	public String reverseBytes(String hexString) { 
		String reversedInChunksOfTwo = "";
		for (int i = 0; i < hexString.length(); i+=2) 
			reversedInChunksOfTwo += hexString.substring(hexString.length()-i-2, hexString.length()-i);		
		return reversedInChunksOfTwo;		
	}
}