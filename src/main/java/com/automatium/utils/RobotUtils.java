package com.automatium.utils;

import com.automatium.exception.UIInteractionException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * Created by Gurusharan on 04-06-2017.
 */
public class RobotUtils {
    public static void enterTextInSystemDialog(String textToEnter) {
        try {

            // Copy the text to clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(textToEnter), null);

            Robot robot = new Robot();

            // Paste from the clipboard
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("mac")) {
                robot.keyPress(KeyEvent.VK_META);
            }
            else {
                robot.keyPress(KeyEvent.VK_CONTROL);
            }

            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);

            if (os.contains("mac")) {
                robot.keyRelease(KeyEvent.VK_META);
            }
            else {
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }

        } catch (AWTException e) {
            throw new UIInteractionException("Unable to access the dialog box: " + e.toString());
        }
    }

    public static void acceptSystemDialog() {
        try {
            Robot robot = new Robot();
            // Press the enter key
            robot.keyPress(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            throw new UIInteractionException("Unable to access the dialog box: " + e.toString());
        }
    }

    public static void dismissSystemDialog() {
        try {
            Robot robot = new Robot();
            // Press the enter key
            robot.keyPress(KeyEvent.VK_ESCAPE);

        } catch (AWTException e) {
            throw new UIInteractionException("Unable to access the dialog box: " + e.toString());
        }
    }
}
