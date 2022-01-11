package gui;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;


// Klasse zum Ver- und Entschluesseln von Nachrichten.
// Dieser Prozess findet auf Client-Side statt. Der Server wird lediglich eine verschluesselte
// Nachricht empfangen und diese nicht weiter bearbeiten. Diese sendet er an alle Clients weiter, 
// welche die Nachricht dann wieder bei sich angekommen entschluesseln und auslesen.
public class AESEncryptor {

	private static SecretKey key;
	private static final int KEY_SIZE = 128;
	private static final int T_LEN = 128;
	private byte[] IV;
	

	// Zu Testzwecken
//	public void init() throws Exception {
//		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//		keyGen.init(KEY_SIZE);
//		key = keyGen.generateKey();
//	}
	
	// Methode die der Client nutzt um den Encryptor zu initialisieren.
	// Empfaengt Strings um den Key und IV zu setzen.
	public void initFromStrings(String secretKey, String IV) {
		key = new SecretKeySpec(decode(secretKey), "AES");
		this.IV = decode(IV);
	}
	
	// Methode um Nachrichten zu verschluesseln.
	public String encrypt(String pText) throws Exception {
		Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
		encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
		IV = encryptionCipher.getIV();
		byte[] encryptedBytes = encryptionCipher.doFinal(pText.getBytes());
		return encode(encryptedBytes);
	}
	
	// Methode um Nachrichten zu entschluesseln.
	public String decrypt(String cText) throws Exception {
		Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
		decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] decryptedBytes = decryptionCipher.doFinal(decode(cText));
		return new String(decryptedBytes);
	}
	
	private String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	private byte[] decode(String data) {
		return Base64.getDecoder().decode(data);
	}
	
	
}
