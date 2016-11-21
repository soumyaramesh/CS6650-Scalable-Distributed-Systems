/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Remote;

/**
 *
 * @author Amitash
 */
@Remote
public interface PublisherSessionBeanRemote {

    long registerPublisher(String topic);

    void publishContent(String publisherId, String title, String message);
    
}
