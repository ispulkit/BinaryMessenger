/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.wr.dellpc.binary.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Transaction;

import java.util.List;

import javax.inject.Named;
import javax.mail.Transport;
import javax.management.Query;

@Api(
        name = "initApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.binary.dellpc.wr.com",
                ownerName = "backend.binary.dellpc.wr.com",
                packagePath = ""
        )
)
public class MyEndpoint {

        @ApiMethod(name = "checkUser")
    public User checkuser(User user){
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            User returnuser= new User();
            Key key = KeyFactory.createKey("User", user.getEmail());
            Entity e;
            try{
                e=datastoreService.get(key);
                String gcmidres = (String)e.getProperty("gcmid");
                String name = (String)e.getProperty("dispname");
                returnuser.setGcmid(gcmidres);
                returnuser.setEmail(user.getEmail());
                returnuser.setDispname(name);
            } catch (EntityNotFoundException e1) {
                e1.printStackTrace();
                returnuser.setEmail(user.getEmail());
                returnuser.setGcmid("null");
                returnuser.setDispname("null");
            }finally {
                return returnuser;
            }
    }


    @ApiMethod(name = "createUser")
    public Responser createuser(User user){
        Responser res = new Responser();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
try{
    Entity entity = new Entity("User",user.getEmail());
    entity.setProperty("gcmid",user.getGcmid());
    entity.setProperty("dispname",user.getDispname());
    datastoreService.put(entity);
    txn.commit();
    res.setResponse("success");
}finally {
    if(txn.isActive()){
        txn.rollback();
        res.setResponse("fail");
    }


}

return res;
    }


}
