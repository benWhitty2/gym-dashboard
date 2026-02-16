package uk.ac.sheffield.com1003.assignment2425.gui;

import uk.ac.sheffield.com1003.assignment2425.EntryCatalog;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractGymDashboardPanel;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractHistogram;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.AbstractHistogramPanel;
import uk.ac.sheffield.com1003.assignment2425.codeprovided.gui.HistogramBin;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

// This class provides some default code, but it needs to be completely replaced

public class HistogramPanel extends AbstractHistogramPanel {
    public HistogramPanel(AbstractGymDashboardPanel parentPanel, AbstractHistogram histogram) {
        super(parentPanel, histogram);
    }

    /**
     * this will paint out the histogram and its axis on to the panel
     *
     * @param g <code>Graphics</code> the Graphics object that the method will draw on
     */
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;
        AbstractHistogram histogram = getHistogram();

        List<HistogramBin> bins = histogram.getBinsInBoundaryOrder();
        int maxHeight = histogram.largestBinCount();
        int numBins = bins.size();
        int barWidth = (int) Math.round((double)(d.width -350)/numBins);

        for (int i = 0; i < numBins; i++) {
            //bars
            int height  =(int) (((double) histogram.getNumberOfEntriesInBin(bins.get(i))/maxHeight) * (d.height-20));
            Rectangle rect = new Rectangle(barWidth*i + 175,(d.height-20) - height,barWidth,height);

            g2.setColor(Color.blue);
            g2.fill(rect);
            g2.setColor(Color.black);
            g2.draw(rect);

            //x axis
            g2.drawString(Double.toString(EntryCatalog.roundTo2DP(bins.get(i).getLowerBoundary())),175 + (i * barWidth),d.height);
        }
        //x axis
        g2.drawString(Double.toString(EntryCatalog.roundTo2DP(bins.get(numBins-1).getUpperBoundary())),d.width-175,d.height);
        g2.drawLine(175,d.height-15,numBins * barWidth +175,d.height-15);

        //y axis
        g2.drawString(Integer.toString(maxHeight),150,10);
        g2.drawString(Integer.toString(maxHeight/2),150,d.height/2 -10);
        g2.drawString("0",150,d.height-20);
        g2.drawLine(165,0,165,d.height-20);


        //average line
        g2.setColor(Color.red);
        double average = ((histogram.getAveragePropertyValue() - histogram.getMinPropertyValue())/
                (histogram.getMaxPropertyValue()-histogram.getMinPropertyValue())) * barWidth * numBins;
        g2.drawLine((int) average+175,0,(int) average+175,d.height-20);

        //y axis label
        g2.setColor(Color.black);
        g2.rotate(Math.toRadians(-90));
        g2.drawString("frequency",-d.height/2 - 20,140);

    }
}
