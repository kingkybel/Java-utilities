/*
 * Copyright (C) 2017 Dieter J Kybelksties
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
 * @date: 2017-10-23
 * @author: Dieter J Kybelksties
 */
package com.kybelksties.protocol;

import static com.kybelksties.protocol.ProtocolException.Type.MessageReceivingActorInvalid;
import static com.kybelksties.protocol.ProtocolException.Type.MessageTypeInvalid;
import static com.kybelksties.protocol.ProtocolException.Type.NoSuchRule;
import static com.kybelksties.protocol.ProtocolException.Type.ResultStateInvalid;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class formalises static interaction protocols between different actors
 * of a system sending messages to each other.
 *
 * @author Dieter J Kybelksties
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Protocol extends DirectedSparseGraph<Actor, Message>
{

    private static final Class CLAZZ = Protocol.class;
    private static final Log LOGGER = LogFactory.getLog(CLAZZ);

    /**
     * Construction.
     */
    public Protocol()
    {

    }

    /**
     * Retrieve the resulting state when a message is received.
     *
     * @param receivingActor the actor that receives the message
     * @param messageType    the type of message received
     * @param sendingActor   optional sending actor
     * @return the state that the receiving actor would assume if it received
     *         the type of message
     * @throws Exception thrown if any of the compulsory parameters is invalid
     *                   or if there is no rule for the given parameters
     */
    public State getResultState(Actor receivingActor,
                                MessageType messageType,
                                Actor sendingActor) throws Exception
    {
        if (receivingActor == null)
        {
            throw new ProtocolException(MessageReceivingActorInvalid,
                                        receivingActor);
        }
        if (messageType == null)
        {
            throw new ProtocolException(MessageTypeInvalid, messageType);
        }
        final Message message = new Message(true,
                                            messageType,
                                            receivingActor,
                                            sendingActor);
        Pair<Actor> resultPair = this.getEndpoints(message);

        if (resultPair == null || resultPair.getSecond() == null)
        {
            throw new ProtocolException(NoSuchRule, message);
        }

        return resultPair.getSecond().getState();
    }

    /**
     * Add a rule for a a state-change of a receiving actor.
     *
     * @param receivingActor             the actor receiving the message
     * @param messageType                the type of message sent/received
     * @param stateAfterReceivingMessage the state that the receiving actor
     *                                   shall assume after receipt of the
     *                                   message
     * @param sendingActor               optional sending actor
     * @param timeout                    optional timeout. If not null then a
     *                                   reverse state transition will be added
     *                                   if the receiver is still in the new
     *                                   state after so many milliseconds
     * @param timeoutState               the state to assume if timeout elapsed
     * @throws Exception thrown when any of the compulsory parts of the rule are
     *                   invalid
     */
    public void addRule(final Actor receivingActor,
                        final MessageType messageType,
                        final State stateAfterReceivingMessage,
                        final Actor sendingActor,
                        Integer timeout,
                        final State timeoutState) throws Exception
    {
        if (receivingActor == null)
        {
            throw new ProtocolException(MessageReceivingActorInvalid,
                                        receivingActor);
        }
        if (messageType == null)
        {
            throw new ProtocolException(MessageTypeInvalid, messageType);
        }
        if (stateAfterReceivingMessage == null)
        {
            throw new ProtocolException(ResultStateInvalid);
        }

        final Actor receiverBefore = new Actor(receivingActor.getName(),
                                               receivingActor.getState());
        final Actor receiverAfter = new Actor(receivingActor.getName(),
                                              stateAfterReceivingMessage);
        addVertex(receiverBefore);
        addVertex(receiverAfter);
        Message message = new Message(messageType,
                                      sendingActor,
                                      receiverBefore);
        addEdge(message, receiverBefore, receiverAfter);
        if (timeout != null && timeout > 0 && timeoutState != null)
        {

        }
    }

    /**
     * Add a rule for a a state-change of a receiving actor.
     *
     * @param receivingActor             the actor receiving the message
     * @param messageType                the type of message sent/received
     * @param stateAfterReceivingMessage the state that the receiving actor
     *                                   shall assume after receipt of the
     *                                   message
     * @param sendingActor               optional sending actor
     * @throws Exception thrown when any of the compulsory parts of the rule are
     *                   invalid
     */
    public void addRule(final Actor receivingActor,
                        final MessageType messageType,
                        final State stateAfterReceivingMessage,
                        final Actor sendingActor) throws Exception
    {
        addRule(receivingActor,
                messageType,
                stateAfterReceivingMessage,
                sendingActor,
                -1,
                null);
    }
}
