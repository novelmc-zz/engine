package net.novelmc.novelengine.banning;

import java.util.Random;

public class BanUIDGen
{
    protected static String PREF;
    protected static String VALUE;
    protected static String banID;
    
    public static String idGen(BanType type) 
    {
        switch (type) 
        {
            case NORMAL:
                PREF = "N"; // Regular ban;
                break;
            case IP:
                PREF = "I"; //Regular IP ban
                break;
            case PERMANENT_NAME:
                PREF = "PN"; //Permanent name ban
                break;
            case PERMANENT_IP: 
                PREF = "PI"; //Permanent IP ban
                break;
            default:
                PREF = "UD"; //Undetermined (In case for some reason it fails to get a ban type)
                break;
        }
        
        String UIDCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder uid = new StringBuilder();
        Random rnd = new Random();
        while (uid.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * UIDCHARS.length());
            uid.append(UIDCHARS.charAt(index));
        }
        VALUE = uid.toString();
        
        banID = PREF + VALUE;
        return banID;
    }
    
   
}
