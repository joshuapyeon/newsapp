package com.example.wntprototype.ui.wordmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is a word cloud generator based on this one written in C:
 * https://github.com/SalvaEB/mask_word_cloud/blob/master/src/maskwc.cc
 *
 * Copyright Salva Espana Boquera sespana@dsic.upv.es LICENSE: LGPL v3
 *
 * @author Luke Greene
 */
public class WordCloudGenerator {
    private static boolean useColorSurface = true;
    private static final Random RAND = new Random();
    private static final String[] rmFromEnd = { ")", ".", "\"", "'", ";", ":", ",", "}", "]", ">", "?" };
    private static final String[] rmFromBegin = { "(", "\"", "'", ";", ":", ",", "{", "[", "<", "?" };
    private static final String[] stopwords = { "and", "that", "there", "that's", "there's", "don't", "have", "the", "but",
            "for", "into", "out", "be", "you", "are", "they", "their", "your", "yours", "theirs", "his", "hers", "he",
            "she", "her", "him", "them", "with", "what", "after", "over", "let", "may", "it's", "its", "where", "about",
            "had", "from", "new", "news", "official", "video", "says", "how", "more", "back" };

    private final Bitmap outputImage;
    private final int[] maxwidthrow;
    private final int[][] countmat;
    private final int[] scanner;
    private final Canvas graphics;

    /*
     * private static double r = 0; // RGB of background private static double g =
     * 0; private static double b = 0;
     */
    private static int MASKRGB = 0xFF000000; // Words can be painted where mask has this color.
    private static int PAINTRGB = 0xFF000000;
    private static int vertical_preference = 50;
    private static int font_step = 2;
    private static int mini_font_size = 5;
    private static int words_margin = 2;
    private static Bitmap maskImage = null;
    private static Bitmap colorImage = null;

    public static Bitmap generateWordCloud(int mrgb, int brgb, int vpref, int fstep, int minfsize, int maxfsize, int wmargin, Bitmap maskImg, Bitmap colorImg, String inputPth, String keyword) {
        MASKRGB = mrgb;
        PAINTRGB = brgb;
        vertical_preference = vpref;
        font_step = fstep;
        mini_font_size = minfsize;
        words_margin = wmargin;
        maskImage = maskImg;
        colorImage = colorImg;

        WordCloudGenerator wc = new WordCloudGenerator(maskImage);

        if (colorImage == null)
            useColorSurface = false;

        File file = new File(inputPth);
        // Essentially use this list as a bag, but we need to sort it
        List<Entry<String, Integer>> wordsList = new ArrayList<>();
        System.out.println("Creating words list...");
        Scanner s = null;

        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e1) {
            System.out.println("Input file not found; stopping...");
            e1.printStackTrace();
            System.exit(404);
        }

