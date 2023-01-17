import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class RSA extends Application  {
	public static int RSAKeySize = 512;
	
	//Restrict encryption key to small values so the encrpytion can be done faster.
	static boolean fastEncryptionOn = false;
	
	/*Since the exponent of a prime might not be a prime number
	 * we need to check for primality of the randomly generated encryption public constant.

	 */
	
	
	static BigInteger p;
	static BigInteger q;

	static BigInteger n;
	
	static BigInteger phiOfN;

	static BigInteger e;
	static BigInteger d;
		

	
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
        Text text = new Text("Hello World!");
        //text.setFont(new Font(40));
        
        
        
        StackPane pane = new StackPane();
        
        VBox verticalBox = new VBox();

        //Generate keys box part
        //Displays encrpytion and decrpytion key
        HBox generateKeysBox = new HBox();

        Button generateKeys = new Button("Generate keys");
        generateKeysBox.getChildren().add(generateKeys);
        
        
        
//        TextField myTextField = new TextField() {
//            @Override
//            public void paste() { }
//        };        
//        pane.getChildren().add(myTextField);


        
        
        Label publicKeyLabel = new Label("Public key: ");
        TextField publicKeyInput = new TextField ();
        generateKeysBox.getChildren().addAll(publicKeyLabel, publicKeyInput);

        Label privateKeyLabel = new Label("Private key: ");
        TextField privateKeyInput = new TextField ();

        generateKeysBox.getChildren().addAll(privateKeyLabel, privateKeyInput);

        verticalBox.getChildren().add(generateKeysBox);

//        publicKeyInput.textProperty().addListener((observable, oldValue, newValue) -> {
//        	e = new BigInteger(newValue);
//        });
//        privateKeyInput.textProperty().addListener((observable, oldValue, newValue) -> {
//        	d = new BigInteger(newValue);
//        });
        
        generateKeys.setOnAction(e -> {

        	if(privateKeyInput.getText().equals("") && publicKeyInput.getText().equals("")) {
        		
        		//Both fields are empty
        		
        		GenerateEncryptionAndDecrpytionKey();
        		//System.out.println(new String(bi.toByteArray())); // prints "Hello there!"
            	privateKeyInput.setText(String.valueOf(RSA.e));
            	publicKeyInput.setText(String.valueOf(d));

        		
        	} else if (publicKeyInput.getText().equals("")) {
        		
        		//Calculate private key

        		
        	} else if (privateKeyInput.getText().equals("")){
        		//Calculate public key

        	}

      });
        
        
        HBox firstRow = new HBox();
        
        Label label1 = new Label("Plaintext: ");
        TextField encrpytionInput = new TextField ();
        
        
        firstRow.getChildren().addAll(label1, encrpytionInput);
        firstRow.setSpacing(20);
        encrpytionInput.setPrefWidth(450);
        //encrpytionInput.setPrefHeight(400);
        encrpytionInput.setAlignment(Pos.TOP_LEFT);
        	
        TextField encryptionOutput = new TextField ();
        
        //VBox firstRow2 = new VBox();

        Label label2 = new Label("Encrypted Text: ");
        firstRow.getChildren().addAll(label2, encryptionOutput);
        
        encryptionOutput.setPrefWidth(450);
        //encryptionOutput.setPrefHeight(400);
        encryptionOutput.setAlignment(Pos.TOP_LEFT);
        
//        pane.setPrefWidth(1000);
        
        Button encryptButton = new Button("Encrypt");
        firstRow.getChildren().add(encryptButton);

        encryptButton.setOnAction(e -> {
        	
    		char charArr[] = encrpytionInput.getText().toCharArray();


    		String outputText = "";
    		for(char c : charArr) {
    			String convertedCharacter = "";
    			    			
    			BigInteger a = new BigInteger(String.valueOf((int)c));

    			BigInteger enc = EncryptMessage(a, RSA.e, n);
    			convertedCharacter += enc;

    			while(convertedCharacter.length() < 309) {
    				convertedCharacter = "0" + convertedCharacter;
    			}
    			outputText += convertedCharacter;
    			
    		}
    		encryptionOutput.setText(outputText);
      	
      });
        
        verticalBox.getChildren().add(firstRow);
        //pane.getChildren().add(firstRow2);
                
        
        HBox secondRow = new HBox();
        secondRow.setSpacing(10);

        TextField decryptionInput = new TextField ();
        
        Label labelDecrypt = new Label("Ciphertext: ");
        secondRow.getChildren().addAll(labelDecrypt, decryptionInput);

        decryptionInput.setPrefWidth(450);
       // decryptionInput.setPrefHeight(400);
        decryptionInput.setAlignment(Pos.TOP_LEFT);
        
        
        
        TextField decryptionOutput = new TextField ();

        Label labelDecrypted = new Label("Decrypted: ");
        secondRow.getChildren().addAll(labelDecrypted, decryptionOutput);

        decryptionOutput.setPrefWidth(450);
        //decryptionOutput.setPrefHeight(400);
        decryptionInput.setAlignment(Pos.TOP_LEFT);
        
        
        Button decryptButton = new Button("Decrypt");

        secondRow.getChildren().add(decryptButton);
        
        
        decryptButton.setOnAction(e -> {
        	

    		List<String> parts = getParts(decryptionInput.getText(), 309);
    		String outputText = "";
    		for(String s : parts) {
    			BigInteger a = new BigInteger(s);
    			BigInteger dec = DecrpytMessage(a, d, n);
    			outputText += Character.toString(dec.shortValue() + '\0');
    			
    		}
    		decryptionOutput.setText(outputText);
      	
      });
        

        verticalBox.getChildren().add(secondRow);
        
        
        HBox filePart = new HBox();
        
        Button chooseFile = new Button("Choose file to encrypt");
        Button decryptChooseFile = new Button("Choose file to decrypt");

        
        FileChooser fileChooser = new FileChooser();

        
        
        filePart.getChildren().add(chooseFile);
        filePart.getChildren().add(decryptChooseFile);
        
        verticalBox.getChildren().add(filePart);
        
        chooseFile.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
    		Scanner s;
			try {
				s = new Scanner(selectedFile);
	    		String outputText = "";

				while(s.hasNext()) {
		    		String input = s.nextLine();
		    		System.out.println(input);
		    		char charArr[] = input.toCharArray();
		
		
		    		for(char c : charArr) {
		    			String convertedCharacter = "";
		    			    			
		    			BigInteger a = new BigInteger(String.valueOf((int)c));
		
		    			BigInteger enc = EncryptMessage(a, RSA.e, n);
		    			convertedCharacter += enc;
		
		    			while(convertedCharacter.length() < 309) {
		    				convertedCharacter = "0" + convertedCharacter;
		    			}
		    			outputText += convertedCharacter;
		    			
		    		}
				}

    		

    	   
        		System.out.println("asdfasdf");

        		File encryptedFile = new File(selectedFile.getParent() + "/"+ selectedFile.getName() + "_encrypted" + ".txt");
        	    if (!encryptedFile.exists()) {
        	    	encryptedFile.createNewFile();
        	    }
        	    
        	    FileWriter fw = new FileWriter(encryptedFile.getAbsoluteFile());
        	    BufferedWriter bw = new BufferedWriter(fw);
				bw.write(outputText);
	    	    bw.close();
	    	    

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      });
        
        
        decryptChooseFile.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
    		Scanner s;
			try {
				s = new Scanner(selectedFile);
	    		String outputText = "";

				while(s.hasNext()) {

		    		String input = s.nextLine();
		    		System.out.println(input);
		
		    		List<String> parts = getParts(input, 309);
		    		for(String str : parts) {
		    			BigInteger a = new BigInteger(str);
		    			BigInteger dec = DecrpytMessage(a, d, n);
		    			outputText += Character.toString(dec.shortValue() + '\0');
		    			
		    		}
				}
    	   
        		System.out.println("asdfasdf");

        		File encryptedFile = new File(selectedFile.getParent() + "/"+ selectedFile.getName() + "_decrypted" + ".txt");
        	    if (!encryptedFile.exists()) {
        	    	encryptedFile.createNewFile();
        	    }
        	    
        	    FileWriter fw = new FileWriter(encryptedFile.getAbsoluteFile());
        	    BufferedWriter bw = new BufferedWriter(fw);
				bw.write(outputText);
	    	    bw.close();
	    	    

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      });
        pane.getChildren().add(verticalBox);
        


        
        
        //secondRow.getChildren().add(generateKeys);
        
        
        Scene scene = new Scene(pane);
        
        stage.setTitle("RSA"); 
        stage.setScene(scene); 
        stage.sizeToScene(); 
        stage.show(); 		
	}
    private static List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }
	public static void GenerateEncryptionAndDecrpytionKey() {
		p = generateLargePrimeNumber(RSAKeySize);
		q = generateLargePrimeNumber(RSAKeySize);

		n = p.multiply(q);
		
		
		phiOfN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		
		if(fastEncryptionOn) {
			
			do {
			    e = new BigInteger(8, new Random());
			} while (e.compareTo(phiOfN) >= 0 || !e.gcd(phiOfN).equals(BigInteger.ONE) || e.bitLength() < RSAKeySize * 2);
			
			d = e.modInverse(phiOfN);

		} else {
			do {
			    e = new BigInteger(n.bitLength(), new Random());
			} while (e.compareTo(phiOfN) >= 0 || !e.gcd(phiOfN).equals(BigInteger.ONE));

			d = e.modInverse(phiOfN);
			
		}
//		System.out.println(e);
//		System.out.println(d);
//		System.out.println("");
//		System.out.println("");
	}
	
    public static void main(String args[]) throws Exception
    {
    	BigInteger sasa = new BigInteger("01");
    	BigInteger sasa1 = new BigInteger("1");
    	System.out.println("sdfasdgsfasdfasdfgvasdgv");
    	System.out.println(sasa);
    	System.out.println(sasa1);
		//Scanner s = new Scanner(System.in);
		
		//Default key size
		
//		BigInteger p = generateLargePrimeNumber(RSAKeySize);
//		BigInteger q = generateLargePrimeNumber(RSAKeySize);
//
//		BigInteger n = p.multiply(q);
//		
//		
//		BigInteger phiOfN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
//
//		BigInteger e;
//		BigInteger d;
		RSA test = new RSA();
		test.launch(args);
		
			
//		if(fastEncryptionOn) {
//			
//			do {
//			    e = new BigInteger(8, new Random());
//			} while (e.compareTo(phiOfN) >= 0 || !e.gcd(phiOfN).equals(BigInteger.ONE) );
//			
//			d = e.modInverse(phiOfN);
//
//		} else {
//			do {
//			    e = new BigInteger(phiOfN.bitLength(), new Random());
//			} while (e.compareTo(phiOfN) >= 0 || !e.gcd(phiOfN).equals(BigInteger.ONE) );
//			
//			d = e.modInverse(phiOfN);
//		}
		
//		String input = s.nextLine();
//		
//		char charArr[] = input.toCharArray();
//		
//		for(char c : charArr) {
//			BigInteger a = new BigInteger(String.valueOf((int)c));
//			System.out.println("aaaaaaaaaaaaa" +a);
//			BigInteger enc = EncryptMessage(a, e, phiOfN);
//			System.out.println("Encrypted Message " + enc);
//			System.out.println();
	//			System.out.println("Decrypted Message " + DecrpytMessage(enc, d, phiOfN).shortValue());
//			System.out.println();
//
//		}
//		
//		System.out.println(input);
//		
		
		
		//BigInteger encryptedText = EncryptMessage(plaintext, e, phiOfN);


    }
    
    //Algorithm from Wikipedia: https://en.wikipedia.org/wiki/Fermat_primality_test
    /*
	 * 
	 * 
	 * The algorithm can be written as follows:
	 * 
	 * Inputs: n: a value to test for primality, n>3; k: a parameter that determines
	 * the number of times to test for primality Output: composite if n is
	 * composite, otherwise probably prime Repeat k times:
	 * 
	 * Pick a randomly in the range [2, n − 2] If a n − 1 ≢ 1 ( mod n )
	 * a^{{n-1}}\not \equiv 1{\pmod n}, then return composite
	 * 
	 * If composite is never returned: return probably prime
	 * 
	 * The a values 1 and n-1 are not used as the equality holds for all n and all
	 * odd n respectively, hence testing them adds no value.
	 * 
	 * 
	 * 
	 * 
	 */
    
    
    private final static Random rand = new Random();

    private static BigInteger getRandomFermatBase(BigInteger n)
    {
        // Rejection method: ask for a random integer but reject it if it isn't
        // in the acceptable set.

        while (true)
        {
            final BigInteger a = new BigInteger (n.bitLength(), rand);
            // must have 1 <= a < n
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(n) < 0)
            {
                return a;
            }
        }
    }

    public static boolean checkPrime(BigInteger n, int maxIterations)
    {
        if (n.equals(BigInteger.ONE))
            return false;

        for (int i = 0; i < maxIterations; i++)
        {
            BigInteger a = getRandomFermatBase(n);
            a = a.modPow(n.subtract(BigInteger.ONE), n);

            if (!a.equals(BigInteger.ONE))
                return false;
        }

        return true;
    }
    
    public static BigInteger generateLargePrimeNumber(int size) {
    	/*
		 * 
		 * 
		 * https://stackoverflow.com/questions/1146274/generating-really-big-primes
		 * 
		 * You don't generate prime numbers exactly. You generate a large odd number
		 * randomly, then test if that number is prime, if not generate another one
		 * randomly. There are some laws of prime numbers that basically state that your
		 * odds of "hitting" a prime via random tries is (2/ln n)
		 * 
		 * For example, if you want a 512-bit random prime number, you will find one in
		 * 2/(512*ln(2)) So roughly 1 out of every 177 of the numbers you try will be
		 * prime.
		 * 
		 * There are multiple ways to test if a number is prime, one good one is the
		 * "Miller-Rabin test" as stated in another answer to this question.
		 * 
		 * 
		 */
    	
    	
    	//So basically, generate random odd numbers until primality condition is satisfied
    	//using fermats primality test. 
    	//if the modulus of the random number is 1, then it is an odd number.
    	
//    	boolean numberIsPrime = false;
//    	LargeNumber primeNumber = new LargeNumber(2, 1);
//    	
//    	//Since integer type is 32 bit in java, exponentation should be at least
//    	//512-32 = 480 
//    	Random random = new Random();
//    	while(!numberIsPrime) {
//    		primeNumber = new LargeNumber(Random.next, 480);
//    	}
//    		
//    	
//    	LargeNumber oddNumber = new LargeNumber(Math.random() % 2 == 1,1);
//    	
//    	return new LargeNumber(1,1);
    	boolean isPrime = false;
    	BigInteger b = null;
    	while(!isPrime) {
        	b = BigInteger.probablePrime(size, new Random()); 
        	isPrime = checkPrime(b, 20);
    	}
    	return b;
    }
    
    public static BigInteger EncryptMessage(BigInteger plaintext, BigInteger eKey, BigInteger n) {
    	
    	BigInteger encryptedText = plaintext.modPow(eKey, n);
    	
    	return encryptedText;
    }
    
    public static BigInteger DecrpytMessage(BigInteger ciphertext, BigInteger dKey, BigInteger n) {
    	
    	BigInteger decryptedText = ciphertext.modPow(dKey, n);
    	
    	return decryptedText;
    }
    
	public static int FastSquareMul(int number, int exponent, int mod) {
		String binaryString = Integer.toBinaryString(exponent);
		int initialNumber = number;
		
		int modulo = initialNumber % mod;
		for(int i = 1; i < binaryString.length(); i++) {
			System.out.println(number);

			number *= number;
			if(binaryString.charAt(binaryString.length() - 1 - i) == '1')
				number *= initialNumber;
			modulo %= mod;
		}
		
		return number;
		
	}

}
