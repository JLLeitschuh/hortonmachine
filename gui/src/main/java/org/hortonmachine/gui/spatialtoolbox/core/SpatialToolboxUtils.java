/*
 * This file is part of HortonMachine (http://www.hortonmachine.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * The HortonMachine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.hortonmachine.gui.spatialtoolbox.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

import org.hortonmachine.gears.libs.modules.HMConstants;

import oms3.Access;
import oms3.annotations.Description;
import oms3.annotations.Status;
import oms3.annotations.Unit;

/**
 * Utilities for the omsbox plugin.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class SpatialToolboxUtils {
    private static final String NO_DOCUMENTATION_AVAILABLE = "<b>NO DOCUMENTATION AVAILABLE</b>";
    private static final String STAGEHTMLDOCS = "stagehtmldocs";
    private static final String NEWLINE = "\n";
    private static final String HTMLNEWLINE = "<br>";
    private static final String DOCSSUFFIX = ".html";

    // /**
    // * Get the code from a {@link CoordinateReferenceSystem}.
    // *
    // * @param crs the crs to get the code from.
    // * @return the code, that can be used with {@link CRS#decode(String)}
    // * to recreate the crs.
    // * @throws Exception
    // */
    // public static String getCodeFromCrs( CoordinateReferenceSystem crs ) throws Exception {
    // String code = null;
    // try {
    // Integer epsg = CRS.lookupEpsgCode(crs, true);
    //            code = "EPSG:" + epsg; //$NON-NLS-1$
    // } catch (Exception e) {
    // // try non epsg
    // code = CRS.lookupIdentifier(crs, true);
    // }
    // return code;
    // }

    // public static ProcessingRegion gridGeometry2ProcessingRegion( GridGeometry2D gridGeometry ) {
    //
    // Envelope envelope = gridGeometry.getEnvelope2D();
    // DirectPosition lowerCorner = envelope.getLowerCorner();
    // double[] westSouth = lowerCorner.getCoordinate();
    // DirectPosition upperCorner = envelope.getUpperCorner();
    // double[] eastNorth = upperCorner.getCoordinate();
    //
    // AffineTransform gridToCRS = (AffineTransform) gridGeometry.getGridToCRS();
    // double xRes = XAffineTransform.getScaleX0(gridToCRS);
    // double yRes = XAffineTransform.getScaleY0(gridToCRS);
    //
    // ProcessingRegion region = new ProcessingRegion(westSouth[0], eastNorth[0], westSouth[1],
    // eastNorth[1], xRes, yRes);
    // return region;
    // }

    public static boolean isFieldExceptional( FieldData inputData ) {
        if (inputData.guiHints != null && inputData.guiHints.equals(HMConstants.FILESPATHLIST_UI_HINT)) {
            return true;
        }
        return false;
    }

//    /**
//     * Get the path for a module doc html.
//     * 
//     * @param moduleClassName the name of the module to get the path for.
//     * @return the path to the html file.
//     * @throws Exception
//     */
//    public static String getModuleDocumentationPath( String moduleClassName ) throws Exception {
//        File configurationsFolder = SpatialToolboxSessionPluginSingleton.getInstance().getConfigurationsFolder();
//        File htmlDocsFolder = new File(configurationsFolder, STAGEHTMLDOCS);
//        File htmlDocs = new File(htmlDocsFolder, moduleClassName + ".html");
//        if (!htmlDocs.exists()) {
//            htmlDocs = new File(htmlDocsFolder, "Oms" + moduleClassName + ".html");
//            if (!htmlDocs.exists()) {
//                return NO_DOCUMENTATION_AVAILABLE;
//            }
//        }
//        return htmlDocs.getAbsolutePath();
//    }

