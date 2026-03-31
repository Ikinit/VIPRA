package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Branding {
    public Font jetBrainsBExtraSmall, jetBrainsBSmall, jetBrainsBMedium, jetBrainsBLarge, jetBrainsBExtraLarge, jetBrainsBGiant;
    public Font jetBrainsRExtraSmall, jetBrainsRSmall, jetBrainsRMedium, jetBrainsRLarge, jetBrainsRExtraLarge, jetBrainsRGiant;
    public ImageIcon lightIcoSimulate, lightIcoHowToUse, lightIcoSettings, lightIcoExit;
    public ImageIcon lightIcoAddProcess, lightIcoRemoveProcess, lightIcoImportProcess, lightIcoRandomProcess;
    public ImageIcon darkIcoSimulate, darkIcoHowToUse, darkIcoSettings, darkIcoExit;
    public ImageIcon darkIcoAddProcess, darkIcoRemoveProcess, darkIcoImportProcess, darkIcoRandomProcess;
    public Image backgroundDark, backgroundLight;
    public Image darkHowToUseAlgo, darkHowToUseInput, darkHowToUseButtons, darkHowToUseOutput;
    public Image lightHowToUseAlgo, lightHowToUseInput, lightHowToUseButtons, lightHowToUseOutput;

    public Branding() {
        initializeFonts();
        initializeIcons();
        initializeImages();
    }

    public Color dark = Color.decode("#09090B");
    public Color light = Color.decode("#ffffff");
    public Color darkGray = Color.decode("#2e2e2e");
    public Color lightGray = Color.decode("#d1d1d1");
    public Color selected = Color.decode("#009DFF");

    public boolean isDark = true;
    public void toggleTheme() {
        Color tmpColor = dark;
        dark = light;
        light = tmpColor;

        Color tmpHover = darkGray;
        darkGray = lightGray;
        lightGray = tmpHover;

        Image tmpBg = backgroundDark;
        backgroundDark = backgroundLight;
        backgroundLight = tmpBg;

        ImageIcon tmpSim = darkIcoSimulate;
        darkIcoSimulate = lightIcoSimulate;
        lightIcoSimulate = tmpSim;

        ImageIcon tmpHow = darkIcoHowToUse;
        darkIcoHowToUse = lightIcoHowToUse;
        lightIcoHowToUse = tmpHow;

        ImageIcon tmpSet = darkIcoSettings;
        darkIcoSettings = lightIcoSettings;
        lightIcoSettings = tmpSet;

        ImageIcon tmpExit = darkIcoExit;
        darkIcoExit = lightIcoExit;
        lightIcoExit = tmpExit;

        ImageIcon tmpAdd = darkIcoAddProcess;
        darkIcoAddProcess = lightIcoAddProcess;
        lightIcoAddProcess = tmpAdd;

        ImageIcon tmpRemove = darkIcoRemoveProcess;
        darkIcoRemoveProcess = lightIcoRemoveProcess;
        lightIcoRemoveProcess = tmpRemove;

        ImageIcon tmpImport = darkIcoImportProcess;
        darkIcoImportProcess = lightIcoImportProcess;
        lightIcoImportProcess = tmpImport;

        ImageIcon tmpRandom = darkIcoRandomProcess;
        darkIcoRandomProcess = lightIcoRandomProcess;
        lightIcoRandomProcess = tmpRandom;

        Image tmpAlgo = darkHowToUseAlgo;
        darkHowToUseAlgo = lightHowToUseAlgo;
        lightHowToUseAlgo = tmpAlgo;

        Image tmpBtn = darkHowToUseButtons;
        darkHowToUseButtons = lightHowToUseButtons;
        lightHowToUseButtons = tmpBtn;

        Image tmpInput = darkHowToUseInput;
        darkHowToUseInput = lightHowToUseInput;
        lightHowToUseInput = tmpInput;

        Image tmpOutput = darkHowToUseOutput;
        darkHowToUseOutput = lightHowToUseOutput;
        lightHowToUseOutput = tmpOutput;

        isDark = !isDark;
    }

    public Color[] processColor = {
        Color.decode("#E6194B"),
        Color.decode("#2ECC71"),
        Color.decode("#4363D8"),
        Color.decode("#F58231"),
        Color.decode("#9B59B6"),
        Color.decode("#1ABC9C"),
        Color.decode("#E91E8C"),
        Color.decode("#CDDC39"),
        Color.decode("#00BCD4"),
        Color.decode("#FF5722"),
        Color.decode("#3F51B5"),
        Color.decode("#8BC34A"),
        Color.decode("#FF9800"),
        Color.decode("#795548"),
        Color.decode("#9C27B0"),
        Color.decode("#009688"),
        Color.decode("#F06292"),
        Color.decode("#607D8B"),
        Color.decode("#FFEB3B"),
        Color.decode("#76FF03")
    };

    public void initializeFonts() {
        System.out.println("Loading Fonts...");

        try {
            Font jetBrainsB = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/fonts/JetBrainsMono-Bold.ttf"));
            jetBrainsBExtraSmall = jetBrainsB.deriveFont(Font.BOLD, 10f);
            jetBrainsBSmall = jetBrainsB.deriveFont(Font.BOLD, 14f);
            jetBrainsBMedium = jetBrainsB.deriveFont(Font.BOLD, 16f);
            jetBrainsBLarge = jetBrainsB.deriveFont(Font.BOLD, 24f);
            jetBrainsBExtraLarge = jetBrainsB.deriveFont(Font.BOLD, 32f);
            jetBrainsBGiant = jetBrainsB.deriveFont(Font.BOLD, 65f);

            Font jetBrainsR = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/fonts/JetBrainsMono-Regular.ttf"));
            jetBrainsRExtraSmall = jetBrainsR.deriveFont(Font.BOLD, 10f);
            jetBrainsRSmall = jetBrainsR.deriveFont(Font.BOLD, 14f);
            jetBrainsRMedium = jetBrainsR.deriveFont(Font.BOLD, 16f);
            jetBrainsRLarge = jetBrainsR.deriveFont(Font.BOLD, 24f);
            jetBrainsRExtraLarge = jetBrainsR.deriveFont(Font.BOLD, 32f);
            jetBrainsRGiant = jetBrainsR.deriveFont(Font.BOLD, 60f);
        } catch (FontFormatException | IOException e){
            System.err.println("Font failed to load.");
        }
    }

    public void initializeIcons() {
        System.out.println("Loading Icons...");
        try {
            BufferedImage buff_lightIcoSimulate = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-simulate.png"));
            BufferedImage buff_lightIcoHowToUse = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-how_to_use.png"));
            BufferedImage buff_lightIcoSettings = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-settings.png"));
            BufferedImage buff_lightIcoExit = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-exit.png"));

            BufferedImage buff_lightIcoAddProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-add_process.png"));
            BufferedImage buff_lightIcoRemoveProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-remove_process.png"));
            BufferedImage buff_lightIcoImportProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-import_process.png"));
            BufferedImage buff_lightIcoRandomProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/light-random_process.png"));

            BufferedImage buff_darkIcoSimulate = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-simulate.png"));
            BufferedImage buff_darkIcoHowToUse = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-how_to_use.png"));
            BufferedImage buff_darkIcoSettings = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-settings.png"));
            BufferedImage buff_darkIcoExit = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-exit.png"));

            BufferedImage buff_darkIcoAddProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-add_process.png"));
            BufferedImage buff_darkIcoRemoveProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-remove_process.png"));
            BufferedImage buff_darkIcoImportProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-import_process.png"));
            BufferedImage buff_darkIcoRandomProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/dark-random_process.png"));

            lightIcoSimulate = resizeImage(buff_lightIcoSimulate, 1f);
            lightIcoHowToUse = resizeImage(buff_lightIcoHowToUse, 1f);
            lightIcoSettings = resizeImage(buff_lightIcoSettings, 1f);
            lightIcoExit = resizeImage(buff_lightIcoExit, 1f);

            lightIcoAddProcess = resizeImage(buff_lightIcoAddProcess, 0.35f);
            lightIcoRemoveProcess = resizeImage(buff_lightIcoRemoveProcess, 0.35f);
            lightIcoImportProcess = resizeImage(buff_lightIcoImportProcess, 0.35f);
            lightIcoRandomProcess = resizeImage(buff_lightIcoRandomProcess, 0.35f);

            darkIcoSimulate = resizeImage(buff_darkIcoSimulate, 1f);
            darkIcoHowToUse = resizeImage(buff_darkIcoHowToUse, 1f);
            darkIcoSettings = resizeImage(buff_darkIcoSettings, 1f);
            darkIcoExit = resizeImage(buff_darkIcoExit, 1f);

            darkIcoAddProcess = resizeImage(buff_darkIcoAddProcess, 0.35f);
            darkIcoRemoveProcess = resizeImage(buff_darkIcoRemoveProcess, 0.35f);
            darkIcoImportProcess = resizeImage(buff_darkIcoImportProcess, 0.35f);
            darkIcoRandomProcess = resizeImage(buff_darkIcoRandomProcess, 0.35f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initializeImages() {
        System.out.println("Loading Images...");
        try {
            backgroundDark = ImageIO.read(getClass().getResourceAsStream("/assets/images/background-dark.png"));
            backgroundLight = ImageIO.read(getClass().getResourceAsStream("/assets/images/background-light.png"));

            darkHowToUseAlgo = ImageIO.read(getClass().getResourceAsStream("/assets/images/dark-howtouse-algorithm.png"));
            darkHowToUseInput = ImageIO.read(getClass().getResourceAsStream("/assets/images/dark-howtouse-inputs.png"));
            darkHowToUseButtons = ImageIO.read(getClass().getResourceAsStream("/assets/images/dark-howtouse-buttons.png"));
            darkHowToUseOutput = ImageIO.read(getClass().getResourceAsStream("/assets/images/dark-howtouse-output.png"));

            lightHowToUseAlgo = ImageIO.read(getClass().getResourceAsStream("/assets/images/light-howtouse-algorithm.png"));
            lightHowToUseInput = ImageIO.read(getClass().getResourceAsStream("/assets/images/light-howtouse-inputs.png"));
            lightHowToUseButtons = ImageIO.read(getClass().getResourceAsStream("/assets/images/light-howtouse-buttons.png"));
            lightHowToUseOutput = ImageIO.read(getClass().getResourceAsStream("/assets/images/light-howtouse-output.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ImageIcon resizeGIF(ImageIcon icon, int width, int height) {
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    public ImageIcon resizeImage(BufferedImage original, float scale) {
        int newWidth  = Math.round(original.getWidth()  * scale);
        int newHeight = Math.round(original.getHeight() * scale);

        Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(resized);
    }

    public ImageIcon resizeImageIcon(ImageIcon original, float scale) {
        Image img = original.getImage();
        
        int newWidth  = Math.round(img.getWidth(null)  * scale);
        int newHeight = Math.round(img.getHeight(null) * scale);

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(resized);
    }
}