package javagame;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;


public class Flashlight 
{

    public static BufferedImage draw(
            BufferedImage source,
            double x1, double y1, double x2, double y2,
            double beamWidth,
            Color color, Color color2) 
    {
        RenderingHints hints = new RenderingHints(
              RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        BufferedImage bi = new BufferedImage(
                source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bi.createGraphics();
        g.setRenderingHints(hints);

        g.drawImage(source, 0, 0, null);

        // Create a conical shape to constrain the beam
        double distance = Math.sqrt(Math.pow(x1 - x2, 2d) + Math.pow(y1 - y2, 2d));
        double tangent = (y2 - y1) / (x2 - x1);
        double theta = Math.atan(tangent);
        System.out.println(
                "distance: " + distance
                + "  tangent: " + tangent
                + "  theta: " + theta);
        double minTheta = theta + beamWidth / 2;
        double maxTheta = theta - beamWidth / 2;
        double xMin = x1 + distance * Math.cos(minTheta);
        double yMin = y1 + distance * Math.sin(minTheta);

        double xMax = x1 + distance * Math.cos(maxTheta);
        double yMax = y1 + distance * Math.sin(maxTheta);

        Polygon beam = new Polygon();
        beam.addPoint((int) x1, (int) y1);
        beam.addPoint((int) xMax, (int) yMax);
        beam.addPoint((int) xMin, (int) yMin);

        g.setColor(color);
        GradientPaint gp = new GradientPaint(
                (int)x1,(int)y1, color,
                (int)x2,(int)y2, color2);
        g.setClip(beam);
        g.setPaint(gp);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        // create an area the size of the image, but lacking the beam area
        Area darknessArea = new Area(new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        darknessArea.subtract(new Area(beam));
        g.setColor(color2);
        g.setClip(darknessArea);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        // fill in the beam edges with black (mostly to smooth lines)
        g.setClip(null);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(new Line2D.Double(x1,y1,xMin,yMin));
        g.draw(new Line2D.Double(x1,y1,xMax,yMax));

        g.dispose();

        return bi;
    }
}