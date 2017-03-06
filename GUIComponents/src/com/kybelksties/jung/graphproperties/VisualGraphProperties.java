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

// <editor-fold defaultstate="collapsed" desc="Imports...">
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.kybelksties.gui.CapJoinComboBox;
import com.kybelksties.gui.ShapeList;
import com.kybelksties.jung.DirectedAcyclicGraph;
import com.kybelksties.jung.EdgeShapeEnum;
import com.kybelksties.jung.GraphTypeChoices;
import com.kybelksties.jung.HalforderLayout;
import com.kybelksties.jung.LayoutChoices;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.VoltageScorer;
import edu.uci.ics.jung.algorithms.scoring.util.VertexScoreTransformer;
import edu.uci.ics.jung.algorithms.util.SelfLoopEdgePredicate;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.GradientEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.openide.util.NbBundle;
// </editor-fold>

/**
 * Encapsulation of visual properties of a graph.
 *
 * @author Dieter J Kybelksties
 */
public class VisualGraphProperties implements Serializable
{

    private static final Class CLAZZ = VisualGraphProperties.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    /**
     * Runs VoltageRanker on a graph, and returns the transformer.
     *
     * @param graph the graph to use for voltage ranking
     * @return a voltage transformer
     * @throws java.lang.Exception
     */
    public static Function createVoltageTransformer(Graph graph)
            throws Exception
    {
        if (graph == null)
        {
            throw new Exception(NbBundle.getMessage(
                    CLAZZ,
                    "VisualGraphProperties.nullGraphInVoltageTransformer"));
        }
        Collection seeds = graph.getVertices();

        if (seeds == null || seeds.size() < 2)
        {
            throw new Exception(NbBundle.getMessage(
                    CLAZZ,
                    "VisualGraphProperties.needAtLeastTwoSeedNodes",
                    seeds == null ? 0 : seeds.size()));
        }

        // use these seeds as source and sink vertices, run VoltageScorer
        boolean source = true;
        Set sources = new HashSet();
        Set sinks = new HashSet();
        for (Iterator iter = seeds.iterator(); iter.hasNext();)
        {
            if (source)
            {
                sources.add(iter.next());
            }
            else
            {
                sinks.add(iter.next());
            }
            source = !source;
        }
        VoltageScorer vs = new VoltageScorer(graph, sources, sinks);
        vs.acceptDisconnectedGraph(true);
        vs.evaluate();

        return new VertexScoreTransformer<>(vs);
    }

    // <editor-fold defaultstate="collapsed" desc="Field Declarations...">
    // Vertex attributes
    private boolean hasGradientVertex = false;
    private VertexFillColorTransformer vertexToFillColor;
    private Color vertexFillColor1 = Color.GREEN;
    private Color vertexFillColor2 = Color.GREEN;
    private Color vertexPickedFillColor1 = Color.CYAN;
    private Color vertexPickedFillColor2 = Color.CYAN;

    private GraphObjectPaintTransformer vertexToDrawColor;
    private Color vertexDrawColor1 = Color.BLACK;
    private Color vertexDrawColor2 = Color.BLACK;

    private ShapeList vertexShapeType = ShapeList.RECTANGLE;
    private boolean hasVertexStringLabels = false;

    private Function vertexToString;
    private VertexStrokeHighlightTransformer vertexStrokeHighlight;
    private VertexFontTransformer vertexToFont;
    private Function noVertexToString;
    private VertexShapeSizeAspectTransformer vertexShapeSizeAspect;
    private VertexDegreeFilterPredicate showVertexPred;
    private Font vertexFont;

    // edge attributes
    private Color edgeColor1 = Color.BLACK;
    private Color edgeColor2 = Color.BLACK;
    private Color pickedEdgeColor1 = Color.WHITE;
    private Color pickedEdgeColor2 = Color.WHITE;
    private boolean hasEdgeStringLabels = false;
    private boolean hasGradientEdge = false;

    private EdgeWeightStrokeTransformer edgeWeightStroke;
    private Function edgeToString;
    private Function noEdgeToString;
    private EdgeFontTransformer edgeToFont;
    private DirectionDisplayPredicate showEdgePred;
    private DirectionDisplayPredicate showArrowPred;
    private Predicate selfLoopPred;
    private GradientPickedEdgePaintTransformer edgeDrawPaint;
    private GradientPickedEdgePaintTransformer edgeFillPaint;
    private Font edgeFont;
    private float[] edgePattern;
    private int edgeWidth;

