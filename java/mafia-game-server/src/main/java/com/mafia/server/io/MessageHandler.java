/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mafia.server.io;

import com.mafia.server.bus.events.MessageboxEvents;
import com.mafia.server.bus.events.PlayerEvents;
import com.mafia.server.model.state.Game;
import com.mafia.server.model.state.Player;
import com.mafia.server.model.state.Repository;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author Just1689
 */
public class MessageHandler {

    public static void handleMessageFromClient(Session session, String message) {
        MessageRouter.handleIncoming(message, session.getId());

    }

    public static void handleDisconnect(Session session) {
        Game game = null;

        Player player = Repository.getPlayerBySession(session);
        if (player != null) {
            game = player.getGame();
        }

        Repository.removeSession(session);
        PlayerEvents.playerQuits(session);

        if (player != null) {
            MessageboxEvents.showMessageboxTimed(game, player.getName(), "has left");
        }

    }

    public static synchronized void sendMessage(Game game, String message) {
        Enumeration<Player> elements = game.getPlayers().elements();
        while (elements.hasMoreElements()) {
            Player player = elements.nextElement();
            sendMessage(player, message);
        }

    }

    public static void sendMessage(Player player, String message) {
        System.out.println("Sending: " + message);
        Session session = Repository.getSessionByPlayer(player);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void handleConnect(Session session) {
        Repository.addSession(session);
    }

    public static void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