        double largest = 0.0;
        System.out.println("Keyword: " + keyword);
        while (s != null && s.hasNextLine()) {
            for (String str : s.nextLine().split(" ")) {
                str = str.trim();

                for (String rm : rmFromEnd)
                    if (str.endsWith(rm))
                        str = str.substring(0, str.length() - rm.length());

                for (String rm : rmFromBegin)
                    if (str.startsWith(rm))
                        str = str.substring(rm.length());

                boolean flag = false;
                for (String rm : stopwords)
                    if (str.toLowerCase().equals(rm))
                        flag = true;

                if (!keyword.isEmpty() && str.equalsIgnoreCase(keyword))
                    flag = true;

                if (str.length() >= 3 && !flag) {
                    int size = 0;
                    for (Entry<String, Integer> e : wordsList)
                        if (str.equals(e.getKey()))
                            e.setValue(size = e.getValue() + 1);
                    if (size == 0)
                        wordsList.add(new AbstractMap.SimpleEntry<>(str, 1));
                    if (size > largest)
                        largest = size;
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            wordsList.sort(Comparator.comparingInt(Entry<String, Integer>::getValue).reversed());
        double multiplier = (double) maxfsize / largest;
        System.out.println("Painting words...");
        boolean flag = true;
        do {
            for (Entry<String, Integer> e : wordsList)
                if (!wc.paintWord(wc.graphics, e.getKey(), Math.min(maxfsize, (int) ((double) e.getValue() * multiplier))))
                    flag = false;
        } while (flag);
        System.out.println("Done!");
        return wc.outputImage;
    }

    private WordCloudGenerator(Bitmap maskImage) {
        if (maskImage == null) {
            maskImage = Bitmap.createBitmap(900, 900, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < maskImage.getWidth(); i++)
                for (int j = 0; j < maskImage.getHeight(); j++)
                    maskImage.setPixel(i, j, MASKRGB);
        }
        WordCloudGenerator.maskImage = maskImage;
        this.outputImage = Bitmap.createBitmap(maskImage.getWidth(), maskImage.getHeight(), Bitmap.Config.ARGB_8888);
        this.maxwidthrow = new int[maskImage.getHeight()];
        this.countmat = new int[maskImage.getWidth()][maskImage.getHeight()];
        this.scanner = new int[maskImage.getWidth()];
        this.graphics = new Canvas(this.outputImage);

        // traverse pixelmat to process the mask and remove from the image
        for (int y = 0; y < maskImage.getHeight(); y++) {
            int count = 0;
            for (int x = 0; x < maskImage.getWidth(); x++) {
                if (maskImage.getPixel(x, y) == MASKRGB) { // not masked
                    count++;
                    if (count > maxwidthrow[y])
                        maxwidthrow[y] = count;
                } else { // masked
                    count = 0;
                }
                this.countmat[x][y] = count;
                // in any case paint in black
                this.outputImage.setPixel(x, y, PAINTRGB);
            }
        }
    }

    private Point findFreeRectangle(int bbWidth, int bbHeight) {
        int lasty = 0, firsty = 0;
        int the_x_pos = -1, the_y_pos = -1, solcount = 0;
        while (lasty < maskImage.getHeight()) {
            while (lasty < maskImage.getHeight() && (maxwidthrow[lasty] >= bbWidth)) {
                lasty++;
            }
            if (lasty - firsty >= bbHeight) {
                for (int x = 0; x < maskImage.getWidth(); ++x)
                    scanner[x] = 0;
                for (int y = firsty; y < lasty; ++y)
                    for (int x = bbWidth; x < maskImage.getWidth(); ++x)
                        if (countmat[x][y] < bbWidth) {
                            scanner[x] = 0;
                        } else {
                            scanner[x]++;
                            if (scanner[x] >= bbHeight) {
                                // annotate the index position using
                                // http://en.wikipedia.org/wiki/Reservoir_sampling
                                if (solcount == 0) {
                                    the_x_pos = x;
                                    the_y_pos = y;
                                } else {
                                    int d = RAND.nextInt(solcount);
                                    if (d == 0) {
                                        the_x_pos = x;
                                        the_y_pos = y;
                                    }
                                }
                                solcount++;
                            }
                        }
            }
            lasty++;
            firsty = lasty;
        }
        if (solcount > 0) {
            int posx = the_x_pos - bbWidth;
            return new Point(posx, the_y_pos);
        }
        return null;
    }

    private void dilateImage(int posx, int posy, int bbWidth, int bbHeight, int margin) {
        // using BFS, not cache friendly and could be improved
        if (margin > 0) {
            Queue<Point> thequeue = new ConcurrentLinkedQueue<>();
            int upY = Math.min(posy + bbHeight, maskImage.getHeight());
            int upX = Math.min(posx + bbWidth, maskImage.getWidth());
            for (int y = posy; y < upY; ++y)
                for (int x = posx; x < upX; ++x) {
                    if ((this.outputImage.getPixel(x, y) & 0xFF0000FF) > 0) {
                        this.outputImage.setPixel(x, y, (this.outputImage.getPixel(x, y) & 0xFFFFFF00) | margin + 1);
                        thequeue.add(new Point(x, y));
                    }
                }
            while (!thequeue.isEmpty()) {
                int x = thequeue.element().x;
                int y = thequeue.element().y;
                int blue = this.outputImage.getPixel(x, y) & 0xFF0000FF;
                int v = blue - 1;
                thequeue.remove();
                if (x > 0 && (this.outputImage.getPixel(x - 1, y) & 0xFF0000FF) == 0) {
                    this.outputImage.setPixel(x - 1, y, (this.outputImage.getPixel(x - 1, y) & 0xFFFFFF00) | v);
                    if (v > 0)
                        thequeue.add(new Point(x - 1, y));
                }
                if (x < maskImage.getWidth() - 1 && (this.outputImage.getPixel(x + 1, y) & 0xFF0000FF) == 0) {
                    this.outputImage.setPixel(x + 1, y, (this.outputImage.getPixel(x + 1, y) & 0xFFFFFF00) | v);
                    if (v > 0)
                        thequeue.add(new Point(x + 1, y));
                }
                if (y > 0 && (this.outputImage.getPixel(x, y - 1) & 0xFF0000FF) == 0) {
                    this.outputImage.setPixel(x, y - 1, (this.outputImage.getPixel(x, y - 1) & 0xFFFFFF00) | v);
                    if (v > 0)
                        thequeue.add(new Point(x, y - 1));
                }
                if (y < maskImage.getHeight() - 1 && (this.outputImage.getPixel(x, y + 1) & 0xFF0000FF) == 0) {
                    this.outputImage.setPixel(x, y + 1, (this.outputImage.getPixel(x, y + 1) & 0xFFFFFF00) | v);
                    if (v > 0)
                        thequeue.add(new Point(x, y + 1));
                }
            }
        }
    }

    private void freezeImage(int posx, int posy, int bbWidth, int bbHeight, int margin) {
        int lowerx = Math.max(0, posx - margin);
        int upperx = Math.min(maskImage.getWidth(), posx + bbWidth + margin);
        int lowery = Math.max(0, posy - bbHeight - margin);
        int uppery = Math.min(maskImage.getHeight(), posy + margin);

        for (int y = lowery; y < uppery; ++y) {
            int maxremoved = 0;
            for (int x = upperx - 1; x >= lowerx; --x) {
                // if ((this.outputImage.getRGB(x, y) & 0xFF0000FF) > 0) {
                // put pixel
                int other = countmat[x][y];
                int count = 0;
                for (int x2 = x; x2 < maskImage.getWidth() && countmat[x2][y] > 0; ++x2) {
                    other = countmat[x2][y];
                    countmat[x2][y] = count++;
                }
                if (other > maxremoved)
                    maxremoved = other;
                // this.outputImage.setRGB(x, y, 0xFF000000);
                // }
            }
            if (maxremoved >= maxwidthrow[y]) {
                maxwidthrow[y] = countmat[0][y];
                for (int x = 1; x < maskImage.getWidth(); ++x)
                    if (countmat[x][y] > maxwidthrow[y])
                        maxwidthrow[y] = countmat[x][y];
            }
        }
    }

    private Vector3 getMeanColor(int posx, int posy, int bbWidth, int bbHeight) {
        double mR = 0, mG = 0, mB = 0;
        for (int y = posy; y < posy + bbHeight; y++)
            for (int x = posx; x < posx + bbWidth; x++) {
                mR += (colorImage.getPixel(x, y) & 0xFF0000) >> 16;
                mG += (colorImage.getPixel(x, y) & 0x00FF00) >> 8;
                mB += (colorImage.getPixel(x, y) & 0x0000FF);
            }
        double count = 255.0 * bbWidth * bbHeight;
        return new Vector3(mR / count, mG / count, mB / count);
    }

    private Vector3 pickRandomColor() {
        return new Vector3((RAND.nextInt(155) + 100.0) / 255.0, (RAND.nextInt(155) + 100.0) / 255.0,
                (RAND.nextInt(155) + 100.0) / 255.0);
    }

    private boolean paintWord(Canvas graphics, String text, double initialFontSz) {
        int fontSize = (int) initialFontSz;
        boolean vertical = choose_vertical();
        while (fontSize >= mini_font_size) {
            Paint paint = new Paint();
            paint.setTextSize((float)initialFontSz);
            Rect extents = new Rect();
            paint.getTextBounds(text, 0, text.length(), extents);
            double bbWidth = extents.width();
            double bbH = extents.height();
            double eks_bearing = extents.left;
            double y_bearing = extents.top;

            int textposx, textposy, bbposx, bbposy;
            if (vertical) {
                double temp = bbH;
                bbH = bbWidth;
                bbWidth = temp;
                temp = eks_bearing;
                eks_bearing = y_bearing;
                y_bearing = temp;
            }
            // returns lower left corner
            Point p = findFreeRectangle((int) bbWidth, (int) bbH);
            if (p != null) {
                bbposx = p.x;
                bbposy = p.y;
                // adjust positions to be used by show_text method
                textposx = (int) (bbposx - eks_bearing);
                textposy = bbposy;
                if (vertical)
                    textposy += y_bearing;
                else
                    textposy -= bbH + y_bearing;

                Vector3 rgb;
                if (useColorSurface)
                    rgb = getMeanColor(bbposx, (int) (bbposy - bbH), (int) bbWidth, (int) bbH);
                else
                    rgb = pickRandomColor();

                graphics.save();
                graphics.translate(textposx, textposy);

                if (vertical)
                    graphics.rotate(-90);

                paint.setColor(0xFF000000 | (((int)(rgb.r * 255)) << 16) | (((int)(rgb.g * 255)) << 8) | ((int)(rgb.b * 255)));

                graphics.drawText(text, 0, 0, paint);

                // expects upper left corner
                dilateImage(bbposx, (int) (bbposy - (bbH - 1)), (int) bbWidth, (int) bbH, words_margin);

                // freeze updates the "RLSA like" matrix and erases the content
                freezeImage(bbposx, bbposy, (int) bbWidth, (int) bbH, words_margin);

                graphics.restore();
                return true;
            } else {
                fontSize -= font_step;
            }
        }
        return false;
    }

    private boolean choose_vertical() {
        return RAND.nextInt(100) < vertical_preference;
    }

    private static class Point {
        private final int x;
        private final int y;
        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Vector3 {
        private final double r;
        private final double g;
        private final double b;

        private Vector3(double r, double g, double b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