    // general graph attributes
    private Map<Object, Number> edge_weight = new HashMap<>();
    private Function voltages;
    private Map<Object, Number> transparency = new HashMap<>();
    private VisualizationViewer vv;
    private LayoutChoices graphLayoutType;
    AbstractLayout layout = null;
    private GraphTypeChoices graphType;
    private DefaultModalGraphMouse<Integer, Number> gm;
    private EdgeShapeEnum edgeShapeType;
    private CapJoinComboBox.CapJoinType capJoinType;
    // </editor-fold>

    /**
     * Create a graph-layout for from the layout-choice.
     *
     * @param <V>        generic vertex type
     * @param <E>        generic edge type
     * @param layoutType type of layout
     * @param graph      the graph on which to apply the layout
     * @param dimension  dimension of the panel
     * @param param      optional parameters
     * @return the generated layout
     * @throws Exception
     */
    public <V, E> Layout<V, E> createGraphLayout(
            LayoutChoices layoutType,
            Graph graph,
            Dimension dimension,
            Object... param) throws Exception
    {
        Layout<V, E> reval = null;
        if (graph == null)
        {
            throw new Exception(NbBundle.getMessage(
                    CLAZZ,
                    "VisualGraphProperties.nullGraphInSetLayout"));
        }
        switch (layoutType)
        {
            case HALFORDER:
                if (graph instanceof DirectedAcyclicGraph)
                {
                    HalforderLayout.LayoutDirection direction =
                                                    (param == null ||
                                                     param.length == 0) ?
                                                    HalforderLayout.LayoutDirection.LEFT_TO_RIGHT :
                                                    (HalforderLayout.LayoutDirection) param[0];
                    reval = new HalforderLayout<>((DirectedAcyclicGraph) graph,
                                                  dimension,
                                                  direction);
                }
                else
                {
                    throw new Exception(
                            NbBundle.getMessage(CLAZZ,
                                                "VisualGraphProperties.halforderLayoutOnlyForDAG",
                                                LayoutChoices.HALFORDER));
                }
        }
        return reval;
    }