//    /**
//     * Porge the html docs folder.
//     * 
//     * @throws Exception
//     */
//    public static void cleanModuleDocumentation() throws Exception {
//        File configurationsFolder = SpatialToolboxSessionPluginSingleton.getInstance().getConfigurationsFolder();
//        File htmlDocsFolder = new File(configurationsFolder, STAGEHTMLDOCS);
//        if (htmlDocsFolder.exists()) {
//            FileUtilities.deleteFileOrDir(htmlDocsFolder);
//        }
//    }
//
//    /**
//     * Generate the module documentation in the configuration area.
//     * 
//     * @param moduleClassName the class for which to generate the doc.
//     * @throws Exception
//     */
//    @SuppressWarnings("nls")
//    public static void generateModuleDocumentation( String moduleClassName ) throws Exception {
//
//        Class< ? > moduleClass = StageModulesManager.getInstance().getModulesClass(moduleClassName);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><body>\n");
//
//        // modules documentation
//        Documentation documentation = moduleClass.getAnnotation(Documentation.class);
//        String documentationStr = null;
//        if (documentation != null) {
//            documentationStr = AnnotationUtilities.getLocalizedDocumentation(documentation);
//            if (documentationStr.length() == 0) {
//                documentationStr = null;
//            } else if (documentationStr.equals(" - ")) {
//                documentationStr = null;
//            }
//        }
//        if (documentation != null && documentationStr != null) {
//            if (documentationStr.endsWith(DOCSSUFFIX)) {
//                // have to get the file
//                String modulePackage = moduleClassName.substring(0, moduleClassName.lastIndexOf('.'));
//                String path = modulePackage.replaceAll("\\.", "/") + "/" + documentationStr;
//                InputStream inStream = StageModulesManager.getInstance().getResourceAsStream(path);
//                // InputStream inStream = moduleClass.getResourceAsStream(documentationStr);
//                if (inStream != null) {
//                    BufferedReader br = null;
//                    try {
//                        br = new BufferedReader(new InputStreamReader(inStream));
//                        StringBuilder tmpSb = new StringBuilder();
//                        String line = "";
//                        while( (line = br.readLine()) != null ) {
//                            tmpSb.append(line).append(NEWLINE);
//                        }
//                        documentationStr = tmpSb.toString();
//                    } finally {
//                        if (br != null)
//                            br.close();
//                    }
//                }
//            }
//            sb.append("<h2>Description</h2>").append(NEWLINE);
//            sb.append(NEWLINE);
//            sb.append("<blockquote>");
//            sb.append(documentationStr);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//            sb.append(NEWLINE);
//        } else {
//            // try with module description
//            Description description = moduleClass.getAnnotation(Description.class);
//            String descriptionStr = AnnotationUtilities.getLocalizedDescription(description);
//            if (description != null) {
//                sb.append("<h2>Description</h2>").append(NEWLINE);
//                sb.append(NEWLINE);
//                sb.append("<blockquote>");
//                sb.append(descriptionStr);
//                sb.append("</blockquote>");
//                sb.append(NEWLINE);
//                sb.append(NEWLINE);
//            }
//        }
//        // general info
//        sb.append("<h2>General Information</h2>").append(NEWLINE);
//        sb.append(NEWLINE);
//        // general info: status
//        Status status = moduleClass.getAnnotation(Status.class);
//        if (status != null) {
//            sb.append("<blockquote>");
//            sb.append("Module status: " + getStatusString(status.value())).append(NEWLINE);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//        }
//
//        // general info: script name
//        Name name = moduleClass.getAnnotation(Name.class);
//        String nameStr = AnnotationUtilities.getLocalizedName(name);
//        if (name != null) {
//            sb.append("<blockquote>");
//            sb.append(" Name to use in a script: <b>" + nameStr + "</b>").append(NEWLINE);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//        }
//        // general info: authors
//        Author author = moduleClass.getAnnotation(Author.class);
//        if (author != null) {
//            String authorNameStr = author.name();
//            String[] authorNameSplit = authorNameStr.split(",");
//
//            String authorContactStr = author.contact();
//            String[] authorContactSplit = authorContactStr.split(",");
//
//            sb.append("<blockquote>");
//            sb.append(" Authors ").append(NEWLINE);
//            sb.append(HTMLNEWLINE);
//            sb.append("<ul>").append(NEWLINE);
//            for( String authorName : authorNameSplit ) {
//                sb.append("<li>").append(authorName.trim());
//            }
//            sb.append("</li>").append(NEWLINE);
//            sb.append("</ul>").append(NEWLINE);
//            sb.append(NEWLINE);
//            sb.append(HTMLNEWLINE);
//            sb.append(HTMLNEWLINE);
//            // if (authorContactStr.startsWith("http")) {
//            // authorContactStr = "<a href=\"" + authorContactStr + "\">" + authorContactStr +
//            // "</a>";
//            // }
//            sb.append(" Contacts: ").append(NEWLINE);
//            sb.append(HTMLNEWLINE);
//            sb.append("<ul>").append(NEWLINE);
//            for( String authorContact : authorContactSplit ) {
//                sb.append("<li>").append(authorContact.trim());
//            }
//            sb.append("</li>").append(NEWLINE);
//            sb.append("</ul>").append(NEWLINE);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//        }
//        // general info: license
//        License license = moduleClass.getAnnotation(License.class);
//        if (license != null) {
//            String licenseStr = AnnotationUtilities.getLocalizedLicense(license);
//            sb.append("<blockquote>");
//            sb.append(" License: " + licenseStr).append(NEWLINE);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//        }
//        // general info: keywords
//        Keywords keywords = moduleClass.getAnnotation(Keywords.class);
//        if (keywords != null) {
//            String keywordsStr = AnnotationUtilities.getLocalizedKeywords(keywords);
//            sb.append("<blockquote>");
//            sb.append(" Keywords: " + keywordsStr).append(NEWLINE);
//            sb.append("</blockquote>");
//            sb.append(NEWLINE);
//        }
//        sb.append(NEWLINE);
//
//        // gather input fields
//        Object annotatedObject = moduleClass.newInstance();
//        ComponentAccess cA = new ComponentAccess(annotatedObject);
//
//        // parameters
//        sb.append("<h2>Parameters</h2>").append(NEWLINE);
//        sb.append(NEWLINE);
//        sb.append("<blockquote>");
//        // parameters: fields
//        Collection<Access> inputs = cA.inputs();
//        StringBuilder sbTmp = new StringBuilder();
//        collectParameters(sbTmp, inputs);
//        toTable(sb, sbTmp, "Input parameters");
//        sb.append(NEWLINE);
//        Collection<Access> outputs = cA.outputs();
//        sbTmp = new StringBuilder();
//        collectParameters(sbTmp, outputs);
//        toTable(sb, sbTmp, "Output parameters");
//        sb.append("</blockquote>");
//        sb.append(NEWLINE);
//        sb.append(NEWLINE);
//
//        sb.append("</body></html>");
//
//        File configurationsFolder = SpatialToolboxSessionPluginSingleton.getInstance().getConfigurationsFolder();
//        File htmlDocsFolder = new File(configurationsFolder, STAGEHTMLDOCS);
//        if (!htmlDocsFolder.exists()) {
//            if (!htmlDocsFolder.mkdir()) {
//                throw new RuntimeException();
//            }
//        }
//
//        File htmlDocs = new File(htmlDocsFolder, moduleClassName + ".html");
//        FileUtilities.writeFile(sb.toString(), htmlDocs);
//    }

    private static void collectParameters( StringBuilder sbTmp, Collection<Access> accessList ) throws Exception {
        for( Access access : accessList ) {
            Field field = access.getField();
            String fieldName = field.getName();
            Description descriptionAnnot = field.getAnnotation(Description.class);

            if (fieldName.equals("pm")) {
                // ignore progress monitor
                continue;
            }
            String fieldDescription = " - ";
            if (descriptionAnnot != null) {
                fieldDescription = AnnotationUtilities.getLocalizedDescription(descriptionAnnot);
                if (fieldDescription == null) {
                    fieldDescription = " - ";
                }

                Unit unitAnn = field.getAnnotation(Unit.class);
                if (unitAnn != null) {
                    fieldDescription = fieldDescription + " [" + unitAnn.value() + "]";
                }
            }

            sbTmp.append("<tr>").append(NEWLINE);
            sbTmp.append("<td width=\"40%\"> <b>").append(fieldName).append("</b> </td><td width=\"60%\"> ");
            sbTmp.append(fieldDescription).append(" </td>").append(NEWLINE);
            sbTmp.append("</tr>").append(NEWLINE);
        }
    }

    private static void toTable( StringBuilder sbToAppendTo, StringBuilder tableContentSb, String tableTitle ) {
        if (tableContentSb.length() > 0) {
            sbToAppendTo.append("<h3>" + tableTitle + "</h3>").append(NEWLINE);
            sbToAppendTo.append("<table width=\"100%\" border=\"1\" cellpadding=\"10\">").append(NEWLINE);
            sbToAppendTo.append(tableContentSb);
            sbToAppendTo.append("</table>").append(NEWLINE);
        }
    }

    private static String getStatusString( int statusValue ) {
        switch( statusValue ) {
        case Status.CERTIFIED:
            return "CERTIFIED";
        case Status.DRAFT:
            return "DRAFT";
        case Status.EXPERIMENTAL:
            return "EXPERIMENTAL";
        case Status.TESTED:
            return "TESTED";
        case Status.VALIDATED:
            return "VALIDATED";
        }
        return "DRAFT";
    }

    /**
     * Checks if the given path is a GRASS raster file.
     * 
     * <p>Note that there is no check on the existence of the file.
     * 
     * @param path the path to check.
     * @return true if the file is a grass raster.
     */
    public static boolean isGrass( String path ) {
        if (path == null)
            return false;
        File file = new File(path);
        File cellFolderFile = file.getParentFile();
        File mapsetFile = cellFolderFile.getParentFile();
        File windFile = new File(mapsetFile, "WIND");
        return cellFolderFile.getName().toLowerCase().equals("cell") && windFile.exists();
    }

}
