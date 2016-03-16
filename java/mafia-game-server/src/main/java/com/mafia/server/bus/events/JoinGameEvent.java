/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mafia.server.bus.events;

import com.mafia.server.model.comm.client.JoinGame;

/**
 *
 * @author Just1689
 */
public class JoinGameEvent implements Runnable, Event {

    private JoinGame data;
    private String createdBy;

    @Override
    public void run() {
        //impl
        System.out.println(data.toString());
    }

    @Override
    public void setData(Object obj, String sessionId) {
        this.data = (JoinGame) obj;
        this.createdBy = sessionId;

    }

}