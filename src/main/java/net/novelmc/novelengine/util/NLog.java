package net.novelmc.novelengine.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NLog
{

    public static Logger logger = Logger.getLogger("Minecraft");
    public static String prefix = "[NovelEngine] ";

    public static void info(String message)
    {
        logger.log(Level.INFO, "{0}{1}", new Object[]
        {
            prefix, message
        });
    }

    public static void severe(String message)
    {
        logger.log(Level.SEVERE, "{0}{1}", new Object[]
        {
            prefix, message
        });
    }

    public static void severe(Exception ex)
    {
        logger.log(Level.SEVERE, "{0}{1}", new Object[]
        {
            prefix, ex.toString()
        });
    }

    public static void warning(String message)
    {
        logger.log(Level.WARNING, "{0}{1}", new Object[]
        {
            prefix, message
        });
    }
}
