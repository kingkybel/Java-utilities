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
package com.kybelksties.process;

import com.kybelksties.general.DateUtils;
import com.kybelksties.general.EnvironmentVarSets;
import com.kybelksties.general.ToString;
import com.kybelksties.gui.ColorUtils;
import com.kybelksties.protocol.ProcessMessage;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.openide.util.NbBundle;

/**
 * Class to encapsulate executables and processes of these executables.
 *
 * @author Dieter J Kybelksties
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProcess implements Serializable
{

    private static final Class CLAZZ = ScheduledProcess.class;
    private static final String CLASS_NAME = CLAZZ.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final long serialVersionUID = -8940196742313991701L;

    private ConcreteProcess process = null;
    private ExeDefinition exeDefinition = null;
    private Long noopTime = 0L;
    private String windowForeground = "white";
    private String windowBackground = "black";
    private String startInDirectory = null;
    private WindowMode windowMode = WindowMode.XTERM;
    private String logFileName;
    ConnectionInfoList.ConnectionInfo connectionInfo =
                                      new ConnectionInfoList.ConnectionInfo();
    private Rectangle dimensions = null;
    private static ArrayList<Rectangle> defaultGeometries =
                                        makeDefaultGeometries();
    private static int defaultGeometryNumber = 0;

    /**
     * Default construct.
     */
    public ScheduledProcess()
    {
        process = new ConcreteProcess();
    }

    /**
     * Construct with an executable definition and environment.
     *
     * @param exeDefinition contains the paths, parameters etc
     * @param environment   contains the environment variables
     */
    public ScheduledProcess(ExeDefinition exeDefinition,
                            EnvironmentVarSets environment)
    {
        this();
        this.process = new ConcreteProcess(environment);
        this.exeDefinition = new ExeDefinition(exeDefinition);
    }

    /**
     * Construct with an executable definition and environment.
     *
     * @param exeDefinition    contains the paths, parameters etc
     * @param environment      contains the environment variables
     * @param startInDirectory contains the directory in which the process
     *                         should be started
     */
    public ScheduledProcess(ExeDefinition exeDefinition,
                            EnvironmentVarSets environment,
                            String startInDirectory)
    {
        this(exeDefinition, environment);
        this.startInDirectory = startInDirectory;
    }

    /**
     * Copy-construct.
     *
     * @param rhs the right-hand-side object
     */
    public ScheduledProcess(ScheduledProcess rhs)
    {
        this(rhs.exeDefinition,
             rhs.getProcess().getCategorisedEnvironment(),
             rhs.startInDirectory);
    }

    @Override
    public String toString()
    {
        TreeMap<String, String> sortedEnv = new TreeMap<>();
        for (String key : process.getEnvironmentMap().keySet())
        {
            sortedEnv.put(key, process.getEnvironmentMap().get(key));
        }
        return NbBundle.getMessage(
                CLAZZ,
                process == null ?
                "ScheduledProcess.noProcess" :
                "ScheduledProcess.process",
                ToString.make(process.command()),
                startInDirectory,
                ToString.make(sortedEnv),
                logFileName);

    }

    /**
     * Retrieve the concrete process object. This is not a system process but
     * rather the wrapped object.
     *
     * @return the concrete process object
     */
    public ConcreteProcess getProcess()
    {
        if (startInDirectory != null)
        {
            process.directory(startInDirectory);
        }
        return process;
    }

    /**
     * Set the concrete process object.
     *
     * @param process the new concrete process
     */
    public void setProcess(ConcreteProcess process)
    {
        this.process = process;
        if (startInDirectory != null)
        {
            process.directory(startInDirectory);
        }
    }

    /**
     * Get the value of targetMachine
     *
     * @return the value of targetMachine
     */
    public String getTargetMachine()
    {
        return connectionInfo.serverIP;
    }

    /**
     * Get the value of targetMachine
     *
     * @return the value of targetMachine
     */
    public int getPort()
    {
        return connectionInfo.port;
    }

    /**
     * Retrieve the connection information.
     *
     * @return the connection information
     */
    public ConnectionInfoList.ConnectionInfo getConnectionInfo()
    {
        return connectionInfo;
    }

    /**
     * Set the connection information
     *
     * @param connectionInfo the new connection information
     */
    public void setConnectionInfo(
            ConnectionInfoList.ConnectionInfo connectionInfo)
    {
        this.connectionInfo = connectionInfo;
    }

    /**
     * Get the value of dimensions.
     *
     * @return the value of dimensions
     */
    public Rectangle getDimensions()
    {
        return dimensions;
    }

    /**
     * Set the value of dimensions.
     *
     * @param dimensions new value of dimensions
     */
    public void setDimensions(Rectangle dimensions)
    {
        this.dimensions = dimensions;
    }

    /**
     * Retrieve the executable definition.
     *
     * @return the executable definition
     */
    public ExeDefinition getExeDefinition()
    {
        return exeDefinition;
    }

    /**
     * Set the executable definition.
     *
     * @param exeDefinition the new definition
     */
    public void setExeDefinition(ExeDefinition exeDefinition)
    {
        this.exeDefinition = exeDefinition;
        if (startInDirectory != null)
        {
            process.directory(startInDirectory);
        }
    }

    /**
     * Retrieve whether the process requires an xterm.
     *
     * @return true if so
     */
    public WindowMode getWindowMode()
    {
        return windowMode;
    }

    /**
     * Set whether the process requires an xterm.
     *
     * @param windowMode the new value
     */
    public void setWindowMode(WindowMode windowMode)
    {
        this.windowMode = windowMode;
    }

    /**
     * Get the value of logFileName
     *
     * @return the value of logFileName
     */
    public String getLogFileName()
    {
        return logFileName;
    }

    /**
     * Set the value of logFileName
     *
     * @param logFileName new value of logFileName
     */
    public void setLogFileName(String logFileName)
    {
        this.logFileName = logFileName;
    }

    /**
     * Retrieve the no-operation time.
     *
     * @return the no-operation time
     */
    public Long getNoopTime()
    {
        return noopTime;
    }

    /**
     * Set the no-operation time.
     *
     * @param noopTime the new no-operation time
     */
    public void setNoopTime(Long noopTime)
    {
        this.noopTime = noopTime;
    }

    /**
     * Convert all the parameters into one string.
     *
     * @return the parameters as a string
     */
    public String params2String()
    {
        return exeDefinition.getParameters().toString();
    }

    /**
     * Scan a string into a list of parameters.
     *
     * @param paramStr the string containing parameters
     */
    public void setParameters(String paramStr)
    {
        exeDefinition.getParameters().fromString(paramStr);
    }

    /**
     * Retrieve the background color for this scheduled process.
     *
     * @return the background color
     */
    public Color getWindowBackground()
    {
        return ColorUtils.getXtermColor(windowBackground);
    }

    /**
     * Set the background color for this scheduled process.
     *
     * @param windowBackgroundColor the new background color
     *
     */
    public void setWindowBackground(Color windowBackgroundColor)
    {
        if (windowBackgroundColor == null)
        {
            windowBackgroundColor = Color.BLACK;
        }
        this.windowBackground = ColorUtils.xtermColorString(
        windowBackgroundColor);
    }

    /**
     * Retrieve the foreground color for this scheduled process.
     *
     * @return the foreground color
     */
    public Color getWindowForeground()
    {
        return ColorUtils.getXtermColor(windowForeground);
    }

    /**
     * Set the foreground color for this scheduled process.
     *
     * @param windowForeground the new background color
     *
     */
    public void setWindowForeground(Color windowForeground)
    {
        if (windowForeground == null)
        {
            windowForeground = Color.WHITE;
        }
        this.windowForeground = ColorUtils.xtermColorString(windowForeground);
    }

    /**
     * Check if all parameters are defined.
     *
     * @return true if so, false otherwise
     */
    public boolean allParametetersDefined()
    {
        return exeDefinition.allParametersDefined();
    }

    /**
     * Retrieve a string describing the missing parameters.
     *
     * @return the missing parameters as string
     */
    public String getMissingParameters()
    {
        return exeDefinition.getMissingParameters();
    }

    /**
     * Set the path to the executable file.
     *
     * @param exePath the path to the file
     */
    public void setExecutable(String exePath)
    {
        exeDefinition.setExecutable(exePath);
    }

    /**
     * Retrieve the directory the process is going to use as
     * sendStartInstruction directory.
     *
     * @return the directory as string
     */
    public String getStartInDirectory()
    {
        return startInDirectory;
    }

    /**
     * Set the directory in which the process should sendStartInstruction
     *
     * @param startInDirectory the new directory
     */
    public void setStartInDirectory(String startInDirectory)
    {
        this.startInDirectory = startInDirectory;
        if (process != null)
        {
            process.directory(startInDirectory);
        }
    }

    void cloneEnvironment(EnvironmentVarSets environment)
    {
        process.cloneEnvironment(environment);
    }

    /**
     * Enquire whether the process is in running state.
     *
     * @return true is so, false otherwise
     */
    public boolean isRunning()
    {
        return (getProcess() == null) ? false : getProcess().isRunning();
    }

    /**
     * Initialise the runtime environment for the process.
     */
    public void initEnvironment()
    {
        if (getProcess() != null)
        {
            try
            {
                getProcess().initEnvironment();
            }
            catch (IOException ex)
            {
                LOGGER.log(Level.INFO,
                           NbBundle.getMessage(
                                   CLAZZ,
                                   "ScheduledProcess.cannotIninialize",
                                   exeDefinition.getName(),
                                   ex.toString()));
            }
        }
        else
        {
            LOGGER.log(Level.INFO,
                       NbBundle.getMessage(
                               CLAZZ,
                               "ScheduledProcess.cannotIninializeNoProcess",
                               exeDefinition.getName()));
        }
    }

    /**
     * Initialise the runtime environment for the process.
     *
     * @param absolutePath a CSV file with environment variables
     */
    public void initEnvironment(String absolutePath)
    {
        if (getProcess() != null)
        {
            try
            {
                getProcess().initEnvironment(absolutePath);
            }
            catch (IOException ex)
            {
                LOGGER.log(Level.INFO,
                           NbBundle.getMessage(
                                   CLAZZ,
                                   "ScheduledProcess.cannotIninialize",
                                   exeDefinition.getName(),
                                   ex.toString()));
            }
        }
        else
        {
            NbBundle.getMessage(
                    CLAZZ,
                    "ScheduledProcess.cannotInitializeNoProcess",
                    exeDefinition.getName());
        }
    }

    /**
     * Send this scheduled process information to the target machine where the
     * server runs. The server will take care of starting the process and return
     * a message stating the success.
     *
     * @return the returned message from the server
     */
    public ProcessMessage sendStartInstruction()
    {
        ProcessMessage msg = new ProcessMessage(
                       ProcessMessage.Type.StartProcess,
                       this);
        ProcessMessage response;
        try
        {
            ProcessClient client = new ProcessClient(
                          connectionInfo.serverIP,
                          connectionInfo.port);
            response = client.sendMessage(msg);
            if (response.isAcknowledgement())
            {
                Object ack = response.getObjects().get(0);
                ConcreteProcess.State state = (ConcreteProcess.State) ack;
                process.setState(state);
            }
            else
            {
                process.setState(ConcreteProcess.State.StartFailed);
                throw new Exception("Failed to start");
            }

        }
        catch (Exception ex)
        {
            response = ProcessMessage.makeInvalid(ex.toString());
        }

        return response;
    }

    /**
     * Start the execution of the scheduled process.
     *
     * @return the wrapped process
     */
    public ConcreteProcess start()
    {
        String[] command = null;

        switch (getWindowMode())
        {
            case XTERM:
                command = makeXtermCommand(dimensions,
                                           ColorUtils.getXtermColor(
                                                   windowBackground));
                break;
            case GUIFRAME:
                Frame parent = new Frame(toString());
                ProcessDialog.makeProcessDialog(parent, this);
                command = makeStandAloneCommand();
                break;
            case BACKGROUND:
            case BACKGROUND_FILE:
                command = makeStandAloneCommand();
                break;
        }
        process.command(command);

        return process.start();

    }

    private List<String> buildParameterList()
    {

        List<String> params = new ArrayList<>();
        for (AbstractParameter param : exeDefinition.getParameters())
        {
            if (exeDefinition.hasPositionalParameters())
            {
                params.add(param.getValue());
            }
            else
            {
                params.add(((LetterParameter) param).getParameterString());
                if (param.hasArgument())
                {
                    params.add(param.getValue());
                }
            }

        }
        return params;
    }

    private String makeCommand()
    {
        List<String> params = buildParameterList();
        String paramString = "";
        for (String param : params)
        {
            paramString += param + " ";
        }
        String timeStamp = DateUtils.file_now();
        return exeDefinition.getExecutableWithPath() + " " + paramString;
    }

    private String[] makeStandAloneCommand()
    {
        String[] reval = makeCommand().trim().split(" ");
        return reval;
    }

    private static ArrayList<Rectangle> makeDefaultGeometries()
    {
        ArrayList<Rectangle> reval = new ArrayList<>();
        reval.add(new Rectangle(0, 0, 60, 22));
        reval.add(new Rectangle(200, 0, 60, 22));
        reval.add(new Rectangle(400, 0, 60, 22));
        reval.add(new Rectangle(600, 0, 60, 22));
        reval.add(new Rectangle(0, 350, 60, 22));
        reval.add(new Rectangle(200, 350, 60, 22));
        reval.add(new Rectangle(400, 350, 60, 22));
        reval.add(new Rectangle(600, 350, 60, 22));
        reval.add(new Rectangle(0, 700, 60, 22));
        reval.add(new Rectangle(200, 700, 60, 22));
        reval.add(new Rectangle(400, 700, 60, 22));
        reval.add(new Rectangle(600, 700, 60, 22));

        return reval;
    }

    static private String makeGeometry()
    {
        return makeGeometry(defaultGeometries.get(
                (defaultGeometryNumber++) % defaultGeometries.size()));
    }

    static private String makeGeometry(Rectangle dimensions)
    {
        return dimensions.width +
               "x" +
               dimensions.height +
               "+" +
               dimensions.x +
               "+" +
               dimensions.y;
    }

    private String[] makeXtermCommand(Rectangle dimensions,
                                      Color backColor)
    {
        Color bgColor = backColor;
        Color fgColor = ColorUtils.contrastColorByComplement(bgColor);
        String foreColorString = ColorUtils.xtermColorString(fgColor);
        String[] reval = new String[]
         {
             "/usr/bin/xterm",
             "-bg", ColorUtils.xtermColorString(backColor),
             "-fg", foreColorString,
             "+cu", "-j", "-nb", "0", "-s",
             "-title", exeDefinition.toString(),
             "-sb", "-cr", foreColorString,
             "-geometry",
             (dimensions == null ? makeGeometry() : makeGeometry(dimensions)),
             "-e",
             makeCommand()
        };
        return reval;
    }

}
