package org.example;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Comments for ${NAME} go here.
 *
 * @author Dan Howard
 * @since 2024-06-20 3:19 p.m.
 */
public class IconMaker extends Application {

    private Stage stage;

    private int bodyIndex = 0;
    private BufferedImage bodyImage;
    private int weaponIndex = 0;
    private BufferedImage weaponImage;
    private int headIndex = 0;
    private BufferedImage headImage;
    private int shieldIndex = 0;
    private BufferedImage shieldImage;

    private BufferedImage composedImage;

    private static final int PART_HEIGHT = 48;
    private static final int PART_WIDTH = 96;

    private static final int BODY_COUNT = 20;
    private static final int SHIELD_COUNT = 4;
    private static final int WEAPON_COUNT = 17;
    private static final int HEAD_COUNT = 19;

    //counting colors
    int color1 = 0;        //skin color
    int color1max = 11;
    int color2 = 0;        //color 1
    int color2max = 12;
    int color3 = 0;        //color 2
    int color3max = 12;
    int color4 = 0;        //hair color
    int color4max = 9;
    int color5 = 0;        //metal color
    int color5max = 9;

    ImageView icon;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        BorderPane root = new BorderPane();

        bodyImage = ImageIO.read(Objects.requireNonNull(IconMaker.class.getResourceAsStream("/parts/cm_Clothes.png")));
        weaponImage = ImageIO.read(Objects.requireNonNull(IconMaker.class.getResourceAsStream("/parts/cm_Arms.png")));
        headImage = ImageIO.read(Objects.requireNonNull(IconMaker.class.getResourceAsStream("/parts/cm_Faces.png")));
        shieldImage = ImageIO.read(Objects.requireNonNull(IconMaker.class.getResourceAsStream("/parts/cm_Shields.png")));

        icon = new ImageView();
        icon.setOpacity(1); // still white background
        icon.setFitHeight(PART_HEIGHT * 2);
        icon.setFitWidth(PART_WIDTH * 2);
        icon.setPreserveRatio(true);
        icon.setSmooth(false);
        icon.setCache(true);

        refreshImage();

        root.setCenter(icon);

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        var btnSize = new Button("Size");
        var btnHead = new Button("Head");
        btnHead.setOnAction(event -> {
            headIndex++;
            if (headIndex >= HEAD_COUNT) {
                headIndex = 0;
            }
            refreshImage();
        });

        var btnShield = new Button("Shield");
        btnShield.setOnAction(event -> {
            shieldIndex++;
            if (shieldIndex >= SHIELD_COUNT) {
                shieldIndex = 0;
            }
            refreshImage();
        });

        var btnBody = new Button("Body");
        btnBody.setOnAction(event -> {
            bodyIndex++;
            if (bodyIndex >= BODY_COUNT) {
                bodyIndex = 0;
            }
            refreshImage();
        });

        var btnArms = new Button("Arms");
        btnArms.setOnAction(event -> {
            weaponIndex++;
            if (weaponIndex >= WEAPON_COUNT) {
                weaponIndex = 0;
            }
            refreshImage();
        });

        var btnSkin = new Button("Skin");
        btnSkin.setOnAction(event -> {
            color1++;
            changeColors();
        });


        buttons.getChildren().addAll(btnSize, btnHead, btnShield, btnBody, btnArms, btnSkin);
        buttons.getChildren().addAll(new Button("Hair"));
        buttons.getChildren().addAll(new Button("Color 1"));
        buttons.getChildren().addAll(new Button("Color 2"));
        buttons.getChildren().addAll(new Button("Metal"));

