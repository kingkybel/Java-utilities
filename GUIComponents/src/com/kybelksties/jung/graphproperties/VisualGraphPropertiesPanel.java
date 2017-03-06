/*
 * Copyright (C) 2015 Dieter J Kybelksties
 *
 * This program is free software: you can redistribute it and/or modify
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
 *
 * @date: 2015-12-14
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.jung.graphproperties;

import com.kybelksties.gui.CapJoinComboBox;
import com.kybelksties.jung.DirectedAcyclicGraph;
import com.kybelksties.jung.GraphTypeChoices;
import com.kybelksties.jung.LayoutChoices;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * A GUI panel for customizing visual properties of a (Jung-) graph.
 *
 * @author Dieter J Kybelksties
 */
public final class VisualGraphPropertiesPanel extends javax.swing.JPanel
{

    private static final Class CLAZZ = VisualGraphPropertiesPanel.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private VisualGraphProperties vgp = new VisualGraphProperties();

    /**
     * Creates new form VisualGraphPropertiesPanel.
     */
    public VisualGraphPropertiesPanel()
    {
        this(new VisualGraphProperties());
    }

    /**
     * Creates new form VisualGraphPropertiesPanel initialising values from the
     * given graphProperties object.
     *
     * @param graphProperties the given graph-properties
     */
    public VisualGraphPropertiesPanel(VisualGraphProperties graphProperties)
    {
        initComponents();
        setVisualGraphProperties(graphProperties);

        vertexGradientColorButton.setVisible(gradientCheckBox.isSelected());
        vertexPickedGradientColorButton.setVisible(
                gradientCheckBox.isSelected());
        edgeGradientColorButton.setVisible(gradientEdgeCheckBox.isSelected());
        edgePickedGradientColorButton.setVisible(
                gradientEdgeCheckBox.isSelected());

        displayPreview();
    }

