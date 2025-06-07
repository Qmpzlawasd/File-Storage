package ro.unibuc.filespace.Helper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ro.unibuc.filespace.Dto.InvitationDto;
import ro.unibuc.filespace.Exception.InvitationExpired;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.*;
import java.util.Base64;

@Configuration
public class EncryptionHelper {
    @Value("${jwt.private.key}")
    private String privateKeyPath;

    @Value("${jwt.public.key}")
    private String publicKeyPath;

    @Value("${invite.expiration.minutes}")
    private long inviteExpirationMinutes;

    @Value("${aes.key}")
    private String keyString;

    private void checkInviteTimestamp(String expirationTime) {
        Instant expiration = Instant.parse(expirationTime);
        if (Instant.now().isAfter(expiration)) {
            throw new InvitationExpired();
        }
    }

    private String buildInvitationString(String groupName, String userName) throws Exception {
        return String.format("%s|%s|%s", Instant.now().plusSeconds(60 * inviteExpirationMinutes), groupName, userName);
    }

    public InvitationDto decodeInvitation(String base64RsaAesInviteString) throws Exception {
        byte[] rsaAesInviteString = Base64.getDecoder().decode(base64RsaAesInviteString);

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey());
        byte[] aesInviteString = rsaCipher.doFinal(rsaAesInviteString);


        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec aesKey = new SecretKeySpec(keyBytes, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] inviteString = aesCipher.doFinal(aesInviteString);

        String[] parts = new String(inviteString, StandardCharsets.UTF_8).split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("format of invite string is incorrect");
        }

        String expirationTimeString = parts[0];
        checkInviteTimestamp(expirationTimeString);

        String groupName = parts[1];
        String username = parts[2];
        return new InvitationDto(username, groupName);
    }

    public String encodeInvitation(String groupName, String userName) throws Exception {
        String inviteString = this.buildInvitationString(groupName, userName);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");

        keyGen.init(256);
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec aesKey = new SecretKeySpec(keyBytes, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] aesInviteString = aesCipher.doFinal(inviteString.getBytes());

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey());
        byte[] rsaAesInviteString = rsaCipher.doFinal(aesInviteString);

        return Base64.getEncoder().encodeToString(rsaAesInviteString);
    }

    private String readKeyFile(String path) throws IOException {
        return Files.readString(Paths.get(path))
                .replaceAll("-----BEGIN (.*) KEY-----", "")
                .replaceAll("-----END (.*) KEY-----", "")
                .replaceAll("\\s", "");
    }

    public RSAPublicKey publicKey() throws Exception {
        String keyContent = readKeyFile(publicKeyPath);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    public RSAPrivateKey privateKey() throws Exception {
        String keyContent = readKeyFile(privateKeyPath);
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }
}
