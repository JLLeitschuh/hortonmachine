/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package eu.hydrologis.jgrass.jgrassgears.modules.v.sourcesdirection;

import static java.lang.Double.NaN;
import static java.lang.Math.sqrt;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.License;
import oms3.annotations.Out;
import oms3.annotations.Status;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.Operations;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import eu.hydrologis.jgrass.jgrassgears.libs.modules.HMModel;
import eu.hydrologis.jgrass.jgrassgears.libs.monitor.DummyProgressMonitor;
import eu.hydrologis.jgrass.jgrassgears.libs.monitor.IHMProgressMonitor;
import eu.hydrologis.jgrass.jgrassgears.utils.features.FeatureExtender;
import eu.hydrologis.jgrass.jgrassgears.utils.geometry.GeometryUtilities;
import eu.hydrologis.jgrass.jgrassgears.utils.sorting.QuickSortAlgorithmObjects;

@Description("Calculates the direction of maximum slope for a source point on a dem.")
@Author(name = "Andrea Antonello", contact = "www.hydrologis.com")
@Keywords("Raster, Vector")
@Status(Status.DRAFT)
@License("http://www.gnu.org/licenses/gpl-3.0.html")
public class SourcesDirectionCalculator extends HMModel {

    @Description("The source point features.")
    @In
    public FeatureCollection<SimpleFeatureType, SimpleFeature> inSources;

    @Description("Resolution to use.")
    @In
    public double pRes = NaN;

    @Description("The progress monitor.")
    @In
    public IHMProgressMonitor pm = new DummyProgressMonitor();

    @Description("The list of gridcoverages to use.")
    @In
    public List<GridCoverage2D> inDems;

    @Description("The source point features with the added azimuth angle.")
    @Out
    public FeatureCollection<SimpleFeatureType, SimpleFeature> outSources;

    private GeometryFactory gF = GeometryUtilities.gf();