    /**
     * Show a dialog with this component.
     *
     * @param window parent
     * @return the newly configured visual graph properties
     */
    static public VisualGraphProperties showDialog(Window window)
    {
        final VisualGraphPropertiesPanel vgpp = new VisualGraphPropertiesPanel();

        final JDialog dlg = new JDialog(window,
                                        Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setTitle("Graph Properties");
        dlg.getContentPane().setLayout(new AbsoluteLayout());
        Dimension vgppDim = vgpp.getPreferredSize();
        dlg.getContentPane().setSize(vgppDim);
        AbsoluteConstraints constraints =
                            new AbsoluteConstraints(2,
                                                    2,
                                                    vgppDim.width,
                                                    vgppDim.height);
        dlg.add(vgpp, constraints);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                dlg.setVisible(false);
            }
        });

        // where to put the OK button
        Dimension okDim = okButton.getPreferredSize();
        constraints = new AbsoluteConstraints(vgppDim.width - okDim.width * 2,
                                              vgppDim.height + 4,
                                              okDim.width,
                                              okDim.height);
        dlg.add(okButton, constraints);

        Dimension dialogDim = new Dimension(vgppDim.width + 4,
                                            vgppDim.height + okDim.height + 40);
        dlg.setMinimumSize(dialogDim);
        dlg.setMaximumSize(dialogDim);
        dlg.setSize(dialogDim);
        Point p1 = window.getLocation();
        Dimension d1 = window.getSize();
        Dimension d2 = dlg.getSize();

        int x = p1.x + (d1.width - d2.width) / 2;
        int y = p1.y + (d1.height - d2.height) / 2;

        if (x < 0)
        {
            x = 0;
        }
        if (y < 0)
        {
            y = 0;
        }

        dlg.setLocation(x, y);

        dlg.setVisible(true);

        return vgpp.getVisualGraphProperties();

    }

    VisualGraphProperties getVisualGraphProperties()
    {
        return vgp;
    }

    void setVisualGraphProperties(VisualGraphProperties visualGraphProperties)
    {
        vgp = visualGraphProperties;
    }

    Graph<String, Integer> graph;

    /**
     * Creates new form GraphPanelTestFrame.
     */
    public void displayPreview()
    {
        createGraph();
        vgp.setGraphLayoutType(LayoutChoices.HALFORDER);
//        previewPanel.display(graph);

    }

    void createGraph()
    {
        graph = new DirectedAcyclicGraph<>();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");

        graph.addEdge(1, "A", "B");
        graph.addEdge(2, "A", "C");
        graph.addEdge(3, "A", "D");
        graph.addEdge(4, "B", "C");
        graph.addEdge(5, "B", "F");
        graph.addEdge(5, "E", "F");
        graph.addEdge(6, "C", "G");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        mainSplitPane = new javax.swing.JSplitPane();
        mainTabbedPane = new javax.swing.JTabbedPane();
        vertexPanel = new javax.swing.JPanel();
        vertexStringLabellerCheckBox = new javax.swing.JCheckBox();
        gradientCheckBox = new javax.swing.JCheckBox();
        outlineColorButton = new javax.swing.JButton();
        fillColorButton = new javax.swing.JButton();
        pickedOutlineColorButton = new javax.swing.JButton();
        pickedFillColorButton = new javax.swing.JButton();
        vertexPickedGradientColorButton = new javax.swing.JButton();
        vertexGradientColorButton = new javax.swing.JButton();
        vertexFontChooser = new com.kybelksties.gui.FontChooser();
        shapeComboBox = new com.kybelksties.gui.ShapeComboBox();
        edgePanel = new javax.swing.JPanel();
        edgeStringLabellerCheckBox = new javax.swing.JCheckBox();
        showEndPointCheckBox = new javax.swing.JCheckBox();
        edgeWidthSpinner = new javax.swing.JSpinner();
        edgeWidthLabel = new javax.swing.JLabel();
        edgePickedGradientColorButton = new javax.swing.JButton();
        edgeGradientColorButton = new javax.swing.JButton();
        gradientEdgeCheckBox = new javax.swing.JCheckBox();
        edgeColorButton = new javax.swing.JButton();
        pickedEdgeColorButton = new javax.swing.JButton();
        edgeFontChooser = new com.kybelksties.gui.FontChooser();
        edgePatternComboBox = new com.kybelksties.gui.LinePatternComboBox();
        capJoinComboBox = new com.kybelksties.gui.CapJoinComboBox();
        edgeShapeComboBox1 = new com.kybelksties.jung.EdgeShapeComboBox();
        mousePanel = new javax.swing.JPanel();
        editMouseCheckBox = new javax.swing.JCheckBox();
        zoomMouseCheckBox = new javax.swing.JCheckBox();
        pickMouseCheckBox = new javax.swing.JCheckBox();
        graphPanel = new javax.swing.JPanel();
        graphTypeComboBox = new javax.swing.JComboBox();
        layoutComboBox = new javax.swing.JComboBox();
        graphTypeLabel = new javax.swing.JLabel();
        layoutTypeLabel = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        vertexPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        vertexStringLabellerCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.vertexStringLabellerCheckBox.text")); // NOI18N
        vertexStringLabellerCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                vertexStringLabellerCheckBoxActionPerformed(evt);
            }
        });
        vertexPanel.add(vertexStringLabellerCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        gradientCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.gradientCheckBox.text")); // NOI18N
        gradientCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                gradientCheckBoxActionPerformed(evt);
            }
        });
        vertexPanel.add(gradientCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        outlineColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.outlineColorButton.text")); // NOI18N
        outlineColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                outlineColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(outlineColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, -1, -1));

        fillColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.fillColorButton.text")); // NOI18N
        fillColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fillColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(fillColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        pickedOutlineColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.pickedOutlineColorButton.text")); // NOI18N
        pickedOutlineColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pickedOutlineColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(pickedOutlineColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, -1, -1));

        pickedFillColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.pickedFillColorButton.text")); // NOI18N
        pickedFillColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pickedFillColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(pickedFillColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        vertexPickedGradientColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.vertexPickedGradientColorButton.text")); // NOI18N
        vertexPickedGradientColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                vertexPickedGradientColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(vertexPickedGradientColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, -1, -1));

        vertexGradientColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.vertexGradientColorButton.text")); // NOI18N
        vertexGradientColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                vertexGradientColorButtonActionPerformed(evt);
            }
        });
        vertexPanel.add(vertexGradientColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));
        vertexPanel.add(vertexFontChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
        vertexPanel.add(shapeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.vertexPanel.TabConstraints.tabTitle"), vertexPanel); // NOI18N

        edgePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        edgeStringLabellerCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgeStringLabellerCheckBox.text")); // NOI18N
        edgeStringLabellerCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                edgeStringLabellerCheckBoxActionPerformed(evt);
            }
        });
        edgePanel.add(edgeStringLabellerCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        showEndPointCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.showEndPointCheckBox.text")); // NOI18N
        edgePanel.add(showEndPointCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        edgeWidthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        edgeWidthSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                edgeWidthSpinnerPropertyChange(evt);
            }
        });
        edgePanel.add(edgeWidthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 50, 20));

        edgeWidthLabel.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgeWidthLabel.text")); // NOI18N
        edgePanel.add(edgeWidthLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        edgePickedGradientColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgePickedGradientColorButton.text")); // NOI18N
        edgePickedGradientColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                edgePickedGradientColorButtonActionPerformed(evt);
            }
        });
        edgePanel.add(edgePickedGradientColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, -1, -1));

        edgeGradientColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgeGradientColorButton.text")); // NOI18N
        edgeGradientColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                edgeGradientColorButtonActionPerformed(evt);
            }
        });
        edgePanel.add(edgeGradientColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, -1, -1));

        gradientEdgeCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.gradientEdgeCheckBox.text")); // NOI18N
        gradientEdgeCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                gradientEdgeCheckBoxActionPerformed(evt);
            }
        });
        edgePanel.add(gradientEdgeCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        edgeColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgeColorButton.text")); // NOI18N
        edgeColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                edgeColorButtonActionPerformed(evt);
            }
        });
        edgePanel.add(edgeColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        pickedEdgeColorButton.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.pickedEdgeColorButton.text")); // NOI18N
        pickedEdgeColorButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pickedEdgeColorButtonActionPerformed(evt);
            }
        });
        edgePanel.add(pickedEdgeColorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));
        edgePanel.add(edgeFontChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
        edgePanel.add(edgePatternComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 290, -1));
        edgePanel.add(capJoinComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));
        edgePanel.add(edgeShapeComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, -1));

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.edgePanel.TabConstraints.tabTitle"), edgePanel); // NOI18N

        mousePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        editMouseCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.editMouseCheckBox.text")); // NOI18N
        editMouseCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editMouseCheckBoxActionPerformed(evt);
            }
        });
        mousePanel.add(editMouseCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        zoomMouseCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.zoomMouseCheckBox.text")); // NOI18N
        mousePanel.add(zoomMouseCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        pickMouseCheckBox.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.pickMouseCheckBox.text")); // NOI18N
        mousePanel.add(pickMouseCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.mousePanel.TabConstraints.tabTitle"), mousePanel); // NOI18N

        graphPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        graphTypeComboBox.setModel(new DefaultComboBoxModel(GraphTypeChoices.values()));
        graphTypeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                graphTypeComboBoxActionPerformed(evt);
            }
        });
        graphPanel.add(graphTypeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 190, -1));

        layoutComboBox.setModel(new DefaultComboBoxModel(LayoutChoices.values()));
        layoutComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                layoutComboBoxActionPerformed(evt);
            }
        });
        graphPanel.add(layoutComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 190, -1));

        graphTypeLabel.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.graphTypeLabel.text")); // NOI18N
        graphPanel.add(graphTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        layoutTypeLabel.setText(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.layoutTypeLabel.text")); // NOI18N
        graphPanel.add(layoutTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, -1));

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(VisualGraphPropertiesPanel.class, "VisualGraphPropertiesPanel.graphPanel.TabConstraints.tabTitle"), graphPanel); // NOI18N

        mainSplitPane.setLeftComponent(mainTabbedPane);

        add(mainSplitPane);
    }// </editor-fold>//GEN-END:initComponents

    private void edgePickedGradientColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgePickedGradientColorButtonActionPerformed
    {//GEN-HEADEREND:event_edgePickedGradientColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked edge color",
                                               vgp.getPickedEdgeColor());
        vgp.setPickedEdgeColor(color);
        displayPreview();
    }//GEN-LAST:event_edgePickedGradientColorButtonActionPerformed

    private void editMouseCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editMouseCheckBoxActionPerformed
    {//GEN-HEADEREND:event_editMouseCheckBoxActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked edge color",
                                               vgp.getPickedEdgeColor());
        vgp.setPickedEdgeColor(color);
        displayPreview();
    }//GEN-LAST:event_editMouseCheckBoxActionPerformed

    private void fillColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fillColorButtonActionPerformed
    {//GEN-HEADEREND:event_fillColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Vertex fill color",
                                               vgp.getVertexFillColor());
        vgp.setVertexFillColor(color);
        displayPreview();
    }//GEN-LAST:event_fillColorButtonActionPerformed

    private void outlineColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_outlineColorButtonActionPerformed
    {//GEN-HEADEREND:event_outlineColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Vertex outline color",
                                               vgp.
                                               getVertexOutlineColor());
        vgp.setVertexOutlineColor(color);
        displayPreview();
    }//GEN-LAST:event_outlineColorButtonActionPerformed

    private void pickedFillColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pickedFillColorButtonActionPerformed
    {//GEN-HEADEREND:event_pickedFillColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked vertex fill color",
                                               vgp.getPickedVertexFillColor());
        vgp.setPickedVertexFillColor(color);
        displayPreview();
    }//GEN-LAST:event_pickedFillColorButtonActionPerformed

    private void pickedOutlineColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pickedOutlineColorButtonActionPerformed
    {//GEN-HEADEREND:event_pickedOutlineColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked vertex outline color",
                                               vgp.getPickedVertexOutlineColor());
        vgp.setPickedVertexOutlineColor(color);
        displayPreview();
    }//GEN-LAST:event_pickedOutlineColorButtonActionPerformed

    private void vertexStringLabellerCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vertexStringLabellerCheckBoxActionPerformed
    {//GEN-HEADEREND:event_vertexStringLabellerCheckBoxActionPerformed
        vgp.setHasVertexStringLabeller(
                vertexStringLabellerCheckBox.isSelected());
        displayPreview();
    }//GEN-LAST:event_vertexStringLabellerCheckBoxActionPerformed

    private void gradientCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gradientCheckBoxActionPerformed
    {//GEN-HEADEREND:event_gradientCheckBoxActionPerformed
        vgp.setHasGradientVertex(gradientCheckBox.isSelected());
        vertexGradientColorButton.setVisible(gradientCheckBox.isSelected());
        vertexPickedGradientColorButton.setVisible(
                gradientCheckBox.isSelected());
        displayPreview();
    }//GEN-LAST:event_gradientCheckBoxActionPerformed

    private void edgeGradientColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgeGradientColorButtonActionPerformed
    {//GEN-HEADEREND:event_edgeGradientColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Normal edge color",
                                               vgp.getEdgeColor());
        vgp.setEdgeColor(color);
        displayPreview();
    }//GEN-LAST:event_edgeGradientColorButtonActionPerformed

    private void vertexPickedGradientColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vertexPickedGradientColorButtonActionPerformed
    {//GEN-HEADEREND:event_vertexPickedGradientColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked gradient color",
                                               vgp.getPickedGradientColor());
        vgp.setPickedGradientColor(color);
        displayPreview();
    }//GEN-LAST:event_vertexPickedGradientColorButtonActionPerformed

    private void vertexGradientColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vertexGradientColorButtonActionPerformed
    {//GEN-HEADEREND:event_vertexGradientColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Gradient color",
                                               vgp.getGradientColor());
        vgp.setGradientColor(color);
        displayPreview();
    }//GEN-LAST:event_vertexGradientColorButtonActionPerformed

    private void vertexFontChooserPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_vertexFontChooserPropertyChange
    {//GEN-HEADEREND:event_vertexFontChooserPropertyChange
        vgp.setVertexFont(vertexFontChooser.getConfiguredFont());
        displayPreview();
    }//GEN-LAST:event_vertexFontChooserPropertyChange

    private void edgeFontChooserPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_edgeFontChooserPropertyChange
    {//GEN-HEADEREND:event_edgeFontChooserPropertyChange
        vgp.setEdgeFont(edgeFontChooser.getConfiguredFont());
        displayPreview();
    }//GEN-LAST:event_edgeFontChooserPropertyChange

    private void edgeColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgeColorButtonActionPerformed
    {//GEN-HEADEREND:event_edgeColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Edge color",
                                               vgp.getEdgeColor());
        vgp.setEdgeColor(color);
        displayPreview();
    }//GEN-LAST:event_edgeColorButtonActionPerformed

    private void pickedEdgeColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pickedEdgeColorButtonActionPerformed
    {//GEN-HEADEREND:event_pickedEdgeColorButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                                               "Picked edge color",
                                               vgp.getPickedEdgeColor());
        vgp.setPickedEdgeColor(color);
        displayPreview();
    }//GEN-LAST:event_pickedEdgeColorButtonActionPerformed

    private void gradientEdgeCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gradientEdgeCheckBoxActionPerformed
    {//GEN-HEADEREND:event_gradientEdgeCheckBoxActionPerformed
        edgeGradientColorButton.setVisible(gradientEdgeCheckBox.isSelected());
        edgePickedGradientColorButton.setVisible(
                gradientEdgeCheckBox.isSelected());
        displayPreview();
    }//GEN-LAST:event_gradientEdgeCheckBoxActionPerformed

    private void edgeStringLabellerCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgeStringLabellerCheckBoxActionPerformed
    {//GEN-HEADEREND:event_edgeStringLabellerCheckBoxActionPerformed
        vgp.setHasEdgeStringLabeller(edgeStringLabellerCheckBox.isSelected());
        displayPreview();
    }//GEN-LAST:event_edgeStringLabellerCheckBoxActionPerformed

    private void edgePatternComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgePatternComboBoxActionPerformed
    {//GEN-HEADEREND:event_edgePatternComboBoxActionPerformed
        vgp.setEdgePattern((float[]) edgePatternComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_edgePatternComboBoxActionPerformed

    private void edgeWidthSpinnerPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_edgeWidthSpinnerPropertyChange
    {//GEN-HEADEREND:event_edgeWidthSpinnerPropertyChange
        vgp.setEdgeLineWidth((int) edgeWidthSpinner.getValue());
        displayPreview();
    }//GEN-LAST:event_edgeWidthSpinnerPropertyChange

    private void edgeShapeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgeShapeComboBoxActionPerformed
    {//GEN-HEADEREND:event_edgeShapeComboBoxActionPerformed
//        vgp.setEdgeShape((EdgeShapeEnum) edgeShapeComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_edgeShapeComboBoxActionPerformed

    private void capJoinComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_capJoinComboBoxActionPerformed
    {//GEN-HEADEREND:event_capJoinComboBoxActionPerformed
        vgp.setCapAndJoin(
                (CapJoinComboBox.CapJoinType) capJoinComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_capJoinComboBoxActionPerformed

    private void vertexShapeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vertexShapeComboBoxActionPerformed
    {//GEN-HEADEREND:event_vertexShapeComboBoxActionPerformed
        //    vgp.setVertexShape((ShapeList) vertexShapeComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_vertexShapeComboBoxActionPerformed

    private void layoutComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_layoutComboBoxActionPerformed
    {//GEN-HEADEREND:event_layoutComboBoxActionPerformed
        vgp.setGraphLayoutType((LayoutChoices) layoutComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_layoutComboBoxActionPerformed

    private void graphTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_graphTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_graphTypeComboBoxActionPerformed
        vgp.setGraphType((GraphTypeChoices) graphTypeComboBox.getSelectedItem());
        displayPreview();
    }//GEN-LAST:event_graphTypeComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.kybelksties.gui.CapJoinComboBox capJoinComboBox;
    private javax.swing.JButton edgeColorButton;
    private com.kybelksties.gui.FontChooser edgeFontChooser;
    private javax.swing.JButton edgeGradientColorButton;
    private javax.swing.JPanel edgePanel;
    private com.kybelksties.gui.LinePatternComboBox edgePatternComboBox;
    private javax.swing.JButton edgePickedGradientColorButton;
    private com.kybelksties.jung.EdgeShapeComboBox edgeShapeComboBox1;
    private javax.swing.JCheckBox edgeStringLabellerCheckBox;
    private javax.swing.JLabel edgeWidthLabel;
    private javax.swing.JSpinner edgeWidthSpinner;
    private javax.swing.JCheckBox editMouseCheckBox;
    private javax.swing.JButton fillColorButton;
    private javax.swing.JCheckBox gradientCheckBox;
    private javax.swing.JCheckBox gradientEdgeCheckBox;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JComboBox graphTypeComboBox;
    private javax.swing.JLabel graphTypeLabel;
    private javax.swing.JComboBox layoutComboBox;
    private javax.swing.JLabel layoutTypeLabel;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel mousePanel;
    private javax.swing.JButton outlineColorButton;
    private javax.swing.JCheckBox pickMouseCheckBox;
    private javax.swing.JButton pickedEdgeColorButton;
    private javax.swing.JButton pickedFillColorButton;
    private javax.swing.JButton pickedOutlineColorButton;
    private com.kybelksties.gui.ShapeComboBox shapeComboBox;
    private javax.swing.JCheckBox showEndPointCheckBox;
    private com.kybelksties.gui.FontChooser vertexFontChooser;
    private javax.swing.JButton vertexGradientColorButton;
    private javax.swing.JPanel vertexPanel;
    private javax.swing.JButton vertexPickedGradientColorButton;
    private javax.swing.JCheckBox vertexStringLabellerCheckBox;
    private javax.swing.JCheckBox zoomMouseCheckBox;
    // End of variables declaration//GEN-END:variables
}
