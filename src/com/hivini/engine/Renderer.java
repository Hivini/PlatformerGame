package com.hivini.engine;

import java.awt.image.DataBuffer;
import com.hivini.engine.gfx.*;
import java.awt.image.DataBufferInt;

public class Renderer {

    private int pW, pH;
    private int[] p;
    private int[] zBuffer;
    private int zDepth = 0;

    private Font font = Font.STANDARD;

    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
    }

    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0;
            zBuffer[i] = 0;
        }
    }

    public void setPixel(int x, int y, int value) {
        int alpha = (value >> 24) & 0xff;
        // The value >> 24 & 0xff is for the alpha
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0) {
            return;
        }

        if (zBuffer[x + y * pW] > zDepth) return;
        if (alpha == 255) {
            p[x + y * pW] = value;
        }
        else {
            int color = 0;
            int pixelColor = p[x + y * pW];

            // Getting the colors
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            p[x + y * pW] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    if (font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, int offX, int offY) {

        // Testers for prevent unnecesary renders
        if (offX < -image.getWidth()) return;
        if (offY < -image.getHeight()) return;
        if (offX >= pW) return;
        if (offY >= pH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        if (offX < 0) newX -= offX;
        if (offY < 0) newY -= offY;
        if (newWidth + offX > pW) newWidth -= newWidth + offX - pW;
        if (newHeight + offY > pH) newHeight -= newHeight + offY - pH;

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getP()[x + y * image.getWidth()]);
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {

        // Testers for prevent unnecesary renders
        if (offX < -image.getTileW()) return;
        if (offY < -image.getTileH()) return;
        if (offX >= pW) return;
        if (offY >= pH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        if (offX < 0) newX -= offX;
        if (offY < 0) newY -= offY;
        if (newWidth + offX > pW) newWidth -= newWidth + offX - pW;
        if (newHeight + offY > pH) newHeight -= newHeight + offY - pH;

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY + image.getTileH()) * image.getWidth()]);
            }
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {

        for (int y = 0; y <= height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);

        }
        for (int x = 0; x <= width; x++) {
            setPixel(x + offY, offY, color);
            setPixel(x + offY, offY + height, color);
        }
    }

    public void drawFillRect(int offX, int offY, int width, int height, int color) {

        // Testers for prevent unnecesary renders
        if (offX < -width) return;
        if (offY < -height) return;
        if (offX >= pW) return;
        if (offY >= pH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        if (offX < 0) newX -= offX;
        if (offY < 0) newY -= offY;
        if (newWidth + offX > pW) newWidth -= newWidth + offX - pW;
        if (newHeight + offY > pH) newHeight -= newHeight + offY - pH;

        for (int y = newY; y <= newHeight; y++) {
            for (int x = newX; x <= newWidth; x++) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
