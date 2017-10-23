package server.utility;

import com.google.gson.Gson;
import server.config.Config;

public class Encryption {

    /**
     * Kryptering kan slås til og fra i config.json
     * <p>
     * Fremgansmåde:
     * <p>
     * Klienten sender et krypteret Json objekt til serveren.
     * Krypteringen hos klienten medfører, at objektet ikke længere er JSON, men blot en ciffertekst.
     * Derfor skal cifferteksten parses til JSON, således at serveren kan modtage det.
     * Det modtagede JSON objekt unparses fra JSON til ciffertekst, således at det kan dekrypteres.
     * Efter objektet er dekrypteret er det igen JSON.
     * Herefter unparses objektet fra JSON igen, således at vi kan bruge objektet i serveren.
     */

    //method to encrypt a string
    public String encryptXOR(String toBeEncrypted) {

        //check if encryption is true in the Config file
        if (Config.getENCRYPTION()) {
            //Vi vælger selv værdierne til nøglen
            char[] key = {'Y', 'O', 'L', 'O'};
            //En StringBuilder er en klasse, der gør det muligt at ændre en string
            StringBuilder isBeingEncrypted = new StringBuilder();

            for (int i = 0; i < toBeEncrypted.length(); i++) {
                isBeingEncrypted.append((char) (toBeEncrypted.charAt(i) ^ key[i % key.length]));
            }

            //convert StringBuilder to String and parse as Json
            String isEncrypted = new Gson().toJson(isBeingEncrypted.toString());

            //reuturn encrypted String
            return isEncrypted;
        }else {
            //if encryption is false in the Config file return default value
            return toBeEncrypted;
        }

    }

    //method to decrypt a string parsed as Json
    public String decryptXOR(String toBeDecrypted) {

        //check if encryption is true in the Config file
        if (Config.getENCRYPTION()) {
            //Parse Json with encrypted Json object to a String with the Encrypted Object thats no longer a Json ( {"rewqr"} => rewqr )
            //then Decrypt the object and assign it as a Object in Json format ( rewqr => {"username":"..." }
            toBeDecrypted = new Gson().fromJson(toBeDecrypted, String.class);

            //Vi vælger selv værdierne til nøglen
            char[] key = {'Y', 'O', 'L', 'O'};
            //En StringBuilder er en klasse, der gør det muligt at ændre en string
            StringBuilder beingDecrypted = new StringBuilder();

            for (int i = 0; i < toBeDecrypted.length(); i++) {
                beingDecrypted.append((char) (toBeDecrypted.charAt(i) ^ key[i % key.length]));
            }

            //convert StringBuilder to String
            String isDecrypted = beingDecrypted.toString();

            //return String
            return isDecrypted;
        }else {
            //if encryption is false in the Config file return default value
            return toBeDecrypted;
        }

    }

}

