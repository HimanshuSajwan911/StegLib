# StegLib
StegLib is a Java-based steganography library designed for robust data hiding. It allows you to hide various types of data, including text, images, audio, and video, in single or fragmented form across different cover media such as images, audio, video, and documents. Importantly, it achieves this without increasing the cover size or causing corruption. StegLib employs multiple obfuscation techniques to resist common steganalysis tools like Hex Editors.

## Key Features
1. **Supports Multiple File Formats:** StegLib is versatile and supports hiding data in multiple file formats, including:  
   *Audio: Wav  
   *Documents: Text  
3. **Handles Large Cover File Sizes:** StegLib is capable of working with large cover files.

### Getting Started

To get started with StegLib, follow these simple steps:

1. Clone the repository.
2. Include the StegLib library in your Java project.
3. Explore the examples and documentation to integrate StegLib into your application.

### Usage Examples
Note: To encode a specific file type, create its corresponding class object.  
For example, use WavSteganography() for WAV file types.

1. For encoding a data file in single cover file:
   ```
        String coverFilePath = "your cover file path";
        String dataFilePath = "your data file path";
        String destinationPath = "your output file path";
        
        StegOptions stegOptions = new StegOptions();
        stegOptions.setByteSkipPerBlock(2);
        stegOptions.setDataBlockSize(3);
        stegOptions.setInitialOffset(7);
        stegOptions.setHiddenBitPosition(0);
        stegOptions.setStartingEndian(BitUtils.Endian.BIG_ENDIAN);
        stegOptions.setPassword("ABC");
        stegOptions.setEndianChangeFrequency(1);
        
        Steganography steganography = new Steganography();
        
        try {
            steganography.encode(coverFilePath, dataFilePath, destinationPath, stegOptions);
        } catch (IOException ex) {
            Logger.getLogger(EncodingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   ```
   for decoding encoded file from a cover file.
   
   ```
        StegOptions stegOptions = new StegOptions();
        stegOptions.setByteSkipPerBlock(2);
        stegOptions.setDataBlockSize(3);
        stegOptions.setInitialOffset(7);
        stegOptions.setHiddenBitPosition(0);
        stegOptions.setStartingEndian(BitUtils.Endian.BIG_ENDIAN);
        stegOptions.setPassword("ABC");
        stegOptions.setEndianChangeFrequency(1);
        
        String encodedCoverFilePath = "encoded cover file path";
        String destinationFilePath = "destination path";
        
        try {
            int result = steganography.decode(encodedCoverFilePath, destinationFilePath, stegOptions);

            System.out.println("Decode result:");
            if (result == Steganography.INVALID_PASSWORD) {
                System.out.println("INVALID_PASSWORD");
            } else if (result == Steganography.DECODING_SUCCESSFUL) {
                System.out.println("DECODING_SUCCESSFUL");
            }
        } catch (IOException ex) {
            Logger.getLogger(EncodingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

   ```

2. For encoding a data file into multiple cover files by fragmenting data file into different chunck size:  
   *Note: The sum of values set in all MultiEncodeSteg instances using setDataAmountToEncode() should not exceed the size of the data file.*
   ```
        String dataFilePath = "your data file path";
        
        StegOptions stegOptions1 = new StegOptions();
        stegOptions1.setByteSkipPerBlock(4);
        stegOptions1.setDataBlockSize(4);
        stegOptions1.setInitialOffset(7);
        stegOptions1.setHiddenBitPosition(0);
        stegOptions1.setStartingEndian(BitUtils.Endian.BIG_ENDIAN);
        stegOptions1.setPassword("ABC");
        stegOptions1.setEndianChangeFrequency(1);
        
        
        MultiEncodeSteg multiEncodeSteg1 = new MultiEncodeSteg();
        multiEncodeSteg1.setCoverFilePath("cover file 1 path");
        multiEncodeSteg1.setDestinationPath("output file 1 path");
        multiEncodeSteg1.setDataAmountToEncode(5); //in bytes
        multiEncodeSteg1.setOptions(stegOptions1);
        
        StegOptions stegOptions2 = new StegOptions();
        stegOptions2.setByteSkipPerBlock(4);
        stegOptions2.setDataBlockSize(4);
        stegOptions2.setInitialOffset(7);
        stegOptions2.setHiddenBitPosition(0);
        stegOptions2.setStartingEndian(BitUtils.Endian.BIG_ENDIAN);
        stegOptions2.setPassword("ABC");
        stegOptions2.setEndianChangeFrequency(1);
        
        MultiEncodeSteg multiEncodeSteg2 = new MultiEncodeSteg();
        multiEncodeSteg2.setCoverFilePath("cover file 2 path");
        multiEncodeSteg2.setDestinationPath("output file 2 path");
        multiEncodeSteg2.setDataAmountToEncode(12); //in bytes
        multiEncodeSteg2.setOptions(stegOptions2);
        
        MultiEncodeSteg multiEncodeSteg3 = new MultiEncodeSteg();
        multiEncodeSteg3.setCoverFilePath("cover file 3 path");
        multiEncodeSteg3.setDestinationPath("output file 3 path");
        multiEncodeSteg3.setDataAmountToEncode(10); //in bytes
        multiEncodeSteg3.setOptions(stegOptions2);
        
        
        ArrayList<MultiEncodeSteg> multiEncodeStegsList = new ArrayList<>();
        multiEncodeStegsList.add(multiEncodeSteg1);
        multiEncodeStegsList.add(multiEncodeSteg2);
        multiEncodeStegsList.add(multiEncodeSteg3);
        
        Steganography steganography = new Steganography();
        try {
            steganography.encode(multiEncodeStegsList, dataFilePath);
        } catch (IOException ex) {
            Logger.getLogger(EncodingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

   ```

   For decoding a encoded data file from multiple cover files:

   ```
        MultiDecodeSteg multiDecodeSteg1 = new MultiDecodeSteg();
        multiDecodeSteg1.setEncodedFilePath("encoded cover file 1 path");
        multiDecodeSteg1.setOptions(stegOptions2);
        
        MultiDecodeSteg multiDecodeSteg2 = new MultiDecodeSteg();
        multiDecodeSteg2.setEncodedFilePath("encoded cover file 2 path");
        multiDecodeSteg2.setOptions(stegOptions2);
        
        MultiDecodeSteg multiDecodeSteg3 = new MultiDecodeSteg();
        multiDecodeSteg3.setEncodedFilePath("encoded cover file 3 path");
        multiDecodeSteg3.setOptions(stegOptions2);
        
        ArrayList<MultiDecodeSteg> multiDecodeStegsList = new ArrayList<>();
        multiDecodeStegsList.add(multiDecodeSteg1);
        multiDecodeStegsList.add(multiDecodeSteg2);
        multiDecodeStegsList.add(multiDecodeSteg3);
        
        String decodedMultiFile = "destination path";
        
        Steganography steganography = new Steganography();
        
        try {
            int result = steganography.decode(multiDecodeStegsList, decodedMultiFile);

            System.out.println("Decode result:");
            if (result == Steganography.INVALID_PASSWORD) {
                System.out.println("INVALID_PASSWORD");
            } else if (result == Steganography.DECODING_SUCCESSFUL) {
                System.out.println("DECODING_SUCCESSFUL");
            }
        } catch (IOException ex) {
            Logger.getLogger(EncodingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   ```
   
  