    @Execute
    public void process() throws Exception {
        if (!concatOr(outSources == null, doReset)) {
            return;
        }

        HashMap<Envelope2D, GridCoverage2D> envelope2Coverage = new HashMap<Envelope2D, GridCoverage2D>();
        for( GridCoverage2D dem : inDems ) {
            Envelope2D envelope2d = dem.getEnvelope2D();
            envelope2Coverage.put(envelope2d, dem);
        }

        FeatureIterator<SimpleFeature> inFeatureIterator = inSources.features();

        outSources = FeatureCollections.newCollection();

        FeatureExtender fExt = new FeatureExtender(inSources.getSchema(), new String[]{"azimuth",
                "availpixels", "c11", "c12", "c13", "c21", "c22", "c23", "c31", "c32", "c33"},
                new Class< ? >[]{Double.class, Integer.class, Double.class, Double.class,
                        Double.class, Double.class, Double.class, Double.class, Double.class,
                        Double.class, Double.class});

        int id = 0;
        int size = inSources.size();
        pm.beginTask("Extracting azimuth...", size);
        while( inFeatureIterator.hasNext() ) {
            SimpleFeature feature = inFeatureIterator.next();

            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();

            Set<Entry<Envelope2D, GridCoverage2D>> env2covEntries = envelope2Coverage.entrySet();
            for( Entry<Envelope2D, GridCoverage2D> entry : env2covEntries ) {
                Envelope2D env = entry.getKey();
                if (env.contains(coordinate.x, coordinate.y)) {
                    // source is in this dem, process it
                    GridCoverage2D dem = entry.getValue();
                    double[] res = resFromCoverage(dem);
                    if (res[0] != pRes) {
                        dem = (GridCoverage2D) Operations.DEFAULT.subsampleAverage(dem, res[0]
                                / pRes, res[0] / pRes);
                    }

                    GridGeometry2D gridGeometry = dem.getGridGeometry();
                    GridEnvelope2D gridRange = gridGeometry.getGridRange2D();
                    int cols = gridRange.width;
                    int rows = gridRange.height;

                    GridCoordinates2D centerGC = gridGeometry.worldToGrid(new DirectPosition2D(
                            coordinate.x, coordinate.y));

                    /*
                     * c11 | c12 | c13
                     * ---------------
                     * c21 | cen | c23
                     * ---------------
                     * c31 | c32 | c33
                     * 
                     * where c23 = row 2, col 3
                     */
                    GridCoordinates2D c11 = new GridCoordinates2D(centerGC.x - 1, centerGC.y - 1);
                    GridCoordinates2D c12 = new GridCoordinates2D(centerGC.x, centerGC.y - 1);
                    GridCoordinates2D c13 = new GridCoordinates2D(centerGC.x + 1, centerGC.y - 1);

                    GridCoordinates2D c21 = new GridCoordinates2D(centerGC.x - 1, centerGC.y);
                    GridCoordinates2D c23 = new GridCoordinates2D(centerGC.x + 1, centerGC.y);

                    GridCoordinates2D c31 = new GridCoordinates2D(centerGC.x - 1, centerGC.y + 1);
                    GridCoordinates2D c32 = new GridCoordinates2D(centerGC.x, centerGC.y + 1);
                    GridCoordinates2D c33 = new GridCoordinates2D(centerGC.x + 1, centerGC.y + 1);

                    double[] center = dem.evaluate((GridCoordinates2D) centerGC, (double[]) null);
                    double dz11 = -10000;
                    double dz12 = -10000;
                    double dz13 = -10000;
                    double dz21 = -10000;
                    double dz23 = -10000;
                    double dz31 = -10000;
                    double dz32 = -10000;
                    double dz33 = -10000;

                    int pixelNum = 0;
                    double[] v11 = getPixelValue(dem, cols, rows, c11);
                    if (v11 != null) {
                        pixelNum++;
                        dz11 = (center[0] - v11[0]) / sqrt(2);
                    }
                    double[] v12 = getPixelValue(dem, cols, rows, c12);
                    if (v12 != null) {
                        pixelNum++;
                        dz12 = (center[0] - v12[0]);
                    }
                    double[] v13 = getPixelValue(dem, cols, rows, c13);
                    if (v13 != null) {
                        pixelNum++;
                        dz13 = (center[0] - v13[0]) / sqrt(2);
                    }
                    double[] v21 = getPixelValue(dem, cols, rows, c21);
                    if (v21 != null) {
                        pixelNum++;
                        dz21 = (center[0] - v21[0]);
                    }
                    double[] v23 = getPixelValue(dem, cols, rows, c23);
                    if (v23 != null) {
                        pixelNum++;
                        dz23 = (center[0] - v23[0]);
                    }
                    double[] v31 = getPixelValue(dem, cols, rows, c31);
                    if (v31 != null) {
                        pixelNum++;
                        dz31 = (center[0] - v31[0]) / sqrt(2);
                    }
                    double[] v32 = getPixelValue(dem, cols, rows, c32);
                    if (v32 != null) {
                        pixelNum++;
                        dz32 = (center[0] - v32[0]);
                    }
                    double[] v33 = getPixelValue(dem, cols, rows, c33);
                    if (v33 != null) {
                        pixelNum++;
                        dz33 = (center[0] - v33[0]) / sqrt(2);
                    }

                    GridCoordinates2D[] cArray = new GridCoordinates2D[]{c31, c32, c33, c21, c23,
                            c11, c12, c13};
                    double[] tArray = new double[]{dz31, dz32, dz33, dz21, dz23, dz11, dz12, dz13};

                    QuickSortAlgorithmObjects qSobj = new QuickSortAlgorithmObjects(null);
                    qSobj.sort(tArray, cArray);

                    GridCoordinates2D steepestCoord = cArray[cArray.length - 1];

                    DirectPosition steepestWorldCoord = gridGeometry.gridToWorld(steepestCoord);
                    double[] c = steepestWorldCoord.getCoordinate();
                    DirectPosition centerCoordOnGrid = gridGeometry.gridToWorld(centerGC);
                    double[] cent = centerCoordOnGrid.getCoordinate();
                    double azimuth = GeometryUtilities.azimuth(new Coordinate(cent[0], cent[1]),
                            new Coordinate(c[0], c[1]));

                    SimpleFeature azimuthFeature = fExt.extendFeature(feature, new Object[]{
                            azimuth, pixelNum, getValue(v11), getValue(v12), getValue(v13),
                            getValue(v21), getValue(center), getValue(v23), getValue(v31),
                            getValue(v32), getValue(v33)}, id++);
                    outSources.add(azimuthFeature);
                }
            }
            pm.worked(1);
        }
        pm.done();

    }

    private double getValue( double[] array ) {
        return array != null ? array[0] : -9999.0;
    }

    private double[] getPixelValue( GridCoverage2D dem, int cols, int rows,
            GridCoordinates2D gridCoordinate ) {
        if (gridCoordinate.x >= 0 && gridCoordinate.x < cols && gridCoordinate.y >= 0
                && gridCoordinate.y < rows) {
            double[] value = dem.evaluate((GridCoordinates2D) gridCoordinate, (double[]) null);
            return value;
        }
        return null;
    }

    private double[] resFromCoverage( GridCoverage2D dem ) {
        GridGeometry2D gridGeometry = dem.getGridGeometry();
        AffineTransform gridToCRS = (AffineTransform) gridGeometry.getGridToCRS();
        double[] res = new double[]{XAffineTransform.getScaleX0(gridToCRS),
                XAffineTransform.getScaleY0(gridToCRS)};
        return res;
    }
}
