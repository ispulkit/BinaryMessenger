package binary.gcmhandlers;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import binary.datahandlers.SessionManager;
import webradic.binarymessenger.BinaryChatActivity;

/**
 * Created by Pulkit on 10/7/2015.
 */
public class CCSManager implements ConnectionListener {
    static XMPPTCPConnectionConfiguration.Builder connConfig;

    public String USERNAME = "393203411312";
    public String PASSWORD = "AIzaSyAfa5YwcDFKuKjkb_oUGT5LmMlD4uAvgBs";
    final String SERVICE = "gcm.googleapis.com";
    final String HOST = "gcm.googleapis.com";
    final int PORT = 5235;
    static XMPPTCPConnection connection;
    public static final String GCM_ELEMENT_NAME = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L;
    private static SSLContext sslctx;
    public static String to_gcm_id, to_msg_id, to_message;

    public CCSManager() {

        connConfig = XMPPTCPConnectionConfiguration.builder();
        connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        connConfig.setUsernameAndPassword(USERNAME, PASSWORD);
        connConfig.setServiceName(SERVICE);
        connConfig.setHost(HOST);
        connConfig.setCompressionEnabled(false);
        connConfig.setPort(PORT);
        connConfig.setSendPresence(true);
        connConfig.setDebuggerEnabled(true);
        //setCompressionEnabled(false).build();
        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        //connection.login(USERNAME, PASSWORD);
        //(HOST, PORT, SERVICE);
        try {
            sslctx = SSLContext.getInstance("Default");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Error in sslctx part", e.toString());
        }
        connConfig.setSocketFactory(sslctx.getSocketFactory());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                connection = new XMPPTCPConnection(connConfig.build());
                XMPPTCPConnection.setUseStreamManagementDefault(true);
                connection.addConnectionListener(CCSManager.this);
                try {
                    connection.connect();
                    connection.login();
                    Log.e("CCSManager", "Connected to " + connection.getHost());


                } catch (XMPPException ex) {
                    Log.e("CCSManager", " Failed to connect to " + connection.getHost());
                    Log.e("CCSManager", ex.toString());
                } catch (SmackException ex) {
                    Log.e("CCSManager", "Failed to connect to " + connection.getHost());
                    Log.e("CCSmanager", ex.toString());
                } catch (IOException ex) {
                    Log.e("CCSManager", " Failed to connect to " + connection.getHost());
                    Log.e("CCSManager", ex.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                ProviderManager.addExtensionProvider(bin_ext_provider.GCM_ELEMENT_NAME, bin_ext_provider.GCM_NAMESPACE, new bin_ext_provider.bin_embedded_ext_provider());
                connection.addPacketListener(new StanzaListener() {


                    @Override
                    public void processPacket(Stanza arg0) throws SmackException.NotConnectedException {
                        // TODO Auto-generated method stub

                        org.jivesoftware.smack.packet.Message msg = (org.jivesoftware.smack.packet.Message) arg0;
                        bin_ext_provider agya = (bin_ext_provider) msg.getExtension(GCM_NAMESPACE);
                        Log.e("Extension recieved ! ", agya.toXML().toString());
                    }
                }, null);
            }
        }.execute();
    }

    public static class bin_ext_provider implements ExtensionElement {

        public static final String GCM_ELEMENT_NAME = "gcm";
        public static final String GCM_NAMESPACE = "google:mobile:data";

        @Override
        public String getNamespace() {
            return GCM_NAMESPACE;
        }

        @Override
        public String getElementName() {
            return GCM_ELEMENT_NAME;
        }

        @Override
        public CharSequence toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<" + getElementName() + " xmlns=\"" + getNamespace() + "\">");
            buf.append("{");
            buf.append("\"" + "to" + "\"" + ":" + "\"" + to_gcm_id + "\"");

            buf.append(",");
            buf.append("\"" + "message_id" + "\"" + ":" + "\"" + to_msg_id + "\"" + ",");
            buf.append("\"" + "data" + "\"" + ":" + "{" + "\"" + "message" + "\"" + ":" + "\"" + to_message + "\"" + ",");
            buf.append("\""+"former\":"+"\""+ SessionManager.login_email+"\""+",");
            buf.append("}" + "}");
            buf.append("</" + getElementName() + ">");
            return buf.toString();

        }


        public static class bin_embedded_ext_provider extends EmbeddedExtensionProvider<ExtensionElement> {


            @Override
            protected ExtensionElement createReturnExtension(String cuurentElement,
                                                             String currentNamespace, Map<String, String> attributeMap,
                                                             List<? extends ExtensionElement> content) {
                // TODO Auto-generated method stub
                Log.e("Inside embeddedext-prov", "hmm");
                return new bin_ext_provider();
            }


        }
    }

    public void sendmessage(String msg, String msgid) {

        to_msg_id = msgid;
        to_message = msg;
        bin_ext_provider b1 = new bin_ext_provider();
        Message m1 = new Message();
        m1.addExtension(b1);
        try {
            connection.sendPacket(m1);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            Log.e("Error in sendingMsg", e.toString());

        }
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {

    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int i) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    public void bye() {

            connection.disconnect();
    }
}
