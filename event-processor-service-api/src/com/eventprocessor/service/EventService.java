package com.eventprocessor.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client event service interface.
 *
 * @author Artem
 * @since 08.07.2017.
 */
public interface EventService extends Remote {
    /**
     * Retrieve events count for specific request
     *
     * @param request request
     * @return response
     * @throws RemoteException in case if some rmi exception is happened
     */
    EventsCountResponse getCount(EventsCountRequest request) throws RemoteException;
}
