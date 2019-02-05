package net.novelmc.novelengine.banning;

import java.util.UUID;
import java.util.logging.Logger;

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
                PREF = "NM"; // Regular ban;
                break;
            case IP:
                PREF = "IP"; //Regular IP ban
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

        VALUE = UUID.randomUUID().toString().substring(0, 7);

        banID = PREF + "-" + VALUE.toLowerCase();
        return banID;
    }
}
