package org.hortonmachine.style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class MainView extends JPanel
{
   JTextField _filepathField = new JTextField();
   JTree _rulesTree = new JTree();
   JButton _browseButton = new JButton();
   JButton _saveButton = new JButton();
   JPanel _stylePanelSpace = new JPanel();
   JPanel _stylePanel = new JPanel();
   JButton _previousButton = new JButton();
   JButton _nextButton = new JButton();
   JButton _allButton = new JButton();
   JPanel _mapPaneHolder = new JPanel();

   /**
    * Default constructor
    */
   public MainView()
   {
      initializePanel();
   }

   /**
    * Adds fill components to empty cells in the first row and first column of the grid.
    * This ensures that the grid spacing will be the same as shown in the designer.
    * @param cols an array of column indices in the first row where fill components should be added.
    * @param rows an array of row indices in the first column where fill components should be added.
    */
   void addFillComponents( Container panel, int[] cols, int[] rows )
   {
      Dimension filler = new Dimension(10,10);

      boolean filled_cell_11 = false;
      CellConstraints cc = new CellConstraints();
      if ( cols.length > 0 && rows.length > 0 )
      {
         if ( cols[0] == 1 && rows[0] == 1 )
         {
            /** add a rigid area  */
            panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
            filled_cell_11 = true;
         }
      }

      for( int index = 0; index < cols.length; index++ )
      {
         if ( cols[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
      }

      for( int index = 0; index < rows.length; index++ )
      {
         if ( rows[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(1,rows[index]) );
      }

   }

   /**
    * Helper method to load an image file from the CLASSPATH
    * @param imageName the package and name of the file to load relative to the CLASSPATH
    * @return an ImageIcon instance with the specified image file
    * @throws IllegalArgumentException if the image resource cannot be loaded.
    */
   public ImageIcon loadImage( String imageName )
   {
      try
      {
         ClassLoader classloader = getClass().getClassLoader();
         java.net.URL url = classloader.getResource( imageName );
         if ( url != null )
         {
            ImageIcon icon = new ImageIcon( url );
            return icon;
         }
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      throw new IllegalArgumentException( "Unable to load image: " + imageName );
   }

   /**
    * Method for recalculating the component orientation for 
    * right-to-left Locales.
    * @param orientation the component orientation to be applied
    */
   public void applyComponentOrientation( ComponentOrientation orientation )
   {
      // Not yet implemented...
      // I18NUtils.applyComponentOrientation(this, orientation);
      super.applyComponentOrientation(orientation);
   }

   public JPanel createPanel()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(0.5),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      _filepathField.setName("filepathField");
      jpanel1.add(_filepathField,cc.xywh(2,2,5,1));

      _rulesTree.setName("rulesTree");
      TitledBorder titledborder1 = new TitledBorder(null,"",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(33,33,33));
      _rulesTree.setBorder(titledborder1);
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(_rulesTree);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xywh(2,4,9,6));

      _browseButton.setActionCommand("...");
      _browseButton.setName("browseButton");
      _browseButton.setText("...");
      jpanel1.add(_browseButton,cc.xy(8,2));

      _saveButton.setActionCommand("save");
      _saveButton.setName("saveButton");
      _saveButton.setText("save");
      jpanel1.add(_saveButton,new CellConstraints(10,2,1,1,CellConstraints.FILL,CellConstraints.FILL));

      jpanel1.add(createstylePanelSpace(),cc.xywh(12,2,10,18));
      jpanel1.add(createPanel1(),cc.xywh(2,19,9,1));
      jpanel1.add(createmapPaneHolder(),cc.xywh(2,13,9,5));
      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 });
      return jpanel1;
   }

   public JPanel createstylePanelSpace()
   {
      _stylePanelSpace.setName("stylePanelSpace");
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0)","FILL:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      _stylePanelSpace.setLayout(formlayout1);

      _stylePanel.setName("stylePanel");
      _stylePanelSpace.add(_stylePanel,cc.xy(1,1));

      addFillComponents(_stylePanelSpace,new int[0],new int[0]);
      return _stylePanelSpace;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      _previousButton.setActionCommand("previous");
      _previousButton.setName("previousButton");
      _previousButton.setText("previous");
      jpanel1.add(_previousButton,new CellConstraints(1,1,1,1,CellConstraints.FILL,CellConstraints.FILL));

      _nextButton.setActionCommand("next");
      _nextButton.setName("nextButton");
      _nextButton.setText("next");
      jpanel1.add(_nextButton,new CellConstraints(5,1,1,1,CellConstraints.FILL,CellConstraints.FILL));

      _allButton.setActionCommand("all");
      _allButton.setName("allButton");
      _allButton.setText("all");
      jpanel1.add(_allButton,new CellConstraints(3,1,1,1,CellConstraints.FILL,CellConstraints.FILL));

      addFillComponents(jpanel1,new int[]{ 2,4 },new int[0]);
      return jpanel1;
   }

   public JPanel createmapPaneHolder()
   {
      _mapPaneHolder.setName("mapPaneHolder");
      EtchedBorder etchedborder1 = new EtchedBorder(EtchedBorder.LOWERED,null,null);
      _mapPaneHolder.setBorder(etchedborder1);
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      _mapPaneHolder.setLayout(formlayout1);

      addFillComponents(_mapPaneHolder,new int[]{ 1 },new int[]{ 1 });
      return _mapPaneHolder;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createPanel(), BorderLayout.CENTER);
   }


}
