# read-wav-file-header
## [Javadocs](https://jasgarcha.github.io/read-wav-file-header/)

## Usage
`ReadWavHeader` is the main program that accepts the absolute path to a `.wav` file as an argument (absolute path with white spaces must be enclosed in double quotes, as per command line convention). If no argument is provided, the program runs as an input of the header as a hexadecimal string, so that a user can copy and paste bytes from a hex editor. The input is passed to the `WavHeader` class, which parses the header information that the program prints.

## Logic
The `wav.WavHeader` class parses the header of a `.wav` file and outputs the chunk size, subchunk 1 size, audio format, number of channels, sample rate, byte rate, block align, bits per sample, and subchunk 2 size. If the constant chunk Id (“RIFF”, “WAVE”), subchunk 1 Id (“fmt”), or subchunk 2 Id (“data”) do not match, the program throws an exception (the substring, corresponding to the header field, is extracted at breakpoints based on the field size).

The hexadecimal string of the `wav` header is converted to decimal using the method `base16tobase10`. Following the binomial expansion theorem, each character’s base 10 value in this string is multiplied by 16 to the power of its position, and the values are summed.

For example, `A1B2… = 2 * 16^0 + 11 * 16^1 + 1 * 16^2 + 10 * 16^3…`

However, because the byte order is little endian, I first pass the input to reverseBytes, which reverses a string in chunks of two characters.

For example, `ABCD -> (AB)(CD)-> (CD)(AB) -> CDAB`.

The output is analogous to converting the bytes from big endian to little endian. In Java, the primitive `byte` data type is signed, ranging from -128 to 127, whereas I assume a byte is an unsigned 8-bit integer, ranging from 0 to 255. A bit mask was applied to offset signed bytes into the unsigned range to produce the desired decimal value.