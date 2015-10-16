package sketchwars.util;

import java.io.File;
import java.lang.reflect.Field;

public class Config
{
    public static void appendToLibraryPath(String libPath) 
    {
        String newLibPath = libPath + File.pathSeparator + System.getProperty("java.library.path");
        System.setProperty("java.library.path", newLibPath);
        try
        {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            if (fieldSysPath != null) {
                fieldSysPath.set(System.class.getClassLoader(), null);
            }
        }
        catch(Exception e)
        {
            System.err.println("Error setting library path: " + e);
        }
    }
    private Config() {}
}