package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandHelper {

    public static String executeCommand(String... command) {
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.getOutputStream().close();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = stdout.readLine())!= null) {
                result.append(line);
                result.append('\n');
            }
            if(result.length() > 0)
                result.deleteCharAt(result.length()-1);
            final int exitCode = process.waitFor();
            stdout.close();
            System.out.println(Arrays.toString(command) + " response: " + String.valueOf(exitCode));
        }catch (IOException e){
            e.printStackTrace();
            System.out.print("Command is: ");
            System.out.println(Arrays.toString(command));
            throw new RuntimeException("Execute command error!!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Command (" + Arrays.toString(command) + ") was interrupted", e);
        }
        return result.toString();
    }
}