    /**
     * Create a visualization viewer on the given panel for the graph with the
     * given layout using this objects properties as graph-properties.
     *
     * @param <V>      generic vertex type
     * @param <E>      generic edge type
     * @param panel    the panel on which to create the visualization viewer -
     *                 if null, then the user needs to add the returned
     *                 visualization view to the component manually.
     * @param newGraph the graph to display
     * @param layout   the graph-layout to use for the graph
     * @return the visualization viewer
     * @throws java.lang.Exception
     */
    public <V, E> VisualizationViewer<V, E> createVisualizationViewer(
            JPanel panel,
            Graph<V, E> newGraph,
            Layout<V, E> layout) throws Exception
    {
        Dimension currentSize = (this.layout != null) ? this.layout.getSize() :
                                (panel != null) ? panel.getSize() :
                                new Dimension(100, 100);

        this.layout = (AbstractLayout) layout;
        vv = new VisualizationViewer<>(layout);
        this.layout.setSize(currentSize);

        PickedState<V> picked_state = vv.getPickedVertexState();

        // affineTransformer  = vv.getLayoutTransformer();
        Collection verts = newGraph.getVertices();

        // assign a transparency value of 0.9 to all vertices
        for (Object v : verts)
        {
            transparency.put(v, 0.9);
        }

        selfLoopPred = new SelfLoopEdgePredicate<>();
        // create decorators
        vertexToFillColor = new VertexFillColorTransformer<>(picked_state);
        vertexToDrawColor = new GraphObjectPaintTransformer<>(picked_state);
        edgeWeightStroke = new EdgeWeightStrokeTransformer<>(edge_weight);
        vertexStrokeHighlight = new VertexStrokeHighlightTransformer<>(newGraph,
                                                                       picked_state);
        vertexToFont = new VertexFontTransformer<>(vertexFont);
        edgeToFont = new EdgeFontTransformer<>(edgeFont);
        vertexToString = new ToStringLabeller();
        noVertexToString = new ConstantTransformer("");
        edgeToString = new ToStringLabeller();
        noEdgeToString = new ConstantTransformer("");
        voltages = createVoltageTransformer(newGraph);
        vertexShapeSizeAspect = new VertexShapeSizeAspectTransformer<>(newGraph,
                                                                       voltages);
        showEdgePred = new DirectionDisplayPredicate<>(true, true);
        showArrowPred = new DirectionDisplayPredicate<>(true, false);
        showVertexPred = new VertexDegreeFilterPredicate<>(false);

        // uses a gradient edge if unpicked, otherwise uses picked selection
        edgeDrawPaint =
        new GradientPickedEdgePaintTransformer<>(
                new PickableEdgePaintTransformer<>(vv.getPickedEdgeState(),
                                                   Color.black,
                                                   Color.cyan),
                vv);
        edgeFillPaint =
        new GradientPickedEdgePaintTransformer<>(
                new PickableEdgePaintTransformer<>(vv.getPickedEdgeState(),
                                                   Color.black,
                                                   Color.cyan),
                vv);

        vv.getRenderContext().setVertexFillPaintTransformer(vertexToFillColor);
        vv.getRenderContext().setVertexDrawPaintTransformer(vertexToDrawColor);
        vv.getRenderContext().setVertexStrokeTransformer(vertexStrokeHighlight);
        vv.getRenderContext().setVertexLabelTransformer(
                (hasVertexStringLabels) ? vertexToString : noVertexToString);
        vv.getRenderContext().setVertexFontTransformer(vertexToFont);
        vv.getRenderContext().setVertexShapeTransformer(vertexShapeSizeAspect);
        vv.getRenderContext().setVertexIncludePredicate(showVertexPred);

        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPaint);
        vv.getRenderContext().setEdgeLabelTransformer(
                (hasEdgeStringLabels) ? edgeToString : noEdgeToString);
        vv.getRenderContext().setEdgeFontTransformer(edgeToFont);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeWeightStroke);
        vv.getRenderContext().setEdgeIncludePredicate(showEdgePred);
        vv.getRenderContext().setEdgeShapeTransformer(
                (new EdgeShape(newGraph)).new Line());
        vv.getRenderContext().setEdgeArrowPredicate(showArrowPred);

        vv.getRenderContext().setArrowFillPaintTransformer(
                new ConstantTransformer(Color.lightGray));
        vv.getRenderContext().setArrowDrawPaintTransformer(
                new ConstantTransformer(Color.black));
        if (panel != null)
        {
            panel.setLayout(new BorderLayout());
            GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
            panel.add(scrollPane);
        }
        gm = new DefaultModalGraphMouse<>();
        vv.setGraphMouse(gm);
        gm.add(new PopupGraphMousePlugin());

        vertexShapeSizeAspect.setScaling(true);

        vv.setVertexToolTipTransformer(new VoltageTips<Number>());
        vv.setToolTipText(
                NbBundle.getMessage(CLAZZ, "VisualGraphProperties.toolTipText"));

        vv.setVisible(true);
        return vv;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters...">
    /**
     *
     * @return
     */
    public Color getVertexFillColor()
    {
        return vertexFillColor1;
    }

    /**
     *
     * @param color
     */
    public void setVertexFillColor(Color color)
    {
        vertexFillColor1 = color;
    }

    /**
     *
     * @return
     */
    public Color getVertexOutlineColor()
    {
        return vertexDrawColor1;
    }

    /**
     *
     * @param color
     */
    public void setVertexOutlineColor(Color color)
    {
        vertexDrawColor1 = color;
    }

    /**
     *
     * @return
     */
    public Color getPickedVertexFillColor()
    {
        return vertexPickedFillColor1;
    }

    /**
     *
     * @param color
     */
    public void setPickedVertexFillColor(Color color)
    {
        vertexPickedFillColor1 = color;
    }

    /**
     *
     * @return
     */
    public Color getPickedVertexOutlineColor()
    {
        return vertexDrawColor1;
    }

    /**
     *
     * @param color
     */
    public void setPickedVertexOutlineColor(Color color)
    {
        vertexDrawColor1 = color;
    }

    /**
     *
     * @return
     */
    public Color getEdgeColor()
    {
        return edgeColor1;
    }

    /**
     *
     * @param color
     */
    public void setEdgeColor(Color color)
    {
        edgeColor1 = color;
    }

    /**
     *
     * @return
     */
    public Color getPickedEdgeColor()
    {
        return edgeColor1;
    }

    /**
     *
     * @param color
     */
    public void setPickedEdgeColor(Color color)
    {
        pickedEdgeColor1 = color;
    }

    /**
     *
     * @param shapeType
     */
    public void setVertexShape(ShapeList shapeType)
    {
        vertexShapeType = shapeType;
    }

    /**
     *
     * @return
     */
    public ShapeList getVertexShape()
    {
        return vertexShapeType;
    }

    /**
     *
     * @param hasVertexStringLabels
     */
    public void setHasVertexStringLabeller(boolean hasVertexStringLabels)
    {
        this.hasVertexStringLabels = hasVertexStringLabels;
    }

    /**
     *
     * @param hasGradientVertex
     */
    public void setHasGradientVertex(boolean hasGradientVertex)
    {
        this.hasGradientVertex = hasGradientVertex;
        if (this.hasGradientVertex)
        {
            vv.getRenderContext().setEdgeLabelTransformer(vertexToString);
        }
        else
        {
            vv.getRenderContext().setEdgeLabelTransformer(noVertexToString);
        }
    }

    /**
     *
     * @param hasEdgeStringLabels
     */
    public void setHasEdgeStringLabeller(boolean hasEdgeStringLabels)
    {
        this.hasEdgeStringLabels = hasEdgeStringLabels;
        if (this.hasEdgeStringLabels)
        {
            vv.getRenderContext().setEdgeLabelTransformer(edgeToString);
        }
        else
        {
            vv.getRenderContext().setEdgeLabelTransformer(noEdgeToString);
        }
    }

    /**
     *
     * @param hasGradientEdge
     */
    public void setHasGradientEdge(boolean hasGradientEdge)
    {
        this.hasGradientEdge = hasGradientEdge;
    }

    /**
     *
     * @return
     */
    public Color getPickedGradientColor()
    {
        return vertexPickedFillColor2;
    }

    /**
     *
     * @param color
     */
    public void setPickedGradientColor(Color color)
    {
        vertexPickedFillColor2 = color;
    }

    /**
     *
     * @return
     */
    public Color getGradientColor()
    {
        return vertexFillColor2;
    }

    /**
     *
     * @param color
     */
    public void setGradientColor(Color color)
    {
        vertexFillColor2 = color;
    }

    /**
     *
     * @return
     */
    public Font getVertexFont()
    {
        return vertexFont;
    }

    /**
     *
     * @param font
     */
    public void setVertexFont(Font font)
    {
        vertexFont = font;
    }

    /**
     *
     * @param configuredFont
     */
    public void setEdgeFont(Font configuredFont)
    {
        edgeFont = configuredFont;
    }

    /**
     *
     * @param pattern
     */
    public void setEdgePattern(float[] pattern)
    {
        edgePattern = pattern;
    }

    /**
     *
     * @return
     */
    public float[] getEdgePattern()
    {
        return edgePattern;
    }

    /**
     *
     * @param width
     */
    public void setEdgeLineWidth(int width)
    {
        edgeWidth = width;
    }

    /**
     *
     * @return
     */
    public int getEdgeLineWidth()
    {
        return edgeWidth;
    }

    /**
     *
     * @return
     */
    public EdgeShapeEnum getEdgeShape()
    {
        return this.edgeShapeType;
    }

    /**
     *
     * @param edgeShapeType
     */
    public void setEdgeShape(EdgeShapeEnum edgeShapeType)
    {
        this.edgeShapeType = edgeShapeType;
    }

    /**
     *
     * @return
     */
    public CapJoinComboBox.CapJoinType getCapAndJoin()
    {
        return this.capJoinType;
    }

    /**
     *
     * @param capJoinType
     */
    public void setCapAndJoin(CapJoinComboBox.CapJoinType capJoinType)
    {
        this.capJoinType = capJoinType;
    }

    /**
     *
     * @param graphLayoutType
     */
    public void setGraphLayoutType(LayoutChoices graphLayoutType)
    {
        this.graphLayoutType = graphLayoutType;
    }

    /**
     *
     * @param graphType
     */
    public void setGraphType(GraphTypeChoices graphType)
    {
        this.graphType = graphType;
    }

    // </editor-fold>
    private final class VertexFillColorTransformer<V> implements
            Function<V, Paint>
    {

        final static float dark_value = 0.8f;
        final static float light_value = 0.2f;
        PickedInfo<V> pickedInfo;
        Color unpickedColor1 = vertexFillColor1;
        Color unpickedColor2 = vertexFillColor2;
        Color pickedColor1 = vertexPickedFillColor1;
        Color pickedColor2 = vertexPickedFillColor2;

        VertexFillColorTransformer(PickedInfo<V> pickedInfo)
        {
            this.pickedInfo = pickedInfo;
        }

        VertexFillColorTransformer(PickedInfo<V> pickedInfo,
                                   Color color1,
                                   Color color2)
        {
            this(pickedInfo);
            this.unpickedColor1 = color1;
            this.unpickedColor2 = color2;
        }

        VertexFillColorTransformer(PickedInfo<V> pickedInfo, Color color)
        {
            this(pickedInfo, color, color);
        }

        @Override
        public Paint apply(V v)
        {
            float alpha = transparency.get(v).floatValue();

            if (pickedInfo.isPicked(v))
            {
                float[] rgb1 = pickedColor1.getColorComponents(null);
                pickedColor1 = new Color(rgb1[0], rgb1[1], rgb1[2], alpha);
                float[] rgb2 = pickedColor2.getColorComponents(null);
                pickedColor2 = new Color(rgb2[0], rgb2[1], rgb2[2], alpha);
                return new GradientPaint(0,
                                         0,
                                         pickedColor1,
                                         10,
                                         0,
                                         pickedColor2,
                                         true);
            }
            else
            {
                float[] rgb1 = unpickedColor1.getColorComponents(null);
                unpickedColor1 = new Color(rgb1[0], rgb1[1], rgb1[2], alpha);
                float[] rgb2 = unpickedColor2.getColorComponents(null);
                unpickedColor2 = new Color(rgb2[0], rgb2[1], rgb2[2], alpha);
                return new GradientPaint(0,
                                         0,
                                         unpickedColor1,
                                         10,
                                         0,
                                         unpickedColor2,
                                         true);
            }

        }
    }

    private final class ConstantTransformer<T1, T2> implements Function< T1, T2>
    {

        T2 constant = null;

        ConstantTransformer(T2 constant)
        {
            this.constant = constant;
        }

        @Override
        public T2 apply(T1 f)
        {
            return constant;
        }

    }

    private final class EdgeWeightStrokeTransformer<E>
            implements Function<E, Stroke>
    {

        final Stroke basic = new BasicStroke(edgeWidth);
        double heavyWidth = (double) edgeWidth * 1.5 + 1.0;
        final Stroke heavy = new BasicStroke((int) heavyWidth);
        final Stroke dotted = RenderContext.DOTTED;

        boolean weighted = false;
        Map<E, Number> edge_weight;

        EdgeWeightStrokeTransformer(Map<E, Number> edge_weight)
        {
            this.edge_weight = edge_weight;
        }

        public void setWeighted(boolean weighted)
        {
            this.weighted = weighted;
        }

        @Override
        public Stroke apply(E e)
        {
            if (weighted)
            {
                if (drawHeavy(e))
                {
                    return heavy;
                }
                else
                {
                    return dotted;
                }
            }
            else
            {
                return basic;

            }
        }

        boolean drawHeavy(E e)
        {
            double value = edge_weight.get(e).doubleValue();
            return value > 0.7;
        }

    }

    private final class VertexStrokeHighlightTransformer<V, E>
            implements Function<V, Stroke>
    {

        boolean highlight = false;
        Stroke heavy = new BasicStroke(5);
        Stroke medium = new BasicStroke(3);
        Stroke light = new BasicStroke(1);
        PickedInfo<V> pi;
        Graph<V, E> graph;

        VertexStrokeHighlightTransformer(Graph<V, E> graph,
                                         PickedInfo<V> pi)
        {
            this.graph = graph;
            this.pi = pi;
        }

        public void setHighlight(boolean highlight)
        {
            this.highlight = highlight;
        }

        @Override
        public Stroke apply(V v)
        {
            if (highlight)
            {
                if (pi.isPicked(v))
                {
                    return heavy;
                }
                else
                {
                    for (V w : graph.getNeighbors(v))
                    {
                        if (pi.isPicked(w))
                        {
                            return medium;
                        }
                    }
                    return light;
                }
            }
            else
            {
                return light;
            }
        }

    }

    private final class VertexFontTransformer<V>
            implements Function<V, Font>
    {

        boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        VertexFontTransformer()
        {
        }

        VertexFontTransformer(Font f)
        {
            this();
            if (f != null)
            {
                this.f = f;
                this.b = new Font(f.getFontName(),
                                  f.isBold() ? Font.PLAIN : Font.BOLD,
                                  f.getSize());
            }
        }

        VertexFontTransformer(Font f, Font b)
        {
            this();
            if (f != null && b != null)
            {
                this.f = f;
                this.b = b;
            }
        }

        public void setBold(boolean bold)
        {
            this.bold = bold;
        }

        @Override
        public Font apply(V v)
        {
            if (bold)
            {
                return b;
            }
            else
            {
                return f;
            }
        }
    }

    private final class EdgeFontTransformer<E>
            implements Function<E, Font>
    {

        boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        EdgeFontTransformer()
        {
        }

        EdgeFontTransformer(Font f)
        {
            this();
            if (f != null)
            {
                this.f = f;
                this.b = new Font(f.getFontName(),
                                  f.isBold() ? Font.PLAIN : Font.BOLD,
                                  f.getSize());
            }
        }

        EdgeFontTransformer(Font f, Font b)
        {
            this();
            if (f != null && b != null)
            {
                this.f = f;
                this.b = b;
            }
        }

        public void setBold(boolean bold)
        {
            this.bold = bold;
        }

        @Override
        public Font apply(E e)
        {
            if (bold)
            {
                return b;
            }
            else
            {
                return f;
            }
        }
    }

    /**
     * Transforms an edge into a string describing its voltage.
     *
     * @param <E> an edge type
     */
    public class VoltageTips<E>
            implements Function<E, String>
    {

        /**
         *
         * @param vertex
         * @return
         */
        @Override
        public String apply(E vertex)
        {
            return NbBundle.getMessage(CLAZZ,
                                       "VisualGraphProperties.voltageString",
                                       voltages.apply(vertex));
        }
    }

    private final class DirectionDisplayPredicate<V, E>
            implements Predicate<Context<Graph<V, E>, E>>
    {

        boolean show_d;
        boolean show_u;

        DirectionDisplayPredicate(boolean show_d, boolean show_u)
        {
            this.show_d = show_d;
            this.show_u = show_u;
        }

        public void showDirected(boolean b)
        {
            show_d = b;
        }

        public void showUndirected(boolean b)
        {
            show_u = b;
        }

        @Override
        public boolean apply(Context<Graph<V, E>, E> context)
        {
            Graph<V, E> graph = context.graph;
            E e = context.element;
            if (graph.getEdgeType(e) == EdgeType.DIRECTED && show_d)
            {
                return true;
            }
            return graph.getEdgeType(e) == EdgeType.UNDIRECTED && show_u;
        }

        @Override
        public boolean test(Context<Graph<V, E>, E> t)
        {
            return apply(t);
        }

    }

    private final class VertexShapeFontStringTransformer<V, E>
            extends AbstractVertexShapeTransformer<V>
            implements Function<V, Shape>
    {

        Graphics graphics;

        VertexShapeFontStringTransformer(Graphics graphics)
        {
            this.graphics = graphics;
        }

        @Override
        public Shape apply(V v)
        {
            // get metrics from the graphics
            FontMetrics metrics = graphics.getFontMetrics(vertexFont);
            // get the height of a line of text in this
            // font and render context
            int fontHeight = metrics.getHeight();
            // get the advance of my text in this font
            // and render context
            String labelString = " ";
            if (v != null && !v.toString().isEmpty())
            {
                labelString = v.toString();
            }
            int fontWidth = metrics.stringWidth(labelString);
            // calculate the size of a box to hold the
            // text with some padding.
            Dimension size = new Dimension(fontWidth + 2, fontHeight + 2);
            float width = size.width;
            float height = size.height;
            float h_offset = -(width / 2);
            float v_offset = -(height / 2);
            Rectangle2D theRectangle = new Rectangle2D.Double();
            theRectangle.setFrame(h_offset, v_offset, width, height);
            return theRectangle;
        }

    }

    /**
     * Controls the shape, size, and aspect ratio for each vertex.
     *
     * @author Joshua O'Madadhain
     */
    private final class VertexShapeSizeAspectTransformer<V, E>
            extends AbstractVertexShapeTransformer<V>
            implements Function<V, Shape>
    {

        boolean stretch = false;
        boolean scale = false;
        Function<V, Double> voltages;
        Graph<V, E> graph;
        AffineTransform scaleTransform = new AffineTransform();

        VertexShapeSizeAspectTransformer(Graph<V, E> graphIn,
                                         Function<V, Double> voltagesIn)
        {
            this.graph = graphIn;
            this.voltages = voltagesIn;
            setSizeTransformer(new Function<V, Integer>()
            {

                @Override
                public Integer apply(V v)
                {
                    if (scale)
                    {
                        return (int) (voltages.apply(v) * 30) + 20;
                    }
                    else
                    {
                        return 20;
                    }

                }
            });
            setAspectRatioTransformer(new Function<V, Float>()
            {

                @Override
                public Float apply(V v)
                {
                    if (stretch)
                    {
                        return (float) (graph.inDegree(v) + 1) /
                               (graph.outDegree(v) + 1);
                    }
                    else
                    {
                        return 1.0f;
                    }
                }
            });
        }

        /**
         * Set the whether to stretch or not.
         *
         * @param stretch the new value
         */
        public void setStretching(boolean stretch)
        {
            this.stretch = stretch;
        }

        public void setScaling(boolean scale)
        {
            this.scale = scale;
        }

        @Override
        public Shape apply(V v)
        {
            if (vertexShapeType == ShapeList.ELLIPSE)
            {
                return factory.getEllipse(v);
            }
            if (vertexShapeType == ShapeList.RECTANGLE)
            {
                return factory.getRectangle(v);
            }
            if (vertexShapeType == ShapeList.ROUNDED_RECTANGLE)
            {
                return factory.getRoundRectangle(v);
            }
            if (vertexShapeType == ShapeList.REGULAR_POLYGON)
            {
                int sides = Math.max(graph.degree(v), 3);
                return factory.getRegularPolygon(v, sides);
            }
            if (vertexShapeType == ShapeList.REGULAR_STAR)
            {
                int sides = Math.max(graph.degree(v), 5);
                return factory.getRegularStar(v, sides);
            }
            else
            {
                return factory.getEllipse(v);
            }
        }
    }

    /**
     * Controls the paint of picked edges.
     *
     * @param <V> generic vertex type
     * @param <E> generic edge type
     */
    public class GradientPickedEdgePaintTransformer<V, E>
            extends GradientEdgePaintTransformer<V, E>
    {

        private final Function<E, Paint> defaultFunc;
        private boolean fillEdge = false;
//        Predicate<Context<Graph<V, E>, E>> selfLoop =
//                                           new SelfLoopEdgePredicate<>();

        /**
         * Construct with a default paint function and a visualisation viewer.
         *
         * @param defaultEdgePaintFunction default edge paint function object
         * @param vv                       the visualisation viewer
         */
        public GradientPickedEdgePaintTransformer(
                Function<E, Paint> defaultEdgePaintFunction,
                VisualizationViewer<V, E> vv)
        {
            super(Color.WHITE, Color.BLACK, vv);
            this.defaultFunc = defaultEdgePaintFunction;
        }

        /**
         * Set whether or not to use fill for the edge.
         *
         * @param b true for yes, false otherwise
         */
        public void useFill(boolean b)
        {
            fillEdge = b;
        }

        @Override
        public Paint apply(E e)
        {
            return hasGradientVertex ?
                   super.apply(e) :
                   defaultFunc.apply(e);
        }

        @Override
        protected Color getColor2(E e)
        {
            return vv.getPickedEdgeState().isPicked(e) ? Color.CYAN : c2;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Predicates/Plugins...">
    private final class VertexDegreeFilterPredicate<V, E>
            implements Predicate<Context<Graph<V, E>, V>>
    {

        private Boolean filterType = null;
        private Integer degree = null;

        /**
         * Default construct. Don't filter any vertices out.
         */
        VertexDegreeFilterPredicate()
        {
        }

        VertexDegreeFilterPredicate(boolean filterSmall)
        {
            this(filterSmall, 4);
        }

        VertexDegreeFilterPredicate(boolean filterType, int minDegree)
        {
            if (filterType)
            {
                setFilterBig(degree);
            }
            else
            {
                setFilterSmall(degree);
            }
        }

        public void setFilterSmall(Integer degree)
        {
            if (degree != null && degree > -1)
            {
                this.filterType = false;
                this.degree = degree;
            }
            else
            {
                unsetFilter();
            }
        }

        public void setFilterBig(Integer degree)
        {
            if (degree != null && degree > -1)
            {
                this.filterType = true;
                this.degree = degree;
            }
            else
            {
                unsetFilter();
            }
        }

        public void unsetFilter()
        {
            this.degree = null;
            this.filterType = null;
        }

        @Override
        public boolean apply(Context<Graph<V, E>, V> context)
        {
            if (filterType == null)
            {
                return true; // show all vertices
            }

            Graph<V, E> graph = context.graph;
            V v = context.element;

            if (filterType == false)
            {
                // show only the ones with big degree
                return (graph.degree(v) >= degree);

            }
            else
            {
                // show only the ones with small degree
                return (graph.degree(v) <= degree);
            }
        }

        @Override
        public boolean test(Context<Graph<V, E>, V> t)
        {
            return apply(t);
        }
    }

    /**
     * A GraphMousePlugin that offers pop-up menu support.
     *
     * @param <V> generic vertex type
     * @param <E> generic edge type
     */
    protected class PopupGraphMousePlugin<V, E>
            extends AbstractPopupGraphMousePlugin
            implements MouseListener
    {

        /**
         * Default construct.
         */
        PopupGraphMousePlugin()
        {
            this(MouseEvent.BUTTON3_MASK);
        }

        /**
         * Construct with modifiers.
         *
         * @param modifiers
         */
        PopupGraphMousePlugin(int modifiers)
        {
            super(modifiers);
        }

        /**
         * If this event is over a Vertex, pop up a menu to allow the user to
         * increase/decrease the voltage attribute of this Vertex.
         *
         * @param e the triggering mouse event
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void handlePopup(MouseEvent e)
        {
            final VisualizationViewer<V, E> vv =
                                            (VisualizationViewer<V, E>) e.
                                            getSource();
            Point2D p = e.getPoint();
            //vv.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());
            GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
            if (pickSupport != null)
            {
                final V v = pickSupport.getVertex(vv.getGraphLayout(),
                                                  p.getX(),
                                                  p.getY());
                if (v != null)
                {
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(new AbstractAction(
                            NbBundle.getMessage(CLAZZ,
                                                "VisualGraphProperties.increaseTransparency"))
                            {
                                @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    Double value = Math.min(1,
                                                            transparency.get(v).
                                                            doubleValue() + 0.1);
                                    transparency.put(v, value);
                                    vv.repaint();
                                }
                            });
                    popup.add(new AbstractAction(
                            NbBundle.getMessage(CLAZZ,
                                                "VisualGraphProperties.decreaseTransparency"))
                            {
                                @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    Double value = Math.max(0,
                                                            transparency.get(v).
                                                            doubleValue() - 0.1);
                                    transparency.put(v, value);
                                    vv.repaint();
                                }
                            });
                    popup.show(vv, e.getX(), e.getY());
                }
                else
                {
                    final E edge = pickSupport.getEdge(vv.getGraphLayout(),
                                                       p.getX(),
                                                       p.getY());
                    if (edge != null)
                    {
                        JPopupMenu popup = new JPopupMenu();
                        popup.add(new AbstractAction(edge.toString())
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                System.err.println("got " + edge);
                            }
                        });
                        popup.show(vv, e.getX(), e.getY());

                    }
                }
            }
        }
    }

}
