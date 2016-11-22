/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Remote;


@Remote
public interface SubscriberStorageBeanRemote {
    long registerSubscriber(String topic);
    
}