        root.getStyleClass().add("panels");
        root.setBottom(buttons);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("/style.css");
        stage.setScene(scene);
        stage.setTitle("IconMaker");
        stage.show();
    }

    private void refreshImage() {
        System.out.println("refreshImage headIndex " + headIndex + " bodyIndex " + bodyIndex + " shieldIndex " + shieldIndex + " weaponIndex " + weaponIndex);
        System.out.println("" + headIndex + "," + bodyIndex + "," + shieldIndex + "," + weaponIndex);
        BufferedImage body = getImage(bodyIndex, bodyImage);
        BufferedImage head = getImage(headIndex, headImage);
        BufferedImage shield = getImage(shieldIndex, shieldImage);
        BufferedImage weapon = getImage(weaponIndex, weaponImage);

        composedImage = composeImage(shield, body, weapon, head);

        java.awt.Image image1 = composedImage.getScaledInstance(PART_WIDTH * 2, PART_HEIGHT * 2, java.awt.Image.SCALE_DEFAULT);
        icon.setImage(SwingFXUtils.toFXImage(toBufferedImage(image1), null));
    }


    BufferedImage getImage(int frame, BufferedImage tileSet) {
//        System.out.println("getImage frame: " + frame);
//        System.out.println("TS H " + tileSet.getHeight() + " TS W " + tileSet.getWidth());

        int h = PART_HEIGHT;
        int w = PART_WIDTH;

        int x = 0;
        int y = frame * h;
//        System.out.println("GETTING FROM x " + x + " y " + y + " w " + w + " h " + h);
        return tileSet.getSubimage(x, y, w, h);
    }


    void changeColors() {
        System.out.println("changeColors headIndex " + headIndex + " bodyIndex " + bodyIndex);
        //reset if over the limit
        if (color1 > color1max) {
            color1 = 0;
        }
        if (color2 > color2max) {
            color2 = 0;
        }
        if (color3 > color3max) {
            color3 = 0;
        }
        if (color4 > color4max) {
            color4 = 0;
        }
        if (color5 > color5max) {
            color5 = 0;
        }
        for (var x = 0; x < composedImage.getWidth(); x++) {
            for (var y = 0; y < composedImage.getHeight(); y++) {
                var index = (x + y * composedImage.getWidth()) * 4;   // ???

                // https://stackoverflow.com/questions/7749895/java-loop-through-pixels-in-an-image
                int clr = composedImage.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

//                var alpha = colorData.data[index + 3]; ???
                System.out.println("x " + x + " y " + y + " red " + red + " green " + green + " blue " + blue);

                switch (color1) {
                    case 1:
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 10;
                            green = green - 5;
                            blue = blue - 30;
                            break;
                        }
                    case 2:
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 20;
                            green = green - 15;
                            blue = blue - 50;
                            break;
                        }
                    case 3:
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 30;
                            green = green - 25;
                            blue = blue - 70;
                            break;
                        }
                    case 4: //light brown
                        if (red > 230 && green > 150 && blue > 110) {
                            red = green;
                            green = green - 50;
                            blue = blue - 80;
                            break;
                        }
                    case 5: //brown
                        if (red > 230 && green > 150 && blue > 110) {
                            red = green;
                            green = green - 80;
                            blue = blue - 110;
                            break;
                        }
                    case 6: //dark brown
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 120;
                            green = green - 120;
                            blue = blue - 110;
                            break;
                        }
                    case 7: //dark skin
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 180;
                            green = green - 140;
                            blue = blue - 100;
                            break;
                        }
                    case 8: //sea elf
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 190;
                            green = green + 30;
                            blue = blue + 50;
                            break;
                        }
                    case 9: //half-orc
                        if (red > 230 && green > 150 && blue > 110) {
                            red = red - 40;
                            green = green + 20;
                            blue = blue - 100;
                            break;
                        }
                    case 10: //grey
                        if (red > 230 && green > 150 && blue > 110) {
                            red = green;
                            green = green - 10;
                            blue = blue - 30;
                            break;
                        }
                    case 11: //infernal
                        if (red > 230 && green > 150 && blue > 110) {
                            red = green;
                            green = green - 160;
                            blue = blue - 130;
                            break;
                        }
                    default:
                }
                System.out.println("AFTER x " + x + " y " + y + " red " + red + " green " + green + " blue " + blue);

                Color color = new Color(red, green, blue);
                composedImage.setRGB(x, y, color.getRGB());
            }
        }

        java.awt.Image image1 = composedImage.getScaledInstance(PART_WIDTH * 2, PART_HEIGHT * 2, java.awt.Image.SCALE_DEFAULT);
        icon.setImage(SwingFXUtils.toFXImage(toBufferedImage(image1), null));
    }


    public static BufferedImage composeImage(BufferedImage... images) {
        return composeImage(Arrays.asList(images));
    }

    public static BufferedImage composeImage(List<BufferedImage> images) {
        BufferedImage im = new BufferedImage(images.getFirst().getWidth(), images.getFirst().getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = im.createGraphics();
        for (BufferedImage image : images) {
            g.drawImage(image, 0, 0, null);
        }
        g.dispose();
        return im;
    }


    public static BufferedImage toBufferedImage(java.awt.Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}